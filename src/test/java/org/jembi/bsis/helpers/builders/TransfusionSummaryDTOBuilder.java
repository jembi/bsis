package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.TransfusionSummaryDTO;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;

public class TransfusionSummaryDTOBuilder extends AbstractBuilder<TransfusionSummaryDTO>{

  private TransfusionOutcome transfusionOutcome;
  private TransfusionReactionType transfusionReactionType;
  private Location transfusionSite;
  private long count;
  
  public TransfusionSummaryDTOBuilder withTransfusionOutcome(TransfusionOutcome transfusionOutcome) {
    this.transfusionOutcome = transfusionOutcome;
    return this;
  }
  
  public TransfusionSummaryDTOBuilder withTransfusionReactionType(TransfusionReactionType transfusionReactionType) {
    this.transfusionReactionType = transfusionReactionType;
    return this;
  }

  public TransfusionSummaryDTOBuilder withTransfusionSite(Location transfusionSite) {
    this.transfusionSite = transfusionSite;
    return this;
  }

  public TransfusionSummaryDTOBuilder withCount(long count) {
    this.count = count;
    return this;
  }

  @Override
  public TransfusionSummaryDTO build() {
    TransfusionSummaryDTO transfusionSummaryDTO = new TransfusionSummaryDTO();
    transfusionSummaryDTO.setTransfusionOutcome(transfusionOutcome);
    transfusionSummaryDTO.setTransfusionReactionType(transfusionReactionType);
    transfusionSummaryDTO.setTransfusionSite(transfusionSite);
    transfusionSummaryDTO.setCount(count);
    return transfusionSummaryDTO;
  }
  
  public static TransfusionSummaryDTOBuilder aTransfusionSummaryDTO() {
    return new TransfusionSummaryDTOBuilder();
  }
  
}
