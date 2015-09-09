package viewmodel;

import java.util.Date;

import utils.DateTimeSerialiser;

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
    
    @JsonSerialize(using = DateTimeSerialiser.class)
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
