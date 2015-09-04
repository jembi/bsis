package helpers.builders;

import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;

public class DeferralReasonBuilder extends AbstractEntityBuilder<DeferralReason> {
    
    private DeferralReasonType type;
    private Boolean deleted;

    public DeferralReasonBuilder withType(DeferralReasonType type) {
        this.type = type;
        return this;
    }
    
    public DeferralReasonBuilder thatIsNotDeleted() {
        deleted = false;
        return this;
    }
    
    public DeferralReasonBuilder thatIsDeleted() {
        deleted = true;
        return this;
    }

    @Override
    public DeferralReason build() {
        DeferralReason deferralReason = new DeferralReason();
        deferralReason.setType(type);
        deferralReason.setIsDeleted(deleted);
        return deferralReason;
    }
    
    public static DeferralReasonBuilder aDeferralReason() {
        return new DeferralReasonBuilder();
    }

}
