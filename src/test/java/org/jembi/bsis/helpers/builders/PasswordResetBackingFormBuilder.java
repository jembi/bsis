package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.PasswordResetBackingForm;

public class PasswordResetBackingFormBuilder extends AbstractBuilder<PasswordResetBackingForm> {

  private String username;

  public PasswordResetBackingFormBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  @Override
  public PasswordResetBackingForm build() {
    PasswordResetBackingForm form = new PasswordResetBackingForm();
    form.setUsername(username);
    return form;
  }

  public static PasswordResetBackingFormBuilder aPasswordResetBackingForm() {
    return new PasswordResetBackingFormBuilder();
  }

}
