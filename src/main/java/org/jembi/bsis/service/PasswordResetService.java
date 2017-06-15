package org.jembi.bsis.service;

import java.io.IOException;
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
    if (user == null) {
      throw new NoResultException();
    }
    
    String newPassword = generateRandomPassword();
    updateUserWithNewPassword(user, newPassword);
   
    sendNewPasswordEmailToUser(user, newPassword);
  }

  private void sendNewPasswordEmailToUser(User user, String newPassword) throws Exception {
    try {
      GeneralConfig passwordResetSubject = generalConfigRepository.getGeneralConfigByName(GeneralConfigConstants.PASSWORD_RESET_SUBJECT);
      bsisEmailSender.sendEmail(user.getEmailId(), passwordResetSubject.getValue(), getPasswordResetMessage(newPassword));
    } catch (Exception e) {
      throw e;
    }
  }

  private void updateUserWithNewPassword(User user, String newPassword) {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    user.setPassword(passwordEncoder.encode(newPassword));
    user.setPasswordReset(true);
    userRepository.updateUser(user, true);
  }

  protected String generateRandomPassword() {
    return RandomStringUtils.randomAlphanumeric(16);
  } 
  
  protected String getPasswordResetMessage(String password) throws IOException {
    GeneralConfig passwordResetMessage = generalConfigRepository.getGeneralConfigByName(GeneralConfigConstants.PASSWORD_RESET_MESSAGE);
    Map<String,String> map = new HashMap<>();
    map.put("password", password);
    return templateEngine.execute(passwordResetMessage.getName(), passwordResetMessage.getValue(), map); 
  }
}
