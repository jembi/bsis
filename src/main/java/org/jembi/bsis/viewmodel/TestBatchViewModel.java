package org.jembi.bsis.viewmodel;

import java.util.Date;

import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TestBatchViewModel {

  private Long id;
  private Date createdDate;
  private Date lastUpdatedDate;
  private TestBatchStatus status;
  private String batchNumber;
  private String notes;
  private Integer numSamples;
  private LocationViewModel location;

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
    return lastUpdatedDate;
  }

  public void setLastUpdated(Date lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
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
