package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.DonationBatchPersister;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.TestBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;

public class DonationBatchBuilder extends AbstractEntityBuilder<DonationBatch> {

  private Long id;
  private String batchNumber;
  private String notes;
  private List<Donation> donations;
  private Location venue = aVenue().build();
  private TestBatch testBatch;
  private boolean deleted;
  private boolean closed;
  private boolean backEntry;
  private ComponentBatch componentBatch;
  private Date createdDate;
  private Date lastUpdatedDate;
  private Date donationBatchDate = new Date();

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

  public DonationBatchBuilder thatIsNotDeleted() {
    deleted = false;
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

  public DonationBatchBuilder withNotes(String notes) {
    this.notes = notes;
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

  public DonationBatchBuilder withLastUpdatedDate(Date lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
    return this;
  }

  public DonationBatchBuilder withDonationBatchDate(Date donationBatchDate){
    this.donationBatchDate = donationBatchDate;
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
    donationBatch.setNotes(notes);
    donationBatch.setDonation(donations);
    donationBatch.setIsDeleted(deleted);
    donationBatch.setIsClosed(closed);
    donationBatch.setVenue(venue);
    donationBatch.setBackEntry(backEntry);
    donationBatch.setTestBatch(testBatch);
    donationBatch.setComponentBatch(componentBatch);
    donationBatch.setCreatedDate(createdDate);
    donationBatch.setLastUpdated(lastUpdatedDate);
    donationBatch.setDonationBatchDate(donationBatchDate);
    return donationBatch;
  }

  public static DonationBatchBuilder aDonationBatch() {
    return new DonationBatchBuilder();
  }

}
