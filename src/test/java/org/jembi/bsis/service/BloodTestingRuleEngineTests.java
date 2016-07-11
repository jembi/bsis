package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleResultSetMatcher.hasSameStateAsBloodTestingRuleResultSet;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.factory.BloodTestingRuleResultViewModelFactory;
import org.jembi.bsis.helpers.builders.BloodTestingRuleBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRuleResultSet;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class BloodTestingRuleEngineTests extends UnitTestSuite {

  @InjectMocks
  private BloodTestingRuleEngine bloodTestingRuleEngine;

  @Mock
  private BloodTestingRepository bloodTestingRepository;

  @Mock
  private BloodTestingRuleResultViewModelFactory bloodTestingRuleResultViewModelFactory;
  
  private List<BloodTestingRule> rules;
  private BloodTest hivBloodTest;
  private BloodTest hbvBloodTest;

  private void setupFixtures() {
    // Setup blood tests
    hivBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI)
        .withId(1L)
        .withPositiveResults("POS,NEG,NT")
        .withTestNameShort("HIV")
        .build();
    hbvBloodTest = aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI)
        .withId(2L)
        .withPositiveResults("POS,NEG,NT")
        .withTestNameShort("HBV")
        .build();
    
    // Setup rules
    BloodTestingRule rule1 = BloodTestingRuleBuilder.aBloodTestingRule().withDonationFieldChange(DonationField.TTISTATUS)
        .withPattern("NT").withNewInformation("INDETERMINATE").withBloodTestsIds("1").build();
    BloodTestingRule rule2 = BloodTestingRuleBuilder.aBloodTestingRule().withDonationFieldChange(DonationField.TTISTATUS)
        .withPattern("POS").withNewInformation("TTI_UNSAFE").withBloodTestsIds("1").build();
    BloodTestingRule rule3 = BloodTestingRuleBuilder.aBloodTestingRule().withDonationFieldChange(DonationField.TTISTATUS)
        .withPattern("NEG").withNewInformation("TTI_SAFE").withBloodTestsIds("1").build();
    BloodTestingRule rule4 = BloodTestingRuleBuilder.aBloodTestingRule().withDonationFieldChange(DonationField.TTISTATUS)
        .withPattern("NT").withNewInformation("INDETERMINATE").withBloodTestsIds("2").build();
    BloodTestingRule rule5 = BloodTestingRuleBuilder.aBloodTestingRule().withDonationFieldChange(DonationField.TTISTATUS)
        .withPattern("POS").withNewInformation("TTI_UNSAFE").withBloodTestsIds("2").build();
    BloodTestingRule rule6 = BloodTestingRuleBuilder.aBloodTestingRule().withDonationFieldChange(DonationField.TTISTATUS)
        .withPattern("NEG").withNewInformation("TTI_SAFE").withBloodTestsIds("2").build();
    rules = new ArrayList<>();
    rules.add(rule1);
    rules.add(rule2);
    rules.add(rule3);
    rules.add(rule4);
    rules.add(rule5);
    rules.add(rule6);
  }

  @Test
  public void testApplyBloodTestsWithNTAndNegTests_ttiStatusShouldBeIndeterminate() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(1L).withPackType(packType).build();

    // Setup existing test results for that donation
    BloodTestResult result1 = aBloodTestResult().withBloodTest(hivBloodTest).withResult("NT").withReEntryRequired(false).build();
    BloodTestResult result2 = aBloodTestResult().withBloodTest(hbvBloodTest).withResult("NEG").withReEntryRequired(false).build();
    Map<Long, BloodTestResult> resultsMap = new HashMap<Long, BloodTestResult>();
    resultsMap.put(1L, result1);
    resultsMap.put(2L, result2);

    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.INDETERMINATE);

    // Setup mocks
    when(bloodTestingRepository.getActiveBloodTestingRules()).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    // assume hiv and hbv are the only basic tty tests
    when(bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI))
        .thenReturn(Arrays.asList(hivBloodTest, hbvBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<Long, String>());

    // Verify last step of applyBloodTests before returning view model
    Mockito.verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }

  @Test
  public void testApplyBloodTestsWithNegTests_ttiStatusShouldBeSafe() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(1L).withPackType(packType).build();
    
    // Setup existing test results for that donation
    BloodTestResult result1 = aBloodTestResult().withBloodTest(hivBloodTest).withResult("NEG").withReEntryRequired(false).build();
    BloodTestResult result2 = aBloodTestResult().withBloodTest(hbvBloodTest).withResult("NEG").withReEntryRequired(false).build();
    Map<Long, BloodTestResult> resultsMap = new HashMap<Long, BloodTestResult>();
    resultsMap.put(1L, result1);
    resultsMap.put(2L, result2);
    
    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.TTI_SAFE);

    // Setup mocks
    when(bloodTestingRepository.getActiveBloodTestingRules()).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    // assume hiv and hbv are the only basic tty tests
    when(bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI)).thenReturn(Arrays.asList(hivBloodTest, hbvBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<Long, String>());

    // Verify last step of applyBloodTests before returning view model
    Mockito.verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }
  
  @Test
  public void testApplyBloodTestsWithPosAndNTTests_ttiStatusShouldBeUnsafe() throws Exception {

    // Setup fixtures
    setupFixtures();

    // Setup donation
    PackType packType = aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withId(1L).withPackType(packType).build();
    
    // Setup existing test results for that donation
    BloodTestResult result1 = aBloodTestResult().withBloodTest(hivBloodTest).withResult("POS").withReEntryRequired(false).build();
    BloodTestResult result2 = aBloodTestResult().withBloodTest(hbvBloodTest).withResult("NT").withReEntryRequired(false).build();
    Map<Long, BloodTestResult> resultsMap = new HashMap<Long, BloodTestResult>();
    resultsMap.put(1L, result1);
    resultsMap.put(2L, result2);
    
    // Setup expected rule engine result set
    BloodTestingRuleResultSet expectedBloodTestingRuleResultSet = new BloodTestingRuleResultSet(donation,
        new HashMap<String, String>(), new HashMap<String, String>(), resultsMap, rules);
    expectedBloodTestingRuleResultSet.setTtiStatus(TTIStatus.TTI_UNSAFE);

    // Setup mocks
    when(bloodTestingRepository.getActiveBloodTestingRules()).thenReturn(rules);
    when(bloodTestingRepository.getRecentTestResultsForDonation(donation.getId())).thenReturn(resultsMap);
    // assume hiv and hbv are the only basic tty tests
    when(bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI)).thenReturn(Arrays.asList(hivBloodTest, hbvBloodTest));

    // Apply test
    bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<Long, String>());

    // Verify last step of applyBloodTests before returning view model
    Mockito.verify(bloodTestingRuleResultViewModelFactory).createBloodTestResultViewModel(
        argThat(hasSameStateAsBloodTestingRuleResultSet(expectedBloodTestingRuleResultSet)));

  }

}
