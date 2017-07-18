package org.jembi.bsis.viewmodel;

import java.util.List;
import java.util.Map;

public class DonationBatchFullViewModel extends DonationBatchViewModel {

  private List<DonationFullViewModel> donations;
  private Map<String, Boolean> permissions;

  public List<DonationFullViewModel> getDonations() {
    return donations;
  }

  public void setDonations(List<DonationFullViewModel> donations) {
    this.donations = donations;
  }

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }



}
