package service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import model.bloodtesting.BloodTest;
import model.bloodtesting.TSVFileHeaderName;
import model.bloodtesting.TTIStatus;
import model.component.Component;
import model.donation.Donation;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.ComponentRepository;
import repository.DonationRepository;
import repository.bloodtesting.BloodTestingRepository;
import repository.bloodtesting.BloodTestingRuleEngine;
import repository.bloodtesting.BloodTypingStatus;
import viewmodel.BloodTestingRuleResult;

/**
 * Service for BloodTest related business logic/workflow
 */
@Transactional
@Service
public class BloodTestsService {
  
  private static final Logger LOGGER = Logger.getLogger(BloodTestsService.class);
  
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
		BloodTestingRuleResult ruleResult = bloodTestingRepository.getAllTestsStatusForDonation(donation.getId());
		return ruleResult;
	}

	
  /**
   * Saves the BloodTest results and updates the Donation (bloodAbo/Rh and statuses)
   * 
   * @param donationId Long identifier of the donation that should be updated with new test results
   * @param bloodTestResults Map of test results
   * @return BloodTestingRuleResult containing the results of the Blood Test Rules Engine
   */
  public BloodTestingRuleResult saveBloodTests(Long donationId, Map<Long, String> bloodTestResults) {
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(donation, bloodTestResults);
    bloodTestingRepository.saveBloodTestResultsToDatabase(bloodTestResults, donation, new Date(), ruleResult);
    updateDonationWithTestResults(donation, ruleResult);
    ruleResult = ruleEngine.applyBloodTests(donation, bloodTestResults); // run the ruleEngine a 2nd time to use the correct Abo/Rh for the donation
    donationRepository.saveDonation(donation);
    return ruleResult;
  }

  /**
   * Saves BloodTest results which are imported from a TSV file
   * 
   * @param tSVFileHeaderNameList List of TSVFileHeaderName that contain the TSV data per row 
   */
  public void saveTestResults(List<TSVFileHeaderName> tSVFileHeaderNameList) {
    for (TSVFileHeaderName ts : tSVFileHeaderNameList) {
      Donation donation = donationRepository.findDonationByDonationIdentificationNumber(ts.getSID());
      if (donation != null) {
        try {
          Map<Long, BloodTestingRuleResult> bloodTestRuleResultsForDonations = new HashMap<Long, BloodTestingRuleResult>();

          BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(donation, new HashMap<Long, String>());
          bloodTestRuleResultsForDonations.put(donation.getId(), ruleResult);

          Long assayNumber = Long.valueOf(ts.getAssayNumber());
          String interpretation = ts.getInterpretation();
          Date completed = ts.getCompleted();
          bloodTestingRepository.saveBloodTestResultToDatabase(assayNumber, interpretation, donation, completed, ruleResult);
          updateDonationWithTestResults(donation, ruleResult);

        } catch (Exception ex) {
          // FIXME: should record this error and present it to the user
          LOGGER.error("Cannot save TTI Test Result for donation " + donation.getId() + " to DB", ex);
        }
      }
    }
  }
  
  /**
   * Validate the test results to ensure that the test results comply with the BloodTests configured
   * 
   * @param bloodTypingTestResults Map<Long, String> containing String results mapped to BloodTest identifiers
   * @return Map<Long, String> of errors mapped to BloodTest identifiers
   */
  public Map<Long, String> validateTestResultValues(Map<Long, String> bloodTypingTestResults) {

    // Build a map of active blood test ids to the active blood tests.
    Map<String, BloodTest> activeBloodTestsMap = new HashMap<>();
    for (BloodTest bloodTypingTest : bloodTestingRepository.findActiveBloodTests()) {
      activeBloodTestsMap.put(bloodTypingTest.getId().toString(), bloodTypingTest);
    }

    Map<Long, String> errorMap = new HashMap<>();
    for (Long testId : bloodTypingTestResults.keySet()) {
      BloodTest activeBloodTest = activeBloodTestsMap.get(testId.toString());
      if (activeBloodTest == null) {
        // No active test was found for the provided id
        errorMap.put(testId, "Invalid test");
        break;
      }

      String result = bloodTypingTestResults.get(testId);

      if (!activeBloodTest.getIsEmptyAllowed() && StringUtils.isBlank(result)) {
        // Empty results are not allowed for this test and the provided result is empty
        errorMap.put(testId, "No value specified");
        break;
      }

      if (!activeBloodTest.getValidResultsList().contains(result)) {
        // The provided result is not in the list of valid results
        errorMap.put(testId, "Invalid value specified");
        break;
      }
    }

    return errorMap;
  }
	
	/**
	 * Updates the specified Donation given the results from the BloodTests. Updates include blood
	 * grouping, extra information, TTI status and blood typing statuses.
	 * 
	 * @param donation Donation on which the tests were run
	 * @param ruleResult BloodTestingRuleResult containing the results from the tests
	 * @return boolean, true if the Donation was updated
	 */
	public boolean updateDonationWithTestResults(Donation donation, BloodTestingRuleResult ruleResult) {
		boolean donationUpdated = false;
		
		String oldExtraInformation = donation.getExtraBloodTypeInformation();
		String newExtraInformation = addNewExtraInformation(oldExtraInformation, ruleResult.getExtraInformation());
		
		String oldBloodAbo = donation.getBloodAbo();
		String newBloodAbo = ruleResult.getBloodAbo();
		
		String oldBloodRh = donation.getBloodRh();
		String newBloodRh = ruleResult.getBloodRh();
		
		TTIStatus oldTtiStatus = donation.getTTIStatus();
		TTIStatus newTtiStatus = ruleResult.getTTIStatus();
		
		BloodTypingStatus oldBloodTypingStatus = donation.getBloodTypingStatus();
		BloodTypingStatus newBloodTypingStatus = ruleResult.getBloodTypingStatus();
		
		if (!newExtraInformation.equals(oldExtraInformation) || !newBloodAbo.equals(oldBloodAbo)
		        || !newBloodRh.equals(oldBloodRh) || !newTtiStatus.equals(oldTtiStatus)
		        || !newBloodTypingStatus.equals(oldBloodTypingStatus)) {
			donation.setExtraBloodTypeInformation(newExtraInformation);
			donation.setBloodAbo(newBloodAbo);
			donation.setBloodRh(newBloodRh);
			donation.setTTIStatus(ruleResult.getTTIStatus());
			donation.setBloodTypingStatus(ruleResult.getBloodTypingStatus());
			
			donationUpdated = true;
			
            if (LOGGER.isInfoEnabled()) {
              LOGGER.info("Updating Donation '" + donation.getId() + "' with Abo/Rh="
                  + donation.getBloodAbo() + donation.getBloodRh() + " TTIStatus="
                  + donation.getTTIStatus() + " BloodTypingStatus=" + donation.getBloodTypingStatus()
                  + " " + donation.getBloodTypingMatchStatus());
            }
		}
		donation.setBloodTypingMatchStatus(ruleResult.getBloodTypingMatchStatus());
		
		return donationUpdated;
	}
	
	/**
	 * Updates Components as a result of Blood Tests being done on a Donation. The updates include
	 * the Component Status - and should result in Components being discarded if a Donation is
	 * marked as TTI_UNSAFE.
	 * 
	 * @param donation Donation on which the tests were run
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
	
	protected String addNewExtraInformation(String donationExtraInformation, Set<String> extraInformationNewSet) {
		String newExtraInformation;
		Set<String> oldExtraInformationSet = new HashSet<String>();
		if (StringUtils.isNotBlank(donationExtraInformation)) {
			oldExtraInformationSet.addAll(Arrays.asList(donationExtraInformation.split(",")));
			extraInformationNewSet.removeAll(oldExtraInformationSet); // remove duplicates
			newExtraInformation = donationExtraInformation + StringUtils.join(extraInformationNewSet, ",");
		} else {
			newExtraInformation = StringUtils.join(extraInformationNewSet, ",");
		}
		return newExtraInformation;
	}
}
