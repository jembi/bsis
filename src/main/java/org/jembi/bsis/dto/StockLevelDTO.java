package org.jembi.bsis.dto;

import org.jembi.bsis.model.componenttype.ComponentType;

public class StockLevelDTO {

  private ComponentType componentType;
  private String bloodAbo;
  private String bloodRh;
  private long count;

  public StockLevelDTO() {
    // Default constructor
  }

  public StockLevelDTO(ComponentType componentType, String bloodAbo, String bloodRh, long count) {
    this.componentType = componentType;
    this.bloodAbo = bloodAbo;
    this.bloodRh = bloodRh;
    this.count = count;
  }

  public ComponentType getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentType componentType) {
    this.componentType = componentType;
  }

  public String getBloodAbo() {
    return bloodAbo;
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

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

}
