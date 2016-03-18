package helpers.builders;

import java.util.Date;

import model.testbatch.TestBatchStatus;
import viewmodel.TestBatchViewModel;

public class TestBatchViewModelBuilder extends AbstractBuilder<TestBatchViewModel> {

  private Long id;
  private TestBatchStatus status;
  private String batchNumber;
  private Date createdDate;
  private Date lastUpdatedDate;
  private String notes;
  private Integer numSamples;

  public TestBatchViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public TestBatchViewModelBuilder withStatus(TestBatchStatus status) {
    this.status = status;
    return this;
  }

  public TestBatchViewModelBuilder withBatchNumber(String batchNumber) {
    this.batchNumber = batchNumber;
    return this;
  }

  public TestBatchViewModelBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  public TestBatchViewModelBuilder withLastUpdatedDate(Date lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
    return this;
  }

  public TestBatchViewModelBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public TestBatchViewModelBuilder withNumSamples(Integer numSamples) {
    this.numSamples = numSamples;
    return this;
  }

  @Override
  public TestBatchViewModel build() {
    TestBatchViewModel testBatchViewModel = new TestBatchViewModel();
    testBatchViewModel.setId(id);
    testBatchViewModel.setStatus(status);
    testBatchViewModel.setBatchNumber(batchNumber);
    testBatchViewModel.setCreatedDate(createdDate);
    testBatchViewModel.setLastUpdated(lastUpdatedDate);
    testBatchViewModel.setNotes(notes);
    testBatchViewModel.setNumSamples(numSamples);
    return testBatchViewModel;
  }

  public static TestBatchViewModelBuilder aTestBatchViewModel() {
    return new TestBatchViewModelBuilder();
  }

}
