package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBackingFormBuilder.aComponentBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.InventoryViewModelBuilder.anInventoryViewModel;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aDistributionSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.builders.OrderFormBackingFormBuilder.anOrderFormBackingForm;
import static org.jembi.bsis.helpers.builders.OrderFormBuilder.anOrderForm;
import static org.jembi.bsis.helpers.builders.OrderFormFullViewModelBuilder.anOrderFormFullViewModel;
import static org.jembi.bsis.helpers.builders.OrderFormItemBackingFormBuilder.anOrderFormItemBackingForm;
import static org.jembi.bsis.helpers.builders.OrderFormItemBuilder.anOrderItemForm;
import static org.jembi.bsis.helpers.builders.OrderFormItemViewModelBuilder.anOrderFormItemViewModel;
import static org.jembi.bsis.helpers.builders.OrderFormViewModelBuilder.anOrderFormViewModel;
import static org.jembi.bsis.helpers.builders.PatientBackingFormBuilder.aPatientBackingForm;
import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;
import static org.jembi.bsis.helpers.builders.PatientViewModelBuilder.aPatientViewModel;
import static org.jembi.bsis.helpers.matchers.OrderFormFullViewModelMatcher.hasSameStateAsOrderFormFullViewModel;
import static org.jembi.bsis.helpers.matchers.OrderFormMatcher.hasSameStateAsOrderForm;
import static org.jembi.bsis.helpers.matchers.OrderFormViewModelMatcher.hasSameStateAsOrderFormViewModel;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.OrderFormBackingForm;
import org.jembi.bsis.backingform.OrderFormItemBackingForm;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderFormItem;
import org.jembi.bsis.model.order.OrderType;
import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.OrderFormConstraintChecker;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.InventoryViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.OrderFormFullViewModel;
import org.jembi.bsis.viewmodel.OrderFormItemViewModel;
import org.jembi.bsis.viewmodel.OrderFormViewModel;
import org.jembi.bsis.viewmodel.PatientViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class OrderFormFactoryTests extends UnitTestSuite {

  @InjectMocks
  private OrderFormFactory orderFormFactory;

  @Mock
  private LocationRepository locationRepository;
  
  @Mock
  private OrderFormItemFactory orderFormItemFactory;

  @Mock
  private InventoryFactory inventoryFactory;
  
  @Mock
  private LocationFactory locationFactory;

  @Mock
  private PatientFactory patientFactory;

  @Mock
  private ComponentRepository componentRepository;

  @Mock
  private OrderFormConstraintChecker orderFormConstraintChecker;
 
  private static final UUID locationId1 = UUID.randomUUID();
  private static final UUID locationId2 = UUID.randomUUID();

  private Location getBaseDispatchedFromLocation() {
    return aDistributionSite().withName("LocFrom").withId(locationId1).build();
  }

  private Location getBaseDispatchedToLocation() {
    return aDistributionSite().withName("LocTo").withId(locationId2).build();
  }

  private LocationBackingForm getBaseDispatchedFromLocationBackingForm() {
    return aDistributionSiteBackingForm().withName("LocFrom").withId(locationId1).build();
  }

  private LocationBackingForm getBaseDispatchedToLocationBackingForm() {
    return aDistributionSiteBackingForm().withName("LocTo").withId(locationId2).build();
  }

  @Test
  public void testConvertOrderFormBackingFormToOrderFormEntity_shouldReturnExpectedEntity() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate = new Date();

    OrderForm expectedEntity = anOrderForm().withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).build();
    
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withDispatchedFrom(getBaseDispatchedFromLocationBackingForm())
        .withDispatchedTo(getBaseDispatchedToLocationBackingForm()).withOrderDate(orderDate).build();

    // Setup mock
    when(locationRepository.getLocation(locationId1)).thenReturn(dispatchedFrom);
    when(locationRepository.getLocation(locationId2)).thenReturn(dispatchedTo);

    OrderForm convertedEntity = orderFormFactory.createEntity(backingForm);
   
    assertThat(convertedEntity, hasSameStateAsOrderForm(expectedEntity));
  }
  
  @Test
  public void testConvertOrderFormBackingFormToOrderFormEntityWithItems_shouldReturnExpectedEntity() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate = new Date();

    OrderFormItem expectedItem1 = anOrderItemForm().withId(UUID.randomUUID()).withBloodAbo("A").withBloodRh("+").build();
    OrderForm expectedEntity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate)
        .withOrderFormItem(expectedItem1).build();
    
    OrderFormItemBackingForm item1 = anOrderFormItemBackingForm().withId(UUID.randomUUID()).withBloodGroup("A+").build();
    OrderFormBackingForm backingForm = anOrderFormBackingForm()
        .withDispatchedFrom(getBaseDispatchedFromLocationBackingForm())
        .withDispatchedTo(getBaseDispatchedToLocationBackingForm())
        .withOrderDate(orderDate)
        .withItem(item1).build();

    // Setup mock
    when(locationRepository.getLocation(locationId1)).thenReturn(dispatchedFrom);
    when(locationRepository.getLocation(locationId2)).thenReturn(dispatchedTo);
    when(orderFormItemFactory.createEntity(Mockito.any(OrderForm.class), Mockito.any(OrderFormItemBackingForm.class)))
      .thenReturn(expectedItem1);

    OrderForm convertedEntity = orderFormFactory.createEntity(backingForm);
   
    assertThat(convertedEntity, hasSameStateAsOrderForm(expectedEntity));
  }

  @Test
  public void testConvertEntityToOrderFormFullViewModel_shouldReturnExpectedViewModel() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate = new Date();
    UUID orderFormId = UUID.randomUUID();

    OrderFormFullViewModel expectedViewModel = anOrderFormFullViewModel()
        .withDispatchedFrom(new LocationFullViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationFullViewModel(dispatchedTo))
        .withOrderDate(orderDate)
        .withPermission("canDispatch", true).withPermission("canEdit", true).withPermission("canDelete", true)
        .withId(orderFormId).build();

    OrderForm entity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).withId(orderFormId).build();

    // Setup mock
    when(locationFactory.createFullViewModel(dispatchedFrom)).thenReturn(expectedViewModel.getDispatchedFrom());
    when(locationFactory.createFullViewModel(dispatchedTo)).thenReturn(expectedViewModel.getDispatchedTo());
    when(orderFormConstraintChecker.canDispatch(entity)).thenReturn(true);
    when(orderFormConstraintChecker.canEdit(entity)).thenReturn(true);
    when(orderFormConstraintChecker.canDelete(entity)).thenReturn(true);

    OrderFormFullViewModel convertedViewModel = orderFormFactory.createFullViewModel(entity);

    assertThat(convertedViewModel, hasSameStateAsOrderFormFullViewModel(expectedViewModel));
  }

  @Test
  public void testConvertEntityToOrderFormFullViewModelWithItems_shouldReturnExpectedViewModel() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate = new Date();
    UUID orderFormId = UUID.randomUUID();

    OrderFormItemViewModel expectedItem1 = anOrderFormItemViewModel().withBloodGroup("A+").build();
    OrderFormItemViewModel expectedItem2 = anOrderFormItemViewModel().withBloodGroup("B+").build();
    OrderFormFullViewModel expectedViewModel = anOrderFormFullViewModel()
        .withDispatchedFrom(new LocationFullViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationFullViewModel(dispatchedTo))
        .withPermission("canDispatch", true).withPermission("canEdit", true).withPermission("canDelete", true)
        .withOrderDate(orderDate).withId(orderFormId)
        .withItem(expectedItem1).withItem(expectedItem2).build();

    OrderFormItem item1 = anOrderItemForm().withBloodAbo("A").withBloodRh("+").build();
    OrderFormItem item2 = anOrderItemForm().withBloodAbo("A").withBloodRh("+").build();
    OrderForm entity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate)
        .withId(orderFormId).withOrderFormItem(item1).withOrderFormItem(item2).build();

    // Setup mock
    when(locationFactory.createFullViewModel(dispatchedFrom)).thenReturn(expectedViewModel.getDispatchedFrom());
    when(locationFactory.createFullViewModel(dispatchedTo)).thenReturn(expectedViewModel.getDispatchedTo());
    when(orderFormItemFactory.createViewModels(Arrays.asList(item1, item2))).thenReturn(Arrays.asList(expectedItem1, expectedItem2));
    when(orderFormConstraintChecker.canDispatch(entity)).thenReturn(true);
    when(orderFormConstraintChecker.canEdit(entity)).thenReturn(true);
    when(orderFormConstraintChecker.canDelete(entity)).thenReturn(true);

    OrderFormFullViewModel convertedViewModel = orderFormFactory.createFullViewModel(entity);

    assertThat(convertedViewModel, hasSameStateAsOrderFormFullViewModel(expectedViewModel));
  }
  
  @Test
  public void testConvertOrderFormBackingFormToOrderFormEntityWithComponents_shouldReturnExpectedEntity() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate = new Date();
    UUID componentId = UUID.randomUUID();

    Component expectedComponent =
        aComponent().withInventoryStatus(InventoryStatus.IN_STOCK)
        .withLocation(dispatchedFrom).withId(componentId).build();
    ComponentBackingForm componentBackingForm = aComponentBackingForm().withId(componentId).build();

    OrderForm expectedEntity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).withComponent(expectedComponent).build();

    OrderFormBackingForm backingForm = anOrderFormBackingForm()
        .withDispatchedFrom(getBaseDispatchedFromLocationBackingForm())
        .withDispatchedTo(getBaseDispatchedToLocationBackingForm())
        .withOrderDate(orderDate).withComponent(componentBackingForm).build();

    // Setup mock
    when(locationRepository.getLocation(locationId1)).thenReturn(dispatchedFrom);
    when(locationRepository.getLocation(locationId2)).thenReturn(dispatchedTo);
    when(componentRepository.findComponent(componentId)).thenReturn(expectedComponent);

    OrderForm convertedEntity = orderFormFactory.createEntity(backingForm);

    assertThat(convertedEntity, hasSameStateAsOrderForm(expectedEntity));
  }
  
  @Test
  public void testConvertEntityToOrderFormFullViewModelWithComponents_shouldReturnExpectedViewModel() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate = new Date();
    UUID orderFormId = UUID.randomUUID();
    UUID componentId = UUID.randomUUID();

    Component component = aComponent().withId(componentId).withInventoryStatus(InventoryStatus.IN_STOCK).withLocation(dispatchedFrom).build();
    InventoryViewModel inventoryViewModel = anInventoryViewModel().withId(componentId)
        .withInventoryStatus(InventoryStatus.IN_STOCK).withLocation(aLocationViewModel().withId(locationId1).build()).build();

    OrderFormFullViewModel expectedViewModel = anOrderFormFullViewModel()
        .withDispatchedFrom(new LocationFullViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationFullViewModel(dispatchedTo))
        .withPermission("canDispatch", true).withPermission("canEdit", true).withPermission("canDelete", true)
        .withOrderDate(orderDate).withId(orderFormId)
        .withComponent(inventoryViewModel).build();

    OrderForm entity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate)
        .withId(orderFormId).withComponent(component).build();
    
    // Setup mock
    when(inventoryFactory.createViewModels(entity.getComponents())).thenReturn(expectedViewModel.getComponents());
    when(locationFactory.createFullViewModel(dispatchedFrom)).thenReturn(expectedViewModel.getDispatchedFrom());
    when(locationFactory.createFullViewModel(dispatchedTo)).thenReturn(expectedViewModel.getDispatchedTo());
    when(orderFormConstraintChecker.canDispatch(entity)).thenReturn(true);
    when(orderFormConstraintChecker.canEdit(entity)).thenReturn(true);
    when(orderFormConstraintChecker.canDelete(entity)).thenReturn(true);

    OrderFormFullViewModel convertedViewModel = orderFormFactory.createFullViewModel(entity);

    assertThat(convertedViewModel, hasSameStateAsOrderFormFullViewModel(expectedViewModel));
  }
  
  @Test
  public void testConvertEntityToOrderFormViewModel_shouldReturnExpectedViewModel() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate = new Date();
    UUID orderFormId = UUID.randomUUID();

    OrderFormViewModel expectedViewModel = anOrderFormViewModel()
        .withDispatchedFrom(new LocationFullViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationFullViewModel(dispatchedTo))
        .withOrderDate(orderDate).withId(orderFormId).build();

    OrderForm entity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).withId(orderFormId).build();

    // Setup mock
    when(locationFactory.createFullViewModel(dispatchedFrom)).thenReturn(expectedViewModel.getDispatchedFrom());
    when(locationFactory.createFullViewModel(dispatchedTo)).thenReturn(expectedViewModel.getDispatchedTo());

    OrderFormViewModel convertedViewModel = orderFormFactory.createViewModel(entity);

    assertThat(convertedViewModel, hasSameStateAsOrderFormViewModel(expectedViewModel));
  }
  
  @Test
  public void testConvertEntitiesToOrderFormViewModels_shouldReturnExpectedViewModels() {
    UUID uuid = UUID.randomUUID();
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Patient patient = aPatient().withId(uuid).build();
    Date orderDate1 = new Date();
    Date orderDate2 = new Date();
    PatientViewModel patientViewModel = aPatientViewModel().withId(uuid).build();
    UUID orderFormId1 = UUID.randomUUID();
    UUID orderFormId2 = UUID.randomUUID();

    OrderFormViewModel expectedViewModel1 = anOrderFormViewModel()
        .withDispatchedFrom(new LocationFullViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationFullViewModel(dispatchedTo))
        .withPatient(patientViewModel)
        .withOrderDate(orderDate1).withId(orderFormId1).build();
    
    OrderFormViewModel expectedViewModel2 = anOrderFormViewModel()
        .withDispatchedFrom(new LocationFullViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationFullViewModel(dispatchedTo))
        .withOrderDate(orderDate2).withId(orderFormId2).build();

    OrderForm entity1 = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate1).withId(orderFormId1)
        .withPatient(patient).build();
    
    OrderForm entity2 = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate2).withId(orderFormId2).build();
    
    // Setup mock
    when(locationFactory.createFullViewModel(dispatchedFrom)).thenReturn(expectedViewModel1.getDispatchedFrom());
    when(locationFactory.createFullViewModel(dispatchedTo)).thenReturn(expectedViewModel1.getDispatchedTo());
    when(patientFactory.createViewModel(patient)).thenReturn(patientViewModel);

    List<OrderFormViewModel> convertedViewModels = orderFormFactory.createViewModels(Arrays.asList(entity1, entity2));

    assertThat("2 OrderFormViewModels returned", convertedViewModels.size() == 2);
    assertThat(convertedViewModels.get(0), hasSameStateAsOrderFormViewModel(expectedViewModel1));
    assertThat(convertedViewModels.get(1), hasSameStateAsOrderFormViewModel(expectedViewModel2));
  }
  
  @Test
  public void testConvertPatientRequestEntitiesToOrderFormViewModel_shouldReturnExpectedViewModel() {
    UUID uuid = UUID.randomUUID();
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate1 = new Date();
    UUID orderFormId = UUID.randomUUID();

    OrderFormViewModel expectedViewModel =
        anOrderFormViewModel()
        .withDispatchedFrom(new LocationFullViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationFullViewModel(dispatchedTo))
        .withOrderType(OrderType.PATIENT_REQUEST)
        .withPatient(aPatientViewModel().withId(uuid).build())
        .withOrderDate(orderDate1).withId(orderFormId).build();

    OrderForm entity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate1).withId(orderFormId)
        .withOrderType(OrderType.PATIENT_REQUEST)
        .withPatient(aPatient().withId(uuid).build()).build();
    
    // Setup mock
    when(locationFactory.createFullViewModel(dispatchedFrom)).thenReturn(expectedViewModel.getDispatchedFrom());
    when(locationFactory.createFullViewModel(dispatchedTo)).thenReturn(expectedViewModel.getDispatchedTo());
    when(patientFactory.createViewModel(entity.getPatient())).thenReturn(expectedViewModel.getPatient());

    OrderFormViewModel convertedViewModel = orderFormFactory.createViewModel(entity);

    assertThat(convertedViewModel, hasSameStateAsOrderFormViewModel(expectedViewModel));
  }
  
  @Test
  public void testConvertPatientRequestOrderFormBackingFormToOrderFormEntity_shouldReturnExpectedEntity() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate = new Date();

    OrderForm expectedEntity = anOrderForm().withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).build();
    
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withDispatchedFrom(getBaseDispatchedFromLocationBackingForm())
        .withDispatchedTo(getBaseDispatchedToLocationBackingForm()).withOrderDate(orderDate)
        .withPatient(aPatientBackingForm().build()).build();

    // Setup mock
    when(locationRepository.getLocation(locationId1)).thenReturn(dispatchedFrom);
    when(locationRepository.getLocation(locationId2)).thenReturn(dispatchedTo);
    when(patientFactory.createEntity(backingForm.getPatient())).thenReturn(aPatient().build());

    OrderForm convertedEntity = orderFormFactory.createEntity(backingForm);
   
    assertThat(convertedEntity, hasSameStateAsOrderForm(expectedEntity));
  }
}
