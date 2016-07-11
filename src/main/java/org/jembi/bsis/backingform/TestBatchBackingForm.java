package org.jembi.bsis.backingform;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.utils.CustomDateFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

  public void setId(Long id) {
    testBatch.setId(id);
  }

  public void setStatus(String status) {
    testBatch.setStatus(TestBatchStatus.valueOf(status));
  }

  public void setCreatedDate(String createdDate) {
    try {
      testBatch.setCreatedDate(CustomDateFormatter.getDateFromString(createdDate));
    } catch (ParseException ex) {
      ex.printStackTrace();
      testBatch.setCreatedDate(null);
    }
  }

  public List<Long> getDonationBatchIds() {
    return donationBatchIds;
  }

  public void setDonationBatchIds(List<Long> donationBatchIds) {
    this.donationBatchIds = donationBatchIds;
  }

  @JsonIgnore
  public Date getLastUpdated() {
    return testBatch.getLastUpdated();
  }

  public void setReadyForReleaseCount(int count) {
    // Ignore value
  }

  public void setLocation(LocationBackingForm location) {
    // TODO: Set location and add validation
  }

}
