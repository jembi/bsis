package org.jembi.bsis.dto;

import org.jembi.bsis.model.location.Location;

public class ComponentProductionDTO {
  private String componentTypeName;
  private String bloodAbo;
  private String bloodRh;
  private Location venue;
  private long count;

  public ComponentProductionDTO() {
    super();
  }
  
  public ComponentProductionDTO(String componentTypeName, String bloodAbo, String bloodRh, Location venue, long count) {
    super();
    this.componentTypeName = componentTypeName;
    this.bloodAbo = bloodAbo;
    this.bloodRh = bloodRh;
    this.venue = venue;
    this.count = count;
  }

  public String getComponentTypeName() {
    return componentTypeName;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public Location getVenue() {
    return venue;
  }

  public long getCount() {
    return count;
  }

  public void setComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
  }

  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }
 
  public String getBloodRh() {
    return bloodRh;
  }

  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
  }

  public void setVenue(Location venue) {
    this.venue = venue;
  }

  public void setCount(long count) {
    this.count = count;
  }
}
