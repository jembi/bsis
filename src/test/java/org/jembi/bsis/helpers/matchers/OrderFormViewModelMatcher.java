package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.OrderFormViewModel;

public class OrderFormViewModelMatcher extends AbstractTypeSafeMatcher<OrderFormViewModel> {

  public OrderFormViewModelMatcher(OrderFormViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, OrderFormViewModel orderFormViewModel) {
    description.appendText("An OrderFormViewModel with the following state:")
        .appendText("\nId: ").appendValue(orderFormViewModel.getId())
        .appendText("\nOrder Date: ").appendValue(orderFormViewModel.getOrderDate())
        .appendText("\nDispatched From: ").appendValue(orderFormViewModel.getDispatchedFrom())
        .appendText("\nDispatched To: ").appendValue(orderFormViewModel.getDispatchedTo())
        .appendText("\nPatient: ").appendValue(orderFormViewModel.getPatient());
  }

  @Override
  public boolean matchesSafely(OrderFormViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getOrderDate(), expected.getOrderDate()) &&
        Objects.equals(actual.getDispatchedFrom(), expected.getDispatchedFrom()) &&
        Objects.equals(actual.getDispatchedTo(), expected.getDispatchedTo()) &&
        Objects.equals(actual.getPatient(), expected.getPatient());
  }

  public static OrderFormViewModelMatcher hasSameStateAsOrderFormViewModel(OrderFormViewModel expected) {
    return new OrderFormViewModelMatcher(expected);
  }

}
