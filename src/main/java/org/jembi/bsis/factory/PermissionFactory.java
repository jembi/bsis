package org.jembi.bsis.factory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
}
