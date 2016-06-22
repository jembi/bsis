package org.jembi.bsis.backingform;

import javax.validation.constraints.NotNull;

public class PasswordResetBackingForm {

  @NotNull
  private String username;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

}
