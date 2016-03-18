package viewmodel;

import java.util.List;
import java.util.Map;

public class DonationBatchFullViewModel extends DonationBatchViewModel {

  private List<DonationViewModel> donations;
  private Map<String, Boolean> permissions;

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



}
