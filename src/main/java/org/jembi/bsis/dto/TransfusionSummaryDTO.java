package org.jembi.bsis.dto;

import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;

public class TransfusionSummaryDTO {

  private TransfusionOutcome transfusionOutcome;
  private TransfusionReactionType transfusionReactionType;
  private Location transfusionSite;
  private long count;

  public TransfusionSummaryDTO() {
  }

  public TransfusionSummaryDTO(TransfusionOutcome transfusionOutcome, TransfusionReactionType transfusionReactionType,
      Location transfusionSite, long count) {
    this.transfusionOutcome = transfusionOutcome;
    this.transfusionReactionType = transfusionReactionType;
    this.transfusionSite = transfusionSite;
    this.count = count;
  }

  public TransfusionOutcome getTransfusionOutcome() {
    return transfusionOutcome;
  }

  public void setTransfusionOutcome(TransfusionOutcome transfusionOutcome) {
    this.transfusionOutcome = transfusionOutcome;
  }

  public TransfusionReactionType getTransfusionReactionType() {
    return transfusionReactionType;
  }

  public void setTransfusionReactionType(TransfusionReactionType transfusionReactionType) {
    this.transfusionReactionType = transfusionReactionType;
  }

  public Location getTransfusionSite() {
    return transfusionSite;
  }

  public void setTransfusionSite(Location transfusionSite) {
    this.transfusionSite = transfusionSite;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }
}
