package org.jembi.bsis.viewmodel;

import java.util.List;
import java.util.UUID;

public class UserViewModel extends BaseViewModel<UUID> {

  private String username;
  private String firstName;
  private String lastName;
  private Boolean isAdmin;
  private String emailId;
  private Boolean passwordReset;

  private List<RoleViewModel> roles;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Boolean getIsAdmin() {
    return isAdmin;
  }

  public void setIsAdmin(Boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public Boolean isPasswordReset() {
    return passwordReset;
  }

  public void setPasswordReset(Boolean isPasswordReset) {
    this.passwordReset = isPasswordReset;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public List<RoleViewModel> getRoles() {
    return roles;
  }

  public void setRoles(List<RoleViewModel> roles) {
    this.roles = roles;
  }
}