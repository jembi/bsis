package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.jembi.bsis.helpers.builders.BloodTestBackingFormBuilder.aBloodTestBackingForm;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestFullViewModelBuilder.aBloodTestFullViewModel;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleBackingFormBuilder.aBloodTestingRuleBackingForm;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleBuilder.aBloodTestingRule;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleFullViewModelBuilder.aBloodTestingRuleFullViewModel;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleViewModelBuilder.aBloodTestingRuleViewModel;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleMatcher.hasSameStateAsBloodTestingRule;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleViewModelMatcher.hasSameStateAsBloodTestingRuleViewModel;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleFullViewModelMatcher.hasSameStateAsBloodTestingRuleFullViewModel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.backingform.BloodTestingRuleBackingForm;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.repository.BloodTestRepository;
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
  
  @Mock
  private BloodTestRepository bloodTestRepository; 
  
  @Test
  public void testCreateViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    BloodTestingRule bloodTestingRule = aBloodTestingRule()
        .withId(1L)
        .withBloodTest(aBloodTest().withTestNameShort("Rh").build())
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .build();

    // Set up expectations
    BloodTestingRuleViewModel expectedViewModel = aBloodTestingRuleViewModel()
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
  public void testCreateViewModels_shouldReturnViewModelsWithTheCorrectState () {
    // Set up fixture
    List<BloodTestingRule> bloodTestingRules = Arrays.asList(
        aBloodTestingRule().withId(1L).build(),
        aBloodTestingRule().withId(2L).build());

    // Set up expectations
    List<BloodTestingRuleViewModel> expectedViewModels = Arrays.asList(
        aBloodTestingRuleViewModel()
            .withId(1L)
            .withTestNameShort(bloodTestingRules.get(0).getBloodTest().getTestNameShort())
            .build(),
        aBloodTestingRuleViewModel()
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
    BloodTest bloodTest = aBloodTest()
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withTestNameShort("Rh")
        .withId(1L)
        .build();
    BloodTestBackingForm bloodTestBackingForm = aBloodTestBackingForm()
        .withTestNameShort(bloodTest.getTestNameShort())
        .withCategory(bloodTest.getCategory())
        .withId(bloodTest.getId())
        .build();
    BloodTestingRuleBackingForm bloodTestingRuleBackingForm = aBloodTestingRuleBackingForm()
        .withId(1L)
        .withBloodTest(bloodTestBackingForm)
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .build();

    BloodTestingRule expectedEntity = aBloodTestingRule()
        .withId(1L)
        .withBloodTest(bloodTest)
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .build();

    when(bloodTestRepository.findBloodTestById(bloodTestingRuleBackingForm.getBloodTest().getId())).thenReturn(bloodTest);

    // Exercise SUT
    BloodTestingRule returnedEntity = bloodTestingRuleFactory.createEntity(bloodTestingRuleBackingForm);
    // Verify
    assertThat(returnedEntity, hasSameStateAsBloodTestingRule(expectedEntity));

  }

  @Test
  public void testCreateFullViewModel_shouldReturnFullViewModelWithTheCorrectState() {
    // Set up fixture
    BloodTest bloodTest = aBloodTest().withId(1L).withTestNameShort("Rh").withTestName("Rh").build();
    BloodTestingRule bloodTestingRule = aBloodTestingRule()
        .withId(1L)
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .withPendingTestsIds("1L")
        .withBloodTest(bloodTest)
        .build();

    // Set up expectations
    BloodTestFullViewModel bloodTestFullViewModel = aBloodTestFullViewModel().withId(1L).withTestNameShort("Rh").build();
    BloodTestingRuleFullViewModel expectedFullViewModel = aBloodTestingRuleFullViewModel()
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
    BloodTest bloodTest = aBloodTest().withId(1L).withTestNameShort("Rh").withTestName("Rh").build();
    BloodTestFullViewModel bloodTestFullViewModel = aBloodTestFullViewModel().withId(1L).withTestNameShort("Rh").build();
    List<BloodTestingRule> bloodTestingRules = Arrays.asList(
        aBloodTestingRule()
            .withId(1L)
            .withCategory(BloodTestCategory.BLOODTYPING)
            .withDonationFieldChanged(DonationField.BLOODRH)
            .withNewInformation("+")
            .withPattern("POS")
            .withPendingTestsIds("1L")
            .withBloodTest(bloodTest)
            .build(),
        aBloodTestingRule()
            .withId(2L)
            .withCategory(BloodTestCategory.BLOODTYPING)
            .withDonationFieldChanged(DonationField.BLOODRH)
            .withNewInformation("+")
            .withPattern("NEG")
            .withPendingTestsIds("2L")
            .withBloodTest(bloodTest)
            .build());

    // Set up expectations
    List<BloodTestingRuleFullViewModel> expectedFullViewModels = Arrays.asList(
        aBloodTestingRuleFullViewModel()
            .withId(1L)
            .withTestNameShort("Rh")
            .withBloodTestCategory(BloodTestCategory.BLOODTYPING)
            .withDonationFieldChanged(DonationField.BLOODRH)
            .withNewInformation("+")
            .withPattern("POS")
            .withPendingTestIds(new HashSet<String>(Arrays.asList("1L")))
            .withBloodTest(bloodTestFullViewModel)
            .build(),
        aBloodTestingRuleFullViewModel()
            .withId(2L)
            .withTestNameShort("Rh")
            .withBloodTestCategory(BloodTestCategory.BLOODTYPING)
            .withDonationFieldChanged(DonationField.BLOODRH)
            .withNewInformation("+")
            .withPattern("NEG")
            .withPendingTestIds(new HashSet<String>(Arrays.asList("2L")))
            .withBloodTest(bloodTestFullViewModel)
            .build());

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
