package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBackingFormBuilder.aBloodTestBackingForm;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestFullViewModelBuilder.aBloodTestFullViewModel;
import static org.jembi.bsis.helpers.builders.BloodTestViewModelBuilder.aBloodTestViewModel;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleBackingFormBuilder.aBloodTestingRuleBackingForm;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleBuilder.aBloodTestingRule;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleFullViewModelBuilder.aBloodTestingRuleFullViewModel;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleViewModelBuilder.aBloodTestingRuleViewModel;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleFullViewModelMatcher.hasSameStateAsBloodTestingRuleFullViewModel;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleMatcher.hasSameStateAsBloodTestingRule;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleViewModelMatcher.hasSameStateAsBloodTestingRuleViewModel;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.backingform.BloodTestingRuleBackingForm;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestViewModel;
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
    UUID bloodTestingRuleId = UUID.randomUUID();

    BloodTest bloodTest = aBloodTest()
        .withTestNameShort("Rh")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .build();
    
    BloodTestingRule bloodTestingRule = aBloodTestingRule()
        .withId(bloodTestingRuleId)
        .withBloodTest(bloodTest)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .build();

    // Set up expectations
    BloodTestingRuleViewModel expectedViewModel = aBloodTestingRuleViewModel()
        .withId(bloodTestingRuleId)
        .withTestNameShort("Rh")
        .withBloodTestCategory(bloodTest.getCategory())
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
    UUID bloodTestingRuleId1 = UUID.randomUUID();
    UUID bloodTestingRuleId2 = UUID.randomUUID();

    List<BloodTestingRule> bloodTestingRules = Arrays.asList(
        aBloodTestingRule().withId(bloodTestingRuleId1).build(),
        aBloodTestingRule().withId(bloodTestingRuleId2).build());

    // Set up expectations
    List<BloodTestingRuleViewModel> expectedViewModels = Arrays.asList(
        aBloodTestingRuleViewModel()
            .withId(bloodTestingRuleId1)
            .withTestNameShort(bloodTestingRules.get(0).getBloodTest().getTestNameShort())
            .build(),
        aBloodTestingRuleViewModel()
            .withId(bloodTestingRuleId2)
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
  public void testCreateEntity_shouldReturnExpectedEntity() {
    // Set up fixture
    UUID bloodTestingRuleId = UUID.randomUUID();

    BloodTest bloodTest = aBloodTest()
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withTestNameShort("Rh")
        .withId(UUID.randomUUID())
        .build();

    BloodTest pendingBloodTest1 = aBloodTest()
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withTestNameShort("Rh Repeat 1")
        .withId(UUID.randomUUID())
        .build();

    BloodTest pendingBloodTest2 = aBloodTest()
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withTestNameShort("Rh Repeat 2")
        .withId(UUID.randomUUID())
        .build();
    
    BloodTestBackingForm bloodTestBackingForm = aBloodTestBackingForm()
        .withTestNameShort(bloodTest.getTestNameShort())
        .withCategory(bloodTest.getCategory())
        .withId(bloodTest.getId())
        .build();

    BloodTestBackingForm pendingBloodTest1BackingForm = aBloodTestBackingForm()
        .withTestNameShort(pendingBloodTest1.getTestNameShort())
        .withCategory(pendingBloodTest1.getCategory())
        .withId(pendingBloodTest1.getId())
        .build();

    BloodTestBackingForm pendingBloodTest2BackingForm = aBloodTestBackingForm()
        .withTestNameShort(pendingBloodTest2.getTestNameShort())
        .withCategory(pendingBloodTest2.getCategory())
        .withId(pendingBloodTest2.getId())
        .build();
    
    BloodTestingRuleBackingForm bloodTestingRuleBackingForm = aBloodTestingRuleBackingForm()
        .withId(bloodTestingRuleId)
        .withBloodTest(bloodTestBackingForm)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withPendingTests(new HashSet<>(Arrays.asList(pendingBloodTest1BackingForm, pendingBloodTest2BackingForm)))
        .withNewInformation("+")
        .withPattern("POS")
        .build();

    BloodTestingRule expectedEntity = aBloodTestingRule()
        .withId(bloodTestingRuleId)
        .withBloodTest(bloodTest)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withPendingBloodTest(pendingBloodTest1)
        .withPendingBloodTest(pendingBloodTest2)
        .withNewInformation("+")
        .withPattern("POS")
        .build();

    when(bloodTestRepository.findBloodTestById(bloodTestingRuleBackingForm.getBloodTest().getId())).thenReturn(bloodTest);
    when(bloodTestRepository.findBloodTestById(pendingBloodTest1BackingForm.getId())).thenReturn(pendingBloodTest1);
    when(bloodTestRepository.findBloodTestById(pendingBloodTest2BackingForm.getId())).thenReturn(pendingBloodTest2);

    // Exercise SUT
    BloodTestingRule returnedEntity = bloodTestingRuleFactory.createEntity(bloodTestingRuleBackingForm);
    // Verify
    assertThat(returnedEntity, hasSameStateAsBloodTestingRule(expectedEntity));

  }

  @Test
  public void testCreateFullViewModel_shouldReturnFullViewModelWithTheCorrectState() {
    // Set up fixture
    UUID bloodTestId = UUID.randomUUID();
    UUID pendingBloodTestId = UUID.randomUUID();
    UUID bloodTestingRuleId = UUID.randomUUID();

    BloodTest bloodTest = aBloodTest()
        .withId(bloodTestId) 
        .withTestNameShort("Rh")
        .withTestName("Rh")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .build();

    BloodTest pendingBloodTest = aBloodTest()
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withTestNameShort("Rh Repeat 1")
        .withId(pendingBloodTestId)
        .build();
    
    BloodTestingRule bloodTestingRule = aBloodTestingRule()
        .withId(bloodTestingRuleId)
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .withPendingBloodTest(pendingBloodTest)
        .withBloodTest(bloodTest)
        .build();

    // Set up expectations
    BloodTestFullViewModel bloodTestFullViewModel = aBloodTestFullViewModel()
        .withId(bloodTestId) 
        .withTestNameShort("Rh")
        .withCategory(bloodTest.getCategory())
        .build();

    BloodTestViewModel pendingBloodTestViewModel = aBloodTestViewModel()
        .withId(pendingBloodTestId) 
        .withTestNameShort("Repeat Rh")
        .withCategory(bloodTest.getCategory())
        .build();
    
    BloodTestingRuleFullViewModel expectedFullViewModel = aBloodTestingRuleFullViewModel()
        .withId(bloodTestingRuleId)
        .withTestNameShort("Rh")
        .withDonationFieldChanged(DonationField.BLOODRH)
        .withNewInformation("+")
        .withPattern("POS")
        .withPendingTests(new HashSet<BloodTestViewModel>(Arrays.asList(pendingBloodTestViewModel)))
        .withBloodTest(bloodTestFullViewModel)
        .withBloodTestCategory(bloodTest.getCategory())
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
    UUID bloodTestId = UUID.randomUUID();
    UUID pendingBloodTest1Id = UUID.randomUUID();
    UUID pendingBloodTest2Id = UUID.randomUUID();
    UUID bloodTestingRuleId1 = UUID.randomUUID();
    UUID bloodTestingRuleId2 = UUID.randomUUID();

    BloodTest bloodTest = aBloodTest()
        .withId(bloodTestId)  
        .withTestNameShort("Rh")
        .withTestName("Rh")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .build();

    BloodTest pendingBloodTest1 = aBloodTest()
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withTestNameShort("Rh Repeat 1")
        .withId(pendingBloodTest1Id)
        .build();

    BloodTest pendingBloodTest2 = aBloodTest()
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withTestNameShort("Rh Repeat 2")
        .withId(pendingBloodTest2Id)
        .build();
    
    BloodTestFullViewModel bloodTestFullViewModel = aBloodTestFullViewModel()
        .withId(bloodTestId)
        .withCategory(bloodTest.getCategory())
        .withTestNameShort("Rh")
        .build();

    BloodTestViewModel bloodTestViewModel1 = aBloodTestViewModel()
        .withId(pendingBloodTest1Id)
        .withCategory(bloodTest.getCategory())
        .withTestNameShort("Repeat Rh")
        .build();

    BloodTestViewModel bloodTestViewModel2 = aBloodTestViewModel()
        .withId(pendingBloodTest2Id)
        .withCategory(bloodTest.getCategory())
        .withTestNameShort("Repeat again Rh")
        .build();
    
    List<BloodTestingRule> bloodTestingRules = Arrays.asList(
        aBloodTestingRule()
            .withId(bloodTestingRuleId1)
            .withDonationFieldChanged(DonationField.BLOODRH)
            .withNewInformation("+")
            .withPattern("POS")
            .withPendingBloodTest(pendingBloodTest1)
            .withBloodTest(bloodTest)
            .build(),
        aBloodTestingRule()
            .withId(bloodTestingRuleId2)
            .withDonationFieldChanged(DonationField.BLOODRH)
            .withNewInformation("+")
            .withPattern("NEG")
            .withPendingBloodTest(pendingBloodTest2)
            .withBloodTest(bloodTest)
            .build());

    // Set up expectations
    List<BloodTestingRuleFullViewModel> expectedFullViewModels = Arrays.asList(
        aBloodTestingRuleFullViewModel()
            .withId(bloodTestingRuleId1)
            .withTestNameShort("Rh")
            .withDonationFieldChanged(DonationField.BLOODRH)
            .withNewInformation("+")
            .withPattern("POS")
            .withPendingTests(new HashSet<BloodTestViewModel>(Arrays.asList(bloodTestViewModel1)))
            .withBloodTest(bloodTestFullViewModel)
            .withBloodTestCategory(bloodTest.getCategory())
            .build(),
        aBloodTestingRuleFullViewModel()
            .withId(bloodTestingRuleId2)
            .withTestNameShort("Rh")
            .withDonationFieldChanged(DonationField.BLOODRH)
            .withNewInformation("+")
            .withPattern("NEG")
            .withPendingTests(new HashSet<BloodTestViewModel>(Arrays.asList(bloodTestViewModel2)))
            .withBloodTest(bloodTestFullViewModel)
            .withBloodTestCategory(bloodTest.getCategory())
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
