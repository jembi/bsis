package backingform;

import java.util.List;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;

public class TestBatchBackingForm {

    private List<String> donationBatchNumbers;
    
    private TestBatch testBatch; 

    public TestBatchBackingForm() {
        testBatch = new TestBatch();
    }

    public TestBatch getTestBatch() {
        return testBatch;
    }

    public void setTestBatch(TestBatch testBatch) {
        this.testBatch = testBatch;
    }
    
    public List<String> getDonationBatchNumbers() {
        return donationBatchNumbers;
    }

    public void setDonationBatchNumbers(List<String> donationBatchNumbers) {
        this.donationBatchNumbers = donationBatchNumbers;
    }
    
    public void setId(Long id){
        testBatch.setId(id);
    }
    
    public void setStatus(String status){
        testBatch.setStatus(TestBatchStatus.valueOf(status));
    }

}
