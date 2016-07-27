package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestSubCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;

public class BloodTestingRuleBuilder extends AbstractBuilder<BloodTestingRule> {

  private Long id;
  private String bloodTestsIds;
  private String pattern;
  private String newInformation;
  private DonationField donationFieldChanged;
  private String pendingTestsIds;
  private BloodTestSubCategory subCategory;
  private BloodTestCategory category;

  public BloodTestingRuleBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public BloodTestingRuleBuilder withBloodTestsIds(String bloodTestsIds) {
    this.bloodTestsIds = bloodTestsIds;
    return this;
  }

  public BloodTestingRuleBuilder withNewInformation(String newInformation) {
    this.newInformation = newInformation;
    return this;
  }

  public BloodTestingRuleBuilder withPattern(String pattern) {
    this.pattern = pattern;
    return this;
  }

  public BloodTestingRuleBuilder withDonationFieldChange(DonationField donationFieldChanged) {
    this.donationFieldChanged = donationFieldChanged;
    return this;
  }
  
  public BloodTestingRuleBuilder withPendingTestsIds(String pendingTestsIds) {
    this.pendingTestsIds = pendingTestsIds;
    return this;
  }
  
  public BloodTestingRuleBuilder withCategory(BloodTestCategory category) {
    this.category = category;
    return this;
  }
  
  public BloodTestingRuleBuilder withSubCategory(BloodTestSubCategory subCategory) {
    this.subCategory = subCategory;
    return this;
  }

  @Override
  public BloodTestingRule build() {
    BloodTestingRule bloodTestingRule = new BloodTestingRule();
    bloodTestingRule.setId(id);
    bloodTestingRule.setBloodTestsIds(bloodTestsIds);
    bloodTestingRule.setPattern(pattern);
    bloodTestingRule.setNewInformation(newInformation);
    bloodTestingRule.setDonationFieldChanged(donationFieldChanged);
    bloodTestingRule.setPendingTestsIds(pendingTestsIds);
    bloodTestingRule.setCategory(category);
    bloodTestingRule.setSubCategory(subCategory);
    return bloodTestingRule;
  }

  public static BloodTestingRuleBuilder aBloodTestingRule() {
    return new BloodTestingRuleBuilder();
  }

}
