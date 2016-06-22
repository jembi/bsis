package org.jembi.bsis.model.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Audited
public class Permission extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 50)
  private String name;

  @ManyToMany(mappedBy = "permissions")
  @JsonIgnore
  private List<Role> roles;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }
}
