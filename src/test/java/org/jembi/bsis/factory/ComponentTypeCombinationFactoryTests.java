package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBackingFormBuilder.aComponentTypeCombinationBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentTypeBackingFormBuilder.aComponentTypeBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationFullViewModelBuilder.aComponentTypeCombinationFullViewModel;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationViewModelBuilder.aComponentTypeCombinationViewModel;
import static org.jembi.bsis.helpers.builders.ComponentTypeViewModelBuilder.aComponentTypeViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentTypeCombinationFullViewModelMatcher.hasSameStateAsComponentTypeCombinationFullViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentTypeCombinationViewModelMatcher.hasSameStateAsComponentTypeCombinationViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentTypeCombinationMatcher.hasSameStateAsComponentTypeCombination;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.jembi.bsis.backingform.ComponentTypeCombinationBackingForm;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ComponentTypeCombinationFactoryTests extends UnitTestSuite {

  @InjectMocks
  private ComponentTypeCombinationFactory componentTypeCombinationFactory;
  
  @Mock
  ComponentTypeRepository componentTypeRepository;
  @Mock
  private ComponentTypeFactory componentTypeFactory;

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
  
  @Test
  public void testConvertComponentTypeCombinationBackingFormToComponentTypeCombinationEntity_shouldReturnExpectedEntity() {
    ComponentType producedComponentType = aComponentType()
        .withId(1L)
        .build();
    ComponentType sourceComponentType = aComponentType()
        .withId(2L)
        .build();
    ComponentTypeCombination expectedEntity = aComponentTypeCombination()
        .withCombinationName("Whole Blood")
        .withComponentTypes(Arrays.asList(producedComponentType))
        .withSourceComponents(new HashSet<>(Arrays.asList(sourceComponentType)))
        .withId(1L)
        .build();
    ComponentTypeCombinationBackingForm backingForm = aComponentTypeCombinationBackingForm()
        .withCombinationName("Whole Blood")
        .withComponentTypes(Arrays.asList(aComponentTypeBackingForm()
            .withId(producedComponentType.getId()).build()))
        .withSourceComponentTypes(new HashSet<>(Arrays.asList(aComponentTypeBackingForm()
            .withId(sourceComponentType.getId()).build())))
        .withId(1L)
        .build();
      // Setup mock
      when(componentTypeRepository.getComponentTypeById(1L)).thenReturn(producedComponentType);
      when(componentTypeRepository.getComponentTypeById(2L)).thenReturn(sourceComponentType);

      ComponentTypeCombination convertedEntity = componentTypeCombinationFactory.createEntity(backingForm);

      assertThat(convertedEntity, hasSameStateAsComponentTypeCombination(expectedEntity));
  }   

  @Test
  public void testCreateComponentTypeCombinationFullViewModel_shouldReturnCorrectViewModel() {
    List<ComponentType> sourceComponentTypes = Arrays.asList(
        aComponentType().withId(1L).build()
    );
    List<ComponentType> producedComponentTypes = Arrays.asList(
        aComponentType().withId(2L).build(),
        aComponentType().withId(2L).build(),
        aComponentType().withId(3L).build()
    );
    ComponentTypeCombination combination = aComponentTypeCombination()
        .withId(2L)
        .withCombinationName("combination")
        .withSourceComponentType(sourceComponentTypes.get(0))
        .withComponentTypes(producedComponentTypes)
        .build();

    List<ComponentTypeViewModel> sourceComponentTypeViewModels = Arrays.asList(
        aComponentTypeViewModel().withId(1L).build()
    );
    List<ComponentTypeViewModel> producedComponentTypeViewModels = Arrays.asList(
        aComponentTypeViewModel().withId(2L).build(),
        aComponentTypeViewModel().withId(2L).build(),
        aComponentTypeViewModel().withId(3L).build()
    );

    ComponentTypeCombinationFullViewModel expectedCombinationViewModel = aComponentTypeCombinationFullViewModel()
        .withId(2L)
        .withCombinationName("combination")
        .withSourceComponentType(sourceComponentTypeViewModels.get(0))
        .withComponentTypes(producedComponentTypeViewModels)
        .build();
    
    when(componentTypeFactory.createViewModels(sourceComponentTypes)).thenReturn(sourceComponentTypeViewModels);
    when(componentTypeFactory.createViewModels(producedComponentTypes)).thenReturn(producedComponentTypeViewModels);
    
    ComponentTypeCombinationFullViewModel viewModel = componentTypeCombinationFactory.createFullViewModel(combination);
    
    assertThat("Correct full view model", viewModel, hasSameStateAsComponentTypeCombinationFullViewModel(expectedCombinationViewModel));
  }
}
