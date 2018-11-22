package org.jembi.bsis.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.jembi.bsis.backingform.TestResultsBackingForm;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.repository.BloodTestResultRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for BloodTest related business logic/workflow
 */
@Transactional
@Service
public class BloodTestsService {

  @Autowired
  private BloodTestRepository bloodTestRepository;

  @Autowired
  private BloodTestResultRepository bloodTestResultRepository;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private BloodTestingRuleEngine ruleEngine;

  @Autowired
  private TestBatchStatusChangeService testBatchStatusChangeService;

  @Autowired
  private GeneralConfigAccessorService generalConfigAccessorService;

  /**
   * Executes the BloodTestingRuleEngine with the configured BloodTests and returns the results
   *
   * @param donations Collection of Donations to run the tests on
   * @return List<BloodTestingRuleResult> containing the results from the tests for the specified Donations
   */
  public List<BloodTestingRuleResult> executeTests(Collection<Donation> donations) {
    List<BloodTestingRuleResult> results = new ArrayList<>();
    for (Donation donation : donations) {
      results.add(executeTests(donation));
    }
    return results;
  }

  /**
   * Executes the BloodTestingRuleEngine with the configured BloodTests and returns the results
   *
   * @param donation Donation to run the tests on
   * @return BloodTestingRuleResult with the results from the tests
   */
  public BloodTestingRuleResult executeTests(Donation donation) {
    return ruleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
  }

  /**
   * Saves the BloodTest results for a list of donations and updates each donation (bloodAbo/Rh and
   * statuses)
   *
   * @param forms the forms
   * @param reEntry the re entry
   */
  public void saveBloodTests(List<TestResultsBackingForm> forms, boolean reEntry) {

    for (TestResultsBackingForm form : forms) {

      // Get donation
      Donation donation =
          donationRepository.findDonationByDonationIdentificationNumber(form.getDonationIdentificationNumber());

      // Get testResults from form
      Map<UUID, String> bloodTestResults = form.getTestResults();

      // Apply reEntry system config
      if (!reEntry
          && !generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.TESTING_RE_ENTRY_REQUIRED, true)) {
        // This is not re-entry, but re-entry is not required, so just set reEntry to true
        // The result of this is that no blood test results will be marked as requiring re-entry
        reEntry = true;
      }

      // Run rule engine for the 1st time and save testResults
      Map<UUID, String> reEnteredBloodTestResults = bloodTestResults;
      if (!reEntry) {
        // only outcomes that have been entered twice will be considered by the rules engine
        reEnteredBloodTestResults = new HashMap<>();
      }
      BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(donation, reEnteredBloodTestResults);
      bloodTestingRepository.saveBloodTestResultsToDatabase(bloodTestResults, donation, new Date(), ruleResult, reEntry);

      // Run rule engine for the 2nd time and save testResults
      // Note: Rules engine will only provide the correct BloodTyping statuses on the 2nd execution
      // because the Donation Abo/Rh is only updated after the 1st execution
      ruleResult = ruleEngine.applyBloodTests(donation, reEnteredBloodTestResults);
      bloodTestingRepository.saveBloodTestResultsToDatabase(bloodTestResults, donation, new Date(), ruleResult, reEntry);

      // Update donation
      if (donation.getTestBatch().getStatus() == TestBatchStatus.RELEASED && reEntry) {
        testBatchStatusChangeService.handleRelease(donation);
      }
    }

  }

  /**
   * Updates the specified Donation given the results from the BloodTests. Updates include blood
   * grouping, TTI status and blood typing statuses.
   *
   * @param donation Donation on which the tests were run
   * @param ruleResult BloodTestingRuleResult containing the results from the tests
   * @return boolean, true if the Donation was updated
   */
  public boolean updateDonationWithTestResults(Donation donation, BloodTestingRuleResult ruleResult) {
    // FIXME: this method should be in this service but it has too many references in
    // BloodTestingRepository
    return bloodTestingRepository.updateDonationWithTestResults(donation, ruleResult);
  }

  protected void setBloodTestingRepository(BloodTestingRepository bloodTestingRepository) {
    this.bloodTestingRepository = bloodTestingRepository;
  }

  public Map<String, Object> getBloodTestShortNames() {
    List<String> basicTtiTestNames = new ArrayList<String>();
    List<String> repeatTtiTestNames = new ArrayList<String>();
    List<String> confirmatoryTtiTestNames = new ArrayList<String>();
    List<String> basicBloodTypingTestNames = new ArrayList<String>();
    List<String> repeatBloodTypingTestNames = new ArrayList<String>();

    Map<String, Object> map = new HashMap<String, Object>();

    for (BloodTest rawBloodTest : bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI)) {
      basicTtiTestNames.add(rawBloodTest.getTestNameShort());
    }
    map.put("basicTtiTestNames", basicTtiTestNames);
    for (BloodTest rawBloodTest : bloodTestRepository.getBloodTestsOfType(BloodTestType.REPEAT_TTI)) {
      repeatTtiTestNames.add(rawBloodTest.getTestNameShort());
    }
    map.put("repeatTtiTestNames", repeatTtiTestNames);
    for (BloodTest rawBloodTest : bloodTestRepository.getBloodTestsOfType(BloodTestType.CONFIRMATORY_TTI)) {
      confirmatoryTtiTestNames.add(rawBloodTest.getTestNameShort());
    }
    map.put("confirmatoryTtiTestNames", confirmatoryTtiTestNames);
    for (BloodTest rawBloodTest : bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)) {
      basicBloodTypingTestNames.add(rawBloodTest.getTestNameShort());
    }
    map.put("basicBloodTypingTestNames", basicBloodTypingTestNames);
    for (BloodTest rawBloodTest : bloodTestRepository.getBloodTestsOfType(BloodTestType.REPEAT_BLOODTYPING)) {
      repeatBloodTypingTestNames.add(rawBloodTest.getTestNameShort());
    }
    map.put("repeatBloodTypingTestNames", repeatBloodTypingTestNames);

    return map;
  }

  public void setTestOutcomesAsDeleted(Donation donation) {
    List<BloodTestResult> testOutcomes = bloodTestResultRepository.getTestOutcomes(donation);
    for (BloodTestResult testOutcome : testOutcomes) {
      testOutcome.setIsDeleted(true);
      bloodTestResultRepository.save(testOutcome);
    }
  }
}
