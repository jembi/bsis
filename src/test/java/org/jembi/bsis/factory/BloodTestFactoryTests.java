package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.matchers.BloodTestFullViewModelMatcher.hasSameStateAsBloodTestFullViewModel;
import static org.jembi.bsis.helpers.matchers.BloodTestMatcher.hasSameStateAsBloodTest;
import static org.jembi.bsis.helpers.matchers.BloodTestViewModelMatcher.hasSameStateAsBloodTestViewModel;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.helpers.builders.BloodTestBackingFormBuilder;
import org.jembi.bsis.helpers.builders.BloodTestBuilder;
import org.jembi.bsis.helpers.builders.BloodTestFullViewModelBuilder;
import org.jembi.bsis.helpers.builders.BloodTestViewModelBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

public class BloodTestFactoryTests extends UnitTestSuite {
  
  @Spy
  @InjectMocks
  private BloodTestFactory bloodTestFactory;
  
  @Test
  public void testCreateFullViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    UUID bloodTestId = UUID.randomUUID();
    BloodTest bloodTest = BloodTestBuilder.aBasicBloodTypingBloodTest().withId(bloodTestId).withTestName("ABC test")
        .withTestNameShort("ABC").withValidResults("A,B,C,D").withPositiveResults("A,B,C").withNegativeResults("D")
        .withRankInCategory(1).thatShouldFlagComponentsContainingPlasmaForDiscard()
        .thatShouldFlagComponentsForDiscard().build();

    // Set up expectations
    BloodTestFullViewModel expectedViewModel = BloodTestFullViewModelBuilder.aBasicBloodTypingBloodTestFullViewModel()
        .withId(bloodTestId).withTestName("ABC test").withTestNameShort("ABC").withValidResult("A").withValidResult("B")
        .withValidResult("C").withValidResult("D").withPositiveResult("A").withPositiveResult("B")
        .withPositiveResult("C").withNegativeResult("D").withRankInCategory(1)
        .thatShouldFlagComponentsContainingPlasmaForDiscard().thatShouldFlagComponentsForDiscard().build();

    // Exercise SUT
    BloodTestFullViewModel returnedViewModel = bloodTestFactory.createFullViewModel(bloodTest);

    // Verify
    assertThat(returnedViewModel, hasSameStateAsBloodTestFullViewModel(expectedViewModel));
  }

  @Test
  public void testCreateFullViewModels_shouldReturnViewModelsWithTheCorrectState() {
    // Set up fixture
    UUID bloodTest1Id = UUID.randomUUID();
    UUID bloodTest2Id = UUID.randomUUID();
    List<BloodTest> bloodTests = Arrays.asList(
        BloodTestBuilder.aBasicBloodTypingBloodTest().withId(bloodTest1Id).withTestName("test1").withTestNameShort("t").build(),
        BloodTestBuilder.aBasicBloodTypingBloodTest().withId(bloodTest2Id).withTestName("test2").withTestNameShort("t").build());

    // Set up expectations
    List<BloodTestFullViewModel> expectedViewModels =
        Arrays.asList(
            BloodTestFullViewModelBuilder.aBasicBloodTypingBloodTestFullViewModel().withId(bloodTest1Id).withTestName("test1")
                .withTestNameShort("t").build(),
            BloodTestFullViewModelBuilder
                .aBasicBloodTypingBloodTestFullViewModel().withId(bloodTest2Id).withTestName("test2").withTestNameShort("t")
                .build());

    // Exercise SUT
    List<BloodTestFullViewModel> returnedViewModels = bloodTestFactory.createFullViewModels(bloodTests);

    // Verify
    assertThat("Correct number of view models returned", returnedViewModels.size(), is(2));
    assertThat(returnedViewModels.get(0), hasSameStateAsBloodTestFullViewModel(expectedViewModels.get(0)));
    assertThat(returnedViewModels.get(1), hasSameStateAsBloodTestFullViewModel(expectedViewModels.get(1)));
  }

  @Test
  public void testCreateViewModel_shouldReturnViewModelWithTheCorrectState() {
    // Set up fixture
    UUID bloodTestId = UUID.randomUUID();
    BloodTest bloodTest = BloodTestBuilder.aBasicBloodTypingBloodTest()
        .withId(bloodTestId)
        .withTestName("ABC test")
        .withTestNameShort("ABC")
        .withValidResults("A,B,C,D")
        .withPositiveResults("A,B,C")
        .withNegativeResults("D")
        .withRankInCategory(1)
        .build();
    
    // Set up expectations
    BloodTestViewModel expectedViewModel = BloodTestViewModelBuilder.aBasicBloodTypingBloodTestViewModel()
        .withId(bloodTestId)
        .withTestNameShort("ABC")
        .withRankInCategory(1)
        .build();
    
    // Exercise SUT
    BloodTestViewModel returnedViewModel = bloodTestFactory.createViewModel(bloodTest);
    
    // Verify
    assertThat(returnedViewModel, hasSameStateAsBloodTestViewModel(expectedViewModel));
  }

  @Test
  public void testCreateViewModels_shouldReturnFullViewModelsWithTheCorrectState() {
    // Set up fixture
    UUID bloodTest1Id = UUID.randomUUID();
    UUID bloodTest2Id = UUID.randomUUID();
    List<BloodTest> bloodTests = Arrays.asList(
        BloodTestBuilder.aBasicBloodTypingBloodTest().withId(bloodTest1Id).withTestNameShort("t").build(), 
        BloodTestBuilder.aBasicBloodTypingBloodTest().withId(bloodTest2Id).withTestNameShort("t").build());

    // Set up expectations
    List<BloodTestViewModel> expectedViewModels = Arrays.asList(
        BloodTestViewModelBuilder.aBasicBloodTypingBloodTestViewModel().withTestNameShort("t").withId(bloodTest1Id).build(),
        BloodTestViewModelBuilder.aBasicBloodTypingBloodTestViewModel().withTestNameShort("t").withId(bloodTest2Id).build());

    // Exercise SUT
    List<BloodTestViewModel> returnedViewModels = bloodTestFactory.createViewModels(bloodTests);
    
    // Verify
    assertThat("Correct number of view models returned", returnedViewModels.size(), is(2));
    assertThat(returnedViewModels.get(0), hasSameStateAsBloodTestViewModel(expectedViewModels.get(0)));
    assertThat(returnedViewModels.get(1), hasSameStateAsBloodTestViewModel(expectedViewModels.get(1)));
  }
  
  @Test 
  public void testConvertBloodTestBackingFormToBloodTestEntity_shouldReturnExpectedEntity() { 
    // Set up fixture 
    UUID bloodTestId = UUID.randomUUID();
    BloodTestBackingForm bloodTestBackingForm = BloodTestBackingFormBuilder.aBloodTestBackingForm() 
        .withId(bloodTestId) 
        .withTestName("Test Name") 
        .withTestNameShort("Test Name Short") 
        .withCategory(BloodTestCategory.BLOODTYPING) 
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING) 
        .withValidResults(new LinkedHashSet<>(Arrays.asList("POS","NEG","NT"))) 
        .withNegativeResults(new LinkedHashSet<>(Arrays.asList("NEG"))) 
        .withPositiveResults(new LinkedHashSet<>(Arrays.asList("POS"))) 
        .thatIsNotActive() 
        .thatIsDeleted() 
        .thatShouldNotFlagComponentsContainingPlasmaForDiscard() 
        .thatShouldFlagComponentsForDiscard() 
        .withRankInCategory(1)
        .build(); 
     
    BloodTest expectedEntity = BloodTestBuilder.aBloodTest() 
        .withId(bloodTestId) 
        .withTestName("Test Name") 
        .withTestNameShort("Test Name Short") 
        .withCategory(BloodTestCategory.BLOODTYPING) 
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING) 
        .withValidResults("POS,NEG,NT") 
        .withNegativeResults("NEG") 
        .withPositiveResults("POS") 
        .thatIsInActive() 
        .thatIsDeleted() 
        .thatShouldNotFlagComponentsContainingPlasmaForDiscard() 
        .thatShouldFlagComponentsForDiscard() 
        .withRankInCategory(1)
        .build(); 
     
    // Exercise SUT 
    BloodTest returnedEntity = bloodTestFactory.createEntity(bloodTestBackingForm); 
     
    // Verify 
    assertThat(returnedEntity, hasSameStateAsBloodTest(expectedEntity)); 
     
  } 
}
