package helpers.builders;

import model.donordeferral.DeferralReason;
import backingform.DeferralReasonBackingForm;

public class DeferralReasonBackingFormBuilder {
    
    private DeferralReason deferralReason;
    private int defaultDuration;

    public DeferralReasonBackingFormBuilder withDeferralReason(DeferralReason deferralReason) {
        this.deferralReason = deferralReason;
        return this;
    }
    
    public DeferralReasonBackingFormBuilder withDefaultDuration(int defaultDuration) {
        this.defaultDuration = defaultDuration;
        return this;
    }
    
    public DeferralReasonBackingForm build() {
        DeferralReasonBackingForm backingForm = new DeferralReasonBackingForm();
        backingForm.setDeferralReason(deferralReason);
        backingForm.setDefaultDuration(defaultDuration);
        return backingForm;
    }
    
    public static DeferralReasonBackingFormBuilder aDeferralReasonBackingForm() {
        return new DeferralReasonBackingFormBuilder();
    }

}
