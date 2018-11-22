package org.jembi.bsis.service;

import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.BloodTestResultRepository;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class DonationConstraintChecker {

  @Autowired
  private DonationRepository donationRepository;
  @Autowired
  private BloodTestResultRepository bloodTestResultRepository;
  @Autowired
  private ComponentRepository componentRepository;
  @Autowired
  private BloodTestsService bloodTestsService;

  public boolean canDeleteDonation(UUID donationId) throws NoResultException {

    Donation donation = donationRepository.findDonationById(donationId);

    // Check for comments
    if (donation.getNotes() != null && !donation.getNotes().isEmpty()) {
      return false;
    }

    // Check for adverse events
    if (donation.getAdverseEvent() != null) {
      return false;
    }

    // Check for recorded test results
    if (bloodTestResultRepository.countBloodTestResultsForDonation(donationId) > 0) {
      return false;
    }

    // Check for processed components
    if (componentRepository.countChangedComponentsForDonation(donationId) > 0) {
      return false;
    }

    return true;
  }

  public boolean canEditBleedTimes(UUID donationId) {

    // Check for recorded test results
    if (bloodTestResultRepository.countBloodTestResultsForDonation(donationId) > 0) {
      return false;
    }

    // Check for processed components
    if (componentRepository.countChangedComponentsForDonation(donationId) > 0) {
      return false;
    }

    return true;
  }

  /**
   * Test outcome discrepancies: tti tests that require confirmatory testing, blood group serology
   * test outcomes that are ambiguous, or require a confirmatory outcome.
   */
  public boolean donationHasDiscrepancies(Donation donation) {

    if (!donation.getPackType().getTestSampleProduced()) {
      return false;
    }

    return donationHasDiscrepancies(donation, bloodTestsService.executeTests(donation));
  }

  /**
   * @return true if the Donation has any pending TTI tests or requires confirmation of blood typing
   *         tests, false otherwise
   */
  public boolean donationHasDiscrepancies(Donation donation, BloodTestingRuleResult bloodTestingRuleResult) {
    if (bloodTestingRuleResult.getPendingRepeatAndConfirmatoryTtiTestsIds() != null
        && bloodTestingRuleResult.getPendingRepeatAndConfirmatoryTtiTestsIds().size() > 0) {

      // Donation has pending TTI tests
      return true;
    }

    if (!BloodTypingMatchStatus.isEndState(donation.getBloodTypingMatchStatus())
        || donation.getBloodTypingStatus() != BloodTypingStatus.COMPLETE) {
      return true;
    }

    return false;
  }

  /**
   * @return true if the Donation has no discrepancies and is in a TestBatch that is either closed
   *         or released
   */
  public boolean donationIsReleased(TestBatch testBatch, Donation donation,
      BloodTestingRuleResult bloodTestingRuleResult) {
    boolean donationReleased = testBatch != null && testBatch.getStatus() != TestBatchStatus.OPEN
        && !donationHasDiscrepancies(donation, bloodTestingRuleResult);
    return donationReleased;
  }

  public boolean donationHasOutstandingOutcomes(Donation donation, BloodTestingRuleResult bloodTestingRuleResult) {

    if (!donation.getPackType().getTestSampleProduced()) {
      return false;
    }

    // {@link BloodTestsService#updateDonationWithTestResults} has side effects so create a copy of
    // the donation
    Donation copy = new Donation(donation);
    bloodTestsService.updateDonationWithTestResults(copy, bloodTestingRuleResult);

    return copy.getTTIStatus() == TTIStatus.NOT_DONE || copy.getBloodTypingStatus() == BloodTypingStatus.NOT_DONE
        || copy.getBloodTypingMatchStatus() == BloodTypingMatchStatus.NOT_DONE;
  }

  /**
   * Determines if there are any blood test results recorded for the specified donation.
   * 
   * @param donation Donation to check
   * @return boolean true if the donation has saved test results, false otherwise
   */
  public boolean donationHasSavedTestResults(Donation donation) {
    int numberOfTestResults = bloodTestResultRepository.countBloodTestResultsForDonation(donation.getId());
    return numberOfTestResults > 0;
  }

  public boolean canEditPackType(Donation donation) {

    for (Component component : donation.getComponents()) {

      if (component.getIsDeleted()) {
        // Ignore deleted components
        continue;
      }

      // Check if the component has been processed, discarded or labelled
      if (component.getStatus() == ComponentStatus.PROCESSED || component.getStatus() == ComponentStatus.DISCARDED
          || component.getInventoryStatus() == InventoryStatus.IN_STOCK) {
        return false;
      }
    }

    return true;
  }

  public boolean canEditToNewPackType(Donation existingDonation, PackType newPackType) {

    if(existingDonation.isReleased() || isTestBatchNotOpen(existingDonation)) {
      //check if pack type change is from one that produces test samples to one that doesn't or vice versa
      if (existingDonation.getPackType().getTestSampleProduced() != newPackType.getTestSampleProduced()) {
        return false;
      }
    }
    return true;
  }

  private boolean isTestBatchNotOpen(Donation donation) {
    return donation.getTestBatch() != null && 
        donation.getTestBatch().getStatus() != null &&
        !donation.getTestBatch().getStatus().equals(TestBatchStatus.OPEN);
  }

}
