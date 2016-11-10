package org.jembi.bsis.backingform;

import java.util.LinkedHashSet;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;

public class BloodTestingRuleBackingForm {

  private Long id;

  private BloodTest bloodTest;

  private String pattern;

  private DonationField donationFieldChanged;

  private String newInformation;

  private LinkedHashSet<String> pendingTestsIds;

  private BloodTestCategory category;

  private boolean isDeleted = false;

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public BloodTest getBloodTest () {
    return bloodTest;
  }

  public void setBloodTest (BloodTest bloodTest) {
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

  public LinkedHashSet<String> getPendingTestsIds () {
    return pendingTestsIds;
  }

  public void setPendingTestsIds (LinkedHashSet<String> pendingTestsIds) {
    this.pendingTestsIds = pendingTestsIds;
  }

  public BloodTestCategory getCategory () {
    return category;
  }

  public void setCategory (BloodTestCategory category) {
    this.category = category;
  }

  public boolean isDeleted () {
    return isDeleted;
  }

  public void setDeleted (boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
