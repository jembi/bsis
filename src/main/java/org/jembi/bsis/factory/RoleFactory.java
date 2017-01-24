package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.user.Role;
import org.jembi.bsis.viewmodel.RoleViewModel;
import org.springframework.stereotype.Service;

@Service
public class RoleFactory {

  public RoleViewModel createViewModel(Role role) {
    return new RoleViewModel(role);
  }

  public List<RoleViewModel> createViewModels(List<Role> roles) {
    List<RoleViewModel> roleViewModels = new ArrayList<RoleViewModel>();
    if (roles != null) {
      for (Role role : roles) {
        roleViewModels.add(createViewModel(role));
      }
    }
    return roleViewModels;
  }
}
