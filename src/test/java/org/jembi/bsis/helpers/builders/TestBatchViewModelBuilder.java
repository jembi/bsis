package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.viewmodel.TestBatchViewModel;

public class TestBatchViewModelBuilder extends AbstractBuilder<TestBatchViewModel> {

  private UUID id;
  private TestBatchStatus status;
  private String batchNumber;
  private Date testBatchDate;
  private Date lastUpdatedDate;
  private String notes;
  private Integer numSamples;

  public TestBatchViewModelBuilder withId(UUID id) {
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

  public TestBatchViewModelBuilder withTestBatchDate(Date testBatchDate) {
    this.testBatchDate = testBatchDate;
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
    testBatchViewModel.setTestBatchDate(testBatchDate);
    testBatchViewModel.setLastUpdated(lastUpdatedDate);
    testBatchViewModel.setNotes(notes);
    testBatchViewModel.setNumSamples(numSamples);
    return testBatchViewModel;
  }

  public static TestBatchViewModelBuilder aTestBatchViewModel() {
    return new TestBatchViewModelBuilder();
  }

}
