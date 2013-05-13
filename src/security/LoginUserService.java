package security;

import model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import repository.UserRepository;
import repository.events.ApplicationContextProvider;

@Service
public class LoginUserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {

    System.out.println("here");
    System.out.println(username);

    System.out.println(userRepository);
    if (userRepository == null) {
      ApplicationContext context = ApplicationContextProvider.getApplicationContext(); 
      userRepository = context.getBean(UserRepository.class);
    }

    System.out.println(userRepository);
    User user = userRepository.findUser(username);
    System.out.println(user);
    if (user != null)
      return new V2VUserDetails(user);
    else
      return null;
  }

}
