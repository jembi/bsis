package org.jembi.bsis.factory;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.BloodTestingRuleBackingForm;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.viewmodel.BloodTestingRuleFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BloodTestingRuleFactory {

  @Autowired
  private BloodTestFactory bloodTestFactory;
  
  public List<BloodTestingRuleViewModel> createViewModels(List<BloodTestingRule> bloodTestingRules) {
    List<BloodTestingRuleViewModel> bloodTestingRuleViewModels = new ArrayList<>();
    if (bloodTestingRules != null) {
      for (BloodTestingRule bloodTestingRule : bloodTestingRules) {
        bloodTestingRuleViewModels.add(createViewModel(bloodTestingRule));
      } 
    }
    return bloodTestingRuleViewModels;
  }
  
  public BloodTestingRuleViewModel createViewModel(BloodTestingRule bloodTestingRule) {
    BloodTestingRuleViewModel viewModel = new BloodTestingRuleViewModel();
    if(bloodTestingRule != null) {
      populateBloodTestingRuleViewModel(viewModel, bloodTestingRule);
    }
    return viewModel;
  }

  public BloodTestingRuleFullViewModel createFullViewModel(BloodTestingRule bloodTestingRule) {
    BloodTestingRuleFullViewModel fullViewModel = new BloodTestingRuleFullViewModel();
    if (bloodTestingRule != null) {
      populateBloodTestingRuleViewModel(fullViewModel, bloodTestingRule);
      populateBloodTestingRuleFullViewModel(fullViewModel, bloodTestingRule);
    }
    return  fullViewModel;
  }

  public List<BloodTestingRuleFullViewModel> createFullViewModels(List<BloodTestingRule> bloodTestingRules) {
    List<BloodTestingRuleFullViewModel> fullViewModels = new ArrayList<>();
    for (BloodTestingRule bloodTestingRule : bloodTestingRules) {
      fullViewModels.add(createFullViewModel(bloodTestingRule));
    }
    return  fullViewModels;
  }
  
  private void populateBloodTestingRuleViewModel(BloodTestingRuleViewModel viewModel,
      BloodTestingRule bloodTestingRule) { 
    viewModel.setId(bloodTestingRule.getId());
    viewModel.setTestNameShort(bloodTestingRule.getBloodTest().getTestNameShort());
    viewModel.setDonationFieldChanged(bloodTestingRule.getDonationFieldChanged());
    viewModel.setCategory(bloodTestingRule.getCategory());
    viewModel.setNewInformation(bloodTestingRule.getNewInformation());
    viewModel.setPattern(bloodTestingRule.getPattern());
    viewModel.setIsDeleted(bloodTestingRule.getIsDeleted());
  }
  
  public BloodTestingRule createEntity(BloodTestingRuleBackingForm bloodTestingRuleBackingForm) {
    BloodTestingRule bloodTestingRule = new BloodTestingRule();
    
    bloodTestingRule.setId(bloodTestingRuleBackingForm.getId());
    bloodTestingRule.setCategory(bloodTestingRuleBackingForm.getCategory());
    bloodTestingRule.setDonationFieldChanged(bloodTestingRuleBackingForm.getDonationFieldChanged());
    bloodTestingRule.setIsDeleted(bloodTestingRuleBackingForm.isDeleted());
    bloodTestingRule.setNewInformation(bloodTestingRuleBackingForm.getNewInformation());
    bloodTestingRule.setPattern(bloodTestingRuleBackingForm.getPattern());
    bloodTestingRule.setBloodTest(bloodTestingRuleBackingForm.getBloodTest());
    bloodTestingRule.setPendingTestsIds(StringUtils.join(bloodTestingRuleBackingForm.getPendingTestsIds(), ','));
    return bloodTestingRule;
  }

  private void populateBloodTestingRuleFullViewModel(BloodTestingRuleFullViewModel fullViewModel,
      BloodTestingRule bloodTestingRule) {
    fullViewModel.setPendingTestsIds(bloodTestingRule.getPendingTestsIds());
    fullViewModel.setBloodTest(bloodTestFactory.createFullViewModel(bloodTestingRule.getBloodTest()));
  }
}
