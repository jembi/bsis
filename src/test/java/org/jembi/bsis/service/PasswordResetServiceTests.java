package org.jembi.bsis.service;

import org.jembi.bsis.service.PasswordResetService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.UserRepository;

import static org.mockito.Matchers.anyString;

import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.builders.SimpleMailMessageBuilder.aSimpleMailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.when;

import javax.mail.MessagingException;
import javax.persistence.NoResultException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class PasswordResetServiceTests {
  
  private static final String TEST_EMAIL = "matome.phoshoko@jembi.org";
  private static final String USERNAME = "superuser";
  private static final String EXPECTED_PASSWORD = "$2a$10$JP08kh2Cz2FF/1pMdkiUN.4r6CdOKFymCLeFGFU3P0ygqFZtkjteq";
  private static final String TEXT = "Your password has been reset to \"" + EXPECTED_PASSWORD +
      "\". You will be required to change it next time you log in.";
  private static final String BSIS_PASSWORD_RESET_MAIL_SUBJECT = "BSIS Password reset";

  @Mock
  private UserRepository userRepository;
  
  @Mock
  private JavaMailSender mailSender;
  
  @InjectMocks
  private PasswordResetService passwordResetService;

  @Before
  public void setup()
  {
    passwordResetService.setPasswordResetMessage(TEXT);
    passwordResetService.setPasswordResetSubject(BSIS_PASSWORD_RESET_MAIL_SUBJECT);
  }
  
  @Test
  public void tesResetUserPassword_shouldUpdateUserPassword() throws MessagingException {
    //Data
    String username        = "superuser";
    SimpleMailMessage expectedMessage = aSimpleMailMessage()
        .withTo(TEST_EMAIL)
        .withSubject(BSIS_PASSWORD_RESET_MAIL_SUBJECT)
        .withText(TEXT)
        .build();
    
    //set up expectations
    User user = aUser()
        .withUsername(username)
        .withEmailId(TEST_EMAIL)
        .withPasswordReset()
        .build();
    
    when(userRepository.findUser(user.getUsername())).thenReturn(user);
    when(userRepository.updateUser(user, true)).thenAnswer(AdditionalAnswers.returnsFirstArg());
    doNothing().when(mailSender).send(any(SimpleMailMessage.class));
    
    //Test
    passwordResetService.resetUserPassword(username);    
    //verify
    verify(mailSender).send(expectedMessage);
    verify(userRepository).updateUser(user, true);
    verifyNoMoreInteractions(mailSender);

  }
  @Test(expected=NoResultException.class)
  public void testPasswordResetWithNoExistingUser_shouldReturn() {
    
    //mocks
    when(userRepository.findUser(anyString())).thenReturn(null);
 
    //Test
    passwordResetService.resetUserPassword(null);
    Assert.assertNotNull(userRepository.findUser(anyString()));
    
    verify(userRepository).findUser(USERNAME);
    verifyNoMoreInteractions(userRepository);
  }
  
 
 
}
