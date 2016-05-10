package helpers.builders;

import static helpers.builders.LocationBuilder.aVenue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.DonationBatchPersister;
import model.componentbatch.ComponentBatch;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.location.Location;
import model.testbatch.TestBatch;

public class DonationBatchBuilder extends AbstractEntityBuilder<DonationBatch> {

  private Long id;
  private String batchNumber;
  private List<Donation> donations;
  private Location venue = aVenue().build();
  private TestBatch testBatch;
  private boolean deleted;
  private boolean closed;
  private boolean backEntry;
  private ComponentBatch componentBatch;
  private Date createdDate;

  public DonationBatchBuilder withId(Long id) {
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

  public DonationBatchBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  public DonationBatchBuilder withTestBatch(TestBatch testBatch) {
    this.testBatch = testBatch;
    return this;
  }

  public DonationBatchBuilder withBatchNumber(String batchNumber) {
    this.batchNumber = batchNumber;
    return this;
  }

  public DonationBatchBuilder thatIsBackEntry() {
    backEntry = true;
    return this;
  }

  public DonationBatchBuilder withComponentBatch(ComponentBatch componentBatch) {
    this.componentBatch = componentBatch;
    return this;
  }

  public DonationBatchBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  @Override
  public AbstractEntityPersister<DonationBatch> getPersister() {
    return new DonationBatchPersister();
  }

  @Override
  public DonationBatch build() {
    DonationBatch donationBatch = new DonationBatch();
    donationBatch.setId(id);
    donationBatch.setBatchNumber(batchNumber);
    donationBatch.setDonation(donations);
    donationBatch.setIsDeleted(deleted);
    donationBatch.setIsClosed(closed);
    donationBatch.setVenue(venue);
    donationBatch.setBackEntry(backEntry);
    donationBatch.setTestBatch(testBatch);
    donationBatch.setComponentBatch(componentBatch);
    donationBatch.setCreatedDate(createdDate);
    return donationBatch;
  }

  public static DonationBatchBuilder aDonationBatch() {
    return new DonationBatchBuilder();
  }

}
