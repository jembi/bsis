package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.AdverseEventViewModel;

public class AdverseEventViewModelMatcher extends TypeSafeMatcher<AdverseEventViewModel> {

  private AdverseEventViewModel expected;

  public AdverseEventViewModelMatcher(AdverseEventViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An adverse event view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nType: ").appendValue(expected.getType())
        .appendText("\nComment: ").appendValue(expected.getComment());
  }

  @Override
  public boolean matchesSafely(AdverseEventViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getType(), expected.getType()) &&
        Objects.equals(actual.getComment(), expected.getComment());
  }

  public static AdverseEventViewModelMatcher hasSameStateAsAdverseEventViewModel(AdverseEventViewModel expected) {
    return new AdverseEventViewModelMatcher(expected);
  }

}
