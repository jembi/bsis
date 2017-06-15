package org.jembi.bsis.service;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.NoResultException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.GeneralConfigRepository;
import org.jembi.bsis.repository.UserRepository;
import org.jembi.bsis.template.TemplateEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private BsisEmailSender bsisEmailSender;;
  @Autowired
  private GeneralConfigRepository generalConfigRepository;
  @Autowired
  private TemplateEngine templateEngine;

  public UserRepository getUserRepository() {
    return userRepository;
  }

  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void resetUserPassword(String username) throws Exception {
    User user = userRepository.findUser(username);
    GeneralConfig passwordResetSubject = generalConfigRepository.getGeneralConfigByName(GeneralConfigConstants.PASSWORD_RESET_SUBJECT);
    GeneralConfig passwordResetMessage = generalConfigRepository.getGeneralConfigByName(GeneralConfigConstants.PASSWORD_RESET_MESSAGE);
    
    if (user == null) {
      throw new NoResultException();
    }
    
    // Generate a new random alphanumeric password
    String newPassword = generateRandomPassword();
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    user.setPassword(passwordEncoder.encode(newPassword));
    user.setPasswordReset(true);
    userRepository.updateUser(user, true);
    
    Map<String, String> map = getMapWithPassword(newPassword);
    String output = templateEngine.execute(passwordResetMessage.getName(), passwordResetMessage.getValue(), map);

    // Send an email containing the new password to the user
    try {
      bsisEmailSender.sendEmail(user.getEmailId(), passwordResetSubject.getValue(), output);
    } catch (Exception e) {
      throw e;
    }
  }

  protected Map<String, String> getMapWithPassword(String newPassword) {
    Map<String,String> map = new HashMap<>();
    map.put("password", newPassword);
    return map;
  }

  protected String generateRandomPassword() {
    // Generate a new random alphanumeric password
    return RandomStringUtils.randomAlphanumeric(16);
  }

}
