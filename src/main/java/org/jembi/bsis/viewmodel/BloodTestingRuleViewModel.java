package org.jembi.bsis.viewmodel;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;

public class BloodTestingRuleViewModel extends BaseViewModel {
  
  private String bloodTestNameShort;
  private DonationField donationField;
  private BloodTestCategory category;
  private String donationFieldValue;
  private Boolean isDeleted = Boolean.FALSE;
  
  public String getBloodTestNameShort() {
    return bloodTestNameShort;
  }
  
  public DonationField getDonationField() {
    return donationField;
  }
  
  public BloodTestCategory getCategory() {
    return category;
  }
  
  public String getDonationFieldValue() {
    return donationFieldValue;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }
  
  public void setBloodTestNameShort(String bloodTestNameShort) {
    this.bloodTestNameShort = bloodTestNameShort;
  }
  
  public void setDonationField(DonationField donationField) {
    this.donationField = donationField;
  }
  
  public void setCategory(BloodTestCategory category) {
    this.category = category;
  }
  
  public void setDonationFieldValue(String donationFieldValue) {
    this.donationFieldValue = donationFieldValue;
  }
  
  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
