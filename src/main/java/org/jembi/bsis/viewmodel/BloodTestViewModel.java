package org.jembi.bsis.viewmodel;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;

public class BloodTestViewModel extends BaseViewModel {

  private String testName;
  private String testNameShort;
  private BloodTestCategory category;
  private BloodTestType bloodTestType;
  private Boolean isActive;
  private Boolean isDeleted;

  public String getTestName() {
    return testName;
  }

  public String getTestNameShort() {
    return testNameShort;
  }

  public BloodTestCategory getCategory() {
    return category;
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

  public void setTestName(String testName) {
    this.testName = testName;
  }

  public void setTestNameShort(String testNameShort) {
    this.testNameShort = testNameShort;
  }

  public void setCategory(BloodTestCategory category) {
    this.category = category;
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
