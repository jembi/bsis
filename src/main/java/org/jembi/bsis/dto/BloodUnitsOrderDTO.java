package org.jembi.bsis.dto;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.order.OrderType;

public class BloodUnitsOrderDTO {

  private ComponentType componentType;
  private Location distributionSite;
  private OrderType orderType;
  private long count;

  public BloodUnitsOrderDTO() {
    // Default constructor
  }

  public BloodUnitsOrderDTO(ComponentType componentType, Location distributionSite, OrderType orderType, long count) {
    this.componentType = componentType;
    this.distributionSite = distributionSite;
    this.orderType = orderType;
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

  public OrderType getOrderType() {
    return orderType;
  }

  public void setOrderType(OrderType orderType) {
    this.orderType = orderType;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

}
