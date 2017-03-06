package org.jembi.bsis.dto;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;

public class BloodUnitsOrderDTO {

  private ComponentType componentType;
  private Location distributionSite;
  private long count;

  public BloodUnitsOrderDTO() {
    // Default constructor
  }

  public BloodUnitsOrderDTO(ComponentType componentType, Location distributionSite, long count) {
    this.componentType = componentType;
    this.distributionSite = distributionSite;
    this.count = count;
  }

  public ComponentType getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentType componentType) {
    this.componentType = componentType;
  }

  public Location getDistributionSite() {
    return distributionSite;
  }

  public void setDistributionSite(Location distributionSite) {
    this.distributionSite = distributionSite;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

}
