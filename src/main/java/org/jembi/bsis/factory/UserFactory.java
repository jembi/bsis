package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.user.User;
import org.jembi.bsis.viewmodel.UserViewModel;
import org.springframework.stereotype.Service;

@Service
public class UserFactory {

  public UserViewModel createViewModel(User user) {
    return new UserViewModel(user);
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
