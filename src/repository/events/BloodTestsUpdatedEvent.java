package repository.events;

import model.donation.Donation;
import viewmodel.BloodTestingRuleResult;

public class BloodTestsUpdatedEvent extends AbstractApplicationEvent {

  private static final long serialVersionUID = 1L;

  private Donation donation;
  private BloodTestingRuleResult ruleResult;

  public BloodTestsUpdatedEvent(String eventId, Object eventContext) {
    super(eventId, eventContext);
  }

  public Donation getDonation() {
    return donation;
  }


  public void setDonation(Donation donation) {
    this.donation = donation;
  }


  public BloodTestingRuleResult getBloodTestingRuleResult() {
    return ruleResult;
  }


  public void setBloodTestingRuleResult(BloodTestingRuleResult bloodTestingRuleResult) {
    this.ruleResult = bloodTestingRuleResult;
  }

}
