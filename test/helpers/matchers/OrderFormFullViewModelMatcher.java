package helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import viewmodel.OrderFormFullViewModel;

public class OrderFormFullViewModelMatcher extends TypeSafeMatcher<OrderFormFullViewModel> {

  private OrderFormFullViewModel expected;

  public OrderFormFullViewModelMatcher(OrderFormFullViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An order form view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nOrder Date: ").appendValue(expected.getOrderDate())
        .appendText("\nDispatched From: ").appendValue(expected.getDispatchedFrom())
        .appendText("\nDispatched To: ").appendValue(expected.getDispatchedTo());
  }

  @Override
  public boolean matchesSafely(OrderFormFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getOrderDate(), expected.getOrderDate()) &&
        Objects.equals(actual.getDispatchedFrom(), expected.getDispatchedFrom()) &&
        Objects.equals(actual.getDispatchedTo(), expected.getDispatchedTo()) &&
        Objects.equals(actual.getItems(), expected.getItems()) &&
        Objects.equals(actual.getPermissions(), expected.getPermissions());
  }

  public static OrderFormFullViewModelMatcher hasSameStateAsOrderFormFullViewModel(OrderFormFullViewModel expected) {
    return new OrderFormFullViewModelMatcher(expected);
  }

}
