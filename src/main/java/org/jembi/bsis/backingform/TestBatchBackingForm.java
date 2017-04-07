package org.jembi.bsis.backingform;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;
import org.jembi.bsis.viewmodel.DonationBatchViewModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TestBatchBackingForm {

  private Long id;
  private TestBatchStatus status;
  private Date createdDate;
  private Date lastUpdated;

  private LocationBackingForm location;
  private List<UUID> donationBatchIds = null;

  public TestBatchBackingForm() {
  }
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public TestBatchStatus getStatus() {
    return status;
  }

  public void setStatus(TestBatchStatus status) {
    this.status = status;
  }
  
  public Date getCreatedDate() {
    return createdDate;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public List<UUID> getDonationBatchIds() {
    return donationBatchIds;
  }

  public void setDonationBatchIds(List<UUID> donationBatchIds) {
    this.donationBatchIds = donationBatchIds;
  }
  
  @JsonIgnore
  public void setDonationBatches(List<DonationBatchViewModel> donationBatches) {
    // Ignore
  }

  @JsonIgnore
  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLocation(LocationBackingForm location) {
    this.location = location;
  }
  
  public LocationBackingForm getLocation() {
    return location;
  }

  @JsonIgnore
  public void setReadyForReleaseCount(int count) {
    // Ignore value
  }
  
  @JsonIgnore
  public void setBatchNumber(String batchNumber) {
    // Ignore value
  }
  
  @JsonIgnore
  public void setNotes(String notes) {
    // Ignore
  }
  
  @JsonIgnore
  public void setNumSamples(int numSamples) {
    //Ignore
  }
  
  @JsonIgnore
  public void setNumReleasedSamples(int numReleasedSamples) {
    //Ignore
  }
  
  @JsonIgnore
  public void setPermissions(Map<String, Boolean> permissions) {
    // Ignore
  }

}
