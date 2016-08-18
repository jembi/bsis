package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.location.LocationDivision;

public class LocationDivisionBuilder extends AbstractEntityBuilder<LocationDivision> {
  
  private long id;
  private String name;
  private int level;

  public LocationDivisionBuilder withId(long id) {
    this.id = id;
    return this;
  }

  public LocationDivisionBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public LocationDivisionBuilder withLevel(int level) {
    this.level = level;
    return this;
  }

  @Override
  public LocationDivision build() {
    LocationDivision locationDivision = new LocationDivision();
    locationDivision.setId(id);
    locationDivision.setName(name);
    locationDivision.setLevel(level);
    return locationDivision;
  }
  
  public static LocationDivisionBuilder aLocationDivision() {
    return new LocationDivisionBuilder();
  }
}
