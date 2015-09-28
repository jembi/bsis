package viewmodel;

import java.util.Date;
import java.util.List;

import utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class DonationBatchViewModel {

    private Integer id;
    private String batchNumber;
    private String notes;
    private List<DonationViewModel> donations;
    private Date createdDate;
    private Date lastUpdatedDate;
    private String createdByUsername;
    private String lastUpdatedByUsername;
    private Boolean closed;
    private LocationViewModel venue;

    public DonationBatchViewModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public List<DonationViewModel> getDonations() {
        return donations;
    }

    public void setDonations(List<DonationViewModel> donations) {
        this.donations = donations;
    }

    public Integer getNumDonations() {
        return donations == null ? 0 : donations.size();
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
}
