package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeViewModelBuilder.aComponentTypeViewModel;
import static org.jembi.bsis.helpers.builders.ComponentViewModelBuilder.aComponentViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentTypeViewModelMatcher.hasSameStateAsComponentTypeViewModel;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.viewmodel.ComponentTypeFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeSearchViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComponentTypeFactoryTests {

  @InjectMocks
  private ComponentTypeFactory componentTypeFactory;

  @Test
  public void testSingleFullComponentType_shouldReturnExpectedViewModel() {
    ComponentTypeCombination producedComponentTypeCombination = new ComponentTypeCombination();
    ComponentType entity = ComponentTypeBuilder.aComponentType()
        .withId(1L)
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
        .withCanBeIssued(false)
        .build();

    ComponentTypeFullViewModel viewModel = componentTypeFactory.createFullViewModel(entity);
    
    Assert.assertNotNull("View Model was created", viewModel);
    Assert.assertEquals("View Model correct", Long.valueOf(1), viewModel.getId());
    Assert.assertEquals("View Model correct", "name", viewModel.getComponentTypeName());
    Assert.assertEquals("View Model correct", "0001", viewModel.getComponentTypeCode());
    Assert.assertEquals("View Model correct", "descr", viewModel.getDescription());
    Assert.assertEquals("View Model correct", Integer.valueOf(90), viewModel.getExpiresAfter());
    Assert.assertEquals("View Model correct", ComponentTypeTimeUnits.DAYS, viewModel.getExpiresAfterUnits());
    Assert.assertEquals("View Model correct", 10, viewModel.getHighStorageTemperature());
    Assert.assertEquals("View Model correct", 0, viewModel.getLowStorageTemperature());
    Assert.assertEquals("View Model correct", "preparationInfo", viewModel.getPreparationInfo());
    Assert.assertNotNull("View Model correct", viewModel.getProducedComponentTypeCombinations());
    Assert.assertEquals("View Model correct", 1, viewModel.getProducedComponentTypeCombinations().size());
    Assert.assertEquals("View Model correct", "transportInfo", viewModel.getTransportInfo());
    Assert.assertEquals("View Model correct", "storageInfo", viewModel.getStorageInfo());
    Assert.assertEquals("View Model correct", false, viewModel.getCanBeIssued());
  }
  
  @Test
  public void testSingleSearchComponentType_shouldReturnExpectedViewModel() {
    ComponentType entity = ComponentTypeBuilder.aComponentType()
        .withId(1L)
        .withComponentTypeName("name")
        .withComponentTypeCode("0001")
        .withDescription("descr")
        .withExpiresAfter(90)
        .withCanBeIssued(false)
        .build();

    ComponentTypeSearchViewModel viewModel = componentTypeFactory.createSearchViewModel(entity);
    
    Assert.assertNotNull("View Model was created", viewModel);
    Assert.assertEquals("View Model correct", Long.valueOf(1), viewModel.getId());
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
    ComponentType entity = ComponentTypeBuilder.aComponentType()
        .withId(1L)
        .withComponentTypeName("name")
        .withComponentTypeCode("0001")
        .withDescription("descr")
        .build();

    ComponentTypeViewModel viewModel = componentTypeFactory.createViewModel(entity);
    
    Assert.assertNotNull("View Model was created", viewModel);
    Assert.assertEquals("View Model correct", Long.valueOf(1), viewModel.getId());
    Assert.assertEquals("View Model correct", "name", viewModel.getComponentTypeName());
    Assert.assertEquals("View Model correct", "0001", viewModel.getComponentTypeCode());
    Assert.assertEquals("View Model correct", "descr", viewModel.getDescription());
  }
  
  @Test
  public void testMultipleComponentType_shouldReturnExpectedEntities() {
    ComponentType entity1 = ComponentTypeBuilder.aComponentType().withId(1L).withComponentTypeName("test1").build();
    ComponentType entity2 = ComponentTypeBuilder.aComponentType().withId(2L).withComponentTypeName("test2").build();

    List<ComponentTypeViewModel> viewModels = componentTypeFactory.createViewModels(Arrays.asList(entity1, entity2));
    
    Assert.assertNotNull("View Models were created", viewModels);
    Assert.assertEquals("View Models were created", 2, viewModels.size());
    Assert.assertEquals("View Model correct", Long.valueOf(1), viewModels.get(0).getId());
    Assert.assertEquals("View Model correct", Long.valueOf(2), viewModels.get(1).getId());
  }
  
  @Test
  public void testMultipleComponentTypeSearch_shouldReturnExpectedViewModels() {
    ComponentType entity1 = ComponentTypeBuilder.aComponentType().withId(1L).withComponentTypeName("test1").build();
    ComponentType entity2 = ComponentTypeBuilder.aComponentType().withId(2L).withComponentTypeName("test2").build();

    List<ComponentTypeSearchViewModel> viewModels = componentTypeFactory.createSearchViewModels(Arrays.asList(entity1, entity2));
    
    Assert.assertNotNull("View Models were created", viewModels);
    Assert.assertEquals("View Models were created", 2, viewModels.size());
    Assert.assertEquals("View Model correct", Long.valueOf(1), viewModels.get(0).getId());
    Assert.assertEquals("View Model correct", Long.valueOf(2), viewModels.get(1).getId());
  }
  
  @Test
  public void testComponentTypeViewModelWithContainsPlasma_shouldReturnExpectedViewModel() {
    boolean containsPlasma = true;
    ComponentType componentType = aComponentType()
        .withId(1L)
        .withComponentTypeCode("0000")
        .withComponentTypeName("name")
        .withDescription("description")
        .withContainsPlasma(containsPlasma)
        .build();
    
    ComponentTypeViewModel expectedViewModel = aComponentTypeViewModel()
        .withId(1L)
        .withComponentTypeCode("0000")
        .withComponentTypeName("name")
        .withDescription("description")
        .withContainsPlasma(containsPlasma)
        .build();
    
    // run test
    ComponentTypeViewModel convertedViewModel = componentTypeFactory.createViewModel(componentType);

    // do asserts
    assertThat(convertedViewModel, is(notNullValue()));
    assertThat("Correct view model", convertedViewModel, hasSameStateAsComponentTypeViewModel(expectedViewModel));
    
  }

}
