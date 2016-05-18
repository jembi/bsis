package factory;

import helpers.builders.ComponentTypeBuilder;

import java.util.Arrays;
import java.util.List;

import model.componenttype.ComponentType;
import model.componenttype.ComponentTypeCombination;
import model.componenttype.ComponentTypeTimeUnits;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import viewmodel.ComponentTypeFullViewModel;
import viewmodel.ComponentTypeViewModel;

@RunWith(MockitoJUnitRunner.class)
public class ComponentTypeFactoryTests {

  @InjectMocks
  private ComponentTypeFactory componentTypeFactory;

  @Test
  public void testSingleFullComponentType_shouldReturnExpectedEntity() {
    ComponentTypeCombination producedComponentTypeCombination = new ComponentTypeCombination();
    ComponentType entity = ComponentTypeBuilder.aComponentType()
        .withId(1L)
        .withComponentTypeName("name")
        .withComponentTypeNameShort("0001")
        .withDescription("descr")
        .withExpiresAfter(90)
        .withHighStorageTemperature(10)
        .withLowStorageTemperature(0)
        .withPreparationInfo("prepare")
        .withProducedComponentTypeCombination(producedComponentTypeCombination)
        .build();

    ComponentTypeFullViewModel viewModel = componentTypeFactory.createFullViewModel(entity);
    
    Assert.assertNotNull("View Model was created", viewModel);
    Assert.assertEquals("View Model correct", Long.valueOf(1), viewModel.getId());
    Assert.assertEquals("View Model correct", "name", viewModel.getComponentTypeName());
    Assert.assertEquals("View Model correct", "0001", viewModel.getComponentTypeNameShort());
    Assert.assertEquals("View Model correct", "descr", viewModel.getDescription());
    Assert.assertEquals("View Model correct", Integer.valueOf(90), viewModel.getExpiresAfter());
    Assert.assertEquals("View Model correct", ComponentTypeTimeUnits.DAYS, viewModel.getExpiresAfterUnits());
    Assert.assertEquals("View Model correct", Integer.valueOf(10), viewModel.getHighStorageTemperature());
    Assert.assertEquals("View Model correct", Integer.valueOf(0), viewModel.getLowStorageTemperature());
    Assert.assertEquals("View Model correct", "prepare", viewModel.getPreparationInfo());
    Assert.assertNotNull("View Model correct", viewModel.getProducedComponentTypeCombinations());
    Assert.assertEquals("View Model correct", 1, viewModel.getProducedComponentTypeCombinations().size());
  }
  
  @Test
  public void testSingleComponentType_shouldReturnExpectedEntity() {
    ComponentType entity = ComponentTypeBuilder.aComponentType()
        .withId(1L)
        .withComponentTypeName("name")
        .withComponentTypeNameShort("0001")
        .withDescription("descr")
        .build();

    ComponentTypeViewModel viewModel = componentTypeFactory.createViewModel(entity);
    
    Assert.assertNotNull("View Model was created", viewModel);
    Assert.assertEquals("View Model correct", Long.valueOf(1), viewModel.getId());
    Assert.assertEquals("View Model correct", "name", viewModel.getComponentTypeName());
    Assert.assertEquals("View Model correct", "0001", viewModel.getComponentTypeNameShort());
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
}
