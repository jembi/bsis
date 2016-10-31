package org.jembi.bsis.backingform;

import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;

public class BloodTestBackingForm {
  
  private Long id;
  
  private String testName;
  
  private String testNameShort;
  
  private BloodTestCategory category;
  
  private BloodTestType bloodTestType;
  
  private List<String> validResults;
  
  private List<String> negativeResults;
  
  private List<String> positiveResults;
  
  private Boolean isActive;
  
  private Boolean isDeleted;
  
  private boolean flagComponentsContainingPlasmaForDiscard;
  
  private boolean flagComponentsForDiscard;
  
  public BloodTestBackingForm() {
    
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTestNameShort() {
    return testNameShort;
  }

  public void setTestNameShort(String testNameShort) {
    this.testNameShort = testNameShort;
  }

  public String getTestName() {
    return testName;
  }

  public void setTestName(String testName) {
    this.testName = testName;
  }

  public List<String> getValidResults() {
    return validResults;
  }

  public void setValidResults(List<String> validResults) {
    this.validResults = validResults;
  }

  public List<String> getNegativeResults() {
    return negativeResults;
  }

  public void setNegativeResults(List<String> negativeResults) {
    this.negativeResults = negativeResults;
  }

  public List<String> getPositiveResults() {
    return positiveResults;
  }

  public void setPositiveResults(List<String> positiveResults) {
    this.positiveResults = positiveResults;
  }

  public BloodTestType getBloodTestType() {
    return bloodTestType;
  }

  public void setBloodTestType(BloodTestType bloodTestType) {
    this.bloodTestType = bloodTestType;
  }

  public BloodTestCategory getCategory() {
    return category;
  }

  public void setCategory(BloodTestCategory category) {
    this.category = category;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public boolean isFlagComponentsContainingPlasmaForDiscard() {
    return flagComponentsContainingPlasmaForDiscard;
  }

  public void setFlagComponentsContainingPlasmaForDiscard(boolean flagComponentsContainingPlasmaForDiscard) {
    this.flagComponentsContainingPlasmaForDiscard = flagComponentsContainingPlasmaForDiscard;
  }

  public boolean isFlagComponentsForDiscard() {
    return flagComponentsForDiscard;
  }

  public void setFlagComponentsForDiscard(boolean flagComponentsForDiscard) {
    this.flagComponentsForDiscard = flagComponentsForDiscard;
  }
  
}
