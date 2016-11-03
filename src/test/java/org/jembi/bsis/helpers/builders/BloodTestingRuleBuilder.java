package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.BloodTestingRulePersister;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestSubCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;

public class BloodTestingRuleBuilder extends AbstractEntityBuilder<BloodTestingRule> {
  
  private Long id;
  private BloodTest bloodTest = aBloodTest().build();
  private String pattern;
  private String newInformation;
  private DonationField donationFieldChanged;
  private String pendingTestsIds;
  private BloodTestSubCategory subCategory;
  private BloodTestCategory category;
  private boolean isDeleted = false;

  public BloodTestingRuleBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public BloodTestingRuleBuilder withBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
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

  public BloodTestingRuleBuilder withDonationFieldChanged(DonationField donationFieldChanged) {
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

  public BloodTestingRuleBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  @Override
  public BloodTestingRule build() {
    BloodTestingRule bloodTestingRule = new BloodTestingRule();
    bloodTestingRule.setId(id);
    bloodTestingRule.setBloodTest(bloodTest);
    bloodTestingRule.setPattern(pattern);
    bloodTestingRule.setNewInformation(newInformation);
    bloodTestingRule.setDonationFieldChanged(donationFieldChanged);
    bloodTestingRule.setPendingTestsIds(pendingTestsIds);
    bloodTestingRule.setCategory(category);
    bloodTestingRule.setSubCategory(subCategory);
    bloodTestingRule.setIsDeleted(isDeleted);
    return bloodTestingRule;
  }

  public static BloodTestingRuleBuilder aBloodTestingRule() {
    return new BloodTestingRuleBuilder();
  }

  @Override
  public AbstractEntityPersister<BloodTestingRule> getPersister() {
    return new BloodTestingRulePersister();
  }
}
