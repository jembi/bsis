package org.jembi.bsis.viewmodel;

import java.util.UUID;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;

public class BloodTestingRuleViewModel extends BaseViewModel<UUID> {

  private String testNameShort;
  private DonationField donationFieldChanged;
  private BloodTestCategory category;
  private String newInformation;
  private String pattern;
  private boolean isDeleted;

  public String getTestNameShort() {
    return testNameShort;
  }

  public DonationField getDonationFieldChanged() {
    return donationFieldChanged;
  }

  public BloodTestCategory getCategory() {
    return category;
  }

  public String getNewInformation() {
    return newInformation;
  }

  public String getPattern() {
    return pattern;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setTestNameShort(String testNameShort) {
    this.testNameShort = testNameShort;
  }

  public void setDonationFieldChanged(DonationField donationFieldChanged) {
    this.donationFieldChanged = donationFieldChanged;
  }

  public void setCategory(BloodTestCategory category) {
    this.category = category;
  }

  public void setNewInformation(String newInformation) {
    this.newInformation = newInformation;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
