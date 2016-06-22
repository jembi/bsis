package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.ComponentBackingFormBuilder.aComponentBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aDistributionSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.OrderFormBackingFormBuilder.anOrderFormBackingForm;
import static org.jembi.bsis.helpers.builders.OrderFormBuilder.anOrderForm;
import static org.jembi.bsis.helpers.builders.OrderFormFullViewModelBuilder.anOrderFormFullViewModel;
import static org.jembi.bsis.helpers.builders.OrderFormItemBackingFormBuilder.anOrderFormItemBackingForm;
import static org.jembi.bsis.helpers.builders.OrderFormItemBuilder.anOrderItemForm;
import static org.jembi.bsis.helpers.builders.OrderFormItemViewModelBuilder.anOrderFormItemViewModel;
import static org.jembi.bsis.helpers.builders.OrderFormViewModelBuilder.anOrderFormViewModel;
import static org.jembi.bsis.helpers.matchers.OrderFormFullViewModelMatcher.hasSameStateAsOrderFormFullViewModel;
import static org.jembi.bsis.helpers.matchers.OrderFormMatcher.hasSameStateAsOrderForm;
import static org.jembi.bsis.helpers.matchers.OrderFormViewModelMatcher.hasSameStateAsOrderFormViewModel;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.OrderFormBackingForm;
import org.jembi.bsis.backingform.OrderFormItemBackingForm;
import org.jembi.bsis.factory.ComponentViewModelFactory;
import org.jembi.bsis.factory.LocationViewModelFactory;
import org.jembi.bsis.factory.OrderFormFactory;
import org.jembi.bsis.factory.OrderFormItemFactory;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderFormItem;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.OrderFormConstraintChecker;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.OrderFormFullViewModel;
import org.jembi.bsis.viewmodel.OrderFormItemViewModel;
import org.jembi.bsis.viewmodel.OrderFormViewModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

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
        .withDispatchedFrom(new LocationViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationViewModel(dispatchedTo))
        .withOrderDate(orderDate).withPermission("canDispatch", true).withPermission("canEdit", true)
        .withId(1L).build();

    OrderForm entity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).withId(1L).build();

    // Setup mock
    when(locationViewModelFactory.createLocationViewModel(dispatchedFrom)).thenReturn(expectedViewModel.getDispatchedFrom());
    when(locationViewModelFactory.createLocationViewModel(dispatchedTo)).thenReturn(expectedViewModel.getDispatchedTo());
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
        .withDispatchedFrom(new LocationViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationViewModel(dispatchedTo))
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

    // Setup mock
    when(locationViewModelFactory.createLocationViewModel(dispatchedFrom)).thenReturn(expectedViewModel.getDispatchedFrom());
    when(locationViewModelFactory.createLocationViewModel(dispatchedTo)).thenReturn(expectedViewModel.getDispatchedTo());
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
        .withDispatchedFrom(new LocationViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationViewModel(dispatchedTo))
        .withPermission("canDispatch", true).withPermission("canEdit", true)
        .withOrderDate(orderDate).withId(1L)
        .withComponent(new ComponentViewModel(component)).build();

    OrderForm entity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate)
        .withId(1L).withComponent(component).build();
    
    // Setup mock
    when(componentViewModelFactory.createComponentViewModels(entity.getComponents())).thenReturn(expectedViewModel.getComponents());
    when(locationViewModelFactory.createLocationViewModel(dispatchedFrom)).thenReturn(expectedViewModel.getDispatchedFrom());
    when(locationViewModelFactory.createLocationViewModel(dispatchedTo)).thenReturn(expectedViewModel.getDispatchedTo());
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
        .withDispatchedFrom(new LocationViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationViewModel(dispatchedTo))
        .withOrderDate(orderDate).withId(1L).build();

    OrderForm entity = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).withId(1L).build();

    // Setup mock
    when(locationViewModelFactory.createLocationViewModel(dispatchedFrom)).thenReturn(expectedViewModel.getDispatchedFrom());
    when(locationViewModelFactory.createLocationViewModel(dispatchedTo)).thenReturn(expectedViewModel.getDispatchedTo());

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
        .withDispatchedFrom(new LocationViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationViewModel(dispatchedTo))
        .withOrderDate(orderDate1).withId(1L).build();
    
    OrderFormViewModel expectedViewModel2 = anOrderFormViewModel()
        .withDispatchedFrom(new LocationViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationViewModel(dispatchedTo))
        .withOrderDate(orderDate2).withId(2L).build();

    OrderForm entity1 = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate1).withId(1L).build();
    
    OrderForm entity2 = anOrderForm()
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate2).withId(2L).build();
    
    // Setup mock
    when(locationViewModelFactory.createLocationViewModel(dispatchedFrom)).thenReturn(expectedViewModel1.getDispatchedFrom());
    when(locationViewModelFactory.createLocationViewModel(dispatchedTo)).thenReturn(expectedViewModel1.getDispatchedTo());

    List<OrderFormViewModel> convertedViewModels = orderFormFactory.createViewModels(Arrays.asList(entity1, entity2));

    assertThat("2 OrderFormViewModels returned", convertedViewModels.size() == 2);
    assertThat(convertedViewModels.get(0), hasSameStateAsOrderFormViewModel(expectedViewModel1));
    assertThat(convertedViewModels.get(1), hasSameStateAsOrderFormViewModel(expectedViewModel2));
  }
}
