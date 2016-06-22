package org.jembi.bsis.helpers.matchers;

import org.jembi.bsis.model.user.User;
import org.mockito.ArgumentMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserPasswordMatcher extends ArgumentMatcher<User> {

  private String expectedPassword;

  public UserPasswordMatcher(String expected) {
    this.expectedPassword = expected;
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

  public static UserPasswordMatcher hasPassword(String expectedPassword) {
    return new UserPasswordMatcher(expectedPassword);
  }

}
