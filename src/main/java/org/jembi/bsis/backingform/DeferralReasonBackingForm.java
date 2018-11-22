
package org.jembi.bsis.backingform;

import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DeferralReasonType;
import org.jembi.bsis.model.donordeferral.DurationType;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DeferralReasonBackingForm {

  @Valid
  @JsonIgnore
  private DeferralReason deferralReason;

  public DeferralReasonBackingForm() {
    deferralReason = new DeferralReason();
  }

  public DeferralReason getDeferralReason() {
    return deferralReason;
  }

  public String getReason() {
    return deferralReason.getReason();
  }

  public UUID getId() {
    return deferralReason.getId();
  }

  public void setDeferralReason(DeferralReason deferralReason) {
    this.deferralReason = deferralReason;
  }

  public void setId(UUID id) {
    deferralReason.setId(id);
  }

  public void setReason(String reason) {
    deferralReason.setReason(reason);
  }

  public void setIsDeleted(Boolean isDeleted) {
    deferralReason.setIsDeleted(isDeleted);
  }

  public Integer getDefaultDuration() {
    return deferralReason.getDefaultDuration();
  }

  public void setDefaultDuration(Integer defaultDuration) {
    deferralReason.setDefaultDuration(defaultDuration);
  }

  public DurationType getDurationType() {
    return deferralReason.getDurationType();
  }

  public void setDurationType(DurationType durationType) {
    if (durationType == null) {
      return;
    }
    deferralReason.setDurationType(durationType);
  }

  @JsonIgnore
  public void setType(DeferralReasonType type) {
    // ignore
  }

}
