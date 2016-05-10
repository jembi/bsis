package factory;

import static helpers.builders.ComponentBuilder.aComponent;

import java.util.ArrayList;
import java.util.List;

import model.component.Component;
import model.component.ComponentStatus;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import viewmodel.ComponentViewModel;

@RunWith(MockitoJUnitRunner.class)
public class ComponentViewModelFactoryTests {
  
  @InjectMocks
  ComponentViewModelFactory componentViewModelFactory;

  @Test
  public void createComponentViewModel_oneComponent() throws Exception {
    // set up data
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.AVAILABLE).build();
    
    // run test
    ComponentViewModel viewModel = componentViewModelFactory.createComponentViewModel(component);
    
    // do asserts
    Assert.assertNotNull("View model created", viewModel);
    Assert.assertEquals("Correct view model created", Long.valueOf(1), viewModel.getId());
    Assert.assertEquals("Correct view model created", "AVAILABLE", viewModel.getStatus());
  }
  
  @Test
  public void createComponentViewModels_componentList() throws Exception {
    // set up data
    ArrayList<Component> components = new ArrayList<>();
    components.add(aComponent().withId(1L).withStatus(ComponentStatus.AVAILABLE).build());
    components.add(aComponent().withId(2L).withStatus(ComponentStatus.DISCARDED).build());
    
    // run test
    List<ComponentViewModel> viewModels = componentViewModelFactory.createComponentViewModels(components);
    
    // do asserts
    Assert.assertNotNull("View models created", viewModels);
    Assert.assertEquals("Correct number of view models created", 2, viewModels.size());
  }
  
  @Test
  public void createComponentViewModels_nullCollection() throws Exception {
    // set up data
    ArrayList<Component> components = new ArrayList<>();
    components.add(aComponent().withId(1L).withStatus(ComponentStatus.AVAILABLE).build());
    components.add(aComponent().withId(2L).withStatus(ComponentStatus.DISCARDED).build());
    
    // run test
    List<ComponentViewModel> viewModels = componentViewModelFactory.createComponentViewModels(null);
    
    // do asserts
    Assert.assertNotNull("View models created", viewModels);
    Assert.assertTrue("No view models", viewModels.isEmpty());
  }
}
