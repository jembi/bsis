package org.jembi.bsis.service;

import org.apache.log4j.Logger;
import org.jembi.bsis.backingform.BloodTypingResolutionBackingForm;
import org.jembi.bsis.backingform.BloodTypingResolutionsBackingForm;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.NoResultException;

@Transactional
@Service
public class DonationCRUDService {

  private static final Logger LOGGER = Logger.getLogger(DonationCRUDService.class);

  @Autowired
  private DonationRepository donationRepository;
  @Autowired
  private DonationConstraintChecker donationConstraintChecker;
  @Autowired
  private DonorRepository donorRepository;
  @Autowired
  private DonationBatchRepository donationBatchRepository;
  @Autowired
  private ComponentCRUDService componentCRUDService;
  @Autowired
  private DonorConstraintChecker donorConstraintChecker;
  @Autowired
  private DonorService donorService;
  @Autowired
  private PostDonationCounsellingCRUDService postDonationCounsellingCRUDService;
  @Autowired
  private TestBatchStatusChangeService testBatchStatusChangeService;
  @Autowired
  private BloodTestsService bloodTestsService;
  @Autowired
  private CheckCharacterService checkCharacterService;
  @Autowired
  private DateGeneratorService dateGeneratorService;

  public void deleteDonation(UUID donationId) throws IllegalStateException, NoResultException {

    if (!donationConstraintChecker.canDeleteDonation(donationId)) {
      throw new IllegalStateException("Cannot delete donation with constraints");
    }

    // Soft delete donation
    Donation donation = donationRepository.findDonationById(donationId);
    donation.setIsDeleted(true);
    donationRepository.update(donation);

    Date donationDate = donation.getDonationDate();
    Donor donor = donation.getDonor();

    // If this was the donor's first donation
    if (donationDate.equals(donor.getDateOfFirstDonation())) {
      Date dateOfFirstDonation = donationRepository.findDateOfFirstDonationForDonor(donor.getId());
      donor.setDateOfFirstDonation(dateOfFirstDonation);
      donorRepository.updateDonor(donor);
    }

    // If this was the donor's last donation
    if (donationDate.equals(donor.getDateOfLastDonation())) {
      Date dateOfLastDonation = donationRepository.findDateOfLastDonationForDonor(donor.getId());
      donor.setDateOfLastDonation(dateOfLastDonation);
      donorRepository.updateDonor(donor);
    }

    // Soft delete related components
    for (Component component: donation.getComponents()) {
      componentCRUDService.deleteComponent(component.getId());
    }

    donorService.setDonorDueToDonate(donor);
  }

  public Donation createDonation(Donation donation) {

    donation.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    donation.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    donation.setTTIStatus(TTIStatus.NOT_DONE);
    donation.setIsDeleted(false);
    donation.setFlagCharacters(checkCharacterService.calculateFlagCharacters(donation.getDonationIdentificationNumber()));

    boolean discardComponents = false;

    if (donation.getPackType().getCountAsDonation() &&
        !donorConstraintChecker.isDonorEligibleToDonate(donation.getDonor().getId())) {

      DonationBatch donationBatch = donationBatchRepository.findDonationBatchByBatchNumber(
          donation.getDonationBatchNumber());

      if (!donationBatch.isBackEntry()) {
        throw new IllegalArgumentException("Do not bleed donor");
      }

      // The donation batch is being back entered so allow the donation to be created but discard the components
      discardComponents = true;
      donation.setIneligibleDonor(true);
    }

    Component component = componentCRUDService.createInitialComponent(donation);
    donation.addComponent(component);
    donationRepository.save(donation);

    if (discardComponents) {
      componentCRUDService.markComponentsBelongingToDonationAsUnsafe(donation);
      //Also flag for counselling
      postDonationCounsellingCRUDService.createPostDonationCounsellingForDonation(donation);
    }

    // update donor
    updateDonorFields(donation);
    return donation;
  }

  /**
   * Updates the specified Donation, performing constraint checks where necessary (for changing bleed times and/or pack type).
   *
   * If the bleed times are updated, then the initial Component's createdOn date will be modified.
   *
   * If the pack type is updated, then initial components can be deleted, modified or created depending on whether the previous
   * and new pack types produce test samples or count as donations. The donation will be released if the new pack type counts
   * as a donation and the test batch it belongs to has been released/closed.
   *
   * @param updatedDonation Donation, must not be a managed entity otherwise checks will not be run correctly
   * @return Donation that has been updated and persisted
   */
  public Donation updateDonation(Donation updatedDonation) {
    Donation existingDonation = donationRepository.findDonationById(updatedDonation.getId());

    // Check if pack type has been updated
    boolean packTypeUpdated = !Objects.equals(existingDonation.getPackType(), updatedDonation.getPackType());

    // Check if bleed times have been updated
    boolean bleedTimesUpdated = existingDonation.getBleedStartTime().getTime() != updatedDonation.getBleedStartTime().getTime()
            || existingDonation.getBleedEndTime().getTime() != updatedDonation.getBleedEndTime().getTime();

    if (bleedTimesUpdated && !donationConstraintChecker.canEditBleedTimes(updatedDonation.getId())) {
      throw new IllegalArgumentException("Cannot edit bleed times");
    }

    // See if donation should be released (in the case of pack type being updated and a new initial component created)
    boolean releaseDonation = false;

    if (packTypeUpdated) {

      // Check if the packType can be updated for this donation
      if (!donationConstraintChecker.canEditPackType(existingDonation)) {
        throw new IllegalArgumentException("Cannot edit pack type");
      }

      // Check if the packType can be edited to newPackType
      PackType newPackType = updatedDonation.getPackType();
      if (!donationConstraintChecker.canEditToNewPackType(existingDonation, newPackType)) {
        throw new IllegalArgumentException("Cannot edit to this new pack type");
      }

      // Check that if the donor is deferred, the packType can't be updated to one that produces
      // components (doesn't apply to back entry)
      if (newPackType.getCountAsDonation() &&
          donorConstraintChecker.isDonorDeferred(existingDonation.getDonor().getId())) {
        DonationBatch donationBatch = existingDonation.getDonationBatch();
        if (!donationBatch.isBackEntry()) {
          throw new IllegalArgumentException("Cannot set pack type that produces components");
        }
      }

      // Set new pack type
      existingDonation.setPackType(newPackType);

      // If the new packType does not count as donation, delete initial component
      if (!newPackType.getCountAsDonation() && !existingDonation.getComponents().isEmpty()) {
          existingDonation.getComponents().get(0).setIsDeleted(true);
      }

      // If the new packType count as donation, update initial component
      if (newPackType.getCountAsDonation() && !existingDonation.getComponents().isEmpty()) {
        componentCRUDService.updateComponentWithNewPackType(existingDonation.getComponents().get(0), newPackType);
      }

      // If the new PackType count as donation and there is no initial component then create it
      if (newPackType.getCountAsDonation() && existingDonation.getComponents().isEmpty()) {
        Component component = componentCRUDService.createInitialComponent(existingDonation);
        existingDonation.getComponents().add(component);
        // ensure that the Donation is released, so that the new initial component is made available (or not)
        releaseDonation = existingDonation.getTestBatch() != null
                && TestBatchStatus.hasBeenReleased(existingDonation.getTestBatch().getStatus());
      }

      // If the new pack type doesn't produce test samples, delete test outcomes and clear statuses
      if (!newPackType.getTestSampleProduced()) {
        clearTestOutcomes(existingDonation);
      }
    }

    existingDonation.setDonorPulse(updatedDonation.getDonorPulse());
    existingDonation.setHaemoglobinCount(updatedDonation.getHaemoglobinCount());
    existingDonation.setHaemoglobinLevel(updatedDonation.getHaemoglobinLevel());
    existingDonation.setBloodPressureSystolic(updatedDonation.getBloodPressureSystolic());
    existingDonation.setBloodPressureDiastolic(updatedDonation.getBloodPressureDiastolic());
    existingDonation.setDonorWeight(updatedDonation.getDonorWeight());
    existingDonation.setNotes(updatedDonation.getNotes());
    existingDonation.setBleedStartTime(updatedDonation.getBleedStartTime());
    existingDonation.setBleedEndTime(updatedDonation.getBleedEndTime());
    existingDonation.setAdverseEvent(updatedDonation.getAdverseEvent());

    if (bleedTimesUpdated) {
      Component initialComponent = existingDonation.getInitialComponent();
      if (initialComponent != null) {
        Date newCreatedOn = dateGeneratorService.generateDateTime(initialComponent.getCreatedOn(), existingDonation.getBleedStartTime());
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Setting createdOn for Component " + initialComponent.getId() + " to " + newCreatedOn);
        }
        initialComponent.setCreatedOn(newCreatedOn);
      }
    }

    Donation donation = donationRepository.update(existingDonation);

    if (packTypeUpdated) {
      donorService.setDonorDueToDonate(existingDonation.getDonor());
    }

    if (releaseDonation) {
      testBatchStatusChangeService.handleRelease(donation);
    }

    return donation;
  }

  public void clearTestOutcomes(Donation donation) {
    bloodTestsService.setTestOutcomesAsDeleted(donation);
    donation.resetTestStatuses();
  }

  public void updateDonationsBloodTypingResolutions(BloodTypingResolutionsBackingForm backingForm) {
    for (BloodTypingResolutionBackingForm form : backingForm.getBloodTypingResolutions()) {
      updateDonationBloodTypingResolution(form);
    }
  }

  public void updateDonationBloodTypingResolution(BloodTypingResolutionBackingForm form) {

    Donation donation = donationRepository.findDonationById(form.getDonationId());
    if (form.getStatus().equals(BloodTypingMatchStatus.RESOLVED)) {
      donation.setBloodAbo(form.getBloodAbo());
      donation.setBloodRh(form.getBloodRh());
      donation.setBloodTypingMatchStatus(BloodTypingMatchStatus.RESOLVED);
    } else {
      donation.setBloodTypingMatchStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED);
    }

    donation = donationRepository.update(donation);

    if (donation.getTestBatch().getStatus() == TestBatchStatus.RELEASED) {
      testBatchStatusChangeService.handleRelease(donation);
    }
  }

  private Donor updateDonorFields(Donation donation) {
    Donor donor = donation.getDonor();

    // set date of first donation
    if (donation.getDonor().getDateOfFirstDonation() == null) {
      donor.setDateOfFirstDonation(donation.getDonationDate());
    }
    // set dueToDonate
    PackType packType = donation.getPackType();
    int periodBetweenDays = packType.getPeriodBetweenDonations();
    Calendar dueToDonateDate = Calendar.getInstance();
    dueToDonateDate.setTime(donation.getDonationDate());
    dueToDonateDate.add(Calendar.DAY_OF_YEAR, periodBetweenDays);

    if (donor.getDueToDonate() == null || dueToDonateDate.getTime().after(donor.getDueToDonate())) {
      donor.setDueToDonate(dueToDonateDate.getTime());
    }

    // set dateOfLastDonation
    Date dateOfLastDonation = donor.getDateOfLastDonation();
    if (dateOfLastDonation == null || donation.getDonationDate().after(dateOfLastDonation)) {
      donor.setDateOfLastDonation(donation.getDonationDate());
    }

    donorRepository.saveDonor(donor);
    return donor;
  }
}
