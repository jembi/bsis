package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeViewModelBuilder.aComponentTypeViewModel;
import static org.jembi.bsis.helpers.builders.InventoryFullViewModelBuilder.anInventoryFullViewModel;
import static org.jembi.bsis.helpers.builders.InventoryViewModelBuilder.anInventoryViewModel;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.OrderFormBuilder.anOrderForm;
import static org.jembi.bsis.helpers.builders.OrderFormFullViewModelBuilder.anOrderFormFullViewModel;
import static org.jembi.bsis.helpers.matchers.InventoryFullViewModelMatcher.hasSameStateAsInventoryFullViewModel;
import static org.jembi.bsis.helpers.matchers.InventoryViewModelMatcher.hasSameStateAsInventoryViewModel;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.repository.OrderFormRepository;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.InventoryFullViewModel;
import org.jembi.bsis.viewmodel.InventoryViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.OrderFormFullViewModel;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InventoryFactoryTests {

  @InjectMocks
  private InventoryFactory inventoryFactory;

  @Mock
  private ComponentTypeFactory componentTypeFactory;

  @Mock
  private LocationFactory locationFactory;

  @Mock
  private OrderFormRepository orderFormRepository;

  @Mock
  private OrderFormFactory orderFormFactory;

  @Test
  public void testCreateInventoryViewModel_shouldReturnViewModelWithTheCorrectStateAndNoExpiryStatus() {

    // Setup
    Date createdOn = new Date();
    ComponentType aComponentType = aComponentType().withId(1L).build();
    Component component = aComponent()
        .withComponentType(aComponentType)
        .withDonation(DonationBuilder.aDonation().withBloodAbo("A").withBloodRh("+").build())
        .withLocation(LocationBuilder.aDistributionSite().withId(1L).build())
        .withCreatedOn(createdOn)
        .withStatus(ComponentStatus.ISSUED)
        .build();   
    
    // Setup mocks
    LocationViewModel locationViewModel = aLocationViewModel().withId(1L).build();
    when(locationFactory.createViewModel(component.getLocation())).thenReturn(locationViewModel);

    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .withId(1L)
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    
    InventoryViewModel expectedInventoryViewModel =
        anInventoryViewModel()
          .withLocation(locationViewModel)
          .withInventoryStatus(component.getInventoryStatus())
          .withId(component.getId())
          .withDonationIdentificationNumber(component.getDonationIdentificationNumber())
          .withComponentCode(component.getComponentCode())
          .withCreatedOn(createdOn)
          .withComponentType(componentTypeViewModel)
          .withExpiryStatus("")
          .withBloodGroup("A+")
          .withComponentStatus(ComponentStatus.ISSUED)
          .build();

    // Run test
    InventoryViewModel createdInventoryViewModel = inventoryFactory.createViewModel(component);
    
    // Verify
    assertThat(createdInventoryViewModel, hasSameStateAsInventoryViewModel(expectedInventoryViewModel));
  }

  @Test
  public void testCreateInventoryViewModels_shouldReturnCorrectNumberOfViewModels() {

    // Setup
    Component component1 = aComponent()
        .build();
    Component component2 = aComponent()
        .build();

    // Run test
    List<InventoryViewModel> createdInventoryViewModels = inventoryFactory.createViewModels(Arrays.asList(component1, component2));

    // do asserts
    Assert.assertNotNull("View models created", createdInventoryViewModels);
    Assert.assertEquals("Correct number of view models created", 2, createdInventoryViewModels.size());
  }

  @Test
  public void testCreateInventoryViewModelsWithNullColection_shouldReturnEmptyCollection() {

    // Run test
    List<InventoryViewModel> createdInventoryViewModels = inventoryFactory.createViewModels(null);

    // do asserts
    Assert.assertTrue("Collection is empty", createdInventoryViewModels.isEmpty());
  }

  @Test
  public void testCreateFullViewModel_shouldReturnFullViewModelWithTheCorrectState() {

    // Setup
    Date createdOn = new Date();
    ComponentType aComponentType = aComponentType().withId(1L).build();
    Component component = aComponent()
        .withId(1L)
        .withComponentType(aComponentType)
        .withDonation(DonationBuilder.aDonation().withBloodAbo("A").withBloodRh("+").build())
        .withLocation(LocationBuilder.aDistributionSite().withId(1L).build())
        .withCreatedOn(createdOn)
        .withStatus(ComponentStatus.ISSUED)
        .build();

    // Setup mocks
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .withId(1L)
        .build();

    LocationViewModel locationViewModel = aLocationViewModel().withId(1L).build();
    InventoryViewModel componentFullViewModel = anInventoryViewModel()
        .withId(1L)
        .withComponentStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withComponentType(componentTypeViewModel)
        .withLocation(locationViewModel)
        .withBloodGroup("A+")
        .build();

    OrderForm orderForm = anOrderForm().withComponents(Arrays.asList(component)).build();
    OrderFormFullViewModel orderFormFullViewModel = anOrderFormFullViewModel().withComponent(componentFullViewModel).build();

    InventoryFullViewModel expectedFullViewModel =
        anInventoryFullViewModel()
            .withLocation(locationViewModel)
            .withInventoryStatus(component.getInventoryStatus())
            .withId(component.getId())
            .withDonationIdentificationNumber(component.getDonationIdentificationNumber())
            .withComponentCode(component.getComponentCode())
            .withCreatedOn(createdOn)
            .withComponentType(componentTypeViewModel)
            .withExpiryStatus("")
            .withBloodGroup("A+")
            .withOrderForm(orderFormFullViewModel)
            .withComponentStatus(ComponentStatus.ISSUED)
            .build();

    when(locationFactory.createViewModel(component.getLocation())).thenReturn(locationViewModel);
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    when(orderFormRepository.findByComponent(component.getId())).thenReturn(orderForm);
    when(orderFormFactory.createFullViewModel(orderForm)).thenReturn(orderFormFullViewModel);

    // Run test
    InventoryFullViewModel createdFullViewModel = inventoryFactory.createFullViewModel(component);

    // Verify
    assertThat(createdFullViewModel, hasSameStateAsInventoryFullViewModel(expectedFullViewModel));
  }

  @Test
  public void testCreateFullViewModelWithNoOrderFormLinkedToComponent_shouldReturnFullViewModelWithTheCorrectState() {

    // Setup
    Date createdOn = new Date();
    ComponentType aComponentType = aComponentType().withId(1L).build();
    Component component = aComponent()
        .withId(1L)
        .withComponentType(aComponentType)
        .withDonation(DonationBuilder.aDonation().withBloodAbo("A").withBloodRh("+").build())
        .withLocation(LocationBuilder.aDistributionSite().withId(1L).build())
        .withCreatedOn(createdOn)
        .withStatus(ComponentStatus.ISSUED)
        .build();

    // Setup mocks
    LocationViewModel locationViewModel = aLocationViewModel().withId(1L).build();
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .withId(1L)
        .build();

    InventoryFullViewModel expectedFullViewModel =
        anInventoryFullViewModel()
            .withLocation(locationViewModel)
            .withInventoryStatus(component.getInventoryStatus())
            .withId(component.getId())
            .withDonationIdentificationNumber(component.getDonationIdentificationNumber())
            .withComponentCode(component.getComponentCode())
            .withCreatedOn(createdOn)
            .withComponentType(componentTypeViewModel)
            .withExpiryStatus("")
            .withOrderForm(null)
            .withBloodGroup("A+")
            .withComponentStatus(ComponentStatus.ISSUED)
            .build();

    when(locationFactory.createViewModel(component.getLocation())).thenReturn(locationViewModel);
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    when(orderFormRepository.findByComponent(component.getId())).thenReturn(null);

    // Run test
    InventoryFullViewModel createdFullViewModel = inventoryFactory.createFullViewModel(component);

    // Verify
    assertThat(createdFullViewModel, hasSameStateAsInventoryFullViewModel(expectedFullViewModel));
  }

  public void testCreateFullViewModels_shouldCreateFullViewModels() {

    // Setup
    Component component1 = aComponent()
        .withId(1L)
        .build();
    Component component2 = aComponent()
        .withId(2L)
        .build();

    // Run test
    List<InventoryFullViewModel> createdFullViewModels = inventoryFactory.createFullViewModels(Arrays.asList(component1, component2));

    // do asserts
    Assert.assertNotNull("View models created", createdFullViewModels);
    Assert.assertEquals("Correct number of view models created", 2, createdFullViewModels.size());
  }

  @Test
  public void testCreateFullViewModelsWithNullCollection_shouldReturnEmptyCollection() {

    // Run test
    List<InventoryFullViewModel> createdFullViewModels = inventoryFactory.createFullViewModels(null);

    // do asserts
    Assert.assertTrue("Collection is empty", createdFullViewModels.isEmpty());
  }

  @Test
  public void testExpiryStatusWithNoExpiryDate_shouldBeEmpty() {

    // Setup
    Component component = aComponent()
        .withDonation(DonationBuilder.aDonation().build())
        .withExpiresOn(null).build();

    // Setup mocks
    LocationFullViewModel locationFullViewModel = new LocationFullViewModel(component.getLocation());
    when(locationFactory.createFullViewModel(component.getLocation())).thenReturn(locationFullViewModel);
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    
    // Run test
    InventoryViewModel viewModel = inventoryFactory.createViewModel(component);

    // Verify
    Assert.assertEquals("correct expiry status", "", viewModel.getExpiryStatus());

  }

  @Test
  public void testExpiryStatusWithFutureExpiryDate_shouldReturnDaysUntilExpiry() {
    // Setup
    Date expiresOn = new DateTime().plusHours(99).toDate();
    Component component = aComponent().withExpiresOn(expiresOn).withStatus(ComponentStatus.ISSUED).build();

    // Setup mocks
    LocationViewModel locationViewModel = aLocationViewModel().withId(1L).build();
    when(locationFactory.createViewModel(component.getLocation())).thenReturn(locationViewModel);
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    
    InventoryViewModel expectedInventoryViewModel =
        anInventoryViewModel()
          .withInventoryStatus(component.getInventoryStatus())
          .withExpiresOn(expiresOn)
          .withExpiryStatus("4 days to expire")
          .withComponentType(componentTypeViewModel)
          .withLocation(locationViewModel)
          .withBloodGroup("")
          .withComponentStatus(ComponentStatus.ISSUED)
          .build();

    // Run test
    InventoryViewModel createdInventoryViewModel = inventoryFactory.createViewModel(component);
    
    // Verify
    assertThat(createdInventoryViewModel, hasSameStateAsInventoryViewModel(expectedInventoryViewModel));
  }
  
  @Test
  public void testExpiryStatusWithPastExpiryDate_shouldReturnAlreadyExpiredMsg() {
    // Setup
    Date expiresOn = new DateTime().minusDays(20).toDate();
    Component component = aComponent().withExpiresOn(expiresOn).withStatus(ComponentStatus.ISSUED).build();

    // Setup mocks.
    LocationViewModel locationViewModel = aLocationViewModel().withId(1L).build();
    when(locationFactory.createViewModel(component.getLocation())).thenReturn(locationViewModel);
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    
    InventoryViewModel expectedInventoryViewModel =
        anInventoryViewModel()
          .withInventoryStatus(component.getInventoryStatus())
          .withExpiresOn(expiresOn)
          .withExpiryStatus("Already expired")
          .withComponentType(componentTypeViewModel)
          .withLocation(locationViewModel)
          .withBloodGroup("")
          .withComponentStatus(ComponentStatus.ISSUED)
          .build();

    // Run test
    InventoryViewModel createdInventoryViewModel = inventoryFactory.createViewModel(component);

    // Verify
    assertThat(createdInventoryViewModel, hasSameStateAsInventoryViewModel(expectedInventoryViewModel));
  }
}
