package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.viewmodel.BloodTestResultFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BloodTestResultFactory {

  @Autowired
  private BloodTestFactory bloodTestFactory;
  
  public BloodTestResultFullViewModel createBloodTestResultFullViewModel(BloodTestResult bloodTestResult) {
    BloodTestResultFullViewModel viewModel = new BloodTestResultFullViewModel();
    viewModel.setId(bloodTestResult.getId());
    viewModel.setBloodTest(bloodTestFactory.createFullViewModel(bloodTestResult.getBloodTest()));
    viewModel.setReEntryRequired(bloodTestResult.getReEntryRequired());
    viewModel.setResult(bloodTestResult.getResult());
    viewModel.setTestedOn(bloodTestResult.getTestedOn());
    return viewModel;
  }
  
  public List<BloodTestResultFullViewModel> createBloodTestResultFullViewModels(List<BloodTestResult> bloodTestResults) {
    List<BloodTestResultFullViewModel> viewModels = new ArrayList<>();
    for (BloodTestResult bloodTestResult : bloodTestResults) {
      viewModels.add(createBloodTestResultFullViewModel(bloodTestResult));
    }
    return viewModels;
  }

}
