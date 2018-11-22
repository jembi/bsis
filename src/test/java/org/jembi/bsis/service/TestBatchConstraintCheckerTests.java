package org.jembi.bsis.service;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.service.TestBatchConstraintChecker.CanReleaseResult;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleResultBuilder.aBloodTestingRuleResult;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
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
  public void testCanReleaseTestBatchWithNullDonations_shouldReturnFalse() {
    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN).withDonations(null).build();
    CanReleaseResult result = testBatchConstraintChecker.canReleaseTestBatch(testBatch);

    assertThat(result.canRelease(), is(false));
    assertThat(result.getReadyCount(), is(0));
  }

  @Test
  public void testCanReleaseTestBatchWithNoDonations_shouldReturnFalse() {
    TestBatch testBatch =
        aTestBatch().withStatus(TestBatchStatus.OPEN).withDonations(Collections.<Donation>emptySet()).build();
    CanReleaseResult result = testBatchConstraintChecker.canReleaseTestBatch(testBatch);

    assertThat(result.canRelease(), is(false));
    assertThat(result.getReadyCount(), is(0));
  }

  @Test
  public void testCanReleaseTestBatchWithNoOutstandingOutcomes_shouldReturnTrue() {

    Donation donation = aDonation()
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .build();

    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN)
        .withDonations(new HashSet<Donation>(Arrays.asList(donation))).build();

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
        .withId(UUID.randomUUID())
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .build();
    Donation donationWithoutDiscrepancies = aDonation()
        .withId(UUID.randomUUID())
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .build();
    Donation donationWithoutTestSample = aDonation()
        .withId(UUID.randomUUID())
        .withPackType(aPackType().withTestSampleProduced(false).build())
        .build();

    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.OPEN)
            .withDonations(new HashSet<Donation>(Arrays.asList(
                donationWithDiscrepancies,
                donationWithoutDiscrepancies,
                donationWithoutTestSample
            )))
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

    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN)
        .withDonations(new HashSet<Donation>(Arrays.asList(donationWithOutstandingOutcomes))).build();

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
  public void testCanCloseTestBatchWithNoDonations_shouldReturnTrue() {

    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED).withDonations(Collections.<Donation>emptySet())
        .build();

    boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanCloseTestBatchWithDonationWithoutDiscrepancies_shouldReturnTrue() {

    Donation donationWithoutDiscrepancies = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .withDonations(new HashSet<Donation>(Arrays.asList(donationWithoutDiscrepancies)))
        .build();

    when(donationConstraintChecker.donationHasDiscrepancies(donationWithoutDiscrepancies)).thenReturn(false);

    boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanCloseTestBatchWithDonationWithDiscrepancies_shouldReturnFalse() {

    Donation donationWithDiscrepancies = aDonation().build();
    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.RELEASED)
        .withDonations(new HashSet<Donation>(Arrays.asList(donationWithDiscrepancies))).build();

    when(donationConstraintChecker.donationHasDiscrepancies(donationWithDiscrepancies)).thenReturn(true);

    boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);

    assertThat(result, is(false));
  }

  @Test
  public void testCanDeleteTestBatchWithNoTestResults_shouldReturnTrue() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED).withDonations(new HashSet<Donation>(Arrays.asList(donation)))
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(false);

    boolean result = testBatchConstraintChecker.canDeleteTestBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanDeleteTestBatchWithTestResults_shouldReturnFalse() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.RELEASED)
        .withDonations(new HashSet<Donation>(Arrays.asList(donation))).build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(true);

    boolean result = testBatchConstraintChecker.canDeleteTestBatch(testBatch);

    assertThat(result, is(false));
  }

  @Test
  public void testCanReopenClosedTestBatches() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.CLOSED)
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(true);

    boolean result = testBatchConstraintChecker.canReopenTestBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanReopenTestBatches() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(false);

    boolean result = testBatchConstraintChecker.canReopenTestBatch(testBatch);

    assertThat(result, is(false));
  }

  @Test
  public void testCanEditReleasedTestBatches() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(false);

    boolean result = testBatchConstraintChecker.canEditTestBatch(testBatch);

    assertThat(result, is(true));
  }

  @Test
  public void testCanEditClosedTestBatches() {
    Donation donation = aDonation().build();
    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.CLOSED)
        .build();

    when(donationConstraintChecker.donationHasSavedTestResults(donation)).thenReturn(false);

    boolean result = testBatchConstraintChecker.canEditTestBatch(testBatch);

    assertThat(result, is(false));
  }

  @Test
  public void testCanCloseTestBatchWithNullDonations_shouldReturnTrue() {
    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.RELEASED).withDonations(null).build();
    boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);
    assertThat(result, is(true));
  }
}
