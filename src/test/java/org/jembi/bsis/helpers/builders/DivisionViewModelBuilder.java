package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.viewmodel.DivisionViewModel;

public class DivisionViewModelBuilder extends AbstractBuilder<DivisionViewModel> {
  
  private long id;
  private String name;
  private int level;
  private DivisionViewModel parent;
  
  public DivisionViewModelBuilder withId(long id) {
    this.id = id;
    return this;
  }

  public DivisionViewModelBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public DivisionViewModelBuilder withLevel(int level) {
    this.level = level;
    return this;
  }
  
  public DivisionViewModelBuilder withParent(DivisionViewModel parent) {
    this.parent = parent;
    return this;
  }

  @Override
  public DivisionViewModel build() {
    DivisionViewModel viewModel = new DivisionViewModel();
    viewModel.setId(id);
    viewModel.setName(name);
    viewModel.setLevel(level);
    viewModel.setParent(parent);
    return viewModel;
  }
  
  public static DivisionViewModelBuilder aDivisionViewModel() {
    return new DivisionViewModelBuilder();
  }

}
