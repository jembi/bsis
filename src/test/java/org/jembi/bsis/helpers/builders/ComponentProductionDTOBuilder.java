package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.ComponentProductionDTO;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;

public class ComponentProductionDTOBuilder extends AbstractBuilder<ComponentProductionDTO>{
  private String componentTypeName;
  private String bloodAbo;
  private String bloodRh;
  private String venue;
  private long count;
  
  public ComponentProductionDTOBuilder withComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
    return this;
  }
  
  public ComponentProductionDTOBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public ComponentProductionDTOBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public ComponentProductionDTOBuilder withCount(long count) {
    this.count = count;
    return this;
  }
  
  public ComponentProductionDTOBuilder withVenue(String venue) {
    this.venue =venue;
    return this;
  }

  @Override
  public ComponentProductionDTO build() {
    ComponentProductionDTO componentProductionExportDTO = new ComponentProductionDTO();
    componentProductionExportDTO.setComponentType(componentTypeName);
    componentProductionExportDTO.setBloodAbo(bloodAbo);
    componentProductionExportDTO.setBloodRh(bloodRh);
    componentProductionExportDTO.setVenue(venue);
    componentProductionExportDTO.setCount(count);
    return componentProductionExportDTO;
  }
  
  public static ComponentProductionDTOBuilder aComponentProductionExportDTO() {
    return new ComponentProductionDTOBuilder();
  }
  
}
