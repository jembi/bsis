package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentManagementViewModelBuilder.aComponentManagementViewModel;
import static org.jembi.bsis.helpers.builders.ComponentViewModelBuilder.aComponentViewModel;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.matchers.ComponentFullViewModelMatcher.hasSameStateAsComponentFullViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentManagementViewModelMatcher.hasSameStateAsComponentManagementViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentViewModelMatcher.hasSameStateAsComponentViewModel;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.helpers.builders.ComponentFullViewModelBuilder;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.service.ComponentConstraintChecker;
import org.jembi.bsis.viewmodel.ComponentFullViewModel;
import org.jembi.bsis.viewmodel.ComponentManagementViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComponentFactoryTests {
  
  @InjectMocks
  private ComponentFactory componentFactory;
  
  @Mock
  private LocationFactory locationFactory;

  @Mock
  private ComponentTypeFactory componentTypeFactory;

  @Mock
  private PackTypeFactory packTypeFactory;

  @Mock
  private ComponentConstraintChecker componentConstraintChecker;
  
  @Mock
  private ComponentRepository componentRepository;

  @Test
  public void createComponentFullViewModel_oneComponent() throws Exception {
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
    ComponentFullViewModel expectedViewModel = ComponentFullViewModelBuilder.aComponentFullViewModel()
        .withId(1L)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withComponentType(new ComponentTypeViewModel(componentType))
        .withLocation(new LocationFullViewModel(location))
        .withBloodAbo(donation.getBloodAbo())
        .withBloodRh(donation.getBloodRh())
        .build();

    // setup mocks
    when(locationFactory.createFullViewModel(location)).thenReturn(new LocationFullViewModel(location));
    when(componentTypeFactory.createViewModel(componentType)).thenReturn(new ComponentTypeViewModel(componentType));

    // run test
    ComponentFullViewModel convertedViewModel = componentFactory.createComponentFullViewModel(component);
    
    // do asserts
    Assert.assertNotNull("View model created", convertedViewModel);
    assertThat("Correct view model", convertedViewModel, hasSameStateAsComponentFullViewModel(expectedViewModel));
  }
  
  @Test
  public void createComponentFullViewModels_componentList() throws Exception {
    // set up data
    ArrayList<Component> components = new ArrayList<>();
    Donation donation = DonationBuilder.aDonation().withBloodAbo("A").withBloodRh("+").build();
    components.add(aComponent().withId(1L).withStatus(ComponentStatus.AVAILABLE).withDonation(donation).build());
    components.add(aComponent().withId(2L).withStatus(ComponentStatus.DISCARDED).withDonation(donation).build());
    
    // run test
    List<ComponentFullViewModel> viewModels = componentFactory.createComponentFullViewModels(components);
    
    // do asserts
    Assert.assertNotNull("View models created", viewModels);
    Assert.assertEquals("Correct number of view models created", 2, viewModels.size());
  }
  
  @Test
  public void createComponentFullViewModels_nullCollection() throws Exception {
    // set up data
    ArrayList<Component> components = new ArrayList<>();
    components.add(aComponent().withId(1L).withStatus(ComponentStatus.AVAILABLE).build());
    components.add(aComponent().withId(2L).withStatus(ComponentStatus.DISCARDED).build());
    
    // run test
    List<ComponentFullViewModel> viewModels = componentFactory.createComponentFullViewModels(null);
    
    // do asserts
    Assert.assertNotNull("View models created", viewModels);
    Assert.assertTrue("No view models", viewModels.isEmpty());
  }

  @Test
  public void createManagementViewModel_oneComponent() {
    // set up data
    Date createdOn = new Date();
    ComponentType componentType = ComponentTypeBuilder.aComponentType().build();
    Component component = aComponent()
        .withId(1L)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentCode("0011")
        .withComponentType(componentType)
        .withCreatedOn(createdOn)
        .withWeight(222)
        .build();
    ComponentManagementViewModel expectedViewModel = aComponentManagementViewModel()
        .withId(1L)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentCode("0011")
        .withComponentType(new ComponentTypeFullViewModel(componentType))
        .withCreatedOn(createdOn)
        .withWeigth(222)
        .withPermission("canDiscard", true)
        .withPermission("canProcess", true)
        .withPermission("canRecordWeight", true)
        .withPermission("canUnprocess", true)
        .withPermission("canUndiscard", true)
        .withExpiryStatus("")
        .build();

    // setup mocks
    when(componentTypeFactory.createFullViewModel(componentType)).thenReturn(new ComponentTypeFullViewModel(componentType));
    when(componentConstraintChecker.canDiscard(component)).thenReturn(true);
    when(componentConstraintChecker.canProcess(component)).thenReturn(true);
    when(componentConstraintChecker.canRecordWeight(component)).thenReturn(true);
    when(componentConstraintChecker.canUnprocess(component)).thenReturn(true);
    when(componentConstraintChecker.canUndiscard(component)).thenReturn(true);

    // run test
    ComponentManagementViewModel convertedViewModel = componentFactory.createManagementViewModel(component);

    // do asserts
    Assert.assertNotNull("View model created", convertedViewModel);
    assertThat("Created correctly", convertedViewModel, hasSameStateAsComponentManagementViewModel(expectedViewModel));
  }

  @Test
  public void createComponentViewModel_oneComponent() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation().withDonationIdentificationNumber("1234567").build();
    ComponentType componentType = ComponentTypeBuilder.aComponentType().build();
    Component component = aComponent().withId(1L)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(componentType)
        .withComponentCode("componentCode")
        .withCreatedOn(new Date())
        .withDonation(donation)
        .build();
    ComponentViewModel expectedViewModel = aComponentViewModel().withId(1L)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(new ComponentTypeViewModel(componentType))
        .withComponentCode("componentCode")
        .withCreatedOn(new Date())
        .withDonationIdentificationNumber("1234567")
        .withExpiryStatus("")
        .build();

    // setup mocks
    when(componentTypeFactory.createViewModel(componentType)).thenReturn(new ComponentTypeViewModel(componentType));

    // run test
    ComponentViewModel convertedViewModel = componentFactory.createComponentViewModel(component);

    // do asserts
    Assert.assertNotNull("View model created", convertedViewModel);
    assertThat("Correct view model", convertedViewModel, hasSameStateAsComponentViewModel(expectedViewModel));
  }

  @Test
  public void createComponentViewModels_componentList() throws Exception {
    // set up data
    ArrayList<Component> components = new ArrayList<>();
    components.add(aComponent().withId(1L).withStatus(ComponentStatus.AVAILABLE).build());
    components.add(aComponent().withId(2L).withStatus(ComponentStatus.DISCARDED).build());

    // run test
    List<ComponentViewModel> viewModels = componentFactory.createComponentViewModels(components);

    // do asserts
    Assert.assertNotNull("View models created", viewModels);
    Assert.assertEquals("Correct number of view models created", 2, viewModels.size());
  }
}
