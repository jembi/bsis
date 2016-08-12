package org.jembi.bsis.controllerservice;


import org.jembi.bsis.backingform.PasswordResetBackingForm;
import org.jembi.bsis.service.PasswordResetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.jembi.bsis.helpers.builders.PasswordResetBackingFormBuilder.aPasswordResetBackingForm;


import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PasswordResetControllerServiceTests {

  @InjectMocks
  private PasswordResetControllerService controllerService;
  @Mock
  private PasswordResetService passwordResetService;
  
  @Test
  public void testResetPassword_should() {
    String username = "superuser";
    
    PasswordResetBackingForm form = aPasswordResetBackingForm().withUsername(username).build();
    
    //Test
    passwordResetService.resetUserPassword(form.getUsername());
    //Assertions
    verify(passwordResetService).resetUserPassword(form.getUsername());
   
  }
}
