package service;

import static helpers.builders.LocationBuilder.aDistributionSite;
import static helpers.builders.OrderFormBackingFormBuilder.anOrderFormBackingForm;
import static helpers.builders.OrderFormBuilder.anOrderForm;
import static helpers.builders.OrderFormItemBuilder.anOrderItemForm;
import static helpers.matchers.OrderFormMatcher.hasSameStateAsOrderForm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import backingform.OrderFormBackingForm;
import factory.OrderFormFactory;
import model.location.Location;
import model.order.OrderForm;
import model.order.OrderFormItem;
import model.order.OrderStatus;
import model.order.OrderType;
import repository.OrderFormRepository;
import suites.UnitTestSuite;

public class OrderFormCRUDServiceTests extends UnitTestSuite {
  
  private static final Long ORDER_FORM_ID = 17L;
  
  @InjectMocks
  private OrderFormCRUDService orderFormCRUDService;
  
  @Mock
  private OrderFormFactory orderFormFactory;
  
  @Mock
  private OrderFormRepository orderFormRepository;
  
  @Test
  public void testUpdateOrderForm_shouldUpdateFieldsCorrectly() {
    // Fixture
    Date orderDate = new Date();
    Location dispatchedFrom = aDistributionSite().build();
    Location dispatchedTo = aDistributionSite().build();
    List<OrderFormItem> orderFormItems = Arrays.asList(anOrderItemForm().build(), anOrderItemForm().build());
    
    OrderForm existingOrderForm = anOrderForm().withId(ORDER_FORM_ID).build();
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withId(ORDER_FORM_ID).build();
    OrderForm orderFormCreatedFromBackingForm = anOrderForm()
        .withId(ORDER_FORM_ID)
        .withOrderDate(orderDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.TRANSFER)
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderFormItems(orderFormItems)
        .build();
    
    // Expectations
    when(orderFormRepository.findById(ORDER_FORM_ID)).thenReturn(existingOrderForm);
    when(orderFormFactory.createEntity(backingForm)).thenReturn(orderFormCreatedFromBackingForm);
    when(orderFormRepository.update(existingOrderForm)).thenReturn(existingOrderForm);

    OrderForm expectedOrderForm = anOrderForm()
        .withId(ORDER_FORM_ID)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderDate(orderDate)
        .withOrderType(OrderType.TRANSFER)
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderFormItems(orderFormItems)
        .build();
    
    // Test
    OrderForm updatedOrderForm = orderFormCRUDService.updateOrderForm(backingForm);
    
    // Assertions
    assertThat(updatedOrderForm, hasSameStateAsOrderForm(expectedOrderForm));
  }

}
