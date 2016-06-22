package org.jembi.bsis.viewmodel;

import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DurationType;

public class DeferralReasonViewModel {

  private DeferralReason deferralReason;

  public DeferralReasonViewModel(DeferralReason deferralReason) {
    this.deferralReason = deferralReason;
  }

  public Long getId() {
    return deferralReason.getId();
  }

  public String getReason() {
    return deferralReason.getReason();
  }

  public Boolean getIsDeleted() {
    return deferralReason.getIsDeleted();
  }

  public Integer getDefaultDuration() {
    return deferralReason.getDefaultDuration();
  }

  public DurationType getDurationType() {
    return deferralReason.getDurationType();
  }
}
