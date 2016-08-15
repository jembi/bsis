package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.viewmodel.BloodTestResultViewModel;
import org.springframework.stereotype.Service;

@Service
public class BloodTestResultFactory {
  
  public BloodTestResultViewModel createBloodTestResultViewModel(BloodTestResult bloodTestResult) {
    return new BloodTestResultViewModel(bloodTestResult);
  }
  
  public List<BloodTestResultViewModel> createBloodTestResultViewModels(List<BloodTestResult> bloodTestResults) {
    List<BloodTestResultViewModel> viewModels = new ArrayList<>();
    for (BloodTestResult bloodTestResult : bloodTestResults) {
      viewModels.add(createBloodTestResultViewModel(bloodTestResult));
    }
    return viewModels;
  }

}
