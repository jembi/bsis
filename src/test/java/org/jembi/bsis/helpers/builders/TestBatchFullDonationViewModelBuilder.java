package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.jembi.bsis.viewmodel.TestBatchFullDonationViewModel;

public class TestBatchFullDonationViewModelBuilder extends AbstractBuilder<TestBatchFullDonationViewModel> {
  private UUID id;
  private Date testBatchDate;

  private List<DonationFullViewModel> donations;

  public TestBatchFullDonationViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public TestBatchFullDonationViewModelBuilder withTestBatchDate(Date testBatchDate) {
    this.testBatchDate = testBatchDate;
    return this;
  }

  public TestBatchFullDonationViewModelBuilder withDonations(List<DonationFullViewModel> donations) {
    this.donations = donations;
    return this;
  }

  @Override
  public TestBatchFullDonationViewModel build() {
    TestBatchFullDonationViewModel testBatchFullDonationViewModel = new TestBatchFullDonationViewModel();
    testBatchFullDonationViewModel.setId(id);
    testBatchFullDonationViewModel.setTestBatchDate(testBatchDate);
    testBatchFullDonationViewModel.setDonations(donations);
    return testBatchFullDonationViewModel;
  }

  public static TestBatchFullDonationViewModelBuilder aTestBatchFullDonationViewModel() {
    return new TestBatchFullDonationViewModelBuilder();
  }
}
