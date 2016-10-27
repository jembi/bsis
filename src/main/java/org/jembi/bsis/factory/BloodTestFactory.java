package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.springframework.stereotype.Service;

@Service
public class BloodTestFactory {

  public List<BloodTestFullViewModel> createViewModels(List<BloodTest> bloodTests) {
    List<BloodTestFullViewModel> viewModels = new ArrayList<>();
    for (BloodTest bloodTest : bloodTests) {
      viewModels.add(createViewModel(bloodTest));
    }
    return viewModels;
  }

  public BloodTestFullViewModel createViewModel(BloodTest bloodTest) {
    BloodTestFullViewModel viewModel = new BloodTestFullViewModel();
    viewModel.setId(bloodTest.getId());
    viewModel.setTestName(bloodTest.getTestName());
    viewModel.setTestNameShort(bloodTest.getTestNameShort());
    viewModel.setValidResults(bloodTest.getValidResultsList());
    viewModel.setPositiveResults(bloodTest.getPositiveResultsList());
    viewModel.setNegativeResults(bloodTest.getNegativeResultsList());
    viewModel.setBloodTestCategory(bloodTest.getCategory());
    viewModel.setBloodTestType(bloodTest.getBloodTestType());
    viewModel.setRankInCategory(bloodTest.getRankInCategory());
    viewModel.setIsActive(bloodTest.getIsActive());
    viewModel.setIsDeleted(bloodTest.getIsDeleted());
    return viewModel;
  }
}
