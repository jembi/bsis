package service;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.LocationBuilder.aDistributionSite;
import static helpers.builders.LocationBuilder.aUsageSite;
import static helpers.builders.OrderFormBackingFormBuilder.anOrderFormBackingForm;
import static helpers.builders.OrderFormBuilder.anOrderForm;
import static helpers.builders.OrderFormFullViewModelBuilder.anOrderFormFullViewModel;
import static helpers.builders.OrderFormItemBuilder.anOrderItemForm;
import static helpers.matchers.OrderFormMatcher.hasSameStateAsOrderForm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import backingform.OrderFormBackingForm;
import factory.OrderFormFactory;
import model.component.Component;
import model.location.Location;
import model.order.OrderForm;
import model.order.OrderFormItem;
import model.order.OrderStatus;
import model.order.OrderType;
import repository.OrderFormRepository;
import suites.UnitTestSuite;
import viewmodel.OrderFormFullViewModel;

public class OrderFormCRUDServiceTests extends UnitTestSuite {
  
  private static final Long ORDER_FORM_ID = 17L;
  
  @InjectMocks
  private OrderFormCRUDService orderFormCRUDService;
  
  @Mock
  private OrderFormFactory orderFormFactory;
  
  @Mock
  private OrderFormRepository orderFormRepository;
  
  @Mock
  private OrderFormItemCRUDService orderFormItemCRUDService;
  
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
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withId(ORDER_FORM_ID).build();
    OrderForm orderFormCreatedFromBackingForm = anOrderForm()
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
    OrderFormFullViewModel expectedViewModel = anOrderFormFullViewModel().withId(ORDER_FORM_ID).build();
    
    // Expectations
    when(orderFormRepository.findById(ORDER_FORM_ID)).thenReturn(existingOrderForm);
    when(orderFormFactory.createEntity(backingForm)).thenReturn(orderFormCreatedFromBackingForm);
    when(orderFormRepository.update(existingOrderForm)).thenReturn(existingOrderForm);
    when(orderFormFactory.createFullViewModel(argThat(hasSameStateAsOrderForm(expectedOrderForm)))).thenReturn(expectedViewModel);
    
    // Test
    OrderFormFullViewModel returnedViewModel = orderFormCRUDService.updateOrderForm(backingForm);
    
    // Assertions
    assertThat(returnedViewModel, is(expectedViewModel));
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
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withId(ORDER_FORM_ID).build();
    OrderForm orderFormCreatedFromBackingForm = anOrderForm()
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
    OrderFormFullViewModel expectedViewModel = anOrderFormFullViewModel().withId(ORDER_FORM_ID).build();
    
    // Expectations
    when(orderFormRepository.findById(ORDER_FORM_ID)).thenReturn(existingOrderForm);
    when(orderFormFactory.createEntity(backingForm)).thenReturn(orderFormCreatedFromBackingForm);
    when(orderFormItemCRUDService.createOrUpdateOrderFormItem(orderFormItem)).thenReturn(orderFormItem);
    when(orderFormRepository.update(existingOrderForm)).thenReturn(existingOrderForm);
    when(orderFormFactory.createFullViewModel(argThat(hasSameStateAsOrderForm(expectedOrderForm)))).thenReturn(expectedViewModel);
    when(orderFormConstraintChecker.canDispatch(expectedOrderForm)).thenReturn(true);
    
    // Test
    OrderFormFullViewModel returnedViewModel = orderFormCRUDService.updateOrderForm(backingForm);
    
    // Assertions
    assertThat(returnedViewModel, is(expectedViewModel));
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
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withId(ORDER_FORM_ID).build();
    OrderForm orderFormCreatedFromBackingForm = anOrderForm()
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
    OrderFormFullViewModel expectedViewModel = anOrderFormFullViewModel().withId(ORDER_FORM_ID).build();

    // Expectations
    when(orderFormRepository.findById(ORDER_FORM_ID)).thenReturn(existingOrderForm);
    when(orderFormFactory.createEntity(backingForm)).thenReturn(orderFormCreatedFromBackingForm);
    when(orderFormRepository.update(existingOrderForm)).thenReturn(existingOrderForm);
    when(orderFormFactory.createFullViewModel(argThat(hasSameStateAsOrderForm(expectedOrderForm)))).thenReturn(expectedViewModel);
    when(orderFormConstraintChecker.canDispatch(expectedOrderForm)).thenReturn(true);
    
    // Test
    OrderFormFullViewModel returnedViewModel = orderFormCRUDService.updateOrderForm(backingForm);
    
    // Assertions
    assertThat(returnedViewModel, is(expectedViewModel));
    verify(componentDispatchService).issueComponent(component, dispatchedTo);
  }

  @Test(expected = IllegalStateException.class)
  public void testUpdatePreviouslyDispatchedOrderForm_shouldThrow() {
    // Fixture
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withId(ORDER_FORM_ID).build();
    OrderForm existingOrderForm = anOrderForm().withId(ORDER_FORM_ID).withOrderStatus(OrderStatus.DISPATCHED).build();
    OrderForm orderFormCreatedFromBackingForm = anOrderForm().withId(ORDER_FORM_ID).build();

    // Expectations
    when(orderFormFactory.createEntity(backingForm)).thenReturn(orderFormCreatedFromBackingForm);
    when(orderFormRepository.findById(ORDER_FORM_ID)).thenReturn(existingOrderForm);

    // Test
    orderFormCRUDService.updateOrderForm(backingForm);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testDispatchPreviouslyDispatchedOrderForm_shouldThrow() {
    // Fixture
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withId(ORDER_FORM_ID).build();
    OrderForm existingOrderForm = anOrderForm().withId(ORDER_FORM_ID).withOrderStatus(OrderStatus.CREATED).build();
    OrderForm orderFormCreatedFromBackingForm = anOrderForm().withId(ORDER_FORM_ID).withOrderStatus(OrderStatus.DISPATCHED).build();

    // Expectations
    when(orderFormFactory.createEntity(backingForm)).thenReturn(orderFormCreatedFromBackingForm);
    when(orderFormRepository.findById(ORDER_FORM_ID)).thenReturn(existingOrderForm);
    when(orderFormConstraintChecker.canDispatch(existingOrderForm)).thenReturn(false);

    // Test
    orderFormCRUDService.updateOrderForm(backingForm);
  }

}
