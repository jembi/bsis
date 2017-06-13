package org.jembi.bsis.service;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class BsisEmailSender {

  @Autowired
  private GeneralConfigAccessorService generalConfigAccessorService;

  public void sendEmail(MimeMailMessage message) {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    Properties mailProperties = new Properties();
    mailProperties.put("mail.smtp.auth", true);
    mailProperties.put("mail.smtp.starttls.enable", true);
    mailSender.setJavaMailProperties(mailProperties);
    mailSender.setHost(generalConfigAccessorService.getGeneralConfigValueByName("smtp.host"));
    mailSender.setPort(generalConfigAccessorService.getIntValue("smtp.port"));
    mailSender.setProtocol("smtp");
    mailSender.setUsername(generalConfigAccessorService.getGeneralConfigValueByName("smtp.auth.username"));
    mailSender.setPassword(generalConfigAccessorService.getGeneralConfigValueByName("smtp.auth.password"));
    mailSender.send(message.getMimeMessage());
  }
  
  public MimeMailMessage createMailMessage(String to, String subject, String message) throws MessagingException {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(message);
    return new MimeMailMessage(mimeMessage);
  }
}
