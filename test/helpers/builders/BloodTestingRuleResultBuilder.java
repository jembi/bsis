package helpers.builders;

import java.util.ArrayList;
import java.util.List;
import viewmodel.BloodTestingRuleResult;

public class BloodTestingRuleResultBuilder extends AbstractBuilder<BloodTestingRuleResult> {

    private List<String> pendingTTITestsIds;
    
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
        bloodTestingRuleResult.setPendingTTITestsIds(pendingTTITestsIds);
        return bloodTestingRuleResult;
    }
    
    public static BloodTestingRuleResultBuilder aBloodTestingRuleResult() {
        return new BloodTestingRuleResultBuilder();
    }

}
