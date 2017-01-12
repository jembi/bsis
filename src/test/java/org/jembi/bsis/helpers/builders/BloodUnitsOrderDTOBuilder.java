package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.BloodUnitsOrderDTO;
import org.jembi.bsis.model.componenttype.ComponentType;

public class BloodUnitsOrderDTOBuilder extends AbstractBuilder<BloodUnitsOrderDTO> {
  
  private ComponentType componentType;
  private long count;
  
  public BloodUnitsOrderDTOBuilder withComponentType(ComponentType componentType) {
    this.componentType = componentType;
    return this;
  }

  public BloodUnitsOrderDTOBuilder withCount(long count) {
    this.count = count;
    return this;
  }

  @Override
  public BloodUnitsOrderDTO build() {
    BloodUnitsOrderDTO dto = new BloodUnitsOrderDTO();
    dto.setCount(count);
    dto.setComponentType(componentType);
    return dto;
  }

  public static BloodUnitsOrderDTOBuilder aBloodUnitsOrderDTO() {
    return new BloodUnitsOrderDTOBuilder();
  }


}
