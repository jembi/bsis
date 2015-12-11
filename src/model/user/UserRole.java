package model.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
class UserRole {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, updatable = false, insertable = false)
  private Long id;

  @ManyToMany
  private List<Role> roles = new ArrayList<>();

  @ManyToMany
  private List<User> users = new ArrayList<>();

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

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

}
