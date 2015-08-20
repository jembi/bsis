package helpers.builders;

import java.util.List;

import model.donation.Donation;
import model.donationbatch.DonationBatch;

public class DonationBatchBuilder extends AbstractEntityBuilder<DonationBatch> {

    private List<Donation> donations;

    public DonationBatchBuilder withDonations(List<Donation> donations) {
        this.donations = donations;
        return this;
    }
    
    @Override
    public DonationBatch build() {
        DonationBatch donationBatch = new DonationBatch();
        donationBatch.setDonation(donations);
        return donationBatch;
    }
    
    public static DonationBatchBuilder aDonationBatch() {
        return new DonationBatchBuilder();
    }

}
