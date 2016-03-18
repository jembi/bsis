package helpers.builders;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.testbatch.TestBatchStatus;
import viewmodel.DonationBatchViewModel;
import viewmodel.TestBatchFullViewModel;

public class TestBatchFullViewModelBuilder extends AbstractBuilder<TestBatchFullViewModel> {

  private Long id;
  private TestBatchStatus status;
  private String batchNumber;
  private Date createdDate;
  private Date lastUpdatedDate;
  private String notes;
  private List<DonationBatchViewModel> donationBatches;
  private Map<String, Boolean> permissions;
  private int readyForReleaseCount;
  private Integer numSamples;

  public TestBatchFullViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public TestBatchFullViewModelBuilder withStatus(TestBatchStatus status) {
    this.status = status;
    return this;
  }

  public TestBatchFullViewModelBuilder withBatchNumber(String batchNumber) {
    this.batchNumber = batchNumber;
    return this;
  }

  public TestBatchFullViewModelBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  public TestBatchFullViewModelBuilder withLastUpdatedDate(Date lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
    return this;
  }

  public TestBatchFullViewModelBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public TestBatchFullViewModelBuilder withDonationBatches(List<DonationBatchViewModel> donationBatches) {
    this.donationBatches = donationBatches;
    return this;
  }

  public TestBatchFullViewModelBuilder withPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
    return this;
  }

  public TestBatchFullViewModelBuilder withPermission(String key, Boolean value) {
    if (permissions == null) {
      permissions = new HashMap<>();
    }
    permissions.put(key, value);
    return this;
  }

  public TestBatchFullViewModelBuilder withReadyForReleaseCount(int readyToReleaseCount) {
    this.readyForReleaseCount = readyToReleaseCount;
    return this;
  }

  public TestBatchFullViewModelBuilder withNumSamples(Integer numSamples) {
    this.numSamples = numSamples;
    return this;
  }

  @Override
  public TestBatchFullViewModel build() {
    TestBatchFullViewModel testBatchViewModel = new TestBatchFullViewModel();
    testBatchViewModel.setId(id);
    testBatchViewModel.setStatus(status);
    testBatchViewModel.setBatchNumber(batchNumber);
    testBatchViewModel.setCreatedDate(createdDate);
    testBatchViewModel.setLastUpdated(lastUpdatedDate);
    testBatchViewModel.setNotes(notes);
    testBatchViewModel.setNumSamples(numSamples);
    testBatchViewModel.setDonationBatches(donationBatches);
    testBatchViewModel.setPermissions(permissions);
    testBatchViewModel.setReadyForReleaseCount(readyForReleaseCount);
    return testBatchViewModel;
  }

  public static TestBatchFullViewModelBuilder aTestBatchFullViewModel() {
    return new TestBatchFullViewModelBuilder();
  }

}
