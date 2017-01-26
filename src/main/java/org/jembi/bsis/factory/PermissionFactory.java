package org.jembi.bsis.factory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jembi.bsis.backingform.PermissionBackingForm;
import org.jembi.bsis.model.user.Permission;
import org.jembi.bsis.viewmodel.PermissionViewModel;
import org.springframework.stereotype.Service;

@Service
public class PermissionFactory {

  public PermissionViewModel createViewModel(Permission permission) {
    PermissionViewModel viewModel = new PermissionViewModel();
    viewModel.setId(permission.getId());
    viewModel.setName(permission.getName());
    return viewModel;
  }

  public Set<PermissionViewModel> createViewModels(Collection<Permission> permissions) {
    Set<PermissionViewModel> viewModels = new HashSet<>();
    if (permissions != null) {
      for (Permission permission : permissions) {
        viewModels.add(createViewModel(permission));
      }
    }
    return viewModels;
  }

  public Permission createEntity(PermissionBackingForm form) {
    Permission permission = new Permission();
    permission.setId(form.getId());
    permission.setName(form.getName());
    return permission;
  }

  public Set<Permission> createEntities(Set<PermissionBackingForm> forms) {
    Set<Permission> entities = new HashSet<>();
    for(PermissionBackingForm form: forms) {
      entities.add(createEntity(form));
    }
    return entities;
  }
}
