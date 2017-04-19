package org.jembi.bsis.backingform;

import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DiscardReasonBackingForm {

  @Valid
  @JsonIgnore
  private ComponentStatusChangeReason discardReason;

  public DiscardReasonBackingForm() {
    discardReason = new ComponentStatusChangeReason();
    discardReason.setCategory(ComponentStatusChangeReasonCategory.DISCARDED);
  }

  public ComponentStatusChangeReason getDiscardReason() {
    return discardReason;
  }

  public String getReason() {
    return discardReason.getStatusChangeReason();
  }

  public UUID getId() {
    return discardReason.getId();
  }

  public void setDiscardReason(ComponentStatusChangeReason discardReason) {
    this.discardReason = discardReason;
  }

  public void setId(UUID id) {
    discardReason.setId(id);
  }

  public void setReason(String reason) {
    discardReason.setStatusChangeReason(reason);
  }

  public void setIsDeleted(Boolean isDeleted) {
    discardReason.setIsDeleted(isDeleted);
  }
}
