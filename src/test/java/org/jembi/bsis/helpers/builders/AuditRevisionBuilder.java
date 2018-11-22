package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.jembi.bsis.helpers.persisters.DefaultPersister;
import org.jembi.bsis.model.audit.AuditRevision;
import org.jembi.bsis.model.audit.EntityModification;

public class AuditRevisionBuilder extends AbstractBuilder<AuditRevision> {

  private long id;
  private long timestamp;
  private String username;
  private Set<EntityModification> entityModifications = new HashSet<>();

  public AuditRevisionBuilder withId(long id) {
    this.id = id;
    return this;
  }

  public AuditRevisionBuilder withRevisionDate(Date revisionDate) {
    timestamp = revisionDate.getTime();
    return this;
  }

  public AuditRevisionBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  public AuditRevisionBuilder withEntityModifications(Set<EntityModification> entityModifications) {
    this.entityModifications = entityModifications;
    return this;
  }

  @Override
  public AuditRevision build() {
    AuditRevision auditRevision = new AuditRevision();
    auditRevision.setId(id);
    auditRevision.setTimestamp(timestamp);
    auditRevision.setUsername(username);
    for (EntityModification entityModification : entityModifications) {
      entityModification.setAuditRevision(auditRevision);
    }
    auditRevision.setEntityModifications(entityModifications);
    return auditRevision;
  }

  public AuditRevision buildAndPersist(EntityManager entityManager) {
    AuditRevision auditRevision = build();
    return new DefaultPersister<AuditRevision>().deepPersist(auditRevision, entityManager);
  }

  public static AuditRevisionBuilder anAuditRevision() {
    return new AuditRevisionBuilder();
  }

}
