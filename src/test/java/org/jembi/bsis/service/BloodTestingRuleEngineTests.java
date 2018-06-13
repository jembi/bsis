package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleBuilder.aBloodTestingRule;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleResultSetMatcher.hasSameStateAsBloodTestingRuleResultSet;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.factory.BloodTestingRuleResultViewModelFactory;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRuleResultSet;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.repository.BloodTestingRuleRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class BloodTestingRuleEngineTests extends UnitTestSuite {
  private static final UUID DONATION_ID = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837b1");

  @InjectMocks
  private BloodTestingRuleEngine bloodTestingRuleEngine;

  @Mock
  private BloodTestRepository bloodTestRepository;

  @Mock
  private BloodTestingRepository bloodTestingRepository;

  @Mock
  private BloodTestingRuleRepository bloodTestingRuleRepository;

  @Mock
  private BloodTestingRuleResultViewModelFactory bloodTestingRuleResultViewModelFactory;
  
  private List<BloodTestingRule> rules;
  private BloodTest hivBloodTest;
  private BloodTest hbvBloodTest;
  private BloodTest aboBloodTest;
  private BloodTest rhBloodTest;
  private BloodTest aboRepeatBloodTest;
  private BloodTest rhRepeatBloodTest;
  private BloodTest titreBloodTest;

  private void setupFixtures() {
    // Setup blood tests
    hivBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI)
        .withId(UUID.randomUUID())
        .withPositiveResults("POS,NEG,NT")
        .withTestNameShort("HIV")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .build();
    hbvBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI)
        .withId(UUID.randomUUID())
        .withPositiveResults("POS,NEG,NT")
        .withTestNameShort("HBV")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .build();
    aboBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .withId(UUID.randomUUID())
        .withValidResults("A,B,AB,O,NT")
        .withTestNameShort("ABO")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .build();
    rhBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .withId(UUID.randomUUID())
        .withValidResults("POS,NEG,NT")
        .withTestNameShort("Rh")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .build();
    aboRepeatBloodTest = aBloodTest().withBloodTestType(BloodTestType.REPEAT_BLOODTYPING)
        .withId(UUID.randomUUID())
        .withValidResults("A,B,AB,O,NT")
        .withTestNameShort("ABO Repeat")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .build();
    rhRepeatBloodTest = aBloodTest().withBloodTestType(BloodTestType.REPEAT_BLOODTYPING)
        .withId(UUID.randomUUID())
        .withValidResults("POS,NEG,NT")
        .withTestNameShort("Rh Repeat")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .build();
    titreBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .withId(UUID.randomUUID())
        .withValidResults("HIGH,LOW,NT")
        .withTestNameShort("Titre")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .build();
    
    // Setup rules
    rules = new ArrayList<>();
    
    // TTI
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.TTISTATUS)
         .withPattern("NT").withNewInformation("INDETERMINATE").withBloodTest(hivBloodTest).build());
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.TTISTATUS)
         .withPattern("POS").withNewInformation("UNSAFE").withBloodTest(hivBloodTest).build());
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.TTISTATUS)
         .withPattern("NEG").withNewInformation("SAFE").withBloodTest(hivBloodTest).build());
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.TTISTATUS)
         .withPattern("NT").withNewInformation("INDETERMINATE").withBloodTest(hbvBloodTest).build());
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.TTISTATUS)
         .withPattern("POS").withNewInformation("UNSAFE").withBloodTest(hbvBloodTest).build());
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.TTISTATUS)
        .withPattern("NEG").withNewInformation("SAFE").withBloodTest(hbvBloodTest).build());
    
    // ABO
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.BLOODABO)
         .withPattern("O").withNewInformation("O").withBloodTest(aboBloodTest).withPendingBloodTest(aboRepeatBloodTest).build());
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.BLOODABO)
         .withPattern("A").withNewInformation("A").withBloodTest(aboBloodTest).withPendingBloodTest(aboRepeatBloodTest).build());
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.BLOODABO)
         .withPattern("B").withNewInformation("B").withBloodTest(aboBloodTest).withPendingBloodTest(aboRepeatBloodTest).build());
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.BLOODABO)
         .withPattern("AB").withNewInformation("AB").withBloodTest(aboBloodTest).withPendingBloodTest(aboRepeatBloodTest).build());
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.BLOODABO)
         .withPattern("NT").withNewInformation("").withBloodTest(aboBloodTest).withPendingBloodTest(aboRepeatBloodTest).build());
    
    // RH
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.BLOODRH)
         .withPattern("POS").withNewInformation("+").withBloodTest(rhBloodTest).withPendingBloodTest(rhRepeatBloodTest).build());
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.BLOODRH)
         .withPattern("NEG").withNewInformation("-").withBloodTest(rhBloodTest).withPendingBloodTest(rhRepeatBloodTest).build());
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.BLOODRH)
         .withPattern("NT").withNewInformation("").withBloodTest(rhBloodTest).withPendingBloodTest(rhRepeatBloodTest).build());

    // Titre
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.TITRE)
        .withPattern("HIGH").withNewInformation("HIGH").withBloodTest(titreBloodTest).build());
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.TITRE)
        .withPattern("LOW").withNewInformation("LOW").withBloodTest(titreBloodTest).build());
    rules.add(aBloodTestingRule().withDonationFieldChanged(DonationField.TITRE)
        .withPattern("NT").withNewInformation("").withBloodTest(titreBloodTest).build());
  }

  @Test
  public void testApplyBloodTestsWithNTAndNegTests_ttiStatusShouldBeIndeterminate() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();

    // Setup existing test results for that donation
    BloodTestResult result1 = aBloodTestResult().withBloodTest(hivBloodTest).withResult("NT").withReEntryRequired(false).build();
    BloodTestResult result2 = aBloodTestResult().withBloodTest(hbvBloodTest).withResult("NEG").withReEntryRequired(false).build();
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(hivBloodTest.getId(), result1);
    resultsMap.put(hbvBloodTest.getId(), result2);

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.INDETERMINATE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    // assume hiv and hbv are the only basic tty tests
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI))
        .thenReturn(Arrays.asList(hivBloodTest, hbvBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }

  @Test
  public void testApplyBloodTestsWithNegTests_ttiStatusShouldBeSafe() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();
    
    // Setup existing test results for that donation
    BloodTestResult result1 = aBloodTestResult().withBloodTest(hivBloodTest).withResult("NEG").withReEntryRequired(false).build();
    BloodTestResult result2 = aBloodTestResult().withBloodTest(hbvBloodTest).withResult("NEG").withReEntryRequired(false).build();
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(hivBloodTest.getId(), result1);
    resultsMap.put(hbvBloodTest.getId(), result2);
    
    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.SAFE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    // assume hiv and hbv are the only basic tty tests
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI)).thenReturn(Arrays.asList(hivBloodTest, hbvBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithPosAndNTTests_ttiStatusShouldBeUnsafe() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();
    
    // Setup existing test results for that donation
    BloodTestResult result1 = aBloodTestResult().withBloodTest(hivBloodTest).withResult("POS").withReEntryRequired(false).build();
    BloodTestResult result2 = aBloodTestResult().withBloodTest(hbvBloodTest).withResult("NT").withReEntryRequired(false).build();
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(hivBloodTest.getId(), result1);
    resultsMap.put(hbvBloodTest.getId(), result2);
    
    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.UNSAFE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    // assume hiv and hbv are the only basic tty tests
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI)).thenReturn(Arrays.asList(hivBloodTest, hbvBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithPosTests_ttiStatusShouldBeUnsafe() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();
    
    // Setup existing test results for that donation
    BloodTestResult result1 = aBloodTestResult().withBloodTest(hivBloodTest).withResult("POS").withReEntryRequired(false).build();
    BloodTestResult result2 = aBloodTestResult().withBloodTest(hbvBloodTest).withResult("POS").withReEntryRequired(false).build();
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(hivBloodTest.getId(), result1);
    resultsMap.put(hbvBloodTest.getId(), result2);
    
    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.UNSAFE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    // assume hiv and hbv are the only basic tty tests
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI)).thenReturn(Arrays.asList(hivBloodTest, hbvBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithNegAndPosTests_ttiStatusShouldBeUnsafe() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();
    
    // Setup existing test results for that donation
    BloodTestResult result1 = aBloodTestResult().withBloodTest(hivBloodTest).withResult("NEG").withReEntryRequired(false).build();
    BloodTestResult result2 = aBloodTestResult().withBloodTest(hbvBloodTest).withResult("POS").withReEntryRequired(false).build();
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(hivBloodTest.getId(), result1);
    resultsMap.put(hbvBloodTest.getId(), result2);
    
    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.UNSAFE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    // assume hiv and hbv are the only basic tty tests
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI)).thenReturn(Arrays.asList(hivBloodTest, hbvBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }

  @Test
  public void testApplyBloodTestsWithNTTests_ttiStatusShouldBeIndeterminate() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();

    // Setup existing test results for that donation
    BloodTestResult result1 =
        aBloodTestResult().withBloodTest(hivBloodTest).withResult("NT").withReEntryRequired(false).build();
    BloodTestResult result2 =
        aBloodTestResult().withBloodTest(hbvBloodTest).withResult("NT").withReEntryRequired(false).build();
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(hivBloodTest.getId(), result1);
    resultsMap.put(hbvBloodTest.getId(), result2);

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.INDETERMINATE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    // assume hiv and hbv are the only basic tty tests
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI))
        .thenReturn(Arrays.asList(hivBloodTest, hbvBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithABOOutcome_bloodTypingStatusShouldBeNotDone() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();

    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(aboBloodTest.getId(), aBloodTestResult().withBloodTest(aboBloodTest).withResult("A").withReEntryRequired(false).build());

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.addBloodAboChanges("A");

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }

  @Test
  public void testApplyBloodTestsWithNTABOTest_bloodTypingStatusShouldBeIndeterminate() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();

    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(aboBloodTest.getId(), aBloodTestResult().withBloodTest(aboBloodTest).withResult("NT").withReEntryRequired(false).build());
    resultsMap.put(rhBloodTest.getId(), aBloodTestResult().withBloodTest(rhBloodTest).withResult("POS").withReEntryRequired(false).build());

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.INDETERMINATE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    expectedBloodTestingRuleResultSet.addBloodAboChanges("");
    expectedBloodTestingRuleResultSet.addBloodRhChanges("+");

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithNTRhTest_bloodTypingStatusShouldBeIndeterminate() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();

    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(aboBloodTest.getId(), aBloodTestResult().withBloodTest(aboBloodTest).withResult("A").withReEntryRequired(false).build());
    resultsMap.put(rhBloodTest.getId(), aBloodTestResult().withBloodTest(rhBloodTest).withResult("NT").withReEntryRequired(false).build());

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.INDETERMINATE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    expectedBloodTestingRuleResultSet.addBloodAboChanges("A");
    expectedBloodTestingRuleResultSet.addBloodRhChanges("");

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithOnlyNTABOTest_bloodTypingStatusShouldBeNotDoneAndIndeterminate() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();

    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(aboBloodTest.getId(), aBloodTestResult().withBloodTest(aboBloodTest).withResult("NT").withReEntryRequired(false).build());

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.INDETERMINATE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.addBloodAboChanges("");

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithNTRhAndABOTest_bloodTypingStatusShouldBeIndeterminate() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();

    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(aboBloodTest.getId(), aBloodTestResult().withBloodTest(aboBloodTest).withResult("NT").withReEntryRequired(false).build());
    resultsMap.put(rhBloodTest.getId(), aBloodTestResult().withBloodTest(rhBloodTest).withResult("NT").withReEntryRequired(false).build());

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.INDETERMINATE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    expectedBloodTestingRuleResultSet.addBloodAboChanges("");
    expectedBloodTestingRuleResultSet.addBloodRhChanges("");

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithRepeatDonorABORh_bloodTypingStatusShouldBeComplete() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donor donor = aDonor().withId(UUID.randomUUID()).withBloodAbo("A").withBloodRh("+").build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).withDonor(donor).withBloodAbo("A").withBloodRh("+").build();

    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(aboBloodTest.getId(), aBloodTestResult().withBloodTest(aboBloodTest).withResult("A").withReEntryRequired(false).build());
    resultsMap.put(rhBloodTest.getId(), aBloodTestResult().withBloodTest(rhBloodTest).withResult("POS").withReEntryRequired(false).build());

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    expectedBloodTestingRuleResultSet.addBloodAboChanges("A");
    expectedBloodTestingRuleResultSet.addBloodRhChanges("+");

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }

  @Test
  public void testApplyBloodTestsWithRepeatDonorOnlyABO_bloodTypingStatusShouldBeNotDoneAndNoPendingTests() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donor donor = aDonor().withId(UUID.randomUUID()).withBloodAbo("A").withBloodRh("+").build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).withDonor(donor).build();

    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(aboBloodTest.getId(), aBloodTestResult().withBloodTest(aboBloodTest).withResult("A").withReEntryRequired(false).build());

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.addBloodAboChanges("A");

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithRepeatDonorABORh_bloodTypingStatusShouldBeAmbiguous() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donor donor = aDonor().withId(UUID.randomUUID()).withBloodAbo("A").withBloodRh("+").build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).withDonor(donor).withBloodAbo("B").withBloodRh("+").build();

    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(aboBloodTest.getId(), aBloodTestResult().withBloodTest(aboBloodTest).withResult("B").withReEntryRequired(false).build());
    resultsMap.put(rhBloodTest.getId(), aBloodTestResult().withBloodTest(rhBloodTest).withResult("POS").withReEntryRequired(false).build());

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    expectedBloodTestingRuleResultSet.addBloodAboChanges("B");
    expectedBloodTestingRuleResultSet.addBloodRhChanges("+");

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithFirstTimeDonorABORh_bloodTypingStatusShouldBeNoMatchPendingTests() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donor donor = aDonor().withId(UUID.randomUUID()).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).withDonor(donor).withBloodAbo("A").withBloodRh("+").build();

    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(aboBloodTest.getId(), aBloodTestResult().withBloodTest(aboBloodTest).withResult("A").withReEntryRequired(false).build());
    resultsMap.put(rhBloodTest.getId(), aBloodTestResult().withBloodTest(rhBloodTest).withResult("POS").withReEntryRequired(false).build());

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NO_MATCH);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.PENDING_TESTS);
    expectedBloodTestingRuleResultSet.addBloodAboChanges("A");
    expectedBloodTestingRuleResultSet.addBloodRhChanges("+");
    expectedBloodTestingRuleResultSet.setPendingAboTestsIds(Arrays.asList(aboRepeatBloodTest.getId()));
    expectedBloodTestingRuleResultSet.setPendingRhTestsIds(Arrays.asList(rhRepeatBloodTest.getId()));

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest));
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.REPEAT_BLOODTYPING)).thenReturn(Arrays.asList(aboRepeatBloodTest, rhRepeatBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithFirstTimeDonorABORh_bloodTypingStatusShouldBeMatchComplete() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donor donor = aDonor().withId(UUID.randomUUID()).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).withDonor(donor).withBloodAbo("A").withBloodRh("+").build();

    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(aboBloodTest.getId(), aBloodTestResult().withBloodTest(aboBloodTest).withResult("A").withReEntryRequired(false).build());
    resultsMap.put(rhBloodTest.getId(), aBloodTestResult().withBloodTest(rhBloodTest).withResult("POS").withReEntryRequired(false).build());
    resultsMap.put(aboRepeatBloodTest.getId(), aBloodTestResult().withBloodTest(aboRepeatBloodTest).withResult("A").withReEntryRequired(false).build());
    resultsMap.put(rhRepeatBloodTest.getId(), aBloodTestResult().withBloodTest(rhRepeatBloodTest).withResult("POS").withReEntryRequired(false).build());

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    expectedBloodTestingRuleResultSet.addBloodAboChanges("A");
    expectedBloodTestingRuleResultSet.addBloodRhChanges("+");

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest));
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.REPEAT_BLOODTYPING)).thenReturn(Arrays.asList(aboRepeatBloodTest, rhRepeatBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithFirstTimeDonorABORh_bloodTypingStatusShouldBeAmbiguous() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donor donor = aDonor().withId(UUID.randomUUID()).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).withDonor(donor).withBloodAbo("A").withBloodRh("+").build();

    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(aboBloodTest.getId(), aBloodTestResult().withBloodTest(aboBloodTest).withResult("A").withReEntryRequired(false).build());
    resultsMap.put(rhBloodTest.getId(), aBloodTestResult().withBloodTest(rhBloodTest).withResult("POS").withReEntryRequired(false).build());
    resultsMap.put(aboRepeatBloodTest.getId(), aBloodTestResult().withBloodTest(aboRepeatBloodTest).withResult("B").withReEntryRequired(false).build());
    resultsMap.put(rhRepeatBloodTest.getId(), aBloodTestResult().withBloodTest(rhRepeatBloodTest).withResult("POS").withReEntryRequired(false).build());

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    expectedBloodTestingRuleResultSet.addBloodAboChanges("A");
    expectedBloodTestingRuleResultSet.addBloodRhChanges("+");

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest));
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.REPEAT_BLOODTYPING)).thenReturn(Arrays.asList(aboRepeatBloodTest, rhRepeatBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithNoTypeDeterminedMatchStatus_ABOAndRhRulesShouldNotBeProcessed() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donor donor = aDonor().withId(UUID.randomUUID()).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).withDonor(donor)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED)
        .build();

    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(aboBloodTest.getId(), aBloodTestResult().withBloodTest(aboBloodTest).withResult("A").withReEntryRequired(false).build());
    resultsMap.put(rhBloodTest.getId(), aBloodTestResult().withBloodTest(rhBloodTest).withResult("POS").withReEntryRequired(false).build());

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }

  @Test
  public void testApplyBloodTestsWithTitreHighTest_titreShouldBeHigh() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();
    
    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(titreBloodTest.getId(), 
        aBloodTestResult().withBloodTest(titreBloodTest).withResult("HIGH").withReEntryRequired(false).build());
    
    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.addTitreChanges("HIGH");
    expectedBloodTestingRuleResultSet.setPendingAboTestsIds(new ArrayList<UUID>());
    expectedBloodTestingRuleResultSet.setPendingRhTestsIds(new ArrayList<UUID>());
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest, titreBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));
  }

  @Test
  public void testApplyBloodTestsWithTitreLowTest_titreShouldBeLow() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();
    
    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(titreBloodTest.getId(), 
        aBloodTestResult().withBloodTest(titreBloodTest).withResult("LOW").withReEntryRequired(false).build());
    
    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.addTitreChanges("LOW");
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest, titreBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));
  }

  @Test
  public void testApplyBloodTestsWithTitreNTTest_titreShouldBeEmpty() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(DONATION_ID).withPackType(packType).build();
    
    // Setup existing test results for that donation
    Map<UUID, BloodTestResult> resultsMap = new HashMap<>();
    resultsMap.put(titreBloodTest.getId(), 
        aBloodTestResult().withBloodTest(titreBloodTest).withResult("NT").withReEntryRequired(false).build());
    
    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<UUID, String>(), new HashMap<UUID, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.addTitreChanges("");
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    expectedBloodTestingRuleResultSet.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);

    // Setup mocks
    when(bloodTestingRuleRepository.getBloodTestingRules(false, false)).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(aboBloodTest, rhBloodTest, titreBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());

    // Verify last step of applyBloodTests before returning view model
    verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultFullViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));
  }
}
