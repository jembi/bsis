package viewmodel;

import model.donationtype.DonationType;

public class DonationTypeViewModel {

    private DonationType donationType;

    public DonationTypeViewModel(DonationType donationType) {
        this.donationType = donationType;
    }

    public Long getId(){
        return donationType.getId();
    }

    public String getType(){
        return donationType.getDonationType();
    }

    public Boolean getIsDeleted(){
        return donationType.getIsDeleted();
    }
}
