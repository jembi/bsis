package viewmodel;

import java.util.Date;

import utils.JsonDateSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import model.donation.Donation;

public class DonationSummaryViewModel {
    
    private Donation donation;
    
    public DonationSummaryViewModel(Donation donation) {
        this.donation = donation;
    }
    
    public String getDonationIdentificationNumber() {
        return donation.getDonationIdentificationNumber();
    }
    
    @JsonSerialize(using = JsonDateSerialiser.class)
    public Date getDonationDate() {
        return donation.getDonationDate();
    }
    
    public LocationViewModel getDonorPanel() {
        return new LocationViewModel(donation.getDonorPanel());
    }
    
    public DonorViewModel getDonor() {
        return new DonorViewModel(donation.getDonor());
    }

}
