package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleResultBuilder.aBloodTestingRuleResult;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.jembi.bsis.helpers.matchers.DonorMatcher.hasSameStateAsDonor;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DeferralReasonType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TestBatchStatusChangeServiceTests extends UnitTestSuite {
  
  private static final UUID FIRST_BLOOD_TEST_RESULT_ID=UUID.randomUUID();
  private static final UUID SECOND_BLOOD_TEST_RESULT_ID=UUID.randomUUID();
  
  @InjectMocks
  private TestBatchStatusChangeService testBatchStatusChangeService;
  @Mock
  private PostDonationCounsellingCRUDService postDonationCounsellingCRUDService;
  @Mock
  private DonorDeferralCRUDService donorDeferralCRUDService;
  @Mock
  private ComponentCRUDService componentCRUDService;
  @Mock
  private DonorDeferralStatusCalculator donorDeferralStatusCalculator;
  @Mock
  private ComponentStatusCalculator componentStatusCalculator;
  @Mock
  private DonationConstraintChecker donationConstraintChecker;
  @Mock
  private BloodTestsService bloodTestsService;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private DonorRepository donorRepository;

  @Test
  public void testHandleReleaseWithNoDonations_shouldDoNothing() {

    TestBatch testBatch = aTestBatch().withDonations(null).build();

    testBatchStatusChangeService.handleRelease(testBatch);

    verifyZeroInteractions(postDonationCounsellingCRUDService, donorDeferralCRUDService, componentCRUDService);
  }

  @Test
  public void testHandleReleaseWithADonationWithDiscrepancies_shouldDoNothing() {

    Donation donationWithDiscrepancies = aDonation()
        .withDonor(aDonor().build())
        .withPackType(aPackType().build())
        .build();
    TestBatch testBatch = aTestBatch()
        .withDonation(donationWithDiscrepancies)
        .build();

    when(donationConstraintChecker.donationHasDiscrepancies(donationWithDiscrepancies)).thenReturn(true);

    testBatchStatusChangeService.handleRelease(testBatch);

    verifyZeroInteractions(postDonationCounsellingCRUDService, donorDeferralCRUDService, componentCRUDService);
  }
  
  @Test
  public void testHandleReleaseWithADonationWithDiscrepancies_shouldNotSetDonorABORh() {

    Donation donationWithDiscrepancies = aDonation()
        .withDonor(aDonor().build())
        .withPackType(aPackType().build())
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    TestBatch testBatch = aTestBatch()
        .withDonation(donationWithDiscrepancies)
        .build();

    when(donationConstraintChecker.donationHasDiscrepancies(donationWithDiscrepancies)).thenReturn(true);

    testBatchStatusChangeService.handleRelease(testBatch);

    assertThat(donationWithDiscrepancies.getDonor().getBloodAbo(), not(equalTo("A")));
    assertThat(donationWithDiscrepancies.getDonor().getBloodRh(), not(equalTo("+")));
  }

  @Test
  public void testHandleReleaseWithDonationWithoutTestSample_shouldDoNothing() {

    Donation donation = aDonation()
        .withDonor(aDonor().build())
        .withPackType(aPackType().withTestSampleProduced(false).build())
        .build();
    TestBatch testBatch = aTestBatch()
        .withDonation(donation)
        .build();

    when(donationConstraintChecker.donationHasDiscrepancies(donation)).thenReturn(true);

    testBatchStatusChangeService.handleRelease(testBatch);

    verifyZeroInteractions(postDonationCounsellingCRUDService, donorDeferralCRUDService, componentCRUDService);
  }

  @Test
  public void testHandleReleaseWithoutComponentsToBeDiscarded_shouldUpdateComponentStatuses() {

    List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
    String bloodAbo = "A";
    String bloodRh = "+";
    Donor donor = aDonor().build();
    Donation donationWithoutDiscrepancies = aDonation()
        .withDonor(donor)
        .withBloodTestResults(bloodTestResults)
        .withPackType(aPackType().build())
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .thatIsNotReleased()
        .build();
    TestBatch testBatch = aTestBatch()
        .withDonation(donationWithoutDiscrepancies)
        .build();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    when(donationConstraintChecker.donationHasDiscrepancies(donationWithoutDiscrepancies)).thenReturn(false);
    when(componentStatusCalculator.shouldComponentsBeDiscardedForTestResults(bloodTestResults)).thenReturn(false);
    when(bloodTestsService.executeTests(donationWithoutDiscrepancies)).thenReturn(bloodTestingRuleResult);
    when(donationRepository.update(donationWithoutDiscrepancies)).thenReturn(donationWithoutDiscrepancies);

    testBatchStatusChangeService.handleRelease(testBatch);

    verify(bloodTestsService).updateDonationWithTestResults(donationWithoutDiscrepancies, bloodTestingRuleResult);
    verify(componentCRUDService).updateComponentStatusesForDonation(donationWithoutDiscrepancies);
    verifyZeroInteractions(postDonationCounsellingCRUDService, donorDeferralCRUDService);
    assertThat(donationWithoutDiscrepancies.isReleased(), is(true));
  }

  @Test
  public void testHandleReleaseWithComponentsToBeDiscarded_shouldMarkComponentsAsUnsafe() {

    List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
    String bloodAbo = "B";
    String bloodRh = "-";
    Donor donor = aDonor().build();
    Donor expectedDonor = aDonor().withBloodAbo(bloodAbo).withBloodRh(bloodRh).build();
    Donation donationWithoutDiscrepancies = aDonation()
        .withDonor(donor)
        .withBloodTestResults(bloodTestResults)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withPackType(aPackType().build())
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .thatIsNotReleased()
        .build();
    TestBatch testBatch = aTestBatch()
        .withDonation(donationWithoutDiscrepancies)
        .build();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    when(donationConstraintChecker.donationHasDiscrepancies(donationWithoutDiscrepancies)).thenReturn(false);
    when(componentStatusCalculator.shouldComponentsBeDiscardedForTestResults(bloodTestResults)).thenReturn(true);
    when(bloodTestsService.executeTests(donationWithoutDiscrepancies)).thenReturn(bloodTestingRuleResult);
    when(donationRepository.update(donationWithoutDiscrepancies)).thenReturn(donationWithoutDiscrepancies);

    testBatchStatusChangeService.handleRelease(testBatch);

    verify(componentCRUDService).markComponentsBelongingToDonationAsUnsafe(donationWithoutDiscrepancies);
    verify(bloodTestsService).updateDonationWithTestResults(donationWithoutDiscrepancies, bloodTestingRuleResult);
    verify(donorRepository).saveDonor(argThat(hasSameStateAsDonor(expectedDonor)));
    verifyZeroInteractions(postDonationCounsellingCRUDService, donorDeferralCRUDService);
    assertThat(donationWithoutDiscrepancies.isReleased(), is(true));
  }

  @Test
  public void testHandleReleaseWithUnsafeDonation_shouldDiscardComponents() {

    List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
    String bloodAbo = "AB";
    String bloodRh = "-";
    Donor donor = aDonor().build();
    Donor expectedDonor = aDonor().withBloodAbo(bloodAbo).withBloodRh(bloodRh).build();
    Donation unsafeDonation = aDonation()
        .withTTIStatus(TTIStatus.UNSAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withDonor(donor)
        .withBloodTestResults(bloodTestResults)
        .withPackType(aPackType().build())
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .thatIsNotReleased()
        .build();
    TestBatch testBatch = aTestBatch()
        .withDonation(unsafeDonation)
        .build();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    when(donationConstraintChecker.donationHasDiscrepancies(unsafeDonation)).thenReturn(false);
    when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(false);
    when(bloodTestsService.executeTests(unsafeDonation)).thenReturn(bloodTestingRuleResult);
    when(donationRepository.update(unsafeDonation)).thenReturn(unsafeDonation);

    testBatchStatusChangeService.handleRelease(testBatch);

    verify(componentCRUDService).markComponentsBelongingToDonorAsUnsafe(donor);
    verify(bloodTestsService).updateDonationWithTestResults(unsafeDonation, bloodTestingRuleResult);
    verify(donorRepository).saveDonor(argThat(hasSameStateAsDonor(expectedDonor)));
    verifyZeroInteractions(donorDeferralCRUDService);
    assertThat(unsafeDonation.isReleased(), is(true));
  }

  @Test
  public void testHandleReleaseWithUnsafeDonationAndDonorToBeDeferred_shouldDeferDonorAndCreateCounsellingReferral() {

    Location location = LocationBuilder.aLocation().withId(UUID.randomUUID()).withName("Test Location").build();
    List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
    String bloodAbo = "O";
    String bloodRh = "+";
    Donor donor = aDonor().withBloodAbo("A").withBloodRh("-").build();
    Donor expectedDonor = aDonor().withBloodAbo(bloodAbo).withBloodRh(bloodRh).build();
    Donation unsafeDonation = aDonation()
        .withTTIStatus(TTIStatus.UNSAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withVenue(location)
        .withDonor(donor)
        .withBloodTestResults(bloodTestResults)
        .withPackType(aPackType().build())
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .thatIsNotReleased()
        .build();
    TestBatch testBatch = aTestBatch()
        .withDonation(unsafeDonation)
        .build();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    when(donationConstraintChecker.donationHasDiscrepancies(unsafeDonation)).thenReturn(false);
    when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(true);
    when(bloodTestsService.executeTests(unsafeDonation)).thenReturn(bloodTestingRuleResult);
    when(donationRepository.update(unsafeDonation)).thenReturn(unsafeDonation);

    testBatchStatusChangeService.handleRelease(testBatch);

    verify(bloodTestsService).updateDonationWithTestResults(unsafeDonation, bloodTestingRuleResult);
    verify(postDonationCounsellingCRUDService).createPostDonationCounsellingForDonation(unsafeDonation);
    verify(donorDeferralCRUDService).createDeferralForDonorWithVenueAndDeferralReasonType(donor, location,
        DeferralReasonType.AUTOMATED_TTI_UNSAFE);
    verify(donorRepository).saveDonor(argThat(hasSameStateAsDonor(expectedDonor)));
    assertThat(unsafeDonation.isReleased(), is(true));
  }
  
  @Test
  public void testHandleReleaseWithNoTypeDeterminedBloodTyping_shouldNotSetBloodABORhOfDonor() {

    Location location = LocationBuilder.aLocation().withId(UUID.randomUUID()).withName("Test Location").build();
    List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
    String bloodAbo = "O";
    String bloodRh = "+";
    Donor donor = aDonor().withBloodAbo(bloodAbo).withBloodRh(bloodRh).build();
    Donation noTypeDeterminedBloodTypingOutcomeDonation = aDonation()
        .withTTIStatus(TTIStatus.SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED)
        .withBloodAbo("A") // note: invalid values
        .withBloodRh("+")
        .withVenue(location)
        .withDonor(donor)
        .withBloodTestResults(bloodTestResults)
        .withPackType(aPackType().build())
        .thatIsNotReleased()
        .build();
    TestBatch testBatch = aTestBatch()
        .withDonation(noTypeDeterminedBloodTypingOutcomeDonation)
        .build();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    when(donationConstraintChecker.donationHasDiscrepancies(noTypeDeterminedBloodTypingOutcomeDonation)).thenReturn(false);
    when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(false);
    when(bloodTestsService.executeTests(noTypeDeterminedBloodTypingOutcomeDonation)).thenReturn(bloodTestingRuleResult);
    when(donationRepository.update(noTypeDeterminedBloodTypingOutcomeDonation)).thenReturn(noTypeDeterminedBloodTypingOutcomeDonation);

    testBatchStatusChangeService.handleRelease(testBatch);

    verify(donorRepository, times(0)).saveDonor(any(Donor.class));
    assertThat(noTypeDeterminedBloodTypingOutcomeDonation.isReleased(), is(true));
    assertThat(donor.getBloodAbo(), is(bloodAbo));
    assertThat(donor.getBloodRh(), is(bloodRh));
  }
  
  @Test
  public void testHandleReleaseWithNoTypeDeterminedBloodTyping_shouldMarkComponentsAsUnsafe() {

    Location location = LocationBuilder.aLocation().withId(UUID.randomUUID()).withName("Test Location").build();
    List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
    String bloodAbo = "O";
    String bloodRh = "+";
    Donor donor = aDonor().withBloodAbo(bloodAbo).withBloodRh(bloodRh).build();
    Donation noTypeDeterminedBloodTypingOutcomeDonation = aDonation()
        .withTTIStatus(TTIStatus.SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED)
        .withBloodAbo("A") // note: invalid values
        .withBloodRh("+")
        .withVenue(location)
        .withDonor(donor)
        .withBloodTestResults(bloodTestResults)
        .withPackType(aPackType().build())
        .thatIsNotReleased()
        .build();
    TestBatch testBatch = aTestBatch()
        .withDonation(noTypeDeterminedBloodTypingOutcomeDonation)
        .build();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    when(donationConstraintChecker.donationHasDiscrepancies(noTypeDeterminedBloodTypingOutcomeDonation)).thenReturn(false);
    when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(false);
    when(bloodTestsService.executeTests(noTypeDeterminedBloodTypingOutcomeDonation)).thenReturn(bloodTestingRuleResult);
    when(donationRepository.update(noTypeDeterminedBloodTypingOutcomeDonation)).thenReturn(noTypeDeterminedBloodTypingOutcomeDonation);

    testBatchStatusChangeService.handleRelease(testBatch);

    verify(componentCRUDService).markComponentsBelongingToDonationAsUnsafe(noTypeDeterminedBloodTypingOutcomeDonation);
    assertThat(noTypeDeterminedBloodTypingOutcomeDonation.isReleased(), is(true));
  }
  
  @Test
  public void testHandleReleaseWithIndeterminateTTIStatus_shouldMarkComponentsAsUnsafe() {

    Location location = LocationBuilder.aLocation().withId(UUID.randomUUID()).withName("Test Location").build();
    List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
    String bloodAbo = "A";
    String bloodRh = "+";
    Donor donor = aDonor().withBloodAbo(bloodAbo).withBloodRh(bloodRh).build();
    Donation indeterminateBloodTypingOutcomeDonation = aDonation()
        .withTTIStatus(TTIStatus.INDETERMINATE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .withVenue(location)
        .withDonor(donor)
        .withBloodTestResults(bloodTestResults)
        .withPackType(aPackType().build())
        .thatIsNotReleased()
        .build();
    TestBatch testBatch = aTestBatch()
        .withDonation(indeterminateBloodTypingOutcomeDonation)
        .build();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    when(donationConstraintChecker.donationHasDiscrepancies(indeterminateBloodTypingOutcomeDonation)).thenReturn(false);
    when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(false);
    when(bloodTestsService.executeTests(indeterminateBloodTypingOutcomeDonation)).thenReturn(bloodTestingRuleResult);
    when(donationRepository.update(indeterminateBloodTypingOutcomeDonation)).thenReturn(indeterminateBloodTypingOutcomeDonation);

    testBatchStatusChangeService.handleRelease(testBatch);

    verify(componentCRUDService).markComponentsBelongingToDonationAsUnsafe(indeterminateBloodTypingOutcomeDonation);
    assertThat(indeterminateBloodTypingOutcomeDonation.isReleased(), is(true));
  }
 
  @Test
  public void testHandleReleaseWithIndeterminateBloodTyping_shouldNotSetBloodABORhOfDonor() {

    Location location = LocationBuilder.aLocation().withId(UUID.randomUUID()).withName("Test Location").build();
    List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
    String bloodAbo = "O";
    String bloodRh = "+";
    Donor donor = aDonor().withBloodAbo(bloodAbo).withBloodRh(bloodRh).build();
    Donation noTypeDeterminedBloodTypingOutcomeDonation = aDonation()
        .withTTIStatus(TTIStatus.SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.INDETERMINATE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodAbo("A") // note: invalid values
        .withBloodRh("+")
        .withVenue(location)
        .withDonor(donor)
        .withBloodTestResults(bloodTestResults)
        .withPackType(aPackType().build())
        .thatIsNotReleased()
        .build();
    TestBatch testBatch = aTestBatch()
        .withDonation(noTypeDeterminedBloodTypingOutcomeDonation)
        .build();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    when(donationConstraintChecker.donationHasDiscrepancies(noTypeDeterminedBloodTypingOutcomeDonation)).thenReturn(false);
    when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(false);
    when(bloodTestsService.executeTests(noTypeDeterminedBloodTypingOutcomeDonation)).thenReturn(bloodTestingRuleResult);
    when(donationRepository.update(noTypeDeterminedBloodTypingOutcomeDonation)).thenReturn(noTypeDeterminedBloodTypingOutcomeDonation);

    testBatchStatusChangeService.handleRelease(testBatch);

    verify(donorRepository, times(0)).saveDonor(any(Donor.class));
    assertThat(noTypeDeterminedBloodTypingOutcomeDonation.isReleased(), is(true));
    assertThat(donor.getBloodAbo(), is(bloodAbo));
    assertThat(donor.getBloodRh(), is(bloodRh));
  }
  
  @Test
  public void testHandleReleaseWithIndeterminateBloodTyping_shouldMarkComponentsAsUnsafe() {

    Location location = LocationBuilder.aLocation().withId(UUID.randomUUID()).withName("Test Location").build();
    List<BloodTestResult> bloodTestResults = Arrays.asList(aBloodTestResult().build());
    String bloodAbo = "O";
    String bloodRh = "+";
    Donor donor = aDonor().withBloodAbo(bloodAbo).withBloodRh(bloodRh).build();
    Donation noTypeDeterminedBloodTypingOutcomeDonation = aDonation()
        .withTTIStatus(TTIStatus.SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.INDETERMINATE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodAbo("A") // note: invalid values
        .withBloodRh("+")
        .withVenue(location)
        .withDonor(donor)
        .withBloodTestResults(bloodTestResults)
        .withPackType(aPackType().build())
        .thatIsNotReleased()
        .build();
    TestBatch testBatch = aTestBatch()
        .withDonation(noTypeDeterminedBloodTypingOutcomeDonation)
        .build();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();

    when(donationConstraintChecker.donationHasDiscrepancies(noTypeDeterminedBloodTypingOutcomeDonation)).thenReturn(false);
    when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(false);
    when(bloodTestsService.executeTests(noTypeDeterminedBloodTypingOutcomeDonation)).thenReturn(bloodTestingRuleResult);
    when(donationRepository.update(noTypeDeterminedBloodTypingOutcomeDonation)).thenReturn(noTypeDeterminedBloodTypingOutcomeDonation);

    testBatchStatusChangeService.handleRelease(testBatch);

    verify(componentCRUDService).markComponentsBelongingToDonationAsUnsafe(noTypeDeterminedBloodTypingOutcomeDonation);
    assertThat(noTypeDeterminedBloodTypingOutcomeDonation.isReleased(), is(true));
  }
  
  @Test
  public void testHandleReleaseWithContainsPlasma_shouldMarkComponentsAsUnsafeAndUpdateComponentsStatuses() {
    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withId(FIRST_BLOOD_TEST_RESULT_ID)
            .withResult("POS")
            .withBloodTest(aBloodTest()
                .thatShouldFlagComponentsContainingPlasmaForDiscard()
                .withPositiveResults("POS,+")
                .build())
            .build()
    );
    
    Donation donationThatContainsPlasma = aDonation()
        .withTTIStatus(TTIStatus.SAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodAbo("A") 
        .withBloodRh("+")
        .withBloodTestResults(bloodTestResults)
        .thatIsNotReleased()
        .build();
   
    TestBatch testBatch = aTestBatch()
        .withDonation(donationThatContainsPlasma)
        .build();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
    
    when(componentStatusCalculator.shouldComponentsBeDiscardedForTestResultsIfContainsPlasma(
        donationThatContainsPlasma.getBloodTestResults())).thenReturn(true);
    when(donationConstraintChecker.donationHasDiscrepancies(donationThatContainsPlasma)).thenReturn(false);
    when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(false);
    when(bloodTestsService.executeTests(donationThatContainsPlasma)).thenReturn(bloodTestingRuleResult);
    when(donationRepository.update(donationThatContainsPlasma)).thenReturn(donationThatContainsPlasma);
    
    // Test
    testBatchStatusChangeService.handleRelease(testBatch);
    
    // Verify
    verify(componentCRUDService).markComponentsBelongingToDonationAsUnsafeIfContainsPlasma(donationThatContainsPlasma);
    verify(componentCRUDService).updateComponentStatusesForDonation(donationThatContainsPlasma);
    assertThat(donationThatContainsPlasma.isReleased(), is(true));
  }

  @Test
  public void testHandleReleaseWithContainsPlasmaAndTTIPos_shouldMarkComponentsAsUnsafeAndUpdateComponentsStatuses() {
    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withId(FIRST_BLOOD_TEST_RESULT_ID)
            .withResult("POS")
            .withBloodTest(aBloodTest()
                .thatShouldFlagComponentsContainingPlasmaForDiscard()
                .withPositiveResults("POS,+")
                .build())
            .build(),
        aBloodTestResult()
            .withId(SECOND_BLOOD_TEST_RESULT_ID)
            .withResult("POS")
            .withBloodTest(aBloodTest()
                .withFlagComponentsForDiscard(true)
                .withPositiveResults("POS,+")
                .build())
            .build()
    );
    
    Donation donationThatContainsPlasma = aDonation()
        .withTTIStatus(TTIStatus.UNSAFE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodAbo("A") 
        .withBloodRh("+")
        .withBloodTestResults(bloodTestResults)
        .thatIsNotReleased()
        .build();
   
    TestBatch testBatch = aTestBatch()
        .withDonation(donationThatContainsPlasma)
        .build();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
    
    when(componentStatusCalculator.shouldComponentsBeDiscardedForTestResultsIfContainsPlasma(
        donationThatContainsPlasma.getBloodTestResults())).thenReturn(true);
    when(donationConstraintChecker.donationHasDiscrepancies(donationThatContainsPlasma)).thenReturn(false);
    when(donorDeferralStatusCalculator.shouldDonorBeDeferred(bloodTestResults)).thenReturn(false);
    when(bloodTestsService.executeTests(donationThatContainsPlasma)).thenReturn(bloodTestingRuleResult);
    when(donationRepository.update(donationThatContainsPlasma)).thenReturn(donationThatContainsPlasma);
    
    // Test
    testBatchStatusChangeService.handleRelease(testBatch);
    
    // Verify
    verify(componentCRUDService).markComponentsBelongingToDonorAsUnsafe(donationThatContainsPlasma.getDonor());
    verify(componentCRUDService).markComponentsBelongingToDonationAsUnsafeIfContainsPlasma(donationThatContainsPlasma);
    verify(componentCRUDService).updateComponentStatusesForDonation(donationThatContainsPlasma);
    assertThat(donationThatContainsPlasma.isReleased(), is(true));
  }
  
}
