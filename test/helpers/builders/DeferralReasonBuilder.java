package helpers.builders;

import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;
import model.donordeferral.DurationType;

public class DeferralReasonBuilder extends AbstractEntityBuilder<DeferralReason> {
    
    private DeferralReasonType type;
    private Boolean deleted;
    private DurationType durationType = DurationType.TEMPORARY;
    private int defaultDuration;

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
    
    public DeferralReasonBuilder withDurationType(DurationType durationType) {
        this.durationType = durationType;
        return this;
    }
    
    public DeferralReasonBuilder withDefaultDuration(int defaultDuration) {
        this.defaultDuration = defaultDuration;
        return this;
    }

    @Override
    public DeferralReason build() {
        DeferralReason deferralReason = new DeferralReason();
        deferralReason.setType(type);
        deferralReason.setIsDeleted(deleted);
        deferralReason.setDurationType(durationType);
        deferralReason.setDefaultDuration(defaultDuration);
        return deferralReason;
    }
    
    public static DeferralReasonBuilder aDeferralReason() {
        return new DeferralReasonBuilder();
    }

}
