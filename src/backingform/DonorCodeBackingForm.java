package backingform;

import javax.validation.constraints.NotNull;

public class DonorCodeBackingForm {

    @NotNull
    private Long donorId;
    
    @NotNull
    private Long donorCodeId;

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public Long getDonorCodeId() {
        return donorCodeId;
    }

    public void setDonorCodeId(Long donorCodeId) {
        this.donorCodeId = donorCodeId;
    }

}
