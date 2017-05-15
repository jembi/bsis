package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestResultViewModel;

public class BloodTestResultViewModelBuilder extends AbstractBuilder<BloodTestResultViewModel> {
  
  private UUID id;
  private BloodTestFullViewModel bloodTest;
  private Boolean reEntryRequired;
  

  public BloodTestResultViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }
  
  public BloodTestResultViewModelBuilder withBloodTest(BloodTestFullViewModel bloodTest) {
    this.bloodTest = bloodTest;
    return this;
  }
  
  public BloodTestResultViewModelBuilder withReEntryRequired() {
    this.reEntryRequired = true; 
    return this;
  }
  
  public BloodTestResultViewModelBuilder withReEntryNotRequired() {
    this.reEntryRequired = false;
    return this;
  } 
  
  @Override
  public BloodTestResultViewModel build() {
    BloodTestResultViewModel viewModel = new BloodTestResultViewModel();
    viewModel.setId(id);
    viewModel.setBloodTest(bloodTest);
    viewModel.setReEntryRequired(reEntryRequired);
    
    return viewModel;
  }
  
  public static BloodTestResultViewModelBuilder aBloodTestResultViewModel() {
    return new BloodTestResultViewModelBuilder();
  }

}
