package service;

import static helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.TestBatchBuilder.aTestBatch;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donor.Donor;
import model.donordeferral.DeferralReasonType;
import model.testbatch.TestBatch;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import suites.UnitTestSuite;

public class TestBatchStatusChangeServiceTests extends UnitTestSuite {
    
    @InjectMocks
    private TestBatchStatusChangeService testBatchStatusChangeService;
    @Mock
    private PostDonationCounsellingCRUDService postDonationCounsellingCRUDService;
    @Mock
    private DonorDeferralCRUDService donorDeferralCRUDService;
    @Mock
    private ComponentCRUDService componentCRUDService;
    @Mock
    private DonorDeferralStatusCalculator donorDeferralStatusCalculator;
    @Mock
    private ComponentStatusCalculator componentStatusCalculator;
    @Mock
    private DonationConstraintChecker donationConstraintChecker;
    
    @Test
    public void testHandleReleaseWithNoDonationBatches_shouldDoNothing() {
        
        TestBatch testBatch = aTestBatch().withDonationBatches(null).build();
        
        testBatchStatusChangeService.handleRelease(testBatch);
        
        verifyZeroInteractions(postDonationCounsellingCRUDService, donorDeferralCRUDService, componentCRUDService);
    }
    
    @Test
    public void testHandleReleaseWithADonationWithDiscrepancies_shouldDoNothing() {
        
        Donation donationWithDiscrepancies = aDonation().build();
        TestBatch testBatch = aTestBatch()
                .withDonationBatch(aDonationBatch().withDonation(donationWithDiscrepancies).build())
                .build();
        
        when(donationConstraintChecker.donationHasDiscrepancies(donationWithDiscrepancies)).thenReturn(true);
        
        testBatchStatusChangeService.handleRelease(testBatch);
        
        verifyZeroInteractions(postDonationCounsellingCRUDService, donorDeferralCRUDService, componentCRUDService);
    }
    
    @Test
    public void testHandleReleaseWithoutComponentsToBeDiscarded_shouldDoNothing() {
        
        List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
        Donation donationWithoutDiscrepancies = aDonation().withBloodTestResults(bloodTestResults).build();
        TestBatch testBatch = aTestBatch()
                .withDonationBatch(aDonationBatch().withDonation(donationWithoutDiscrepancies).build())
                .build();
        
        when(donationConstraintChecker.donationHasDiscrepancies(donationWithoutDiscrepancies)).thenReturn(false);
        when(componentStatusCalculator.shouldComponentsBeDiscarded(bloodTestResults)).thenReturn(false);
        
        testBatchStatusChangeService.handleRelease(testBatch);
        
        verifyZeroInteractions(postDonationCounsellingCRUDService, donorDeferralCRUDService, componentCRUDService);
    }
    
    @Test
    public void testHandleReleaseWithComponentsToBeDiscarded_shouldMarkComponentsAsUnsafe() {
        
        List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
        Donation donationWithoutDiscrepancies = aDonation().withBloodTestResults(bloodTestResults).build();
        TestBatch testBatch = aTestBatch()
                .withDonationBatch(aDonationBatch().withDonation(donationWithoutDiscrepancies).build())
                .build();
        
        when(donationConstraintChecker.donationHasDiscrepancies(donationWithoutDiscrepancies)).thenReturn(false);
        when(componentStatusCalculator.shouldComponentsBeDiscarded(bloodTestResults)).thenReturn(true);
        
        testBatchStatusChangeService.handleRelease(testBatch);
        
        verify(componentCRUDService).markComponentsBelongingToDonationAsUnsafe(donationWithoutDiscrepancies);
        verifyZeroInteractions(postDonationCounsellingCRUDService, donorDeferralCRUDService);
    }
    
    @Test
    public void testHandleReleaseWithUnsafeDonation_shouldCreateCounsellingAndDiscardComponents() {
        
        List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
        Donor donor = aDonor().build();
        Donation unsafeDonation = aDonation()
                .withTTIStatus(TTIStatus.TTI_UNSAFE)
                .withDonor(donor)
                .withBloodTestResults(bloodTestResults)
                .build();
        TestBatch testBatch = aTestBatch()
                .withDonationBatch(aDonationBatch().withDonation(unsafeDonation).build())
                .build();

        when(donationConstraintChecker.donationHasDiscrepancies(unsafeDonation)).thenReturn(false);
        when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(false);
        
        testBatchStatusChangeService.handleRelease(testBatch);
        
        verify(postDonationCounsellingCRUDService).createPostDonationCounsellingForDonation(unsafeDonation);
        verify(componentCRUDService).markComponentsBelongingToDonorAsUnsafe(donor);
        verifyZeroInteractions(donorDeferralCRUDService);
    }
    
    @Test
    public void testHandleReleaseWithUnsafeDonationAndDonorToBeDeferred_shouldDeferDonor() {
        
        List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
        Donor donor = aDonor().build();
        Donation unsafeDonation = aDonation()
                .withTTIStatus(TTIStatus.TTI_UNSAFE)
                .withDonor(donor)
                .withBloodTestResults(bloodTestResults)
                .build();
        TestBatch testBatch = aTestBatch()
                .withDonationBatch(aDonationBatch().withDonation(unsafeDonation).build())
                .build();
        
        when(donationConstraintChecker.donationHasDiscrepancies(unsafeDonation)).thenReturn(false);
        when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(true);
        
        testBatchStatusChangeService.handleRelease(testBatch);
        
        verify(donorDeferralCRUDService).createDeferralForDonorWithDeferralReasonType(donor,
                DeferralReasonType.AUTOMATED_TTI_UNSAFE);
    }

}
