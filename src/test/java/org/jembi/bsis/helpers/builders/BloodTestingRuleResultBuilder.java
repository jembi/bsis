package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donation.Titre;
import org.jembi.bsis.viewmodel.BloodTestResultFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;

public class BloodTestingRuleResultBuilder extends AbstractBuilder<BloodTestingRuleResult> {

  private String bloodAbo;
  private String bloodRh;
  private TTIStatus ttiStatus;
  private Titre titre;
  private BloodTypingStatus bloodTypingStatus;
  private BloodTypingMatchStatus bloodTypingMatchStatus;
  private List<UUID> pendingRepeatAndConfirmatoryTtiTestsIds;
  private Map<UUID, BloodTestResultFullViewModel> recentTestResults;
  private List<UUID> pendingBloodTypingTestsIds;
  private List<UUID> pendingConfirmatoryTTITestsIds;
  private List<UUID> pendingRepeatTTITestsIds;
 
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
      UUID pendingRepeatAndConfirmatoryTtiTestsId) {
    if (pendingRepeatAndConfirmatoryTtiTestsIds == null) {
      pendingRepeatAndConfirmatoryTtiTestsIds = new ArrayList<>();
    }
    pendingRepeatAndConfirmatoryTtiTestsIds.add(pendingRepeatAndConfirmatoryTtiTestsId);
    return this;
  }

  public BloodTestingRuleResultBuilder withPendingRepeatAndConfirmatoryTtiTestsIds(
      List<UUID> pendingRepeatAndConfirmatoryTtiTestsIds) {
    this.pendingRepeatAndConfirmatoryTtiTestsIds = pendingRepeatAndConfirmatoryTtiTestsIds;
    return this;
  }

  public BloodTestingRuleResultBuilder withBloodTypingMatchStatus(BloodTypingMatchStatus bloodTypingMatchStatus) {
    this.bloodTypingMatchStatus = bloodTypingMatchStatus;
    return this;
  }
  
  public BloodTestingRuleResultBuilder withRecentResults(Map<UUID, BloodTestResultFullViewModel> recentTestResults) {
    this.recentTestResults = recentTestResults;
    return this;
  }
  
  public BloodTestingRuleResultBuilder withPendingBloodTypingTestsIds(List<UUID> pendingBloodTypingTestsIds) {
    this.pendingBloodTypingTestsIds = pendingBloodTypingTestsIds;
    return this;
  }
  
  public BloodTestingRuleResultBuilder withPendingConfirmatoryTTITestsIds(List<UUID> pendingConfirmatoryTTITestsIds) {
    this.pendingConfirmatoryTTITestsIds = pendingConfirmatoryTTITestsIds;
    return this;
  }
  
  public BloodTestingRuleResultBuilder withPendingRepeatTTITestsIds(List<UUID> pendingRepeatTTITestsIds) {
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
