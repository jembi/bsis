package org.jembi.bsis.helpers.builders;

import java.util.List;

import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;

public class BloodTestBackingFormBuilder extends AbstractBuilder<BloodTestBackingForm> {
  
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
  
  public BloodTestBackingFormBuilder withId(Long id) {
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
  
  public BloodTestBackingFormBuilder withValidResults(List<String> validResults) {
    this.validResults = validResults;
    return this;
  }

  public BloodTestBackingFormBuilder withNegativeResults(List<String> negativeResults) {
    this.negativeResults = negativeResults;
    return this;
  }
  
  public BloodTestBackingFormBuilder withPositiveResults(List<String> positiveResults) {
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
    return bloodTestBackingForm;
  }
  
  public static BloodTestBackingFormBuilder aBloodTestBackingForm() {
    return new BloodTestBackingFormBuilder();
  }

}
