package org.jembi.bsis.backingform;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.utils.CustomDateFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DonationBatchBackingForm {

  @Valid
  @JsonIgnore
  private DonationBatch donationBatch;

  public DonationBatchBackingForm() {
    donationBatch = new DonationBatch();
  }

  public DonationBatch getDonationBatch() {
    return donationBatch;
  }

  public void setDonationBatch(DonationBatch donationBatch) {
    this.donationBatch = donationBatch;
  }

  public void setId(Long id) {
    donationBatch.setId(id);
  }

  public Long getId() {
    return donationBatch.getId();
  }

  public String getBatchNumber() {
    return donationBatch.getBatchNumber();
  }

  public void setBatchNumber(String batchNumber) {
    donationBatch.setBatchNumber(batchNumber);
  }


  public String getNotes() {
    return donationBatch.getNotes();
  }

  public void setNotes(String notes) {
    donationBatch.setNotes(notes);
  }

  public Boolean getIsClosed() {
    return donationBatch.getIsClosed();
  }

  public void setIsClosed(Boolean isClosed) {
    donationBatch.setIsClosed(isClosed);
  }

  public void setVenue(Long venueId) {
    Location venue = new Location();
    venue.setId(venueId);
    donationBatch.setVenue(venue);
  }

  @JsonIgnore
  public Date getLastUpdated() {
    return donationBatch.getLastUpdated();
  }

  public Date getCreatedDate() {
    return donationBatch.getCreatedDate();
  }

  public void setCreatedDate(String createdDate) {
    try {
      donationBatch.setCreatedDate(CustomDateFormatter.getDateTimeFromString(createdDate));
    } catch (ParseException ex) {
      ex.printStackTrace();
      donationBatch.setCreatedDate(null);
    }
  }

  @JsonIgnore
  public User getCreatedBy() {
    return donationBatch.getCreatedBy();
  }

  @JsonIgnore
  public User getLastUpdatedBy() {
    return donationBatch.getLastUpdatedBy();
  }

  @JsonIgnore
  public List<Donation> getDonations() {
    return donationBatch.getDonations();
  }

  @JsonIgnore
  public Integer getNumDonations() {
    return donationBatch.getDonations().size();
  }

  @JsonIgnore
  public void setPermissions(Map<String, Boolean> permissions) {
    // Ignore
  }

  public boolean isBackEntry() {
    return donationBatch.isBackEntry();
  }

  public void setBackEntry(boolean backEntry) {
    donationBatch.setBackEntry(backEntry);
  }

  @JsonIgnore
  public void setStatus(String status) {
    // Ignore
  }
}