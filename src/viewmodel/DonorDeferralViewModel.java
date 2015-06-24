package viewmodel;

import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;
import utils.CustomDateFormatter;
import model.user.User;

public class DonorDeferralViewModel {

  private DonorDeferral donorDeferral;

  public DonorDeferralViewModel(DonorDeferral donorDeferral) {
    this.donorDeferral = donorDeferral;
  }

  public Long getId() {
    return donorDeferral.getId();
  }

  public Long getDeferredUntil() {
    return CustomDateFormatter.getUnixTimestampLong(donorDeferral.getDeferredUntil());
  }

  public DeferralReason getDeferralReason() {
    return donorDeferral.getDeferralReason();
  }

  public String getDeferralReasonText() {
    return donorDeferral.getDeferralReasonText();
  }

  public Long getLastUpdated() {
    return CustomDateFormatter.getUnixTimestampLong(donorDeferral.getLastUpdated());
  }

  public Long getCreatedDate() {
    return CustomDateFormatter.getUnixTimestampLong(donorDeferral.getCreatedDate());
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
  
  public Long getVoidedDate() {
    return CustomDateFormatter.getUnixTimestampLong(donorDeferral.getVoidedDate());
  }
}
