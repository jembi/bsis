package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.OrderFormBuilder.anOrderForm;
import static org.jembi.bsis.helpers.builders.OrderFormItemBuilder.anOrderItemForm;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderFormItem;
import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;
import org.jembi.bsis.repository.OrderFormRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class OrderFormCRUDServiceTests extends UnitTestSuite {
  
  private static final Long ORDER_FORM_ID = 17L;
  
  @InjectMocks
  private OrderFormCRUDService orderFormCRUDService;
  
  @Mock
  private OrderFormRepository orderFormRepository;
  
  @Mock
  private ComponentDispatchService componentDispatchService;
  
  @Mock
  private OrderFormConstraintChecker orderFormConstraintChecker;
  
  @Test
  public void testUpdateOrderFormWithoutStatusChange_shouldUpdateFieldsCorrectly() {
    // Fixture
    Date createdDate = new Date();
    Date orderDate = new Date();
    Location dispatchedFrom = aDistributionSite().build();
    Location dispatchedTo = aDistributionSite().build();
    
    OrderForm existingOrderForm = anOrderForm().withId(ORDER_FORM_ID).withCreatedDate(createdDate).build();
    OrderForm orderForm = anOrderForm()
        .withId(ORDER_FORM_ID)
        .withOrderDate(orderDate)
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .build();
    OrderForm expectedOrderForm = anOrderForm()
        .withId(ORDER_FORM_ID)
        .withCreatedDate(createdDate)
        .withOrderDate(orderDate)
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .build();
    
    // Expectations
    when(orderFormRepository.findById(ORDER_FORM_ID)).thenReturn(existingOrderForm);
    when(orderFormRepository.update(existingOrderForm)).thenReturn(existingOrderForm);
    when(orderFormConstraintChecker.canEdit(existingOrderForm)).thenReturn(true);
    
    // Test
    OrderForm returnedOrderForm = orderFormCRUDService.updateOrderForm(orderForm);
    
    // Assertions
    assertThat(returnedOrderForm, is(expectedOrderForm));
  }
  
  @Test
  public void testUpdateTransferOrderForm_shouldUpdateFieldsCorrectly() {
    // Fixture
    Date createdDate = new Date();
    Date orderDate = new Date();
    Location dispatchedFrom = aDistributionSite().build();
    Location dispatchedTo = aDistributionSite().build();
    OrderFormItem orderFormItem = anOrderItemForm().withId(7L).build();
    Component component = aComponent().build();
    
    OrderForm existingOrderForm = anOrderForm().withId(ORDER_FORM_ID).withCreatedDate(createdDate).build();
    OrderForm orderForm = anOrderForm()
        .withId(ORDER_FORM_ID)
        .withOrderDate(orderDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.TRANSFER)
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderFormItems(Arrays.asList(orderFormItem))
        .withComponent(component)
        .build();
    OrderForm expectedOrderForm = anOrderForm()
        .withId(ORDER_FORM_ID)
        .withCreatedDate(createdDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderDate(orderDate)
        .withOrderType(OrderType.TRANSFER)
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withOrderFormItems(Arrays.asList(orderFormItem))
        .withComponent(component)
        .build();
    
    // Expectations
    when(orderFormRepository.findById(ORDER_FORM_ID)).thenReturn(existingOrderForm);
    when(orderFormRepository.update(existingOrderForm)).thenReturn(existingOrderForm);
    when(orderFormConstraintChecker.canDispatch(existingOrderForm)).thenReturn(true);
    when(orderFormConstraintChecker.canEdit(existingOrderForm)).thenReturn(true);
    
    // Test
    OrderForm returnedOrderForm = orderFormCRUDService.updateOrderForm(orderForm);
    
    // Assertions
    assertThat(returnedOrderForm, is(expectedOrderForm));
    verify(componentDispatchService).transferComponent(component, dispatchedTo);
  }
  
  @Test
  public void testUpdateIssueOrderForm_shouldUpdateFieldsCorrectly() {
    // Fixture
    Date createdDate = new Date();
    Location dispatchedFrom = aDistributionSite().build();
    Location dispatchedTo = aUsageSite().build();
    Component component = aComponent().build();
    
    OrderForm existingOrderForm = anOrderForm().withId(ORDER_FORM_ID).withCreatedDate(createdDate).build();
    OrderForm orderForm = anOrderForm()
        .withId(ORDER_FORM_ID)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withComponent(component)
        .build();
    OrderForm expectedOrderForm = anOrderForm()
        .withId(ORDER_FORM_ID)
        .withCreatedDate(createdDate)
        .withOrderStatus(OrderStatus.DISPATCHED)
        .withOrderType(OrderType.ISSUE)
        .withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo)
        .withComponent(component)
        .build();

    // Expectations
    when(orderFormRepository.findById(ORDER_FORM_ID)).thenReturn(existingOrderForm);
    when(orderFormRepository.update(existingOrderForm)).thenReturn(existingOrderForm);
    when(orderFormConstraintChecker.canDispatch(existingOrderForm)).thenReturn(true);
    when(orderFormConstraintChecker.canEdit(existingOrderForm)).thenReturn(true);
    
    // Test
    OrderForm returnedOrderForm = orderFormCRUDService.updateOrderForm(orderForm);
    
    // Assertions
    assertThat(returnedOrderForm, is(expectedOrderForm));
    verify(componentDispatchService).issueComponent(component, dispatchedTo);
  }

  @Test(expected = IllegalStateException.class)
  public void testUpdatePreviouslyDispatchedOrderForm_shouldThrow() {
    // Fixture
    OrderForm orderForm = anOrderForm().withId(ORDER_FORM_ID).build();
    OrderForm existingOrderForm = anOrderForm().withId(ORDER_FORM_ID).withOrderStatus(OrderStatus.DISPATCHED).build();

    // Expectations
    when(orderFormRepository.findById(ORDER_FORM_ID)).thenReturn(existingOrderForm);

    // Test
    orderFormCRUDService.updateOrderForm(orderForm);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testDispatchNotDispatchableOrderForm_shouldThrow() {
    // Fixture
    OrderForm existingOrderForm = anOrderForm().withId(ORDER_FORM_ID).build();
    OrderForm orderForm = anOrderForm().withId(ORDER_FORM_ID).withOrderStatus(OrderStatus.DISPATCHED).build();

    // Expectations
    when(orderFormRepository.findById(ORDER_FORM_ID)).thenReturn(existingOrderForm);
    when(orderFormConstraintChecker.canEdit(existingOrderForm)).thenReturn(true);
    when(orderFormConstraintChecker.canDispatch(existingOrderForm)).thenReturn(false);

    // Test
    orderFormCRUDService.updateOrderForm(orderForm);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testUpdateNonEditableOrderForm_shouldThrow() {
    // Fixture
    OrderForm existingOrderForm = anOrderForm().withId(ORDER_FORM_ID).withOrderStatus(OrderStatus.DISPATCHED).build();
    OrderForm orderForm = anOrderForm().withId(ORDER_FORM_ID).withOrderStatus(OrderStatus.DISPATCHED).build();

    // Expectations
    when(orderFormRepository.findById(ORDER_FORM_ID)).thenReturn(existingOrderForm);
    when(orderFormConstraintChecker.canEdit(existingOrderForm)).thenReturn(false);

    // Test
    orderFormCRUDService.updateOrderForm(orderForm);
  }

  @Test
  public void testDeleteOrderForm_shouldMarkAsDeleted() {
    // Data
    OrderForm existingOrderForm = anOrderForm().withId(ORDER_FORM_ID).withIsDeleted(false).build();
    OrderForm expectedOrderForm = anOrderForm().withId(ORDER_FORM_ID).withIsDeleted(true).build();
    
    // Mocks
    when(orderFormRepository.findById(ORDER_FORM_ID)).thenReturn(existingOrderForm);
    when(orderFormRepository.update(expectedOrderForm)).thenReturn(expectedOrderForm);
    when(orderFormConstraintChecker.canDispatch(existingOrderForm)).thenReturn(false);
    
    // Test
    OrderForm returnedOrderForm = orderFormCRUDService.deleteOrderForm(ORDER_FORM_ID);
    
    // Assertions
    assertThat(returnedOrderForm, is(expectedOrderForm));
  }
}
