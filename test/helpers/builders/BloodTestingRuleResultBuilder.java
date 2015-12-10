package helpers.builders;

import viewmodel.BloodTestingRuleResult;

import java.util.ArrayList;
import java.util.List;

public class BloodTestingRuleResultBuilder extends AbstractBuilder<BloodTestingRuleResult> {

  private List<String> pendingTTITestsIds;

  public static BloodTestingRuleResultBuilder aBloodTestingRuleResult() {
    return new BloodTestingRuleResultBuilder();
  }

  public BloodTestingRuleResultBuilder withPendingTTITestId(String pendingTTITestId) {
    if (pendingTTITestsIds == null) {
      pendingTTITestsIds = new ArrayList<>();
    }
    pendingTTITestsIds.add(pendingTTITestId);
    return this;
  }

  @Override
  public BloodTestingRuleResult build() {
    BloodTestingRuleResult bloodTestingRuleResult = new BloodTestingRuleResult();
    bloodTestingRuleResult.setPendingTTITestsIds(pendingTTITestsIds);
    return bloodTestingRuleResult;
  }

}
