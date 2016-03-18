package helpers.builders;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.testbatch.TestBatchStatus;
import viewmodel.DonationBatchFullViewModel;
import viewmodel.TestBatchViewModel;

public class TestBatchViewModelBuilder extends AbstractBuilder<TestBatchViewModel> {

  private Long id;
  private TestBatchStatus status;
  private String batchNumber;
  private Date createdDate;
  private Date lastUpdatedDate;
  private String notes;
  private List<DonationBatchFullViewModel> donationBatches;
  private Map<String, Boolean> permissions;
  private int readyForReleaseCount;

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

  public TestBatchViewModelBuilder withDonationBatches(List<DonationBatchFullViewModel> donationBatches) {
    this.donationBatches = donationBatches;
    return this;
  }

  public TestBatchViewModelBuilder withPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
    return this;
  }

  public TestBatchViewModelBuilder withPermission(String key, Boolean value) {
    if (permissions == null) {
      permissions = new HashMap<>();
    }
    permissions.put(key, value);
    return this;
  }

  public TestBatchViewModelBuilder withReadyForReleaseCount(int readyToReleaseCount) {
    this.readyForReleaseCount = readyToReleaseCount;
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
    testBatchViewModel.setDonationBatches(donationBatches);
    testBatchViewModel.setPermissions(permissions);
    testBatchViewModel.setReadyForReleaseCount(readyForReleaseCount);
    return testBatchViewModel;
  }

  public static TestBatchViewModelBuilder aTestBatchViewModel() {
    return new TestBatchViewModelBuilder();
  }

}
