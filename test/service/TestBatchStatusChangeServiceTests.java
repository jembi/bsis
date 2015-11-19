package service;

import static helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static helpers.builders.BloodTestingRuleResultBuilder.aBloodTestingRuleResult;
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
import repository.DonationRepository;
import suites.UnitTestSuite;
import viewmodel.BloodTestingRuleResult;

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
    @Mock
    private BloodTestsService bloodTestsService;
    @Mock
    private DonationRepository donationRepository;
    
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
    public void testHandleReleaseWithoutComponentsToBeDiscarded_shouldUpdateComponentStatuses() {
        
        List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
        Donation donationWithoutDiscrepancies = aDonation().withBloodTestResults(bloodTestResults).build();
        TestBatch testBatch = aTestBatch()
                .withDonationBatch(aDonationBatch().withDonation(donationWithoutDiscrepancies).build())
                .build();
        BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
        
        when(donationConstraintChecker.donationHasDiscrepancies(donationWithoutDiscrepancies)).thenReturn(false);
        when(componentStatusCalculator.shouldComponentsBeDiscarded(bloodTestResults)).thenReturn(false);
        when(bloodTestsService.executeTests(donationWithoutDiscrepancies)).thenReturn(bloodTestingRuleResult);
        when(donationRepository.updateDonation(donationWithoutDiscrepancies)).thenReturn(donationWithoutDiscrepancies);
        
        testBatchStatusChangeService.handleRelease(testBatch);
        
        verify(bloodTestsService).updateDonationWithTestResults(donationWithoutDiscrepancies, bloodTestingRuleResult);
        verify(componentCRUDService).updateComponentStatusesForDonation(donationWithoutDiscrepancies);
        verifyZeroInteractions(postDonationCounsellingCRUDService, donorDeferralCRUDService);
    }
    
    @Test
    public void testHandleReleaseWithComponentsToBeDiscarded_shouldMarkComponentsAsUnsafe() {
        
        List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
        Donation donationWithoutDiscrepancies = aDonation().withBloodTestResults(bloodTestResults).build();
        TestBatch testBatch = aTestBatch()
                .withDonationBatch(aDonationBatch().withDonation(donationWithoutDiscrepancies).build())
                .build();
        BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
        
        when(donationConstraintChecker.donationHasDiscrepancies(donationWithoutDiscrepancies)).thenReturn(false);
        when(componentStatusCalculator.shouldComponentsBeDiscarded(bloodTestResults)).thenReturn(true);
        when(bloodTestsService.executeTests(donationWithoutDiscrepancies)).thenReturn(bloodTestingRuleResult);
        when(donationRepository.updateDonation(donationWithoutDiscrepancies)).thenReturn(donationWithoutDiscrepancies);
        
        testBatchStatusChangeService.handleRelease(testBatch);
        
        verify(componentCRUDService).markComponentsBelongingToDonationAsUnsafe(donationWithoutDiscrepancies);
        verify(bloodTestsService).updateDonationWithTestResults(donationWithoutDiscrepancies, bloodTestingRuleResult);
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
        BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

        when(donationConstraintChecker.donationHasDiscrepancies(unsafeDonation)).thenReturn(false);
        when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(false);
        when(bloodTestsService.executeTests(unsafeDonation)).thenReturn(bloodTestingRuleResult);
        when(donationRepository.updateDonation(unsafeDonation)).thenReturn(unsafeDonation);
        
        testBatchStatusChangeService.handleRelease(testBatch);
        
        verify(postDonationCounsellingCRUDService).createPostDonationCounsellingForDonation(unsafeDonation);
        verify(componentCRUDService).markComponentsBelongingToDonorAsUnsafe(donor);
        verify(bloodTestsService).updateDonationWithTestResults(unsafeDonation, bloodTestingRuleResult);
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
        BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
        
        when(donationConstraintChecker.donationHasDiscrepancies(unsafeDonation)).thenReturn(false);
        when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(true);
        when(bloodTestsService.executeTests(unsafeDonation)).thenReturn(bloodTestingRuleResult);
        when(donationRepository.updateDonation(unsafeDonation)).thenReturn(unsafeDonation);
        
        testBatchStatusChangeService.handleRelease(testBatch);
        
        verify(bloodTestsService).updateDonationWithTestResults(unsafeDonation, bloodTestingRuleResult);
        verify(donorDeferralCRUDService).createDeferralForDonorWithDeferralReasonType(donor,
                DeferralReasonType.AUTOMATED_TTI_UNSAFE);
    }

}
