package org.jembi.bsis.backingform;

import java.util.List;

public class DiscardComponentsBackingForm {

  private List<Long> componentIds;

  private DiscardReasonBackingForm discardReason;

  private String discardReasonText;

  public List<Long> getComponentIds() {
    return componentIds;
  }

  public void setComponentIds(List<Long> componentIds) {
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
