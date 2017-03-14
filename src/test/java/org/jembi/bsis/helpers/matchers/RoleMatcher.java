package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.user.Role;

public class RoleMatcher extends TypeSafeMatcher<Role> {

  private Role expected;

  public RoleMatcher(Role expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A role with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nDescription: ").appendValue(expected.getDescription())
        .appendText("\nPermissions: ").appendValue(expected.getPermissions());
  }

  @Override
  protected boolean matchesSafely(Role actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getDescription(), expected.getDescription())
        && Objects.equals(actual.getPermissions(), expected.getPermissions());
  }

  public static RoleMatcher hasSameStateAsRole(Role expected) {
    return new RoleMatcher(expected);
  }
}