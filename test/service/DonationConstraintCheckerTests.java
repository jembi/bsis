package service;

import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.BloodTestResultRepository;
import repository.ComponentRepository;
import repository.DonationRepository;
import repository.DonorRepository;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.bloodtesting.BloodTypingStatus;
import viewmodel.BloodTestingRuleResult;

import static helpers.builders.BloodTestingRuleResultBuilder.aBloodTestingRuleResult;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.PackTypeBuilder.aPackType;
import static helpers.builders.TestBatchBuilder.aTestBatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DonationConstraintCheckerTests {

  private static final long IRRELEVANT_DONATION_ID = 17;

  @InjectMocks
  private DonationConstraintChecker donationConstraintChecker;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private BloodTestResultRepository bloodTestResultRepository;
  @Mock
  private ComponentRepository componentRepository;
  @Mock
  private BloodTestsService bloodTestsService;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private DonorDeferralStatusCalculator donorDeferralStatusCalculator;

  @Test
  public void testCanDeleteDonationWithDonationWithNotes_shouldReturnFalse() {
    Donation donationWithNotes = aDonation().withNotes("irrelevant.notes").build();

    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithNotes);

    boolean canDelete = donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID);

    assertThat(canDelete, is(false));
  }

  @Test
  public void testCanDeleteDonationWithDonationWithBloodTestResults_shouldReturnFalse() {
    Donation donationWithTestResults = aDonation().build();

    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithTestResults);
    when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(1);

    boolean canDelete = donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID);

    assertThat(canDelete, is(false));
  }

  @Test
  public void testCanDeleteDonationWithDonationWithChangedComponents_shouldReturnFalse() {
    Donation donationWithChangedComponents = aDonation().build();

    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithChangedComponents);
    when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
    when(componentRepository.countChangedComponentsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(1);

    boolean canDelete = donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID);

    assertThat(canDelete, is(false));
  }

  @Test
  public void testCanDeleteDonationWithDonationWithNoConstraints_shouldReturnTrue() {
    Donation donationWithNotes = aDonation().withNotes("").build();

    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithNotes);
    when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
    when(componentRepository.countChangedComponentsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);

    boolean canDelete = donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID);

    assertThat(canDelete, is(true));
  }

  @Test
  public void testCanUpdateDonationFieldsWithDonationWithBloodTestResults_shouldReturnFalse() {
    Donation donationWithNotes = aDonation().build();

    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithNotes);
    when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(1);

    boolean canDelete = donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID);

    assertThat(canDelete, is(false));
  }

  @Test
  public void testCanUpdateDonationFieldsWithDonationWithChangedComponents_shouldReturnFalse() {
    when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
    when(componentRepository.countChangedComponentsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(1);

    boolean canDelete = donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID);

    assertThat(canDelete, is(false));
  }

  @Test
  public void testCanUpdateDonationWithFieldsDonationWithNoConstraints_shouldReturnTrue() {
    when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
    when(componentRepository.countChangedComponentsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);

    boolean canDelete = donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID);

    assertThat(canDelete, is(true));
  }

  @Test
  public void testDonationHasDiscrepanciesWithDonationWithNoTestSample_shouldReturnFalse() {
    Donation donation = aDonation()
            .withPackType(aPackType().withTestSampleProduced(false).build())
            .build();

    boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationHasDiscrepanciesWithNoDiscrepancies_shouldReturnFalse() {
    Donation donation = aDonation()
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withPackType(aPackType().build())
            .build();

    when(bloodTestsService.executeTests(donation))
            .thenReturn(aBloodTestingRuleResult().build());

    boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationHasDiscrepanciesWithPendingTTITests_shouldReturnTrue() {
    Donation donation = aDonation()
            .withDonor(aDonor().build())
            .withTTIStatus(TTIStatus.NOT_DONE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withPackType(aPackType().build())
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().withPendingTTITestId("12").build();

    when(bloodTestsService.executeTests(donation)).thenReturn(bloodTestingRuleResult);

    boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);

    assertThat(result, is(true));
  }

  @Test
  public void testDonationHasDiscrepanciesWithAmbiguousBloodTypingMatchStatus_shouldReturnTrue() {
    Donation donation = aDonation()
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withPackType(aPackType().build())
            .build();

    when(bloodTestsService.executeTests(donation))
            .thenReturn(aBloodTestingRuleResult().build());

    boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);

    assertThat(result, is(true));
  }

  @Test
  public void testDonationHasDiscrepanciesWithPendingTestsBloodTypingStatus_shouldReturnTrue() {
    Donation donation = aDonation()
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withBloodTyingStatus(BloodTypingStatus.PENDING_TESTS)
            .withPackType(aPackType().build())
            .build();

    when(bloodTestsService.executeTests(donation))
            .thenReturn(aBloodTestingRuleResult().build());

    boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);

    assertThat(result, is(true));
  }

  @Test
  public void testDonationIsReleasedTestBatchReleasedNoPendingTests_shouldReturnTrue() {
    Donation donation = aDonation()
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withPackType(aPackType().build())
            .build();

    TestBatch testBatch = aTestBatch()
            .withStatus(TestBatchStatus.RELEASED)
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    boolean result = donationConstraintChecker.donationIsReleased(testBatch, donation, bloodTestingRuleResult);

    assertThat(result, is(true));
  }

  @Test
  public void testDonationIsReleasedTestBatchReleasedNoPendingTests2_shouldReturnTrue() {
    Donation donation = aDonation()
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withPackType(aPackType().build())
            .build();

    TestBatch testBatch = aTestBatch()
            .withStatus(TestBatchStatus.RELEASED)
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
    when(bloodTestsService.executeTests(donation)).thenReturn(bloodTestingRuleResult);

    boolean result = donationConstraintChecker.donationIsReleased(testBatch, donation);

    assertThat(result, is(true));
  }

  @Test
  public void testDonationIsOpenTestBatchReleasedNoPendingTests_shouldReturnFalse() {
    Donation donation = aDonation()
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withPackType(aPackType().build())
            .build();

    TestBatch testBatch = aTestBatch()
            .withStatus(TestBatchStatus.OPEN)
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    boolean result = donationConstraintChecker.donationIsReleased(testBatch, donation, bloodTestingRuleResult);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationIsOpenTestBatchReleasedNoPendingTests2_shouldReturnFalse() {
    Donation donation = aDonation()
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withPackType(aPackType().build())
            .build();

    TestBatch testBatch = aTestBatch()
            .withStatus(TestBatchStatus.OPEN)
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
    when(bloodTestsService.executeTests(donation)).thenReturn(bloodTestingRuleResult);

    boolean result = donationConstraintChecker.donationIsReleased(testBatch, donation);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationIsNullTestBatch_shouldReturnFalse() {
    Donation donation = aDonation()
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withPackType(aPackType().build())
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    boolean result = donationConstraintChecker.donationIsReleased(null, donation, bloodTestingRuleResult);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationIsNullTestBatch2_shouldReturnFalse() {
    Donation donation = aDonation()
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withPackType(aPackType().build())
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
    when(bloodTestsService.executeTests(donation)).thenReturn(bloodTestingRuleResult);

    boolean result = donationConstraintChecker.donationIsReleased(null, donation);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationIsReleasedTestBatchReleasedPendingTests_shouldReturnFalse() {
    Donation donation = aDonation()
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withPackType(aPackType().build())
            .build();

    TestBatch testBatch = aTestBatch()
            .withStatus(TestBatchStatus.RELEASED)
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().withPendingTTITestId("12").build();

    boolean result = donationConstraintChecker.donationIsReleased(testBatch, donation, bloodTestingRuleResult);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationIsReleasedTestBatchReleasedPendingTests2_shouldReturnFalse() {
    Donation donation = aDonation()
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withPackType(aPackType().build())
            .build();

    TestBatch testBatch = aTestBatch()
            .withStatus(TestBatchStatus.RELEASED)
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().withPendingTTITestId("12").build();
    when(bloodTestsService.executeTests(donation)).thenReturn(bloodTestingRuleResult);

    boolean result = donationConstraintChecker.donationIsReleased(testBatch, donation);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationIsOpenTestBatchReleasedPendingTests_shouldReturnFalse() {
    Donation donation = aDonation()
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withPackType(aPackType().build())
            .build();

    TestBatch testBatch = aTestBatch()
            .withStatus(TestBatchStatus.OPEN)
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().withPendingTTITestId("12").build();

    boolean result = donationConstraintChecker.donationIsReleased(testBatch, donation, bloodTestingRuleResult);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationIsOpenTestBatchReleasedPendingTests2_shouldReturnFalse() {
    Donation donation = aDonation()
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withPackType(aPackType().build())
            .build();

    TestBatch testBatch = aTestBatch()
            .withStatus(TestBatchStatus.OPEN)
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().withPendingTTITestId("12").build();
    when(bloodTestsService.executeTests(donation)).thenReturn(bloodTestingRuleResult);

    boolean result = donationConstraintChecker.donationIsReleased(testBatch, donation);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationHasOutstandingOutcomesWithDonationWithNoTestSample_shouldReturnFalse() {
    Donation donation = aDonation()
            .withPackType(aPackType().withTestSampleProduced(false).build())
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    boolean result = donationConstraintChecker.donationHasOutstandingOutcomes(donation, bloodTestingRuleResult);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationHasOutstandingOutcomesWithNotDoneTTIStatus_shouldReturnTrue() {

    Donation donation = aDonation()
            .withId(IRRELEVANT_DONATION_ID)
            .withDonor(aDonor().build())
            .withTTIStatus(TTIStatus.NOT_DONE)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withPackType(aPackType().build())
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    boolean result = donationConstraintChecker.donationHasOutstandingOutcomes(donation, bloodTestingRuleResult);

    assertThat(result, is(true));
  }

  @Test
  public void testDonationHasOutstandingOutcomesWithNotDoneBloodTypingStatus_shouldReturnTrue() {

    Donation donation = aDonation()
            .withId(IRRELEVANT_DONATION_ID)
            .withDonor(aDonor().build())
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTyingStatus(BloodTypingStatus.NOT_DONE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS)
            .withPackType(aPackType().build())
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    boolean result = donationConstraintChecker.donationHasOutstandingOutcomes(donation, bloodTestingRuleResult);

    assertThat(result, is(true));
  }

  @Test
  public void testDonationHasOutstandingOutcomesWithNotDoneBloodTypingMatchStatus_shouldReturnTrue() {

    Donation donation = aDonation()
            .withId(IRRELEVANT_DONATION_ID)
            .withDonor(aDonor().build())
            .withTTIStatus(TTIStatus.TTI_UNSAFE)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
            .withPackType(aPackType().build())
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    boolean result = donationConstraintChecker.donationHasOutstandingOutcomes(donation, bloodTestingRuleResult);

    assertThat(result, is(true));
  }

  @Test
  public void testDonationHasOutstandingOutcomesWithNoOutstandingOutcomes_shouldReturnFalse() {

    Donation donation = aDonation()
            .withId(IRRELEVANT_DONATION_ID)
            .withDonor(aDonor().build())
            .withTTIStatus(TTIStatus.TTI_SAFE)
            .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withPackType(aPackType().build())
            .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    boolean result = donationConstraintChecker.donationHasOutstandingOutcomes(donation, bloodTestingRuleResult);

    assertThat(result, is(false));

  }
}
