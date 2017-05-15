package org.jembi.bsis.model.user;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Role extends BaseModificationTrackerUUIDEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 50)
  private String name;

  @ManyToMany(mappedBy = "roles")
  @JsonIgnore
  private List<User> users;


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "Permission_Role", joinColumns = {@JoinColumn(name = "roles_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "permissions_id", referencedColumnName = "id")})
  private Set<Permission> permissions;

  @Lob
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public Set<Permission> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<Permission> permissions) {
    this.permissions = permissions;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}