package viewmodel;

import model.CustomDateFormatter;
import model.donor.DonorDeferral;
import model.donordeferral.DeferralReason;

public class DonorDeferralViewModel {

  private DonorDeferral donorDeferral;

  public DonorDeferralViewModel(DonorDeferral donorDeferral) {
    this.donorDeferral = donorDeferral;
  }

  public Long getId() {
    return donorDeferral.getId();
  }

  public String getDeferredOn() {
    return CustomDateFormatter.getDateString(donorDeferral.getDeferredOn());
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

  public String getDeferredBy() {
    if (donorDeferral.getDeferredBy() == null)
      return "";
    return donorDeferral.getDeferredBy().getUsername();
  }
}
