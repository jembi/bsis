package org.jembi.bsis.viewmodel;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donation.Titre;

public class BloodTestingRuleResult {

  private DonationFullViewModel donation;

  private Set<String> allBloodAboChanges;

  private Set<String> allBloodRhChanges;

  private String bloodAbo;

  private String bloodRh;

  private Titre titre;

  private List<UUID> pendingBloodTypingTestsIds;

  private List<UUID> pendingConfirmatoryTTITestsIds;

  private List<UUID> pendingRepeatTTITestsIds;

  private List<UUID> pendingRepeatAndConfirmatoryTtiTestsIds;

  private Map<UUID, String> availableTestResults;

  private Map<UUID, BloodTestResultFullViewModel> recentTestResults;

  private BloodTypingStatus bloodTypingStatus;

  private BloodTypingMatchStatus bloodTypingMatchStatus;

  private Map<UUID, String> storedTestResults;

  // Read about Bean Naming convention in Java
  // http://stackoverflow.com/a/5599478/161628
  // http://www.oracle.com/technetwork/java/javase/documentation/spec-136004.html
  private Set<String> ttiStatusChanges;

  private TTIStatus ttiStatus;

  public BloodTestingRuleResult() {
  }

  public DonationFullViewModel getDonation() {
    return donation;
  }

  public void setDonation(DonationFullViewModel donation) {
    this.donation = donation;
  }

  public Set<String> getAllBloodAboChanges() {
    return allBloodAboChanges;
  }

  public void setAllBloodAboChanges(Set<String> allBloodAboChanges) {
    this.allBloodAboChanges = allBloodAboChanges;
  }

  public Set<String> getAllBloodRhChanges() {
    return allBloodRhChanges;
  }

  public void setAllBloodRhChanges(Set<String> allBloodRhChanges) {
    this.allBloodRhChanges = allBloodRhChanges;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public String getBloodRh() {
    return bloodRh;
  }

  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
  }

  public Titre getTitre() {
    return titre;
  }

  public void setTitre(Titre titre) {
    this.titre = titre;
  }

  public List<UUID> getPendingBloodTypingTestsIds() {
    return pendingBloodTypingTestsIds;
  }

  public void setPendingBloodTypingTestsIds(List<UUID> pendingBloodTypingTestsIds) {
    this.pendingBloodTypingTestsIds = pendingBloodTypingTestsIds;
  }

  public Map<UUID, String> getAvailableTestResults() {
    return availableTestResults;
  }

  public void setAvailableTestResults(Map<UUID, String> availableTestResults) {
    this.availableTestResults = availableTestResults;
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

  public Map<UUID, String> getStoredTestResults() {
    return storedTestResults;
  }

  public void setStoredTestResults(Map<UUID, String> storedTestResults) {
    this.storedTestResults = storedTestResults;
  }

  public Set<String> getTTIStatusChanges() {
    return ttiStatusChanges;
  }

  public void setTTIStatusChanges(Set<String> ttiStatusChanges) {
    this.ttiStatusChanges = ttiStatusChanges;
  }

  public TTIStatus getTTIStatus() {
    return ttiStatus;
  }

  public void setTTIStatus(TTIStatus ttiStatus) {
    this.ttiStatus = ttiStatus;
  }

  public List<UUID> getPendingConfirmatoryTTITestsIds() {
    return pendingConfirmatoryTTITestsIds;
  }

  public void setPendingConfirmatoryTTITestsIds(List<UUID> pendingConfirmatoryTTITestsIds) {
    this.pendingConfirmatoryTTITestsIds = pendingConfirmatoryTTITestsIds;
  }

  public List<UUID> getPendingRepeatTTITestsIds() {
    return pendingRepeatTTITestsIds;
  }

  public void setPendingRepeatTTITestsIds(List<UUID> pendingRepeatTTITestsIds) {
    this.pendingRepeatTTITestsIds = pendingRepeatTTITestsIds;
  }

  public List<UUID> getPendingRepeatAndConfirmatoryTtiTestsIds() {
    return pendingRepeatAndConfirmatoryTtiTestsIds;
  }

  public void addPendingRepeatAndConfirmatoryTtiTestsIds(UUID pendingRepeatAndConfirmatoryTtiTestsIds) {
    this.pendingRepeatAndConfirmatoryTtiTestsIds.add(pendingRepeatAndConfirmatoryTtiTestsIds);
  }

  public void setPendingRepeatAndConfirmatoryTtiTestsIds(List<UUID> pendingRepeatAndConfirmatoryTtiTestsIds) {
    this.pendingRepeatAndConfirmatoryTtiTestsIds = pendingRepeatAndConfirmatoryTtiTestsIds;
  }

  public Map<UUID, BloodTestResultFullViewModel> getRecentTestResults() {
    return recentTestResults;
  }

  public void setRecentTestResults(Map<UUID, BloodTestResultFullViewModel> recentTestResults) {
    this.recentTestResults = recentTestResults;
  }

}
