package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.matchers.RegexMatcher.containsPattern;
import static org.jembi.bsis.helpers.builders.GeneralConfigBuilder.aGeneralConfig;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.NoResultException;

import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.GeneralConfigRepository;
import org.jembi.bsis.repository.UserRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.template.TemplateEngine;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class PasswordResetServiceTests extends UnitTestSuite {

  private static final String TEST_EMAIL = "default.user@jembi.org";
  private static final String USERNAME = "Суперпользователь";
  private static final String EXPECTED_PASSWORD = "$2a$10$JP08kh2Cz2FF/1pMdkiUN.4r6CdOKFymCLeFGFU3P0ygqFZtkjteq";
  private static final String TEXT = "Ваш пароль был сброшен до  \"" + EXPECTED_PASSWORD 
      + "\". Вам потребуется изменить его при следующем входе в систему";
  private static final String BSIS_PASSWORD_RESET_MAIL_SUBJECT = "BSIS Password reset";

  @Spy
  @InjectMocks
  private PasswordResetService passwordResetService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private BsisEmailSender bsisEmailSender;
  @Mock
  private GeneralConfigRepository generalConfigRepository;
  @Mock
  private TemplateEngine templateEngine;

  @SuppressWarnings("unchecked")
  @Test
  public void tesResetUserPasswordWithLatinCharacters_shouldUpdateUserPassword() throws Exception {
    // Data
    String passwordResetName = "email.resetPassword.message";
    String passwordResetDescription = "Password reset email";
    
    Map<String, String> map = new HashMap<>();
    map.put("password", EXPECTED_PASSWORD);
    
    GeneralConfig passwordResetMessage = aGeneralConfig()
        .withName(passwordResetName)
        .withValue(TEXT)
        .withDescription(passwordResetDescription)
        .build();
    GeneralConfig passwordResetSubject= aGeneralConfig()
        .withName(passwordResetName)
        .withValue(BSIS_PASSWORD_RESET_MAIL_SUBJECT)
        .withDescription(passwordResetDescription)
        .build();
    
    // set up expectations
    User user = aUser().withUsername(USERNAME).withEmailId(TEST_EMAIL).withPasswordReset().build();
    when(userRepository.findUser(user.getUsername())).thenReturn(user);
    when(generalConfigRepository.getGeneralConfigByName(GeneralConfigConstants.PASSWORD_RESET_MESSAGE)).thenReturn(passwordResetMessage);
    when(generalConfigRepository.getGeneralConfigByName(GeneralConfigConstants.PASSWORD_RESET_SUBJECT)).thenReturn(passwordResetSubject);
    when(userRepository.updateUser(user, true)).thenAnswer(AdditionalAnswers.returnsFirstArg());
    when(templateEngine.execute(argThat(is(passwordResetMessage.getName())), argThat(is(passwordResetMessage.getValue())),
        any(HashMap.class))).thenReturn(TEXT);
    doReturn(EXPECTED_PASSWORD).when(passwordResetService).generateRandomPassword();
    doNothing().when(bsisEmailSender).sendEmail(any(String.class), any(String.class), any(String.class));
    
    // Test
    passwordResetService.resetUserPassword(USERNAME);
    // verify
    verify(bsisEmailSender).sendEmail(user.getEmailId(), BSIS_PASSWORD_RESET_MAIL_SUBJECT, TEXT);
    verify(userRepository).updateUser(user, true);
  }
  
  @Test(expected = NoResultException.class)
  public void testPasswordResetWithNoExistingUser_shouldThrow() throws Exception {
    String username = "anyUser";

    // mocks
    when(userRepository.findUser(anyString())).thenReturn(null);

    // Test
    passwordResetService.resetUserPassword(username);
  }

  @Test
  public void testGenerateRandomPassword_shouldReturnRandomAlphanumericPassword() throws Exception {
    String alphanumeric16Pattern = "[a-zA-Z0-9]{16}";
    String password = passwordResetService.generateRandomPassword();
    assertThat(password, containsPattern(alphanumeric16Pattern));
  }
}
