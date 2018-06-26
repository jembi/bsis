package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aTestingSite;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.jembi.bsis.helpers.matchers.TestBatchMatcher.hasSameStateAsTestBatch;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.SequenceNumberRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.jembi.bsis.service.TestBatchConstraintChecker.CanReleaseResult;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.util.RandomTestDate;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
  public void testAddDonationsToTestBatchWithTestResults_shouldThrow() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).build();
    Donation donation1 = aDonation().withId(UUID.randomUUID()).thatIsNotDeleted().build();
    Donation donation2 = aDonation().withId(UUID.randomUUID()).thatIsNotDeleted().build();
    List<Donation> donations = Arrays.asList(donation1, donation2);

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canAddOrRemoveDonation(testBatch)).thenReturn(false);

    testBatchCRUDService.addDonationsToTestBatch(TEST_BATCH_ID, donations);
  }
  
  @Test
  public void testAddDonationsToTestBatchWithDonationNotProducingSamples_shouldSave() {
    Location location = aTestingSite().build();
    TestBatch testBatch = aTestBatch()
      .withId(TEST_BATCH_ID)
      .withStatus(TestBatchStatus.OPEN)
      .withLocation(location)
      .withDonations(new HashSet<Donation>())
      .build();

    PackType didNotBleedPackType = aPackType()
        .withTestSampleProduced(false)
        .build();
    Donation donationThatDoesNotProduceATestSample = aDonation()
        .withId(UUID.randomUUID())
        .withPackType(didNotBleedPackType)
        .thatIsNotDeleted()
        .build();

    PackType testSamplePackType = aPackType()
        .withTestSampleProduced(true)
        .build();
    Donation donationThatProducesATestSample = aDonation()
        .withId(UUID.randomUUID())
        .withPackType(testSamplePackType)
        .thatIsNotDeleted()
        .build();
    
    List<Donation> donations = Arrays.asList(donationThatDoesNotProduceATestSample, donationThatProducesATestSample);

    TestBatch expectedTestBatch = aTestBatch()
        .withId(TEST_BATCH_ID)
        .withStatus(TestBatchStatus.OPEN)
        .withLocation(location)
        .withDonations(new HashSet<>(Arrays.asList(donationThatProducesATestSample)))
        .build();
    
    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canAddOrRemoveDonation(testBatch)).thenReturn(true);
    
    testBatchCRUDService.addDonationsToTestBatch(TEST_BATCH_ID, donations);

    verify(testBatchRepository).save(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
    assertThat(donationThatDoesNotProduceATestSample.getTestBatch(), nullValue());
    assertThat(donationThatProducesATestSample.getTestBatch(), is(hasSameStateAsTestBatch(expectedTestBatch)));
  }

  @Test
  public void testAddDonationsToTestBatchWithDonationBelongingToAnotherTEstBatch_shouldSave() {
    Location location = aTestingSite().build();
    TestBatch testBatch = aTestBatch()
      .withId(TEST_BATCH_ID)
      .withStatus(TestBatchStatus.OPEN)
      .withLocation(location)
      .withDonations(new HashSet<Donation>())
      .build();
    TestBatch anotherTestBatch = aTestBatch()
        .withId(UUID.randomUUID())
        .withStatus(TestBatchStatus.OPEN)
        .withLocation(location)
        .withDonations(new HashSet<Donation>())
        .build();

    PackType packType = aPackType()
        .withTestSampleProduced(true)
        .build();
    Donation donation = aDonation()
        .withId(UUID.randomUUID())
        .withPackType(packType)
        .thatIsNotDeleted()
        .build();
    Donation donationBelongingToAnotherTestBatch = aDonation()
        .withId(UUID.randomUUID())
        .withPackType(packType)
        .withTestBatch(anotherTestBatch)
        .thatIsNotDeleted()
        .build();
    anotherTestBatch.getDonations().add(donationBelongingToAnotherTestBatch);
    
    List<Donation> donations = Arrays.asList(donation, donationBelongingToAnotherTestBatch);

    TestBatch expectedTestBatch = aTestBatch()
        .withId(TEST_BATCH_ID)
        .withStatus(TestBatchStatus.OPEN)
        .withLocation(location)
        .withDonations(new HashSet<>(Arrays.asList(donation)))
        .build();
    
    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canAddOrRemoveDonation(testBatch)).thenReturn(true);
    
    testBatchCRUDService.addDonationsToTestBatch(TEST_BATCH_ID, donations);

    verify(testBatchRepository).save(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
    assertThat(donationBelongingToAnotherTestBatch.getTestBatch(), is(hasSameStateAsTestBatch(anotherTestBatch)));
    assertThat(donation.getTestBatch(), is(hasSameStateAsTestBatch(expectedTestBatch)));
  }
  
  @Test
  public void testAddDonationsToTestBatchWithDonationThatProducesSamples_shouldSave() {
    UUID donationId = UUID.randomUUID();
    Location location = aTestingSite().build();
    PackType packtype = aPackType()
        .withTestSampleProduced(true)
        .build();
    
    List<Donation> donations = Arrays.asList(
        aDonation()
            .withId(donationId)
            .withPackType(packtype)
            .thatIsNotDeleted()
            .build()       
    );
    HashSet<Donation> donationSet = new HashSet<>(donations);
        
    TestBatch testBatch = aTestBatch()
        .withId(TEST_BATCH_ID)
        .withStatus(TestBatchStatus.OPEN)
        .withLocation(location)
        .withDonations(new HashSet<Donation>())
        .build();
    
    TestBatch expectedTestBatch = aTestBatch()
        .withId(TEST_BATCH_ID)
        .withDonations(donationSet)
        .withLocation(location)
        .withStatus(TestBatchStatus.OPEN)
        .build();
    
    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canAddOrRemoveDonation(testBatch)).thenReturn(true);
    
    TestBatch updatedTestBatch = testBatchCRUDService.addDonationsToTestBatch(TEST_BATCH_ID, donations);
    verify(testBatchRepository).save(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
    for (Donation donation: updatedTestBatch.getDonations()) {
      assertThat(donation.getTestBatch(), hasSameStateAsTestBatch(testBatch)); 
    }
  }

  @Test
  public void testAddDonationsToTestBatch_shouldSave() {
    UUID donationOneId = UUID.randomUUID();
    UUID donationTwoId = UUID.randomUUID();
    Donation donation1 = aDonation().withId(donationOneId).thatIsNotDeleted().build();
    Donation donation2 = aDonation().withId(donationTwoId).thatIsNotDeleted().build();
    List<Donation> donations = Arrays.asList(donation1, donation2);
    HashSet<Donation> donationSet = new HashSet<Donation>(donations);
    Location location = aTestingSite().build();

    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN).withLocation(location)
        .withDonations(new HashSet<Donation>()).build();

    TestBatch expectedTestBatch = aTestBatch().withId(TEST_BATCH_ID).withStatus(TestBatchStatus.OPEN)
        .withLocation(location).withDonations(donationSet).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canAddOrRemoveDonation(testBatch)).thenReturn(true);

    TestBatch updatedTestBatch = testBatchCRUDService.addDonationsToTestBatch(TEST_BATCH_ID, donations);

    verify(testBatchRepository, times(1)).save(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
    for (Donation donation : updatedTestBatch.getDonations()) {
      assertThat(donation.getTestBatch(), is(testBatch));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddDonationsToTestBatchWithNoDonationsThatCanBeAdded_shouldThrow() {
    TestBatch testBatch = aTestBatch().withId(TEST_BATCH_ID).build();
    TestBatch anotherTestBatch = aTestBatch().withId(UUID.randomUUID()).build();
    Donation donation1 = aDonation()
        .withPackType(aPackType().withTestSampleProduced(false).build())
        .build();
    Donation donation2 = aDonation().withTestBatch(anotherTestBatch).build();

    when(testBatchRepository.findTestBatchById(TEST_BATCH_ID)).thenReturn(testBatch);
    when(testBatchConstraintChecker.canAddOrRemoveDonation(testBatch)).thenReturn(true);

    testBatchCRUDService.addDonationsToTestBatch(TEST_BATCH_ID,
        Arrays.asList(donation1, donation2));
  }

  @Test
  public void testCreatTestBatch_shouldCreateEntity() {
    Date testBatchDate = new RandomTestDate();
    String batchNumber = "1234567";
    Location location = aTestingSite().build();
    TestBatch testBatch = aTestBatch()
        .withTestBatchDate(testBatchDate)
        .withLocation(location)
        .build();

    TestBatch expectedTestBatch = aTestBatch()
        .withTestBatchDate(testBatchDate)
        .withLocation(location)
        .withBatchNumber(batchNumber)
        .withStatus(TestBatchStatus.OPEN)
        .build();

    when(sequenceNumberRepository.getNextTestBatchNumber()).thenReturn(batchNumber);

    TestBatch savedTestBatch = testBatchCRUDService.createTestBatch(testBatch);

    assertThat(savedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
    verify(testBatchRepository).save(argThat(hasSameStateAsTestBatch(expectedTestBatch)));
  }
}
