package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jembi.bsis.model.audit.AuditRevision;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AuditRevisionRepository {

  @PersistenceContext
  private EntityManager entityManager;

  public List<AuditRevision> findAuditRevisions(Date startDate, Date endDate) {
    return entityManager.createNamedQuery(
        AuditRevisionNamedQueryConstants.NAME_FIND_AUDIT_REVISIONS,
        AuditRevision.class)
        .setParameter("startTimestamp", startDate.getTime())
        .setParameter("endTimestamp", endDate.getTime())
        .getResultList();
  }

  public List<AuditRevision> findAuditRevisionsByUser(String search, Date startDate, Date endDate) {
    return entityManager.createNamedQuery(
        AuditRevisionNamedQueryConstants.NAME_FIND_AUDIT_REVISIONS_BY_USER,
        AuditRevision.class)
        .setParameter("search", "%" + search.toLowerCase() + "%")
        .setParameter("startTimestamp", startDate.getTime())
        .setParameter("endTimestamp", endDate.getTime())
        .getResultList();
  }

}
