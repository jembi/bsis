package helpers.builders;

import viewmodel.BloodTestingRuleResult;

import java.util.ArrayList;
import java.util.List;

import model.bloodtesting.TTIStatus;
import repository.bloodtesting.BloodTypingStatus;
import viewmodel.BloodTestingRuleResult;

public class BloodTestingRuleResultBuilder extends AbstractBuilder<BloodTestingRuleResult> {

    private String bloodAbo;
    private String bloodRh;
    private TTIStatus ttiStatus;
    private BloodTypingStatus bloodTypingStatus;
    private List<String> pendingTTITestsIds;

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
            pendingTTITestsIds =  new ArrayList<>();
        }
        pendingTTITestsIds.add(pendingTTITestId);
        return this;
    }

    @Override
    public BloodTestingRuleResult build() {
        BloodTestingRuleResult bloodTestingRuleResult = new BloodTestingRuleResult();
        bloodTestingRuleResult.setBloodAbo(bloodAbo);
        bloodTestingRuleResult.setBloodRh(bloodRh);
        bloodTestingRuleResult.setTTIStatus(ttiStatus);
        bloodTestingRuleResult.setBloodTypingStatus(bloodTypingStatus);
        bloodTestingRuleResult.setPendingTTITestsIds(pendingTTITestsIds);
        return bloodTestingRuleResult;
    }
    
    public static BloodTestingRuleResultBuilder aBloodTestingRuleResult() {
        return new BloodTestingRuleResultBuilder();
    }

}
