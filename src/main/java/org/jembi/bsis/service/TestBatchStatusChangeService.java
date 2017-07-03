package org.jembi.bsis.service;

import org.apache.log4j.Logger;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DeferralReasonType;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TestBatchStatusChangeService {

  private static final Logger LOGGER = Logger.getLogger(TestBatchStatusChangeService.class);

  @Autowired
  private PostDonationCounsellingCRUDService postDonationCounsellingCRUDService;
  @Autowired
  private DonorDeferralCRUDService donorDeferralCRUDService;
  @Autowired
  private ComponentCRUDService componentCRUDService;
  @Autowired
  private DonorDeferralStatusCalculator donorDeferralStatusCalculator;
  @Autowired
  private ComponentStatusCalculator componentStatusCalculator;
  @Autowired
  private DonationConstraintChecker donationConstraintChecker;
  @Autowired
  private BloodTestsService bloodTestsService;
  @Autowired
  private DonationRepository donationRepository;
  @Autowired
  private DonorRepository donorRepository;

  public void handleRelease(TestBatch testBatch) {

    LOGGER.info("Handling release for test batch: " + testBatch);

    if (testBatch.getDonations() == null) {
      // No donation so nothing to do
      return;
    }

    for (Donation donation : testBatch.getDonations()) {
      handleRelease(donation);
    }
  }

  public void handleRelease(Donation donation) {
    if (!donation.getPackType().getTestSampleProduced()) {
      LOGGER.debug("Skipping donation without test sample: " + donation);
      return;
    }

    if (donationConstraintChecker.donationHasDiscrepancies(donation)) {
      LOGGER.info("Skipping donation with discrepancies: " + donation);
      return;
    }

    Donor donor = donation.getDonor();
    if (BloodTypingMatchStatus.isBloodGroupConfirmed(donation.getBloodTypingMatchStatus())) {
      // Update the donor's Abo/Rh values to match the donation
      donor.setBloodAbo(donation.getBloodAbo());
      donor.setBloodRh(donation.getBloodRh());
      LOGGER.debug("Updating blood type of donor: " + donor + " to " + donation.getBloodAbo() + donation.getBloodRh());
      donorRepository.saveDonor(donor);
    }

    // Mark this donation as released
    donation.setReleased(true);

    // Execute tests and update the donation with the results
    BloodTestingRuleResult bloodTestingRuleResult = bloodTestsService.executeTests(donation);
    bloodTestsService.updateDonationWithTestResults(donation, bloodTestingRuleResult);
    donation = donationRepository.update(donation);

    // Handle the situation where the Donors and/or Donation are unsafe
    if (donation.getTTIStatus() == TTIStatus.UNSAFE) {
      LOGGER.info("Handling donation with unsafe TTI status: " + donation);
      componentCRUDService.markComponentsBelongingToDonorAsUnsafe(donation.getDonor());

      if (donorDeferralStatusCalculator.shouldDonorBeDeferred(donation.getBloodTestResults())) {
        LOGGER.info("Deferring donor and referring donor for counselling: " + donation.getDonorNumber());
        postDonationCounsellingCRUDService.createPostDonationCounsellingForDonation(donation);
        donorDeferralCRUDService.createDeferralForDonorWithVenueAndDeferralReasonType(donor, donation.getVenue(),
            DeferralReasonType.AUTOMATED_TTI_UNSAFE);
      }
    } else if (componentStatusCalculator.shouldComponentsBeDiscardedForTestResults(donation.getBloodTestResults())) {
      LOGGER.info("Handling donation with components flagged for discard: " + donation);
      componentCRUDService.markComponentsBelongingToDonationAsUnsafe(donation);

    } else if (donation.getTTIStatus() == TTIStatus.INDETERMINATE 
        || donation.getBloodTypingMatchStatus() == BloodTypingMatchStatus.INDETERMINATE) {
      LOGGER.info("Handling donation with INDETERMINATE ttiStatus or bloodTypingMatchStatus: " + donation);
      componentCRUDService.markComponentsBelongingToDonationAsUnsafe(donation);

    } else if (donation.getBloodTypingMatchStatus().equals(BloodTypingMatchStatus.NO_TYPE_DETERMINED)) {
      LOGGER.info("Handling donation with NO_TYPE_DETERMINED bloodTypingMatchStatus: " + donation);
      componentCRUDService.markComponentsBelongingToDonationAsUnsafe(donation);
    }
    
    // Handle the situation where only Components containing plasma should be discarded
    if (componentStatusCalculator.shouldComponentsBeDiscardedForTestResultsIfContainsPlasma(donation.getBloodTestResults())) {
      LOGGER.info("Handling donation with components that contains plasma: " + donation);
      componentCRUDService.markComponentsBelongingToDonationAsUnsafeIfContainsPlasma(donation);
    }

    // Re-evaluate all donation's components statuses and update where necessary
    componentCRUDService.updateComponentStatusesForDonation(donation);

  }

}
