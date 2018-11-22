package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.model.user.Role;
import org.jembi.bsis.model.user.User;

public class UserBuilder extends AbstractEntityBuilder<User> {

  private String emailId;
  private String username = "default.username";
  private boolean passwordReset;
  private boolean isAdmin;
  private UUID id;
  private String firstName = "Default";
  private String lastName = "User";
  // Password = "password", rounds = 4
  private String password = "$2a$04$iA45ovNGD4hhA1puc/a8J.FN8WCMzKft1vdBAgw6o7oe7KBpVVkRS";
  private List<Role> roles;
  private Boolean isDeleted;
  private boolean isStaff = true;
  private String notes;
  private Date lastLogin;

  public UserBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public UserBuilder withLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
    return this;
  }

  public UserBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public UserBuilder withEmailId(String emailId) {
    this.emailId = emailId;
    return this;
  }

  public UserBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  public UserBuilder withPassword(String password) {
    this.password = password;
    return this;
  }

  public UserBuilder withPasswordReset() {
    passwordReset = true;
    return this;
  }

  public UserBuilder thatIsAdmin() {
    isAdmin = true;
    return this;
  }

  public UserBuilder thatIsStaff() {
    isStaff = true;
    return this;
  }

  public UserBuilder thatIsNotAdmin() {
    isAdmin = false;
    return this;
  }

  public UserBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public UserBuilder withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public UserBuilder withRoles(List<Role> roles) {
    this.roles = roles;
    return this;
  }

  public UserBuilder withRole(Role role) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(role);
    return this;
  }

  public UserBuilder thatIsDeleted() {
    isDeleted = true;
    return this;
  }

  public UserBuilder thatIsNotDeleted() {
    isDeleted = false;
    return this;
  }

  @Override
  public User build() {
    User user = new User();
    user.setId(id);
    user.setEmailId(emailId);
    user.setUsername(username);
    user.setPasswordReset(passwordReset);
    user.setIsAdmin(isAdmin);
    user.setIsStaff(isStaff);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setPassword(password);
    user.setIsDeleted(isDeleted);
    user.setRoles(roles);
    user.setNotes(notes);
    user.setLastLogin(lastLogin);
    return user;
  }

  public static UserBuilder aUser() {
    return new UserBuilder();
  }
}
