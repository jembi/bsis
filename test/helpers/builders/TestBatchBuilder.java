package helpers.builders;

import java.util.List;

import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;

public class TestBatchBuilder extends AbstractEntityBuilder<TestBatch> {
    
    private Long id;
    private TestBatchStatus status;
    private List<DonationBatch> donationBatches;

    public TestBatchBuilder withId(Long id) {
        this.id = id;
        return this;
    }
    
    public TestBatchBuilder withStatus(TestBatchStatus status) {
        this.status = status;
        return this;
    }
    
    public TestBatchBuilder withDonationBatches(List<DonationBatch> donationBatches) {
        this.donationBatches = donationBatches;
        return this;
    }

    @Override
    public TestBatch build() {
        TestBatch testBatch = new TestBatch();
        testBatch.setId(id);
        testBatch.setStatus(status);
        testBatch.setDonationBatches(donationBatches);
        return testBatch;
    }
    
    public static TestBatchBuilder aTestBatch() {
        return new TestBatchBuilder();
    }

}
