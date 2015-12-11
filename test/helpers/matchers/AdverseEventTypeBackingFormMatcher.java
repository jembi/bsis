package helpers.matchers;

import backingform.AdverseEventTypeBackingForm;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Objects;

public class AdverseEventTypeBackingFormMatcher extends TypeSafeMatcher<AdverseEventTypeBackingForm> {

  private AdverseEventTypeBackingForm expected;

  private AdverseEventTypeBackingFormMatcher(AdverseEventTypeBackingForm expected) {
    this.expected = expected;
  }

  public static AdverseEventTypeBackingFormMatcher hasSameStateAsAdverseEventTypeBackingForm(
          AdverseEventTypeBackingForm expected) {
    return new AdverseEventTypeBackingFormMatcher(expected);
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

}
