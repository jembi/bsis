package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.ComponentProductionDTO;
import org.jembi.bsis.model.location.Location;

public class ComponentProductionDTOBuilder extends AbstractBuilder<ComponentProductionDTO>{
  private String componentTypeName;
  private String bloodAbo;
  private String bloodRh;
  private Location processingSite;
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
  
  public ComponentProductionDTOBuilder withProcessingSite(Location processingSite) {
    this.processingSite = processingSite;
    return this;
  }

  @Override
  public ComponentProductionDTO build() {
    ComponentProductionDTO componentProductionDTO = new ComponentProductionDTO();
    componentProductionDTO.setComponentTypeName(componentTypeName);
    componentProductionDTO.setBloodAbo(bloodAbo);
    componentProductionDTO.setBloodRh(bloodRh);
    componentProductionDTO.setProcessingSite(processingSite);
    componentProductionDTO.setCount(count);
    return componentProductionDTO;
  }
  
  public static ComponentProductionDTOBuilder aComponentProductionDTO() {
    return new ComponentProductionDTOBuilder();
  }
  
}
