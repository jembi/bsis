package helpers.builders;

import backingform.DeferralReasonBackingForm;
import model.donordeferral.DeferralReason;
import model.donordeferral.DurationType;

public class DeferralReasonBackingFormBuilder {

  private DeferralReason deferralReason;
  private int defaultDuration;
  private DurationType durationType;

  public static DeferralReasonBackingFormBuilder aDeferralReasonBackingForm() {
    return new DeferralReasonBackingFormBuilder();
  }

  public DeferralReasonBackingFormBuilder withDeferralReason(DeferralReason deferralReason) {
    this.deferralReason = deferralReason;
    return this;
  }

  public DeferralReasonBackingFormBuilder withDefaultDuration(int defaultDuration) {
    this.defaultDuration = defaultDuration;
    return this;
  }

  public DeferralReasonBackingFormBuilder withDurationType(DurationType durationType) {
    this.durationType = durationType;
    return this;
  }

  public DeferralReasonBackingForm build() {
    DeferralReasonBackingForm backingForm = new DeferralReasonBackingForm();
    backingForm.setDeferralReason(deferralReason);
    backingForm.setDefaultDuration(defaultDuration);
    backingForm.setDurationType(durationType);
    return backingForm;
  }

}
