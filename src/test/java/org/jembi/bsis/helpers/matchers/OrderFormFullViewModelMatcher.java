package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.OrderFormFullViewModel;

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
        .appendText("\nDispatched To: ").appendValue(expected.getDispatchedTo())
        .appendText("\nOrder items: ").appendValue(expected.getItems())
        .appendText("\nComponents: ").appendValue(expected.getComponents())
        .appendText("\nPermissions: ").appendValue(expected.getPermissions());        ;
  }

  @Override
  public boolean matchesSafely(OrderFormFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getOrderDate(), expected.getOrderDate()) &&
        Objects.equals(actual.getDispatchedFrom(), expected.getDispatchedFrom()) &&
        Objects.equals(actual.getDispatchedTo(), expected.getDispatchedTo()) &&
        Objects.equals(actual.getItems(), expected.getItems()) &&
        Objects.equals(actual.getComponents(), expected.getComponents()) &&
        Objects.equals(actual.getPermissions(), expected.getPermissions());
  }

  public static OrderFormFullViewModelMatcher hasSameStateAsOrderFormFullViewModel(OrderFormFullViewModel expected) {
    return new OrderFormFullViewModelMatcher(expected);
  }

}
