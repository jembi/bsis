package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.UserBackingForm;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.viewmodel.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFactory {

  @Autowired
  private RoleFactory roleFactory;

  public UserViewModel createViewModel(User user) {
    UserViewModel viewModel = new UserViewModel();
    viewModel.setId(user.getId());
    viewModel.setUsername(user.getUsername());
    viewModel.setEmailId(user.getEmailId());
    viewModel.setFirstName(user.getFirstName());
    viewModel.setLastName(user.getLastName());
    viewModel.setIsAdmin(user.getIsAdmin());
    viewModel.setPasswordReset(user.isPasswordReset());
    viewModel.setRoles(roleFactory.createViewModels(user.getRoles()));
    return viewModel;
  }

  public List<UserViewModel> createViewModels(List<User> users) {
    List<UserViewModel> userViewModels = new ArrayList<UserViewModel>();
    if (users != null) {
      for (User user : users) {
        userViewModels.add(createViewModel(user));
      }
    }
    return userViewModels;
  }

  public User createEntity(UserBackingForm form) {
    User user = new User();

    user.setId(form.getId());
    user.setUsername(form.getUsername());
    user.setPassword(form.getPassword());
    user.setFirstName(form.getFirstName());
    user.setLastName(form.getLastName());
    user.setEmailId(form.getEmailId());
    user.setIsStaff(form.getIsStaff());
    user.setIsActive(form.getIsActive());
    user.setIsAdmin(form.getIsAdmin());
    user.setIsDeleted(form.getIsDeleted());
    user.setNotes(form.getNotes());
    user.setLastLogin(form.getLastLogin());
    user.setPasswordReset(form.isPasswordReset());
    user.setRoles(roleFactory.createEntities(form.getRoles()));
    return user;
  }
}
