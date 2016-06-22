package org.jembi.bsis.viewmodel;

import java.util.List;
import java.util.Map;

public class TestBatchFullViewModel extends TestBatchViewModel {

  private List<DonationBatchViewModel> donationBatchViewModels;
  private Map<String, Boolean> permissions;
  private int readyForReleaseCount;

  public List<DonationBatchViewModel> getDonationBatches() {
    return donationBatchViewModels;
  }

  public void setDonationBatches(List<DonationBatchViewModel> donationBatchViewModels) {
    this.donationBatchViewModels = donationBatchViewModels;
  }

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }

  public int getReadyForReleaseCount() {
    return readyForReleaseCount;
  }

  public void setReadyForReleaseCount(int readyForReleaseCount) {
    this.readyForReleaseCount = readyForReleaseCount;
  }

}
