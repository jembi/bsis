package service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import helpers.builders.ComponentBuilder;
import helpers.builders.OrderFormBuilder;
import model.component.Component;
import model.order.OrderForm;
import model.order.OrderStatus;

@RunWith(MockitoJUnitRunner.class)
public class OrderFormConstraintCheckerTests {

  @InjectMocks
  private OrderFormConstraintChecker orderFormConstraintChecker;

  @Test
  public void testCanDispatch_shouldReturnTrue() {
    Component component = ComponentBuilder.aComponent().build();
    OrderForm orderForm = OrderFormBuilder.anOrderForm().withComponent(component).withOrderStatus(OrderStatus.CREATED).build();

    // Verify
    Assert.assertTrue("Can dispatch", orderFormConstraintChecker.canDispatch(orderForm));
  }

  @Test
  public void testCanDispatchWrongStatus_shouldReturnFalse() {
    Component component = ComponentBuilder.aComponent().build();
    OrderForm orderForm = OrderFormBuilder.anOrderForm().withComponent(component).withOrderStatus(OrderStatus.DISPATCHED).build();

    // Verify
    Assert.assertFalse("Can't dispatch", orderFormConstraintChecker.canDispatch(orderForm));
  }

  @Test
  public void testCanDispatchNoComponents_shouldReturnFalse() {
    OrderForm orderForm = OrderFormBuilder.anOrderForm().withOrderStatus(OrderStatus.CREATED).build();

    // Verify
    Assert.assertFalse("Can't dispatch", orderFormConstraintChecker.canDispatch(orderForm));
  }
  
  @Test
  public void testCanDispatchNullComponents_shouldReturnFalse() {
    OrderForm orderForm = OrderFormBuilder.anOrderForm().withOrderStatus(OrderStatus.CREATED).withComponents(null).build();

    // Verify
    Assert.assertFalse("Can't dispatch", orderFormConstraintChecker.canDispatch(orderForm));
  }
  
  @Test
  public void testCanEdit_shouldReturnTrue() {
    OrderForm orderForm = OrderFormBuilder.anOrderForm().withOrderStatus(OrderStatus.CREATED).build();

    // Verify
    Assert.assertTrue("Can edit", orderFormConstraintChecker.canEdit(orderForm));
  }

  @Test
  public void testCanEdit_shouldReturnFalse() {
    OrderForm orderForm = OrderFormBuilder.anOrderForm().withOrderStatus(OrderStatus.DISPATCHED).build();

    // Verify
    Assert.assertFalse("Can't edit", orderFormConstraintChecker.canEdit(orderForm));
  }
}
