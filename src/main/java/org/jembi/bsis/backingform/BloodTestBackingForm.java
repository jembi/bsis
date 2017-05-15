package org.jembi.bsis.backingform;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;

public class BloodTestBackingForm {
  
  private UUID id;
  
  private String testName;
  
  private String testNameShort;
  
  private BloodTestCategory category;
  
  private BloodTestType bloodTestType;
  
  private LinkedHashSet<String> validResults;
  
  private LinkedHashSet<String> negativeResults;
  
  private LinkedHashSet<String> positiveResults;
  
  private Boolean isActive;
  
  private Boolean isDeleted;
  
  private Boolean flagComponentsContainingPlasmaForDiscard;
  
  private Boolean flagComponentsForDiscard;
  
  private Integer rankInCategory;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
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

  public LinkedHashSet<String> getValidResults() {
    return validResults;
  }

  public void setValidResults(LinkedHashSet<String> validResults) {
    this.validResults = validResults;
  }

  public Set<String> getNegativeResults() {
    return negativeResults;
  }

  public void setNegativeResults(LinkedHashSet<String> negativeResults) {
    this.negativeResults = negativeResults;
  }

  public LinkedHashSet<String> getPositiveResults() {
    return positiveResults;
  }

  public void setPositiveResults(LinkedHashSet<String> positiveResults) {
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

  public Boolean getFlagComponentsContainingPlasmaForDiscard() {
    return flagComponentsContainingPlasmaForDiscard;
  }

  public void setFlagComponentsContainingPlasmaForDiscard(Boolean flagComponentsContainingPlasmaForDiscard) {
    this.flagComponentsContainingPlasmaForDiscard = flagComponentsContainingPlasmaForDiscard;
  }

  public Boolean getFlagComponentsForDiscard() {
    return flagComponentsForDiscard;
  }

  public void setFlagComponentsForDiscard(Boolean flagComponentsForDiscard) {
    this.flagComponentsForDiscard = flagComponentsForDiscard;
  }
  
  public Integer getRankInCategory() {
    return rankInCategory;
  }
  
  public void setRankInCategory(Integer rankInCategory) {
    this.rankInCategory = rankInCategory;
  }

}
