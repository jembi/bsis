package org.jembi.bsis.dto;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;

public class ComponentProductionExportDTO {
  private ComponentType componentType;
  private String bloodAbo;
  private Location venues;
  private long count;

  public ComponentProductionExportDTO() {
    super();
  }
  
  public ComponentProductionExportDTO(ComponentType componentType, String bloodAbo, Location venues, long count) {
    super();
    this.componentType = componentType;
    this.bloodAbo = bloodAbo;
    this.venues = venues;
    this.count = count;
  }

  public ComponentType getComponentType() {
    return componentType;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public Location getVenues() {
    return venues;
  }

  public long getCount() {
    return count;
  }

  public void setComponentType(ComponentType componentType) {
    this.componentType = componentType;
  }

  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public void setVenues(Location venues) {
    this.venues = venues;
  }

  public void setCount(long count) {
    this.count = count;
  }
}
