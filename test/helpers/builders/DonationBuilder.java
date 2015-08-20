package helpers.builders;

import java.util.Date;

import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.DonationPersister;
import model.donation.Donation;
import model.donor.Donor;
import model.location.Location;

public class DonationBuilder extends AbstractEntityBuilder<Donation> {
    
    private Donor donor;
    private Date donationDate;
    private Location donorPanel;

    public DonationBuilder withDonor(Donor donor) {
        this.donor = donor;
        return this;
    }
    
    public DonationBuilder withDonationDate(Date donationDate) {
        this.donationDate = donationDate;
        return this;
    }
    
    public DonationBuilder withDonorPanel(Location donorPanel) {
        this.donorPanel = donorPanel;
        return this;
    }

    @Override
    public Donation build() {
        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setDonationDate(donationDate);
        donation.setDonorPanel(donorPanel);
        return donation;
    }

    @Override
    public AbstractEntityPersister<Donation> getPersister() {
        return new DonationPersister();
    }

    public static DonationBuilder aDonation() {
        return new DonationBuilder();
    }

}
