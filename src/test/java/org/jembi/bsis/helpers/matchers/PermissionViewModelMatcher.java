package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.PermissionViewModel;

public class PermissionViewModelMatcher extends TypeSafeMatcher<PermissionViewModel> {

  private PermissionViewModel expected;

  public PermissionViewModelMatcher(PermissionViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A permission view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName());
  }

  @Override
  protected boolean matchesSafely(PermissionViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName());
  }

  public static PermissionViewModelMatcher hasSameStateAsPermissionViewModel(PermissionViewModel expected) {
    return new PermissionViewModelMatcher(expected);
  }
}