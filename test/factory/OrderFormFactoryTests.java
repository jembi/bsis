package factory;

import static helpers.builders.LocationBuilder.aDistributionSite;
import static helpers.builders.OrderFormBackingFormBuilder.anOrderFormBackingForm;
import static helpers.builders.OrderFormBuilder.anOrderForm;
import static helpers.builders.OrderFormItemBackingFormBuilder.anOrderFormItemBackingForm;
import static helpers.builders.OrderFormItemBuilder.anOrderItemForm;
import static helpers.builders.OrderFormItemViewModelBuilder.anOrderFormItemViewModel;
import static helpers.builders.OrderFormViewModelBuilder.anOrderFormViewModel;
import static helpers.matchers.OrderFormMatcher.hasSameStateAsOrderForm;
import static helpers.matchers.OrderFormViewModelMatcher.hasSameStateAsOrderFormViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Date;

import model.location.Location;
import model.order.OrderForm;
import model.order.OrderFormItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import repository.LocationRepository;
import viewmodel.LocationViewModel;
import viewmodel.OrderFormItemViewModel;
import viewmodel.OrderFormViewModel;
import backingform.LocationBackingForm;
import backingform.OrderFormBackingForm;
import backingform.OrderFormItemBackingForm;

@RunWith(MockitoJUnitRunner.class)
public class OrderFormFactoryTests {

  @InjectMocks
  private OrderFormFactory orderFormFactory;

  @Mock
  private LocationRepository locationRepository;
  
  @Mock
  private OrderFormItemFactory orderFormItemFactory;

  @Test
  public void testConvertOrderFormBackingFormToOrderFormEntity_shouldReturnExpectedEntity() {
    Location dispatchedFrom = aDistributionSite().withName("LocFrom").withId(1l).build();
    Location dispatchedTo = aDistributionSite().withName("LocTo").withId(2l).build();
    Date orderDate = new Date();

    OrderForm expectedEntity = anOrderForm().withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).build();
    
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withDispatchedFrom(new LocationBackingForm(dispatchedFrom))
        .withDispatchedTo(new LocationBackingForm(dispatchedTo)).withOrderDate(orderDate).build();

    // Setup mock
    when(locationRepository.getLocation(1l)).thenReturn(dispatchedFrom);
    when(locationRepository.getLocation(2l)).thenReturn(dispatchedTo);

    OrderForm convertedEntity = orderFormFactory.createEntity(backingForm);
   
    assertThat(convertedEntity, hasSameStateAsOrderForm(expectedEntity));
  }
  
  @Test
  public void testConvertOrderFormBackingFormToOrderFormEntityWithItems_shouldReturnExpectedEntity() {
    Location dispatchedFrom = aDistributionSite().withName("LocFrom").withId(1l).build();
    Location dispatchedTo = aDistributionSite().withName("LocTo").withId(2l).build();
    Date orderDate = new Date();

    OrderFormItem expectedItem1 = anOrderItemForm().withId(1L).withBloodAbo("A").withBloodRh("+").build();
    OrderFormItem expectedItem2 = anOrderItemForm().withId(2L).withBloodAbo("A").withBloodRh("+").build();
    OrderForm expectedEntity = anOrderForm().withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).withOrderFormItem(expectedItem1).withOrderFormItem(expectedItem2).build();
    
    OrderFormItemBackingForm item1 = anOrderFormItemBackingForm().withId(1L).withBloodAbo("A").withBloodRh("+").build();
    OrderFormItemBackingForm item2 = anOrderFormItemBackingForm().withId(2L).withBloodAbo("B").withBloodRh("+").build();
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withDispatchedFrom(new LocationBackingForm(dispatchedFrom))
        .withDispatchedTo(new LocationBackingForm(dispatchedTo)).withOrderDate(orderDate)
        .withItem(item1).withItem(item2).build();

    // Setup mock
    when(locationRepository.getLocation(1l)).thenReturn(dispatchedFrom);
    when(locationRepository.getLocation(2l)).thenReturn(dispatchedTo);

    OrderForm convertedEntity = orderFormFactory.createEntity(backingForm);
   
    assertThat(convertedEntity, hasSameStateAsOrderForm(expectedEntity));
  }

  @Test
  public void testConvertEntityToOrderFormViewModel_shouldReturnExpectedViewModel() {
    Location dispatchedFrom = aDistributionSite().withName("LocFrom").build();
    Location dispatchedTo = aDistributionSite().withName("LocTo").build();
    Date orderDate = new Date();

    OrderFormViewModel expectedViewModel = anOrderFormViewModel().withDispatchedFrom(new LocationViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationViewModel(dispatchedTo)).withOrderDate(orderDate).withId(1L).build();

    OrderForm entity = anOrderForm().withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).withId(1L).build();

    OrderFormViewModel convertedViewModel = orderFormFactory.createViewModel(entity);

    assertThat(convertedViewModel, hasSameStateAsOrderFormViewModel(expectedViewModel));
  }

  @Test
  public void testConvertEntityToOrderFormViewModelWithItems_shouldReturnExpectedViewModel() {
    Location dispatchedFrom = aDistributionSite().withName("LocFrom").build();
    Location dispatchedTo = aDistributionSite().withName("LocTo").build();
    Date orderDate = new Date();

    OrderFormItemViewModel expectedItem1 = anOrderFormItemViewModel().withBloodAbo("A").withBloodRh("+").build();
    OrderFormItemViewModel expectedItem2 = anOrderFormItemViewModel().withBloodAbo("B").withBloodRh("+").build();
    OrderFormViewModel expectedViewModel = anOrderFormViewModel().withDispatchedFrom(new LocationViewModel(dispatchedFrom))
        .withDispatchedTo(new LocationViewModel(dispatchedTo)).withOrderDate(orderDate).withId(1L)
        .withItem(expectedItem1).withItem(expectedItem2).build();

    OrderFormItem item1 = anOrderItemForm().withBloodAbo("A").withBloodRh("+").build();
    OrderFormItem item2 = anOrderItemForm().withBloodAbo("A").withBloodRh("+").build();
    OrderForm entity = anOrderForm().withDispatchedFrom(dispatchedFrom).withDispatchedTo(dispatchedTo)
        .withOrderDate(orderDate).withId(1L).withOrderFormItem(item1).withOrderFormItem(item2).build();

    OrderFormViewModel convertedViewModel = orderFormFactory.createViewModel(entity);

    assertThat(convertedViewModel, hasSameStateAsOrderFormViewModel(expectedViewModel));
  }
}
