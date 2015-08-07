package backingform;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TestBatchBackingForm {

    @Valid
    @JsonIgnore
    private TestBatch testBatch;

    @SuppressWarnings("unchecked")
    private List<Integer> donationBatchIds = Collections.EMPTY_LIST;

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

    public List<Integer> getDonationBatchIds() {
        return donationBatchIds;
    }

    public void setDonationBatchIds(List<Integer> donationBatchIds) {
        this.donationBatchIds = donationBatchIds;
    }
    
    @JsonIgnore
    public Date getLastUpdated() {
      return testBatch.getLastUpdated();
    }

}
