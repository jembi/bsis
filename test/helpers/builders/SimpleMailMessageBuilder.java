package helpers.builders;

import org.springframework.mail.SimpleMailMessage;

public class SimpleMailMessageBuilder {

  private String[] to;
  private String subject;
  private String text;

  public SimpleMailMessageBuilder withTo(String... to) {
    this.to = to;
    return this;
  }

  public SimpleMailMessageBuilder withSubject(String subject) {
    this.subject = subject;
    return this;
  }

  public SimpleMailMessageBuilder withText(String text) {
    this.text = text;
    return this;
  }

  public SimpleMailMessage build() {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    return message;
  }

  public static SimpleMailMessageBuilder aSimpleMailMessage() {
    return new SimpleMailMessageBuilder();
  }

}
