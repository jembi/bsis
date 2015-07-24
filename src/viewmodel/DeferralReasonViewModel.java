package viewmodel;

import model.donordeferral.DeferralReason;

public class DeferralReasonViewModel {

    private DeferralReason deferralReason;

    public DeferralReasonViewModel(DeferralReason deferralReason) {
        this.deferralReason = deferralReason;
    }

    public Integer getId(){
        return deferralReason.getId();
    }

    public String getReason(){
        return deferralReason.getReason();
    }

    public Boolean getIsDeleted(){
        return deferralReason.getIsDeleted();
    }
}
