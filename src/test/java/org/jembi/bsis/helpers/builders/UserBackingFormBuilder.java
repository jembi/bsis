package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.RoleBackingForm;
import org.jembi.bsis.backingform.UserBackingForm;

public class UserBackingFormBuilder extends AbstractBuilder<UserBackingForm> {

  private String emailId;
  private String username;
  private boolean isAdmin = true;
  private UUID id;
  private String firstName;
  private String lastName;
  private String password;
  private List<RoleBackingForm> roles;
  private boolean isDeleted = false;
  private boolean isStaff = true;
  private boolean isActive = true;
  private String notes;
  private Date lastLogin;
  private boolean isPasswordReset = false;

  public UserBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public UserBackingFormBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public UserBackingFormBuilder withLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
    return this;
  }

  public UserBackingFormBuilder thatIsStaff() {
    this.isStaff = Boolean.TRUE;
    return this;
  }

  public UserBackingFormBuilder thatIsPasswordReset() {
    this.isPasswordReset = Boolean.TRUE;
    return this;
  }

  public UserBackingFormBuilder thatIsNotPasswordReset() {
    this.isPasswordReset = Boolean.FALSE;
    return this;
  }

  public UserBackingFormBuilder thatIsActive() {
    this.isActive = Boolean.TRUE;
    return this;
  }

  public UserBackingFormBuilder withEmailId(String emailId) {
    this.emailId = emailId;
    return this;
  }

  public UserBackingFormBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  public UserBackingFormBuilder thatIsAdmin() {
    isAdmin = true;
    return this;
  }

  public UserBackingFormBuilder thatIsNotAdmin() {
    isAdmin = false;
    return this;
  }

  public UserBackingFormBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public UserBackingFormBuilder withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public UserBackingFormBuilder withRoles(List<RoleBackingForm> roles) {
    this.roles = roles;
    return this;
  }

  public UserBackingFormBuilder withPassword(String password) {
    this.password = password;
    return this;
  }

  public UserBackingFormBuilder withRole(RoleBackingForm role) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(role);
    return this;
  }

  public UserBackingFormBuilder thatIsDeleted() {
    isDeleted = true;
    return this;
  }

  public UserBackingFormBuilder thatIsNotDeleted() {
    isDeleted = false;
    return this;
  }

  @Override
  public UserBackingForm build() {
    UserBackingForm user = new UserBackingForm();
    user.setId(id);
    user.setEmailId(emailId);
    user.setUsername(username);
    user.setIsAdmin(isAdmin);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setPassword(password);
    user.setIsDeleted(isDeleted);
    user.setIsStaff(isStaff);
    user.setIsAdmin(isAdmin);
    user.setIsActive(isActive);
    user.setRoles(roles);
    user.setNotes(notes);
    user.setLastLogin(lastLogin);
    user.setIsPasswordReset(isPasswordReset);
    return user;
  }

  public static UserBackingFormBuilder aUserBackingForm() {
    return new UserBackingFormBuilder();
  }
}
