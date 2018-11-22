package org.jembi.bsis.service;

import java.util.UUID;

import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserCRUDService {
  
  @Autowired
  private UserRepository userRepository;
  
  public void deleteUser(UUID userId) {
    User user = userRepository.findUserById(userId);
    user.setIsDeleted(true);
    userRepository.save(user);
  }

}
