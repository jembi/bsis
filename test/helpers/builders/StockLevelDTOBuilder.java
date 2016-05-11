package helpers.builders;

import dto.StockLevelDTO;
import model.componenttype.ComponentType;
import model.location.Location;

public class StockLevelDTOBuilder extends AbstractBuilder<StockLevelDTO> {

  private ComponentType componentType;
  private String bloodAbo;
  private String bloodRh;
  private long count;
  private Location location;

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

  public StockLevelDTOBuilder withVenue(Location location) {
    this.location = location;
    return this;
  }

  @Override
  public StockLevelDTO build() {
    StockLevelDTO stockLevelDTO = new StockLevelDTO();
    stockLevelDTO.setComponentType(componentType);
    stockLevelDTO.setBloodAbo(bloodAbo);
    stockLevelDTO.setBloodRh(bloodRh);
    stockLevelDTO.setCount(count);
    stockLevelDTO.setLocation(location);
    return stockLevelDTO;
  }

  public static StockLevelDTOBuilder aStockLevelDTO() {
    return new StockLevelDTOBuilder();
  }

}
