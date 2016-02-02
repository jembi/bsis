
package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;

import model.donordeferral.DeferralReason;
import model.donordeferral.DurationType;

import javax.validation.Valid;

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

  public Long getId() {
    return deferralReason.getId();
  }

  public void setDeferralReason(DeferralReason deferralReason) {
    this.deferralReason = deferralReason;
  }

  public void setId(Long id) {
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

}
