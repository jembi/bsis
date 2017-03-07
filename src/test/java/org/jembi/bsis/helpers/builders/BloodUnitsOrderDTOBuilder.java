package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.BloodUnitsOrderDTO;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.order.OrderType;

public class BloodUnitsOrderDTOBuilder extends AbstractBuilder<BloodUnitsOrderDTO> {
  
  private ComponentType componentType;
  private Location distributionSite;
  private long count;
  private OrderType orderType;

  public BloodUnitsOrderDTOBuilder withComponentType(ComponentType componentType) {
    this.componentType = componentType;
    return this;
  }

  public BloodUnitsOrderDTOBuilder withDistributionSite(Location distributionSite) {
    this.distributionSite = distributionSite;
    return this;
  }

  public BloodUnitsOrderDTOBuilder withCount(long count) {
    this.count = count;
    return this;
  }

  public BloodUnitsOrderDTOBuilder withOrderType(OrderType orderType) {
    this.orderType = orderType;
    return this;
  }

  @Override
  public BloodUnitsOrderDTO build() {
    BloodUnitsOrderDTO dto = new BloodUnitsOrderDTO();
    dto.setCount(count);
    dto.setComponentType(componentType);
    dto.setDistributionSite(distributionSite);
    dto.setOrderType(orderType);
    return dto;
  }

  public static BloodUnitsOrderDTOBuilder aBloodUnitsOrderDTO() {
    return new BloodUnitsOrderDTOBuilder();
  }


}
