package org.jembi.bsis.viewmodel;

import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;

public class BloodTestViewModel extends BaseViewModel {

  private String testNameShort;
  private String testName;
  private List<String> validResults;
  private List<String> negativeResults;
  private List<String> positiveResults;
  private BloodTestCategory bloodTestCategory;
  private BloodTestType bloodTestType;
  private Integer rankInCategory;
  private Boolean isActive;
  private Boolean isDeleted;

  public BloodTestViewModel() {
  }

  public String getTestNameShort() {
    return testNameShort;
  }

  public String getTestName() {
    return testName;
  }

  public List<String> getValidResults() {
    return validResults;
  }

  public BloodTestCategory getBloodTestCategory() {
    return bloodTestCategory;
  }

  public BloodTestType getBloodTestType() {
    return bloodTestType;
  }

  public List<String> getNegativeResults() {
    return negativeResults;
  }

  public List<String> getPositiveResults() {
    return positiveResults;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public Integer getRankInCategory() {
    return rankInCategory;
  }

  public void setTestNameShort(String testNameShort) {
    this.testNameShort = testNameShort;
  }

  public void setTestName(String testName) {
    this.testName = testName;
  }

  public void setValidResults(List<String> validResults) {
    this.validResults = validResults;
  }

  public void setNegativeResults(List<String> negativeResults) {
    this.negativeResults = negativeResults;
  }

  public void setPositiveResults(List<String> positiveResults) {
    this.positiveResults = positiveResults;
  }

  public void setBloodTestCategory(BloodTestCategory bloodTestCategory) {
    this.bloodTestCategory = bloodTestCategory;
  }

  public void setBloodTestType(BloodTestType bloodTestType) {
    this.bloodTestType = bloodTestType;
  }

  public void setRankInCategory(Integer rankInCategory) {
    this.rankInCategory = rankInCategory;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
