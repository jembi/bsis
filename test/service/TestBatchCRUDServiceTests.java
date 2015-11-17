package service;

import static helpers.builders.TestBatchBuilder.aTestBatch;
import static helpers.matchers.TestBatchMatcher.hasSameStateAsTestBatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import helpers.builders.DonationBatchBuilder;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import repository.DonationBatchRepository;
import repository.TestBatchRepository;
import scala.actors.threadpool.Arrays;
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
    @Mock
    private DonationBatchRepository donationBatchRepository;
    
    @Test
    public void testUpdateTestBatchStatusWithNoStatusChange_shouldDoNothing() {
        TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();
        
        testBatchCRUDService.changeTestBatchStatus(testBatch, TestBatchStatus.OPEN);

        assertThat(testBatch.getStatus(), is(TestBatchStatus.OPEN));
        verifyZeroInteractions(testBatchStatusChangeService);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUpdateTestBatchStatusWithTestBatchThatCannotBeReleased_shouldThrow() {
        TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();

        when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(false);
        
        testBatchCRUDService.changeTestBatchStatus(testBatch, TestBatchStatus.RELEASED);

        assertThat(testBatch.getStatus(), is(TestBatchStatus.RELEASED));
        verifyZeroInteractions(testBatchStatusChangeService);
    }
    
    @Test
    public void testUpdateTestBatchStatusWithReleasedStatus_shouldHandleRelease() {
        TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();
        TestBatch updatedTestBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();

        when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(true);
        when(testBatchRepository.updateTestBatch(testBatch)).thenReturn(updatedTestBatch);
        
        testBatchCRUDService.changeTestBatchStatus(testBatch, TestBatchStatus.RELEASED);
        
        verify(testBatchStatusChangeService).handleRelease(updatedTestBatch);
        assertThat(testBatch.getStatus(), is(TestBatchStatus.RELEASED));
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdateTestBatchStatusWithTestBatchThatCannotBeClosed_shouldThrow() {
        TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();

        when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(false);
        
        testBatchCRUDService.changeTestBatchStatus(testBatch, TestBatchStatus.CLOSED);

        verifyZeroInteractions(testBatchStatusChangeService);
    }

    @Test
    public void testUpdateTestBatchStatusWithTestBatchThatCanBeClosed_shouldUpdateTestBatch() {
        TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();

        when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(true);
        
        testBatchCRUDService.changeTestBatchStatus(testBatch, TestBatchStatus.CLOSED);

        assertThat(testBatch.getStatus(), is(TestBatchStatus.CLOSED));
    }
    
    @Test
    public void testUpdateTestBatch_shouldUpdateStatus() {
        TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();
        TestBatch expectedTestBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();
        
        when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
        when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(true);
		when(testBatchRepository.updateTestBatch(any(TestBatch.class))).thenReturn(testBatch);

		testBatchCRUDService.updateTestBatch(TEST_BATCH_ID, TestBatchStatus.RELEASED, null, null);
        
        verify(testBatchRepository, times(2)).updateTestBatch(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
        verify(testBatchStatusChangeService).handleRelease(testBatch);
    }
    
    @Test
    public void testUpdateTestBatch_shouldUpdateDateCreated() throws Exception {
    	
    	final Date createdDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-10-17");
    	final Date newCreatedDate = new Date();

        TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withCreatedDate(createdDate).build();
        
		final TestBatch expected = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN)
		        .withCreatedDate(newCreatedDate).build();
        
        when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
		when(testBatchRepository.updateTestBatch(testBatch)).thenReturn(testBatch);

		testBatchCRUDService.updateTestBatch(TEST_BATCH_ID, null, newCreatedDate, null);

		verify(testBatchRepository).updateTestBatch(argThat(hasSameStateAsTestBatch(expected)));
    }
    
	@Test
	public void testUpdateTestBatch_shouldUpdateDonationBatch() {
		final DonationBatch donationBatch1 = new DonationBatchBuilder().withId(1).build();
		final DonationBatch donationBatch2 = new DonationBatchBuilder().withId(2).build();
		final DonationBatch donationBatch3 = new DonationBatchBuilder().withId(3).build();
		
		final TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withDonationBatch(donationBatch1)
		        .withDonationBatch(donationBatch2).withStatus(TestBatchStatus.OPEN).build();
		
		when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
		when(testBatchRepository.updateTestBatch(testBatch)).thenReturn(testBatch);
		when(donationBatchRepository.findDonationBatchById(1)).thenReturn(donationBatch1);
		when(donationBatchRepository.findDonationBatchById(2)).thenReturn(donationBatch2);
		when(donationBatchRepository.findDonationBatchById(3)).thenReturn(donationBatch3);
		when(donationBatchRepository.updateDonationBatch(donationBatch1)).thenReturn(donationBatch1);
		when(donationBatchRepository.updateDonationBatch(donationBatch2)).thenReturn(donationBatch2);
		when(donationBatchRepository.updateDonationBatch(donationBatch3)).thenReturn(donationBatch3);
		
		testBatchCRUDService.updateTestBatch(TEST_BATCH_ID, null, null, Arrays.asList(new Integer[] { 2, 3 }));
		
		verify(donationBatchRepository,times(2)).updateDonationBatch(argThat(new TypeSafeMatcher<DonationBatch>() {
			
			@Override
			public void describeTo(Description description) {
				description.appendText("A donation batch");
			}
			
			@Override
			protected boolean matchesSafely(DonationBatch actual) {
				if (actual.getId().equals(1)) {
					return (actual.getTestBatch() == null);
				}
				if (actual.getId().equals(3)) {
					return (actual.getTestBatch() == testBatch);
				}
				return false;
			}
		}));
	}
}
