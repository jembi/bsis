package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.user.User;
import repository.UserRepository;

@Transactional
@Service
public class UserCRUDService {
  
  @Autowired
  private UserRepository userRepository;
  
  public void deleteUser(Long userId) {
    User user = userRepository.findUserById(userId);
    user.setIsDeleted(true);
    userRepository.save(user);
  }

}
