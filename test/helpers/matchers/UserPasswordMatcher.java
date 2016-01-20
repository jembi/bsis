package helpers.matchers;

import model.user.User;
import org.mockito.ArgumentMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserPasswordMatcher extends ArgumentMatcher<User> {

  private String expectedPassword;

  private UserPasswordMatcher(String expected) {
    this.expectedPassword = expected;
  }

  public static UserPasswordMatcher hasPassword(String expectedPassword) {
    return new UserPasswordMatcher(expectedPassword);
  }

  @Override
  public boolean matches(Object object) {
    if (!(object instanceof User)) {
      return false;
    }
    User actualUser = (User) object;
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.matches(expectedPassword, actualUser.getPassword());
  }

}
