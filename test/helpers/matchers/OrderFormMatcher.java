package helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import model.order.OrderForm;

public class OrderFormMatcher extends TypeSafeMatcher<OrderForm> {

  private OrderForm expected;

  public OrderFormMatcher(OrderForm expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An order form entity with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nOrder Date: ").appendValue(expected.getOrderDate())
        .appendText("\nDispatched From: ").appendValue(expected.getDispatchedFrom())
        .appendText("\nDispatched To: ").appendValue(expected.getDispatchedTo());
  }

  @Override
  public boolean matchesSafely(OrderForm actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getOrderDate(), expected.getOrderDate()) &&
        Objects.equals(actual.getDispatchedFrom(), expected.getDispatchedFrom()) &&
        Objects.equals(actual.getDispatchedTo(), expected.getDispatchedTo());
  }

  public static OrderFormMatcher hasSameStateAsOrderForm(OrderForm expected) {
    return new OrderFormMatcher(expected);
  }

}
