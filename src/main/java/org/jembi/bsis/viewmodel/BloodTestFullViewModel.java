package org.jembi.bsis.viewmodel;

import java.util.Set;

public class BloodTestFullViewModel extends BloodTestViewModel {

  private String testName;
  private Set<String> validResults;
  private Set<String> negativeResults;
  private Set<String> positiveResults;
  private boolean flagComponentsForDiscard = false;
  private boolean flagComponentsContainingPlasmaForDiscard = false;

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

  public boolean isFlagComponentsForDiscard() {
    return flagComponentsForDiscard;
  }

  public void setFlagComponentsForDiscard(boolean flagComponentsForDiscard) {
    this.flagComponentsForDiscard = flagComponentsForDiscard;
  }

  public boolean isFlagComponentsContainingPlasmaForDiscard() {
    return flagComponentsContainingPlasmaForDiscard;
  }

  public void setFlagComponentsContainingPlasmaForDiscard(boolean flagComponentsContainingPlasmaForDiscard) {
    this.flagComponentsContainingPlasmaForDiscard = flagComponentsContainingPlasmaForDiscard;
  }
}
