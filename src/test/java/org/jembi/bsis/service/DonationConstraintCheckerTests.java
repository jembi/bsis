package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleResultBuilder.aBloodTestingRuleResult;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.helpers.builders.AdverseEventBuilder;
import org.jembi.bsis.helpers.builders.DonationBatchBuilder;
import org.jembi.bsis.helpers.builders.TestBatchBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.BloodTestResultRepository;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DonationConstraintCheckerTests extends UnitTestSuite {

  private static final UUID IRRELEVANT_DONATION_ID = UUID.randomUUID();

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

    boolean canDelete = donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID);

    assertThat(canDelete, is(false));
  }

  @Test
  public void testCanUpdateDonationFieldsWithDonationWithChangedComponents_shouldReturnFalse() {
    when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
    when(componentRepository.countChangedComponentsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(1);

    boolean canDelete = donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID);

    assertThat(canDelete, is(false));
  }

  @Test
  public void testCanUpdateDonationWithFieldsDonationWithNoConstraints_shouldReturnTrue() {
    when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
    when(componentRepository.countChangedComponentsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);

    boolean canDelete = donationConstraintChecker.canEditBleedTimes(IRRELEVANT_DONATION_ID);

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
        .withTTIStatus(TTIStatus.SAFE)
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

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult()
        .withPendingRepeatAndConfirmatoryTtiTestsIds(UUID.randomUUID()).build();

    when(bloodTestsService.executeTests(donation)).thenReturn(bloodTestingRuleResult);

    boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);

    assertThat(result, is(true));
  }

  @Test
  public void testDonationHasDiscrepanciesWithNoTypeDeterminedBloodTypingMatchStatus_shouldReturnFalse() {
    Donation donation = aDonation()
        .withTTIStatus(TTIStatus.SAFE)
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
  public void testDonationHasDiscrepanciesInDeterminateBloodTypingStatus_shouldReturnFalse() {
    Donation donation = aDonation()
        .withTTIStatus(TTIStatus.SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.INDETERMINATE)
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
        .withTTIStatus(TTIStatus.SAFE)
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
        .withTTIStatus(TTIStatus.SAFE)
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
        .withTTIStatus(TTIStatus.SAFE)
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
        .withTTIStatus(TTIStatus.SAFE)
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
        .withTTIStatus(TTIStatus.SAFE)
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
        .withTTIStatus(TTIStatus.SAFE)
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
        .withTTIStatus(TTIStatus.SAFE)
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
        .withTTIStatus(TTIStatus.SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withPackType(aPackType().build())
        .build();

    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.RELEASED)
        .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult()
        .withPendingRepeatAndConfirmatoryTtiTestsIds(UUID.randomUUID()).build();

    boolean result = donationConstraintChecker.donationIsReleased(testBatch, donation, bloodTestingRuleResult);

    assertThat(result, is(false));
  }

  @Test
  public void testDonationIsOpenTestBatchReleasedPendingTests_shouldReturnFalse() {
    Donation donation = aDonation()
        .withTTIStatus(TTIStatus.SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withPackType(aPackType().build())
        .build();

    TestBatch testBatch = aTestBatch()
        .withStatus(TestBatchStatus.OPEN)
        .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult()
        .withPendingRepeatAndConfirmatoryTtiTestsIds(UUID.randomUUID()).build();

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
        .withTTIStatus(TTIStatus.SAFE)
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
        .withTTIStatus(TTIStatus.UNSAFE)
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
        .withTTIStatus(TTIStatus.SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withPackType(aPackType().build())
        .build();

    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    boolean result = donationConstraintChecker.donationHasOutstandingOutcomes(donation, bloodTestingRuleResult);

    assertThat(result, is(false));

  }
  
  @Test
  public void testCanEditPackTypeWithNoConstraints_shouldReturnTrue() {
    // Set up fixture
    Donation donation = aDonation().withId(IRRELEVANT_DONATION_ID).build();
    
    // Exercise SUT
    boolean canEditPackType = donationConstraintChecker.canEditPackType(donation);
    
    // Verify
    assertThat(canEditPackType, is(true));
  }
  
  @Test
  public void testCanEditPackTypeWithProcessedComponent_shouldReturnFalse() {
    // Set up fixture
    UUID componentId1 = UUID.randomUUID();
    UUID componentId2 = UUID.randomUUID();
    UUID componentId3 = UUID.randomUUID();
    Component processedComponent = aComponent().withId(componentId1).withStatus(ComponentStatus.PROCESSED).build();
    List<Component> components = Arrays.asList(
        processedComponent,
        aComponent().withId(componentId2).withParentComponent(processedComponent).withStatus(ComponentStatus.QUARANTINED).build(),
        aComponent().withId(componentId3).withParentComponent(processedComponent).withStatus(ComponentStatus.QUARANTINED).build()
    );
    Donation donation = aDonation().withId(IRRELEVANT_DONATION_ID).withComponents(components).build();
    
    // Exercise SUT
    boolean canEditPackType = donationConstraintChecker.canEditPackType(donation);
    
    // Verify
    assertThat(canEditPackType, is(false));
  }
  
  @Test
  public void testCanEditPackTypeWithDiscardedComponent_shouldReturnFalse() {
    // Set up fixture
    UUID componentId = UUID.randomUUID();
    Component discardedComponent = aComponent().withId(componentId).withStatus(ComponentStatus.DISCARDED).build();
    Donation donation = aDonation().withId(IRRELEVANT_DONATION_ID).withComponent(discardedComponent).build();
    
    // Exercise SUT
    boolean canEditPackType = donationConstraintChecker.canEditPackType(donation);
    
    // Verify
    assertThat(canEditPackType, is(false));
  }
  
  @Test
  public void testCanEditPackTypeWithLabelledComponent_shouldReturnFalse() {
    // Set up fixture
    UUID componentId = UUID.randomUUID();
    Component discardedComponent = aComponent()
        .withId(componentId)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .build();
    Donation donation = aDonation().withId(IRRELEVANT_DONATION_ID).withComponent(discardedComponent).build();
    
    // Exercise SUT
    boolean canEditPackType = donationConstraintChecker.canEditPackType(donation);
    
    // Verify
    assertThat(canEditPackType, is(false));
  }
  
  @Test
  public void testCanEditPackTypeWithDeletedDiscardedComponent_shouldReturnTrue() {
    // Set up fixture
    UUID componentId1 = UUID.randomUUID();
    UUID componentId2 = UUID.randomUUID();
    List<Component> components = Arrays.asList(
        aComponent().withId(componentId1).withStatus(ComponentStatus.DISCARDED).withIsDeleted(true).build(),
        aComponent().withId(componentId2).withStatus(ComponentStatus.QUARANTINED).build()
    );
    Donation donation = aDonation().withId(IRRELEVANT_DONATION_ID).withComponents(components).build();
    
    // Exercise SUT
    boolean canEditPackType = donationConstraintChecker.canEditPackType(donation);
    
    // Verify
    assertThat(canEditPackType, is(true));
  }

  @Test
  public void testCanEditToNewPackTypeThatDoesntProduceTestSamples_shouldReturnTrue() {
    // Set up fixture
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.RELEASED).build();
    Donation donation = aDonation().withTestBatch(testBatch).withPackType(aPackType().withTestSampleProduced(false).build()).build();
    PackType newPackType = aPackType().withTestSampleProduced(false).build();

    // Exercise SUT
    boolean canEditToNewPackType = donationConstraintChecker.canEditToNewPackType(donation, newPackType);

    // Verify
    assertThat(canEditToNewPackType, is(true));
  }

  @Test
  public void testCanEditToNewPackTypeThatDoesntProduceTestSamplesWithNoTestBatchStatus_shouldReturnTrue() {
    // Set up fixture
    TestBatch testBatch = TestBatchBuilder.aTestBatch().build();
    Donation donation = aDonation().withTestBatch(testBatch).withPackType(aPackType().withTestSampleProduced(false).build()).build();
    PackType newPackType = aPackType().withTestSampleProduced(false).build();

    // Exercise SUT
    boolean canEditToNewPackType = donationConstraintChecker.canEditToNewPackType(donation, newPackType);

    // Verify
    assertThat(canEditToNewPackType, is(true));
  }

  @Test
  public void testCanEditToNewPackTypeThatProducesTestSamplesFromPackTypeThatAlsoDoesWithNoTestBatch_shouldReturnTrue() {
    // Set up fixture
    Donation donation = aDonation().withPackType(aPackType().withTestSampleProduced(true).build()).build();
    PackType newPackType = aPackType().withTestSampleProduced(true).build();

    // Exercise SUT
    boolean canEditToNewPackType = donationConstraintChecker.canEditToNewPackType(donation, newPackType);

    // Verify
    assertThat(canEditToNewPackType, is(true));
  }

  @Test
  public void testCanEditToNewPackTypeThatProducesTestSamplesFromPackTypeThatAlsoDoesWithTestBatch_shouldReturnTrue() {
    // Set up fixture
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.RELEASED).build();
    Donation donation = aDonation()
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .withDonationBatch(DonationBatchBuilder.aDonationBatch().build())
        .withTestBatch(testBatch)
        .build();
    PackType newPackType = aPackType().withTestSampleProduced(true).build();

    // Exercise SUT
    boolean canEditToNewPackType = donationConstraintChecker.canEditToNewPackType(donation, newPackType);

    // Verify
    assertThat(canEditToNewPackType, is(true));
  }

  @Test
  public void testCanEditToNewPackTypeThatProducesTestSamplesFromPackTypeThatDoesntWithNoTestBatch_shouldReturnTrue() {
    // Set up fixture
    Donation donation = aDonation().withPackType(aPackType().withTestSampleProduced(false).build()).build();
    PackType newPackType = aPackType().withTestSampleProduced(true).build();

    // Exercise SUT
    boolean canEditToNewPackType = donationConstraintChecker.canEditToNewPackType(donation, newPackType);

    // Verify
    assertThat(canEditToNewPackType, is(true));
  }

  @Test
  public void testCanEditToNewPackTypeThatProducesTestSamplesFromPackTypeThatDoesntWithClosedTestBatch_shouldReturnFalse() {
    // Set up fixture
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.CLOSED).build();
    Donation donation = aDonation()
        .withPackType(aPackType().withTestSampleProduced(false).build())
        .withDonationBatch(DonationBatchBuilder.aDonationBatch().build())
        .withTestBatch(testBatch)
        .build();
    PackType newPackType = aPackType().withTestSampleProduced(true).build();

    // Exercise SUT
    boolean canEditToNewPackType = donationConstraintChecker.canEditToNewPackType(donation, newPackType);

    // Verify
    assertThat(canEditToNewPackType, is(false));
  }
  
  @Test
  public void testCanEditToNewPackTypeThatProducesTestSamplesFromPackTypeThatDoesntWithReleasedTestBatch_shouldReturnFalse() {
    // Set up fixture
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.RELEASED).build();
    Donation donation = aDonation()
        .withPackType(aPackType().withTestSampleProduced(false).build())
        .withDonationBatch(DonationBatchBuilder.aDonationBatch().build())
        .withTestBatch(testBatch)
        .build();
    PackType newPackType = aPackType().withTestSampleProduced(true).build();

    // Exercise SUT
    boolean canEditToNewPackType = donationConstraintChecker.canEditToNewPackType(donation, newPackType);

    // Verify
    assertThat(canEditToNewPackType, is(false));
  }
  
  @Test
  public void testCanEditToNewPackTypeThatProducesTestSamplesFromPackTypeThatDoesntWithOpenTestBatch_shouldReturnTrue() {
    // Set up fixture
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    Donation donation = aDonation()
        .withPackType(aPackType().withTestSampleProduced(false).build())
        .withDonationBatch(DonationBatchBuilder.aDonationBatch().build())
        .withTestBatch(testBatch)
        .build();
    PackType newPackType = aPackType().withTestSampleProduced(true).build();

    // Exercise SUT
    boolean canEditToNewPackType = donationConstraintChecker.canEditToNewPackType(donation, newPackType);

    // Verify
    assertThat(canEditToNewPackType, is(true));
  }

  @Test
  public void testCanEditToNewPackTypeThatDoesNotProduceTestSamplesFromPackTypeThatDoesWithReleasedTestBatch_shouldReturnFalse() {
    // Set up fixture
    TestBatch testBatch = TestBatchBuilder.aReleasedTestBatch().build();
    Donation donation = aDonation()
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .withDonationBatch(DonationBatchBuilder.aDonationBatch().build())
        .withTestBatch(testBatch)
        .build();
    PackType newPackType = aPackType().withTestSampleProduced(false).build();

    // Exercise SUT
    boolean canEditToNewPackType = donationConstraintChecker.canEditToNewPackType(donation, newPackType);

    // Verify
    assertThat(canEditToNewPackType, is(false));
  }

  @Test
  public void testCanEditToNewPackTypeThatDoesNotProduceTestSamplesFromPackTypeThatDoesWithClosedTestBatch_shouldReturnFalse() {
    // Set up fixture
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.CLOSED).build();
    Donation donation = aDonation()
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .withDonationBatch(DonationBatchBuilder.aDonationBatch().build())
        .withTestBatch(testBatch)
        .build();
    PackType newPackType = aPackType().withTestSampleProduced(false).build();

    // Exercise SUT
    boolean canEditToNewPackType = donationConstraintChecker.canEditToNewPackType(donation, newPackType);

    // Verify
    assertThat(canEditToNewPackType, is(false));
  }

  @Test
  public void testCanEditToNewPackTypeThatDoesNotProduceTestSamplesFromPackTypeThatDoesWithOpenTestBatch_shouldReturnTrue() {
    // Set up fixture
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    Donation donation = aDonation()
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .withDonationBatch(DonationBatchBuilder.aDonationBatch().build())
        .withTestBatch(testBatch)
        .build();
    PackType newPackType = aPackType().withTestSampleProduced(false).build();

    // Exercise SUT
    boolean canEditToNewPackType = donationConstraintChecker.canEditToNewPackType(donation, newPackType);

    // Verify
    assertThat(canEditToNewPackType, is(true));
  }

  @Test
  public void testCanEditToNewPackTypeThatDoesNotProduceTestSamplesFromPackTypeThatDoesWithDonationThatIsReleased_shouldReturnFalse() {
    // Set up fixture
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    Donation donation = aDonation()
        .withPackType(aPackType().withTestSampleProduced(true).build())
        .withDonationBatch(DonationBatchBuilder.aDonationBatch().build())
        .withTestBatch(testBatch)
        .thatIsReleased()
        .build();
    PackType newPackType = aPackType().withTestSampleProduced(false).build();

    // Exercise SUT
    boolean canEditToNewPackType = donationConstraintChecker.canEditToNewPackType(donation, newPackType);

    // Verify
    assertThat(canEditToNewPackType, is(false));
  }
}
