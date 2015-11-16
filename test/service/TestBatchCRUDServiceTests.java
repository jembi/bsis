package service;

import static helpers.builders.TestBatchBuilder.aTestBatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import repository.TestBatchRepository;
import suites.UnitTestSuite;

public class TestBatchCRUDServiceTests extends UnitTestSuite {
    
    public static final Long TEST_BATCH_ID = 7L;
    
    @InjectMocks
    private TestBatchCRUDService testBatchCRUDService;
    @Mock
    private TestBatchRepository testBatchRepository;
    @Mock
    private TestBatchConstraintChecker testBatchConstraintChecker;
    @Mock
    private TestBatchStatusChangeService testBatchStatusChangeService;
    
    @Test
    public void testUpdateTestBatchStatusWithNoStatusChange_shouldDoNothing() {
        TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();
        
        when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
        
        testBatchCRUDService.updateTestBatchStatus(TEST_BATCH_ID, TestBatchStatus.OPEN);
        
        verify(testBatchRepository, never()).updateTestBatch(any(TestBatch.class));
        verifyZeroInteractions(testBatchStatusChangeService);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUpdateTestBatchStatusWithTestBatchThatCannotBeReleased_shouldThrow() {
        TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();
        
        when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
        when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(false);
        
        testBatchCRUDService.updateTestBatchStatus(TEST_BATCH_ID, TestBatchStatus.RELEASED);
        
        verify(testBatchRepository, never()).updateTestBatch(any(TestBatch.class));
        verifyZeroInteractions(testBatchStatusChangeService);
    }
    
    @Test
    public void testUpdateTestBatchStatusWithReleasedStatus_shouldHandleRelease() {
        TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();
        TestBatch updatedTestBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();
        
        when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
        when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(true);
        when(testBatchRepository.updateTestBatch(testBatch)).thenReturn(updatedTestBatch);
        
        TestBatch returnedTestBatch = testBatchCRUDService.updateTestBatchStatus(TEST_BATCH_ID, TestBatchStatus.RELEASED);
        
        verify(testBatchStatusChangeService).handleRelease(updatedTestBatch);
        verify(testBatchRepository).updateTestBatch(testBatch);
        assertThat(returnedTestBatch.getStatus(), is(TestBatchStatus.RELEASED));
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdateTestBatchStatusWithTestBatchThatCannotBeClosed_shouldThrow() {
        TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();
        
        when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
        when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(false);
        
        testBatchCRUDService.updateTestBatchStatus(TEST_BATCH_ID, TestBatchStatus.CLOSED);
        
        verify(testBatchRepository, never()).updateTestBatch(any(TestBatch.class));
        verifyZeroInteractions(testBatchStatusChangeService);
    }

    @Test
    public void testUpdateTestBatchStatusWithTestBatchThatCanBeClosed_shouldUpdateTestBatch() {
        TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();
        
        when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
        when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(true);
        when(testBatchRepository.updateTestBatch(testBatch)).thenReturn(testBatch);
        
        TestBatch returnedTestBatch = testBatchCRUDService.updateTestBatchStatus(TEST_BATCH_ID, TestBatchStatus.CLOSED);
        
        verify(testBatchRepository).updateTestBatch(testBatch);
        assertThat(returnedTestBatch.getStatus(), is(TestBatchStatus.CLOSED));
    }
}
