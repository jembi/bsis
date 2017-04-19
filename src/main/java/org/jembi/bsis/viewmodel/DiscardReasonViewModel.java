package org.jembi.bsis.viewmodel;

import java.util.UUID;

import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;

public class DiscardReasonViewModel {

  private ComponentStatusChangeReason discardReason;

  public DiscardReasonViewModel(ComponentStatusChangeReason discardReason) {
    this.discardReason = discardReason;
  }

  public UUID getId() {
    return discardReason.getId();
  }

  public String getReason() {
    return discardReason.getStatusChangeReason();
  }

  public Boolean getIsDeleted() {
    return discardReason.getIsDeleted();
  }
}
