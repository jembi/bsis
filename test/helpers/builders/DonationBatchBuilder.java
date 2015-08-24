package helpers.builders;

import java.util.List;

import model.donation.Donation;
import model.donationbatch.DonationBatch;

public class DonationBatchBuilder extends AbstractEntityBuilder<DonationBatch> {

    private List<Donation> donations;
    private boolean deleted;
    private boolean closed;

    public DonationBatchBuilder withDonations(List<Donation> donations) {
        this.donations = donations;
        return this;
    }
    
    public DonationBatchBuilder thatIsDeleted() {
        deleted = true;
        return this;
    }
    
    public DonationBatchBuilder thatIsClosed() {
        closed = true;
        return this;
    }

    @Override
    public DonationBatch build() {
        DonationBatch donationBatch = new DonationBatch();
        donationBatch.setDonation(donations);
        donationBatch.setIsDeleted(deleted);
        donationBatch.setIsClosed(closed);
        return donationBatch;
    }
    
    public static DonationBatchBuilder aDonationBatch() {
        return new DonationBatchBuilder();
    }

}
