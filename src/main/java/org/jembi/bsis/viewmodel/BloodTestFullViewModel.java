package org.jembi.bsis.viewmodel;

import java.util.List;

public class BloodTestFullViewModel extends BloodTestViewModel {

  private String testName;
  private List<String> validResults;
  private List<String> negativeResults;
  private List<String> positiveResults;
  private Integer rankInCategory;

  public String getTestName() {
    return testName;
  }

  public List<String> getValidResults() {
    return validResults;
  }

  public List<String> getNegativeResults() {
    return negativeResults;
  }

  public List<String> getPositiveResults() {
    return positiveResults;
  }

  public Integer getRankInCategory() {
    return rankInCategory;
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

  public void setRankInCategory(Integer rankInCategory) {
    this.rankInCategory = rankInCategory;
  }
}
