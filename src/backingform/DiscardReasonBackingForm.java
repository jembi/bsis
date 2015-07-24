package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.productmovement.ProductStatusChangeReason;
import model.productmovement.ProductStatusChangeReasonCategory;

import javax.validation.Valid;

public class DiscardReasonBackingForm {

    @Valid
    @JsonIgnore
    private ProductStatusChangeReason discardReason;

    public DiscardReasonBackingForm() {
        discardReason = new ProductStatusChangeReason();
        discardReason.setCategory(ProductStatusChangeReasonCategory.DISCARDED);
    }

    public ProductStatusChangeReason getDiscardReason() {
        return discardReason;
    }

    public String getReason(){
        return discardReason.getStatusChangeReason();
    }

    public Integer getId() {
        return discardReason.getId();
    }

    public void setDiscardReason(ProductStatusChangeReason discardReason) {
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
