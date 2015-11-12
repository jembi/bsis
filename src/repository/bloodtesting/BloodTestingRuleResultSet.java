package repository.bloodtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;

import org.apache.log4j.Logger;

import viewmodel.BloodTestingRuleResult;

/**
 * Collects the intermediate analysis of the Donation during the processing of the
 * BloodTestingRules and generates the final outcomes.
 * 
 * @see BloodTestingRuleResult
 */
public class BloodTestingRuleResultSet {
	
	private static final Logger LOGGER = Logger.getLogger(BloodTestingRuleResultSet.class);
	
	/* the blood donation */
	private Donation donation;
	
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
	
	/* the status of the donation blood typing */
	private BloodTypingStatus bloodTypingStatus;
	
	/* the status of the blood typing match between the donation and the donor */
	private BloodTypingMatchStatus bloodTypingMatchStatus;
	
	/* the TTI status of the donation */
	private TTIStatus ttiStatus;
	
	/* collection of the basic TTI tests that weren't done */
	private Set<Integer> basicTtiTestsNotDone = new HashSet<Integer>();
	
	/* map of tti tests with pending confirmation tests */
	private Map<Integer, List<Integer>> pendingTests = new HashMap<Integer, List<Integer>>();
	
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
	 * @param donation Donation regarding the blood donation
	 * @param storedTestResults Map<String, String> of the saved test results
	 * @param availableTestResults Map<String, String> of the available test results (saved and latest)
	 * @param recentTestResults Map<Integer, BloodTestResult> of the most recent test results
	 */
	public BloodTestingRuleResultSet(Donation donation, Map<String, String> storedTestResults,
	    Map<String, String> availableTestResults, Map<Integer, BloodTestResult> recentTestResults) {
		this.donation = donation;
		this.storedTestResults = storedTestResults;
		this.availableTestResults = availableTestResults;
		this.recentTestResults = recentTestResults;
	}
	
	public Donation getDonation() {
		return donation;
	}
	
	public void setDonation(Donation donation) {
		this.donation = donation;
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
	
	public boolean getAboUninterpretable() {
		return aboUninterpretable;
	}
	
	public void setAboUninterpretable(boolean aboUninterpretable) {
		this.aboUninterpretable = aboUninterpretable;
	}
	
	public boolean getRhUninterpretable() {
		return rhUninterpretable;
	}
	
	public void setRhUninterpretable(boolean rhUninterpretable) {
		this.rhUninterpretable = rhUninterpretable;
	}
	
	public boolean getTtiUninterpretable() {
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

	
    public Map<String, String> getStoredTestResults() {
    	return storedTestResults;
    }

	
    public void setStoredTestResults(Map<String, String> storedTestResults) {
    	this.storedTestResults = storedTestResults;
    }

	
    public Map<String, String> getAvailableTestResults() {
    	return availableTestResults;
    }

	
    public void setAvailableTestResults(Map<String, String> availableTestResults) {
    	this.availableTestResults = availableTestResults;
    }

	
    public void setRecentTestResults(Map<Integer, BloodTestResult> recentTestResults) {
    	this.recentTestResults = recentTestResults;
    }

	
    public void setBloodAboChanges(Set<String> bloodAboChanges) {
    	this.bloodAboChanges = bloodAboChanges;
    }

	
    public void setBloodRhChanges(Set<String> bloodRhChanges) {
    	this.bloodRhChanges = bloodRhChanges;
    }

	
    public void setTtiStatusChanges(Set<String> ttiStatusChanges) {
    	this.ttiStatusChanges = ttiStatusChanges;
    }

	
    public void setExtraInformation(Set<String> extraInformation) {
    	this.extraInformation = extraInformation;
    }

	
    public void setPendingAboTestsIds(List<String> pendingAboTestsIds) {
    	this.pendingAboTestsIds = pendingAboTestsIds;
    }

	
    public void setPendingRhTestsIds(List<String> pendingRhTestsIds) {
    	this.pendingRhTestsIds = pendingRhTestsIds;
    }

	
    public void setPendingTtiTestsIds(List<String> pendingTtiTestsIds) {
    	this.pendingTtiTestsIds = pendingTtiTestsIds;
    }

	
    public Map<Integer, List<Integer>> getPendingTests() {
    	return pendingTests;
    }

	
    public void setPendingTests(Map<Integer, List<Integer>> pendingTests) {
    	this.pendingTests = pendingTests;
    }
    
	public void addPendingTest(String testId, String pendingTestId) {
		Integer testInt = null;
		try {
			testInt = Integer.valueOf(testId);
		} catch (NumberFormatException e) {
			LOGGER.warn("Could not parse integer testId '" + testId + "'. Skipping this id.", e);
		}
		if (testInt != null) {
			List<Integer> pendingTestsForTest = pendingTests.get(testInt);
			if (pendingTestsForTest == null) {
				pendingTestsForTest = new ArrayList<Integer>();
				pendingTests.put(testInt, pendingTestsForTest);
			}
			try {
				Integer pendingInt = Integer.valueOf(pendingTestId);
				pendingTestsForTest.add(pendingInt);
			}
			catch (NumberFormatException e) {
				LOGGER.warn("Could not parse integer pendingTtiTestId '" + pendingTestId + "'. Skipping this id.", e);
			}
		}
	}
}
