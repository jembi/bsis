package repository.bloodtesting;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;
import model.bloodtesting.rules.BloodTestingRule;
import model.bloodtesting.rules.CollectionField;
import model.collectedsample.CollectedSample;
import model.donor.Donor;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import viewmodel.BloodTestingRuleResult;

@Repository
@Transactional
public class BloodTestingRuleEngine {
	
	private static final Logger LOGGER = Logger.getLogger(BloodTestingRuleEngine.class);
	
	@Autowired
	private BloodTestingRepository bloodTestingRepository;
	
	/**
	 * Apply blood typing rules to blood typing tests (combination of what is present in the
	 * database and those passed as parameter.
	 * 
	 * @param collectedSample Blood Typing results for which collection
	 * @param bloodTestResults map of blood typing test id to result. Only character allowed in the
	 *            result. multiple characters should be mapped to negative/positive (TODO) Assume
	 *            validation of results already done.
	 * @return Result of applying the rules. The following values should be present in the map
	 *         bloodAbo (what changes should be made to blood abo after applying these rules),\
	 *         bloodRh (what changes should be made to blood rh), extra (extra information that
	 *         should be added to the blood type like weak A), pendingTests (comma separated list of
	 *         blood typing tests that must be done to determine the blood type), testResults (map
	 *         of blood typing test id to blood typing test either stored or those passed to this
	 *         function or those already stored in the database), bloodTypingStatus (enum
	 *         BloodTypingStatus indicates if complete typing information is available),
	 *         storedTestResults (what blood typing results are actually stored in the database, a
	 *         subset of testResults)
	 */
	public BloodTestingRuleResult applyBloodTests(CollectedSample collectedSample, Map<Long, String> bloodTestResults) {
		
		List<BloodTestingRule> rules = bloodTestingRepository.getActiveBloodTestingRules();
		
		// Get the latest test results 
		Map<String, String> storedTestResults = new TreeMap<String, String>();
		Map<String, String> availableTestResults = new TreeMap<String, String>();
		Map<Integer, BloodTestResult> recentTestResults = bloodTestingRepository
		        .getRecentTestResultsForCollection(collectedSample.getId());
		for (Integer testId : recentTestResults.keySet()) {
			BloodTestResult testResult = recentTestResults.get(testId);
			String testKey = testId.toString();
			String testValue = testResult.getResult();
			storedTestResults.put(testKey, testValue);
			availableTestResults.put(testKey, testValue);
		}
		
		// Overwrite the existing blood  and (where necessary) with the new bloodTestResults provided (as a parameter)
		for (Long testId : bloodTestResults.keySet()) {
			String testResult = bloodTestResults.get(testId);
			availableTestResults.put(testId.toString(), testResult);
		}
		
		BloodTestingRuleResultSet resultSet = new BloodTestingRuleResultSet(collectedSample, storedTestResults,
		        availableTestResults, recentTestResults);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BloodTestingRuleEngine running for collectedSample with id '" + collectedSample.getId()
			        + "' and donor with number '" + collectedSample.getDonorNumber() + "' using available test results = "
			        + availableTestResults);
		}
		
		// Go through each rule and see if the pattern matches the available result and tally the TTI, ABO, RH results
		for (BloodTestingRule rule : rules) {
			processRule(rule, resultSet, availableTestResults);
		}
		
		// Determine how many blood typing tests were done
		List<BloodTest> bloodTypingTests = bloodTestingRepository.getBloodTypingTests();
		setBloodTypingTestsDone(resultSet, bloodTypingTests, availableTestResults);
		
		// Determine the blood status based on ABO/Rh tests
		setBloodMatchStatus(resultSet);
		
		// Check ABO/Rh results against donor's ABO/Rh
		setBloodTypingMatchStatus(resultSet, collectedSample);
		
		// Determine if there are missing required basic blood TTI tests
		List<BloodTest> basicTTITests = bloodTestingRepository.getBasicTTITests();
		setBasicTtiTestsNotDone(resultSet, basicTTITests, availableTestResults);
		
		// Determine the TTI status
		setTTIStatus(resultSet);
		
		return resultSet.buildBloodTestingRuleResult();
	}
	
	/**
	 * Process the specified BloodTestingRule and store the results in the blood testing result set.
	 * 
	 * @param rule BloodTestingRule defining what is being tested
	 * @param resultSet BloodTestingRuleResultSet that contains the processed test results.
	 * @param availableTestResults Map<String, String> the currently available (and recent) test
	 *            results
	 */
	private void processRule(BloodTestingRule rule, BloodTestingRuleResultSet resultSet,
	                         Map<String, String> availableTestResults) {
		boolean patternMatch = true;
		boolean atLeastOneResultFoundForPattern = false;
		List<String> pattern = Arrays.asList(rule.getPattern().split(","));
		List<String> testIds = Arrays.asList(rule.getBloodTestsIds().split(","));
		for (int i = 0, n = testIds.size(); i < n; i++) {
			String testId = testIds.get(i);
			String expectedResult = pattern.get(i);
			String actualResult = availableTestResults.get(testId);
			if (actualResult == null) {
				patternMatch = false;
				continue;
			}
			if (expectedResult != null) {
				atLeastOneResultFoundForPattern = true;
				if (!expectedResult.equals(actualResult)) {
					patternMatch = false;
				}
			}
		}
		
		if (patternMatch) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Pattern matched for rule with id '" + rule.getId() + "' and subcategory '"
				        + rule.getSubCategory() + "'.");
			}
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Test ids: " + rule.getBloodTestsIds());
				LOGGER.trace("pattern: " + rule.getPattern());
				LOGGER.trace("Collection field changed: " + rule.getCollectionFieldChanged());
				LOGGER.trace("Pending test ids: " + rule.getPendingTestsIds());
				LOGGER.trace("Changes to result: " + rule.getNewInformation() + ", " + rule.getExtraInformation());
			}
			CollectionField collectionFieldChanged = rule.getCollectionFieldChanged();
			switch (collectionFieldChanged) {
				case BLOODABO:
					resultSet.addBloodAboChanges(rule.getNewInformation());
					break;
				case BLOODRH:
					resultSet.addBloodRhChanges(rule.getNewInformation());
					break;
				case TTISTATUS:
					resultSet.addTtiStatusChanges(rule.getNewInformation());
					break;
				case EXTRA:
					resultSet.addExtraInformation(rule.getNewInformation());
					break;
				default:
					LOGGER.warn("Unknown collection field: " + collectionFieldChanged);
					break;
			}
			
			if (StringUtils.isNotBlank(rule.getExtraInformation()))
				resultSet.addExtraInformation(rule.getExtraInformation());
			
			// find extra tests for ABO
			if (StringUtils.isNotBlank(rule.getPendingTestsIds())) {
				for (String extraTestId : rule.getPendingTestsIds().split(",")) {
					if (!availableTestResults.containsKey(extraTestId)) {
						switch (rule.getSubCategory()) {
							case BLOODABO:
								resultSet.addPendingAboTestsIds(extraTestId);
								break;
							case BLOODRH:
								resultSet.addPendingRhTestsIds(extraTestId);
								break;
							case TTI:
								resultSet.addPendingTtiTestsIds(extraTestId);
								break;
							default:
								LOGGER.warn("Unknown rule subcategory: " + rule.getSubCategory());
								break;
						}
					}
				}
			}
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Pattern NOT matched for rule with id '" + rule.getId()
				        + "' and at least one result was found for pattern: " + atLeastOneResultFoundForPattern);
			}
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Test ids: " + rule.getBloodTestsIds());
				LOGGER.trace("pattern: " + rule.getPattern());
				LOGGER.trace("Collection field changed: " + rule.getCollectionFieldChanged());
				LOGGER.trace("Changes to result: " + rule.getNewInformation() + ", " + rule.getExtraInformation());
			}
			CollectionField collectionFieldChanged = rule.getCollectionFieldChanged();
			switch (collectionFieldChanged) {
				case BLOODABO:
					if (atLeastOneResultFoundForPattern)
						resultSet.setAboUninterpretable(true);
					break;
				case BLOODRH:
					if (atLeastOneResultFoundForPattern)
						resultSet.setRhUninterpretable(true);
					break;
				case TTISTATUS:
					if (atLeastOneResultFoundForPattern)
						resultSet.setTtiUninterpretable(true);
					break;
				default:
					LOGGER.warn("Unknown collection field: " + collectionFieldChanged);
					break;
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
	                                     Map<String, String> availableTestResults) {
		Set<Integer> basicTtiTestsNotDone = new HashSet<Integer>();
		for (BloodTest bt : basicTTITests) {
			basicTtiTestsNotDone.add(bt.getId());
		}
		for (String testId : availableTestResults.keySet()) {
			System.out.println("available test=" + testId + " result=" + availableTestResults.get(testId));
			basicTtiTestsNotDone.remove(Integer.parseInt(testId));
		}
		System.out.println("basicTtiTestsNotDone=" + basicTtiTestsNotDone);
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
	                                     Map<String, String> availableTestResults) {
		int numBloodTypingTests = 0;
		for (BloodTest bt : bloodTypingTests) {
			if (availableTestResults.containsKey((bt.getId().toString())))
				numBloodTypingTests++;
		}
		resultSet.setNumBloodTypingTests(numBloodTypingTests);
	}
	
	/**
	 * Determine the blood status based on ABO/Rh tests (and pending tests) results. Saves the
	 * BloodTypingStatus result in the resultSet.
	 * 
	 * @param resultSet BloodTestingRuleResultSet that contains the processed test results.
	 */
	private void setBloodMatchStatus(BloodTestingRuleResultSet resultSet) {
		// Determine the blood status based on ABO/Rh tests results
		BloodTypingStatus bloodTypingStatus = BloodTypingStatus.NOT_DONE;
		
		if (resultSet.getNumBloodTypingTests() > 0) {
			bloodTypingStatus = BloodTypingStatus.NO_MATCH;
		}
		int numAboChanges = resultSet.getBloodAboChanges().size();
		int numRhChanges = resultSet.getBloodRhChanges().size();
		if (numAboChanges > 1 || numRhChanges > 1) {
			bloodTypingStatus = BloodTypingStatus.AMBIGUOUS;
		}
		int numPendingAboTests = resultSet.getPendingAboTestsIds().size();
		if (numAboChanges == 0 && numPendingAboTests > 0) {
			bloodTypingStatus = BloodTypingStatus.PENDING_TESTS;
		}
		int numPendingRhTests = resultSet.getPendingRhTestsIds().size();
		if (numRhChanges == 0 && numPendingRhTests > 0) {
			bloodTypingStatus = BloodTypingStatus.PENDING_TESTS;
		}
		if (numAboChanges == 1 && numRhChanges == 1) {
			bloodTypingStatus = BloodTypingStatus.COMPLETE;
		}
		
		resultSet.setBloodTypingStatus(bloodTypingStatus);
	}
	
	/**
	 * Check ABO/Rh results against donor's ABO/Rh and saves the BloodTypingMatchStatus result in
	 * the resultSet
	 * 
	 * @param resultSet BloodTestingRuleResultSet that contains the processed test results.
	 * @param collectedSample CollectedSample containing the information about the Donor.
	 */
	private void setBloodTypingMatchStatus(BloodTestingRuleResultSet resultSet, CollectedSample collectedSample) {
		BloodTypingMatchStatus bloodTypingMatchStatus = BloodTypingMatchStatus.NOT_DONE;
		
		Donor donor = collectedSample.getDonor();
		if (collectedSample.getBloodAbo() != null && collectedSample.getBloodRh() != null) {
			// first time donor - required to enter in confirmatory result
			if (donor.getBloodAbo().equals("") || donor.getBloodRh().equals("")) {
				bloodTypingMatchStatus = BloodTypingMatchStatus.NO_MATCH;
			}
			// ambiguous result - required to enter in confirmatory result
			else if ((!donor.getBloodAbo().equals("") && !donor.getBloodAbo().equals(collectedSample.getBloodAbo()))
			        || (!donor.getBloodRh().equals("") && !donor.getBloodRh().equals(collectedSample.getBloodRh()))) {
				bloodTypingMatchStatus = BloodTypingMatchStatus.AMBIGUOUS;
			}
			// blood Abo/Rh matches
			else if ((!donor.getBloodAbo().equals("") && donor.getBloodAbo().equals(collectedSample.getBloodAbo()))
			        && (!donor.getBloodRh().equals("") && donor.getBloodRh().equals(collectedSample.getBloodRh()))) {
				bloodTypingMatchStatus = BloodTypingMatchStatus.MATCH;
			}
		}
		
		resultSet.setBloodTypingMatchStatus(bloodTypingMatchStatus);
		collectedSample.setBloodTypingMatchStatus(bloodTypingMatchStatus);
	}
	
	/**
	 * Check the TTI status and determine if it is safe or not. Result will be stored in the
	 * resultSet.
	 * 
	 * @param resultSet BloodTestingRuleResultSet that contains the processed test results.
	 */
	private void setTTIStatus(BloodTestingRuleResultSet resultSet) {
		TTIStatus ttiStatus = TTIStatus.NOT_DONE;
		
		Set<String> ttiStatusChanges = resultSet.getTtiStatusChanges();
		if (!ttiStatusChanges.isEmpty()) {
			if (ttiStatusChanges.contains(TTIStatus.TTI_UNSAFE.toString())) {
				ttiStatus = TTIStatus.TTI_UNSAFE;
			} else if (ttiStatusChanges.size() == 1 && ttiStatusChanges.contains(TTIStatus.TTI_SAFE.toString())) {
				ttiStatus = TTIStatus.TTI_SAFE;
			}
		}
		
		Set<Integer> basicTtiTestsNotDone = resultSet.getBasicTtiTestsNotDone();
		if (ttiStatus.equals(TTIStatus.TTI_SAFE) && !basicTtiTestsNotDone.isEmpty()) {
			// the test has been marked as safe while some basic TTI Tests were not done
			ttiStatus = TTIStatus.NOT_DONE;
		}
		
		resultSet.setTtiStatus(ttiStatus);
	}
}
