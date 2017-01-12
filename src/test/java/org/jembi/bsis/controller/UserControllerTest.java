package org.jembi.bsis.controller;


import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.UserRepository;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserControllerTest extends ContextDependentTestSuite  {

  @Autowired
  public UserRepository userRepository;


  @Ignore
  @Test
  public void testAddUser() {
    User user = new User();
    user.setFirstName("Test");
    user.setUsername("testUser");
    user.setLastName("Test");
    user.setPassword("test");
    user.setIsDeleted(false);
    userRepository.addUser(user);
  }

  @Ignore
  @Test
  public void testUpdateUser() {
    User user = userRepository.findUserById(2l);
    user.setLastName("technician");
    userRepository.updateUser(user, false);
  }
}
