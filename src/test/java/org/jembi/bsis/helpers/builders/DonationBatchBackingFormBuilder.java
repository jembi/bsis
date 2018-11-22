package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.backingform.DonationBatchBackingForm;

public class DonationBatchBackingFormBuilder extends AbstractBuilder<DonationBatchBackingForm> {

  private UUID id;
  private String batchNumber;
  private String notes;
  private UUID venue;
  private boolean closed;
  private boolean backEntry;
  private Date donationBatchDate;

  public DonationBatchBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public DonationBatchBackingFormBuilder thatIsClosed() {
    closed = true;
    return this;
  }

  public DonationBatchBackingFormBuilder withVenue(UUID venue) {
    this.venue = venue;
    return this;
  }

  public DonationBatchBackingFormBuilder withBatchNumber(String batchNumber) {
    this.batchNumber = batchNumber;
    return this;
  }

  public DonationBatchBackingFormBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public DonationBatchBackingFormBuilder thatIsBackEntry() {
    backEntry = true;
    return this;
  }

  public DonationBatchBackingFormBuilder withDonationBatchDate(Date donationBatchDate){
    this.donationBatchDate = donationBatchDate;
    return this;
  }

  @Override
  public DonationBatchBackingForm build() {
    DonationBatchBackingForm donationBatch = new DonationBatchBackingForm();
    donationBatch.setId(id);
    donationBatch.setBatchNumber(batchNumber);
    donationBatch.setNotes(notes);
    donationBatch.setIsClosed(closed);
    donationBatch.setVenue(venue);
    donationBatch.setBackEntry(backEntry);
    donationBatch.setDonationBatchDate(donationBatchDate);
    return donationBatch;
  }

  public static DonationBatchBackingFormBuilder aDonationBatchBackingForm() {
    return new DonationBatchBackingFormBuilder();
  }
}