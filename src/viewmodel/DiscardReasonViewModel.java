package viewmodel;

import model.componentmovement.ComponentStatusChangeReason;

public class DiscardReasonViewModel {

    private ComponentStatusChangeReason discardReason;

    public DiscardReasonViewModel(ComponentStatusChangeReason discardReason) {
        this.discardReason = discardReason;
    }

    public Long getId(){
        return discardReason.getId();
    }

    public String getReason(){
        return discardReason.getStatusChangeReason();
    }

    public Boolean getIsDeleted(){
        return discardReason.getIsDeleted();
    }
}
