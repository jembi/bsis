package viewmodel;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import utils.DateTimeSerialiser;

public class DonationBatchViewModel {

  private Long id;
  private String batchNumber;
  private String notes;
  private Date createdDate;
  private Date lastUpdatedDate;
  private String createdByUsername;
  private String lastUpdatedByUsername;
  private Boolean closed;
  private LocationViewModel venue;
  private boolean backEntry;
  private Integer numDonations;
  private String status;

  public DonationBatchViewModel() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public void setUpdatedDate(Date lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getCreatedBy() {
    return createdByUsername;
  }

  public void setCreatedBy(String createdByUsername) {
    this.createdByUsername = createdByUsername;
  }

  public String getLastUpdatedBy() {
    return lastUpdatedByUsername;
  }

  public void setLastUpdatedBy(String lastUpdatedByUsername) {
    this.lastUpdatedByUsername = lastUpdatedByUsername;
  }

  public Boolean getIsClosed() {
    return closed;
  }

  public void setIsClosed(Boolean closed) {
    this.closed = closed;
  }

  public LocationViewModel getVenue() {
    return venue;
  }

  public void setVenue(LocationViewModel venue) {
    this.venue = venue;
  }

  public boolean isBackEntry() {
    return backEntry;
  }

  public void setBackEntry(boolean backEntry) {
    this.backEntry = backEntry;
  }

  public Integer getNumDonations() {
    return numDonations;
  }

  public void setNumDonations(Integer numDonations) {
    this.numDonations = numDonations;
  }

  public String getStatus() {
    return getIsClosed().booleanValue() == true ? "CLOSED" : "OPEN";
  }

}
