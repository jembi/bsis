package org.jembi.bsis.model.audit;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import org.jembi.bsis.interceptor.AuditRevisionListener;
import org.jembi.bsis.repository.AuditRevisionNamedQueryConstants;

@NamedQueries({
    @NamedQuery(name = AuditRevisionNamedQueryConstants.NAME_FIND_AUDIT_REVISIONS,
        query = AuditRevisionNamedQueryConstants.QUERY_FIND_AUDIT_REVISIONS),
    @NamedQuery(name = AuditRevisionNamedQueryConstants.NAME_FIND_AUDIT_REVISIONS_BY_USER,
        query = AuditRevisionNamedQueryConstants.QUERY_FIND_AUDIT_REVISIONS_BY_USER)
})
@Entity
@RevisionEntity(AuditRevisionListener.class)
public class AuditRevision implements Serializable {

  private static final long serialVersionUID = -8340597018519102447L;

  @Id
  @GeneratedValue
  @RevisionNumber
  private long id;

  @RevisionTimestamp
  private long timestamp;

  @Column(length = 30)
  private String username;

  @OneToMany(mappedBy = "auditRevision", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
  private Set<EntityModification> entityModifications = new HashSet<>();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Transient
  public Date getRevisionDate() {
    return new Date(timestamp);
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AuditRevision other = (AuditRevision) obj;
    if (id != other.id)
      return false;
    if (timestamp != other.timestamp)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "AuditRevision [id=" + id + ", timestamp=" + timestamp + ", username=" + username + ", entityModifications="
        + entityModifications + "]";
  }

}
