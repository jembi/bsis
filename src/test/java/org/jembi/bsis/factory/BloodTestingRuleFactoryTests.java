package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleViewModelMatcher.hasSameStateAsBloodTestingRuleViewModel;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.helpers.builders.BloodTestBuilder;
import org.jembi.bsis.helpers.builders.BloodTestingRuleBuilder;
import org.jembi.bsis.helpers.builders.BloodTestingRuleViewModelBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestingRuleViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;

public class BloodTestingRuleFactoryTests extends UnitTestSuite {
  
  @InjectMocks
  private BloodTestingRuleFactory bloodTestingRuleFactory;
  
  @Test
  public void testCreateViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    BloodTestingRule bloodTestingRule = BloodTestingRuleBuilder.aBloodTestingRule()
        .withId(1L)
        .withBloodTest(BloodTestBuilder.aBloodTest().withTestNameShort("Rh").build())
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .build();

    // Set up expectations
    BloodTestingRuleViewModel expectedViewModel = BloodTestingRuleViewModelBuilder.aBloodTestingRuleViewModel() 
        .withId(1L)
        .withTestNameShort("Rh")
        .withBloodTestCategory(BloodTestCategory.BLOODTYPING)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .build();

    // Exercise SUT
    BloodTestingRuleViewModel returnedViewModel = bloodTestingRuleFactory.createViewModel(bloodTestingRule);

    // Verify
    assertThat(returnedViewModel, hasSameStateAsBloodTestingRuleViewModel(expectedViewModel));
  }

  @Test
  public void testCreateFullViewModels_shouldReturnViewModelsWithTheCorrectState() {
    // Set up fixture
    List<BloodTestingRule> bloodTestingRules = Arrays.asList(
        BloodTestingRuleBuilder.aBloodTestingRule().withId(1L).build(),
        BloodTestingRuleBuilder.aBloodTestingRule().withId(2L).build());

    // Set up expectations
    List<BloodTestingRuleViewModel> expectedViewModels = Arrays.asList(
        BloodTestingRuleViewModelBuilder.aBloodTestingRuleViewModel()
            .withId(1L)
            .withTestNameShort(bloodTestingRules.get(0).getBloodTest().getTestNameShort())
            .build(),
        BloodTestingRuleViewModelBuilder.aBloodTestingRuleViewModel()
            .withId(2L)
            .withTestNameShort(bloodTestingRules.get(1).getBloodTest().getTestNameShort())
            .build());

    // Exercise SUT
    List<BloodTestingRuleViewModel> returnedViewModels = bloodTestingRuleFactory.createViewModels(bloodTestingRules);

    // Verify
    assertThat("Correct number of view models returned", returnedViewModels.size(), is(2));
    assertThat(returnedViewModels.get(0), hasSameStateAsBloodTestingRuleViewModel(expectedViewModels.get(0)));
    assertThat(returnedViewModels.get(1), hasSameStateAsBloodTestingRuleViewModel(expectedViewModels.get(1)));
  }

}
