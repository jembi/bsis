package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.jembi.bsis.helpers.builders.ComponentTypeBackingFormBuilder.aComponentTypeBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationViewModelBuilder.aComponentTypeCombinationViewModel;
import static org.jembi.bsis.helpers.builders.ComponentTypeSearchViewModelBuilder.aComponentTypeSearchViewModel;
import static org.jembi.bsis.helpers.builders.ComponentTypeViewModelBuilder.aComponentTypeViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentTypeMatcher.hasSameStateAsComponentType;
import static org.jembi.bsis.helpers.matchers.ComponentTypeSearchViewModelMatcher.hasSameStateAsComponentTypeSearchViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentTypeViewModelMatcher.hasSameStateAsComponentTypeViewModel;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeSearchViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ComponentTypeFactoryTests extends UnitTestSuite {

  @InjectMocks
  private ComponentTypeFactory componentTypeFactory;
  @Mock
  private ComponentTypeCombinationFactory componentTypeCombinationFactory;
  
  @Test
  public void testCreateEntity_shouldReturnExpectedEntity() {
    // Set up
    UUID componentTypeId = UUID.randomUUID();
    ComponentTypeBackingForm componentTypeBackingForm = aComponentTypeBackingForm()
        .withId(componentTypeId)
        .withComponentTypeName("componentTypeName")
        .withComponentTypeCode("componentTypeCode")
        .withExpiresAfter(20)
        .withMaxBleedTime(3)
        .withMaxTimeSinceDonation(10)
        .withExpiresAfterUnit(ComponentTypeTimeUnits.DAYS)
        .thatHasBloodGroup()
        .withDescription("description")
        .thatIsNotDeleted()
        .withLowStorageTemperature(2)
        .withHighStorageTemperature(5)
        .withLowTransportTemperature(7)
        .withHighTransportTemperature(14)
        .withPreparationInfo("preparationInfo")
        .withTransportInfo("transportInfo")
        .withStorageInfo("storageInfo")
        .withGravity(1.145)
        .thatCanBeIssued()
        .thatContainsPlasma()
        .build();
    
    ComponentType componentType = aComponentType()
        .withId(componentTypeId)
        .withComponentTypeName("componentTypeName")
        .withComponentTypeCode("componentTypeCode")
        .withExpiresAfter(20)
        .withMaxBleedTime(3)
        .withMaxTimeSinceDonation(10)
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .thatHasBloodGroup()
        .withDescription("description")
        .thatIsNotDeleted()
        .withLowStorageTemperature(2)
        .withHighStorageTemperature(5)
        .withLowTransportTemperature(7)
        .withHighTransportTemperature(14)
        .withPreparationInfo("preparationInfo")
        .withTransportInfo("transportInfo")
        .withStorageInfo("storageInfo")
        .withGravity(1.145)
        .thatCanBeIssued()
        .thatContainsPlasma()
        .build();
    
    // Run test
    ComponentType result = componentTypeFactory.createEntity(componentTypeBackingForm);
    
    // Assert
    assertThat("Correct entity", result, hasSameStateAsComponentType(componentType));
  }

  @Test
  public void testSingleFullComponentType_shouldReturnExpectedViewModel() {
    UUID componentTypeCombinationId = UUID.randomUUID();
    ComponentTypeCombination producedComponentTypeCombination = aComponentTypeCombination()
        .withId(componentTypeCombinationId)
        .build();
    UUID componentTypeId = UUID.randomUUID();
    ComponentType entity = ComponentTypeBuilder.aComponentType()
        .withId(componentTypeId)
        .withComponentTypeName("name")
        .withComponentTypeCode("0001")
        .withDescription("descr")
        .withExpiresAfter(90)
        .withHighStorageTemperature(10)
        .withLowStorageTemperature(0)
        .withPreparationInfo("preparationInfo")
        .withProducedComponentTypeCombination(producedComponentTypeCombination)
        .withTransportInfo("transportInfo")
        .withStorageInfo("storageInfo")
        .thatCanNotBeIssued()
        .withMaxBleedTime(10)
        .withGravity(1.145)
        .withMaxTimeSinceDonation(5)
        .build();

    ComponentTypeCombinationViewModel combinationViewModel = aComponentTypeCombinationViewModel().withId(componentTypeCombinationId).build();

    when(componentTypeCombinationFactory.createViewModels(Arrays.asList(producedComponentTypeCombination)))
        .thenReturn(Arrays.asList(combinationViewModel));

    ComponentTypeFullViewModel viewModel = componentTypeFactory.createFullViewModel(entity);
    
    Assert.assertNotNull("View Model was created", viewModel);
    Assert.assertEquals("View Model correct", componentTypeId, viewModel.getId());
    Assert.assertEquals("View Model correct", "name", viewModel.getComponentTypeName());
    Assert.assertEquals("View Model correct", "0001", viewModel.getComponentTypeCode());
    Assert.assertEquals("View Model correct", "descr", viewModel.getDescription());
    Assert.assertEquals("View Model correct", Integer.valueOf(90), viewModel.getExpiresAfter());
    Assert.assertEquals("View Model correct", ComponentTypeTimeUnits.DAYS, viewModel.getExpiresAfterUnits());
    Assert.assertEquals("View Model correct", Integer.valueOf(10), viewModel.getHighStorageTemperature());
    Assert.assertEquals("View Model correct", Integer.valueOf(0), viewModel.getLowStorageTemperature());
    Assert.assertEquals("View Model correct", "preparationInfo", viewModel.getPreparationInfo());
    Assert.assertNotNull("View Model correct", viewModel.getProducedComponentTypeCombinations());
    Assert.assertEquals("View Model correct",  1, viewModel.getProducedComponentTypeCombinations().size());
    Assert.assertEquals("View Model correct", "transportInfo", viewModel.getTransportInfo());
    Assert.assertEquals("View Model correct", "storageInfo", viewModel.getStorageInfo());
    Assert.assertEquals("View Model correct", false, viewModel.getCanBeIssued());
    Assert.assertEquals("View Model correct", Integer.valueOf(5), viewModel.getMaxTimeSinceDonation());
    Assert.assertEquals("View Model correct", Integer.valueOf(10), viewModel.getMaxBleedTime());
    assertThat("View Model correct", Double.valueOf(1.145).equals(viewModel.getGravity()));
  }

  @Test
  public void testCreateFullViewModelWithNewComponentType_shouldReturnExpected() {
    UUID componentTypeId = UUID.randomUUID();
    ComponentType entity = ComponentTypeBuilder.aComponentType()
        .withId(componentTypeId)
        .withComponentTypeName("name")
        .withComponentTypeCode("0001")
        .build();

    ComponentTypeFullViewModel viewModel = componentTypeFactory.createFullViewModel(entity);
    
    Assert.assertNotNull("View Model was created", viewModel);
    Assert.assertNull("No produced components",  viewModel.getProducedComponentTypeCombinations());
  }
  
  @Test
  public void testSingleSearchComponentType_shouldReturnExpectedViewModel() {
    UUID componentTypeId = UUID.randomUUID();
    ComponentType entity = ComponentTypeBuilder.aComponentType()
        .withId(componentTypeId)
        .withComponentTypeName("name")
        .withComponentTypeCode("0001")
        .withDescription("descr")
        .withExpiresAfter(90)
        .thatCanNotBeIssued()
        .build();

    ComponentTypeSearchViewModel viewModel = componentTypeFactory.createSearchViewModel(entity);
    
    Assert.assertNotNull("View Model was created", viewModel);
    Assert.assertEquals("View Model correct", componentTypeId, viewModel.getId());
    Assert.assertEquals("View Model correct", "name", viewModel.getComponentTypeName());
    Assert.assertEquals("View Model correct", "0001", viewModel.getComponentTypeCode());
    Assert.assertEquals("View Model correct", "descr", viewModel.getDescription());
    Assert.assertEquals("View Model correct", Integer.valueOf(90), viewModel.getExpiresAfter());
    Assert.assertEquals("View Model correct", ComponentTypeTimeUnits.DAYS, viewModel.getExpiresAfterUnits());
    Assert.assertEquals("View Model correct", false, viewModel.getCanBeIssued());
    Assert.assertEquals("View Model correct", false, viewModel.getIsDeleted());
  }
  
  @Test
  public void testSingleComponentType_shouldReturnExpectedViewModel() {
    UUID componentTypeId = UUID.randomUUID();
    ComponentType entity = ComponentTypeBuilder.aComponentType()
        .withId(componentTypeId)
        .withComponentTypeName("name")
        .withComponentTypeCode("0001")
        .withDescription("descr")
        .withMaxBleedTime(3)
        .withMaxTimeSinceDonation(3)
        .build();
    
    ComponentTypeViewModel expectedViewModel = aComponentTypeViewModel()
        .withId(componentTypeId)
        .withComponentTypeName("name")
        .withComponentTypeCode("0001")
        .withDescription("descr")
        .withMaxBleedTime(3)
        .withMaxTimeSinceDonation(3)
        .build();

    ComponentTypeViewModel viewModel = componentTypeFactory.createViewModel(entity);
    
    assertThat(viewModel, hasSameStateAsComponentTypeViewModel(expectedViewModel));
  }
  
  @Test
  public void testMultipleComponentType_shouldReturnExpectedEntities() {
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentTypeId2 = UUID.randomUUID();
    ComponentType entity1 =
        ComponentTypeBuilder.aComponentType().withId(componentTypeId1).withComponentTypeName("test1").build();
    ComponentType entity2 =
        ComponentTypeBuilder.aComponentType().withId(componentTypeId2).withComponentTypeName("test2").build();

    List<ComponentTypeViewModel> viewModels = componentTypeFactory.createViewModels(Arrays.asList(entity1, entity2));
    
    Assert.assertNotNull("View Models were created", viewModels);
    Assert.assertEquals("View Models were created", 2, viewModels.size());
    Assert.assertEquals("View Model correct", componentTypeId1, viewModels.get(0).getId());
    Assert.assertEquals("View Model correct", componentTypeId2, viewModels.get(1).getId());
  }
  
  @Test
  public void testMultipleComponentTypeSearch_shouldReturnExpectedViewModels() {
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentTypeId2 = UUID.randomUUID();
    ComponentType entity1 = ComponentTypeBuilder.aComponentType().withId(componentTypeId1).withComponentTypeName("test1").build();
    ComponentType entity2 = ComponentTypeBuilder.aComponentType().withId(componentTypeId2).withComponentTypeName("test2").build();

    List<ComponentTypeSearchViewModel> viewModels = componentTypeFactory.createSearchViewModels(Arrays.asList(entity1, entity2));
    
    Assert.assertNotNull("View Models were created", viewModels);
    Assert.assertEquals("View Models were created", 2, viewModels.size());
    Assert.assertEquals("View Model correct", componentTypeId1, viewModels.get(0).getId());
    Assert.assertEquals("View Model correct", componentTypeId2, viewModels.get(1).getId());
  }
  
  @Test
  public void testComponentTypeSearchViewModelWithContainsPlasma_shouldReturnExpectedViewModel() {
    UUID componentTypeId = UUID.randomUUID();
    ComponentType componentType = aComponentType()
        .withId(componentTypeId)
        .withComponentTypeCode("0000")
        .withComponentTypeName("name")
        .withDescription("description")
        .thatContainsPlasma()
        .build();
    
    ComponentTypeSearchViewModel expectedViewModel = aComponentTypeSearchViewModel()
        .withId(componentTypeId)
        .withComponentTypeCode("0000")
        .withComponentTypeName("name")
        .withDescription("description")
        .thatContainsPlasma()
        .build();
    
    // run test
    ComponentTypeSearchViewModel convertedViewModel = componentTypeFactory.createSearchViewModel(componentType);

    // do asserts
    assertThat(convertedViewModel, is(notNullValue()));
    assertThat("Correct view model", convertedViewModel, hasSameStateAsComponentTypeSearchViewModel(expectedViewModel));
    
  }

}
