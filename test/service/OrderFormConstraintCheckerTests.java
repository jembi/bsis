package service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import helpers.builders.OrderFormBuilder;
import model.order.OrderForm;
import model.order.OrderStatus;

@RunWith(MockitoJUnitRunner.class)
public class OrderFormConstraintCheckerTests {

  @InjectMocks
  private OrderFormConstraintChecker orderFormConstraintChecker;

  @Test
  public void testCanDispatch_shouldDispatch() {
    OrderForm orderForm = OrderFormBuilder.anOrderForm().withOrderStatus(OrderStatus.CREATED).build();

    // Verify
    Assert.assertTrue("Can dispatch", orderFormConstraintChecker.canDispatch(orderForm));
  }

  @Test
  public void testCanDispatch_shouldNotDispatch() {
    OrderForm orderForm = OrderFormBuilder.anOrderForm().withOrderStatus(OrderStatus.DISPATCHED).build();

    // Verify
    Assert.assertFalse("Can't dispatch", orderFormConstraintChecker.canDispatch(orderForm));
  }
}
