package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.BloodTestingRulePersister;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;

public class BloodTestingRuleBuilder extends AbstractEntityBuilder<BloodTestingRule> {
  
  private UUID id;
  private BloodTest bloodTest = aBloodTest().build();
  private String pattern;
  private String newInformation;
  private DonationField donationFieldChanged;
  private boolean isDeleted = false;
  private List<BloodTest> pendingBloodTests = new ArrayList<>();

  public BloodTestingRuleBuilder withId(UUID id) {
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
  
  public BloodTestingRuleBuilder withPendingBloodTest(BloodTest pendingBloodTest) {
    this.pendingBloodTests.add(pendingBloodTest);
    return this;
  }

  public BloodTestingRuleBuilder withPendingTests(List<BloodTest> pendingBloodTests) {
    this.pendingBloodTests = pendingBloodTests;
    return this;
  }

  public BloodTestingRuleBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  public BloodTestingRuleBuilder thatIsNotDeleted() {
    this.isDeleted = false;
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
    bloodTestingRule.setPendingBloodTests(pendingBloodTests);
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
