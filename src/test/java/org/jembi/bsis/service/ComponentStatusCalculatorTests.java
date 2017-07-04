package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeBuilder.aComponentStatusChange;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aReturnReason;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.anUnsafeReason;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aReleasedTestBatch;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonType;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ComponentStatusCalculatorTests extends UnitTestSuite {

  private static final UUID DONATION_BATCH_ID = UUID.randomUUID();
  private static final UUID DONATION_ID = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a1");
  private static final UUID COMPONENT_ID = UUID.randomUUID();

  @InjectMocks
  private ComponentStatusCalculator componentStatusCalculator;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private DateGeneratorService dateGeneratorService;
  
  @Test
  public void testShouldComponentsBeDiscardedForTestResultsIfContainsPlasmaWithPositiveResult_shouldReturnTrue(){
    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withId(UUID.randomUUID())
            .withResult("POS")
            .withBloodTest(aBloodTest()
                .withFlagComponentsForDiscard(false)
                .thatShouldFlagComponentsContainingPlasmaForDiscard()
                .withPositiveResults("POS,+")
                .build())
            .build()
    );
    
    boolean result = componentStatusCalculator.shouldComponentsBeDiscardedForTestResultsIfContainsPlasma(bloodTestResults);
    
    assertThat(result, is(true));
  }
  
  @Test
  public void testShouldComponentsBeDiscardedForTestResultsIfContainsPlasmaWithNegativeResult_shouldReturnFalse(){
    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withId(UUID.randomUUID())
            .withResult("NEG")
            .withBloodTest(aBloodTest()
                .withFlagComponentsForDiscard(false)
                .thatShouldFlagComponentsContainingPlasmaForDiscard()
                .withPositiveResults("POS,+")
                .build())
            .build()
    );
    
    boolean result = componentStatusCalculator.shouldComponentsBeDiscardedForTestResultsIfContainsPlasma(bloodTestResults);
    
    assertThat(result, is(false));
  }  

  @Test
  public void testShouldComponentsBeDiscardedForTestResultsIfContainsPlasmaWithAllBloodTestResultsNotContainingPlasma_shouldReturnFalse(){
    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withId(UUID.randomUUID())
            .withResult("POS")
            .withBloodTest(aBloodTest()
                .withFlagComponentsForDiscard(false)
                .thatShouldNotFlagComponentsContainingPlasmaForDiscard()
                .withPositiveResults("POS,+")
                .build())
            .build()
    );
    
    boolean result = componentStatusCalculator.shouldComponentsBeDiscardedForTestResultsIfContainsPlasma(bloodTestResults);
    
    assertThat(result, is(false));
  }
  
  @Test
  public void testShouldComponentsBeDiscardedForTestResultsWithBloodTestNotFlaggedForDiscard_shouldReturnFalse() {

    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withId(UUID.randomUUID())
            .withResult("POS")
            .withBloodTest(aBloodTest()
                .withFlagComponentsForDiscard(false)
                .withPositiveResults("POS,+")
                .build())
            .build()
    );

    boolean result = componentStatusCalculator.shouldComponentsBeDiscardedForTestResults(bloodTestResults);

    assertThat(result, is(false));
  }

  @Test
  public void testShouldComponentsBeDiscardedForTestResultsWithBloodTestFlaggedForDiscardWithNegativeResult_shouldReturnFalse() {

    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withId(UUID.randomUUID())
            .withResult("NEG")
            .withBloodTest(aBloodTest()
                .withFlagComponentsForDiscard(true)
                .withPositiveResults("POS,+")
                .build())
            .build()
    );

    boolean result = componentStatusCalculator.shouldComponentsBeDiscardedForTestResults(bloodTestResults);

    assertThat(result, is(false));
  }

  @Test
  public void testShouldComponentsBeDiscardedForTestResultsWithBloodTestFlaggedForDiscardWithPositiveResult_shouldReturnTrue() {

    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withId(UUID.randomUUID())
            .withResult("POS")
            .withBloodTest(aBloodTest()
                .withFlagComponentsForDiscard(true)
                .withPositiveResults("POS,+")
                .build())
            .build()
    );

    boolean result = componentStatusCalculator.shouldComponentsBeDiscardedForTestResults(bloodTestResults);

    assertThat(result, is(true));
  }
  
  @Test
  public void testShouldComponentBeDiscardedForInvalidWeightLowWeight_shouldReturnTrue() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(320)
        .withDonation(aDonation()
            .withPackType(aPackType()
                .withLowVolumeWeight(350)
                .withMinWeight(400)
                .withMaxWeight(500).build())
            .build())
        .build();
    
    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(component);
    
    // verify
    assertThat("component should be discarded", discarded, is(true));
  }

  @Test
  public void testShouldComponentBeDiscardedForInvalidWeightHasLowWeightNoLowVolume_shouldReturnTrue() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(499)
        .withDonation(aDonation()
            .withPackType(aPackType()
                .withMinWeight(500)
                .withMaxWeight(550)
                .build())
            .build())
        .build();

    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(component);

    // verify
    assertThat("component should be discarded", discarded, is(true));
  }
   
  @Test
  public void testShouldComponentBeDiscardedForInvalidWeightHasInvalidWeightNoLowVolume_shouldReturnTrue() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(570)
        .withDonation(aDonation()
            .withPackType(aPackType()
                .withMinWeight(500)
                .withMaxWeight(550)
                .build())
            .build())
        .build();

    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(component);

    // verify
    assertThat("component should be discarded", discarded, is(true));
  }

  @Test
  public void testShouldComponentBeDiscardedForInvalidWeightHasValidWeightNoLowVolume_shouldReturnFalse() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(520)
        .withDonation(aDonation()
            .withPackType(aPackType()
                .withMinWeight(500)
                .withMaxWeight(550)
                .build())
            .build())
        .build();

    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(component);

    // verify
    assertThat("component should be discarded", discarded, is(false));
  }
  
  @Test
  public void testShouldComponentBeDiscardedForInvalidWeightHighWeight_shouldReturnTrue() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(520)
        .withDonation(aDonation().withPackType(aPackType().withLowVolumeWeight(400).withMinWeight(420).withMaxWeight(500).build()).build())
        .build();
    
    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(component);
    
    // verify
    assertThat("component should be discarded", discarded, is(true));
  }

  @Test
  public void testShouldComponentBeDiscardedForLowWeightHasPlasmaMoreThanLowVolumeWeightMoreThanMinWeight_shouldReturnFalse() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(410)
        .withComponentType(aComponentType().thatContainsPlasma().build())
        .withDonation(aDonation()
            .withPackType(aPackType()
                .withLowVolumeWeight(370)
                .withMinWeight(400)
                .withMaxWeight(500)
                .build())
            .build())
        .build();
    
    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForLowWeight(component);
    
    // verify
    assertThat("component should be discarded", discarded, is(false));
  }
  
  @Test
  public void testShouldComponentBeDiscardedForLowWeightHasPlasmaMoreThanLowVolumeWeightLessThanMinWeight_shouldReturnTrue() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(380)
        .withComponentType(aComponentType().thatContainsPlasma().build())
        .withDonation(aDonation()
            .withPackType(aPackType()
                .withLowVolumeWeight(370)
                .withMinWeight(400)
                .withMaxWeight(500)
                .build())
            .build())
        .build();
    
    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForLowWeight(component);
    
    // verify
    assertThat("component should be discarded", discarded, is(true));
  }
  
  @Test
  public void testShouldComponentBeDiscardedForLowWeightHasPlasmaEqualToLowVolumeWeightLessThanMinWeight_shouldReturnTrue() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(370)
        .withComponentType(aComponentType().thatContainsPlasma().build())
        .withDonation(aDonation()
            .withPackType(aPackType()
                .withLowVolumeWeight(370)
                .withMinWeight(400)
                .withMaxWeight(500)
                .build())
            .build())
        .build();
    
    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForLowWeight(component);
    
    // verify
    assertThat("component should be discarded", discarded, is(true));
  }
  
  @Test
  public void testShouldComponentBeDiscardedForLowWeightHasPlasmaEqualToMinWeight_shouldReturnFalse() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(400)
        .withComponentType(aComponentType().thatContainsPlasma().build())
        .withDonation(aDonation()
            .withPackType(aPackType()
                .withLowVolumeWeight(370)
                .withMinWeight(400)
                .withMaxWeight(500)
                .build())
            .build())
        .build();
    
    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForLowWeight(component);
    
    // verify
    assertThat("component should be discarded", discarded, is(false));
  }

  @Test
  public void testShouldComponentBeDiscardedForInvalidWeightHasPlasmaLessThanLowVolumeWeightLessThanMinWeight_shouldReturnTrue() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(330)
        .withComponentType(aComponentType().thatContainsPlasma().build())
        .withDonation(aDonation()
            .withPackType(aPackType()
                .withLowVolumeWeight(340)
                .withMinWeight(350)
                .withMaxWeight(500)
                .build())
            .build())
        .build();

    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(component);

    // verify
    assertThat("component should be discarded", discarded, is(true));
  }

  @Test
  public void testShouldComponentBeDiscardedForInvalidWeightNoPlasmaMoreThanLowVolumeWeightMoreThanMinWeight_shouldReturnFalse() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(410)
        .withComponentType(aComponentType().thatDoesntContainsPlasma().build())
        .withDonation(aDonation()
            .withPackType(aPackType()
                .withLowVolumeWeight(370)
                .withMinWeight(400)
                .withMaxWeight(500)
                .build())
            .build())
        .build();
    
    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(component);
    
    // verify
    assertThat("component should be discarded", discarded, is(false));
  }
  
  @Test
  public void testShouldComponentBeDiscardedForInvalidWeightNoPlasmaLessThanLowVolumeWeightLessThanMinWeight_shouldReturnTrue() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(330)
        .withComponentType(aComponentType().thatDoesntContainsPlasma().build())
        .withDonation(aDonation()
            .withPackType(aPackType()
                .withLowVolumeWeight(340)
                .withMinWeight(350)
                .withMaxWeight(500)
                .build())
            .build())
        .build();
    
    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(component);
    
    // verify
    assertThat("component should be discarded", discarded, is(true));
  }

  @Test
  public void testShouldComponentBeDiscardedForWeight_shouldReturnFalse() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(420)
        .withDonation(aDonation().withPackType(aPackType().withLowVolumeWeight(400).withMinWeight(410).withMaxWeight(500).build()).build())
        .build();
    
    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(component);
    
    // verify
    assertThat("component shouldn't be discarded", discarded, is(false));
  }

  @Test
  public void testShouldComponentBeDiscardedForInvalidWeightChildComponent_shouldReturnFalse() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withParentComponent(aComponent().build())
        .withWeight(420)
        .withDonation(aDonation().withPackType(aPackType().withLowVolumeWeight(400).withMaxWeight(500).build()).build())
        .build();

    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(component);

    // verify
    assertThat("component shouldn't be discarded", discarded, is(false));
  }

  @Test
  public void testShouldComponentBeDiscardedForLowWeightChildComponent_shouldReturnFalse() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withParentComponent(aComponent().build())
        .withWeight(420)
        .withDonation(aDonation().withPackType(aPackType().withLowVolumeWeight(400).withMaxWeight(500).build()).build())
        .build();

    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForLowWeight(component);

    // verify
    assertThat("component shouldn't be discarded", discarded, is(false));
  }
  
  @Test
  public void testShouldComponentBeDiscardedForLowWeightHasNoPlasmaWeightMoreThanLowWeightLessThanMinWeight_shouldReturnFalse() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withComponentType(aComponentType().thatDoesntContainsPlasma().build())
        .withWeight(440)
        .withDonation(aDonation()
            .withPackType(aPackType()
                .withLowVolumeWeight(400)
                .withMinWeight(450)
                .withMaxWeight(500)
                .build())
            .build())
        .build();

    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForLowWeight(component);

    // verify
    assertThat("component shouldn't be discarded", discarded, is(false));
  }
  
  @Test
  public void testShouldComponentBeDiscardedForLowWeightNoLowWeight_shouldReturnFalse() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withParentComponent(aComponent().build())
        .withWeight(420)
        .withDonation(aDonation()
            .withPackType(aPackType()
                .withMinWeight(450)
                .withMaxWeight(500)
                .build())
            .build())
        .build();

    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForLowWeight(component);

    // verify
    assertThat("component shouldn't be discarded", discarded, is(false));
  }
  
  @Test(expected=java.lang.IllegalStateException.class)
  public void testShouldComponentBeDiscardedForInvalidWeightNoLowMinAndMaxWeight_shouldThrowAnException() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID)
        .withWeight(420)
        .withDonation(aDonation().withPackType(aPackType().withLowVolumeWeight(400).build()).build())
        .build();
    
    // SUT
    componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(component);
  }

  @Test
  public void testUpdateComponentStatusProcessed_shouldNotChange() throws Exception {
    // set up data
    Component component = aComponent().withId(COMPONENT_ID).withStatus(ComponentStatus.PROCESSED).build();
    
    // SUT
    componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", component.getStatus(), is(ComponentStatus.PROCESSED));
  }
  
  @Test
  public void testUpdateComponentStatusDiscarded_shouldNotChange() throws Exception {
    // set up data
    Component component = aComponent().withId(COMPONENT_ID).withStatus(ComponentStatus.DISCARDED).build();
    
    // SUT
    componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", component.getStatus(), is(ComponentStatus.DISCARDED));
  }
  
  @Test
  public void testUpdateComponentStatusIssued_shouldNotChange() throws Exception {
    // set up data
    Component component = aComponent().withId(COMPONENT_ID).withStatus(ComponentStatus.ISSUED).build();
    
    // SUT
    componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", component.getStatus(), is(ComponentStatus.ISSUED));
  }
  
  @Test
  public void testUpdateComponentStatusUsed_shouldNotChange() throws Exception {
    // set up data
    Component component = aComponent().withId(COMPONENT_ID).withStatus(ComponentStatus.TRANSFUSED).build();
    
    // SUT
    componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", component.getStatus(), is(ComponentStatus.TRANSFUSED));
  }

  @Test
  public void testUpdateComponentStatusWithNoInitialStatus_shouldChangeStatusToQuarantined() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .build();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(null)
        .withExpiresOn(new DateTime().plusDays(3).toDate())
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantined_shouldNotChangeStatus() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedBloodGroupingMatch_shouldNotChangeStatus() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedBloodGroupingPendingTests_shouldNotChangeStatus() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.PENDING_TESTS)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NO_MATCH)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedBloodGroupingPendingTestsTTISafe_shouldNotChangeStatus() throws Exception {
    // set up data
    TestBatch testBatch = aReleasedTestBatch().withId(UUID.randomUUID()).build();
    DonationBatch donationBatch = aDonationBatch().withId(DONATION_BATCH_ID).build();
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.PENDING_TESTS)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NO_MATCH)
        .withTTIStatus(TTIStatus.SAFE)
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedOpenTestBatch_shouldNotChangeStatus() throws Exception {
    // set up data
    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN).withId(UUID.randomUUID()).build();
    DonationBatch donationBatch = aDonationBatch().withId(DONATION_BATCH_ID).build();
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.SAFE)
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedWithDiscrepancies_shouldNotChangeStatus() throws Exception {
    // set up data
    TestBatch testBatch = aReleasedTestBatch().withId(UUID.randomUUID()).build();
    DonationBatch donationBatch = aDonationBatch().withId(DONATION_BATCH_ID).build();
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.SAFE)
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedBloodGroupingCompleteTTISafe_shouldChangeStatusToAvailable() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is AVAILABLE", component.getStatus(), is(ComponentStatus.AVAILABLE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantined_shouldChangeStatusToExpired() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, -10);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is EXPIRED", component.getStatus(), is(ComponentStatus.EXPIRED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedWithIneligibleDonor_shouldChangeStatusToUnsafe() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .withIneligibleDonor(true)
        .build();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(new DateTime().plusDays(10).toDate())
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedWithUnconfirmedBloodGroupIndeterminate_shouldChangeStatusToUnsafe() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.INDETERMINATE) // Unconfirmed blood group
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .build();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(new DateTime().plusDays(10).toDate())
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedWithUnconfirmedBloodGroupNoTypeDetermined_shouldChangeStatusToUnsafe() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED) // Unconfirmed blood group
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .build();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(new DateTime().plusDays(10).toDate())
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusUnsafe_shouldNotChangeStatusToExpired() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, -10);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.UNSAFE)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(false));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedBloodGroupingCompleteTTIUnSafe_shouldChangeStatusToUnsafe() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.UNSAFE)
        .thatIsReleased()
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentWithIndeterminateTTI_shouldChangeStatusToUnsafe() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.INDETERMINATE)
        .thatIsReleased()
        .build();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(new DateTime().plusDays(90).toDate())
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedBloodGroupingCompleteOldStatusTTIUnSafe_shouldNotChangeStatus() throws Exception {
    // set up data
    TestBatch testBatch = aReleasedTestBatch().withId(UUID.randomUUID()).build();
    DonationBatch donationBatch = aDonationBatch().withId(DONATION_BATCH_ID).build();
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.SAFE) // TTI says safe, but the component status in UNSAFE
        .withDonationBatch(donationBatch)
        .withTestBatch(testBatch)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.UNSAFE)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is still UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedHasUnsafeComponentStatusChange_shouldChangeStatusToUnsafe() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    ComponentStatusChange statusChange = aComponentStatusChange().withStatusChangedOn(new Date()).withStatusChangeReason(anUnsafeReason().build()).build();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .withComponentStatusChange(statusChange)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedHasReturnedComponentStatusChange_shouldChangeStatusToAvailable() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    ComponentStatusChange statusChange = aComponentStatusChange().withStatusChangedOn(new Date()).withStatusChangeReason(aReturnReason().build()).build();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .withComponentStatusChange(statusChange)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is AVAILABLE", component.getStatus(), is(ComponentStatus.AVAILABLE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedHasDeletedUnsafeWeightComponentStatusChange_shouldChangeStatusToAvailable() throws Exception {
    // set up data
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.SAFE)
        .thatIsReleased()
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    ComponentStatusChangeReason statusChangeReason = anUnsafeReason().withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.INVALID_WEIGHT).build();
    ComponentStatusChange statusChange = aComponentStatusChange().withStatusChangedOn(new Date()).withStatusChangeReason(statusChangeReason).thatIsDeleted().build();
    Component component = aComponent().withId(COMPONENT_ID)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .withComponentStatusChange(statusChange)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is AVAILABLE", component.getStatus(), is(ComponentStatus.AVAILABLE));
  }
  
  @Test
  public void testGetDaysToExpire_shouldReturnZeroDaysToExpire() {

    // set up data
    Date today = new DateTime().toDate();
    Component component = aComponent().withExpiresOn(today).build();

    // set up mocks
    when(dateGeneratorService.generateDate()).thenReturn(today);

    // verify
    assertThat(0, comparesEqualTo(componentStatusCalculator.getDaysToExpire(component)));
  }

  @Test
  public void testGetDaysToExpire_shouldReturnOneDayToExpire() {

    // set up data
    Date today = new Date();
    DateTime expiresOn = new DateTime(today);
    expiresOn = expiresOn.plusDays(1);
    Component component = aComponent().withExpiresOn(expiresOn.toDate()).build();

    //setup mocks
    when(dateGeneratorService.generateDate()).thenReturn(today);

    // verify
    assertThat(1, comparesEqualTo(componentStatusCalculator.getDaysToExpire(component)));
  }

  @Test
  public void testGetDaysToExpire_shouldReturnMinusOneDayToExpire() {

    // set up
    Date today = new Date();
    DateTime expiresOn = new DateTime(today);
    expiresOn = expiresOn.minusDays(1);
    Component component = aComponent().withExpiresOn(expiresOn.toDate()).build();;

    // set up mocks
    when(dateGeneratorService.generateDate()).thenReturn(today);

    // verify
    assertThat(-1, comparesEqualTo(componentStatusCalculator.getDaysToExpire(component)));
  }

  @Test
  public void testGetDaysToExpire_shouldReturnOneHundredDaysToExpire() {

    // set up data
    Date today = new Date();
    DateTime expiresOn = new DateTime(today);
    expiresOn = expiresOn.plusDays(100);
    Component component = aComponent().withExpiresOn(expiresOn.toDate()).build();;

    // set up mocks
    when(dateGeneratorService.generateDate()).thenReturn(today);

    // verify
    assertThat(100, comparesEqualTo(componentStatusCalculator.getDaysToExpire(component)));
  }

  @Test
  public void testGetDaysToExpire_shouldReturnMinusOneForOneHundredDaysAfterExpiryDate() {

    // set up data
    Date today = new Date();
    DateTime expiresOn = new DateTime(today);
    expiresOn = expiresOn.minusDays(100);
    Component component = aComponent().withExpiresOn(expiresOn.toDate()).build();

    // set up mocks
    when(dateGeneratorService.generateDate()).thenReturn(today);;

    // verify
    assertThat(-1, comparesEqualTo(componentStatusCalculator.getDaysToExpire(component)));
  }

  @Test
  public void testGetDaysToExpire_shouldReturnMinusOneForSameExpiryDateEarlierExpiryTime() {

    // set up data
    Date today = new Date();
    DateTime expiresOn = new DateTime(today);
    expiresOn = expiresOn.minusMillis(5);
    Component component = aComponent().withExpiresOn(expiresOn.toDate()).build();

    //set up mocks
    when(dateGeneratorService.generateDate()).thenReturn(today);

    // verify
    assertThat(-1, comparesEqualTo(componentStatusCalculator.getDaysToExpire(component)));
  }

  @Test
  public void testGetDaysToExpire_shouldReturnZeroForSameExpiryDateLaterExpiryTime() {

    // set up data and mocks
    Date today = new Date();
    DateTime expiresOn = new DateTime(today);
    expiresOn = expiresOn.plusMillis(5);
    Component component = aComponent().withExpiresOn(expiresOn.toDate()).build();

    when(dateGeneratorService.generateDate()).thenReturn(today);

    // verify
    assertThat(0, comparesEqualTo(componentStatusCalculator.getDaysToExpire(component)));
  }
}

