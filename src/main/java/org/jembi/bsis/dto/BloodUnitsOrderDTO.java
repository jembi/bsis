package org.jembi.bsis.dto;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;

public class BloodUnitsOrderDTO {

  private ComponentType componentType;
  private Location location;
  private long count;

  public BloodUnitsOrderDTO() {
    // Default constructor
  }

  public BloodUnitsOrderDTO(ComponentType componentType, Location location, long count) {
    this.componentType = componentType;
    this.location = location;
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

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

}
