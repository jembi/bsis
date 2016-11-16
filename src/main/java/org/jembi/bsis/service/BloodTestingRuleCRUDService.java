package org.jembi.bsis.service;

import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.repository.BloodTestingRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BloodTestingRuleCRUDService {

  @Autowired
  private BloodTestingRuleRepository bloodTestingRuleRepository;
  
  public BloodTestingRule createBloodTestingRule(BloodTestingRule bloodTestingRule) {
    bloodTestingRuleRepository.save(bloodTestingRule);
    return bloodTestingRule;
  }
}