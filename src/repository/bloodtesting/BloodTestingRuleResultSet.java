package repository.bloodtesting;

import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;
import model.bloodtesting.rules.BloodTestingRule;
import model.donation.Donation;
import org.apache.log4j.Logger;
import viewmodel.BloodTestingRuleResult;

import java.util.*;

/**
 * Collects the intermediate analysis of the Donation during the processing of the
 * BloodTestingRules and generates the final outcomes.
 *
 * @see BloodTestingRuleResult
 */
public class BloodTestingRuleResultSet {

  @SuppressWarnings("unused")
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
  private Set<Integer> basicTtiTestsNotDone = new HashSet<>();

  /* collection of the various blood typing ABO tests done */
  private Set<String> bloodAboChanges = new HashSet<>();

  /* collection of the various blood typing Rh tests done */
  private Set<String> bloodRhChanges = new HashSet<>();

  /* collection of the various blood typing TTI tests done */
  private Set<String> ttiStatusChanges = new HashSet<>();

  /* collection of the extra and new information provided by the BloodTestingRules */
  private Set<String> extraInformation = new HashSet<>();

  /* collection of the ABO tests that are still outstanding */
  private List<String> pendingAboTestsIds = new ArrayList<>();

  /* collection of the Rh tests that are still outstanding */
  private List<String> pendingRhTestsIds = new ArrayList<>();

  /* collection of the TTI tests that are still outstanding */
  private List<String> pendingTtiTestsIds = new ArrayList<>();

  /* collection of the TTI and Serology tests that have been done */
  private List<BloodTestingRule> bloodTestingRules = new ArrayList<>();

  /**
   * Creates and initialises the BloodTestingRuleResultSet
   *
   * @param donation             Donation regarding the blood donation
   * @param storedTestResults    Map<String, String> of the saved test results
   * @param availableTestResults Map<String, String> of the available test results (saved and latest)
   * @param recentTestResults    Map<Integer, BloodTestResult> of the most recent test results
   * @param bloodTestingRules    List<BloodTestingRules> of the tests performed
   */
  public BloodTestingRuleResultSet(Donation donation, Map<String, String> storedTestResults,
                                   Map<String, String> availableTestResults, Map<Integer, BloodTestResult> recentTestResults,
                                   List<BloodTestingRule> bloodTestingRules) {
    this.donation = donation;
    this.storedTestResults = storedTestResults;
    this.availableTestResults = availableTestResults;
    this.recentTestResults = recentTestResults;
    this.bloodTestingRules = bloodTestingRules;
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

  public void setRecentTestResults(Map<Integer, BloodTestResult> recentTestResults) {
    this.recentTestResults = recentTestResults;
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

  public void setBloodTypingStatus(BloodTypingStatus bloodTypingStatus) {
    this.bloodTypingStatus = bloodTypingStatus;
  }

  public BloodTypingMatchStatus getBloodTypingMatchStatus() {
    return bloodTypingMatchStatus;
  }

  public void setBloodTypingMatchStatus(BloodTypingMatchStatus bloodTypingMatchStatus) {
    this.bloodTypingMatchStatus = bloodTypingMatchStatus;
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

  public void setBloodAboChanges(Set<String> bloodAboChanges) {
    this.bloodAboChanges = bloodAboChanges;
  }

  public void addBloodAboChanges(String bloodAboChange) {
    this.bloodAboChanges.add(bloodAboChange);
  }

  public Set<String> getBloodRhChanges() {
    return bloodRhChanges;
  }

  public void setBloodRhChanges(Set<String> bloodRhChanges) {
    this.bloodRhChanges = bloodRhChanges;
  }

  public void addBloodRhChanges(String bloodRhChange) {
    this.bloodRhChanges.add(bloodRhChange);
  }

  public Set<String> getTtiStatusChanges() {
    return ttiStatusChanges;
  }

  public void setTtiStatusChanges(Set<String> ttiStatusChanges) {
    this.ttiStatusChanges = ttiStatusChanges;
  }

  public void addTtiStatusChanges(String ttiStatusChange) {
    this.ttiStatusChanges.add(ttiStatusChange);
  }

  public Set<String> getExtraInformation() {
    return extraInformation;
  }

  public void setExtraInformation(Set<String> extraInformation) {
    this.extraInformation = extraInformation;
  }

  public void addExtraInformation(String extraInformation) {
    this.extraInformation.add(extraInformation);
  }

  public List<String> getPendingAboTestsIds() {
    return pendingAboTestsIds;
  }

  public void setPendingAboTestsIds(List<String> pendingAboTestsIds) {
    this.pendingAboTestsIds = pendingAboTestsIds;
  }

  public void addPendingAboTestsIds(String pendingAboTestsId) {
    this.pendingAboTestsIds.add(pendingAboTestsId);
  }

  public List<String> getPendingRhTestsIds() {
    return pendingRhTestsIds;
  }

  public void setPendingRhTestsIds(List<String> pendingRhTestsIds) {
    this.pendingRhTestsIds = pendingRhTestsIds;
  }

  public void addPendingRhTestsIds(String pendingRhTestsId) {
    this.pendingRhTestsIds.add(pendingRhTestsId);
  }

  public List<String> getPendingTtiTestsIds() {
    return pendingTtiTestsIds;
  }

  public void setPendingTtiTestsIds(List<String> pendingTtiTestsIds) {
    this.pendingTtiTestsIds = pendingTtiTestsIds;
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

  public List<BloodTestingRule> getBloodTestingRules() {
    return bloodTestingRules;
  }


  public void setBloodTestingRules(List<BloodTestingRule> bloodTestingRules) {
    this.bloodTestingRules = bloodTestingRules;
  }
}
