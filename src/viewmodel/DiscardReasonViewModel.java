package viewmodel;

import model.productmovement.ProductStatusChangeReason;

public class DiscardReasonViewModel {

    private ProductStatusChangeReason discardReason;

    public DiscardReasonViewModel(ProductStatusChangeReason discardReason) {
        this.discardReason = discardReason;
    }

    public Integer getId(){
        return discardReason.getId();
    }

    public String getReason(){
        return discardReason.getStatusChangeReason();
    }

    public Boolean getIsDeleted(){
        return discardReason.getIsDeleted();
    }
}
