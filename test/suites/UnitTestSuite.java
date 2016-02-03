package suites;

import helpers.builders.UserBuilder;
import model.user.User;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.transaction.AfterTransaction;

import security.BsisUserDetails;

@RunWith(MockitoJUnitRunner.class)
public abstract class UnitTestSuite {
  
  protected User admin;

  @Before
  public void init() throws Exception {
    admin = UserBuilder.aUser().withId(1L).withUsername("admin").build();
    setSecurityUser(admin);
  }
  
  protected void setSecurityUser(User user, String... authorities) {
    BsisUserDetails bsisUser = new BsisUserDetails(user);
    TestingAuthenticationToken auth = new TestingAuthenticationToken(bsisUser, "Credentials", authorities);
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  @AfterTransaction
  public void after() throws Exception {
      clearSpringSecurityUser(); // reverse the initiation of the Spring Security user
  }
    
  public void clearSpringSecurityUser() throws Exception {
      SecurityContextHolder.getContext().setAuthentication(null);
  }
}
