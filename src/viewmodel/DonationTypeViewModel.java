package viewmodel;

import model.donationtype.DonationType;

public class DonationTypeViewModel {

    private DonationType donationType;

    public DonationTypeViewModel(DonationType donationType) {
        this.donationType = donationType;
    }

    public Integer getId(){
        return donationType.getId();
    }

    public String getDonationType(){
        return donationType.getDonationType();
    }

    public Boolean getIsDeleted(){
        return donationType.getIsDeleted();
    }
}
