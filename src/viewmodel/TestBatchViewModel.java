package viewmodel;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import model.testbatch.TestBatchStatus;
import utils.DateTimeSerialiser;

public class TestBatchViewModel {

  private Long id;
  private Date createdDate;
  private Date lastUpdatedDate;
  private TestBatchStatus status;
  private String batchNumber;
  private String notes;
  private Integer numSamples;

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



}
