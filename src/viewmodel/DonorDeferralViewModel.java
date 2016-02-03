package viewmodel;

import java.util.Map;

import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;
import utils.CustomDateFormatter;
import model.user.User;

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

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(donorDeferral.getLastUpdated());
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateString(donorDeferral.getCreatedDate());
  }

  public String getCreatedBy() {
    User user = donorDeferral.getCreatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getLastUpdatedBy() {
    User user = donorDeferral.getLastUpdatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getVoidedDate() {
    return CustomDateFormatter.getDateString(donorDeferral.getVoidedDate());
  }

  public String getDonorNumber() {
    return donorDeferral.getDeferredDonor().getDonorNumber();
  }
}
