
package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.donordeferral.DeferralReason;

import javax.validation.Valid;


public class DeferralReasonBackingForm {

    @Valid
    @JsonIgnore
    private DeferralReason deferralReason;

    public DeferralReasonBackingForm() {
        deferralReason = new DeferralReason();
    }

    public DeferralReason getDeferralReason() {
        return deferralReason;
    }

    public void setDeferralReason(DeferralReason deferralReason) {
        this.deferralReason = deferralReason;
    }

    public void setId(Integer id){
        deferralReason.setId(id);
    }

    public void setReason(String reason){
        deferralReason.setReason(reason);
    }

    public void setIsDeleted(Boolean isDeleted){
        deferralReason.setIsDeleted(isDeleted);
    }

}
