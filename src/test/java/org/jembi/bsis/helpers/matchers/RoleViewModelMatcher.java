package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.RoleViewModel;

public class RoleViewModelMatcher extends TypeSafeMatcher<RoleViewModel> {

  private RoleViewModel expected;

  public RoleViewModelMatcher(RoleViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A role view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nDescription: ").appendValue(expected.getDescription())
        .appendText("\nPermissions: ").appendValue(expected.getPermissions());
  }

  @Override
  protected boolean matchesSafely(RoleViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getDescription(), expected.getDescription())
        && Objects.equals(actual.getPermissions(), expected.getPermissions());
  }

  public static RoleViewModelMatcher hasSameStateAsRoleViewModel(RoleViewModel expected) {
    return new RoleViewModelMatcher(expected);
  }
}