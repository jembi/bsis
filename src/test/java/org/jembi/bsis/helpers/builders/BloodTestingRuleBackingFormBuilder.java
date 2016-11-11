package org.jembi.bsis.helpers.builders;

import java.util.Set;

import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.backingform.BloodTestingRuleBackingForm;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;

public class BloodTestingRuleBackingFormBuilder extends AbstractBuilder<BloodTestingRuleBackingForm> {

  private Long id;
  private BloodTestBackingForm bloodTest;
  private String pattern;
  private DonationField donationFieldChanged;
  private String newInformation;
  private Set<String> pendingTestsIds;
  private BloodTestCategory category;
  private boolean isDeleted = false;

  public BloodTestingRuleBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }
  
  public BloodTestingRuleBackingFormBuilder withBloodTest(BloodTestBackingForm bloodTest) {
    this.bloodTest = bloodTest;
    return this;
  }

  public BloodTestingRuleBackingFormBuilder withPattern(String pattern) {
    this.pattern = pattern;
    return this;
  }
  
  public BloodTestingRuleBackingFormBuilder withDonationFieldChanged(DonationField donationFieldChanged) {
    this.donationFieldChanged = donationFieldChanged;
    return this;
  }
  
  public BloodTestingRuleBackingFormBuilder withNewInformation(String newInformation) {
    this.newInformation = newInformation;
    return this;
  }
  
  public BloodTestingRuleBackingFormBuilder withPendingTestsIds(Set<String> pendingTestsIds) {
    this.pendingTestsIds = pendingTestsIds;
    return this;
  }

  public BloodTestingRuleBackingFormBuilder withCategory(BloodTestCategory category) {
    this.category = category;
    return this;
  }
  
  public BloodTestingRuleBackingFormBuilder withDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }

  @Override
  public BloodTestingRuleBackingForm build() {
    BloodTestingRuleBackingForm bloodTestingRuleBackingForm = new BloodTestingRuleBackingForm();
    bloodTestingRuleBackingForm.setBloodTest(bloodTest);
    bloodTestingRuleBackingForm.setCategory(category);
    bloodTestingRuleBackingForm.setIsDeleted(isDeleted);
    bloodTestingRuleBackingForm.setDonationFieldChanged(donationFieldChanged);
    bloodTestingRuleBackingForm.setId(id);
    bloodTestingRuleBackingForm.setNewInformation(newInformation);
    bloodTestingRuleBackingForm.setPattern(pattern);
    bloodTestingRuleBackingForm.setPendingTestsIds(pendingTestsIds);
    return bloodTestingRuleBackingForm;
  }
  
  public static BloodTestingRuleBackingFormBuilder aBloodTestingRuleBackingForm() {
    return new BloodTestingRuleBackingFormBuilder();
  }
}