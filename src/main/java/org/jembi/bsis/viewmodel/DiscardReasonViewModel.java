package org.jembi.bsis.viewmodel;

import java.util.UUID;

public class DiscardReasonViewModel {

  private UUID id;
  private String reason;
  private Boolean isDeleted;

  public DiscardReasonViewModel() {
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
