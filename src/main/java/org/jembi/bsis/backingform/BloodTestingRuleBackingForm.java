package org.jembi.bsis.backingform;

import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BloodTestingRuleBackingForm {

  private UUID id;

  private BloodTestBackingForm bloodTest;

  private String pattern;

  private DonationField donationFieldChanged;

  private String newInformation;

  private Set<BloodTestBackingForm> pendingTests;

  private Boolean isDeleted;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public BloodTestBackingForm getBloodTest() {
    return bloodTest;
  }

  public void setBloodTest(BloodTestBackingForm bloodTest) {
    this.bloodTest = bloodTest;
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public DonationField getDonationFieldChanged() {
    return donationFieldChanged;
  }

  public void setDonationFieldChanged(DonationField donationFieldChanged) {
    this.donationFieldChanged = donationFieldChanged;
  }

  public String getNewInformation() {
    return newInformation;
  }

  public void setNewInformation(String newInformation) {
    this.newInformation = newInformation;
  }

  public Set<BloodTestBackingForm> getPendingTests() {
    return pendingTests;
  }

  public void setPendingTests(Set<BloodTestBackingForm> pendingTests) {
    this.pendingTests = pendingTests;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  @JsonIgnore
  public void setCategory(BloodTestCategory category) {
    // ignore
  }
}
