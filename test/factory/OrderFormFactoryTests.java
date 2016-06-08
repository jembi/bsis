package factory;

import static helpers.builders.ComponentBackingFormBuilder.aComponentBackingForm;
import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.LocationBackingFormBuilder.aDistributionSiteBackingForm;
import static helpers.builders.LocationBuilder.aDistributionSite;
import static helpers.builders.OrderFormBackingFormBuilder.anOrderFormBackingForm;
import static helpers.builders.OrderFormBuilder.anOrderForm;
import static helpers.builders.OrderFormFullViewModelBuilder.anOrderFormFullViewModel;
import static helpers.builders.OrderFormItemBackingFormBuilder.anOrderFormItemBackingForm;
import static helpers.builders.OrderFormItemBuilder.anOrderItemForm;
import static helpers.builders.OrderFormItemViewModelBuilder.anOrderFormItemViewModel;
import static helpers.builders.OrderFormViewModelBuilder.anOrderFormViewModel;
import static helpers.matchers.OrderFormFullViewModelMatcher.hasSameStateAsOrderFormFullViewModel;
import static helpers.matchers.OrderFormMatcher.hasSameStateAsOrderForm;
import static helpers.matchers.OrderFormViewModelMatcher.hasSameStateAsOrderFormViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import backingform.ComponentBackingForm;
import backingform.LocationBackingForm;
import backingform.OrderFormBackingForm;
import backingform.OrderFormItemBackingForm;
import model.component.Component;
import model.inventory.InventoryStatus;
import model.location.Location;
import model.order.OrderForm;
import model.order.OrderFormItem;
import repository.ComponentRepository;
import repository.LocationRepository;
import service.OrderFormConstraintChecker;
import viewmodel.OrderFormFullViewModel;
import viewmodel.OrderFormItemViewModel;
import viewmodel.OrderFormViewModel;

@RunWith(MockitoJUnitRunner.class)
public class OrderFormFactoryTests {

  @InjectMocks
  private OrderFormFactory orderFormFactory;

  @Mock
  private LocationRepository locationRepository;
  
  @Mock
  private OrderFormItemFactory orderFormItemFactory;

  @Mock
  private ComponentViewModelFactory componentViewModelFactory;
  
  @Mock
  private LocationViewModelFactory locationViewModelFactory;

  @Mock
  private ComponentRepository componentRepository;

  @Mock
  private OrderFormConstraintChecker orderFormConstraintChecker;
 
  private Location getBaseDispatchedFromLocation() {
    return aDistributionSite().withName("LocFrom").withId(1l).build();
  }

  private Location getBaseDispatchedToLocation() {
    return aDistributionSite().withName("LocTo").withId(2l).build();
  }

  private LocationBackingForm getBaseDispatchedFromLocationBackingForm() {
    return aDistributionSiteBackingForm().withName("LocFrom").withId(1l).build();
  }

  private LocationBackingForm getBaseDispatchedToLocationBackingForm() {
    return aDistributionSiteBackingForm().withName("LocTo").withId(2l).build();
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
    when(locationRepository.getLocation(1l)).thenReturn(dispatchedFrom);
    when(locationRepository.getLocation(2l)).thenReturn(dispatchedTo);

    OrderForm convertedEntity = orderFormFactory.createEntity(backingForm);
   
    assertThat(convertedEntity, hasSameStateAsOrderForm(expectedEntity));
  }
  
  @Test
  public void testConvertOrderFormBackingFormToOrderFormEntityWithItems_shouldReturnExpectedEntity() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate = new Date();

    OrderFormItem expectedItem1 = anOrderItemForm().withId(1L).withBloodAbo("A").withBloodRh("+").build();
    OrderForm expectedEntity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate)
        .withOrderFormItem(expectedItem1).build();
    
    OrderFormItemBackingForm item1 = anOrderFormItemBackingForm().withId(1L).withBloodGroup("A+").build();
    OrderFormBackingForm backingForm = anOrderFormBackingForm()
        .withDispatchedFrom(getBaseDispatchedFromLocationBackingForm())
        .withDispatchedTo(getBaseDispatchedToLocationBackingForm())
        .withOrderDate(orderDate)
        .withItem(item1).build();

    // Setup mock
    when(locationRepository.getLocation(1l)).thenReturn(dispatchedFrom);
    when(locationRepository.getLocation(2l)).thenReturn(dispatchedTo);
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

    OrderFormFullViewModel expectedViewModel = anOrderFormFullViewModel()
        .withDispatchedFrom(locationViewModelFactory.createLocationViewModel(dispatchedFrom))
        .withDispatchedTo(locationViewModelFactory.createLocationViewModel(dispatchedTo))
        .withOrderDate(orderDate).withPermission("canDispatch", true).withPermission("canEdit", true)
        .withId(1L).build();

    OrderForm entity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).withId(1L).build();

    // Setup mocks
    when(orderFormConstraintChecker.canDispatch(entity)).thenReturn(true);
    when(orderFormConstraintChecker.canEdit(entity)).thenReturn(true);

    OrderFormFullViewModel convertedViewModel = orderFormFactory.createFullViewModel(entity);

    assertThat(convertedViewModel, hasSameStateAsOrderFormFullViewModel(expectedViewModel));
  }

  @Test
  public void testConvertEntityToOrderFormFullViewModelWithItems_shouldReturnExpectedViewModel() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate = new Date();

    OrderFormItemViewModel expectedItem1 = anOrderFormItemViewModel().withBloodGroup("A+").build();
    OrderFormItemViewModel expectedItem2 = anOrderFormItemViewModel().withBloodGroup("B+").build();
    OrderFormFullViewModel expectedViewModel = anOrderFormFullViewModel()
        .withDispatchedFrom(locationViewModelFactory.createLocationViewModel(dispatchedFrom))
        .withDispatchedTo(locationViewModelFactory.createLocationViewModel(dispatchedTo))
        .withPermission("canDispatch", true).withPermission("canEdit", true)
        .withOrderDate(orderDate).withId(1L)
        .withItem(expectedItem1).withItem(expectedItem2).build();

    OrderFormItem item1 = anOrderItemForm().withBloodAbo("A").withBloodRh("+").build();
    OrderFormItem item2 = anOrderItemForm().withBloodAbo("A").withBloodRh("+").build();
    OrderForm entity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate)
        .withId(1L).withOrderFormItem(item1).withOrderFormItem(item2).build();
    
    // Setup mocks
    when(orderFormItemFactory.createViewModel(item1)).thenReturn(expectedItem1);
    when(orderFormItemFactory.createViewModel(item2)).thenReturn(expectedItem2);
    when(orderFormConstraintChecker.canDispatch(entity)).thenReturn(true);
    when(orderFormConstraintChecker.canEdit(entity)).thenReturn(true);

    OrderFormFullViewModel convertedViewModel = orderFormFactory.createFullViewModel(entity);

    assertThat(convertedViewModel, hasSameStateAsOrderFormFullViewModel(expectedViewModel));
  }
  
  @Test
  public void testConvertOrderFormBackingFormToOrderFormEntityWithComponents_shouldReturnExpectedEntity() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate = new Date();

    Component expectedComponent =
        aComponent().withInventoryStatus(InventoryStatus.IN_STOCK)
        .withLocation(dispatchedFrom).withId(1L).build();
    ComponentBackingForm componentBackingForm = aComponentBackingForm().withId(1L).build();

    OrderForm expectedEntity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).withComponent(expectedComponent).build();

    OrderFormBackingForm backingForm = anOrderFormBackingForm()
        .withDispatchedFrom(getBaseDispatchedFromLocationBackingForm())
        .withDispatchedTo(getBaseDispatchedToLocationBackingForm())
        .withOrderDate(orderDate).withComponent(componentBackingForm).build();

    // Setup mock
    when(locationRepository.getLocation(1l)).thenReturn(dispatchedFrom);
    when(locationRepository.getLocation(2l)).thenReturn(dispatchedTo);
    when(componentRepository.findComponent(1L)).thenReturn(expectedComponent);

    OrderForm convertedEntity = orderFormFactory.createEntity(backingForm);

    assertThat(convertedEntity, hasSameStateAsOrderForm(expectedEntity));
  }
  
  @Test
  public void testConvertEntityToOrderFormFullViewModelWithComponents_shouldReturnExpectedViewModel() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate = new Date();

    Component component = aComponent().withId(1L).withInventoryStatus(InventoryStatus.IN_STOCK).withLocation(dispatchedFrom).build();
    OrderFormFullViewModel expectedViewModel = anOrderFormFullViewModel()
        .withDispatchedFrom(locationViewModelFactory.createLocationViewModel(dispatchedFrom))
        .withDispatchedTo(locationViewModelFactory.createLocationViewModel(dispatchedTo))
        .withPermission("canDispatch", true).withPermission("canEdit", true)
        .withOrderDate(orderDate).withId(1L)
        .withComponent(componentViewModelFactory.createComponentViewModel(component)).build();

    OrderForm entity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate)
        .withId(1L).withComponent(component).build();

    // Setup mocks
    when(orderFormConstraintChecker.canDispatch(entity)).thenReturn(true);
    when(orderFormConstraintChecker.canEdit(entity)).thenReturn(true);

    OrderFormFullViewModel convertedViewModel = orderFormFactory.createFullViewModel(entity);

    assertThat(convertedViewModel, hasSameStateAsOrderFormFullViewModel(expectedViewModel));
  }
  
  @Test
  public void testConvertEntityToOrderFormViewModel_shouldReturnExpectedViewModel() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate = new Date();

    OrderFormViewModel expectedViewModel = anOrderFormViewModel()
        .withDispatchedFrom(locationViewModelFactory.createLocationViewModel(dispatchedFrom))
        .withDispatchedTo(locationViewModelFactory.createLocationViewModel(dispatchedTo))
        .withOrderDate(orderDate).withId(1L).build();

    OrderForm entity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).withId(1L).build();

    OrderFormViewModel convertedViewModel = orderFormFactory.createViewModel(entity);

    assertThat(convertedViewModel, hasSameStateAsOrderFormViewModel(expectedViewModel));
  }
  
  @Test
  public void testConvertEntitiesToOrderFormViewModels_shouldReturnExpectedViewModels() {
    Location dispatchedFrom = getBaseDispatchedFromLocation();
    Location dispatchedTo = getBaseDispatchedToLocation();
    Date orderDate1 = new Date();
    Date orderDate2 = new Date();

    OrderFormViewModel expectedViewModel1 = anOrderFormViewModel()
        .withDispatchedFrom(locationViewModelFactory.createLocationViewModel(dispatchedFrom))
        .withDispatchedTo(locationViewModelFactory.createLocationViewModel(dispatchedTo))
        .withOrderDate(orderDate1).withId(1L).build();
    
    OrderFormViewModel expectedViewModel2 = anOrderFormViewModel()
        .withDispatchedFrom(locationViewModelFactory.createLocationViewModel(dispatchedFrom))
        .withDispatchedTo(locationViewModelFactory.createLocationViewModel(dispatchedTo))
        .withOrderDate(orderDate2).withId(2L).build();

    OrderForm entity1 = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate1).withId(1L).build();
    
    OrderForm entity2 = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate2).withId(2L).build();

    List<OrderFormViewModel> convertedViewModels = orderFormFactory.createViewModels(Arrays.asList(entity1, entity2));

    assertThat("2 OrderFormViewModels returned", convertedViewModels.size() == 2);
    assertThat(convertedViewModels.get(0), hasSameStateAsOrderFormViewModel(expectedViewModel1));
    assertThat(convertedViewModels.get(1), hasSameStateAsOrderFormViewModel(expectedViewModel2));
  }
}
