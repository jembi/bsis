package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleBuilder.aBloodTestingRule;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleMatcher.hasSameStateAsBloodTestingRule;

import java.util.List;

import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BloodTestingRuleRepositoryTests extends ContextDependentTestSuite {
  
  @Autowired
  private BloodTestingRuleRepository bloodTestingRuleRepository;
  
  @Test
  public void tesGetBloodTestingRules_shouldReturnEnabled() {
    
    BloodTestingRule enabledRule = aBloodTestingRule().buildAndPersist(entityManager);
    aBloodTestingRule().thatIsDeleted().buildAndPersist(entityManager); // excluded due to being deleted

    List<BloodTestingRule> rules = bloodTestingRuleRepository.getBloodTestingRules(false);

    assertThat("Only enabled rules returned", rules.size(), is(1));
    assertThat("Only enabled rule returned", rules.get(0), hasSameStateAsBloodTestingRule(enabledRule));
  }

  @Test
  public void tesGetBloodTestingRules_shouldReturnAll() {
    
    BloodTestingRule enabledRule = aBloodTestingRule().buildAndPersist(entityManager);
    BloodTestingRule deletedRule = aBloodTestingRule().thatIsDeleted().buildAndPersist(entityManager);

    List<BloodTestingRule> rules = bloodTestingRuleRepository.getBloodTestingRules(true);

    assertThat("All rules returned", rules.size(), is(2));
    assertThat("Enabled rule returned", rules.get(0), hasSameStateAsBloodTestingRule(enabledRule));
    assertThat("Deleted rule returned", rules.get(1), hasSameStateAsBloodTestingRule(deletedRule));
  }
}
