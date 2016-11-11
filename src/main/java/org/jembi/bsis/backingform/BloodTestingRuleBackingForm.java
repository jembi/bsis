package org.jembi.bsis.backingform;

import java.util.Set;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BloodTestingRuleBackingForm {

  private Long id;

  private BloodTestBackingForm bloodTest;

  private String pattern;

  private DonationField donationFieldChanged;

  private String newInformation;

  private Set<String> pendingTestsIds;

  private Boolean isDeleted;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public BloodTestBackingForm getBloodTest () {
    return bloodTest;
  }

  public void setBloodTest (BloodTestBackingForm bloodTest) {
    this.bloodTest = bloodTest;
  }

  public String getPattern () {
    return pattern;
  }

  public void setPattern (String pattern) {
    this.pattern = pattern;
  }

  public DonationField getDonationFieldChanged () {
    return donationFieldChanged;
  }

  public void setDonationFieldChanged (DonationField donationFieldChanged) {
    this.donationFieldChanged = donationFieldChanged;
  }

  public String getNewInformation () {
    return newInformation;
  }

  public void setNewInformation (String newInformation) {
    this.newInformation = newInformation;
  }

  public Set<String> getPendingTestsIds () {
    return pendingTestsIds;
  }

  public void setPendingTestsIds (Set<String> pendingTestsIds) {
    this.pendingTestsIds = pendingTestsIds;
  }

  public Boolean getIsDeleted () {
    return isDeleted;
  }

  public void setIsDeleted (Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  @JsonIgnore
  public void setCategory(BloodTestCategory category) {
    //ignore
  }
}
