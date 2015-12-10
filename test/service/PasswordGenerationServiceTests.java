package service;

import junit.framework.Assert;
import org.junit.Test;

public class PasswordGenerationServiceTests {

  @Test
  public void testGeneratePassword_shouldReturnAnAlphanumericPassword() {
    PasswordGenerationService passwordGenerationService = new PasswordGenerationService();

    String password = passwordGenerationService.generatePassword();

    Assert.assertTrue("Password should be 16 alphanumeric characters", password.matches("^[0-9a-zA-Z]{16}$"));
  }

}
