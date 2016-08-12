package org.jembi.bsis.controllerservice;

import org.jembi.bsis.backingform.PasswordResetBackingForm;
import org.jembi.bsis.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor={Exception.class})
@Service
public class PasswordResetControllerService {

  @Autowired
  private PasswordResetService passwordResetService;

  public void resetPassword(PasswordResetBackingForm form) {
    passwordResetService.resetUserPassword(form.getUsername());
    
  }
}
