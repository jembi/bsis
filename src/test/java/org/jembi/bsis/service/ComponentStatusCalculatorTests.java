package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeBuilder.aComponentStatusChange;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aReturnReason;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.anUnsafeReason;
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

import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTypingMatchStatus;
import org.jembi.bsis.repository.bloodtesting.BloodTypingStatus;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ComponentStatusCalculatorTests extends UnitTestSuite {

  @InjectMocks
  private ComponentStatusCalculator componentStatusCalculator;
  @Mock
  private DonationRepository donationRepository;
  
  @Test
  public void testShouldComponentsBeDiscardedForTestResultsIfContainsPlasmaWithPositiveResult_shouldReturnTrue(){
    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withId(9L)
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
            .withId(9L)
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
            .withId(9L)
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
            .withId(9L)
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
            .withId(9L)
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
            .withId(9L)
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
  public void testShouldComponentBeDiscardedForWeightLowWeight_shouldReturnTrue() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(1L)
        .withWeight(320)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .build();
    
    // set up mocks
    
    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForWeight(component);
    
    // verify
    assertThat("component should be discarded", discarded, is(true));
  }
  
  @Test
  public void testShouldComponentBeDiscardedForWeightHighWeight_shouldReturnTrue() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(1L)
        .withWeight(520)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .build();
    
    // set up mocks
    
    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForWeight(component);
    
    // verify
    assertThat("component should be discarded", discarded, is(true));
  }
  
  @Test
  public void testShouldComponentBeDiscardedForWeight_shouldReturnFalse() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(1L)
        .withWeight(420)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .build();
    
    // set up mocks
    
    // SUT
    boolean discarded = componentStatusCalculator.shouldComponentBeDiscardedForWeight(component);
    
    // verify
    assertThat("component shouldn't be discarded", discarded, is(false));
  }
  
  @Test(expected=java.lang.IllegalStateException.class)
  public void testShouldComponentBeDiscardedForWeightNoMinAndMaxWeight_shouldThrowAnException() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(1L)
        .withWeight(420)
        .withDonation(aDonation().withPackType(aPackType().build()).build())
        .build();
    
    // set up mocks
    
    // SUT
    componentStatusCalculator.shouldComponentBeDiscardedForWeight(component);
  }

  @Test(expected=java.lang.IllegalStateException.class)
  public void testShouldComponentBeDiscardedForWeightNoMinWeight_shouldThrowAnException() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(1L)
        .withWeight(420)
        .withDonation(aDonation().withPackType(aPackType().withMaxWeight(500).build()).build())
        .build();
    
    // set up mocks
    
    // SUT
    componentStatusCalculator.shouldComponentBeDiscardedForWeight(component);
  }
  
  @Test(expected=java.lang.IllegalStateException.class)
  public void testShouldComponentBeDiscardedForWeightNoMaxWeight_shouldThrowAnException() throws Exception {
    // set up data
    Component component = aComponent()
        .withId(1L)
        .withWeight(420)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).build()).build())
        .build();
    
    // set up mocks
    
    // SUT
    componentStatusCalculator.shouldComponentBeDiscardedForWeight(component);
  }

  @Test
  public void testUpdateComponentStatusProcessed_shouldNotChange() throws Exception {
    // set up data
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.PROCESSED).build();
    
    // set up mocks
    
    // SUT
    componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", component.getStatus(), is(ComponentStatus.PROCESSED));
  }
  
  @Test
  public void testUpdateComponentStatusDiscarded_shouldNotChange() throws Exception {
    // set up data
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.DISCARDED).build();
    
    // set up mocks
    
    // SUT
    componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", component.getStatus(), is(ComponentStatus.DISCARDED));
  }
  
  @Test
  public void testUpdateComponentStatusIssued_shouldNotChange() throws Exception {
    // set up data
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.ISSUED).build();
    
    // set up mocks
    
    // SUT
    componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", component.getStatus(), is(ComponentStatus.ISSUED));
  }
  
  @Test
  public void testUpdateComponentStatusUsed_shouldNotChange() throws Exception {
    // set up data
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.USED).build();
    
    // set up mocks
    
    // SUT
    componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", component.getStatus(), is(ComponentStatus.USED));
  }
  
  @Test
  public void testUpdateComponentStatusSplit_shouldNotChange() throws Exception {
    // set up data
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.SPLIT).build();
    
    // set up mocks
    
    // SUT
    componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", component.getStatus(), is(ComponentStatus.SPLIT));
  }
  
  @Test
  public void testUpdateComponentStatusWithNoInitialStatus_shouldChangeStatusToQuarantined() throws Exception {
    // set up data
    long donationId = 1234L;
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .build();
    Component component = aComponent().withId(1L)
        .withStatus(null)
        .withExpiresOn(new DateTime().plusDays(3).toDate())
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantined_shouldNotChangeStatus() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1234);
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedBloodGroupingMatch_shouldNotChangeStatus() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1234);
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedBloodGroupingPendingTests_shouldNotChangeStatus() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1234);
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.PENDING_TESTS)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NO_MATCH)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedBloodGroupingPendingTestsTTISafe_shouldNotChangeStatus() throws Exception {
    // set up data
    TestBatch testBatch = aReleasedTestBatch().withId(1L).build();
    DonationBatch donationBatch = aDonationBatch().withId(1L).withTestBatch(testBatch).build();
    Long donationId = Long.valueOf(1234);
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.PENDING_TESTS)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NO_MATCH)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withDonationBatch(donationBatch)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedOpenTestBatch_shouldNotChangeStatus() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1234);
    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN).withId(1L).build();
    DonationBatch donationBatch = aDonationBatch().withId(1L).withTestBatch(testBatch).build();
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withDonationBatch(donationBatch)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedWithDiscrepancies_shouldNotChangeStatus() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1234);
    TestBatch testBatch = aReleasedTestBatch().withId(1L).build();
    DonationBatch donationBatch = aDonationBatch().withId(1L).withTestBatch(testBatch).build();
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withDonationBatch(donationBatch)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is QUARANTINED", component.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedBloodGroupingCompleteTTISafe_shouldChangeStatusToAvailable() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1234);
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is AVAILABLE", component.getStatus(), is(ComponentStatus.AVAILABLE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantined_shouldChangeStatusToExpired() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1234);
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, -10);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is EXPIRED", component.getStatus(), is(ComponentStatus.EXPIRED));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedWithIneligibleDonor_shouldChangeStatusToUnsafe() throws Exception {
    // set up data
    Long donationId = 113L;
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .withIneligibleDonor(true)
        .build();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(new DateTime().plusDays(10).toDate())
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedWithUnconfirmedBloodGroupIndeterminate_shouldChangeStatusToUnsafe() throws Exception {
    // set up data
    Long donationId = 113L;
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.INDETERMINATE) // Unconfirmed blood group
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .build();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(new DateTime().plusDays(10).toDate())
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedWithUnconfirmedBloodGroupNoTypeDetermined_shouldChangeStatusToUnsafe() throws Exception {
    // set up data
    Long donationId = 113L;
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED) // Unconfirmed blood group
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .build();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(new DateTime().plusDays(10).toDate())
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusUnsafe_shouldNotChangeStatusToExpired() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1234);
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, -10);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.UNSAFE)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(false));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedBloodGroupingCompleteTTIUnSafe_shouldChangeStatusToUnsafe() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1234);
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.TTI_UNSAFE)
        .thatIsReleased()
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentWithIndeterminateTTI_shouldChangeStatusToUnsafe() throws Exception {
    // set up data
    long donationId = 1234;
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.INDETERMINATE)
        .thatIsReleased()
        .build();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(new DateTime().plusDays(90).toDate())
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedBloodGroupingCompleteOldStatusTTIUnSafe_shouldNotChangeStatus() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1234);
    TestBatch testBatch = aReleasedTestBatch().withId(1L).build();
    DonationBatch donationBatch = aDonationBatch().withId(1L).withTestBatch(testBatch).build();
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.TTI_SAFE) // TTI says safe, but the component status in UNSAFE
        .withDonationBatch(donationBatch)
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.UNSAFE)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is not changed", statusChanged, is(false));
    assertThat("status is still UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedHasUnsafeComponentStatusChange_shouldChangeStatusToUnsafe() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1234);
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    ComponentStatusChange statusChange = aComponentStatusChange().withStatusChangedOn(new Date()).withStatusChangeReason(anUnsafeReason().build()).build();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .withComponentStatusChange(statusChange)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is UNSAFE", component.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedHasReturnedComponentStatusChange_shouldChangeStatusToAvailable() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1234);
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    ComponentStatusChange statusChange = aComponentStatusChange().withStatusChangedOn(new Date()).withStatusChangeReason(aReturnReason().build()).build();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .withComponentStatusChange(statusChange)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is AVAILABLE", component.getStatus(), is(ComponentStatus.AVAILABLE));
  }
  
  @Test
  public void testUpdateComponentStatusQuarantinedHasDeletedUnsafeWeightComponentStatusChange_shouldChangeStatusToAvailable() throws Exception {
    // set up data
    Long donationId = Long.valueOf(1234);
    Donation donation = aDonation()
        .withId(donationId)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .thatIsReleased()
        .build();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 90);
    Date expiresOn = cal.getTime();
    ComponentStatusChangeReason statusChangeReason = anUnsafeReason().withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.INVALID_WEIGHT).build();
    ComponentStatusChange statusChange = aComponentStatusChange().withStatusChangedOn(new Date()).withStatusChangeReason(statusChangeReason).thatIsDeleted().build();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.QUARANTINED)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .withComponentStatusChange(statusChange)
        .build();
    
    // set up mocks
    when(donationRepository.findDonationById(donationId)).thenReturn(donation);
    
    // SUT
    boolean statusChanged = componentStatusCalculator.updateComponentStatus(component);
    
    // verify
    assertThat("status is changed", statusChanged, is(true));
    assertThat("status is AVAILABLE", component.getStatus(), is(ComponentStatus.AVAILABLE));
  }
}
