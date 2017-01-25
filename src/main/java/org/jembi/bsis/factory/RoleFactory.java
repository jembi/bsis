package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.user.Role;
import org.jembi.bsis.viewmodel.RoleViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleFactory {

  @Autowired
  PermissionFactory permissionFactory;

  public RoleViewModel createViewModel(Role role) {
    RoleViewModel roleViewModel = new RoleViewModel();
    roleViewModel.setId(role.getId());
    roleViewModel.setName(role.getName());
    roleViewModel.setDescription(role.getDescription());
    roleViewModel.setPermissions(permissionFactory.createViewModels(role.getPermissions()));
    return roleViewModel;
  }

  public List<RoleViewModel> createViewModels(List<Role> roles) {
    List<RoleViewModel> roleViewModels = new ArrayList<>();
    if (roles != null) {
      for (Role role : roles) {
        roleViewModels.add(createViewModel(role));
      }
    }
    return roleViewModels;
  }
}
