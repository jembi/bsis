package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.viewmodel.BloodTestResultViewModel;

public class BloodTestResultViewModelBuilder extends AbstractBuilder<BloodTestResultViewModel> {
  
  private long id;

  public BloodTestResultViewModelBuilder withId(long id) {
    this.id = id;
    return this;
  }

  @Override
  public BloodTestResultViewModel build() {
    BloodTestResultViewModel viewModel = new BloodTestResultViewModel();
    viewModel.setId(id);
    return viewModel;
  }
  
  public static BloodTestResultViewModelBuilder aBloodTestResultViewModel() {
    return new BloodTestResultViewModelBuilder();
  }

}
