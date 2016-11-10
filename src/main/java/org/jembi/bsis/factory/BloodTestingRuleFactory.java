package org.jembi.bsis.factory;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.BloodTestingRuleBackingForm;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.viewmodel.BloodTestingRuleViewModel;
import org.springframework.stereotype.Service;

@Service
public class BloodTestingRuleFactory {
  
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
    bloodTestingRule.setContext(bloodTestingRuleBackingForm.getContext());
    bloodTestingRule.setDonationFieldChanged(bloodTestingRuleBackingForm.getDonationFieldChanged());
    bloodTestingRule.setExtraInformation(bloodTestingRuleBackingForm.getExtraInformation());
    bloodTestingRule.setIsDeleted(bloodTestingRuleBackingForm.isDeleted());
    bloodTestingRule.setMarkSampleAsUnsafe(bloodTestingRuleBackingForm.getMarkSampleAsUnsafe());
    bloodTestingRule.setNewInformation(bloodTestingRuleBackingForm.getNewInformation());
    bloodTestingRule.setPattern(bloodTestingRuleBackingForm.getPattern());
    bloodTestingRule.setSubCategory(bloodTestingRuleBackingForm.getSubCategory());
    bloodTestingRule.setBloodTest(bloodTestingRuleBackingForm.getBloodTest());
    bloodTestingRule.setPendingTestsIds(StringUtils.join(bloodTestingRuleBackingForm.getPendingTestsIds(), ','));
    return bloodTestingRule;
  }
}
