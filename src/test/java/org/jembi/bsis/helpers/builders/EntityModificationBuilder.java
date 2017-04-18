package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.hibernate.envers.RevisionType;
import org.jembi.bsis.model.audit.EntityModification;

public class EntityModificationBuilder {

  private UUID id;
  private RevisionType revisionType;
  private String entityName;

  public EntityModificationBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public EntityModificationBuilder withRevisionType(RevisionType revisionType) {
    this.revisionType = revisionType;
    return this;
  }

  public EntityModificationBuilder withEntityName(String entityName) {
    this.entityName = entityName;
    return this;
  }

  public EntityModification build() {
    EntityModification entityModification = new EntityModification();
    entityModification.setId(id);
    entityModification.setRevisionType(revisionType);
    entityModification.setEntityName(entityName);
    return entityModification;
  }

  public static EntityModificationBuilder anEntityModification() {
    return new EntityModificationBuilder();
  }

}
