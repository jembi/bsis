package helpers.matchers;

import java.util.Objects;

import model.user.User;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class UserMatcher extends TypeSafeMatcher<User> {

  private User expected;

  public UserMatcher(User expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A user with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nEmail Id: ").appendValue(expected.getEmailId())
        .appendText("\nPassword Reset: ").appendValue(expected.isPasswordReset());
  }

  @Override
  protected boolean matchesSafely(User actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted())
        && Objects.equals(actual.getEmailId(), expected.getEmailId())
        && Objects.equals(actual.isPasswordReset(), expected.isPasswordReset());
  }

  public static UserMatcher hasSameStateAsUser(User expected) {
    return new UserMatcher(expected);
  }

}
