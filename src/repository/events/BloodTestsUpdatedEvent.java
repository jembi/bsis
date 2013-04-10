package repository.events;

import model.collectedsample.CollectedSample;
import viewmodel.BloodTestingRuleResult;

public class BloodTestsUpdatedEvent extends AbstractApplicationEvent {

  private static final long serialVersionUID = 1L;

  private CollectedSample collectedSample;
  private BloodTestingRuleResult ruleResult;

  public BloodTestsUpdatedEvent(String eventId, Object eventContext) {
    super(eventId, eventContext);
  }

  public CollectedSample getCollectedSample() {
    return collectedSample;
  }


  public void setCollectedSample(CollectedSample collectedSample) {
    this.collectedSample = collectedSample;
  }


  public BloodTestingRuleResult getBloodTestingRuleResult() {
    return ruleResult;
  }


  public void setBloodTestingRuleResult(BloodTestingRuleResult bloodTestingRuleResult) {
    this.ruleResult = bloodTestingRuleResult;
  }

}
