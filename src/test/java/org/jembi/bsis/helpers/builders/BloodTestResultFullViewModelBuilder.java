package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestResultFullViewModel;

public class BloodTestResultFullViewModelBuilder extends AbstractBuilder<BloodTestResultFullViewModel> {
  
  private UUID id;
  private BloodTestFullViewModel bloodTest;
  private Boolean reEntryRequired;
  private String result;

  public BloodTestResultFullViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }
  
  public BloodTestResultFullViewModelBuilder withBloodTest(BloodTestFullViewModel bloodTest) {
    this.bloodTest = bloodTest;
    return this;
  }
  
  public BloodTestResultFullViewModelBuilder withReEntryRequired() {
    this.reEntryRequired = true; 
    return this;
  }
  
  public BloodTestResultFullViewModelBuilder withReEntryNotRequired() {
    this.reEntryRequired = false;
    return this;
  }

  public BloodTestResultFullViewModelBuilder withResult(String result) {
    this.result = result;
    return this;
  }
  
  @Override
  public BloodTestResultFullViewModel build() {
    BloodTestResultFullViewModel viewModel = new BloodTestResultFullViewModel();
    viewModel.setId(id);
    viewModel.setBloodTest(bloodTest);
    viewModel.setReEntryRequired(reEntryRequired);
    viewModel.setResult(result);
    
    return viewModel;
  }
  
  public static BloodTestResultFullViewModelBuilder aBloodTestResultFullViewModel() {
    return new BloodTestResultFullViewModelBuilder();
  }

}
