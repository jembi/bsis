package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;

import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeReasonCategory;

import javax.validation.Valid;

public class DiscardReasonBackingForm {

    @Valid
    @JsonIgnore
    private ComponentStatusChangeReason discardReason;

    public DiscardReasonBackingForm() {
        discardReason = new ComponentStatusChangeReason();
        discardReason.setCategory(ComponentStatusChangeReasonCategory.DISCARDED);
    }

    public ComponentStatusChangeReason getDiscardReason() {
        return discardReason;
    }

    public String getReason(){
        return discardReason.getStatusChangeReason();
    }

    public Integer getId() {
        return discardReason.getId();
    }

    public void setDiscardReason(ComponentStatusChangeReason discardReason) {
        this.discardReason = discardReason;
    }

    public void setId(Integer id){
        discardReason.setId(id);
    }

    public void setReason(String reason){
        discardReason.setStatusChangeReason(reason);
    }

    public void setIsDeleted(Boolean isDeleted){
        discardReason.setIsDeleted(isDeleted);
    }
}
