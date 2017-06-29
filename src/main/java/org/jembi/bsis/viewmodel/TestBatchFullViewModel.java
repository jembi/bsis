package org.jembi.bsis.viewmodel;

import java.util.List;
import java.util.Map;

public class TestBatchFullViewModel extends TestBatchViewModel {

  private List<DonationViewModel> donations;
  private Map<String, Boolean> permissions;
  private int readyForReleaseCount;

  public List<DonationViewModel> getDonations() {
    return donations;
  }

  public void setDonations(List<DonationViewModel> donations) {
    this.donations = donations;
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
