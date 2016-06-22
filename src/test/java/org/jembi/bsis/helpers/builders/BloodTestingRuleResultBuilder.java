package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.repository.bloodtesting.BloodTypingMatchStatus;
import org.jembi.bsis.repository.bloodtesting.BloodTypingStatus;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;

public class BloodTestingRuleResultBuilder extends AbstractBuilder<BloodTestingRuleResult> {

  private String bloodAbo;
  private String bloodRh;
  private TTIStatus ttiStatus;
  private BloodTypingStatus bloodTypingStatus;
  private BloodTypingMatchStatus bloodTypingMatchStatus;
  private List<String> pendingTTITestsIds;
  private Set<String> extraInformation;

  public BloodTestingRuleResultBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public BloodTestingRuleResultBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public BloodTestingRuleResultBuilder withTTIStatus(TTIStatus ttiStatus) {
    this.ttiStatus = ttiStatus;
    return this;
  }

  public BloodTestingRuleResultBuilder withBloodTypingStatus(BloodTypingStatus bloodTypingStatus) {
    this.bloodTypingStatus = bloodTypingStatus;
    return this;
  }

  public BloodTestingRuleResultBuilder withPendingTTITestId(String pendingTTITestId) {
    if (pendingTTITestsIds == null) {
      pendingTTITestsIds = new ArrayList<>();
    }
    pendingTTITestsIds.add(pendingTTITestId);
    return this;
  }

  public BloodTestingRuleResultBuilder withBloodTypingMatchStatus(BloodTypingMatchStatus bloodTypingMatchStatus) {
    this.bloodTypingMatchStatus = bloodTypingMatchStatus;
    return this;
  }

  public BloodTestingRuleResultBuilder withExtraInformation(Set<String> extraInformation) {
    this.extraInformation = extraInformation;
    return this;
  }

  @Override
  public BloodTestingRuleResult build() {
    BloodTestingRuleResult bloodTestingRuleResult = new BloodTestingRuleResult();
    bloodTestingRuleResult.setBloodAbo(bloodAbo);
    bloodTestingRuleResult.setBloodRh(bloodRh);
    bloodTestingRuleResult.setTTIStatus(ttiStatus);
    bloodTestingRuleResult.setBloodTypingStatus(bloodTypingStatus);
    bloodTestingRuleResult.setBloodTypingMatchStatus(bloodTypingMatchStatus);
    bloodTestingRuleResult.setPendingTTITestsIds(pendingTTITestsIds);
    bloodTestingRuleResult.setExtraInformation(extraInformation);
    return bloodTestingRuleResult;
  }

  public static BloodTestingRuleResultBuilder aBloodTestingRuleResult() {
    return new BloodTestingRuleResultBuilder();
  }

}