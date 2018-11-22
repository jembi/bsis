package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.DivisionPersister;
import org.jembi.bsis.model.location.Division;

public class DivisionBuilder extends AbstractEntityBuilder<Division> {
  
  private UUID id;
  private String name = "default.division.name";
  private int level;
  private Division parent;

  public DivisionBuilder withId(UUID id) {
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
  
  public DivisionBuilder withParent(Division parent) {
    this.parent = parent;
    return this;
  }

  @Override
  public Division build() {
    Division division = new Division();
    division.setId(id);
    division.setName(name);
    division.setLevel(level);
    division.setParent(parent);
    return division;
  }

  @Override
  public AbstractEntityPersister<Division> getPersister() {
    return new DivisionPersister();
  }

  public static DivisionBuilder aDivision() {
    return new DivisionBuilder();
  }

}
