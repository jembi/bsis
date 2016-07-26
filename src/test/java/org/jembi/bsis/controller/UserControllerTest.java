package org.jembi.bsis.controller;


import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.UserRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:**/bsis-servlet.xml"})
@WebAppConfiguration
@Transactional
public class UserControllerTest {

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
