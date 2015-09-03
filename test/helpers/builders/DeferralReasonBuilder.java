package helpers.builders;

import model.donordeferral.DeferralReason;

public class DeferralReasonBuilder extends AbstractEntityBuilder<DeferralReason> {

    @Override
    public DeferralReason build() {
        DeferralReason deferralReason = new DeferralReason();
        return deferralReason;
    }
    
    public static DeferralReasonBuilder aDeferralReason() {
        return new DeferralReasonBuilder();
    }

}
