package org.jembi.bsis.viewmodel;

import java.util.Set;

public class BloodTestFullViewModel extends BloodTestViewModel {

  private String testName;
  private Set<String> validResults;
  private Set<String> negativeResults;
  private Set<String> positiveResults;
  private Integer rankInCategory;

  public String getTestName() {
    return testName;
  }

  public Set<String> getValidResults() {
    return validResults;
  }

  public Set<String> getNegativeResults() {
    return negativeResults;
  }

  public Set<String> getPositiveResults() {
    return positiveResults;
  }

  public Integer getRankInCategory() {
    return rankInCategory;
  }

  public void setTestName(String testName) {
    this.testName = testName;
  }

  public void setValidResults(Set<String> validResults) {
    this.validResults = validResults;
  }

  public void setNegativeResults(Set<String> negativeResults) {
    this.negativeResults = negativeResults;
  }

  public void setPositiveResults(Set<String> positiveResults) {
    this.positiveResults = positiveResults;
  }

  public void setRankInCategory(Integer rankInCategory) {
    this.rankInCategory = rankInCategory;
  }
}
