package org.jembi.bsis.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jembi.bsis.factory.BloodTestingRuleResultViewModelFactory;
import org.jembi.bsis.model.bloodtesting.BloodTest;
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
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.repository.BloodTestingRuleRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BloodTestingRuleEngine {

  private static final Logger LOGGER = Logger.getLogger(BloodTestingRuleEngine.class);

  @Autowired
  private BloodTestRepository bloodTestRepository;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  @Autowired
  private BloodTestingRuleRepository bloodTestingRuleRepository;

  @Autowired
  private BloodTestingRuleResultViewModelFactory bloodTestingRuleResultViewModelFactory;

  /**
   * Apply blood typing rules to blood typing tests (combination of what is present in the database
   * and those passed as parameter.
   *
   * @param donation Blood Typing results for which collection
   * @param bloodTestResults map of blood typing test id to result. Only character allowed in the
   *        result. multiple characters should be mapped to negative/positive (TODO) Assume
   *        validation of results already done.
   * @return Result of applying the rules. The following values should be present in the map -
   *         bloodAbo (what changes should be made to blood abo after applying these rules) -
   *         bloodRh (what changes should be made to blood rh), - pendingTests (comma separated list
   *         of blood typing tests that must be done to determine the blood type), - testResults
   *         (map of blood typing test id to blood typing test either stored or those passed to this
   *         function or those already stored in the database), - bloodTypingStatus (enum
   *         BloodTypingStatus indicates if complete typing information is available), -
   *         storedTestResults (what blood typing results are actually stored in the database, a
   *         subset of testResults)
   */
  public BloodTestingRuleResult applyBloodTests(Donation donation, Map<UUID, String> bloodTestResults)
      throws IllegalArgumentException {

    if (!donation.getPackType().getTestSampleProduced()) {
      throw new IllegalArgumentException("Can't apply blood tests to a donation which does not produce test " +
          "samples");
    }

    List<BloodTestingRule> rules = bloodTestingRuleRepository.getBloodTestingRules(false, false);

    // Get the latest test results
    Map<UUID, String> storedTestResults = new TreeMap<>();
    Map<UUID, String> availableTestResults = new TreeMap<>();
    Map<UUID, BloodTestResult> recentTestResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    for (UUID testId : recentTestResults.keySet()) {
      BloodTestResult testResult = recentTestResults.get(testId);
      if (!testResult.getReEntryRequired()) {
        String testValue = testResult.getResult();
        storedTestResults.put(testId, testValue);
        availableTestResults.put(testId, testValue);
      }
    }

    // Overwrite the existing blood  and (where necessary) with the new bloodTestResults provided (as a parameter)
    for (UUID testId : bloodTestResults.keySet()) {
      String testResult = bloodTestResults.get(testId);
      availableTestResults.put(testId, testResult);
    }

    BloodTestingRuleResultSet resultSet = new BloodTestingRuleResultSet(donation, storedTestResults,
        availableTestResults, recentTestResults, rules);
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("BloodTestingRuleEngine running for donation with id '" + donation.getId()
          + "' and donor with number '" + donation.getDonorNumber() + "' using available test results = "
          + availableTestResults);
    }

    // Go through each rule and see if the pattern matches the available result and tally the TTI, ABO, RH results
    for (BloodTestingRule rule : rules) {

      if (BloodTypingMatchStatus.isResolvedState(donation.getBloodTypingMatchStatus())
          && (rule.getDonationFieldChanged() == DonationField.BLOODABO
          || rule.getDonationFieldChanged() == DonationField.BLOODRH)) {
        // Don't process the rule if it is for blood typing and the blood typing is resolved
        continue;
      }

      processRule(rule, resultSet, availableTestResults);
    }

    // Determine how many blood typing tests were done
    List<BloodTest> bloodTypingTests = bloodTestRepository.getBloodTypingTests();
    setBloodTypingTestsDone(resultSet, bloodTypingTests, availableTestResults);

    // Check ABO/Rh results against donor's ABO/Rh
    setBloodTypingMatchStatus(resultSet, donation);

    // Determine if the pending Blood ABO/Rh tests are required
    updatePendingAboRhTests(resultSet);

    // Determine the blood status based on ABO/Rh tests
    setBloodTypingStatus(resultSet, availableTestResults, donation);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Donation " + resultSet.getDonation().getId() + " for donor " +
          resultSet.getDonation().getDonor().getId() + " has BloodTypingStatus of " + resultSet.getBloodTypingStatus());
    }

    // Determine if there are missing required basic blood TTI tests
    List<BloodTest> basicTTITests = bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI);
    setBasicTtiTestsNotDone(resultSet, basicTTITests, availableTestResults);

    // Determine the TTI status
    setTTIStatus(resultSet);

    // Split repeat and confirmatory pending TTI tests
    List<BloodTest> repeatTTITests = bloodTestRepository.getBloodTestsOfType(BloodTestType.REPEAT_TTI);
    List<BloodTest> confirmatoryTTITests = bloodTestRepository.getBloodTestsOfType(BloodTestType.CONFIRMATORY_TTI);
    setSeparateRepeatAndConfirmatoryPendingTTITests(resultSet, repeatTTITests, confirmatoryTTITests);

    return bloodTestingRuleResultViewModelFactory.createBloodTestResultFullViewModel(resultSet);
  }

  /**
   * Process the specified BloodTestingRule and store the results in the blood testing result set.
   * 
   * @param rule BloodTestingRule defining what is being tested
   * @param resultSet BloodTestingRuleResultSet that contains the processed test results.
   * @param availableTestResults Map<UUID, String> the currently available (and recent) test results
   */
  private void processRule(BloodTestingRule rule, BloodTestingRuleResultSet resultSet,
      Map<UUID, String> availableTestResults) {

    boolean patternMatch = false;
    String expectedResult = rule.getPattern();
    String actualResult = availableTestResults.get(rule.getBloodTest().getId());
    if (actualResult != null && expectedResult != null && expectedResult.equals(actualResult)) {
      patternMatch = true;
    }

    if (patternMatch) {
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace("Pattern matched for rule with id '" + rule.getId() + "' and donationFieldChanged '" + rule.getDonationFieldChanged() + "'.");
        LOGGER.trace("Test id: " + rule.getBloodTest().getId());
        LOGGER.trace("pattern: " + rule.getPattern());
        LOGGER.trace("Donation field changed: " + rule.getDonationFieldChanged());
        LOGGER.trace("Pending test ids: " + rule.getPendingTestsIdsSet());
        LOGGER.trace("Changes to result: " + rule.getNewInformation());
      }
      
      // determine which changes are necessary, depending on the DonationField
      DonationField donationFieldChanged = rule.getDonationFieldChanged();
      switch (donationFieldChanged) {
        case BLOODABO:
          resultSet.addBloodAboChanges(rule.getNewInformation());
          break;
        case BLOODRH:
          resultSet.addBloodRhChanges(rule.getNewInformation());
          break;
        case TTISTATUS:
          resultSet.addTtiStatusChanges(rule.getNewInformation());
          break;
        case TITRE:
          resultSet.addTitreChanges(rule.getNewInformation());
          break;
        default:
          LOGGER.warn("Unknown donation field: " + donationFieldChanged);
          break;
      }

      // determine which tests are pending
      // Note: pending tests are dependent on having a donation field changed.
      // This might not be desired in the future
      for (UUID extraTestId : rule.getPendingTestsIdsSet()) {
        if (!availableTestResults.containsKey(extraTestId)) {
          switch (rule.getDonationFieldChanged()) {
            case BLOODABO:
              resultSet.addPendingAboTestsIds(extraTestId);
              break;
            case BLOODRH:
              resultSet.addPendingRhTestsIds(extraTestId);
              break;
            case TTISTATUS:
              resultSet.addPendingRepeatAndConfirmatoryTtiTestsIds(extraTestId);
              break;
            case TITRE:
              // see note above
              break;
            default:
              LOGGER.warn("Unknown rule donationFieldChanged: " + rule.getDonationFieldChanged());
              break;
          }
        }
      }
    }
  }

  /**
   * Determines which TTI tests were not done by comparing the available test results to the basic
   * TTI tests.
   * 
   * @param resultSet BloodTestingRuleResultSet that contains the processed test results.
   * @param basicTTITests List<BloodTest> the required TTI tests
   * @param availableTestResults Map<String, String> the currently available (and recent) test
   *            results
   */
  private void setBasicTtiTestsNotDone(BloodTestingRuleResultSet resultSet, List<BloodTest> basicTTITests,
      Map<UUID, String> availableTestResults) {
    Set<UUID> basicTtiTestsNotDone = new HashSet<>();
    for (BloodTest bt : basicTTITests) {
      basicTtiTestsNotDone.add(bt.getId());
    }
    for (UUID testId : availableTestResults.keySet()) {
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace("available test=" + testId + " result=" + availableTestResults.get(testId));
      }
      basicTtiTestsNotDone.remove(testId);
    }
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("basicTtiTestsNotDone=" + basicTtiTestsNotDone);
    }
    resultSet.setBasicTtiTestsNotDone(basicTtiTestsNotDone);
  }

  /**
   * Counts the number of blood typing tests done by comparing all the blood typing tests to the
   * available test results.
   * 
   * @param resultSet BloodTestingRuleResultSet that contains the processed test results.
   * @param bloodTypingTests List<BloodTest> all the blood typing tests
   * @param availableTestResults Map<String, String> the currently available (and recent) test
   *            results
   */
  private void setBloodTypingTestsDone(BloodTestingRuleResultSet resultSet, List<BloodTest> bloodTypingTests,
      Map<UUID, String> availableTestResults) {
    int numBloodTypingTests = 0;
    for (BloodTest bt : bloodTypingTests) {
      if (availableTestResults.containsKey(bt.getId()))
        numBloodTypingTests++;
    }
    resultSet.setNumBloodTypingTests(numBloodTypingTests);
  }

  /**
   * Determine the blood status based on ABO/Rh tests (and pending tests) results. Saves the
   * BloodTypingStatus result in the resultSet.
   */
  private void setBloodTypingStatus(BloodTestingRuleResultSet resultSet, Map<UUID, String> availableTestResults, Donation donation) {

    // Determine if there are missing required basic blood typing tests
    List<BloodTest> basicBloodTypingTests = bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING);
    for (BloodTest bt : basicBloodTypingTests) {
      if (availableTestResults.get(bt.getId()) == null) {
        resultSet.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
        return;
      }
    }
    
    // Check if there are pending tests.
    if (!resultSet.getPendingAboTestsIds().isEmpty() || !resultSet.getPendingRhTestsIds().isEmpty()) {
      resultSet.setBloodTypingStatus(BloodTypingStatus.PENDING_TESTS);
      return;
    }

    resultSet.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
  }

  private BloodTypingMatchStatus getBloodTypingMatchStatusForFirstTimeDonor(BloodTestingRuleResultSet resultSet) {

    Map<UUID, String> availableTestResults = resultSet.getAvailableTestResults();
    List<BloodTest> repeatBloodtypingTests = bloodTestRepository.getBloodTestsOfType(BloodTestType.REPEAT_BLOODTYPING);

    for (BloodTest repeatBloodTypingTest : repeatBloodtypingTests) {

      String repeatResult = availableTestResults.get(repeatBloodTypingTest.getId());

      if (repeatResult == null) {
        // There is a missing repeat result
        return BloodTypingMatchStatus.NO_MATCH;
      }

      for (BloodTestingRule bloodTestingRule : resultSet.getBloodTestingRules()) {

        // Find which tests resulted in the repeat test
        Set<UUID> pendingTestIds = bloodTestingRule.getPendingTestsIdsSet();
        if (pendingTestIds.contains(repeatBloodTypingTest.getId())) {

          // Compare the result of the repeat test to previous test
          String initialResult = availableTestResults.get(bloodTestingRule.getBloodTest().getId());
          if (!repeatResult.equals(initialResult)) {
            // There is a repeat result which does not match the initial result
            return BloodTypingMatchStatus.AMBIGUOUS;
          }
        }
      }
    }

    // There were no missing or mismatched results
    return BloodTypingMatchStatus.MATCH;
  }

  /**
   * Check ABO/Rh results against donor's ABO/Rh and saves the BloodTypingMatchStatus result in
   * the resultSet
   * 
   * @param resultSet BloodTestingRuleResultSet that contains the processed test results.
   * @param donation Donation containing the information about the Donor.
   */
  private void setBloodTypingMatchStatus(BloodTestingRuleResultSet resultSet, Donation donation) {
    BloodTypingMatchStatus bloodTypingMatchStatus = BloodTypingMatchStatus.NOT_DONE;

    Donor donor = donation.getDonor();

    if (BloodTypingMatchStatus.isResolvedState(donation.getBloodTypingMatchStatus())) {
      // The Abo/Rh values have already been resolved, so keep the match status the same
      bloodTypingMatchStatus = donation.getBloodTypingMatchStatus();
      
    } else if (resultSet.getBloodAboChanges().contains("") || resultSet.getBloodRhChanges().contains("")) {
      // One of the basic blood typing tests have Not Tested (NT) as the result
      bloodTypingMatchStatus = BloodTypingMatchStatus.INDETERMINATE;

    } else if (StringUtils.isNotEmpty(donation.getBloodAbo()) && StringUtils.isNotEmpty(donation.getBloodRh())) {

      if (StringUtils.isEmpty(donor.getBloodAbo()) || StringUtils.isEmpty(donor.getBloodRh())) {
        // first time donor - required to enter in confirmatory result
        bloodTypingMatchStatus = getBloodTypingMatchStatusForFirstTimeDonor(resultSet);
      } else {

        if (donor.getBloodAbo().equals(donation.getBloodAbo()) && donor.getBloodRh().equals(donation.getBloodRh())) {
          // blood Abo/Rh matches
          bloodTypingMatchStatus = BloodTypingMatchStatus.MATCH;
        } else {
          // ambiguous result - required to enter in confirmatory result
          bloodTypingMatchStatus = BloodTypingMatchStatus.AMBIGUOUS;
        }
      }
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("donation " + donation.getId() + " for donor " + donor.getId() + " has BloodTypingMatchStatus of " + bloodTypingMatchStatus);
      LOGGER.debug("donor Abo/Rh = " + donor.getBloodAbo() + donor.getBloodRh() + " donation Abo/Rh = " + donation.getBloodAbo() + donation.getBloodRh());
    }

    resultSet.setBloodTypingMatchStatus(bloodTypingMatchStatus);
  }

  /**
   * Clear pending ABO and Rh tests if the donor is not a first time donor.
   * 
   * @param resultSet BloodTestingRuleResultSet that contains the processed test results.
   */
  private void updatePendingAboRhTests(BloodTestingRuleResultSet resultSet) {
    if (resultSet.getBloodTypingMatchStatus() == BloodTypingMatchStatus.NO_MATCH) {
      if (LOGGER.isInfoEnabled()) {
        LOGGER.info("Donor " + resultSet.getDonation().getDonor().getId()
            + " is a first time donor, so pending ABO tests ("
            + resultSet.getPendingAboTestsIds().size() + ") and pending Rh tests ("
            + resultSet.getPendingRhTestsIds().size() + ") are required.");
      }
    } else {
      resultSet.getPendingAboTestsIds().clear();
      resultSet.getPendingRhTestsIds().clear();
    }
  }

  /**
   * Check the TTI status and determine if it is safe or not. Result will be stored in the
   * resultSet.
   * 
   * @param resultSet BloodTestingRuleResultSet that contains the processed test results.
   */
  private void setTTIStatus(BloodTestingRuleResultSet resultSet) {
    TTIStatus ttiStatus = TTIStatus.NOT_DONE;

    boolean basicTTITestsDone = resultSet.getBasicTtiTestsNotDone().isEmpty();

    Set<String> ttiStatusChanges = resultSet.getTtiStatusChanges();
    if (!ttiStatusChanges.isEmpty()) {
      if (ttiStatusChanges.contains(TTIStatus.UNSAFE.toString())) {
        ttiStatus = TTIStatus.UNSAFE;
      } else if (ttiStatusChanges.contains(TTIStatus.INDETERMINATE.toString()) && basicTTITestsDone) {
        ttiStatus = TTIStatus.INDETERMINATE;
      } else if (ttiStatusChanges.size() == 1 && ttiStatusChanges.contains(TTIStatus.SAFE.toString()) && basicTTITestsDone) {
        ttiStatus = TTIStatus.SAFE;
      } 
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("donation " + resultSet.getDonation().getId() + " for donor " + resultSet.getDonation().getDonor().getId() + " has TTIStatus of " + ttiStatus);
    }

    resultSet.setTtiStatus(ttiStatus);
  }

  /**
   * Sets the separate repeat and confirmatory pending tti tests.
   *
   * @param resultSet the result set
   * @param repeatTTITests the repeat tti tests
   * @param confirmatoryTTITests the confirmatory tti tests
   */
  private void setSeparateRepeatAndConfirmatoryPendingTTITests(BloodTestingRuleResultSet resultSet,
      List<BloodTest> repeatTTITests, List<BloodTest> confirmatoryTTITests) {
    for (BloodTest repeatTTITest : repeatTTITests) {
      if (resultSet.getPendingRepeatAndConfirmatoryTtiTestsIds().contains((repeatTTITest.getId()))) {
        resultSet.addPendingRepeatTtiTestsIds(repeatTTITest.getId());
      }
    }

    for (BloodTest confirmatoryTTITest : confirmatoryTTITests) {
      if (resultSet.getPendingRepeatAndConfirmatoryTtiTestsIds().contains((confirmatoryTTITest.getId()))) {
        resultSet.addPendingConfirmatoryTtiTestsIds(confirmatoryTTITest.getId());
      }
    }

  }
}
