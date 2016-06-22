package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.dto.StockLevelDTO;
import org.jembi.bsis.model.componenttype.ComponentType;

public class StockLevelDTOBuilder extends AbstractBuilder<StockLevelDTO> {

  private ComponentType componentType;
  private String bloodAbo;
  private String bloodRh;
  private long count;

  public StockLevelDTOBuilder withComponentType(ComponentType componentType) {
    this.componentType = componentType;
    return this;
  }

  public StockLevelDTOBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public StockLevelDTOBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public StockLevelDTOBuilder withCount(long count) {
    this.count = count;
    return this;
  }

  @Override
  public StockLevelDTO build() {
    StockLevelDTO stockLevelDTO = new StockLevelDTO();
    stockLevelDTO.setComponentType(componentType);
    stockLevelDTO.setBloodAbo(bloodAbo);
    stockLevelDTO.setBloodRh(bloodRh);
    stockLevelDTO.setCount(count);
    return stockLevelDTO;
  }

  public static StockLevelDTOBuilder aStockLevelDTO() {
    return new StockLevelDTOBuilder();
  }

}
