package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.backingform.DeferralReasonBackingForm;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DurationType;

public class DeferralReasonBackingFormBuilder {

  private DeferralReason deferralReason = new DeferralReason();
  private int defaultDuration;
  private DurationType durationType = DurationType.TEMPORARY;
  private UUID id;

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

  public DeferralReasonBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public DeferralReasonBackingForm build() {
    DeferralReasonBackingForm backingForm = new DeferralReasonBackingForm();
    backingForm.setDeferralReason(deferralReason);
    backingForm.setDefaultDuration(defaultDuration);
    backingForm.setDurationType(durationType);
    backingForm.setId(id);
    return backingForm;
  }

  public static DeferralReasonBackingFormBuilder aDeferralReasonBackingForm() {
    return new DeferralReasonBackingFormBuilder();
  }

}
