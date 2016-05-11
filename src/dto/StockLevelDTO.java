package dto;

import model.componenttype.ComponentType;
import model.location.Location;

public class StockLevelDTO {

  private ComponentType componentType;
  private String bloodAbo;
  private String bloodRh;
  private long count;
  private Location location;

  public StockLevelDTO() {
    // Default constructor
  }

  public StockLevelDTO(Location location, ComponentType componentType, String bloodAbo, String bloodRh, long count) {
    this.componentType = componentType;
    this.bloodAbo = bloodAbo;
    this.bloodRh = bloodRh;
    this.location = location;
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

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  @Override
  public String toString() {
    return "StockLevelsDTO [componentType=" + componentType + ", bloodAbo=" + bloodAbo + ", bloodRh=" + bloodRh
        + ", count=" + count + ", location=" + location + "]";
  }



}
