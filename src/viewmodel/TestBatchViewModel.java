package viewmodel;

import java.util.Date;
import java.util.List;
import java.util.Map;
import model.testbatch.TestBatchStatus;
import utils.DateTimeSerialiser;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TestBatchViewModel {

    private Long id;
    private Date createdDate;
    private Date lastUpdatedDate;
    private TestBatchStatus status;
    private String batchNumber;
    private String notes;
    private List<DonationBatchViewModel> donationBatchViewModels;
    private Map<String, Boolean> permissions;
    private int readyForReleaseCount;

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

    public Integer getNumSamples() {
        if (donationBatchViewModels == null) {
            return 0;
        }

        int count = 0;
        for (DonationBatchViewModel donationBatchViewModel : donationBatchViewModels) {
            count += donationBatchViewModel.getNumDonations();
        }
        return count;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<DonationBatchViewModel> getDonationBatches() {
        return donationBatchViewModels;
    }

    public void setDonationBatches(List<DonationBatchViewModel> donationBatchViewModels) {
        this.donationBatchViewModels = donationBatchViewModels;
    }

    @JsonSerialize(using = DateTimeSerialiser.class)
    public Date getLastUpdated() {
        return lastUpdatedDate;
    }

    public void setLastUpdated(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
        this.permissions = permissions;
    }

    public void setReadyForReleaseCount(int readyForReleaseCount) {
      this.readyForReleaseCount = readyForReleaseCount;
    }

    public int getReadyForReleaseCount() {
      return readyForReleaseCount;
    }

}
