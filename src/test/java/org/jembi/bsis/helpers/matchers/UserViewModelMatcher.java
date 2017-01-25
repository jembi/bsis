package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.UserViewModel;

public class UserViewModelMatcher extends TypeSafeMatcher<UserViewModel> {

  private UserViewModel expected;

  public UserViewModelMatcher(UserViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A user view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nUsername: ").appendValue(expected.getUsername())
        .appendText("\nEmail: ").appendValue(expected.getEmailId())
        .appendText("\nFirst name: ").appendValue(expected.getFirstName())
        .appendText("\nLast name: ").appendValue(expected.getLastName())
        .appendText("\nRoles: ").appendValue(expected.getRoles())
        .appendText("\nIs admin: ").appendValue(expected.getIsAdmin())
        .appendText("\nIs password reset: ").appendValue(expected.isPasswordReset())
        ;
  }

  @Override
  protected boolean matchesSafely(UserViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getUsername(), expected.getUsername())
        && Objects.equals(actual.getEmailId(), expected.getEmailId())
        && Objects.equals(actual.getFirstName(), expected.getFirstName())
        && Objects.equals(actual.getLastName(), expected.getLastName())
        && Objects.equals(actual.getRoles(), expected.getRoles())
        && Objects.equals(actual.getIsAdmin(), expected.getIsAdmin())
        && Objects.equals(actual.isPasswordReset(), expected.isPasswordReset());
  }

  public static UserViewModelMatcher hasSameStateAsUserViewModel(UserViewModel expected) {
    return new UserViewModelMatcher(expected);
  }
}