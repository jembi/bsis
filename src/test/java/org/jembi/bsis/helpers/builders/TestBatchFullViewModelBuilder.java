package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.jembi.bsis.viewmodel.TestBatchFullViewModel;

public class TestBatchFullViewModelBuilder extends AbstractBuilder<TestBatchFullViewModel> {

  private UUID id;
  private TestBatchStatus status;
  private String batchNumber;
  private Date testBatchDate;
  private Date lastUpdatedDate;
  private String notes;
  private List<DonationViewModel> donations;
  private Map<String, Boolean> permissions;
  private int readyForReleaseCount;
  private Integer numSamples;

  public TestBatchFullViewModelBuilder withId(UUID id) {
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

  public TestBatchFullViewModelBuilder withTestBatchDate(Date testBatchDate) {
    this.testBatchDate = testBatchDate;
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

  public TestBatchFullViewModelBuilder withDonations(List<DonationViewModel> donations) {
    this.donations = donations;
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
    testBatchViewModel.setTestBatchDate(testBatchDate);
    testBatchViewModel.setLastUpdated(lastUpdatedDate);
    testBatchViewModel.setNotes(notes);
    testBatchViewModel.setNumSamples(numSamples);
    testBatchViewModel.setDonations(donations);
    testBatchViewModel.setPermissions(permissions);
    testBatchViewModel.setReadyForReleaseCount(readyForReleaseCount);
    return testBatchViewModel;
  }

  public static TestBatchFullViewModelBuilder aTestBatchFullViewModel() {
    return new TestBatchFullViewModelBuilder();
  }

}
