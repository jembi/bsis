package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.audit.AuditRevision;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AuditRevisionRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<AuditRevision> findRecentAuditRevisions() {
        return entityManager.createNamedQuery(
                AuditRevisionNamedQueryConstants.NAME_FIND_RECENT_AUDIT_REVISIONS,
                AuditRevision.class)
                .getResultList();
    }
    
    public List<AuditRevision> findAuditRevisionsByUser(String search) {
        return entityManager.createNamedQuery(
                AuditRevisionNamedQueryConstants.NAME_FIND_AUDIT_REVISIONS_BY_USER,
                AuditRevision.class)
                .setParameter("search", "%" + search.toLowerCase() + "%")
                .getResultList();
    }

}
