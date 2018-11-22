package org.jembi.bsis.service;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.testbatch.DonationAdditionResult;
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
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aTestingSite;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
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
  public void testAddDonationsToClosedTestBatch_shouldThrow() {
    UUID testBatchId = UUID.randomUUID();

    TestBatch testBatch = aTestBatch().withId(testBatchId).withStatus(CLOSED).build();

    DonationBatch donationBatchClosed = aDonationBatch().thatIsClosed().build();
    Donation donationOne = aDonation().withDonationBatch(donationBatchClosed).withId(UUID.randomUUID()).thatIsNotDeleted().build();
    Donation donationTwo = aDonation().withDonationBatch(donationBatchClosed).withId(UUID.randomUUID()).thatIsNotDeleted().build();
    List<Donation> donations = Arrays.asList(donationOne, donationTwo);

    testBatchCRUDService.addDonationsToTestBatch(testBatch, donations);
  }

  @Test
  public void testAddDonationsToTestBatchWithDonationNotProducingSamples_shouldNotSetTestBatch() {
    Location location = aTestingSite().build();
    TestBatch testBatch = aTestBatch()
        .withId(UUID.randomUUID())
        .withStatus(OPEN)
        .withLocation(location)
        .withDonations(new HashSet<>())
        .build();

    DonationBatch donationBatchClosed = aDonationBatch().thatIsClosed().build();

    PackType didNotBleedPackType = aPackType()
        .withTestSampleProduced(false)
        .build();
    Donation donationThatDoesNotProduceATestSample = aDonation()
        .withId(UUID.randomUUID())
        .withDonationBatch(donationBatchClosed)
        .withDonationIdentificationNumber("no-test-sample")
        .withPackType(didNotBleedPackType)
        .thatIsNotDeleted()
        .build();

    PackType testSamplePackType = aPackType()
        .withTestSampleProduced(true)
        .build();
    Donation donationThatProducesATestSample = aDonation()
        .withId(UUID.randomUUID())
        .withDonationBatch(donationBatchClosed)
        .withPackType(testSamplePackType)
        .thatIsNotDeleted()
        .build();

    List<Donation> donations = Arrays.asList(donationThatDoesNotProduceATestSample, donationThatProducesATestSample);

    DonationAdditionResult expected = DonationAdditionResult.builder().testBatch(testBatch).build()
        .addDinWithoutTestSample(donationThatDoesNotProduceATestSample.getDonationIdentificationNumber());

    DonationAdditionResult actual = testBatchCRUDService.addDonationsToTestBatch(testBatch, donations);

    assertThat(donationThatDoesNotProduceATestSample.getTestBatch(), is(not(equalTo(testBatch))));
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void testAddDonationsToTestBatchWithDonationBelongingToAnotherTestBatch_shouldNotSetTestBatch() {
    TestBatch testBatch = aTestBatch()
        .withId(UUID.randomUUID())
        .withStatus(OPEN)
        .withDonations(new HashSet<>())
        .build();
    TestBatch anotherTestBatch = aTestBatch()
        .withId(UUID.randomUUID())
        .withStatus(OPEN)
        .withDonations(new HashSet<>())
        .build();

    DonationBatch donationBatchClosed = aDonationBatch().thatIsClosed().build();

    PackType packType = aPackType()
        .withTestSampleProduced(true)
        .build();
    Donation donation = aDonation()
        .withId(UUID.randomUUID())
        .withDonationBatch(donationBatchClosed)
        .withPackType(packType)
        .build();
    Donation donationBelongingToAnotherTestBatch = aDonation()
        .withId(UUID.randomUUID())
        .withDonationBatch(donationBatchClosed)
        .withDonationIdentificationNumber("another")
        .withPackType(packType)
        .withTestBatch(anotherTestBatch)
        .thatIsNotDeleted()
        .build();

    List<Donation> donations = Arrays.asList(donation, donationBelongingToAnotherTestBatch);

    DonationAdditionResult expected = DonationAdditionResult.builder().testBatch(testBatch).build()
        .addDinInAnotherTestBatch(donationBelongingToAnotherTestBatch.getDonationIdentificationNumber());

    DonationAdditionResult actual = testBatchCRUDService.addDonationsToTestBatch(testBatch, donations);

    assertThat(donationBelongingToAnotherTestBatch.getTestBatch(), is(not(equalTo(testBatch))));
    assertThat(donation.getTestBatch(), is(testBatch));
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void testAddDonationsToTestBatchWithDonationBelongingToOpenDonationBatch_shouldNotSetTestBatch() {
    TestBatch testBatch = aTestBatch()
        .withId(UUID.randomUUID())
        .withStatus(OPEN)
        .withDonations(new HashSet<>())
        .build();

    DonationBatch donationBatchClosed = aDonationBatch().thatIsClosed().build();
    DonationBatch donationBatchOpen = aDonationBatch().build();

    PackType packType = aPackType()
        .withTestSampleProduced(true)
        .build();
    Donation donation = aDonation()
        .withId(UUID.randomUUID())
        .withDonationBatch(donationBatchClosed)
        .withPackType(packType)
        .build();
    Donation donationBelongingToOpenDonationBatch = aDonation()
        .withId(UUID.randomUUID())
        .withDonationBatch(donationBatchOpen)
        .withDonationIdentificationNumber("another")
        .withPackType(packType)
        .thatIsNotDeleted()
        .build();

    List<Donation> donations = Arrays.asList(donation, donationBelongingToOpenDonationBatch);

    DonationAdditionResult expected = DonationAdditionResult.builder().testBatch(testBatch).build()
        .addDinInOpenDonationBatch(donationBelongingToOpenDonationBatch.getDonationIdentificationNumber());

    DonationAdditionResult actual = testBatchCRUDService.addDonationsToTestBatch(testBatch, donations);

    assertThat(donationBelongingToOpenDonationBatch.getTestBatch(), is(not(equalTo(testBatch))));
    assertThat(donation.getTestBatch(), is(testBatch));
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void testAddDonationsToTestBatchWithTestSampleProducingPackType_shouldSetTestBatch() {
    PackType packtype = aPackType()
        .withTestSampleProduced(true)
        .build();

    DonationBatch donationBatchClosed = aDonationBatch().thatIsClosed().build();
    Donation donationOne = aDonation().withDonationBatch(donationBatchClosed).withId(UUID.randomUUID()).thatIsNotDeleted().withPackType(packtype).build();
    Donation donationTwo = aDonation().withDonationBatch(donationBatchClosed).withId(UUID.randomUUID()).thatIsNotDeleted().withPackType(packtype).build();
    List<Donation> donations = Arrays.asList(donationOne, donationTwo);

    TestBatch testBatch = aTestBatch().withId(UUID.randomUUID()).withStatus(OPEN).withDonations(new HashSet<>()).build();

    DonationAdditionResult expected = DonationAdditionResult.builder().testBatch(testBatch).build();

    DonationAdditionResult actual = testBatchCRUDService.addDonationsToTestBatch(testBatch, donations);

    assertThat(donationOne.getTestBatch(), is(testBatch));
    assertThat(donationTwo.getTestBatch(), is(testBatch));
    assertThat(actual, is(expected));
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
        aTestBatch().withId(TEST_BATCH_ID).withStatus(OPEN).withDonations(Sets.newHashSet(donationToRemove)).build();

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
