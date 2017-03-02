package org.jembi.bsis.dto;

import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;

public class TransfusionSummaryDTO {

  private TransfusionOutcome transfusionOutcome;
  private TransfusionReactionType transfusionreactionType;
  private long count;
  private Location transfusionSite;

  public TransfusionSummaryDTO () {
  }

  public TransfusionSummaryDTO (TransfusionOutcome transfusionOutcome, TransfusionReactionType transfusionreactionType, long count, Location transfusionSite) {
    this.transfusionOutcome = transfusionOutcome;
    this.transfusionreactionType = transfusionreactionType;
    this.count = count;
    this.transfusionSite = transfusionSite;
  }

  public TransfusionOutcome getTransfusionOutcome () {
    return transfusionOutcome;
  }

  public void setTransfusionOutcome (TransfusionOutcome transfusionOutcome) {
    this.transfusionOutcome = transfusionOutcome;
  }

  public TransfusionReactionType getTransfusionreactionType () {
    return transfusionreactionType;
  }

  public void setTransfusionreactionType (TransfusionReactionType transfusionreactionType) {
    this.transfusionreactionType = transfusionreactionType;
  }

  public long getCount () {
    return count;
  }

  public void setCount (long count) {
    this.count = count;
  }

  public Location getTransfusionSite () {
    return transfusionSite;
  }

  public void setTransfusionSite (Location transfusionSite) {
    this.transfusionSite = transfusionSite;
  }
}
