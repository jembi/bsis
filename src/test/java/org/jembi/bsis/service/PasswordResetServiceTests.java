package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.builders.MimeMailMessageBuilder.aMimeMailMessage;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.mail.MessagingException;
import javax.persistence.NoResultException;

import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.UserRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.MimeMailMessage;

public class PasswordResetServiceTests extends UnitTestSuite {

  private static final String TEST_EMAIL = "default.user@jembi.org";
  private static final String USERNAME = "Суперпользователь";
  private static final String EXPECTED_PASSWORD = "$2a$10$JP08kh2Cz2FF/1pMdkiUN.4r6CdOKFymCLeFGFU3P0ygqFZtkjteq";
  private static final String TEXT = "Ваш пароль был сброшен до  \"" + EXPECTED_PASSWORD 
      + "\". Вам потребуется изменить его при следующем входе в систему";
  private static final String BSIS_PASSWORD_RESET_MAIL_SUBJECT = "BSIS Password reset";

  @InjectMocks
  private PasswordResetService passwordResetService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private BsisEmailSender bsisEmailSender;

  @Before
  public void setup() {
    passwordResetService.setPasswordResetMessage(TEXT);
    passwordResetService.setPasswordResetSubject(BSIS_PASSWORD_RESET_MAIL_SUBJECT);
  }

  @Test
  public void tesResetUserPasswordWithLatinCharacters_shouldUpdateUserPassword() throws MessagingException {
    // Data
    MimeMailMessage expectedMessage = aMimeMailMessage()
        .withTo(TEST_EMAIL)
        .withSubject(BSIS_PASSWORD_RESET_MAIL_SUBJECT)
        .withText(TEXT)
        .build();

    // set up expectations
    User user = aUser().withUsername(USERNAME).withEmailId(TEST_EMAIL).withPasswordReset().build();
    when(userRepository.findUser(user.getUsername())).thenReturn(user);
    when(userRepository.updateUser(user, true)).thenAnswer(AdditionalAnswers.returnsFirstArg());
    when(bsisEmailSender.createMailMessage(TEST_EMAIL, BSIS_PASSWORD_RESET_MAIL_SUBJECT, TEXT)).thenReturn(expectedMessage);
    doNothing().when(bsisEmailSender).sendEmail(any(MimeMailMessage.class));
    
    // Test
    passwordResetService.resetUserPassword(USERNAME);
    // verify
    verify(bsisEmailSender).sendEmail(expectedMessage);
    verify(userRepository).updateUser(user, true);
  }
  
  @Test(expected = NoResultException.class)
  public void testPasswordResetWithNoExistingUser_shouldThrow() {
    String username = "anyUser";

    // mocks
    when(userRepository.findUser(anyString())).thenReturn(null);

    // Test
    passwordResetService.resetUserPassword(username);
  }
}
