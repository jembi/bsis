package suites;

import helpers.builders.UserBuilder;
import model.user.User;

import org.junit.After;
import org.junit.Before;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import security.BsisUserDetails;

public abstract class SecurityContextDependentTestSuite extends ContextDependentTestSuite {

  protected static final String USERNAME = "admin";
  protected User loggedInUser;

  @Before
  public void init() throws Exception {
    initSpringSecurityUser();
  }

  @After
  public void after() throws Exception {
    clearSpringSecurityUser(); // reverse the initiation of the Spring Security user
  }

  protected void initSpringSecurityUser() throws Exception {
    loggedInUser = UserBuilder.aUser().withUsername(USERNAME).buildAndPersist(entityManager);
    BsisUserDetails user = new BsisUserDetails(loggedInUser);
    TestingAuthenticationToken auth = new TestingAuthenticationToken(user, "Credentials");
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  private void clearSpringSecurityUser() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(null);
  }
}
