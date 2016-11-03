package org.jembi.bsis.model.bloodtesting.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;

/**
 * Collects the intermediate analysis of the Donation during the processing of the BloodTestingRules
 * and generates the final outcomes.
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
  private Map<Long, BloodTestResult> recentTestResults;

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
  private Set<Long> basicTtiTestsNotDone = new HashSet<Long>();

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

  /* collection of the TTI Confirmatory tests that are still outstanding */
  private List<String> pendingConfirmatoryTtiTestsIds = new ArrayList<String>();

  /* collection of the TTI Repeat tests that are still outstanding */
  private List<String> pendingRepeatTtiTestsIds = new ArrayList<String>();

  /* collection of the TTI Repeat and Confimatory tests that are still outstanding */
  private List<String> pendingRepeatAndConfirmatoryTtiTestsIds = new ArrayList<String>();

  /* collection of the TTI and Serology tests that have been done */
  private List<BloodTestingRule> bloodTestingRules = new ArrayList<BloodTestingRule>();

  /**
   * Creates and initialises the BloodTestingRuleResultSet
   *
   * @param donation             Donation regarding the blood donation
   * @param storedTestResults    Map<String, String> of the saved test results
   * @param availableTestResults Map<String, String> of the available test results (saved and
   *                             latest)
   * @param recentTestResults    Map<Long, BloodTestResult> of the most recent test results
   * @param bloodTestingRules    List<BloodTestingRules> of the tests performed
   */
  public BloodTestingRuleResultSet(Donation donation, Map<String, String> storedTestResults,
                                   Map<String, String> availableTestResults, Map<Long, BloodTestResult> recentTestResults,
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

  public Map<Long, BloodTestResult> getRecentTestResults() {
    return recentTestResults;
  }

  public Set<Long> getBasicTtiTestsNotDone() {
    return basicTtiTestsNotDone;
  }

  public void setBasicTtiTestsNotDone(Set<Long> basicTtiTestsNotDone) {
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

  public List<String> getPendingConfirmatoryTtiTestsIds() {
    return pendingConfirmatoryTtiTestsIds;
  }

  public void addPendingConfirmatoryTtiTestsIds(String pendingConfirmatoryTtiTestsIds) {
    this.pendingConfirmatoryTtiTestsIds.add(pendingConfirmatoryTtiTestsIds);
  }

  public List<String> getPendingRepeatTtiTestsIds() {
    return pendingRepeatTtiTestsIds;
  }

  public void addPendingRepeatTtiTestsIds(String pendingRepeatTtiTestsId) {
    this.pendingRepeatTtiTestsIds.add(pendingRepeatTtiTestsId);
  }

  public List<String> getPendingRepeatAndConfirmatoryTtiTestsIds() {
    return pendingRepeatAndConfirmatoryTtiTestsIds;
  }

  public void addPendingRepeatAndConfirmatoryTtiTestsIds(String pendingRepeatAndConfirmatoryTtiTestsIds) {
    this.pendingRepeatAndConfirmatoryTtiTestsIds.add(pendingRepeatAndConfirmatoryTtiTestsIds);
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


  public void setRecentTestResults(Map<Long, BloodTestResult> recentTestResults) {
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


  public void setPendingConfirmatoryTtiTestsIds(List<String> pendingConfirmatoryTtiTestsIds) {
    this.pendingConfirmatoryTtiTestsIds = pendingConfirmatoryTtiTestsIds;
  }

  public void setPendingRepeatTtiTestsIds(List<String> pendingRepeatTtiTestsIds) {
    this.pendingRepeatTtiTestsIds = pendingRepeatTtiTestsIds;
  }

  public void setPendingRepeatAndConfirmatoryTtiTestsIds(List<String> pendingRepeatAndConfirmatoryTtiTestsIds) {
    this.pendingRepeatAndConfirmatoryTtiTestsIds = pendingRepeatAndConfirmatoryTtiTestsIds;
  }

  public List<BloodTestingRule> getBloodTestingRules() {
    return bloodTestingRules;
  }


  public void setBloodTestingRules(List<BloodTestingRule> bloodTestingRules) {
    this.bloodTestingRules = bloodTestingRules;
  }

}
