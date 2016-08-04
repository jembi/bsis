package org.jembi.bsis.helpers.matchers;

import java.text.SimpleDateFormat;
import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;

public class ComponentStatusChangeMatcher extends TypeSafeMatcher<ComponentStatusChange> {

  private ComponentStatusChange expected;

  public ComponentStatusChangeMatcher(ComponentStatusChange expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A ComponentStatusChange with the following state:")
        .appendText("\nNewStatus: ").appendValue(expected.getNewStatus())
        .appendText("\nStatusChangedOn: ").appendValue(expected.getStatusChangedOn())
        .appendText("\nComponent: ").appendValue(expected.getComponent())
        .appendText("\nStatusChangeReasonCategory: ").appendValue(expected.getStatusChangeReason().getCategory())
        .appendText("\nStatusChangeReasonType: ").appendValue(expected.getStatusChangeReason().getType())
        .appendText("\nStatusChangeReason: ").appendValue(expected.getStatusChangeReason().getStatusChangeReason())
        .appendText("\nStatusChangeReasonText: ").appendValue(expected.getStatusChangeReasonText());
  }

  @Override
  public boolean matchesSafely(ComponentStatusChange actual) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return Objects.equals(actual.getNewStatus(), expected.getNewStatus()) &&
        (Objects.equals(actual.getStatusChangedOn(), expected.getStatusChangedOn()) || Objects.equals(sdf.format(actual.getStatusChangedOn()), sdf.format(expected.getStatusChangedOn()))) &&
        Objects.equals(actual.getComponent(), expected.getComponent()) && actual.getComponent() != null &&
        Objects.equals(actual.getStatusChangeReason().getCategory(), expected.getStatusChangeReason().getCategory()) &&
        Objects.equals(actual.getStatusChangeReason().getType(), expected.getStatusChangeReason().getType()) &&
        Objects.equals(actual.getStatusChangeReason().getStatusChangeReason(), expected.getStatusChangeReason().getStatusChangeReason()) &&
        Objects.equals(actual.getStatusChangeReasonText(), expected.getStatusChangeReasonText());
  }

  public static ComponentStatusChangeMatcher hasSameStateAsComponentStatusChange(ComponentStatusChange expected) {
    return new ComponentStatusChangeMatcher(expected);
  }

}
