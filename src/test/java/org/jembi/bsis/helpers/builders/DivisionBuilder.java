package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.location.LocationDivision;

public class DivisionBuilder extends AbstractEntityBuilder<LocationDivision> {
  
  private long id;
  private String name;
  private int level;

  public DivisionBuilder withId(long id) {
    this.id = id;
    return this;
  }

  public DivisionBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public DivisionBuilder withLevel(int level) {
    this.level = level;
    return this;
  }

  @Override
  public LocationDivision build() {
    LocationDivision division = new LocationDivision();
    division.setId(id);
    division.setName(name);
    division.setLevel(level);
    return division;
  }
  
  public static DivisionBuilder aDivision() {
    return new DivisionBuilder();
  }
}
