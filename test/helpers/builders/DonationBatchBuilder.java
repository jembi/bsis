package helpers.builders;

import model.donationbatch.DonationBatch;

public class DonationBatchBuilder extends AbstractEntityBuilder<DonationBatch> {

    private boolean deleted;
    private boolean closed;
    
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
        donationBatch.setIsDeleted(deleted);
        donationBatch.setIsClosed(closed);
        return donationBatch;
    }
    
    public static DonationBatchBuilder aDonationBatch() {
        return new DonationBatchBuilder();
    }

}
