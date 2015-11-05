package helpers.builders;

import java.util.ArrayList;
import java.util.List;
import model.donation.Donation;
import model.donationbatch.DonationBatch;

public class DonationBatchBuilder extends AbstractEntityBuilder<DonationBatch> {

	private Integer id;
    private List<Donation> donations;
    private boolean deleted;
    private boolean closed;
    private boolean backEntry;

    public DonationBatchBuilder withId(Integer id) {
        this.id = id;
        return this;
    }
    
    public DonationBatchBuilder withDonations(List<Donation> donations) {
        this.donations = donations;
        return this;
    }

    public DonationBatchBuilder withDonation(Donation donation) {
        if (donations == null) {
            donations = new ArrayList<>();
        }
        donations.add(donation);
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
    
    public DonationBatchBuilder thatIsBackEntry() {
        backEntry = true;
        return this;
    }

    @Override
    public DonationBatch build() {
        DonationBatch donationBatch = new DonationBatch();
        donationBatch.setId(id);
        donationBatch.setDonation(donations);
        donationBatch.setIsDeleted(deleted);
        donationBatch.setIsClosed(closed);
        donationBatch.setBackEntry(backEntry);
        return donationBatch;
    }
    
    public static DonationBatchBuilder aDonationBatch() {
        return new DonationBatchBuilder();
    }

}
