package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.viewmodel.PermissionViewModel;

public class PermissionViewModelBuilder extends AbstractBuilder<PermissionViewModel> {

  private Long id;
  private String name;

  public PermissionViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public PermissionViewModelBuilder withName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public PermissionViewModel build() {
    PermissionViewModel perm = new PermissionViewModel();
    perm.setId(id);
    perm.setName(name);
    return perm;
  }

  public static PermissionViewModelBuilder aPermissionViewModel() {
    return new PermissionViewModelBuilder();
  }
}