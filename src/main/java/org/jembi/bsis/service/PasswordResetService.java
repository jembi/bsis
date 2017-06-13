package org.jembi.bsis.service;

import javax.mail.MessagingException;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private BsisEmailSender bsisEmailSender;
  @Value("${password.reset.subject}")
  private String passwordResetSubject;
  @Value("${password.reset.message}")
  private String passwordResetMessage;

  public UserRepository getUserRepository() {
    return userRepository;
  }

  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void setPasswordResetSubject(String passwordResetSubject) {
    this.passwordResetSubject = passwordResetSubject;
  }

  public void setPasswordResetMessage(String passwordResetMessage) {
    this.passwordResetMessage = passwordResetMessage;
  }

  public void resetUserPassword(String username) {
    User user = userRepository.findUser(username);
    if (user == null) {
      throw new NoResultException();
    }
    // Generate a new random alphanumeric password
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String newPassword = RandomStringUtils.randomAlphanumeric(16);
    user.setPassword(passwordEncoder.encode(newPassword));
    user.setPasswordReset(true);
    userRepository.updateUser(user, true);
    // Send an email containing the new password to the user
    try {
      MimeMailMessage message= bsisEmailSender.createMailMessage(user.getEmailId(), passwordResetSubject,
          String.format(passwordResetMessage, newPassword));
      bsisEmailSender.sendEmail(message);
    } catch (MessagingException e) {
      throw new MailParseException(e);
    }
  }

}
