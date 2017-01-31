package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.user.User;

public class UserMatcher extends TypeSafeMatcher<User> {

  private User expected;

  public UserMatcher(User expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A user with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nUser name: ").appendValue(expected.getUsername())
        .appendText("\nPassword: ").appendValue(expected.getPassword())
        .appendText("\nFirst name: ").appendValue(expected.getFirstName())
        .appendText("\nLast name: ").appendValue(expected.getLastName())
        .appendText("\nIs staff: ").appendValue(expected.getIsStaff())
        .appendText("\nIs admin: ").appendValue(expected.getIsAdmin())
        .appendText("\nNotes: ").appendValue(expected.getNotes())
        .appendText("\nLast login: ").appendValue(expected.getLastLogin())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nEmail Id: ").appendValue(expected.getEmailId())
        .appendText("\nPassword Reset: ").appendValue(expected.isPasswordReset());
  }

  @Override
  protected boolean matchesSafely(User actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getUsername(), expected.getUsername())
        && Objects.equals(actual.getPassword(), expected.getPassword())
        && Objects.equals(actual.getFirstName(), expected.getFirstName())
        && Objects.equals(actual.getLastName(), expected.getLastName())
        && Objects.equals(actual.getIsStaff(), expected.getIsStaff())
        && Objects.equals(actual.getIsAdmin(), expected.getIsAdmin())
        && Objects.equals(actual.getNotes(), expected.getNotes())
        && Objects.equals(actual.getLastLogin(), expected.getLastLogin())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted())
        && Objects.equals(actual.getEmailId(), expected.getEmailId())
        && Objects.equals(actual.isPasswordReset(), expected.isPasswordReset());
  }

  public static UserMatcher hasSameStateAsUser(User expected) {
    return new UserMatcher(expected);
  }

}
