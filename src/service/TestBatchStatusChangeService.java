package service;

import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.donordeferral.DeferralReasonType;
import model.testbatch.TestBatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.DonationRepository;
import viewmodel.BloodTestingRuleResult;

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

    public void handleRelease(TestBatch testBatch) {
        
        LOGGER.info("Handling release for test batch: " + testBatch);
        
        if (testBatch.getDonationBatches() == null) {
            // No donation batches so nothing to do
            return;
        }

        for (DonationBatch donationBatch : testBatch.getDonationBatches()) {

            for (Donation donation : donationBatch.getDonations()) {

                handleRelease(donation);
            }
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
      
        // Execute tests and update the donation with the results
        BloodTestingRuleResult bloodTestingRuleResult = bloodTestsService.executeTests(donation);
        bloodTestsService.updateDonationWithTestResults(donation, bloodTestingRuleResult);
        donation = donationRepository.updateDonation(donation);

        if (donation.getTTIStatus() == TTIStatus.TTI_UNSAFE) {
            LOGGER.info("Handling donation with unsafe TTI status: " + donation);
            postDonationCounsellingCRUDService.createPostDonationCounsellingForDonation(donation);
            componentCRUDService.markComponentsBelongingToDonorAsUnsafe(donation.getDonor());

            if (donorDeferralStatusCalculator.shouldDonorBeDeferred(donation.getBloodTestResults())) {
                donorDeferralCRUDService.createDeferralForDonorWithDeferralReasonType(donation.getDonor(),
                        DeferralReasonType.AUTOMATED_TTI_UNSAFE);
            }
        } else if (componentStatusCalculator.shouldComponentsBeDiscarded(donation.getBloodTestResults())) {
            LOGGER.info("Handling donation with components flagged for discard: " + donation);
            componentCRUDService.markComponentsBelongingToDonationAsUnsafe(donation);
        } else {
            componentCRUDService.updateComponentStatusesForDonation(donation);
        }

    }

}
