package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TestBatchViewModel {

  private UUID id;
  private Date testBatchDate;
  private Date lastUpdated;
  private TestBatchStatus status;
  private String batchNumber;
  private String notes;
  private Integer numSamples;
  private LocationViewModel location;

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getTestBatchDate() {
    return testBatchDate;
  }

  public void setTestBatchDate(Date testBatchDate) {
    this.testBatchDate = testBatchDate;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setStatus(TestBatchStatus status) {
    this.status = status;
  }

  public TestBatchStatus getStatus() {
    return status;
  }

  public String getBatchNumber() {
    return batchNumber;
  }

  public void setBatchNumber(String batchNumber) {
    this.batchNumber = batchNumber;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdatedDate) {
    this.lastUpdated = lastUpdatedDate;
  }

  public Integer getNumSamples() {
    return numSamples;
  }

  public void setNumSamples(Integer numSamples) {
    this.numSamples = numSamples;
  }

  public LocationViewModel getLocation() {
    return location;
  }

  public void setLocation(LocationViewModel location) {
    this.location = location;
  }

}
