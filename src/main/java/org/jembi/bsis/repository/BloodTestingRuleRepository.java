package org.jembi.bsis.repository;

import java.util.List;

import javax.persistence.TypedQuery;

import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BloodTestingRuleRepository extends AbstractRepository<BloodTestingRule> {

  public List<BloodTestingRule> getBloodTestingRules(boolean includeInactive) {
    TypedQuery<BloodTestingRule> query;
    query = entityManager.createNamedQuery(BloodTestingRuleNamedQueryConstants.NAME_GET_ACTIVE_BLOOD_TESTING_RULES, BloodTestingRule.class);
    query.setParameter("includeInactive", includeInactive);
    return query.getResultList();
  }

}
