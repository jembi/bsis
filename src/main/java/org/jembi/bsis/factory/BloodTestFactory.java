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
    viewModel.setTestNameShort(bloodTest.getTestNameShort());
    viewModel.setBloodTestCategory(bloodTest.getCategory());
    viewModel.setBloodTestType(bloodTest.getBloodTestType());
    viewModel.setIsActive(bloodTest.getIsActive());
    viewModel.setIsDeleted(bloodTest.getIsDeleted());
  }

  private void populateFullViewModelFields(BloodTest bloodTest, BloodTestFullViewModel viewModel) {
    viewModel.setTestName(bloodTest.getTestName());
    viewModel.setValidResults(bloodTest.getValidResultsList());
    viewModel.setPositiveResults(bloodTest.getPositiveResultsList());
    viewModel.setNegativeResults(bloodTest.getNegativeResultsList());
    viewModel.setRankInCategory(bloodTest.getRankInCategory());
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
    bloodTest.setFlagComponentsContainingPlasmaForDiscard(bloodTestBackingForm.isFlagComponentsContainingPlasmaForDiscard());
    bloodTest.setFlagComponentsForDiscard(bloodTestBackingForm.isFlagComponentsForDiscard());
    
    return bloodTest;
  }

}