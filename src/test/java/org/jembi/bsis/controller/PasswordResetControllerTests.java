package org.jembi.bsis.controller;

import static org.jembi.bsis.helpers.builders.PasswordResetBackingFormBuilder.aPasswordResetBackingForm;



import static org.mockito.Mockito.verify;

import org.junit.Assert;




import org.jembi.bsis.backingform.PasswordResetBackingForm;
import org.jembi.bsis.controller.PasswordResetController;
import org.jembi.bsis.controllerservice.PasswordResetControllerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

@RunWith(MockitoJUnitRunner.class)
public class PasswordResetControllerTests {
 
 @Mock
 private PasswordResetControllerService passwordResetControllerService;
 @InjectMocks
 private PasswordResetController passwordResetController;

 @Test
 public void testResetPassword_shouldReturnResponseEntityWithCreatedStatus(){
   String username = "superuser";
   PasswordResetBackingForm form = aPasswordResetBackingForm().withUsername(username).build();
  
   //Test
   passwordResetControllerService.resetPassword(form);
   
   verify(passwordResetControllerService).resetPassword(form);
   Assert.assertEquals("Response status should be CREATED", HttpStatus.CREATED,  passwordResetController.resetPassword(form).getStatusCode());

 }
}
