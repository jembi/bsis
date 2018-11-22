package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;

public class ComponentStatusChangeReasonMatcher extends AbstractTypeSafeMatcher<ComponentStatusChangeReason> {

  public ComponentStatusChangeReasonMatcher(ComponentStatusChangeReason expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, ComponentStatusChangeReason reason) {
    description.appendText("A ComponentStatusChangeReason with the following state:")
        .appendText("\nId: ").appendValue(reason.getId())
        .appendText("\nCategory: ").appendValue(reason.getCategory())
        .appendText("\nType: ").appendValue(reason.getType())
        .appendText("\nStatusChangeReason: ").appendValue(reason.getStatusChangeReason())
        .appendText("\nIsDeleted: ").appendValue(reason.getIsDeleted());
  }

  @Override
  public boolean matchesSafely(ComponentStatusChangeReason actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getCategory(), expected.getCategory()) &&
        Objects.equals(actual.getType(), expected.getType()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getStatusChangeReason(), expected.getStatusChangeReason());
  }

  public static ComponentStatusChangeReasonMatcher hasSameStateAsComponentStatusChangeReason(ComponentStatusChangeReason expected) {
    return new ComponentStatusChangeReasonMatcher(expected);
  }

}
