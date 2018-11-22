package org.jembi.bsis.suites;

import java.util.UUID;

import org.jembi.bsis.helpers.builders.UserBuilder;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.security.BsisUserDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@RunWith(MockitoJUnitRunner.class)
public abstract class UnitTestSuite {

  protected static final UUID USER_ID = UUID.randomUUID();
  protected static final String USERNAME = "admin";

  protected User loggedInUser;

  @Before
  public void init() throws Exception {
    loggedInUser = UserBuilder.aUser().withId(USER_ID).withUsername(USERNAME).build();
    setSecurityUser(loggedInUser);
  }

  protected void setSecurityUser(User user, String... authorities) {
    BsisUserDetails bsisUser = new BsisUserDetails(user);
    TestingAuthenticationToken auth = new TestingAuthenticationToken(bsisUser, "Credentials", authorities);
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  @After
  public void after() throws Exception {
    clearSpringSecurityUser(); // reverse the initiation of the Spring Security user
  }

  public void clearSpringSecurityUser() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(null);
  }
}
