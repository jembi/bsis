package repository.bloodtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;
import model.collectedsample.CollectedSample;
import viewmodel.BloodTestResultViewModel;
import viewmodel.BloodTestingRuleResult;

/**
 * Collects the intermediate analysis of the CollectedSample during the processing of the
 * BloodTestingRules and generates the final outcomes.
 * 
 * @see BloodTestingRuleResult
 */
public class BloodTestingRuleResultSet {
	
	/* the blood donation */
	private CollectedSample collectedSample;
	
	/* the previously saved test results */
	private Map<String, String> storedTestResults;
	
	/* the saved test results combined with the current test results */
	private Map<String, String> availableTestResults;
	
	/* only the recent (saved) blood test results */
	private Map<Integer, BloodTestResult> recentTestResults;
	
	/* indicates that there was no match for an ABO test */
	private boolean aboUninterpretable = false;
	
	/* indicates that there was no match for an Rh test */
	private boolean rhUninterpretable = false;
	
	/* indicates that there was no match for a TTI test */
	private boolean ttiUninterpretable = false;
	
	/* how many blood typing tests were done */
	private Integer numBloodTypingTests = 0;
	
	/* the status of the collectedSample blood typing */
	private BloodTypingStatus bloodTypingStatus;
	
	/* the status of the blood typing match between the collectedSample and the donor */
	private BloodTypingMatchStatus bloodTypingMatchStatus;
	
	/* the TTI status of the collectedSample */
	private TTIStatus ttiStatus;
	
	/* collection of the basic TTI tests that weren't done */
	private Set<Integer> basicTtiTestsNotDone = new HashSet<Integer>();
	
	/* collection of the various blood typing ABO tests done */
	private Set<String> bloodAboChanges = new HashSet<String>();
	
	/* collection of the various blood typing Rh tests done */
	private Set<String> bloodRhChanges = new HashSet<String>();
	
	/* collection of the various blood typing TTI tests done */
	private Set<String> ttiStatusChanges = new HashSet<String>();
	
	/* collection of the extra and new information provided by the BloodTestingRules */
	private Set<String> extraInformation = new HashSet<String>();
	
	/* collection of the ABO tests that are still outstanding */
	private List<String> pendingAboTestsIds = new ArrayList<String>();
	
	/* collection of the Rh tests that are still outstanding */
	private List<String> pendingRhTestsIds = new ArrayList<String>();
	
	/* collection of the TTI tests that are still outstanding */
	private List<String> pendingTtiTestsIds = new ArrayList<String>();
	
	/**
	 * Creates and initialises the BloodTestingRuleResultSet
	 * 
	 * @param collectedSample CollectedSample regarding the blood donation
	 * @param storedTestResults Map<String, String> of the saved test results
	 * @param availableTestResults Map<String, String> of the available test results (saved and latest)
	 * @param recentTestResults Map<Integer, BloodTestResult> of the most recent test results
	 */
	public BloodTestingRuleResultSet(CollectedSample collectedSample, Map<String, String> storedTestResults,
	    Map<String, String> availableTestResults, Map<Integer, BloodTestResult> recentTestResults) {
		this.collectedSample = collectedSample;
		this.storedTestResults = storedTestResults;
		this.availableTestResults = availableTestResults;
		this.recentTestResults = recentTestResults;
	}
	
	/**
	 * Generates the BloodTestingRuleResult based on the data collected.
	 * 
	 * @return BloodTestingRuleResult
	 */
	public BloodTestingRuleResult buildBloodTestingRuleResult() {
		BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
		
		// the blood donation
		ruleResult.setCollectedSample(collectedSample);
		
		// test data in various formats
		ruleResult.setStoredTestResults(storedTestResults);
		Map<String, BloodTestResultViewModel> recentTestResultsViewModel = new HashMap<String, BloodTestResultViewModel>();
		for (Integer testId : recentTestResults.keySet()) {
			recentTestResultsViewModel.put(testId.toString(), new BloodTestResultViewModel(recentTestResults.get(testId)));
		}
		ruleResult.setRecentTestResults(recentTestResultsViewModel);
		ruleResult.setAvailableTestResults(availableTestResults);
		
		// pending tests
		List<String> pendingBloodTypingTestsIds = new ArrayList<String>();
		pendingBloodTypingTestsIds.addAll(pendingAboTestsIds);
		pendingBloodTypingTestsIds.addAll(pendingRhTestsIds);
		ruleResult.setPendingBloodTypingTestsIds(pendingBloodTypingTestsIds);
		ruleResult.setPendingTTITestsIds(pendingTtiTestsIds);
		
		// blood typing results
		ruleResult.setBloodTypingMatchStatus(bloodTypingMatchStatus);
		ruleResult.setBloodTypingStatus(bloodTypingStatus);
		
		// determine if there are any uninterpretable results
		if (bloodAboChanges.isEmpty() && pendingAboTestsIds.isEmpty() && aboUninterpretable) {
			// there was an attempt to match a rule for blood ABO
			ruleResult.setAboUninterpretable(true);
		}
		if (bloodRhChanges.isEmpty() && pendingRhTestsIds.isEmpty() && rhUninterpretable) {
			// there was an attempt to match a rule for blood Rh
			ruleResult.setRhUninterpretable(true);
		}
		
		ruleResult.setAllBloodAboChanges(bloodAboChanges);
		ruleResult.setAllBloodRhChanges(bloodRhChanges);
		String bloodAbo = "";
		if (bloodAboChanges != null && bloodAboChanges.size() == 1) {
			bloodAbo = bloodAboChanges.iterator().next();
		}
		ruleResult.setBloodAbo(bloodAbo);
		String bloodRh = "";
		if (bloodRhChanges != null && bloodRhChanges.size() == 1) {
			bloodRh = bloodRhChanges.iterator().next();
		}
		ruleResult.setBloodRh(bloodRh);
		ruleResult.setExtraInformation(extraInformation);
		
		// TTI test information
		ruleResult.setTTIStatus(ttiStatus);
		ruleResult.setTTIStatusChanges(ttiStatusChanges);
		ruleResult.setTTIStatus(ttiStatus);
		ruleResult.setTtiUninterpretable(false);
		
		return ruleResult;
	}
	
	public CollectedSample getCollectedSample() {
		return collectedSample;
	}
	
	public void setCollectedSample(CollectedSample collectedSample) {
		this.collectedSample = collectedSample;
	}
	
	public Map<Integer, BloodTestResult> getRecentTestResults() {
		return recentTestResults;
	}
	
	public Set<Integer> getBasicTtiTestsNotDone() {
		return basicTtiTestsNotDone;
	}
	
	public void setBasicTtiTestsNotDone(Set<Integer> basicTtiTestsNotDone) {
		this.basicTtiTestsNotDone = basicTtiTestsNotDone;
	}
	
	public Integer getNumBloodTypingTests() {
		return numBloodTypingTests;
	}
	
	public void setNumBloodTypingTests(Integer numBloodTypingTests) {
		this.numBloodTypingTests = numBloodTypingTests;
	}
	
	public BloodTypingStatus getBloodTypingStatus() {
		return bloodTypingStatus;
	}
	
	public BloodTypingMatchStatus getBloodTypingMatchStatus() {
		return bloodTypingMatchStatus;
	}
	
	public void setBloodTypingMatchStatus(BloodTypingMatchStatus bloodTypingMatchStatus) {
		this.bloodTypingMatchStatus = bloodTypingMatchStatus;
	}
	
	public void setBloodTypingStatus(BloodTypingStatus bloodTypingStatus) {
		this.bloodTypingStatus = bloodTypingStatus;
	}
	
	public TTIStatus getTtiStatus() {
		return ttiStatus;
	}
	
	public void setTtiStatus(TTIStatus ttiStatus) {
		this.ttiStatus = ttiStatus;
	}
	
	public boolean isAboUninterpretable() {
		return aboUninterpretable;
	}
	
	public void setAboUninterpretable(boolean aboUninterpretable) {
		this.aboUninterpretable = aboUninterpretable;
	}
	
	public boolean isRhUninterpretable() {
		return rhUninterpretable;
	}
	
	public void setRhUninterpretable(boolean rhUninterpretable) {
		this.rhUninterpretable = rhUninterpretable;
	}
	
	public boolean isTtiUninterpretable() {
		return ttiUninterpretable;
	}
	
	public void setTtiUninterpretable(boolean ttiUninterpretable) {
		this.ttiUninterpretable = ttiUninterpretable;
	}
	
	public Set<String> getBloodAboChanges() {
		return bloodAboChanges;
	}
	
	public void addBloodAboChanges(String bloodAboChange) {
		this.bloodAboChanges.add(bloodAboChange);
	}
	
	public Set<String> getBloodRhChanges() {
		return bloodRhChanges;
	}
	
	public void addBloodRhChanges(String bloodRhChange) {
		this.bloodRhChanges.add(bloodRhChange);
	}
	
	public Set<String> getTtiStatusChanges() {
		return ttiStatusChanges;
	}
	
	public void addTtiStatusChanges(String ttiStatusChange) {
		this.ttiStatusChanges.add(ttiStatusChange);
	}
	
	public Set<String> getExtraInformation() {
		return extraInformation;
	}
	
	public void addExtraInformation(String extraInformation) {
		this.extraInformation.add(extraInformation);
	}
	
	public List<String> getPendingAboTestsIds() {
		return pendingAboTestsIds;
	}
	
	public void addPendingAboTestsIds(String pendingAboTestsId) {
		this.pendingAboTestsIds.add(pendingAboTestsId);
	}
	
	public List<String> getPendingRhTestsIds() {
		return pendingRhTestsIds;
	}
	
	public void addPendingRhTestsIds(String pendingRhTestsId) {
		this.pendingRhTestsIds.add(pendingRhTestsId);
	}
	
	public List<String> getPendingTtiTestsIds() {
		return pendingTtiTestsIds;
	}
	
	public void addPendingTtiTestsIds(String pendingTtiTestsId) {
		this.pendingTtiTestsIds.add(pendingTtiTestsId);
	}
}
