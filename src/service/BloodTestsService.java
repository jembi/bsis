package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import constant.GeneralConfigConstants;
import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestType;
import model.donation.Donation;
import model.testbatch.TestBatchStatus;
import repository.ComponentRepository;
import repository.DonationRepository;
import repository.bloodtesting.BloodTestingRepository;
import repository.bloodtesting.BloodTestingRuleEngine;
import viewmodel.BloodTestingRuleResult;

/**
 * Service for BloodTest related business logic/workflow
 */
@Transactional
@Service
public class BloodTestsService {

  @Autowired
  ComponentRepository componentRepository;

  @Autowired
  BloodTestingRepository bloodTestingRepository;

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
   * @param donation Donation to run the tests on
   * @return BloodTestingRuleResult with the results from the tests
   */
  public BloodTestingRuleResult executeTests(Donation donation) {
    BloodTestingRuleResult ruleResult = bloodTestingRepository.getAllTestsStatusForDonation(donation.getId());
    return ruleResult;
  }


  /**
   * Validate the test results to ensure that the test results comply with the BloodTests
   * configured
   *
   * @param bloodTypingTestResults Map<Long, String> containing String results mapped to BloodTest
   *                               identifiers
   * @return Map<Long, String> of errors mapped to BloodTest identifiers
   */
  public Map<Long, String> validateTestResultValues(Map<Long, String> bloodTypingTestResults) {
    // FIXME: this method should be in this service but due to the many references in BloodTestingRepository, it was not moved
    return bloodTestingRepository.validateTestResultValues(null, bloodTypingTestResults);
  }

  /**
   * Saves the BloodTest results and updates the Donation (bloodAbo/Rh and statuses)
   *
   * @param donationId       Long identifier of the donation that should be updated with new test
   *                         results
   * @param bloodTestResults Map of test results
   * @param reEntry          boolean true if the results are the re-entry and false if the results are first entry
   * @return BloodTestingRuleResult containing the results of the Blood Test Rules Engine
   */
  public BloodTestingRuleResult saveBloodTests(Long donationId, Map<Long, String> bloodTestResults, boolean reEntry) {
    Donation donation = donationRepository.findDonationById(donationId);
    Map<Long, String> reEnteredBloodTestResults = bloodTestResults;
    
    if (!reEntry && !generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.TESTING_RE_ENTRY_REQUIRED, true)) {
      // This is not re-entry, but re-entry is not required, so just set reEntry to true
      // The result of this is that no blood test results will be marked as requiring re-entry
      reEntry = true;
    }
    
    if (!reEntry) {
      // only outcomes that have been entered twice will be considered by the rules engine
      reEnteredBloodTestResults = new HashMap<>();
    }
    BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(donation, reEnteredBloodTestResults);
    bloodTestingRepository.saveBloodTestResultsToDatabase(bloodTestResults, donation, new Date(), ruleResult, reEntry);
    // Note: Rules engine will only provide the correct BloodTyping statuses on the 2nd execution
    // because the Donation Abo/Rh is only updated after the 1st execution
    ruleResult = ruleEngine.applyBloodTests(donation, reEnteredBloodTestResults);
    bloodTestingRepository.saveBloodTestResultsToDatabase(bloodTestResults, donation, new Date(), ruleResult, reEntry);
    if (donation.getDonationBatch().getTestBatch().getStatus() == TestBatchStatus.RELEASED && reEntry) {
      testBatchStatusChangeService.handleRelease(donation);
    }
    return ruleResult;
  }

  /**
   * Updates the specified Donation given the results from the BloodTests. Updates include blood
   * grouping, extra information, TTI status and blood typing statuses.
   *
   * @param donation   Donation on which the tests were run
   * @param ruleResult BloodTestingRuleResult containing the results from the tests
   * @return boolean, true if the Donation was updated
   */
  public boolean updateDonationWithTestResults(Donation donation, BloodTestingRuleResult ruleResult) {
    // FIXME: this method should be in this service but it has too many references in BloodTestingRepository
    return bloodTestingRepository.updateDonationWithTestResults(donation, ruleResult);
  }

  protected void setBloodTestingRepository(BloodTestingRepository bloodTestingRepository) {
    this.bloodTestingRepository = bloodTestingRepository;
  }

  public Map<String, Object> getBloodTestShortNames() {
    List<String> basicTtiTestNames = new ArrayList<String>();
    List<String> repeatTtiTestNames = new ArrayList<String>();
    List<String> basicBloodTypingTestNames = new ArrayList<String>();
    List<String> repeatBloodTypingTestNames = new ArrayList<String>();

    Map<String, Object> map = new HashMap<String, Object>();

    for (BloodTest rawBloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI)) {
      basicTtiTestNames.add(rawBloodTest.getTestNameShort());
    }
    map.put("basicTtiTestNames", basicTtiTestNames);
    for (BloodTest rawBloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.CONFIRMATORY_TTI)) {
      repeatTtiTestNames.add(rawBloodTest.getTestNameShort());
    }
    map.put("repeatTtiTestNames", repeatTtiTestNames);
    for (BloodTest rawBloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)) {
      basicBloodTypingTestNames.add(rawBloodTest.getTestNameShort());
    }
    map.put("basicBloodTypingTestNames", basicBloodTypingTestNames);
    for (BloodTest rawBloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.REPEAT_BLOODTYPING)) {
      repeatBloodTypingTestNames.add(rawBloodTest.getTestNameShort());
    }
    map.put("repeatBloodTypingTestNames", repeatBloodTypingTestNames);

    return map;
  }
}
