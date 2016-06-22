package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.AdverseEventTypeViewModel;

public class AdverseEventTypeViewModelMatcher extends TypeSafeMatcher<AdverseEventTypeViewModel> {

  private AdverseEventTypeViewModel expected;

  public AdverseEventTypeViewModelMatcher(AdverseEventTypeViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An adverse event type view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nDescription: ").appendValue(expected.getDescription())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted());
  }

  @Override
  public boolean matchesSafely(AdverseEventTypeViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getName(), expected.getName()) &&
        Objects.equals(actual.getDescription(), expected.getDescription()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted());
  }

  public static AdverseEventTypeViewModelMatcher hasSameStateAsAdverseEventTypeViewModel(
      AdverseEventTypeViewModel expected) {

    return new AdverseEventTypeViewModelMatcher(expected);
  }

}
