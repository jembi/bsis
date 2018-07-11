package org.jembi.bsis.service;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.SequenceNumberRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.jembi.bsis.service.TestBatchConstraintChecker.CanReleaseResult;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.jembi.bsis.helpers.matchers.TestBatchMatcher.hasSameStateAsTestBatch;
import static org.jembi.bsis.model.testbatch.TestBatchStatus.CLOSED;
import static org.jembi.bsis.model.testbatch.TestBatchStatus.OPEN;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;

public class TestBatchCRUDServiceTests extends UnitTestSuite {

  private static final UUID TEST_BATCH_ID = UUID.randomUUID();
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
  private SequenceNumberRepository sequenceNumberRepository;
  @Mock
  private DonationCRUDService donationCRUDService;

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
  public void testUpdateTestBatchWithOpenTestBatchWithoutCanEditPermission_shouldThrowError() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID)
        .withStatus(TestBatchStatus.OPEN).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);

    testBatchCRUDService.updateTestBatch(testBatch);
  }
  
  @Test
  public void testUpdateTestBatchWithOpenTestBatchAndCanEditPermission_shouldNotThrowError() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID)
        .withStatus(TestBatchStatus.OPEN).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(true);

    testBatchCRUDService.updateTestBatch(testBatch);
  }
  
  @Test
  public void testUpdateTestBatchWithClosedTestBatchWithoutCanEditPermission_shouldNotThrowError() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID)
        .withStatus(TestBatchStatus.CLOSED).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);

    testBatchCRUDService.updateTestBatch(testBatch);
  }
  
  @Test
  public void testUpdateTestBatchWithClosedTestBatchAndCanEditPermission_shouldNotThrowError() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID)
        .withStatus(TestBatchStatus.CLOSED).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(true);

    testBatchCRUDService.updateTestBatch(testBatch);
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
    TestBatch updatedTestBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(CAN_RELEASE);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(true);
    when(testBatchRepository.update(any(TestBatch.class))).thenReturn(testBatch);

    testBatchCRUDService.updateTestBatch(updatedTestBatch);

    verify(testBatchRepository, times(2)).update(argThat(hasSameStateAsTestBatch(updatedTestBatch)));
    verify(testBatchStatusChangeService).handleRelease(testBatch);
  }

  @Test
  public void testCloseReleasedTestBatch_shouldUpdateStatus() {
    TestBatch existingTestBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.RELEASED).build();
    TestBatch updatedTestBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.CLOSED).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(existingTestBatch);
    when(testBatchConstraintChecker.canReleaseTestBatch(existingTestBatch)).thenReturn(CAN_RELEASE);
    when(testBatchConstraintChecker.canEditTestBatch(existingTestBatch)).thenReturn(true);
    when(testBatchConstraintChecker.canCloseTestBatch(existingTestBatch)).thenReturn(true);
    when(testBatchRepository.update(any(TestBatch.class))).then(returnsFirstArg());

    testBatchCRUDService.updateTestBatch(updatedTestBatch);

    verify(testBatchRepository, times(2)).update(argThat(hasSameStateAsTestBatch(updatedTestBatch)));
    verify(testBatchStatusChangeService, times(0)).handleRelease(existingTestBatch);
  }

  @Test
  public void testUpdateTestBatch_shouldUpdateDateCreated() throws Exception {

    final Date testBatchDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-10-17");
    final Date newTestBatchDate = new Date();

    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN)
        .withTestBatchDate(testBatchDate).build();

    final TestBatch updated = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN)
        .withTestBatchDate(newTestBatchDate).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchRepository.update(testBatch)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(true);

    testBatchCRUDService.updateTestBatch(updated);

    verify(testBatchRepository).update(argThat(hasSameStateAsTestBatch(updated)));
  }

  @Test
  public void testDeleteTestBatch() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(true);

    testBatchCRUDService.deleteTestBatch(TEST_BATCH_ID);

    verify(testBatchRepository, times(1)).deleteTestBatch(TEST_BATCH_ID);
  }

  @Test(expected = IllegalStateException.class)
  public void testUnableDeleteTestBatch() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(false);

    testBatchCRUDService.deleteTestBatch(TEST_BATCH_ID);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testRemoveDonationsFromTestBatchThatCantAddOrRemoveDonations_shouldThrowIllegalStateException() {
    Donation donationToRemove = aDonation().build();

    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(CLOSED)
        .withDonations(Sets.newHashSet(donationToRemove)).build();

    donationToRemove.setTestBatch(testBatch);

    testBatchCRUDService.removeDonationsFromTestBatch(Arrays.asList(donationToRemove), testBatch);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveDonationsFromTestBatchWithDonationThatBelongToOtherTestBatch_shouldThrowIllegalArgumentException() {
    Donation donationToRemove = aDonation()
        .withTestBatch(aTestBatch().withId(UUID.randomUUID()).withStatus(OPEN).build()).build();

    TestBatch testBatch =
        aTestBatch().withId(TEST_BATCH_ID).withDonations(Sets.newHashSet(donationToRemove)).build();

    testBatchCRUDService.removeDonationsFromTestBatch(Arrays.asList(donationToRemove), testBatch);
  }

  @Test
  public void testRemoveDonationsFromTestBatch_shouldRemoveCorrectDonationAndDelegateToDonationCRUDService() {
    Donation donationToRemove = aDonation().build();
    Donation donation = aDonation().build();

    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(OPEN)
        .withDonations(Sets.newHashSet(donationToRemove, donation)).build();

    donationToRemove.setTestBatch(testBatch);
    donation.setTestBatch(testBatch);

    TestBatch actual = testBatchCRUDService
        .removeDonationsFromTestBatch(Collections.singletonList(donationToRemove), testBatch);

    assertThat(actual.getDonations().size(), is(equalTo(1)));
    assertThat(actual.getDonations(), contains(donation));
    verify(donationCRUDService).clearTestOutcomes(donationToRemove);
  }
}
