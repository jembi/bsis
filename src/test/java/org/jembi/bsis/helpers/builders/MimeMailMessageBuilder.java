package org.jembi.bsis.helpers.builders;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MimeMailMessageBuilder {

  private String to;
  private String subject;
  private String text;

  public MimeMailMessageBuilder withTo(String to) {
    this.to = to;
    return this;
  }

  public MimeMailMessageBuilder withSubject(String subject) {
    this.subject = subject;
    return this;
  }

  public MimeMailMessageBuilder withText(String text) {
    this.text = text;
    return this;
  }

  public MimeMailMessage build() throws MessagingException {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(text);
    return new MimeMailMessage(message);
  }

  public static MimeMailMessageBuilder aMimeMailMessage() {
    return new MimeMailMessageBuilder();
  }

}
