package helpers.matchers;

import model.user.User;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

public class UserMatcher extends ArgumentMatcher<User> {

  private User expected;

  private UserMatcher(User expected) {
    this.expected = expected;
  }

  public static UserMatcher hasSameStateAsUser(User expected) {
    return new UserMatcher(expected);
  }

  @Override
  public boolean matches(Object object) {
    if (!(object instanceof User)) {
      return false;
    }

    User actual = (User) object;

    return Objects.equals(actual.getEmailId(), expected.getEmailId())
        && Objects.equals(actual.isPasswordReset(), expected.isPasswordReset());
  }

}
