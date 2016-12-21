package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donation.Titre;
import org.jembi.bsis.viewmodel.BloodTestResultViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;

public class BloodTestingRuleResultBuilder extends AbstractBuilder<BloodTestingRuleResult> {

  private String bloodAbo;
  private String bloodRh;
  private TTIStatus ttiStatus;
  private Titre titre;
  private BloodTypingStatus bloodTypingStatus;
  private BloodTypingMatchStatus bloodTypingMatchStatus;
  private List<Long> pendingRepeatAndConfirmatoryTtiTestsIds;
  private Map<Long, BloodTestResultViewModel> recentTestResults;
  private List<Long> pendingBloodTypingTestsIds;
  private List<Long> pendingConfirmatoryTTITestsIds;
  private List<Long> pendingRepeatTTITestsIds;
 
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

  public BloodTestingRuleResultBuilder withTitre(Titre titre) {
    this.titre = titre;
    return this;
  }

  public BloodTestingRuleResultBuilder withBloodTypingStatus(BloodTypingStatus bloodTypingStatus) {
    this.bloodTypingStatus = bloodTypingStatus;
    return this;
  }
  
  public BloodTestingRuleResultBuilder withPendingRepeatAndConfirmatoryTtiTestsIds(
      Long pendingRepeatAndConfirmatoryTtiTestsId) {
    if (pendingRepeatAndConfirmatoryTtiTestsIds == null) {
      pendingRepeatAndConfirmatoryTtiTestsIds = new ArrayList<>();
    }
    pendingRepeatAndConfirmatoryTtiTestsIds.add(pendingRepeatAndConfirmatoryTtiTestsId);
    return this;
  }

  public BloodTestingRuleResultBuilder withPendingRepeatAndConfirmatoryTtiTestsIds(
      List<Long> pendingRepeatAndConfirmatoryTtiTestsIds) {
    this.pendingRepeatAndConfirmatoryTtiTestsIds = pendingRepeatAndConfirmatoryTtiTestsIds;
    return this;
  }

  public BloodTestingRuleResultBuilder withBloodTypingMatchStatus(BloodTypingMatchStatus bloodTypingMatchStatus) {
    this.bloodTypingMatchStatus = bloodTypingMatchStatus;
    return this;
  }
  
  public BloodTestingRuleResultBuilder withRecentResults(Map<Long, BloodTestResultViewModel> recentTestResults) {
    this.recentTestResults = recentTestResults;
    return this;
  }
  
  public BloodTestingRuleResultBuilder withPendingBloodTypingTestsIds(List<Long> pendingBloodTypingTestsIds) {
    this.pendingBloodTypingTestsIds = pendingBloodTypingTestsIds;
    return this;
  }
  
  public BloodTestingRuleResultBuilder withPendingConfirmatoryTTITestsIds(List<Long> pendingConfirmatoryTTITestsIds) {
    this.pendingConfirmatoryTTITestsIds = pendingConfirmatoryTTITestsIds;
    return this;
  }
  
  public BloodTestingRuleResultBuilder withPendingRepeatTTITestsIds(List<Long> pendingRepeatTTITestsIds) {
    this.pendingRepeatTTITestsIds = pendingRepeatTTITestsIds;
    return this;
  }

  @Override
  public BloodTestingRuleResult build() {
    BloodTestingRuleResult bloodTestingRuleResult = new BloodTestingRuleResult();
    bloodTestingRuleResult.setBloodAbo(bloodAbo);
    bloodTestingRuleResult.setBloodRh(bloodRh);
    bloodTestingRuleResult.setTTIStatus(ttiStatus);
    bloodTestingRuleResult.setTitre(titre);
    bloodTestingRuleResult.setBloodTypingStatus(bloodTypingStatus);
    bloodTestingRuleResult.setBloodTypingMatchStatus(bloodTypingMatchStatus);
    bloodTestingRuleResult.setPendingRepeatAndConfirmatoryTtiTestsIds(pendingRepeatAndConfirmatoryTtiTestsIds);
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
