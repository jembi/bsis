package service;

import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.donordeferral.DeferralReasonType;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.TestBatchRepository;

@Service
@Transactional
public class TestBatchCRUDService {

    private static final Logger LOGGER = Logger.getLogger(TestBatchCRUDService.class);

    @Autowired
    private TestBatchRepository testBatchRepository;
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
    private TestBatchConstraintChecker testBatchConstraintChecker;

    public void setTestBatchRepository(TestBatchRepository testBatchRepository) {
        this.testBatchRepository = testBatchRepository;
    }

    public void setPostDonationCounsellingCRUDService(PostDonationCounsellingCRUDService postDonationCounsellingCRUDService) {
        this.postDonationCounsellingCRUDService = postDonationCounsellingCRUDService;
    }

    public TestBatch updateTestBatchStatus(Long testBatchId, TestBatchStatus newStatus) {

        LOGGER.info("Updating status of test batch " + testBatchId + " to " + newStatus);
        
        TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
        
        if (newStatus == testBatch.getStatus()) {
            // The status is not being changed so return early
            return testBatch;
        }

        if (newStatus == TestBatchStatus.CLOSED && testBatch.getStatus() != TestBatchStatus.RELEASED) {
            throw new IllegalStateException("Only released test batches can be closed");
        }

        // If the test batch status is changing to released and it has donation batches
        if (newStatus == TestBatchStatus.RELEASED && testBatch.getDonationBatches() != null) {
            
            if (!testBatchConstraintChecker.canReleaseTestBatch(testBatch)) {
                throw new IllegalStateException("Test batch cannot be released");
            }
            
            for (DonationBatch donationBatch : testBatch.getDonationBatches()) {

                for (Donation donation : donationBatch.getDonations()) {
                    
                    if (donation.getTTIStatus() == TTIStatus.TTI_UNSAFE) {
                        
                        LOGGER.info("Handling donation with unsafe TTI status: " + donation);
                        
                        postDonationCounsellingCRUDService.createPostDonationCounsellingForDonation(donation);
                        
                        // Flag all components for this donor as unsafe
                        componentCRUDService.markComponentsBelongingToDonorAsUnsafe(donation.getDonor());

                        if (donorDeferralStatusCalculator.shouldDonorBeDeferred(donation.getBloodTestResults())) {

                            donorDeferralCRUDService.createDeferralForDonorWithDeferralReasonType(donation.getDonor(),
                                    DeferralReasonType.AUTOMATED_TTI_UNSAFE);
                        }
                    } else if (componentStatusCalculator.shouldComponentsBeDiscarded(donation.getBloodTestResults())) {

                        LOGGER.info("Handling donation with components flagged for discard: " + donation);

                        // Flag only components from this donation as unsafe
                        componentCRUDService.markComponentsBelongingToDonationAsUnsafe(donation);
                    }
                }
            }
        }

        // Set the new status
        testBatch.setStatus(newStatus);

        return testBatchRepository.updateTestBatch(testBatch);
    }

}
