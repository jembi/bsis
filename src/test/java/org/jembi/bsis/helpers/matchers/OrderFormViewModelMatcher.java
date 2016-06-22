package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.OrderFormViewModel;

public class OrderFormViewModelMatcher extends TypeSafeMatcher<OrderFormViewModel> {

  private OrderFormViewModel expected;

  public OrderFormViewModelMatcher(OrderFormViewModel expected) {
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
  public boolean matchesSafely(OrderFormViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getOrderDate(), expected.getOrderDate()) &&
        Objects.equals(actual.getDispatchedFrom(), expected.getDispatchedFrom()) &&
        Objects.equals(actual.getDispatchedTo(), expected.getDispatchedTo());
  }

  public static OrderFormViewModelMatcher hasSameStateAsOrderFormViewModel(OrderFormViewModel expected) {
    return new OrderFormViewModelMatcher(expected);
  }

}
