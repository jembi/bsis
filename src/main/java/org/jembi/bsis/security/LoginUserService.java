package org.jembi.bsis.security;

import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.UserRepository;
import org.jembi.bsis.repository.events.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginUserService implements UserDetailsService {

  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {

    // http://stackoverflow.com/questions/16455348/autowired-dependency-injection-with-spring-security
    if (userRepository == null) {
      ApplicationContext context = ApplicationContextProvider.getApplicationContext();
      userRepository = context.getBean(UserRepository.class);
    }

    User user = userRepository.findUser(username);
    if (user != null)
      return new BsisUserDetails(user);
    else
      return null;
  }

}
