package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestViewModel;
import org.springframework.stereotype.Service;

@Service
public class BloodTestFactory {

  public BloodTestViewModel createViewModel(BloodTest bloodTest) {
    BloodTestViewModel viewModel = new BloodTestViewModel();
    populateViewModelFields(bloodTest, viewModel);
    return viewModel;
  }

  public List<BloodTestViewModel> createViewModels(List<BloodTest> bloodTests) {
    List<BloodTestViewModel> viewModels = new ArrayList<>();
    for (BloodTest bloodTest : bloodTests) {
      viewModels.add(createViewModel(bloodTest));
    }
    return viewModels;
  }

  public BloodTestFullViewModel createFullViewModel(BloodTest bloodTest) {
    BloodTestFullViewModel viewModel = new BloodTestFullViewModel();
    populateViewModelFields(bloodTest, viewModel);
    populateFullViewModelFields(bloodTest, viewModel);
    return viewModel;
  }

  public List<BloodTestFullViewModel> createFullViewModels(List<BloodTest> bloodTests) {
    List<BloodTestFullViewModel> viewModels = new ArrayList<>();
    for (BloodTest bloodTest : bloodTests) {
      viewModels.add(createFullViewModel(bloodTest));
    }
    return viewModels;
  }

  private void populateViewModelFields(BloodTest bloodTest, BloodTestViewModel viewModel) {
    viewModel.setId(bloodTest.getId());
    viewModel.setTestName(bloodTest.getTestName());
    viewModel.setTestNameShort(bloodTest.getTestNameShort());
    viewModel.setCategory(bloodTest.getCategory());
    viewModel.setBloodTestType(bloodTest.getBloodTestType());
    viewModel.setIsActive(bloodTest.getIsActive());
    viewModel.setIsDeleted(bloodTest.getIsDeleted());
    viewModel.setRankInCategory(bloodTest.getRankInCategory());
  }

  private void populateFullViewModelFields(BloodTest bloodTest, BloodTestFullViewModel viewModel) {
    viewModel.setValidResults(bloodTest.getValidResultsSet());
    viewModel.setPositiveResults(bloodTest.getPositiveResultsSet());
    viewModel.setNegativeResults(bloodTest.getNegativeResultsSet());
    viewModel.setFlagComponentsForDiscard(bloodTest.isFlagComponentsForDiscard());
    viewModel.setFlagComponentsContainingPlasmaForDiscard(bloodTest.getFlagComponentsContainingPlasmaForDiscard());
  }
  
  public BloodTest createEntity(BloodTestBackingForm bloodTestBackingForm){
    BloodTest bloodTest = new BloodTest();
    
    bloodTest.setId(bloodTestBackingForm.getId());
    bloodTest.setTestName(bloodTestBackingForm.getTestName());
    bloodTest.setTestNameShort(bloodTestBackingForm.getTestNameShort());
    bloodTest.setCategory(bloodTestBackingForm.getCategory());
    bloodTest.setBloodTestType(bloodTestBackingForm.getBloodTestType());
    bloodTest.setValidResults(StringUtils.join(bloodTestBackingForm.getValidResults(), ','));
    bloodTest.setNegativeResults(StringUtils.join(bloodTestBackingForm.getNegativeResults(), ','));
    bloodTest.setPositiveResults(StringUtils.join(bloodTestBackingForm.getPositiveResults(), ','));
    bloodTest.setIsActive(bloodTestBackingForm.getIsActive());
    bloodTest.setIsDeleted(bloodTestBackingForm.getIsDeleted());
    bloodTest.setFlagComponentsContainingPlasmaForDiscard(bloodTestBackingForm.getFlagComponentsContainingPlasmaForDiscard());
    bloodTest.setFlagComponentsForDiscard(bloodTestBackingForm.getFlagComponentsForDiscard());
    bloodTest.setRankInCategory(bloodTestBackingForm.getRankInCategory());
    
    return bloodTest;
  }

}
