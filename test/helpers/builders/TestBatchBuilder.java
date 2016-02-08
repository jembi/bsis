package helpers.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;

public class TestBatchBuilder extends AbstractEntityBuilder<TestBatch> {

  private Long id;
  private TestBatchStatus status;
  private List<DonationBatch> donationBatches;
  private String batchNumber;
  private Date createdDate;
  private Date lastUpdatedDate;
  private String notes;

  public TestBatchBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public TestBatchBuilder withStatus(TestBatchStatus status) {
    this.status = status;
    return this;
  }

  public TestBatchBuilder withBatchNumber(String batchNumber) {
    this.batchNumber = batchNumber;
    return this;
  }

  public TestBatchBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  public TestBatchBuilder withLastUpdatedDate(Date lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
    return this;
  }

  public TestBatchBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public TestBatchBuilder withDonationBatches(List<DonationBatch> donationBatches) {
    this.donationBatches = donationBatches;
    return this;
  }

  public TestBatchBuilder withDonationBatch(DonationBatch donationBatch) {
    if (donationBatches == null) {
      donationBatches = new ArrayList<>();
    }
    donationBatches.add(donationBatch);
    return this;
  }

  @Override
  public TestBatch build() {
    TestBatch testBatch = new TestBatch();
    testBatch.setId(id);
    testBatch.setStatus(status);
    testBatch.setBatchNumber(batchNumber);
    testBatch.setCreatedDate(createdDate);
    testBatch.setLastUpdated(lastUpdatedDate);
    testBatch.setNotes(notes);
    testBatch.setDonationBatches(donationBatches);
    return testBatch;
  }

  public static TestBatchBuilder aTestBatch() {
    return new TestBatchBuilder();
  }

}
