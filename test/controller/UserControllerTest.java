package test;


import model.user.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:**/v2v-servlet.xml" })
@WebAppConfiguration
public class UserControllerTest {

	@Autowired
	public UserRepository userRepository;
	

	@Test
	public void testAddUser() {
		try{
			User user = new User();
			user.setFirstName("Test");
			user.setUsername("testUser");
			user.setLastName("Test");
			user.setPassword("test");
			user.setIsDeleted(false);
			userRepository.addUser(user);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateUser() {
		try{
			User user = userRepository.findUserById(2);
			user.setLastName("technician");
			userRepository.updateUser(user, true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
