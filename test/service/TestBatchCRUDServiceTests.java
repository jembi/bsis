package service;

import static helpers.builders.BloodTestBuilder.aBloodTest;
import static helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.TestBatchBuilder.aTestBatch;
import static helpers.matchers.TestBatchMatcher.hasSameStateAsTestBatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.donor.Donor;
import model.donordeferral.DeferralReasonType;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.TestBatchRepository;

@RunWith(MockitoJUnitRunner.class)
public class TestBatchCRUDServiceTests {
    
    @InjectMocks
    private TestBatchCRUDService testBatchCRUDService;
    @Mock
    private TestBatchRepository testBatchRepository;
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
    private TestBatchConstraintChecker testBatchConstraintChecker;
    
    @Test
    public void testUpdateTestBatchStatusWithStatusChangeNotToReleased_shouldUpdateTestBatchStatus() {
        long testBatchId = 526;
        TestBatchStatus newStatus = TestBatchStatus.RELEASED;

        TestBatch existingTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(TestBatchStatus.OPEN)
                .build();

        TestBatch expectedTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(newStatus)
                .build();
        
        when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(existingTestBatch);
        when(testBatchRepository.updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch))))
                .thenReturn(expectedTestBatch);

        TestBatch returnedTestBatch = testBatchCRUDService.updateTestBatchStatus(testBatchId, newStatus);
        
        verify(testBatchRepository).updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
        verifyZeroInteractions(postDonationCounsellingCRUDService);
        verifyZeroInteractions(donorDeferralCRUDService);
        verifyZeroInteractions(componentCRUDService);
        assertThat(returnedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
    }
    
    @Test
    public void testUpdateTestBatchStatusWithNoStatusChange_shouldUpdateTestBatchStatus() {
        long testBatchId = 526;
        TestBatchStatus newStatus = TestBatchStatus.RELEASED;

        TestBatch existingTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(TestBatchStatus.RELEASED)
                .build();

        TestBatch expectedTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(newStatus)
                .build();
        
        when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(existingTestBatch);

        TestBatch returnedTestBatch = testBatchCRUDService.updateTestBatchStatus(testBatchId, newStatus);
        
        verify(testBatchRepository, never()).updateTestBatch(any(TestBatch.class));
        verifyZeroInteractions(postDonationCounsellingCRUDService);
        verifyZeroInteractions(donorDeferralCRUDService);
        verifyZeroInteractions(componentCRUDService);
        assertThat(returnedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUpdateTestBatchStatusToClosedWithOpenTestBatch_shouldThrow() {
        long testBatchId = 526;
        TestBatchStatus newStatus = TestBatchStatus.CLOSED;

        TestBatch existingTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(TestBatchStatus.OPEN)
                .build();
        
        when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(existingTestBatch);

        testBatchCRUDService.updateTestBatchStatus(testBatchId, newStatus);
        
        verify(testBatchRepository, never()).updateTestBatch(any(TestBatch.class));
        verifyZeroInteractions(postDonationCounsellingCRUDService);
        verifyZeroInteractions(donorDeferralCRUDService);
        verifyZeroInteractions(componentCRUDService);
    }
    
    @Test
    public void testUpdateTestBatchStatusWithNoDonationBatches_shouldUpdateTestBatchStatus() {
        long testBatchId = 526;
        TestBatchStatus newStatus = TestBatchStatus.RELEASED;

        TestBatch existingTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(TestBatchStatus.OPEN)
                .build();

        TestBatch expectedTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(newStatus)
                .build();
        
        when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(existingTestBatch);
        when(testBatchRepository.updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch))))
                .thenReturn(expectedTestBatch);

        TestBatch returnedTestBatch = testBatchCRUDService.updateTestBatchStatus(testBatchId, newStatus);
        
        verify(testBatchRepository).updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
        verifyZeroInteractions(postDonationCounsellingCRUDService);
        verifyZeroInteractions(donorDeferralCRUDService);
        verifyZeroInteractions(componentCRUDService);
        assertThat(returnedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUpdateTestBatchStatusWithTestBatchWithConstraints_shouldThrow() {
        long testBatchId = 526;

        List<DonationBatch> donationBatches = Arrays.asList(
                aDonationBatch()
                    .withDonations(Arrays.asList(aDonation().withId(22L).withTTIStatus(TTIStatus.TTI_SAFE).build()))
                    .build()
        );

        TestBatch existingTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(TestBatchStatus.OPEN)
                .withDonationBatches(donationBatches)
                .build();

        when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(existingTestBatch);
        when(testBatchConstraintChecker.canReleaseTestBatch(existingTestBatch)).thenReturn(false);

        testBatchCRUDService.updateTestBatchStatus(testBatchId, TestBatchStatus.RELEASED);
        
        verify(testBatchRepository, never()).updateTestBatch(any(TestBatch.class));
        verifyZeroInteractions(postDonationCounsellingCRUDService);
        verifyZeroInteractions(donorDeferralCRUDService);
        verifyZeroInteractions(componentCRUDService);
    }
    
    @Test
    public void testUpdateTestBatchStatusWithSafeDonation_shouldOnlyUpdateTestBatch() {
        long testBatchId = 526;
        TestBatchStatus newStatus = TestBatchStatus.RELEASED;

        Donation safeDonation = aDonation()
                .withId(22L)
                .withTTIStatus(TTIStatus.TTI_SAFE)
                .build();
        
        List<DonationBatch> donationBatches = Arrays.asList(
                aDonationBatch()
                    .withDonations(Arrays.asList(safeDonation))
                    .build()
        );

        TestBatch existingTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(TestBatchStatus.OPEN)
                .withDonationBatches(donationBatches)
                .build();

        TestBatch expectedTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(newStatus)
                .withDonationBatches(donationBatches)
                .build();
        
        when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(existingTestBatch);
        when(testBatchConstraintChecker.canReleaseTestBatch(existingTestBatch)).thenReturn(true);
        when(testBatchRepository.updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch))))
                .thenReturn(expectedTestBatch);

        TestBatch returnedTestBatch = testBatchCRUDService.updateTestBatchStatus(testBatchId, newStatus);
        
        verify(testBatchRepository).updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
        verifyZeroInteractions(postDonationCounsellingCRUDService);
        verifyZeroInteractions(donorDeferralCRUDService);
        verifyZeroInteractions(componentCRUDService);
        assertThat(returnedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
    }
    
    @Test
    public void testUpdateTestBatchStatusWithSafeDonationWithTestsThatDiscardComponents_shouldDiscardComponentsForDonation() {
        long testBatchId = 526;
        TestBatchStatus newStatus = TestBatchStatus.RELEASED;
        
        List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult()
                .withId(18L)
                .withBloodTest(aBloodTest().withId(99).withFlagComponentsForDiscard(true).build())
                .build());

        Donation safeDonation = aDonation()
                .withId(22L)
                .withTTIStatus(TTIStatus.TTI_SAFE)
                .withBloodTestResults(bloodTestResults)
                .build();
        
        List<DonationBatch> donationBatches = Arrays.asList(
                aDonationBatch()
                    .withDonations(Arrays.asList(safeDonation))
                    .build()
        );

        TestBatch existingTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(TestBatchStatus.OPEN)
                .withDonationBatches(donationBatches)
                .build();

        TestBatch expectedTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(newStatus)
                .withDonationBatches(donationBatches)
                .build();
        
        when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(existingTestBatch);
        when(testBatchConstraintChecker.canReleaseTestBatch(existingTestBatch)).thenReturn(true);
        when(componentStatusCalculator.shouldComponentsBeDiscarded(bloodTestResults)).thenReturn(true);
        when(testBatchRepository.updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch))))
                .thenReturn(expectedTestBatch);

        TestBatch returnedTestBatch = testBatchCRUDService.updateTestBatchStatus(testBatchId, newStatus);
        
        verify(testBatchRepository).updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
        verify(componentCRUDService).markComponentsBelongingToDonationAsUnsafe(safeDonation);
        verifyZeroInteractions(postDonationCounsellingCRUDService);
        verifyZeroInteractions(donorDeferralCRUDService);
        assertThat(returnedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
    }
    
    @Test
    public void testUpdateTestBatchStatusWithUnsafeDonationAndDonorNotToBeDeferred_shouldNotCreateDeferral() {
        long testBatchId = 526;
        TestBatchStatus newStatus = TestBatchStatus.RELEASED;
        
        Donor donorWithUnsafeDonation = aDonor().withId(86L).build();
        
        List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().withId(18L).build());
        
        Donation unsafeDonation = aDonation()
                .withId(123L)
                .withDonor(donorWithUnsafeDonation)
                .withTTIStatus(TTIStatus.TTI_UNSAFE)
                .withBloodTestResults(bloodTestResults)
                .build();
        
        List<DonationBatch> donationBatches = Arrays.asList(
                aDonationBatch()
                    .withDonations(Arrays.asList(unsafeDonation))
                    .build()
        );

        TestBatch existingTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(TestBatchStatus.OPEN)
                .withDonationBatches(donationBatches)
                .build();

        TestBatch expectedTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(newStatus)
                .withDonationBatches(donationBatches)
                .build();
        
        when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(existingTestBatch);
        when(testBatchConstraintChecker.canReleaseTestBatch(existingTestBatch)).thenReturn(true);
        when(testBatchRepository.updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch))))
                .thenReturn(expectedTestBatch);
        when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(false);

        TestBatch returnedTestBatch = testBatchCRUDService.updateTestBatchStatus(testBatchId, newStatus);

        verify(postDonationCounsellingCRUDService).createPostDonationCounsellingForDonation(unsafeDonation);
        verify(componentCRUDService).markComponentsBelongingToDonorAsUnsafe(donorWithUnsafeDonation);
        verifyZeroInteractions(donorDeferralCRUDService);
        verify(testBatchRepository).updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
        assertThat(returnedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
    }
    
    @Test
    public void testUpdateTestBatchStatusWithUnsafeDonationAndDonorToBeDeferred_shouldCreateDeferralAndCounsellingAndUpdateComponents() {
        long testBatchId = 526;
        TestBatchStatus newStatus = TestBatchStatus.RELEASED;
        
        Donor donorWithUnsafeDonation = aDonor().withId(86L).build();
        
        List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().withId(18L).build());
        
        Donation unsafeDonation = aDonation()
                .withId(123L)
                .withDonor(donorWithUnsafeDonation)
                .withTTIStatus(TTIStatus.TTI_UNSAFE)
                .withBloodTestResults(bloodTestResults)
                .build();
        
        List<DonationBatch> donationBatches = Arrays.asList(
                aDonationBatch()
                    .withDonations(Arrays.asList(unsafeDonation))
                    .build()
        );

        TestBatch existingTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(TestBatchStatus.OPEN)
                .withDonationBatches(donationBatches)
                .build();

        TestBatch expectedTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(newStatus)
                .withDonationBatches(donationBatches)
                .build();
        
        when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(existingTestBatch);
        when(testBatchConstraintChecker.canReleaseTestBatch(existingTestBatch)).thenReturn(true);
        when(testBatchRepository.updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch))))
                .thenReturn(expectedTestBatch);
        when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(true);

        TestBatch returnedTestBatch = testBatchCRUDService.updateTestBatchStatus(testBatchId, newStatus);

        verify(postDonationCounsellingCRUDService).createPostDonationCounsellingForDonation(unsafeDonation);
        verify(donorDeferralCRUDService).createDeferralForDonorWithDeferralReasonType(donorWithUnsafeDonation,
                DeferralReasonType.AUTOMATED_TTI_UNSAFE);
        verify(componentCRUDService).markComponentsBelongingToDonorAsUnsafe(donorWithUnsafeDonation);
        verify(testBatchRepository).updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
        assertThat(returnedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
    }

}
