package org.jembi.bsis.dto;

public class ComponentProductionDTO {
  private String componentType;
  private String bloodAbo;
  private String bloodRh;
  private String venue;
  private long count;

  public ComponentProductionDTO() {
    super();
  }
  
  public ComponentProductionDTO(String componentTypeName, String bloodAbo, String bloodRh, String venue, long count) {
    super();
    this.componentType = componentTypeName;
    this.bloodAbo = bloodAbo;
    this.bloodRh = bloodRh;
    this.venue = venue;
    this.count = count;
  }

  public String getComponentTypeName() {
    return componentType;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public String getVenue() {
    return venue;
  }

  public long getCount() {
    return count;
  }

  public void setComponentType(String componentTypeName) {
    this.componentType = componentTypeName;
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

  public void setVenue(String venue) {
    this.venue = venue;
  }

  public void setCount(long count) {
    this.count = count;
  }
}
