package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

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
}
