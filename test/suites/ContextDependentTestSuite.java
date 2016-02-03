package suites;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import helpers.builders.UserBuilder;
import model.user.User;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import security.BsisUserDetails;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
@Transactional
public abstract class ContextDependentTestSuite {
  
  protected static final String ADMIN_USERNAME = "admin";
  protected User adminUser;
  
  @PersistenceContext
  protected EntityManager entityManager;
  
  @Before
  public void init() throws Exception {
    initSpringSecurityUser();
  }

  @AfterTransaction
  public void after() throws Exception {
    clearSpringSecurityUser(); // reverse the initiation of the Spring Security user
  }

  public void initSpringSecurityUser() throws Exception {
    adminUser = UserBuilder.aUser().withUsername(ADMIN_USERNAME).buildAndPersist(entityManager);
    BsisUserDetails user = new BsisUserDetails(adminUser);
    TestingAuthenticationToken auth = new TestingAuthenticationToken(user, "Credentials");
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public void clearSpringSecurityUser() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(null);
  }
}
