package org.jembi.bsis.viewmodel;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;

public class BloodTestViewModel extends BaseViewModel {

  private String testNameShort;
  private BloodTestCategory bloodTestCategory;
  private BloodTestType bloodTestType;
  private Boolean isActive;
  private Boolean isDeleted;

  public String getTestNameShort() {
    return testNameShort;
  }

  public BloodTestCategory getBloodTestCategory() {
    return bloodTestCategory;
  }

  public BloodTestType getBloodTestType() {
    return bloodTestType;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setTestNameShort(String testNameShort) {
    this.testNameShort = testNameShort;
  }

  public void setBloodTestCategory(BloodTestCategory bloodTestCategory) {
    this.bloodTestCategory = bloodTestCategory;
  }

  public void setBloodTestType(BloodTestType bloodTestType) {
    this.bloodTestType = bloodTestType;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
