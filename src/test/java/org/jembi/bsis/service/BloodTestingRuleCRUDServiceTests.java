package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.builders.BloodTestingRuleBuilder.aBloodTestingRule;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleMatcher.hasSameStateAsBloodTestingRule;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

import org.jembi.bsis.helpers.builders.BloodTestBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.repository.BloodTestingRuleRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class BloodTestingRuleCRUDServiceTests extends UnitTestSuite {

  @InjectMocks
  private BloodTestingRuleCRUDService bloodTestingRuleCRUDService;
  @Mock
  private BloodTestingRuleRepository bloodTestingRuleRepository;
  
  @Test
  public void testCreateBloodTest_shouldSave() {
    // Set up data
    BloodTestingRule bloodTestingRule = aBloodTestingRule()
        .withBloodTest(BloodTestBuilder.aBloodTest().withTestNameShort("Rh").withId(1L).build())
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .build();
    
    // Run test
    bloodTestingRule = bloodTestingRuleCRUDService.createBloodTestingRule(bloodTestingRule);
    
    // Verify
    verify(bloodTestingRuleRepository).save(argThat(hasSameStateAsBloodTestingRule(bloodTestingRule)));
  }
}