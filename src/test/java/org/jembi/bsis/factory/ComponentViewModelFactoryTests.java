package org.jembi.bsis.factory;

import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.factory.ComponentViewModelFactory;
import org.jembi.bsis.factory.LocationViewModelFactory;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComponentViewModelFactoryTests {
  
  @InjectMocks
  ComponentViewModelFactory componentViewModelFactory;
  
  @Mock
  LocationViewModelFactory locationViewModelFactory;

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
  public void createComponentViewModel_oneComponentWithLocation() throws Exception {
    // set up data
    Location location = aLocation().build();
    Component component = aComponent().withId(1L).withStatus(ComponentStatus.AVAILABLE).withLocation(location).build();
    
    // setup mocks
    when(locationViewModelFactory.createLocationViewModel(location)).thenReturn(new LocationViewModel(location));
    
    // run test
    ComponentViewModel viewModel = componentViewModelFactory.createComponentViewModel(component);
    
    // do asserts
    Assert.assertNotNull("View model created", viewModel);
    Assert.assertNotNull("Location view model created", viewModel.getLocation());
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
