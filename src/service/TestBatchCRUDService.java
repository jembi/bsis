package service;

import java.util.HashSet;
import java.util.Set;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.donor.Donor;
import model.donordeferral.DeferralReasonType;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.TestBatchRepository;

@Service
@Transactional
public class TestBatchCRUDService {

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

    public void setTestBatchRepository(TestBatchRepository testBatchRepository) {
        this.testBatchRepository = testBatchRepository;
    }

    public void setPostDonationCounsellingCRUDService(PostDonationCounsellingCRUDService postDonationCounsellingCRUDService) {
        this.postDonationCounsellingCRUDService = postDonationCounsellingCRUDService;
    }

    public TestBatch updateTestBatchStatus(Long testBatchId, TestBatchStatus newStatus) {
        
        TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);

        // If the test batch status is changing to closed and it has donation batches
        if (newStatus == TestBatchStatus.CLOSED &&
                testBatch.getStatus() != TestBatchStatus.CLOSED &&
                testBatch.getDonationBatches() != null) {
            
            Set<Donor> donorsToDefer = new HashSet<>();

            for (DonationBatch donationBatch : testBatch.getDonationBatches()) {

                for (Donation donation : donationBatch.getDonations()) {

                    if (donation.getTTIStatus() == TTIStatus.TTI_UNSAFE) {
                        
                        // This donor has a positive test result so flag them for counselling
                        postDonationCounsellingCRUDService.createPostDonationCounsellingForDonation(donation);

                        // Determine if this donor should be deferred or not based on their blood test results
                        if (donorDeferralStatusCalculator.shouldDonorBeDeferred(donation.getBloodTestResults())) {
                            donorsToDefer.add(donation.getDonor());
                        }
                    }
                }
            }
            
            // Create deferrals for donors who have unsafe donations
            for (Donor donorToDefer : donorsToDefer) {
                donorDeferralCRUDService.createDeferralForDonorWithDeferralReasonType(donorToDefer,
                        DeferralReasonType.AUTOMATED_TTI_UNSAFE);

                // Make sure that components belonging to this donor are marked as unsafe
                componentCRUDService.markComponentsBelongingToDonorAsUnsafe(donorToDefer);
            }
        }

        // Set the new status
        testBatch.setStatus(newStatus);

        return testBatchRepository.updateTestBatch(testBatch);
    }

}
