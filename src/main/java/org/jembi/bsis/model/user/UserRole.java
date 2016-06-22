package org.jembi.bsis.model.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class UserRole extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @ManyToMany
  private List<Role> roles = new ArrayList<Role>();

  @ManyToMany
  private List<User> users = new ArrayList<User>();

  /**
   * @return the roles
   */
  public List<Role> getRoles() {
    return roles;
  }

  /**
   * @param roles the roles to set
   */
  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  /**
   * @return the users
   */
  public List<User> getUsers() {
    return users;
  }

  /**
   * @param users the users to set
   */
  public void setUsers(List<User> users) {
    this.users = users;
  }
}