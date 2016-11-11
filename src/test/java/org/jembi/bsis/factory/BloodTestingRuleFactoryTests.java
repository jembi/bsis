package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleMatcher.hasSameStateAsBloodTestingRule;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleViewModelMatcher.hasSameStateAsBloodTestingRuleViewModel;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleFullViewModelMatcher.hasSameStateAsBloodTestingRuleFullViewModel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.backingform.BloodTestingRuleBackingForm;
import org.jembi.bsis.helpers.builders.BloodTestBackingFormBuilder;
import org.jembi.bsis.helpers.builders.BloodTestBuilder;
import org.jembi.bsis.helpers.builders.BloodTestFullViewModelBuilder;
import org.jembi.bsis.helpers.builders.BloodTestingRuleBackingFormBuilder;
import org.jembi.bsis.helpers.builders.BloodTestingRuleBuilder;
import org.jembi.bsis.helpers.builders.BloodTestingRuleFullViewModelBuilder;
import org.jembi.bsis.helpers.builders.BloodTestingRuleViewModelBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class BloodTestingRuleFactoryTests extends UnitTestSuite {
  
  @InjectMocks
  private BloodTestingRuleFactory bloodTestingRuleFactory;

  @Mock
  private BloodTestFactory bloodTestFactory;
  
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
  public void testCreateViewModels_shouldReturnViewModelsWithTheCorrectState() {
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
  
  @Test
  public void testConvertBloodTestingRuleBackingFormToBloodTestingRuleEntity_shouldReturnExpectedEntity() {
    // Set up fixture
    BloodTest bloodTest = BloodTestBuilder.aBloodTest()
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withTestNameShort("Rh")
        .withId(1L)
        .build();
    BloodTestBackingForm bloodTestBackingForm = BloodTestBackingFormBuilder.aBloodTestBackingForm()
        .withTestNameShort(bloodTest.getTestNameShort())
        .withCategory(bloodTest.getCategory())
        .withId(bloodTest.getId())
        .build();
    BloodTestingRuleBackingForm bloodTestingRuleBackingForm = BloodTestingRuleBackingFormBuilder.aBloodTestingRuleBackingForm()
        .withId(1L)
        .withBloodTest(bloodTestBackingForm)
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .build();

    BloodTestingRule expectedEntity = BloodTestingRuleBuilder.aBloodTestingRule()
        .withId(1L)
        .withBloodTest(bloodTest)
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .build();

    when(bloodTestFactory.createEntity(bloodTestingRuleBackingForm.getBloodTest())).thenReturn(bloodTest);

    // Exercise SUT
    BloodTestingRule returnedEntity = bloodTestingRuleFactory.createEntity(bloodTestingRuleBackingForm);
    // Verify
    assertThat(returnedEntity, hasSameStateAsBloodTestingRule(expectedEntity));

  }

  @Test
  public void testCreateFullViewModel_shouldReturnFullViewModelWithTheCorrectState() {
    // Set up fixture
    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(1L).withTestNameShort("Rh").withTestName("Rh").build();
    BloodTestingRule bloodTestingRule = BloodTestingRuleBuilder.aBloodTestingRule()
        .withId(1L)
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .withPendingTestsIds("1L")
        .withBloodTest(bloodTest)
        .build();

    // Set up expectations
    BloodTestFullViewModel bloodTestFullViewModel = BloodTestFullViewModelBuilder.aBloodTestFullViewModel().withId(1L).withTestNameShort("Rh").build();
    BloodTestingRuleFullViewModel expectedFullViewModel = BloodTestingRuleFullViewModelBuilder.aBloodTestingRuleFullViewModel()
        .withId(1L)
        .withTestNameShort("Rh")
        .withBloodTestCategory(BloodTestCategory.BLOODTYPING)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .withPendingTestIds(new HashSet<String>(Arrays.asList("1L")))
        .withBloodTest(bloodTestFullViewModel)
        .build();

    // Set up mocks
    when(bloodTestFactory.createFullViewModel(bloodTest)).thenReturn(bloodTestFullViewModel);
    
    // Exercise SUT
    BloodTestingRuleFullViewModel returnedFullViewModel = bloodTestingRuleFactory.createFullViewModel(bloodTestingRule);

    // Verify
    assertThat(returnedFullViewModel, hasSameStateAsBloodTestingRuleFullViewModel(expectedFullViewModel));
  }

  @Test
  public void testCreateFullViewModels_shouldReturnFullViewModelsWithTheCorrectState() {
    // Set up fixture
    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(1L).build();
    List<BloodTestingRule> bloodTestingRules = Arrays.asList(
        BloodTestingRuleBuilder.aBloodTestingRule().withId(1L).withBloodTest(bloodTest).build(),
        BloodTestingRuleBuilder.aBloodTestingRule().withId(2L).withBloodTest(bloodTest).build());

    // Set up expectations
    List<BloodTestingRuleFullViewModel> expectedFullViewModels = Arrays.asList(
        BloodTestingRuleFullViewModelBuilder.aBloodTestingRuleFullViewModel()
            .withId(1L)
            .withTestNameShort(bloodTestingRules.get(0).getBloodTest().getTestNameShort())
            .build(),
        BloodTestingRuleFullViewModelBuilder.aBloodTestingRuleFullViewModel()
            .withId(2L)
            .withTestNameShort(bloodTestingRules.get(1).getBloodTest().getTestNameShort())
            .build());

    BloodTestFullViewModel bloodTestFullViewModel = BloodTestFullViewModelBuilder.aBloodTestFullViewModel().withId(1l).build();

    // Set up mocks
    when(bloodTestFactory.createFullViewModel(bloodTest)).thenReturn(bloodTestFullViewModel);

    // Exercise SUT
    List<BloodTestingRuleFullViewModel> returnedViewModels = bloodTestingRuleFactory.createFullViewModels(bloodTestingRules);

    // Verify
    assertThat("Correct number of view models returned", returnedViewModels.size(), is(2));
    assertThat(returnedViewModels.get(0), hasSameStateAsBloodTestingRuleFullViewModel(expectedFullViewModels.get(0)));
    assertThat(returnedViewModels.get(1), hasSameStateAsBloodTestingRuleFullViewModel(expectedFullViewModels.get(1)));
  }
}
