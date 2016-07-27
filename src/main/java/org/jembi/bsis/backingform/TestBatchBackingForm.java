package org.jembi.bsis.backingform;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;
import org.jembi.bsis.viewmodel.DonationBatchViewModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TestBatchBackingForm {

  @Valid
  @JsonIgnore
  private TestBatch testBatch;

  private List<Long> donationBatchIds = null;

  public TestBatchBackingForm() {
    testBatch = new TestBatch();
  }

  public TestBatch getTestBatch() {
    return testBatch;
  }

  public void setTestBatch(TestBatch testBatch) {
    this.testBatch = testBatch;
  }
  
  public Long getId() {
    return testBatch.getId();
  }

  public void setId(Long id) {
    testBatch.setId(id);
  }
  
  public TestBatchStatus getStatus() {
    return testBatch.getStatus();
  }

  public void setStatus(String status) {
    testBatch.setStatus(TestBatchStatus.valueOf(status));
  }
  
  public Date getCreatedDate() {
    return testBatch.getCreatedDate();
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setCreatedDate(Date createdDate) {
    testBatch.setCreatedDate(createdDate);
  }

  public List<Long> getDonationBatchIds() {
    return donationBatchIds;
  }

  public void setDonationBatchIds(List<Long> donationBatchIds) {
    this.donationBatchIds = donationBatchIds;
  }
  
  @JsonIgnore
  public void setDonationBatches(List<DonationBatchViewModel> donationBatches) {
    // Ignore
  }

  @JsonIgnore
  public Date getLastUpdated() {
    return testBatch.getLastUpdated();
  }

  public void setLocation(LocationBackingForm location) {
    testBatch.setLocation(location.getLocation());
  }
  
  public LocationBackingForm getLocation() {
    if (testBatch.getLocation() == null) {
      return null;
    }
    return new LocationBackingForm(testBatch.getLocation());
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
