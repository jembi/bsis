package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;

import org.jembi.bsis.helpers.builders.ComponentBuilder;
import org.jembi.bsis.helpers.builders.OrderFormBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

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
  public void testCanDispatchPatientRequestWithNoPatientRecord_shouldReturnFalse() {
    Component component = ComponentBuilder.aComponent().build();
    OrderForm orderForm = OrderFormBuilder
        .anOrderForm()
        .withComponent(component)
        .withOrderStatus(OrderStatus.CREATED)
        .withOrderType(OrderType.PATIENT_REQUEST)
        .build();

    // Verify
    Assert.assertFalse("Can dispatch", orderFormConstraintChecker.canDispatch(orderForm));
  }

  @Test
  public void testCanDispatchPatientRequestWithPatientRecord_shouldReturnTrue() {
    Component component = ComponentBuilder.aComponent().build();
    OrderForm orderForm = OrderFormBuilder.anOrderForm()
        .withComponent(component)
        .withOrderStatus(OrderStatus.CREATED)
        .withOrderType(OrderType.PATIENT_REQUEST)
        .withPatient(aPatient().build())
        .build();

    // Verify
    Assert.assertTrue("Can dispatch", orderFormConstraintChecker.canDispatch(orderForm));
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
  
  @Test
  public void testCanDelete_shouldReturnTrue() {
    OrderForm orderForm = OrderFormBuilder.anOrderForm().withOrderStatus(OrderStatus.CREATED).build();

    // Verify
    Assert.assertTrue("Can delete", orderFormConstraintChecker.canDelete(orderForm));
  }
  
  @Test
  public void testCanDelete_shouldReturnFalse() {
    OrderForm orderForm = OrderFormBuilder.anOrderForm().withOrderStatus(OrderStatus.DISPATCHED).build();

    // Verify
    Assert.assertFalse("Can delete", orderFormConstraintChecker.canDelete(orderForm));
  }
}
