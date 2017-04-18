package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.viewmodel.RoleViewModel;
import org.jembi.bsis.viewmodel.UserViewModel;

public class UserViewModelBuilder extends AbstractBuilder<UserViewModel> {

  private UUID id;
  private String emailId;
  private String username;
  private boolean passwordReset;
  private boolean isAdmin;
  private String firstName;
  private String lastName;
  private List<RoleViewModel> roles;

  public UserViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public UserViewModelBuilder withEmailId(String emailId) {
    this.emailId = emailId;
    return this;
  }

  public UserViewModelBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  public UserViewModelBuilder withPasswordReset() {
    passwordReset = true;
    return this;
  }

  public UserViewModelBuilder thatIsAdmin() {
    isAdmin = true;
    return this;
  }

  public UserViewModelBuilder thatIsNotAdmin() {
    isAdmin = false;
    return this;
  }

  public UserViewModelBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public UserViewModelBuilder withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public UserViewModelBuilder withRoles(List<RoleViewModel> roles) {
    this.roles = roles;
    return this;
  }

  public UserViewModelBuilder withRole(RoleViewModel role) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(role);
    return this;
  }

  @Override
  public UserViewModel build() {
    UserViewModel user = new UserViewModel();
    user.setId(id);
    user.setEmailId(emailId);
    user.setUsername(username);
    user.setPasswordReset(passwordReset);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setIsAdmin(isAdmin);
    user.setRoles(roles);
    return user;
  }

  public static UserViewModelBuilder aUserViewModel() {
    return new UserViewModelBuilder();
  }
}
