package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleResultBuilder.aBloodTestingRuleResult;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.mockito.Mockito.when;

import org.jembi.bsis.helpers.builders.AdverseEventBuilder;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.BloodTestResultRepository;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTypingMatchStatus;
import org.jembi.bsis.repository.bloodtesting.BloodTypingStatus;
import org.jembi.bsis.service.BloodTestsService;
import org.jembi.bsis.service.DonationConstraintChecker;
import org.jembi.bsis.service.DonorDeferralStatusCalculator;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
  public void testCanDeleteDonationWithAdverseEvent_shouldReturnFalse() {
    Donation donationWithAdverseEvents = aDonation().withAdverseEvent(AdverseEventBuilder.anAdverseEvent().build()).build();

    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithAdverseEvents);

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
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
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
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withPackType(aPackType().build())
        .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().withPendingTTITestId("12").build();

    when(bloodTestsService.executeTests(donation)).thenReturn(bloodTestingRuleResult);

    boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);

    assertThat(result, is(true));
  }

  @Test
  public void testDonationHasDiscrepanciesWithNoTypeDeterminedBloodTypingMatchStatus_shouldReturnFalse() {
    Donation donation = aDonation()
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withPackType(aPackType().build())
        .build();

    when(bloodTestsService.executeTests(donation))
        .thenReturn(aBloodTestingRuleResult().build());

    boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationHasDiscrepanciesWithMatchBloodTypingMatchStatus_shouldReturnFalse() {
    Donation donation = aDonation()
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withPackType(aPackType().build())
        .build();

    when(bloodTestsService.executeTests(donation))
        .thenReturn(aBloodTestingRuleResult().build());

    boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationHasDiscrepanciesWithResolvedBloodTypingMatchStatus_shouldReturnFalse() {
    Donation donation = aDonation()
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.RESOLVED)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withPackType(aPackType().build())
        .build();

    when(bloodTestsService.executeTests(donation))
        .thenReturn(aBloodTestingRuleResult().build());

    boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationHasDiscrepanciesWithAmbiguousBloodTypingMatchStatus_shouldReturnTrue() {
    Donation donation = aDonation()
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
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
        .withBloodTypingStatus(BloodTypingStatus.PENDING_TESTS)
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
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
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
  public void testDonationIsOpenTestBatchReleasedNoPendingTests_shouldReturnFalse() {
    Donation donation = aDonation()
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
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
  public void testDonationIsNullTestBatch_shouldReturnFalse() {
    Donation donation = aDonation()
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withPackType(aPackType().build())
        .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    boolean result = donationConstraintChecker.donationIsReleased(null, donation, bloodTestingRuleResult);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationIsReleasedTestBatchReleasedPendingTests_shouldReturnFalse() {
    Donation donation = aDonation()
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
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
  public void testDonationIsOpenTestBatchReleasedPendingTests_shouldReturnFalse() {
    Donation donation = aDonation()
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
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
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
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
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
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
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
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
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withPackType(aPackType().build())
        .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    boolean result = donationConstraintChecker.donationHasOutstandingOutcomes(donation, bloodTestingRuleResult);

    assertThat(result, is(false));

  }
}
