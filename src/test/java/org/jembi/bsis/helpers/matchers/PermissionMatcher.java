package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.user.Permission;

public class PermissionMatcher extends TypeSafeMatcher<Permission> {

  private Permission expected;

  public PermissionMatcher(Permission expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A permission with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName());
  }

  @Override
  protected boolean matchesSafely(Permission actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName());
  }

  public static PermissionMatcher hasSameStateAsPermission(Permission expected) {
    return new PermissionMatcher(expected);
  }
}