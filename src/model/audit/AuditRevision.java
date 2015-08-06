package model.audit;

import interceptor.AuditRevisionListener;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.ModifiedEntityNames;
import org.hibernate.envers.RevisionEntity;

@Entity
@RevisionEntity(AuditRevisionListener.class)
public class AuditRevision extends DefaultRevisionEntity {

    private static final long serialVersionUID = -8340597018519102447L;

    @Column(length = 30)
    private String username;

    @ModifiedEntityNames
    @ElementCollection
    @CollectionTable(name = "REVCHANGES", joinColumns = @JoinColumn(name = "REV"))
    @Column(name = "entityName")
    private Set<String> modifiedEntityNames;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getModifiedEntityNames() {
        return modifiedEntityNames;
    }

    public void setModifiedEntityNames(Set<String> modifiedEntityNames) {
        this.modifiedEntityNames = modifiedEntityNames;
    }

}
