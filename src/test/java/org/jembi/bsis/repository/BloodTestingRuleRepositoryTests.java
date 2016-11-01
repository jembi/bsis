package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleBuilder.aBloodTestingRule;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleMatcher.hasSameStateAsBloodTestingRule;

import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestSubCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BloodTestingRuleRepositoryTests extends ContextDependentTestSuite {
  
  @Autowired
  private BloodTestingRuleRepository bloodTestingRuleRepository;
  
  @Test
  public void tesGetBloodTestingRules_shouldReturnActive() {
    
    BloodTestingRule activeRule = aBloodTestingRule()
        .withBloodTestsIds("1")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withSubCategory(BloodTestSubCategory.BLOODABO)
        .withDonationFieldChange(DonationField.BLOODABO)
        .withNewInformation("A")
        .withPattern("A")
        .withPendingTestsIds("2,3")
        .buildAndPersist(entityManager);

    // inactive rule excluded
    aBloodTestingRule().thatIsDeleted().buildAndPersist(entityManager);

    List<BloodTestingRule> rules = bloodTestingRuleRepository.getBloodTestingRules(false);

    assertThat("Only active rules returned", rules.size(), is(1));
    assertThat("Only active rule returned", rules.get(0), hasSameStateAsBloodTestingRule(activeRule));
  }

  @Test
  public void tesGetBloodTestingRules_shouldReturnAll() {
    
    aBloodTestingRule()
        .withBloodTestsIds("1")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withSubCategory(BloodTestSubCategory.BLOODABO)
        .withDonationFieldChange(DonationField.BLOODABO)
        .withNewInformation("A")
        .withPattern("A")
        .withPendingTestsIds("2,3")
        .buildAndPersist(entityManager);

    aBloodTestingRule().thatIsDeleted().buildAndPersist(entityManager);

    List<BloodTestingRule> rules = bloodTestingRuleRepository.getBloodTestingRules(true);

    assertThat("Only active rules returned", rules.size(), is(2));
  }
}
