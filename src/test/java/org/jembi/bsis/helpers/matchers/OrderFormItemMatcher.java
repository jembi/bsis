package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.model.order.OrderFormItem;

public class OrderFormItemMatcher extends AbstractTypeSafeMatcher<OrderFormItem> {

  public OrderFormItemMatcher(OrderFormItem expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, OrderFormItem orderFormItem) {
    description.appendText("An OrderFormItem with the following state:")
      .appendText("\nId: ").appendValue(orderFormItem.getId())
      .appendText("\nBloodAbo: ").appendValue(orderFormItem.getBloodAbo())
      .appendText("\nBloodRh: ").appendValue(orderFormItem.getBloodRh())
      .appendText("\nComponentType: ").appendValue(orderFormItem.getComponentType())
      .appendText("\nNumberOfUnits: ").appendValue(orderFormItem.getNumberOfUnits());
  }

  @Override
  protected boolean matchesSafely(OrderFormItem actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getBloodAbo(), expected.getBloodAbo())
        && Objects.equals(actual.getBloodRh(), expected.getBloodRh())
        && Objects.equals(actual.getComponentType(), expected.getComponentType())
        && Objects.equals(actual.getNumberOfUnits(), expected.getNumberOfUnits());
  }
  
  public static OrderFormItemMatcher hasSameStateAsOrderFormItem(OrderFormItem expected) {
    return new OrderFormItemMatcher(expected);
  }

}
