package org.jembi.bsis.helpers.builders;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.viewmodel.DivisionFullViewModel;
import org.jembi.bsis.viewmodel.DivisionViewModel;

public class DivisionFullViewModelBuilder extends AbstractBuilder<DivisionFullViewModel> {
  
  private UUID id;
  private String name;
  private int level;
  private DivisionViewModel parent;
  private Map<String, Boolean> permissions = new HashMap<>();
  
  public DivisionFullViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public DivisionFullViewModelBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public DivisionFullViewModelBuilder withLevel(int level) {
    this.level = level;
    return this;
  }
  
  public DivisionFullViewModelBuilder withParent(DivisionViewModel parent) {
    this.parent = parent;
    return this;
  }
  
  public DivisionFullViewModelBuilder withPermission(String key, boolean value) {
    permissions.put(key, value);
    return this;
  }

  @Override
  public DivisionFullViewModel build() {
    DivisionFullViewModel viewModel = new DivisionFullViewModel();
    viewModel.setId(id);
    viewModel.setName(name);
    viewModel.setLevel(level);
    viewModel.setParent(parent);
    viewModel.setPermissions(permissions);
    return viewModel;
  }
  
  public static DivisionFullViewModelBuilder aDivisionFullViewModel() {
    return new DivisionFullViewModelBuilder();
  }

}
