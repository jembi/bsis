package org.jembi.bsis.dto;

import org.jembi.bsis.model.componenttype.ComponentType;

public class BloodUnitsOrderDTO {

  private ComponentType componentType;
  private long count;

  public BloodUnitsOrderDTO() {
    // Default constructor
  }

  public BloodUnitsOrderDTO(ComponentType componentType, long count) {
    this.componentType = componentType;
    this.count = count;
  }

  public ComponentType getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentType componentType) {
    this.componentType = componentType;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

}
