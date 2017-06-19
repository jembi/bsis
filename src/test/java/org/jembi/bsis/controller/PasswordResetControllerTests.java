package org.jembi.bsis.controller;

import static org.jembi.bsis.helpers.builders.PasswordResetBackingFormBuilder.aPasswordResetBackingForm;

import org.jembi.bsis.backingform.PasswordResetBackingForm;
import org.jembi.bsis.controllerservice.PasswordResetControllerService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

public class PasswordResetControllerTests extends UnitTestSuite {

  @Mock
  private PasswordResetControllerService passwordResetControllerService;
  @InjectMocks
  private PasswordResetController passwordResetController;

  @Test
  public void testResetPassword_shouldReturnResponseEntityWithCreatedStatus() throws Exception {
    String username = "superuser";
    PasswordResetBackingForm form = aPasswordResetBackingForm().withUsername(username).build();

    // Test
    HttpStatus statusCode = passwordResetController.resetPassword(form).getStatusCode();

    // Assertions
    Assert.assertEquals("Response status should be CREATED", HttpStatus.CREATED, statusCode);
  }
}
