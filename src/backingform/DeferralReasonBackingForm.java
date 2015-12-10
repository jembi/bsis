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

  public void setDeferralReason(DeferralReason deferralReason) {
    this.deferralReason = deferralReason;
  }

  public String getReason() {
    return deferralReason.getReason();
  }

  public void setReason(String reason) {
    deferralReason.setReason(reason);
  }

  public Integer getId() {
    return deferralReason.getId();
  }

  public void setId(Integer id) {
    deferralReason.setId(id);
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
