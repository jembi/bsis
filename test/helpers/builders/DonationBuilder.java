package helpers.builders;

import java.util.Date;

import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.DonationPersister;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donor.Donor;
import model.location.Location;

public class DonationBuilder extends AbstractEntityBuilder<Donation> {
    
    private Long id;
    private Donor donor;
    private Date donationDate;
    private Location donorPanel;
    private TTIStatus ttiStatus;
    private String notes;
    private Boolean deleted;
    
    public DonationBuilder withId(Long id) {
        this.id = id;
        return this;
    }

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
    
    public DonationBuilder withTTIStatus(TTIStatus ttiStatus) {
        this.ttiStatus = ttiStatus;
        return this;
    }
    
    public DonationBuilder withNotes(String notes) {
        this.notes = notes;
        return this;
    }
    
    public DonationBuilder thatIsDeleted() {
        deleted = true;
        return this;
    }

    @Override
    public Donation build() {
        Donation donation = new Donation();
        donation.setId(id);
        donation.setDonor(donor);
        donation.setDonationDate(donationDate);
        donation.setDonorPanel(donorPanel);
        donation.setTTIStatus(ttiStatus);
        donation.setNotes(notes);
        donation.setIsDeleted(deleted);
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
