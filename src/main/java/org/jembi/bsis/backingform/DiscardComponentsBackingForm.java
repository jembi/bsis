package org.jembi.bsis.backingform;

import java.util.List;
import java.util.UUID;

public class DiscardComponentsBackingForm {

  private List<UUID> componentIds;

  private DiscardReasonBackingForm discardReason;

  private String discardReasonText;

  public List<UUID> getComponentIds() {
    return componentIds;
  }

  public void setComponentIds(List<UUID> componentIds) {
    this.componentIds = componentIds;
  }

  public DiscardReasonBackingForm getDiscardReason() {
    return discardReason;
  }

  public void setDiscardReason(DiscardReasonBackingForm discardReason) {
    this.discardReason = discardReason;
  }

  public String getDiscardReasonText() {
    return discardReasonText;
  }

  public void setDiscardReasonText(String discardReasonText) {
    this.discardReasonText = discardReasonText;
  }

}
