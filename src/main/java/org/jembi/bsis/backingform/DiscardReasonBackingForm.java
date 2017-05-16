package org.jembi.bsis.backingform;

import java.util.UUID;

public class DiscardReasonBackingForm {
  
  private UUID id;
  private String reason;
  private Boolean isDeleted;

  public DiscardReasonBackingForm() {
  }

  public String getReason() {
    return reason;
  }

  public UUID getId() {
    return id;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
