package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationViewModelBuilder.aComponentTypeCombinationViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentTypeCombinationViewModelMatcher.hasSameStateAsComponentTypeCombinationViewModel;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;

public class ComponentTypeCombinationFactoryTests extends UnitTestSuite {

  @InjectMocks
  private ComponentTypeCombinationFactory componentTypeCombinationFactory;

  @Test
  public void testCreateComponentTypeCombinationViewModel_shouldReturnCorrectViewModel() {
    ComponentTypeCombination combination = aComponentTypeCombination()
        .withId(1L)
        .withCombinationName("combination")
        .build();

    ComponentTypeCombinationViewModel expectedCombinationViewModel = aComponentTypeCombinationViewModel()
        .withId(1L)
        .withCombinationName("combination")
        .build();

    ComponentTypeCombinationViewModel viewModel = componentTypeCombinationFactory.createViewModel(combination);
    
    assertThat("Correct view model", viewModel,
        hasSameStateAsComponentTypeCombinationViewModel(expectedCombinationViewModel));
  }

  @Test
  public void testCreateComponentTypeCombinationViewModels_shouldReturnCorrectViewModels() {

    ComponentTypeCombination combination1 = aComponentTypeCombination()
        .withId(1L)
        .withCombinationName("combination1")
        .build();

    ComponentTypeCombination combination2 = aComponentTypeCombination()
        .withId(2L)
        .withCombinationName("combination2")
        .thatIsDeleted()
        .build();
    
    ComponentTypeCombinationViewModel expectedCombinationViewModel1 = aComponentTypeCombinationViewModel()
        .withId(1L)
        .withCombinationName("combination1")
        .build();
    
    ComponentTypeCombinationViewModel expectedCombinationViewModel2 = aComponentTypeCombinationViewModel()
        .withId(2L)
        .withCombinationName("combination2")
        .thatIsDeleted()
        .build();

    List<ComponentTypeCombinationViewModel> viewModels =
        componentTypeCombinationFactory.createViewModels(Arrays.asList(combination1, combination2));
    
    assertThat("Correct view model 1", viewModels.get(0),
        hasSameStateAsComponentTypeCombinationViewModel(expectedCombinationViewModel1));
    assertThat("Correct view model 2", viewModels.get(1),
        hasSameStateAsComponentTypeCombinationViewModel(expectedCombinationViewModel2));
    
  }
  
  @Test
  public void testCreateComponentTypeCombinationViewModelWithNullArgument_shouldReturnEmptyViewModel() {
    ComponentTypeCombinationViewModel viewModel = componentTypeCombinationFactory.createViewModel(null);
    assertThat("Empty view model", viewModel, is(new ComponentTypeCombinationViewModel()));
  }

  @Test
  public void testCreateComponentTypeCombinationViewModelsWithNullArgument_shouldReturnEmptyViewModels() {
    List<ComponentTypeCombinationViewModel> viewModels = componentTypeCombinationFactory.createViewModels(null);
    assertThat(viewModels.size(), is(equalTo(0)));
  }

}
