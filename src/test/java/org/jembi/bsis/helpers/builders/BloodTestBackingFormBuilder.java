package org.jembi.bsis.helpers.builders;

import java.util.LinkedHashSet;
import java.util.UUID;

import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;

public class BloodTestBackingFormBuilder extends AbstractBuilder<BloodTestBackingForm> {
  
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
  private boolean flagComponentsContainingPlasmaForDiscard;
  private boolean flagComponentsForDiscard;
  private Integer rankInCategory;
  
  public BloodTestBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }
  
  public BloodTestBackingFormBuilder withTestName(String testName) {
    this.testName = testName;
    return this;
  }
  
  public BloodTestBackingFormBuilder withTestNameShort(String testNameShort) {
    this.testNameShort = testNameShort;
    return this;
  }
  
  public BloodTestBackingFormBuilder withCategory(BloodTestCategory category) {
    this.category = category;
    return this;
  }
  
  public BloodTestBackingFormBuilder withBloodTestType(BloodTestType bloodTestType) {
    this.bloodTestType = bloodTestType;
    return this;
  }
  
  public BloodTestBackingFormBuilder withValidResults(LinkedHashSet<String> validResults) {
    this.validResults = validResults;
    return this;
  }

  public BloodTestBackingFormBuilder withNegativeResults(LinkedHashSet<String> negativeResults) {
    this.negativeResults = negativeResults;
    return this;
  }
  
  public BloodTestBackingFormBuilder withPositiveResults(LinkedHashSet<String> positiveResults) {
    this.positiveResults = positiveResults;
    return this;
  }
  
  public BloodTestBackingFormBuilder thatIsActive() {
    this.isActive = true;
    return this;
  }
  
  public BloodTestBackingFormBuilder thatIsNotActive() {
    this.isActive = false;
    return this;
  }
  
  public BloodTestBackingFormBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }
  
  public BloodTestBackingFormBuilder thatIsNotDeleted() {
    this.isDeleted = false;
    return this;
  }
  
  public BloodTestBackingFormBuilder thatShouldFlagComponentsContainingPlasmaForDiscard() {
    this.flagComponentsContainingPlasmaForDiscard = true;
    return this;
  }
  
  public BloodTestBackingFormBuilder thatShouldNotFlagComponentsContainingPlasmaForDiscard() {
    this.flagComponentsContainingPlasmaForDiscard = false;
    return this;
  }  
  
  public BloodTestBackingFormBuilder thatShouldFlagComponentsForDiscard() {
    this.flagComponentsForDiscard = true;
    return this;
  }  
  
  public BloodTestBackingFormBuilder thatShouldNotFlagComponentsForDiscard() {
    this.flagComponentsForDiscard = false;
    return this;
  }
  
  public BloodTestBackingFormBuilder withRankInCategory(Integer rankInCategory) {
    this.rankInCategory = rankInCategory;
    return this;
  }
  
  @Override
  public BloodTestBackingForm build() {
    BloodTestBackingForm bloodTestBackingForm = new BloodTestBackingForm();
    bloodTestBackingForm.setId(id);
    bloodTestBackingForm.setTestName(testName);
    bloodTestBackingForm.setTestNameShort(testNameShort);
    bloodTestBackingForm.setCategory(category);
    bloodTestBackingForm.setBloodTestType(bloodTestType);
    bloodTestBackingForm.setValidResults(validResults);
    bloodTestBackingForm.setNegativeResults(negativeResults);
    bloodTestBackingForm.setPositiveResults(positiveResults);
    bloodTestBackingForm.setIsActive(isActive);
    bloodTestBackingForm.setIsDeleted(isDeleted);
    bloodTestBackingForm.setFlagComponentsContainingPlasmaForDiscard(flagComponentsContainingPlasmaForDiscard);
    bloodTestBackingForm.setFlagComponentsForDiscard(flagComponentsForDiscard);
    bloodTestBackingForm.setRankInCategory(rankInCategory);
    return bloodTestBackingForm;
  }
  
  public static BloodTestBackingFormBuilder aBloodTestBackingForm() {
    return new BloodTestBackingFormBuilder();
  }

}
