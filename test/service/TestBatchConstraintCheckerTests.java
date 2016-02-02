package service;

import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import service.TestBatchConstraintChecker.CanReleaseResult;
import suites.UnitTestSuite;
import viewmodel.BloodTestingRuleResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static helpers.builders.BloodTestingRuleResultBuilder.aBloodTestingRuleResult;
import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.PackTypeBuilder.aPackType;
import static helpers.builders.TestBatchBuilder.aTestBatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class TestBatchConstraintCheckerTests extends UnitTestSuite {

  @InjectMocks
  private TestBatchConstraintChecker testBatchConstraintChecker;
  @Mock
  private DonationConstraintChecker donationConstraintChecker;
  @Mock
  private BloodTestsService bloodTestsService;

  @Test
  public void testCanReleaseTestBatchWithNonOpenTestBatch_shouldReturnFalse() {

    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.CLOSED).build();

    CanReleaseResult result = testBatchConstraintChecker.canReleaseTestBatch(testBatch);

    assertThat(result.canRelease(), is(false));
  }

  @Test
  public void testCanReleaseTestBatchWithNullDonationBatch_shouldReturnTrue() {

    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN).withDonationBatches(null).build();

    CanReleaseResult result = testBatchConstraintChecker.canReleaseTestBatch(testBatch);

    assertThat(result.canRelease(), is(true));
    assertThat(result.getReadyCount(), is(0));
  }

  @Test
  public void testCanReleaseTestBatchWithNoOutstandingOutcomes_shouldReturnTrue() {

    Donation donation = aDonation()
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .build();

    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.OPEN)
        .withDonationBatches(Collections.singletonList(aDonationBatch().withDonation(donation).build()))
        .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    when(bloodTestsService.executeTests(donation)).thenReturn(bloodTestingRuleResult);
    when(donationConstraintChecker.donationHasOutstandingOutcomes(donation, bloodTestingRuleResult)).thenReturn(false);

    CanReleaseResult result = testBatchConstraintChecker.canReleaseTestBatch(testBatch);

    assertThat(result.canRelease(), is(true));
    assertThat(result.getReadyCount(), is(1));
  }

  @Test
  public void testCanReleaseTestBatchWithVariousDonations_shouldReturnCorrectCounts() {

    Donation donationWithDiscrepancies = aDonation()
        .withId(1L)
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .build();
    Donation donationWithoutDiscrepancies = aDonation()
        .withId(2L)
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .build();
    Donation donationWithoutTestSample = aDonation()
        .withId(3L)
        .withPackType(aPackType().withTestSampleProduced(false).build())
        .build();

    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.OPEN)
        .withDonationBatch(aDonationBatch()
            .withDonations(Arrays.asList(
                donationWithDiscrepancies,
                donationWithoutDiscrepancies,
                donationWithoutTestSample
            ))
            .build())
        .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
    BloodTestingRuleResult bloodTestingRuleResult2 = aBloodTestingRuleResult().build();

    when(bloodTestsService.executeTests(donationWithDiscrepancies)).thenReturn(bloodTestingRuleResult);
    when(donationConstraintChecker.donationHasOutstandingOutcomes(donationWithDiscrepancies, bloodTestingRuleResult))
        .thenReturn(false);
    when(donationConstraintChecker.donationHasDiscrepancies(donationWithDiscrepancies, bloodTestingRuleResult))
        .thenReturn(true);

    when(bloodTestsService.executeTests(donationWithoutDiscrepancies)).thenReturn(bloodTestingRuleResult2);
    when(donationConstraintChecker.donationHasOutstandingOutcomes(donationWithoutDiscrepancies, bloodTestingRuleResult2))
        .thenReturn(false);
    when(donationConstraintChecker.donationHasDiscrepancies(donationWithoutDiscrepancies, bloodTestingRuleResult2))
        .thenReturn(false);

    CanReleaseResult result = testBatchConstraintChecker.canReleaseTestBatch(testBatch);

    assertThat(result.canRelease(), is(true));
    assertThat(result.getReadyCount(), is(1));
  }

  @Test
  public void testCanReleaseTestBatchWithOutstandingOutcomes_shouldReturnFalse() {

    Donation donationWithOutstandingOutcomes = aDonation()
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .build();

    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.OPEN)
        .withDonationBatch(aDonationBatch().withDonation(donationWithOutstandingOutcomes).build())
        .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    when(bloodTestsService.executeTests(donationWithOutstandingOutcomes)).thenReturn(bloodTestingRuleResult);
    when(donationConstraintChecker.donationHasOutstandingOutcomes(donationWithOutstandingOutcomes, bloodTestingRuleResult))
        .thenReturn(true);

    CanReleaseResult result = testBatchConstraintChecker.canReleaseTestBatch(testBatch);

    assertThat(result.canRelease(), is(false));
  }

  @Test
  public void testCanCloseTestBatchWithOpenTestBatch_shouldReturnFalse() {

    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN).build();

    boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);

    assertThat(result, is(false));
  }

  @Test
  public void testCanCloseTestBatchWithNullDonationBatches_shouldReturnTrue() {

    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .withDonationBatches(null)
        .build();

    boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanCloseTestBatchWithNoDonationBatches_shouldReturnTrue() {

    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .withDonationBatches(Collections.emptyList())
        .build();

    boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanCloseTestBatchWithNoDonations_shouldReturnTrue() {

    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .withDonationBatch(aDonationBatch().withDonations(Collections.emptyList()).build())
        .build();

    boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanCloseTestBatchWithDonationWithoutDiscrepancies_shouldReturnTrue() {

    Donation donationWithoutDiscrepancies = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .withDonationBatch(aDonationBatch().withDonation(donationWithoutDiscrepancies).build())
        .build();

    when(donationConstraintChecker.donationHasDiscrepancies(donationWithoutDiscrepancies)).thenReturn(false);

    boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanCloseTestBatchWithDonationWithDiscrepancies_shouldReturnFalse() {

    Donation donationWithDiscrepancies = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .withDonationBatch(aDonationBatch().withDonation(donationWithDiscrepancies).build())
        .build();

    when(donationConstraintChecker.donationHasDiscrepancies(donationWithDiscrepancies)).thenReturn(true);

    boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);

    assertThat(result, is(false));
  }

  @Test
  public void testCanDeleteTestBatchWithNoTestResults_shouldReturnTrue() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .withDonationBatch(aDonationBatch().withDonation(donation).build())
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(false);

    boolean result = testBatchConstraintChecker.canDeleteTestBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanDeleteTestBatchWithTestResults_shouldReturnFalse() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .withDonationBatch(aDonationBatch().withDonation(donation).build())
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(true);

    boolean result = testBatchConstraintChecker.canDeleteTestBatch(testBatch);

    assertThat(result, is(false));
  }

  @Test
  public void testCanAddOrRemoveDonationBatchesWithTestResults_shouldReturnFalse() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .withDonationBatch(aDonationBatch().withDonation(donation).build())
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(true);

    boolean result = testBatchConstraintChecker.canAddOrRemoveDonationBatch(testBatch);

    assertThat(result, is(false));
  }

  @Test
  public void testCanAddOrRemoveDonationBatchesFromClosedTestBatch_shouldReturnFalse() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.CLOSED)
        .withDonationBatch(aDonationBatch().withDonation(donation).build())
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(false);

    boolean result = testBatchConstraintChecker.canAddOrRemoveDonationBatch(testBatch);

    assertThat(result, is(false));
  }

  @Test
  public void testCanAddOrRemoveDonationBatchesWithNullTestResults_shouldReturnTrue() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(false);

    boolean result = testBatchConstraintChecker.canAddOrRemoveDonationBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanAddOrRemoveDonationBatchesWithoutTestResults_shouldReturnTrue() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .withDonationBatches(new ArrayList<>())
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(false);

    boolean result = testBatchConstraintChecker.canAddOrRemoveDonationBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanReopenClosedDonationBatches() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.CLOSED)
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(true);

    boolean result = testBatchConstraintChecker.canReopenTestBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanReopenDonationBatches() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(false);

    boolean result = testBatchConstraintChecker.canReopenTestBatch(testBatch);

    assertThat(result, is(false));
  }

  @Test
  public void testCanEditDonationBatches() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(false);

    boolean result = testBatchConstraintChecker.canEditTestBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanEditClosedDonationBatches() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.CLOSED)
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(false);

    boolean result = testBatchConstraintChecker.canEditTestBatch(testBatch);

    assertThat(result, is(false));
  }
}
