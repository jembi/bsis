package org.jembi.bsis.suites;

import org.jembi.bsis.helpers.builders.UserBuilder;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.security.BsisUserDetails;
import org.junit.After;
import org.junit.Before;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

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
