package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.repository.bloodtesting.BloodTypingMatchStatus;
import org.jembi.bsis.repository.bloodtesting.BloodTypingStatus;
import org.jembi.bsis.viewmodel.BloodTestResultViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;

public class BloodTestingRuleResultBuilder extends AbstractBuilder<BloodTestingRuleResult> {

  private String bloodAbo;
  private String bloodRh;
  private TTIStatus ttiStatus;
  private BloodTypingStatus bloodTypingStatus;
  private BloodTypingMatchStatus bloodTypingMatchStatus;
  private List<String> pendingRepeatAndConfirmatoryTtiTestsIds;
  private Set<String> extraInformation;
  private Map<String, BloodTestResultViewModel> recentTestResults;
  private List<String> pendingBloodTypingTestsIds;
  private List<String> pendingConfirmatoryTTITestsIds;
  private List<String> pendingRepeatTTITestsIds;
 
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
  
  public BloodTestingRuleResultBuilder withPendingRepeatAndConfirmatoryTtiTestsIds(
      String pendingRepeatAndConfirmatoryTtiTestsId) {
    if (pendingRepeatAndConfirmatoryTtiTestsIds == null) {
      pendingRepeatAndConfirmatoryTtiTestsIds = new ArrayList<String>();
    }
    pendingRepeatAndConfirmatoryTtiTestsIds.add(pendingRepeatAndConfirmatoryTtiTestsId);
    return this;
  }

  public BloodTestingRuleResultBuilder withPendingRepeatAndConfirmatoryTtiTestsIds(
      List<String> pendingRepeatAndConfirmatoryTtiTestsIds) {
    this.pendingRepeatAndConfirmatoryTtiTestsIds = pendingRepeatAndConfirmatoryTtiTestsIds;
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
  
  public BloodTestingRuleResultBuilder withRecentResults(Map<String, BloodTestResultViewModel> recentTestResults) {
    this.recentTestResults = recentTestResults;
    return this;
  }
  
  public BloodTestingRuleResultBuilder withPendingBloodTypingTestsIds(List<String> pendingBloodTypingTestsIds) {
    this.pendingBloodTypingTestsIds = pendingBloodTypingTestsIds;
    return this;
  }
  
  public BloodTestingRuleResultBuilder withPendingConfirmatoryTTITestsIds(List<String> pendingConfirmatoryTTITestsIds) {
    this.pendingConfirmatoryTTITestsIds = pendingConfirmatoryTTITestsIds;
    return this;
  }
  
  public BloodTestingRuleResultBuilder withPendingRepeatTTITestsIds(List<String> pendingRepeatTTITestsIds) {
    this.pendingRepeatTTITestsIds = pendingRepeatTTITestsIds;
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
    bloodTestingRuleResult.setPendingRepeatAndConfirmatoryTtiTestsIds(pendingRepeatAndConfirmatoryTtiTestsIds);
    bloodTestingRuleResult.setExtraInformation(extraInformation);
    bloodTestingRuleResult.setRecentTestResults(recentTestResults);
    bloodTestingRuleResult.setPendingBloodTypingTestsIds(pendingBloodTypingTestsIds);
    bloodTestingRuleResult.setPendingConfirmatoryTTITestsIds(pendingConfirmatoryTTITestsIds);
    bloodTestingRuleResult.setPendingRepeatTTITestsIds(pendingRepeatTTITestsIds);
    return bloodTestingRuleResult;
  }

  public static BloodTestingRuleResultBuilder aBloodTestingRuleResult() {
    return new BloodTestingRuleResultBuilder();
  }

}
