package model.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import model.BaseEntity;

import org.hibernate.envers.RevisionType;

@Entity
public class EntityModification extends BaseEntity {

  private static final long serialVersionUID = 1L;

    @ManyToOne
    private AuditRevision auditRevision;
    
    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private RevisionType revisionType;
    
    @Column(length = 30, nullable = false)
    private String entityName;

    public AuditRevision getAuditRevision() {
        return auditRevision;
    }

    public void setAuditRevision(AuditRevision auditRevision) {
        this.auditRevision = auditRevision;
    }

    public RevisionType getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(RevisionType revisionType) {
        this.revisionType = revisionType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
