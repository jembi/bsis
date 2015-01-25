package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import javax.validation.Valid;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;

public class TestBatchBackingForm {

    @Valid
    @JsonIgnore
    private TestBatch testBatch;

    private List<Integer> collectionBatchIds = Collections.EMPTY_LIST;

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

    public List<Integer> getCollectionBatchIds() {
        return collectionBatchIds;
    }

    public void setCollectionBatchIds(List<Integer> collectionBatchIds) {
        this.collectionBatchIds = collectionBatchIds;
    }
    
    @JsonIgnore
    public Date getLastUpdated() {
      return testBatch.getLastUpdated();
    }

}
