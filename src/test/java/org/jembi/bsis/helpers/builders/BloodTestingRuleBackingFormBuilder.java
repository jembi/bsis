package org.jembi.bsis.helpers.builders;

import java.util.LinkedHashSet;

import org.jembi.bsis.backingform.BloodTestingRuleBackingForm;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestContext;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestSubCategory;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;

public class BloodTestingRuleBackingFormBuilder extends AbstractBuilder<BloodTestingRuleBackingForm> {

  private Long id;
  private BloodTest bloodTest;
  private String pattern;
  private DonationField donationFieldChanged;
  private String newInformation;
  private String extraInformation;
  private LinkedHashSet<String> pendingTestsIds;
  private BloodTestCategory category;
  private BloodTestSubCategory subCategory;
  private BloodTestContext context;
  private Boolean markSampleAsUnsafe;
  private boolean isDeleted = false;

  public BloodTestingRuleBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }
  
  public BloodTestingRuleBackingFormBuilder withBloodTest(BloodTest bloodTest) {
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
  
  public BloodTestingRuleBackingFormBuilder withExtraInformation(String extraInformation) {
    this.extraInformation = extraInformation;
    return this;
  }
  
  public BloodTestingRuleBackingFormBuilder withPendingTestsIds(LinkedHashSet<String> pendingTestsIds) {
    this.pendingTestsIds = pendingTestsIds;
    return this;
  }

  public BloodTestingRuleBackingFormBuilder withCategory(BloodTestCategory category) {
    this.category = category;
    return this;
  }

  public BloodTestingRuleBackingFormBuilder withSubCategory(BloodTestSubCategory subCategory) {
    this.subCategory = subCategory;
    return this;
  }
  
  public BloodTestingRuleBackingFormBuilder withContext(BloodTestContext context) {
    this.context = context;
    return this;
  }
  
  public BloodTestingRuleBackingFormBuilder withMarkSampleAsUnsafe(Boolean markSampleAsUnsafe) {
    this.markSampleAsUnsafe = markSampleAsUnsafe;
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
    bloodTestingRuleBackingForm.setContext(context);
    bloodTestingRuleBackingForm.setDeleted(isDeleted);
    bloodTestingRuleBackingForm.setDonationFieldChanged(donationFieldChanged);
    bloodTestingRuleBackingForm.setExtraInformation(extraInformation);
    bloodTestingRuleBackingForm.setId(id);
    bloodTestingRuleBackingForm.setMarkSampleAsUnsafe(markSampleAsUnsafe);
    bloodTestingRuleBackingForm.setNewInformation(newInformation);
    bloodTestingRuleBackingForm.setPattern(pattern);
    bloodTestingRuleBackingForm.setPendingTestsIds(pendingTestsIds);
    bloodTestingRuleBackingForm.setSubCategory(subCategory);
    return bloodTestingRuleBackingForm;
  }
  
  public static BloodTestingRuleBackingFormBuilder aBloodTestingRuleBackingForm() {
    return new BloodTestingRuleBackingFormBuilder();
  }
}