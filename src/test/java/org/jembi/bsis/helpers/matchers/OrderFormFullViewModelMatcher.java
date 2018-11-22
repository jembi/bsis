package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.OrderFormFullViewModel;

public class OrderFormFullViewModelMatcher extends AbstractTypeSafeMatcher<OrderFormFullViewModel> {

  public OrderFormFullViewModelMatcher(OrderFormFullViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, OrderFormFullViewModel orderFormFullViewModel) {
    description.appendText("An OrderFormFullViewModel with the following state:")
        .appendText("\nId: ").appendValue(orderFormFullViewModel.getId())
        .appendText("\nOrder Date: ").appendValue(orderFormFullViewModel.getOrderDate())
        .appendText("\nDispatched From: ").appendValue(orderFormFullViewModel.getDispatchedFrom())
        .appendText("\nDispatched To: ").appendValue(orderFormFullViewModel.getDispatchedTo())
        .appendText("\nOrder items: ").appendValue(orderFormFullViewModel.getItems())
        .appendText("\nComponents: ").appendValue(orderFormFullViewModel.getComponents())
        .appendText("\nPermissions: ").appendValue(orderFormFullViewModel.getPermissions());        ;
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
