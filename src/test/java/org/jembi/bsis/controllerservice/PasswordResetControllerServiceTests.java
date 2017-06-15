package org.jembi.bsis.controllerservice;


import static org.jembi.bsis.helpers.builders.PasswordResetBackingFormBuilder.aPasswordResetBackingForm;
import static org.mockito.Mockito.verify;

import org.jembi.bsis.backingform.PasswordResetBackingForm;
import org.jembi.bsis.service.PasswordResetService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class PasswordResetControllerServiceTests extends UnitTestSuite {

  @InjectMocks
  private PasswordResetControllerService controllerService;
  @Mock
  private PasswordResetService passwordResetService;

  @Test
  public void testResetPassword_shouldUpdatePassword() throws Exception {
    String username = "superuser";

    PasswordResetBackingForm form = aPasswordResetBackingForm().withUsername(username).build();

    // Test
    controllerService.resetPassword(form);
    // Assertions
    verify(passwordResetService).resetUserPassword(username);
  }
}
