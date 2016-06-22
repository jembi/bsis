package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.adverseevent.AdverseEventType;

public class AdverseEventTypeMatcher extends TypeSafeMatcher<AdverseEventType> {

  private AdverseEventType expected;

  public AdverseEventTypeMatcher(AdverseEventType expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An adverse event type with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nDescription: ").appendValue(expected.getDescription())
        .appendText("\nDeleted: ").appendValue(expected.isDeleted());
  }

  @Override
  public boolean matchesSafely(AdverseEventType actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getName(), expected.getName()) &&
        Objects.equals(actual.getDescription(), expected.getDescription()) &&
        Objects.equals(actual.isDeleted(), expected.isDeleted());
  }

  public static AdverseEventTypeMatcher hasSameStateAsAdverseEventType(AdverseEventType expected) {

    return new AdverseEventTypeMatcher(expected);
  }

}
