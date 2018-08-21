package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.model.order.OrderForm;

public class OrderFormMatcher extends AbstractTypeSafeMatcher<OrderForm> {

  public OrderFormMatcher(OrderForm expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, OrderForm orderForm) {
    description.appendText("An OrderForm with the following state:")
        .appendText("\nId: ").appendValue(orderForm.getId())
        .appendText("\nCreated Date: ").appendValue(orderForm.getCreatedDate())
        .appendText("\nStatus: ").appendValue(orderForm.getStatus())
        .appendText("\nType: ").appendValue(orderForm.getType())
        .appendText("\nOrder Date: ").appendValue(orderForm.getOrderDate())
        .appendText("\nDispatched From: ").appendValue(orderForm.getDispatchedFrom())
        .appendText("\nDispatched To: ").appendValue(orderForm.getDispatchedTo())
        .appendText("\nItems: ").appendValue(orderForm.getItems())
        .appendText("\nComponents: ").appendValue(orderForm.getComponents())
        .appendText("\nIs deleted: ").appendValue(orderForm.getIsDeleted());
  }
  
  @Override
  public boolean matchesSafely(OrderForm actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getCreatedDate(), expected.getCreatedDate()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getType(), expected.getType()) &&
        Objects.equals(actual.getOrderDate(), expected.getOrderDate()) &&
        Objects.equals(actual.getDispatchedFrom(), expected.getDispatchedFrom()) &&
        Objects.equals(actual.getDispatchedTo(), expected.getDispatchedTo()) &&
        Objects.equals(actual.getItems(), expected.getItems()) &&
        Objects.equals(actual.getComponents(), expected.getComponents()) &&
        Objects.equals(actual.getIsDeleted(),  expected.getIsDeleted());
  }

  public static OrderFormMatcher hasSameStateAsOrderForm(OrderForm expected) {
    return new OrderFormMatcher(expected);
  }

}
