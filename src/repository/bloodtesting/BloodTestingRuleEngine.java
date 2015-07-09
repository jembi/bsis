package repository.bloodtesting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;
import model.bloodtesting.rules.BloodTestingRule;
import model.bloodtesting.rules.CollectionField;
import model.collectedsample.CollectedSample;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import viewmodel.BloodTestResultViewModel;

import model.donor.Donor;
import viewmodel.BloodTestingRuleResult;
import repository.bloodtesting.BloodTypingMatchStatus;

@Repository
@Transactional
public class BloodTestingRuleEngine {
	
	@PersistenceContext
	private EntityManager em;
	
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
		
		String queryStr = "SELECT r FROM BloodTestingRule r WHERE isActive=:isActive";
		TypedQuery<BloodTestingRule> query = em.createQuery(queryStr, BloodTestingRule.class);
		
		query.setParameter("isActive", true);
		List<BloodTestingRule> rules = query.getResultList();
		
		Map<String, String> storedTestResults = new TreeMap<String, String>();
		
		Map<Integer, BloodTestResult> recentTestResults = bloodTestingRepository
		        .getRecentTestResultsForCollection(collectedSample.getId());
		for (Integer testId : recentTestResults.keySet()) {
			BloodTestResult testResult = recentTestResults.get(testId);
			storedTestResults.put(testId.toString(), testResult.getResult());
		}
		
		List<BloodTest> basicTTITests = bloodTestingRepository.getBasicTTITests();
		Set<Integer> basicTTITestsNotAdded = new HashSet<Integer>();
		
		for (BloodTest bt : basicTTITests) {
			basicTTITestsNotAdded.add(bt.getId());
		}
		
		Map<String, String> availableTestResults = new TreeMap<String, String>();
		availableTestResults.putAll(storedTestResults);
		for (String storedTestId : storedTestResults.keySet()) {
			basicTTITestsNotAdded.remove(new Integer(storedTestId.toString()));
		}
		for (Long extraTestId : bloodTestResults.keySet()) {
			// for rule comparison we are overwriting existing test results with new test results
			availableTestResults.put(extraTestId.toString(), bloodTestResults.get(extraTestId));
			// this will check if all basic tti tests are available
			basicTTITestsNotAdded.remove(new Integer(extraTestId.toString()));
		}
		
		//System.out.println("available test results:" + availableTestResults);
		//System.out.println("basic TTI Tests not added:" + basicTTITestsNotAdded);
		
		Set<String> bloodAboChanges = new HashSet<String>();
		Set<String> bloodRhChanges = new HashSet<String>();
		Set<String> ttiStatusChanges = new HashSet<String>();
		Set<String> extraInformation = new HashSet<String>();
		List<String> pendingAboTestsIds = new ArrayList<String>();
		List<String> pendingRhTestsIds = new ArrayList<String>();
		List<String> pendingTtiTestsIds = new ArrayList<String>();
		
		boolean aboUninterpretable = false;
		boolean rhUninterpretable = false;
		@SuppressWarnings("unused")
		boolean ttiUninterpretable = false;
		
		for (BloodTestingRule rule : rules) {
			
			List<String> pattern = Arrays.asList(rule.getPattern().split(","));
			boolean patternMatch = true;
			int indexInPattern = 0;
			
			List<String> missingTestIdsForRule = new ArrayList<String>();
			List<String> testIds = Arrays.asList(rule.getBloodTestsIds().split(","));
			
			boolean atLeastOneResultFoundForPattern = false;
			for (String testId : testIds) {
				String actualResult = availableTestResults.get(testId);
				if (actualResult == null) {
					missingTestIdsForRule.add(testId);
					patternMatch = false;
					continue;
				}
				atLeastOneResultFoundForPattern = true;
				String expectedResult = pattern.get(indexInPattern);
				if (!expectedResult.equals(actualResult)) {
					patternMatch = false;
				}
				indexInPattern = indexInPattern + 1;
			}
			
			//      System.out.println();
			if (patternMatch) {
				System.out.println("Pattern matched for rule.");
				//        System.out.println("test ids: " + rule.getBloodTypingTestIds() + ", " +
				//                           "pattern: " + rule.getPattern() + ", " +
				//                           "input pattern: " + inputPattern);
				//        System.out.println("extra tests to be done: " + rule.getExtraTestsIds());
				//        System.out.println("Changes to result: " +
				//                            rule.getPart() + ", " + rule.getNewInformation() + ", " +
				//                            rule.getExtraInformation() + ", " + rule.getMarkSampleAsUnsafe());
				//
				CollectionField collectionFieldChanged = rule.getCollectionFieldChanged();
				switch (collectionFieldChanged) {
					case BLOODABO:
						bloodAboChanges.add(rule.getNewInformation());
						break;
					case BLOODRH:
						bloodRhChanges.add(rule.getNewInformation());
						break;
					case TTISTATUS:
						ttiStatusChanges.add(rule.getNewInformation());
						break;
					case EXTRA:
						extraInformation.add(rule.getNewInformation());
						break;
				}
				
				if (StringUtils.isNotBlank(rule.getExtraInformation()))
					extraInformation.add(rule.getExtraInformation());
				
				// find extra tests for ABO
				if (StringUtils.isNotBlank(rule.getPendingTestsIds())) {
					for (String extraTestId : rule.getPendingTestsIds().split(",")) {
						if (!availableTestResults.containsKey(extraTestId)) {
							switch (rule.getSubCategory()) {
								case BLOODABO:
									pendingAboTestsIds.add(extraTestId);
									break;
								case BLOODRH:
									pendingRhTestsIds.add(extraTestId);
									break;
								case TTI:
									pendingTtiTestsIds.add(extraTestId);
									break;
							}
						}
					}
				}
				
			} else {
				// pattern did not match
				CollectionField collectionFieldChanged = rule.getCollectionFieldChanged();
				switch (collectionFieldChanged) {
					case BLOODABO:
						if (atLeastOneResultFoundForPattern)
							aboUninterpretable = true;
						break;
					case BLOODRH:
						if (atLeastOneResultFoundForPattern)
							rhUninterpretable = true;
						break;
					case TTISTATUS:
						if (atLeastOneResultFoundForPattern)
							ttiUninterpretable = true;
						break;
				}
			}
		}
		
		String bloodAbo = "";
		String bloodRh = "";
		
		BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
		ruleResult.setCollectedSample(collectedSample);
		BloodTypingStatus bloodTypingStatus = BloodTypingStatus.NOT_DONE;
		
		int numBloodTypingTests = 0;
		for (BloodTest bt : bloodTestingRepository.getBloodTypingTests()) {
			if (availableTestResults.containsKey((bt.getId().toString())))
				numBloodTypingTests++;
		}
		
		// if even one blood typing test is done then we cannot say
		// blood typing status is NOT_DONE
		if (numBloodTypingTests > 0)
			bloodTypingStatus = BloodTypingStatus.NO_MATCH;
		
		if (bloodAboChanges.size() > 1 || bloodRhChanges.size() > 1) {
			bloodTypingStatus = BloodTypingStatus.AMBIGUOUS;
		} else {
			if (bloodAboChanges.size() == 1)
				bloodAbo = bloodAboChanges.iterator().next();
			if (bloodRhChanges.size() == 1)
				bloodRh = bloodRhChanges.iterator().next();
		}
		
		if (bloodAboChanges.isEmpty()) {
			if (pendingAboTestsIds.size() > 0) {
				bloodTypingStatus = BloodTypingStatus.PENDING_TESTS;
			} else if (aboUninterpretable) {
				// there was an attempt to match a rule for blood abo
				ruleResult.setAboUninterpretable(true);
			}
		}
		
		if (bloodRhChanges.isEmpty()) {
			if (pendingRhTestsIds.size() > 0) {
				bloodTypingStatus = BloodTypingStatus.PENDING_TESTS;
			} else if (rhUninterpretable) {
				// there was an attempt to match a rule for blood abo
				ruleResult.setRhUninterpretable(true);
			}
		}
		
		if (bloodAboChanges.size() == 1 && bloodRhChanges.size() == 1) {
			bloodTypingStatus = BloodTypingStatus.COMPLETE;
		}
		
		// Check ABO/Rh results against donor's ABO/Rh
		// If donor's ABO/Rh empty - considered as First Time Donor 
		//		- required to enter confirmatory results
		// If donation ABO/Rh do not match donor's ABO/Rh, required to enter confirmatory results 
		//		- these results overwrite donor's existing values
		Donor donor = collectedSample.getDonor();
		if (collectedSample.getBloodAbo() != null && collectedSample.getBloodRh() != null) {
			
			// first time donor - required to enter in confirmatory result
			if (donor.getBloodAbo().equals("") || donor.getBloodRh().equals("")) {
				ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NO_MATCH);
			}
			
			// ambiguous result - required to enter in confirmatory result
			else if ((!donor.getBloodAbo().equals("") && !donor.getBloodAbo().equals(collectedSample.getBloodAbo()))
			        || (!donor.getBloodRh().equals("") && !donor.getBloodRh().equals(collectedSample.getBloodRh()))) {
				ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS);
			}
			
			// blood Abo/Rh matches
			else if ((!donor.getBloodAbo().equals("") && donor.getBloodAbo().equals(collectedSample.getBloodAbo()))
			        && (!donor.getBloodRh().equals("") && donor.getBloodRh().equals(collectedSample.getBloodRh()))) {
				ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
			}
		} else {
			ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
		}
		
		collectedSample.setBloodTypingMatchStatus(ruleResult.getBloodTypingMatchStatus());
		
		System.out.println(ttiStatusChanges);
		TTIStatus ttiStatus = TTIStatus.NOT_DONE;
		if (!ttiStatusChanges.isEmpty()) {
			if (ttiStatusChanges.contains(TTIStatus.TTI_UNSAFE.toString())) {
				ttiStatus = TTIStatus.TTI_UNSAFE;
			} else if (ttiStatusChanges.size() == 1 && ttiStatusChanges.contains(TTIStatus.TTI_SAFE.toString())) {
				ttiStatus = TTIStatus.TTI_SAFE;
			}
		}
		
		if (ttiStatus.equals(TTIStatus.TTI_SAFE) && !basicTTITestsNotAdded.isEmpty()) {
			// the test has been marked as safe while some basic TTI Tests were not done
			ttiStatus = TTIStatus.NOT_DONE;
		}
		
		List<String> pendingBloodTypingTestsIds = new ArrayList<String>();
		pendingBloodTypingTestsIds.addAll(pendingAboTestsIds);
		pendingBloodTypingTestsIds.addAll(pendingRhTestsIds);
		ruleResult.setAllBloodAboChanges(bloodAboChanges);
		ruleResult.setAllBloodRhChanges(bloodRhChanges);
		ruleResult.setBloodAbo(bloodAbo);
		ruleResult.setBloodRh(bloodRh);
		ruleResult.setExtraInformation(extraInformation);
		ruleResult.setPendingBloodTypingTestsIds(pendingBloodTypingTestsIds);
		ruleResult.setPendingTTITestsIds(pendingTtiTestsIds);
		ruleResult.setAvailableTestResults(availableTestResults);
		ruleResult.setBloodTypingStatus(bloodTypingStatus);
		ruleResult.setStoredTestResults(storedTestResults);
		
		Map<String, BloodTestResultViewModel> recentTestResultsWithStringKey = new HashMap<String, BloodTestResultViewModel>();
		
		for (Integer testId : recentTestResults.keySet()) {
			recentTestResultsWithStringKey.put(testId.toString(),
			    new BloodTestResultViewModel(recentTestResults.get(testId)));
		}
		
		ruleResult.setRecentTestResults(recentTestResultsWithStringKey);
		
		ruleResult.setTTIStatusChanges(ttiStatusChanges);
		ruleResult.setTTIStatus(ttiStatus);
		ruleResult.setTtiUninterpretable(false);
		
		return ruleResult;
	}
	
}
