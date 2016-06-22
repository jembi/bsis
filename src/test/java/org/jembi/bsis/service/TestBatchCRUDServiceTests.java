package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.jembi.bsis.helpers.matchers.TestBatchMatcher.hasSameStateAsTestBatch;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.helpers.builders.DonationBatchBuilder;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.jembi.bsis.service.TestBatchCRUDService;
import org.jembi.bsis.service.TestBatchConstraintChecker;
import org.jembi.bsis.service.TestBatchStatusChangeService;
import org.jembi.bsis.service.TestBatchConstraintChecker.CanReleaseResult;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import scala.actors.threadpool.Arrays;

public class TestBatchCRUDServiceTests extends UnitTestSuite {

  private static final Long TEST_BATCH_ID = 7L;
  private static final CanReleaseResult CAN_RELEASE = new TestBatchConstraintChecker.CanReleaseResult(true);
  private static final CanReleaseResult CANT_RELEASE = new TestBatchConstraintChecker.CanReleaseResult(false);

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

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(CANT_RELEASE);

    testBatchCRUDService.changeTestBatchStatus(testBatch, TestBatchStatus.RELEASED);

    assertThat(testBatch.getStatus(), is(TestBatchStatus.RELEASED));
    verifyZeroInteractions(testBatchStatusChangeService);
  }

  @Test
  public void testUpdateTestBatchStatusWithReleasedStatus_shouldHandleRelease() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();
    TestBatch updatedTestBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(CAN_RELEASE);
    when(testBatchRepository.update(testBatch)).thenReturn(updatedTestBatch);

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
  public void testUpdateTestBatchStatusWithTestBatchThatCanBeReopened_shouldUpdateTestBatch() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.CLOSED).build();

    when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(true);

    testBatchCRUDService.changeTestBatchStatus(testBatch, TestBatchStatus.OPEN);

    assertThat(testBatch.getStatus(), is(TestBatchStatus.OPEN));
  }

  @Test(expected = IllegalStateException.class)
  public void testUpdateTestBatchStatusWithTestBatchThatCannotBeReopend_shouldThrow() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();

    when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(false);

    testBatchCRUDService.changeTestBatchStatus(testBatch, TestBatchStatus.OPEN);

    verifyZeroInteractions(testBatchStatusChangeService);
  }

  @Test
  public void testUpdateTestBatch_shouldUpdateStatus() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();
    TestBatch expectedTestBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(CAN_RELEASE);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(true);
    when(testBatchRepository.update(any(TestBatch.class))).thenReturn(testBatch);

    testBatchCRUDService.updateTestBatch(TEST_BATCH_ID, TestBatchStatus.RELEASED, null, null);

    verify(testBatchRepository, times(2)).update(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
    verify(testBatchStatusChangeService).handleRelease(testBatch);
  }

  @Test(expected = java.lang.IllegalStateException.class)
  public void testCloseClosedTestBatch_shouldntUpdateStatus() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.CLOSED).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(true);

    testBatchCRUDService.updateTestBatch(TEST_BATCH_ID, TestBatchStatus.RELEASED, null, null);
  }

  @Test
  public void testCloseReleasedTestBatch_shouldUpdateStatus() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.CLOSED).build();
    TestBatch expectedTestBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(CAN_RELEASE);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(true);
    when(testBatchRepository.update(any(TestBatch.class))).thenReturn(testBatch);

    testBatchCRUDService.updateTestBatch(TEST_BATCH_ID, TestBatchStatus.RELEASED, null, null);

    verify(testBatchRepository, times(2)).update(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
    verify(testBatchStatusChangeService, times(0)).handleRelease(testBatch);
  }

  @Test
  public void testUpdateTestBatch_shouldUpdateDateCreated() throws Exception {

    final Date createdDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-10-17");
    final Date newCreatedDate = new Date();

    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN)
        .withCreatedDate(createdDate).build();

    final TestBatch expected = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN)
        .withCreatedDate(newCreatedDate).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchRepository.update(testBatch)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(true);

    testBatchCRUDService.updateTestBatch(TEST_BATCH_ID, null, newCreatedDate, null);

    verify(testBatchRepository).update(argThat(hasSameStateAsTestBatch(expected)));
  }

  @Test
  public void testUpdateTestBatch_shouldUpdateDonationBatch() {
    final DonationBatch donationBatch1 = new DonationBatchBuilder().withId(1l).build();
    final DonationBatch donationBatch2 = new DonationBatchBuilder().withId(2l).build();
    final DonationBatch donationBatch3 = new DonationBatchBuilder().withId(3l).build();

    final TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withDonationBatch(donationBatch1)
        .withDonationBatch(donationBatch2).withStatus(TestBatchStatus.OPEN).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(true);
    when(donationBatchRepository.findDonationBatchById(1l)).thenReturn(donationBatch1);
    when(donationBatchRepository.findDonationBatchById(2l)).thenReturn(donationBatch2);
    when(donationBatchRepository.findDonationBatchById(3l)).thenReturn(donationBatch3);
    when(donationBatchRepository.updateDonationBatch(donationBatch1)).thenReturn(donationBatch1);
    when(donationBatchRepository.updateDonationBatch(donationBatch2)).thenReturn(donationBatch2);
    when(donationBatchRepository.updateDonationBatch(donationBatch3)).thenReturn(donationBatch3);

    testBatchCRUDService.updateTestBatch(TEST_BATCH_ID, null, null, Arrays.asList(new Long[]{2l, 3l}));

    verify(donationBatchRepository, times(2)).updateDonationBatch(argThat(new TypeSafeMatcher<DonationBatch>() {

      @Override
      public void describeTo(Description description) {
        description.appendText("A donation batch");
      }

      @Override
      protected boolean matchesSafely(DonationBatch actual) {
        if (actual.getId().equals(1l)) {
          return (actual.getTestBatch() == null);
        }
        if (actual.getId().equals(3l)) {
          return (actual.getTestBatch() == testBatch);
        }
        return false;
      }
    }));
  }

  @Test(expected = java.lang.IllegalStateException.class)
  public void testUpdateTestBatch_shouldNotUpdateDonationBatch() {
    final DonationBatch donationBatch1 = new DonationBatchBuilder().withId(1l).build();

    final TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withDonationBatch(donationBatch1)
        .withDonationBatch(donationBatch1).withStatus(TestBatchStatus.OPEN).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(donationBatchRepository.findDonationBatchById(1l)).thenReturn(donationBatch1);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);
    when(donationBatchRepository.updateDonationBatch(donationBatch1)).thenReturn(donationBatch1);

    testBatchCRUDService.updateTestBatch(TEST_BATCH_ID, null, null, Arrays.asList(new Long[]{1l}));
  }

  @Test
  public void testDeleteTestBatch() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(true);

    testBatchCRUDService.deleteTestBatch(TEST_BATCH_ID);

    verify(testBatchRepository, times(1)).deleteTestBatch(TEST_BATCH_ID);
  }

  @Test(expected = java.lang.IllegalStateException.class)
  public void testUnableDeleteTestBatch() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(false);

    testBatchCRUDService.deleteTestBatch(TEST_BATCH_ID);
  }
}
