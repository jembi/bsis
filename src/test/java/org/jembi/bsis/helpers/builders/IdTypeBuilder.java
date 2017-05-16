package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.model.idtype.IdType;

public class IdTypeBuilder extends AbstractEntityBuilder<IdType> {

  private UUID id;
  private String type;

  public IdTypeBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public IdTypeBuilder withType(String type) {
    this.type = type;
    return this;
  }

  @Override
  public IdType build() {
    IdType idType = new IdType();
    idType.setId(id);
    idType.setIdType(type);
    return idType;
  }

  public static IdTypeBuilder anIdType() {
    return new IdTypeBuilder();
  }
  
  public static IdTypeBuilder aNationalId() {
    return new IdTypeBuilder().withType("National Id");
  }

}
