package org.jembi.bsis.controller;

import java.util.Map;

import javax.validation.Valid;

import org.jembi.bsis.backingform.PasswordResetBackingForm;
import org.jembi.bsis.controllerservice.PasswordResetControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passwordresets")
public class PasswordResetController {
  @Autowired
  private PasswordResetControllerService passwordResetControllerService;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> resetPassword(@Valid @RequestBody PasswordResetBackingForm form) throws Exception {
    passwordResetControllerService.resetPassword(form);
    return new ResponseEntity<Map<String, Object>>(HttpStatus.CREATED);
  }
}
