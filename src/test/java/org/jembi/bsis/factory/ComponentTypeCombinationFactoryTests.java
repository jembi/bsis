package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentTypeBackingFormBuilder.aComponentTypeBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBackingFormBuilder.aComponentTypeCombinationBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationFullViewModelBuilder.aComponentTypeCombinationFullViewModel;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationViewModelBuilder.aComponentTypeCombinationViewModel;
import static org.jembi.bsis.helpers.builders.ComponentTypeViewModelBuilder.aComponentTypeViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentTypeCombinationFullViewModelMatcher.hasSameStateAsComponentTypeCombinationFullViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentTypeCombinationMatcher.hasSameStateAsComponentTypeCombination;
import static org.jembi.bsis.helpers.matchers.ComponentTypeCombinationViewModelMatcher.hasSameStateAsComponentTypeCombinationViewModel;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
  
  private static final UUID COMPONENT_TYPE_COMBINATION_ID_1 = UUID.randomUUID();
  private static final UUID COMPONENT_TYPE_COMBINATION_ID_2 = UUID.randomUUID();

  @Test
  public void testCreateComponentTypeCombinationViewModel_shouldReturnCorrectViewModel() {
    
    ComponentTypeCombination combination = aComponentTypeCombination()
        .withId(COMPONENT_TYPE_COMBINATION_ID_1)
        .withCombinationName("combination")
        .build();

    ComponentTypeCombinationViewModel expectedCombinationViewModel = aComponentTypeCombinationViewModel()
        .withId(COMPONENT_TYPE_COMBINATION_ID_1)
        .withCombinationName("combination")
        .build();

    ComponentTypeCombinationViewModel viewModel = componentTypeCombinationFactory.createViewModel(combination);
    
    assertThat("Correct view model", viewModel,
        hasSameStateAsComponentTypeCombinationViewModel(expectedCombinationViewModel));
  }

  @Test
  public void testCreateComponentTypeCombinationViewModels_shouldReturnCorrectViewModels() {

    ComponentTypeCombination combination1 = aComponentTypeCombination()
        .withId(COMPONENT_TYPE_COMBINATION_ID_1)
        .withCombinationName("combination1")
        .build();

    ComponentTypeCombination combination2 = aComponentTypeCombination()
        .withId(COMPONENT_TYPE_COMBINATION_ID_2)
        .withCombinationName("combination2")
        .thatIsDeleted()
        .build();
    
    ComponentTypeCombinationViewModel expectedCombinationViewModel1 = aComponentTypeCombinationViewModel()
        .withId(COMPONENT_TYPE_COMBINATION_ID_1)
        .withCombinationName("combination1")
        .build();
    
    ComponentTypeCombinationViewModel expectedCombinationViewModel2 = aComponentTypeCombinationViewModel()
        .withId(COMPONENT_TYPE_COMBINATION_ID_2)
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
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentTypeId2 = UUID.randomUUID();
    ComponentType producedComponentType = aComponentType()
        .withId(componentTypeId1)
        .build();
    ComponentType sourceComponentType = aComponentType()
        .withId(componentTypeId2)
        .build();

    ComponentTypeCombination expectedComponentTypeCombination = aComponentTypeCombination()
        .withCombinationName("Whole Blood")
        .withComponentTypes(Arrays.asList(producedComponentType))
        .withSourceComponents(new HashSet<>(Arrays.asList(sourceComponentType)))
        .withId(COMPONENT_TYPE_COMBINATION_ID_1)
        .build();

    //the Source Component Type will be updated by the createEntity operation
    Set<ComponentType> sourceComponentTypes = new HashSet<ComponentType>();
    ComponentType expectedSourceComponentType = aComponentType()
        .withId(componentTypeId2)
        .withProducedComponentTypeCombination(expectedComponentTypeCombination)
        .build();
    sourceComponentTypes.add(expectedSourceComponentType);
    expectedComponentTypeCombination.setSourceComponentTypes(sourceComponentTypes);
    
    ComponentTypeCombinationBackingForm backingForm = aComponentTypeCombinationBackingForm()
        .withCombinationName("Whole Blood")
        .withComponentTypes(Arrays.asList(aComponentTypeBackingForm()
        .withId(producedComponentType.getId()).build()))
        .withSourceComponentTypes(new HashSet<>(Arrays.asList(aComponentTypeBackingForm()
        .withId(sourceComponentType.getId()).build())))
        .withId(COMPONENT_TYPE_COMBINATION_ID_1)
        .build();

    // Setup mock
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(producedComponentType);
    when(componentTypeRepository.getComponentTypeById(componentTypeId2)).thenReturn(sourceComponentType);

    ComponentTypeCombination convertedComponentTypeCombinationEntity =
        componentTypeCombinationFactory.createEntity(backingForm);

    assertThat(convertedComponentTypeCombinationEntity,
        hasSameStateAsComponentTypeCombination(expectedComponentTypeCombination));
    
    // ensure that the Component Type has also been updated to reference the new Combination
    Set<ComponentType> componentTypes = convertedComponentTypeCombinationEntity.getSourceComponentTypes();
    
    ComponentTypeCombination actualComponentTypeCombination =
        componentTypes.iterator().next().getProducedComponentTypeCombinations().iterator().next();
    assertThat(actualComponentTypeCombination,
        hasSameStateAsComponentTypeCombination(expectedComponentTypeCombination));
  }
  
  @Test
  public void testConvertComponentTypeCombinationBackingFormWithTwoSourceComponentsToComponentTypeCombinationEntity_shouldReturnExpectedEntity() {
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentTypeId2 = UUID.randomUUID();
    UUID componentTypeId3 = UUID.randomUUID();
    ComponentType producedComponentType = aComponentType()
        .withId(componentTypeId1)
        .build();
    ComponentType sourceComponentType = aComponentType()
        .withId(componentTypeId2)
        .build();
    ComponentType sourceComponentType2 = aComponentType()
        .withId(componentTypeId3)
        .build();

    ComponentTypeCombination expectedComponentTypeCombination = aComponentTypeCombination()
        .withCombinationName("Whole Blood")
        .withComponentTypes(Arrays.asList(producedComponentType))
        .withSourceComponents(new HashSet<>(Arrays.asList(sourceComponentType, sourceComponentType2)))
        .withId(COMPONENT_TYPE_COMBINATION_ID_1)
        .build();

    //the Source Component Type will be updated by the createEntity operation
    Set<ComponentType> sourceComponentTypes = new HashSet<ComponentType>();
    ComponentType expectedSourceComponentType = aComponentType()
        .withId(componentTypeId2)
        .withProducedComponentTypeCombination(expectedComponentTypeCombination)
        .build();
    ComponentType expectedSourceComponentType2 = aComponentType()
        .withId(componentTypeId3)
        .withProducedComponentTypeCombination(expectedComponentTypeCombination)
        .build();
    sourceComponentTypes.add(expectedSourceComponentType);
    sourceComponentTypes.add(expectedSourceComponentType2);
    expectedComponentTypeCombination.setSourceComponentTypes(sourceComponentTypes);
    
    ComponentTypeCombinationBackingForm backingForm = aComponentTypeCombinationBackingForm()
        .withCombinationName("Whole Blood")
        .withComponentTypes(Arrays.asList(aComponentTypeBackingForm()
        .withId(producedComponentType.getId()).build()))
        .withSourceComponentTypes(new HashSet<>(Arrays.asList(
            aComponentTypeBackingForm().withId(sourceComponentType.getId()).build(),
            aComponentTypeBackingForm().withId(sourceComponentType2.getId()).build())))
        .withId(COMPONENT_TYPE_COMBINATION_ID_1)
        .build();

    // Setup mock
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(producedComponentType);
    when(componentTypeRepository.getComponentTypeById(componentTypeId2)).thenReturn(sourceComponentType);
    when(componentTypeRepository.getComponentTypeById(componentTypeId3)).thenReturn(sourceComponentType2);

    ComponentTypeCombination convertedComponentTypeCombinationEntity =
        componentTypeCombinationFactory.createEntity(backingForm);

    assertThat(convertedComponentTypeCombinationEntity,
        hasSameStateAsComponentTypeCombination(expectedComponentTypeCombination));
    
    // ensure that the Component Type has also been updated to reference the new Combination
    Set<ComponentType> componentTypes = convertedComponentTypeCombinationEntity.getSourceComponentTypes();

    // note ordering does not matter since we expect both to have the same componentTypeCombination.
    Iterator<ComponentType> iterator = componentTypes.iterator();
    ComponentTypeCombination actualComponentTypeCombination =
        iterator.next().getProducedComponentTypeCombinations().iterator().next();
    assertThat(actualComponentTypeCombination,
        hasSameStateAsComponentTypeCombination(expectedComponentTypeCombination));

    ComponentTypeCombination actualComponentTypeCombination2 =
        iterator.next().getProducedComponentTypeCombinations().iterator().next();
    assertThat(actualComponentTypeCombination2,
        hasSameStateAsComponentTypeCombination(expectedComponentTypeCombination));
  }
  
  @Test
  public void testConvertComponentTypeCombinationBackingFormWithTwoProducedComponentsToComponentTypeCombinationEntity_shouldReturnExpectedEntity() {
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentTypeId2 = UUID.randomUUID();
    UUID componentTypeId3 = UUID.randomUUID();
    ComponentType producedComponentType = aComponentType()
        .withId(componentTypeId1)
        .build();
    ComponentType producedComponentType2 = aComponentType()
        .withId(componentTypeId2)
        .build();
    ComponentType sourceComponentType = aComponentType()
        .withId(componentTypeId3)
        .build();

    ComponentTypeCombination expectedComponentTypeCombination = aComponentTypeCombination()
        .withCombinationName("Whole Blood")
        .withComponentTypes(Arrays.asList(producedComponentType, producedComponentType2))
        .withSourceComponents(new HashSet<>(Arrays.asList(sourceComponentType)))
        .withId(COMPONENT_TYPE_COMBINATION_ID_1)
        .build();

    //the Source Component Type will be updated by the createEntity operation
    Set<ComponentType> sourceComponentTypes = new HashSet<ComponentType>();
    ComponentType expectedSourceComponentType = aComponentType()
        .withId(componentTypeId3)
        .withProducedComponentTypeCombination(expectedComponentTypeCombination)
        .build();
    sourceComponentTypes.add(expectedSourceComponentType);
    expectedComponentTypeCombination.setSourceComponentTypes(sourceComponentTypes);
    
    ComponentTypeCombinationBackingForm backingForm = aComponentTypeCombinationBackingForm()
        .withCombinationName("Whole Blood")
        .withComponentTypes(Arrays.asList(
            aComponentTypeBackingForm().withId(producedComponentType.getId()).build(),
            aComponentTypeBackingForm().withId(producedComponentType2.getId()).build()))
        .withSourceComponentTypes(new HashSet<>(Arrays.asList(
            aComponentTypeBackingForm().withId(sourceComponentType.getId()).build())))
        .withId(COMPONENT_TYPE_COMBINATION_ID_1)
        .build();

    // Setup mock
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(producedComponentType);
    when(componentTypeRepository.getComponentTypeById(componentTypeId2)).thenReturn(producedComponentType2);
    when(componentTypeRepository.getComponentTypeById(componentTypeId3)).thenReturn(sourceComponentType);

    ComponentTypeCombination convertedComponentTypeCombinationEntity =
        componentTypeCombinationFactory.createEntity(backingForm);

    assertThat(convertedComponentTypeCombinationEntity,
        hasSameStateAsComponentTypeCombination(expectedComponentTypeCombination));
    
    // ensure that the Component Type has also been updated to reference the new Combination
    Set<ComponentType> componentTypes = convertedComponentTypeCombinationEntity.getSourceComponentTypes();

    // note ordering does not matter since we expect both to have the same componentTypeCombination.
    Iterator<ComponentType> iterator = componentTypes.iterator();
    ComponentTypeCombination actualComponentTypeCombination =
        iterator.next().getProducedComponentTypeCombinations().iterator().next();
    assertThat(actualComponentTypeCombination,
        hasSameStateAsComponentTypeCombination(expectedComponentTypeCombination));
  }

  @Test
  public void testCreateComponentTypeCombinationFullViewModel_shouldReturnCorrectViewModel() {
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentTypeId2 = UUID.randomUUID();
    UUID componentTypeId3 = UUID.randomUUID();
    List<ComponentType> sourceComponentTypes = Arrays.asList(
        aComponentType().withId(componentTypeId1).build()
    );
    List<ComponentType> producedComponentTypes = Arrays.asList(
        aComponentType().withId(componentTypeId2).build(), aComponentType().withId(componentTypeId2).build(),
        aComponentType().withId(componentTypeId3).build()
    );
    ComponentTypeCombination combination = aComponentTypeCombination()
        .withId(COMPONENT_TYPE_COMBINATION_ID_2)
        .withCombinationName("combination")
        .withSourceComponentType(sourceComponentTypes.get(0))
        .withComponentTypes(producedComponentTypes)
        .build();

    List<ComponentTypeViewModel> sourceComponentTypeViewModels = Arrays.asList(
        aComponentTypeViewModel().withId(componentTypeId1).build()
    );
    List<ComponentTypeViewModel> producedComponentTypeViewModels = Arrays.asList(
        aComponentTypeViewModel().withId(componentTypeId2).build(),
        aComponentTypeViewModel().withId(componentTypeId2).build(),
        aComponentTypeViewModel().withId(componentTypeId3).build()
    );

    ComponentTypeCombinationFullViewModel expectedCombinationViewModel = aComponentTypeCombinationFullViewModel()
        .withId(COMPONENT_TYPE_COMBINATION_ID_2)
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
