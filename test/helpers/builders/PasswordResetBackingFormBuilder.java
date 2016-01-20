package helpers.builders;

import backingform.PasswordResetBackingForm;

public class PasswordResetBackingFormBuilder {

  private String username;

  public static PasswordResetBackingFormBuilder aPasswordResetBackingForm() {
    return new PasswordResetBackingFormBuilder();
  }

  public PasswordResetBackingFormBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  public PasswordResetBackingForm build() {
    PasswordResetBackingForm form = new PasswordResetBackingForm();
    form.setUsername(username);
    return form;
  }

}
