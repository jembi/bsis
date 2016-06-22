package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.backingform.AdverseEventTypeBackingForm;

public class AdverseEventTypeBackingFormMatcher extends TypeSafeMatcher<AdverseEventTypeBackingForm> {

  private AdverseEventTypeBackingForm expected;

  public AdverseEventTypeBackingFormMatcher(AdverseEventTypeBackingForm expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An adverse event type with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nDescription: ").appendValue(expected.getDescription())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted());
  }

  @Override
  public boolean matchesSafely(AdverseEventTypeBackingForm actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getName(), expected.getName()) &&
        Objects.equals(actual.getDescription(), expected.getDescription()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted());
  }

  public static AdverseEventTypeBackingFormMatcher hasSameStateAsAdverseEventTypeBackingForm(
      AdverseEventTypeBackingForm expected) {
    return new AdverseEventTypeBackingFormMatcher(expected);
  }

}
