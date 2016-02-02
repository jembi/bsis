package service;

import java.util.Date;
import java.util.Map;

import model.component.Component;
import model.donation.Donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ComponentRepository;
import repository.DonationRepository;
import repository.bloodtesting.BloodTestingRepository;
import repository.bloodtesting.BloodTestingRuleEngine;
import viewmodel.BloodTestingRuleResult;

import javax.transaction.Transactional;
import java.util.List;

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

  /**
   * Executes the BloodTestingRuleEngine with the configured BloodTests and returns the results
   *
   * @param donation Donation to run the tests on
   * @return BloodTestingRuleResult with the results from the tests
   */
  public BloodTestingRuleResult executeTests(Donation donation) {
    return bloodTestingRepository.getAllTestsStatusForDonation(donation.getId());
  }


  /**
   * Validate the test results to ensure that the test results comply with the BloodTests configured
   *
   * @param bloodTypingTestResults Map<Long, String> containing String results mapped to BloodTest identifiers
   * @return Map<Long, String> of errors mapped to BloodTest identifiers
   */
  public Map<Long, String> validateTestResultValues(Map<Long, String> bloodTypingTestResults) {
    // FIXME: this method should be in this service but due to the many references in BloodTestingRepository, it was not moved
    return bloodTestingRepository.validateTestResultValues(null, bloodTypingTestResults);
  }

  /**
   * Saves the BloodTest results and updates the Donation (bloodAbo/Rh and statuses)
   *
   * @param donationId       Long identifier of the donation that should be updated with new test results
   * @param bloodTestResults Map of test results
   * @return BloodTestingRuleResult containing the results of the Blood Test Rules Engine
   */
  public BloodTestingRuleResult saveBloodTests(Long donationId, Map<Long, String> bloodTestResults) {
    Donation donation = donationRepository.findDonationById(donationId);
    // FIXME: rules engine will not provide the correct BloodTyping statuses because the Donation passed in has the wrong Abo/Rh (see FIXME below)
    BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(donation, bloodTestResults);
    bloodTestingRepository.saveBloodTestResultsToDatabase(bloodTestResults, donation, new Date(), ruleResult);
    // FIXME: run the ruleEngine a 2nd time to use the correct Abo/Rh for the donation
    ruleResult = ruleEngine.applyBloodTests(donation, bloodTestResults);
    donationRepository.saveDonation(donation);
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

  /**
   * Updates Components as a result of Blood Tests being done on a Donation. The updates include
   * the Component Status - and should result in Components being discarded if a Donation is
   * marked as TTI_UNSAFE.
   *
   * @param donation   Donation on which the tests were run
   * @param ruleResult BloodTestingRuleResult results from the Blood Tests.
   */
  public void updateComponentsWithTestResults(Donation donation, BloodTestingRuleResult ruleResult) {
    List<Component> components = componentRepository.findComponentsByDonationIdentificationNumber(donation
        .getDonationIdentificationNumber());
    if (components != null) {
      for (Component component : components) {
        // FIXME: this method should be in this service, but it has too many references in ComponentRepository
        componentRepository.updateComponentInternalFields(component);
      }
    }
  }

  protected void setBloodTestingRepository(BloodTestingRepository bloodTestingRepository) {
    this.bloodTestingRepository = bloodTestingRepository;
  }
}
