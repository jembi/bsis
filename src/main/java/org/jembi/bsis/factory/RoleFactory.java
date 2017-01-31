package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.RoleBackingForm;
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

  public Role createEntity(RoleBackingForm form) {
    Role role = new Role();
    role.setId(form.getId());
    role.setName(form.getName());
    role.setDescription(form.getDescription());
    role.setPermissions(permissionFactory.createEntities(form.getPermissions()));
    return role;
  }

  public List<Role> createEntities(List<RoleBackingForm> forms) {
    List<Role> roles = new ArrayList<>();
    for(RoleBackingForm form: forms) {
      roles.add(createEntity(form));
    }
    return roles;
  }
}
