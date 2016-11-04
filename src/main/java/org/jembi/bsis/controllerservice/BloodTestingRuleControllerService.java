package org.jembi.bsis.controllerservice;

import java.util.List;

import javax.transaction.Transactional;

import org.jembi.bsis.factory.BloodTestingRuleFactory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.repository.BloodTestingRuleRepository;
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
  
  public List<BloodTestingRuleViewModel> getAllBloodTestingRules() {
    List<BloodTestingRule> bloodTestingRules = bloodTestingRuleRepository.getBloodTestingRules(true);
    return bloodTestingRuleFactory.createViewModels(bloodTestingRules);
  }  
}