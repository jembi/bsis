package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleBuilder.aBloodTestingRule;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleMatcher.hasSameStateAsBloodTestingRule;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.jembi.bsis.helpers.builders.BloodTestBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTest;
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
  public void testCreateBloodTestingRule_shouldSave() {
    // Set up data
    BloodTestingRule bloodTestingRule = aBloodTestingRule()
        .withBloodTest(BloodTestBuilder.aBloodTest().withTestNameShort("Rh").withId(UUID.randomUUID()).build())
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .build();
    
    // Run test
    bloodTestingRule = bloodTestingRuleCRUDService.createBloodTestingRule(bloodTestingRule);
    
    // Verify
    verify(bloodTestingRuleRepository).save(argThat(hasSameStateAsBloodTestingRule(bloodTestingRule)));
  }
  
  @Test
  public void testUpdateBloodTestingRule_shouldUpdate() {
    // Set up data
    UUID bloodTestingRuleId = UUID.randomUUID();

    BloodTest bloodTest = aBloodTest()
        .withTestNameShort("Rh")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .build();
    
    BloodTestingRule existingBloodTestingRule = aBloodTestingRule()
        .withId(bloodTestingRuleId)
        .withBloodTest(bloodTest)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .thatIsDeleted()
        .build();
    
    BloodTest bloodTestUpdated = aBloodTest()
        .withTestNameShort("TTITest")
        .withCategory(BloodTestCategory.TTI)
        .build();
    
    BloodTestingRule updatedBloodTestingRule = aBloodTestingRule()
        .withId(bloodTestingRuleId)
        .withBloodTest(bloodTestUpdated)
        .withDonationFieldChanged(DonationField.TTISTATUS)
        .withNewInformation("-")
        .withPattern("NEG")
        .thatIsNotDeleted()
        .build();
    
    // Set up mocks
    when(bloodTestingRuleRepository.findBloodTestingRuleById(bloodTestingRuleId)).thenReturn(existingBloodTestingRule);
    when(bloodTestingRuleRepository.update(existingBloodTestingRule)).thenReturn(updatedBloodTestingRule);

    // Run test
    BloodTestingRule returnedBloodTestingRule = bloodTestingRuleCRUDService.updateBloodTestingRule(updatedBloodTestingRule);
    
    // Verify
    verify(bloodTestingRuleRepository).update(argThat(hasSameStateAsBloodTestingRule(updatedBloodTestingRule)));
    assertThat(returnedBloodTestingRule, hasSameStateAsBloodTestingRule(updatedBloodTestingRule));
  }
}