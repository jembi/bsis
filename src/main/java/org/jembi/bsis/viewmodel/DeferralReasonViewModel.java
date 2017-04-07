package org.jembi.bsis.viewmodel;

import java.util.UUID;
import org.jembi.bsis.model.donordeferral.DurationType;

public class DeferralReasonViewModel {

  private UUID id;
  private String reason;
  private Integer defaultDuration;
  private DurationType durationType;
  private boolean isDeleted = false;

  public DeferralReasonViewModel() {
  }

  public UUID getId() {
    return id;
  }

  public String getReason() {
    return reason;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public Integer getDefaultDuration() {
    return defaultDuration;
  }

  public DurationType getDurationType() {
    return durationType;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public void setDefaultDuration(Integer defaultDuration) {
    this.defaultDuration = defaultDuration;
  }

  public void setDurationType(DurationType durationType) {
    this.durationType = durationType;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
