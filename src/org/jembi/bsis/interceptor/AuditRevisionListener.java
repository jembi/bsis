package org.jembi.bsis.interceptor;

import java.io.Serializable;

import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;
import org.jembi.bsis.model.audit.AuditRevision;
import org.jembi.bsis.model.audit.EntityModification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditRevisionListener implements EntityTrackingRevisionListener {

  @Override
  public void newRevision(Object revisionEntity) {

    AuditRevision auditRevision = (AuditRevision) revisionEntity;

    // Store the username of the user making the revision
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      auditRevision.setUsername(authentication.getName());
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void entityChanged(Class entityClass, String entityName, Serializable entityId, RevisionType revisionType,
                            Object revisionEntity) {

    AuditRevision auditRevision = (AuditRevision) revisionEntity;

    EntityModification entityModification = new EntityModification();
    entityModification.setAuditRevision(auditRevision);
    entityModification.setRevisionType(revisionType);
    entityModification.setEntityName(entityClass.getSimpleName());

    auditRevision.addEntityModification(entityModification);
  }

}
