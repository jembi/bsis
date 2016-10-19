package org.jembi.bsis.dto;

import org.jembi.bsis.model.location.Location;

public class ComponentProductionDTO {
  private String componentTypeName;
  private String bloodAbo;
  private String bloodRh;
  private Location processingSite;
  private long count;

  public ComponentProductionDTO() {
   // default constructor
  }
  
  public ComponentProductionDTO(String componentTypeName, String bloodAbo, String bloodRh, Location processingSite,
      long count) {
    this.componentTypeName = componentTypeName;
    this.bloodAbo = bloodAbo;
    this.bloodRh = bloodRh;
    this.processingSite = processingSite;
    this.count = count;
  }

  public String getComponentTypeName() {
    return componentTypeName;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public Location getProcessingSite() {
    return processingSite;
  }

  public long getCount() {
    return count;
  }
  
  public String getBloodRh() {
    return bloodRh;
  }

  public void setComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
  }

  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }
 
  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
  }

  public void setProcessingSite(Location processingSite) {
    this.processingSite = processingSite;
  }

  public void setCount(long count) {
    this.count = count;
  }
}
