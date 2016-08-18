package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.viewmodel.DivisionViewModel;

public class DivisionViewModelBuilder extends AbstractBuilder<DivisionViewModel> {
  
  private long id;
  private String name;
  private int level;
  private DivisionViewModel parentDivision;
  
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
  
  public DivisionViewModelBuilder withParentDivision(DivisionViewModel parentDivision) {
    this.parentDivision = parentDivision;
    return this;
  }

  @Override
  public DivisionViewModel build() {
    DivisionViewModel viewModel = new DivisionViewModel();
    viewModel.setId(id);
    viewModel.setName(name);
    viewModel.setLevel(level);
    viewModel.setParentDivision(parentDivision);
    return viewModel;
  }
  
  public static DivisionViewModelBuilder aDivisionViewModel() {
    return new DivisionViewModelBuilder();
  }

}
