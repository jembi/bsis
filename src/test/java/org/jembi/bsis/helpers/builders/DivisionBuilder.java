package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.location.LocationDivision;

public class DivisionBuilder extends AbstractEntityBuilder<LocationDivision> {
  
  private long id;
  private String name = "default.division.name";
  private int level;
  private LocationDivision parentDivision;

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
  
  public DivisionBuilder withParentDivision(LocationDivision parentDivision) {
    this.parentDivision = parentDivision;
    return this;
  }

  @Override
  public LocationDivision build() {
    LocationDivision division = new LocationDivision();
    division.setId(id);
    division.setName(name);
    division.setLevel(level);
    division.setParentDivision(parentDivision);
    return division;
  }
  
  public static DivisionBuilder aDivision() {
    return new DivisionBuilder();
  }
}
