package org.jembi.bsis.model.bloodtesting.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
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
  private Map<UUID, String> storedTestResults;

  /* the saved test results combined with the current test results */
  private Map<UUID, String> availableTestResults;

  /* only the recent (saved) blood test results */
  private Map<UUID, BloodTestResult> recentTestResults;

  /* how many blood typing tests were done */
  private Integer numBloodTypingTests = 0;

  /* the status of the donation blood typing */
  private BloodTypingStatus bloodTypingStatus;

  /* the status of the blood typing match between the donation and the donor */
  private BloodTypingMatchStatus bloodTypingMatchStatus;

  /* the TTI status of the donation */
  private TTIStatus ttiStatus;

  /* collection of the basic TTI tests that weren't done */
  private Set<UUID> basicTtiTestsNotDone = new HashSet<>();

  /* collection of the various blood typing ABO tests done */
  private Set<String> bloodAboChanges = new HashSet<>();

  /* collection of the various blood typing Rh tests done */
  private Set<String> bloodRhChanges = new HashSet<>();

  /* collection of the various blood typing TTI tests done */
  private Set<String> ttiStatusChanges = new HashSet<>();

  /* collection of the blood typing Titre test outcomes */
  private Set<String> titreChanges = new HashSet<>();

  /* collection of the ABO tests that are still outstanding */
  private List<UUID> pendingAboTestsIds = new ArrayList<>();

  /* collection of the Rh tests that are still outstanding */
  private List<UUID> pendingRhTestsIds = new ArrayList<>();

  /* collection of the TTI Confirmatory tests that are still outstanding */
  private List<UUID> pendingConfirmatoryTtiTestsIds = new ArrayList<>();

  /* collection of the TTI Repeat tests that are still outstanding */
  private List<UUID> pendingRepeatTtiTestsIds = new ArrayList<>();

  /* collection of the TTI Repeat and Confimatory tests that are still outstanding */
  private List<UUID> pendingRepeatAndConfirmatoryTtiTestsIds = new ArrayList<>();

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
  public BloodTestingRuleResultSet(Donation donation, Map<UUID, String> storedTestResults,
                                   Map<UUID, String> availableTestResults, Map<UUID, BloodTestResult> recentTestResults,
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

  public Map<UUID, BloodTestResult> getRecentTestResults() {
    return recentTestResults;
  }

  public Set<UUID> getBasicTtiTestsNotDone() {
    return basicTtiTestsNotDone;
  }

  public void setBasicTtiTestsNotDone(Set<UUID> basicTtiTestsNotDone) {
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

  public Set<String> getTitreChanges() {
    return titreChanges;
  }

  public void addTitreChanges(String titreChange) {
    this.titreChanges.add(titreChange);
  }

  public List<UUID> getPendingAboTestsIds() {
    return pendingAboTestsIds;
  }

  public void addPendingAboTestsIds(UUID pendingAboTestsId) {
    this.pendingAboTestsIds.add(pendingAboTestsId);
  }

  public List<UUID> getPendingRhTestsIds() {
    return pendingRhTestsIds;
  }

  public void addPendingRhTestsIds(UUID pendingRhTestsId) {
    this.pendingRhTestsIds.add(pendingRhTestsId);
  }

  public List<UUID> getPendingConfirmatoryTtiTestsIds() {
    return pendingConfirmatoryTtiTestsIds;
  }

  public void addPendingConfirmatoryTtiTestsIds(UUID pendingConfirmatoryTtiTestsIds) {
    this.pendingConfirmatoryTtiTestsIds.add(pendingConfirmatoryTtiTestsIds);
  }

  public List<UUID> getPendingRepeatTtiTestsIds() {
    return pendingRepeatTtiTestsIds;
  }

  public void addPendingRepeatTtiTestsIds(UUID pendingRepeatTtiTestsId) {
    this.pendingRepeatTtiTestsIds.add(pendingRepeatTtiTestsId);
  }

  public List<UUID> getPendingRepeatAndConfirmatoryTtiTestsIds() {
    return pendingRepeatAndConfirmatoryTtiTestsIds;
  }

  public void addPendingRepeatAndConfirmatoryTtiTestsIds(UUID pendingRepeatAndConfirmatoryTtiTestsIds) {
    this.pendingRepeatAndConfirmatoryTtiTestsIds.add(pendingRepeatAndConfirmatoryTtiTestsIds);
  }

  public Map<UUID, String> getStoredTestResults() {
    return storedTestResults;
  }


  public void setStoredTestResults(Map<UUID, String> storedTestResults) {
    this.storedTestResults = storedTestResults;
  }


  public Map<UUID, String> getAvailableTestResults() {
    return availableTestResults;
  }


  public void setAvailableTestResults(Map<UUID, String> availableTestResults) {
    this.availableTestResults = availableTestResults;
  }


  public void setRecentTestResults(Map<UUID, BloodTestResult> recentTestResults) {
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


  public void setPendingAboTestsIds(List<UUID> pendingAboTestsIds) {
    this.pendingAboTestsIds = pendingAboTestsIds;
  }


  public void setPendingRhTestsIds(List<UUID> pendingRhTestsIds) {
    this.pendingRhTestsIds = pendingRhTestsIds;
  }


  public void setPendingConfirmatoryTtiTestsIds(List<UUID> pendingConfirmatoryTtiTestsIds) {
    this.pendingConfirmatoryTtiTestsIds = pendingConfirmatoryTtiTestsIds;
  }

  public void setPendingRepeatTtiTestsIds(List<UUID> pendingRepeatTtiTestsIds) {
    this.pendingRepeatTtiTestsIds = pendingRepeatTtiTestsIds;
  }

  public void setPendingRepeatAndConfirmatoryTtiTestsIds(List<UUID> pendingRepeatAndConfirmatoryTtiTestsIds) {
    this.pendingRepeatAndConfirmatoryTtiTestsIds = pendingRepeatAndConfirmatoryTtiTestsIds;
  }

  public List<BloodTestingRule> getBloodTestingRules() {
    return bloodTestingRules;
  }

  public void setBloodTestingRules(List<BloodTestingRule> bloodTestingRules) {
    this.bloodTestingRules = bloodTestingRules;
  }

}
