package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.BloodTestResultDTO;
import org.jembi.bsis.dto.DiscardedComponentDTO;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;

public class DiscardedComponentDTOBuilder extends AbstractBuilder<DiscardedComponentDTO> {
  
  private String componentType;
  private String componentStatusChangeReason;
  private long count;
  private Location venue;
  
  public DiscardedComponentDTOBuilder withComponentType(String componentType) {
    this.componentType = componentType;
    return this;
  }

  public DiscardedComponentDTOBuilder withComponentStatusChangeReason(String componentStatusChangeReason) {
    this.componentStatusChangeReason = componentStatusChangeReason;
    return this;
  }

  public DiscardedComponentDTOBuilder withCount(long count) {
    this.count = count;
    return this;
  }

  public DiscardedComponentDTOBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  @Override
  public DiscardedComponentDTO build() {
    DiscardedComponentDTO discardedComponentDTO = new DiscardedComponentDTO();
    discardedComponentDTO.setComponentType(componentType);
    discardedComponentDTO.setComponentStatusChangeReason(componentStatusChangeReason);
    discardedComponentDTO.setCount(count);
    discardedComponentDTO.setVenue(venue);
    
    return discardedComponentDTO;
  }

  public static DiscardedComponentDTOBuilder aDiscardedComponentDTO() {
    return new DiscardedComponentDTOBuilder();
  }


}
