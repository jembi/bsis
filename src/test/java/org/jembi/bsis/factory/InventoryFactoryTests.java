package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeViewModelBuilder.aComponentTypeViewModel;
import static org.jembi.bsis.helpers.builders.InventoryFullViewModelBuilder.anInventoryFullViewModel;
import static org.jembi.bsis.helpers.builders.InventoryViewModelBuilder.anInventoryViewModel;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.OrderFormBuilder.anOrderForm;
import static org.jembi.bsis.helpers.builders.OrderFormViewModelBuilder.anOrderFormViewModel;
import static org.jembi.bsis.helpers.matchers.InventoryFullViewModelMatcher.hasSameStateAsInventoryFullViewModel;
import static org.jembi.bsis.helpers.matchers.InventoryViewModelMatcher.hasSameStateAsInventoryViewModel;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.repository.OrderFormRepository;
import org.jembi.bsis.service.ComponentStatusCalculator;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.InventoryFullViewModel;
import org.jembi.bsis.viewmodel.InventoryViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.OrderFormViewModel;
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

  @Mock
  private ComponentStatusCalculator componentStatusCalculator;


  private static final UUID COMPONENT_ID_1 = UUID.randomUUID();
  private static final UUID COMPONENT_ID_2 = UUID.randomUUID();

  @Test
  public void testCreateInventoryViewModel_shouldReturnViewModelWithTheCorrectStateAndNoExpiryStatus() {

    // Setup
    Date createdOn = new Date();
    UUID locationId = UUID.randomUUID();
    UUID componentTypeId = UUID.randomUUID();
    Date expiresOn = new DateTime().minusDays(2).toDate();
    ComponentType aComponentType = aComponentType().withId(componentTypeId).build();
    Component component = aComponent()
        .withComponentType(aComponentType)
        .withDonation(DonationBuilder.aDonation().withBloodAbo("A").withBloodRh("+").build())
        .withLocation(LocationBuilder.aDistributionSite().withId(locationId).build())
        .withCreatedOn(createdOn)
        .withStatus(ComponentStatus.ISSUED)
        .withExpiresOn(expiresOn)
        .build();   
    
    // Setup mocks
    LocationViewModel locationViewModel = aLocationViewModel().withId(locationId).build();
    when(locationFactory.createViewModel(component.getLocation())).thenReturn(locationViewModel);

    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .withId(componentTypeId)
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    when(componentStatusCalculator.getDaysToExpire(component)).thenReturn(-1);

    InventoryViewModel expectedInventoryViewModel =
        anInventoryViewModel()
          .withLocation(locationViewModel)
          .withInventoryStatus(component.getInventoryStatus())
          .withId(component.getId())
          .withDonationIdentificationNumber(component.getDonationIdentificationNumber())
          .withComponentCode(component.getComponentCode())
          .withCreatedOn(createdOn)
          .withComponentType(componentTypeViewModel)
          .withExpiresOn(expiresOn)
          .withDaysToExpire(-1)
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
    UUID locationId = UUID.randomUUID();
    UUID componentTypeId = UUID.randomUUID();
    Date expiresOn = new DateTime().minusDays(2).toDate();
    ComponentType aComponentType = aComponentType().withId(componentTypeId).build();
    Component component = aComponent()
        .withId(COMPONENT_ID_1)
        .withComponentType(aComponentType)
        .withDonation(DonationBuilder.aDonation().withBloodAbo("A").withBloodRh("+").build())
        .withLocation(LocationBuilder.aDistributionSite().withId(locationId).build())
        .withCreatedOn(createdOn)
        .withStatus(ComponentStatus.ISSUED)
        .withExpiresOn(expiresOn)
        .build();

    // Setup mocks
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .withId(componentTypeId)
        .build();

    LocationViewModel locationViewModel = aLocationViewModel().withId(locationId).build();

    OrderForm orderForm = anOrderForm().withComponents(Arrays.asList(component)).build();
    List<OrderForm> orderForms = Arrays.asList(orderForm);
    OrderFormViewModel orderFormViewModel = anOrderFormViewModel().build();
    List<OrderFormViewModel> orderFormViewModels = Arrays.asList(orderFormViewModel);

    InventoryFullViewModel expectedFullViewModel =
        anInventoryFullViewModel()
            .withLocation(locationViewModel)
            .withInventoryStatus(component.getInventoryStatus())
            .withId(component.getId())
            .withDonationIdentificationNumber(component.getDonationIdentificationNumber())
            .withComponentCode(component.getComponentCode())
            .withCreatedOn(createdOn)
            .withComponentType(componentTypeViewModel)
            .withExpiresOn(expiresOn)
            .withDaysToExpire(-1)
            .withBloodGroup("A+")
            .withOrderForms(orderFormViewModels)
            .withComponentStatus(ComponentStatus.ISSUED)
            .build();

    when(locationFactory.createViewModel(component.getLocation())).thenReturn(locationViewModel);
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    when(orderFormRepository.findByComponent(component.getId())).thenReturn(orderForms);
    when(orderFormFactory.createViewModels(orderForms)).thenReturn(orderFormViewModels);
    when(componentStatusCalculator.getDaysToExpire(component)).thenReturn(-1);

    // Run test
    InventoryFullViewModel createdFullViewModel = inventoryFactory.createFullViewModel(component);

    // Verify
    assertThat(createdFullViewModel, hasSameStateAsInventoryFullViewModel(expectedFullViewModel));
  }

  @Test
  public void testCreateFullViewModelWithNoOrderFormLinkedToComponent_shouldReturnFullViewModelWithTheCorrectState() {

    // Setup
    Date createdOn = new Date();
    UUID locationId = UUID.randomUUID();
    UUID componentTypeId = UUID.randomUUID();
    ComponentType aComponentType = aComponentType().withId(componentTypeId).build();
    Component component = aComponent()
        .withId(COMPONENT_ID_1)
        .withComponentType(aComponentType)
        .withDonation(DonationBuilder.aDonation().withBloodAbo("A").withBloodRh("+").build())
        .withLocation(LocationBuilder.aDistributionSite().withId(locationId).build())
        .withCreatedOn(createdOn)
        .withStatus(ComponentStatus.ISSUED)
        .build();

    // Setup mocks
    LocationViewModel locationViewModel = aLocationViewModel().withId(locationId).build();
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .withId(componentTypeId)
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
            .withDaysToExpire(-1)
            .withOrderForms(new ArrayList<OrderFormViewModel>())
            .withBloodGroup("A+")
            .withComponentStatus(ComponentStatus.ISSUED)
            .build();

    when(locationFactory.createViewModel(component.getLocation())).thenReturn(locationViewModel);
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    when(orderFormRepository.findByComponent(component.getId())).thenReturn(new ArrayList<OrderForm>());
    when(componentStatusCalculator.getDaysToExpire(component)).thenReturn(-1);

    // Run test
    InventoryFullViewModel createdFullViewModel = inventoryFactory.createFullViewModel(component);

    // Verify
    assertThat(createdFullViewModel, hasSameStateAsInventoryFullViewModel(expectedFullViewModel));
  }

  @Test
  public void testCreateFullViewModels_shouldCreateFullViewModels() {

    // Setup
    Component component1 = aComponent()
        .withId(COMPONENT_ID_1)
        .build();
    Component component2 = aComponent()
        .withId(COMPONENT_ID_2)
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
  public void testExpiryStatusWithFutureExpiryDate_shouldReturnDaysUntilExpiry() {
    // Setup
    Date expiresOn = new DateTime().plusHours(99).toDate();
    Component component = aComponent().withExpiresOn(expiresOn).withStatus(ComponentStatus.ISSUED).build();

    // Setup mocks
    UUID locationId = UUID.randomUUID();
    LocationViewModel locationViewModel = aLocationViewModel().withId(locationId).build();
    when(locationFactory.createViewModel(component.getLocation())).thenReturn(locationViewModel);
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    when(componentStatusCalculator.getDaysToExpire(component)).thenReturn(4);

    InventoryViewModel expectedInventoryViewModel =
        anInventoryViewModel()
          .withInventoryStatus(component.getInventoryStatus())
          .withExpiresOn(expiresOn)
          .withDaysToExpire(4)
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
    UUID locationId = UUID.randomUUID();
    LocationViewModel locationViewModel = aLocationViewModel().withId(locationId).build();
    when(locationFactory.createViewModel(component.getLocation())).thenReturn(locationViewModel);
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .build();
    when(componentTypeFactory.createViewModel(component.getComponentType()))
        .thenReturn(componentTypeViewModel);
    when(componentStatusCalculator.getDaysToExpire(component)).thenReturn(-1);

    InventoryViewModel expectedInventoryViewModel =
        anInventoryViewModel()
          .withInventoryStatus(component.getInventoryStatus())
          .withExpiresOn(expiresOn)
          .withDaysToExpire(-1)
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
