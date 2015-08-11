package model.audit;

import interceptor.AuditRevisionListener;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import repository.AuditRevisionNamedQueryConstants;

@NamedQueries({
    @NamedQuery(name = AuditRevisionNamedQueryConstants.NAME_FIND_RECENT_AUDIT_REVISIONS,
            query = AuditRevisionNamedQueryConstants.QUERY_FIND_RECENT_AUDIT_REVISIONS),
    @NamedQuery(name = AuditRevisionNamedQueryConstants.NAME_FIND_AUDIT_REVISIONS_BY_USER,
            query = AuditRevisionNamedQueryConstants.QUERY_FIND_AUDIT_REVISIONS_BY_USER)
})
@Entity
@RevisionEntity(AuditRevisionListener.class)
public class AuditRevision extends DefaultRevisionEntity {

    private static final long serialVersionUID = -8340597018519102447L;

    @Column(length = 30)
    private String username;
    
    @OneToMany(mappedBy = "auditRevision", cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
    private Set<EntityModification> entityModifications = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<EntityModification> getEntityModifications() {
        return entityModifications;
    }

    public void setEntityModifications(Set<EntityModification> entityModifications) {
        this.entityModifications = entityModifications;
    }
    
    public boolean addEntityModification(EntityModification entityModification) {
        return entityModifications.add(entityModification);
    }

}
