package org.jembi.bsis.repository;

import java.util.List;
import java.util.UUID;

import javax.persistence.TypedQuery;

import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BloodTestingRuleRepository extends AbstractRepository<BloodTestingRule> {

  public List<BloodTestingRule> getBloodTestingRules(boolean includeDeleted, boolean includeWithDeletedOrInactiveBloodTest) {
    TypedQuery<BloodTestingRule> query;
    query = entityManager.createNamedQuery(BloodTestingRuleNamedQueryConstants.NAME_GET_BLOOD_TESTING_RULES, BloodTestingRule.class);
    query.setParameter("includeDeleted", includeDeleted);
    query.setParameter("includeWithDeletedOrInactiveBloodTest", includeWithDeletedOrInactiveBloodTest);
    return query.getResultList();
  }

  public BloodTestingRule findBloodTestingRuleById(UUID bloodTestingRuleId) {
    return entityManager.createNamedQuery(BloodTestingRuleNamedQueryConstants.NAME_FIND_BLOOD_TESTING_RULE_BY_ID, BloodTestingRule.class)
        .setParameter("bloodTestingRuleId", bloodTestingRuleId)
        .getSingleResult();
  }

}
