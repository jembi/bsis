package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.CollectedDonationDTO;
import org.jembi.bsis.dto.ComponentProductionExportDTO;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;

public class ComponentProductionDTOBuilder extends AbstractBuilder<ComponentProductionExportDTO> {

  private ComponentType componentType;
  private String bloodAbo;
  private long count;
  private Location venue;

  public ComponentProductionDTOBuilder withComponentType(ComponentType componentType) {
    this.componentType = componentType;
    return this;
  }

  public ComponentProductionDTOBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public ComponentProductionDTOBuilder withCount(long count) {
    this.count = count;
    return this;
  }

  public ComponentProductionDTOBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  @Override
  public ComponentProductionExportDTO build() {
    ComponentProductionExportDTO componentProductionExportDTO = new ComponentProductionExportDTO();
    componentProductionExportDTO.setComponentType(componentType);
    componentProductionExportDTO.setBloodAbo(bloodAbo);
    componentProductionExportDTO.setCount(count);
    componentProductionExportDTO.setVenues(venue);
    return componentProductionExportDTO;
  }

  public static ComponentProductionDTOBuilder aComponentTypeDTO() {
    return new ComponentProductionDTOBuilder();
  }

}
