package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aBloodTestPersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;

public class BloodTestingRulePersister extends AbstractEntityPersister<BloodTestingRule> {

  @Override
  public BloodTestingRule deepPersist(BloodTestingRule bloodTestingRule, EntityManager entityManager) {

    if (bloodTestingRule.getBloodTest() != null) {
      aBloodTestPersister().deepPersist(bloodTestingRule.getBloodTest(), entityManager);
    }
    
    return persist(bloodTestingRule, entityManager);
  }
}
