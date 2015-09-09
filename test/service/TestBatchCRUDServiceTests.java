package service;

import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.TestBatchBuilder.aTestBatch;
import static helpers.matchers.TestBatchMatcher.hasSameStateAsTestBatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
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
    
    @Test
    public void testUpdateTestBatchStatusWithStatusChangeNotToClosed_shouldUpdateTestBatchStatus() {
        long testBatchId = 526;
        TestBatchStatus newStatus = TestBatchStatus.READY_TO_CLOSE;

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
        
        verify(testBatchRepository).findTestBatchById(testBatchId);
        verify(testBatchRepository).updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
        verifyNoMoreInteractions(testBatchRepository);
        verifyZeroInteractions(postDonationCounsellingCRUDService);
        assertThat(returnedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
    }
    
    @Test
    public void testUpdateTestBatchStatusWithNoStatusChange_shouldUpdateTestBatchStatus() {
        long testBatchId = 526;
        TestBatchStatus newStatus = TestBatchStatus.CLOSED;

        TestBatch existingTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(TestBatchStatus.CLOSED)
                .build();

        TestBatch expectedTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(newStatus)
                .build();
        
        when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(existingTestBatch);
        when(testBatchRepository.updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch))))
                .thenReturn(expectedTestBatch);

        TestBatch returnedTestBatch = testBatchCRUDService.updateTestBatchStatus(testBatchId, newStatus);
        
        verify(testBatchRepository).findTestBatchById(testBatchId);
        verify(testBatchRepository).updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
        verifyNoMoreInteractions(testBatchRepository);
        verifyZeroInteractions(postDonationCounsellingCRUDService);
        assertThat(returnedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
    }
    
    @Test
    public void testUpdateTestBatchStatusWithNoDonationBatches_shouldUpdateTestBatchStatus() {
        long testBatchId = 526;
        TestBatchStatus newStatus = TestBatchStatus.CLOSED;

        TestBatch existingTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(TestBatchStatus.READY_TO_CLOSE)
                .build();

        TestBatch expectedTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(newStatus)
                .build();
        
        when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(existingTestBatch);
        when(testBatchRepository.updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch))))
                .thenReturn(expectedTestBatch);

        TestBatch returnedTestBatch = testBatchCRUDService.updateTestBatchStatus(testBatchId, newStatus);
        
        verify(testBatchRepository).findTestBatchById(testBatchId);
        verify(testBatchRepository).updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
        verifyNoMoreInteractions(testBatchRepository);
        verifyZeroInteractions(postDonationCounsellingCRUDService);
        assertThat(returnedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
    }
    
    @Test
    public void testUpdateTestBatchStatusWithDonationBatches_shouldCreatePostDonationCounsellingForDonationsWithUnsafeStatus() {
        long testBatchId = 526;
        TestBatchStatus newStatus = TestBatchStatus.CLOSED;
        
        Donation unsafeDonation = aDonation()
                .withId(123L)
                .withTTIStatus(TTIStatus.TTI_UNSAFE)
                .build();

        Donation safeDonation = aDonation()
                .withId(22L)
                .withTTIStatus(TTIStatus.TTI_SAFE)
                .build();
        
        List<DonationBatch> donationBatches = Arrays.asList(
                aDonationBatch()
                    .withDonations(Arrays.asList(unsafeDonation, safeDonation))
                    .build()
        );

        TestBatch existingTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(TestBatchStatus.READY_TO_CLOSE)
                .withDonationBatches(donationBatches)
                .build();

        TestBatch expectedTestBatch = aTestBatch()
                .withId(testBatchId)
                .withStatus(newStatus)
                .withDonationBatches(donationBatches)
                .build();
        
        when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(existingTestBatch);
        when(testBatchRepository.updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch))))
                .thenReturn(expectedTestBatch);

        TestBatch returnedTestBatch = testBatchCRUDService.updateTestBatchStatus(testBatchId, newStatus);
        
        verify(testBatchRepository).findTestBatchById(testBatchId);
        verify(postDonationCounsellingCRUDService).createPostDonationCounsellingForDonation(unsafeDonation);
        verify(postDonationCounsellingCRUDService, never()).createPostDonationCounsellingForDonation(safeDonation);
        verify(testBatchRepository).updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
        verifyNoMoreInteractions(testBatchRepository, postDonationCounsellingCRUDService);
        assertThat(returnedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
    }

}
