package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.matchers.ComponentViewModelMatcher.hasSameStateAsComponentViewModel;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.helpers.builders.ComponentViewModelBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
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

  @Mock
  ComponentTypeFactory componentTypeFactory;

  @Mock
  PackTypeFactory packTypeFactory;

  @Test
  public void createComponentViewModel_oneComponent() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation().withBloodAbo("A").withBloodRh("+").build();
    Location location = aLocation().build();
    ComponentType componentType = ComponentTypeBuilder.aComponentType().build();
    Component component = aComponent()
        .withId(1L)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
            .withComponentType(componentType)
        .withLocation(location)
        .withDonation(donation).build();
    ComponentViewModel expectedViewModel = ComponentViewModelBuilder.aComponentViewModel()
        .withId(1L)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withComponentType(new ComponentTypeViewModel(componentType))
        .withLocation(new LocationViewModel(location))
        .withBloodAbo(donation.getBloodAbo())
        .withBloodRh(donation.getBloodRh())
        .build();

    // setup mocks
    when(locationViewModelFactory.createLocationViewModel(location)).thenReturn(new LocationViewModel(location));
    when(componentTypeFactory.createViewModel(componentType)).thenReturn(new ComponentTypeViewModel(componentType));

    // run test
    ComponentViewModel convertedViewModel = componentViewModelFactory.createComponentViewModel(component);
    
    // do asserts
    Assert.assertNotNull("View model created", convertedViewModel);
    assertThat("Correct view model", convertedViewModel, hasSameStateAsComponentViewModel(expectedViewModel));
  }
  
  @Test
  public void createComponentViewModels_componentList() throws Exception {
    // set up data
    ArrayList<Component> components = new ArrayList<>();
    Donation donation = DonationBuilder.aDonation().withBloodAbo("A").withBloodRh("+").build();
    components.add(aComponent().withId(1L).withStatus(ComponentStatus.AVAILABLE).withDonation(donation).build());
    components.add(aComponent().withId(2L).withStatus(ComponentStatus.DISCARDED).withDonation(donation).build());
    
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
