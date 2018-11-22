package org.jembi.bsis.controllerservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.jembi.bsis.backingform.BloodTestingRuleBackingForm;
import org.jembi.bsis.factory.BloodTestFactory;
import org.jembi.bsis.factory.BloodTestingRuleFactory;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.repository.BloodTestingRuleRepository;
import org.jembi.bsis.service.BloodTestingRuleCRUDService;
import org.jembi.bsis.viewmodel.BloodTestViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BloodTestingRuleControllerService {
  
  @Autowired
  private BloodTestingRuleFactory bloodTestingRuleFactory;

  @Autowired
  private BloodTestingRuleRepository bloodTestingRuleRepository;

  @Autowired
  private BloodTestFactory bloodTestFactory;

  @Autowired
  private BloodTestRepository bloodTestRepository;

  @Autowired
  private BloodTestingRuleCRUDService bloodTestingRuleCRUDService;

  
  public List<BloodTestingRuleViewModel> getAllBloodTestingRules() {
    List<BloodTestingRule> bloodTestingRules = bloodTestingRuleRepository.getBloodTestingRules(true, true);
    return bloodTestingRuleFactory.createViewModels(bloodTestingRules);
  }

  public List<BloodTestViewModel> getAllBloodTests() {
    List<BloodTest> bloodTests = bloodTestRepository.getBloodTests(true, false);
    return bloodTestFactory.createViewModels(bloodTests);
  }

  public Map<DonationField, List<String>> getNewInformationForDonationFields() {
    Map<DonationField, List<String>> newInformation = new HashMap<>();
    for (DonationField donationField : DonationField.values()) {
      newInformation.put(donationField, DonationField.getNewInformationForDonationField(donationField));
    }
    return newInformation;
  }

  public Map<BloodTestCategory, List<DonationField>> getDonationFieldsForBloodTestCategory() {
    Map<BloodTestCategory, List<DonationField>> donationFields = new HashMap<>();
    for (BloodTestCategory category : BloodTestCategory.values()) {
      donationFields.put(category, DonationField.getDonationFieldsForCategory(category));
    }
    return donationFields;
  }

  public BloodTestingRuleFullViewModel createBloodTestingRule(BloodTestingRuleBackingForm backingForm) {
    BloodTestingRule bloodTestingRule = bloodTestingRuleFactory.createEntity(backingForm);
    return bloodTestingRuleFactory.createFullViewModel(bloodTestingRuleCRUDService.createBloodTestingRule(bloodTestingRule));
  }
  
  public BloodTestingRuleViewModel findBloodTestingRuleById(UUID id) {
    return bloodTestingRuleFactory.createFullViewModel(bloodTestingRuleRepository.findBloodTestingRuleById(id));
  }
  
  public BloodTestingRuleViewModel updateBloodTestinRule(BloodTestingRuleBackingForm bloodTestingRulebackingForm) {
    BloodTestingRule bloodtestingRule = bloodTestingRuleFactory.createEntity(bloodTestingRulebackingForm);
    BloodTestingRule updateBloodTestingRule = bloodTestingRuleCRUDService.updateBloodTestingRule(bloodtestingRule);
    return bloodTestingRuleFactory.createFullViewModel(updateBloodTestingRule);
  }
}