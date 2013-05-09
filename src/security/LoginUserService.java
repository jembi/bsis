package security;

import model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import repository.UserRepository;

@Service(value="myUserDetailsService")
public class LoginUserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {

    System.out.println("here");

    User user = userRepository.findUser(username);
    if (user != null)
      return new V2VUserDetails(user);
    else
      return null;
  }

}
