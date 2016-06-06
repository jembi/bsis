package viewmodel;

import java.util.Date;
import java.util.Map;

import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;
import model.user.User;
import utils.CustomDateFormatter;

public class DonorDeferralViewModel {

  private DonorDeferral donorDeferral;
  private Map<String, Boolean> permissions;

  public DonorDeferralViewModel(DonorDeferral donorDeferral) {
    this.donorDeferral = donorDeferral;
  }

  public Long getId() {
    return donorDeferral.getId();
  }

  public String getDeferredUntil() {
    return CustomDateFormatter.getDateString(donorDeferral.getDeferredUntil());
  }

  public DeferralReason getDeferralReason() {
    return donorDeferral.getDeferralReason();
  }

  public String getDeferralReasonText() {
    return donorDeferral.getDeferralReasonText();
  }

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }

  public String getCreatedBy() {
    User user = donorDeferral.getCreatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getDonorNumber() {
    return donorDeferral.getDeferredDonor().getDonorNumber();
  }

  public LocationViewModel getVenue() {
    return new LocationViewModel(donorDeferral.getVenue());
  }
  
  // FIXME: The name of this method is misleading, but it is made to match the backing form.
  public Long getDeferredDonor() {
    return donorDeferral.getDeferredDonor().getId();
  }

  public Date getDeferralDate() {
    return donorDeferral.getDeferralDate();
  }
}
