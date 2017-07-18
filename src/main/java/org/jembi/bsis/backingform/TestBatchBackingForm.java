package org.jembi.bsis.backingform;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.viewmodel.DonationFullViewModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TestBatchBackingForm {

  private UUID id;
  private TestBatchStatus status;
  private Date testBatchDate;
  private LocationBackingForm location;

  public TestBatchBackingForm() {
  }
  
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }
  
  public TestBatchStatus getStatus() {
    return status;
  }

  public void setStatus(TestBatchStatus status) {
    this.status = status;
  }

  public void setLocation(LocationBackingForm location) {
    this.location = location;
  }
  
  public LocationBackingForm getLocation() {
    return location;
  }

  public Date getTestBatchDate() {
    return testBatchDate;
  }

  public void setTestBatchDate(Date testBatchDate) {
    this.testBatchDate = testBatchDate;
  }

  @JsonIgnore
  public void setDonations(List<DonationFullViewModel> donations) {
    // Ignore
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

  @JsonIgnore
  public void setLastUpdated(Date lastUpdated) {
    // Ignore
  }
}
