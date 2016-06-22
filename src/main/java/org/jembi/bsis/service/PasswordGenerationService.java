package org.jembi.bsis.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class PasswordGenerationService {

  public String generatePassword() {
    return RandomStringUtils.randomAlphanumeric(16);
  }

}
