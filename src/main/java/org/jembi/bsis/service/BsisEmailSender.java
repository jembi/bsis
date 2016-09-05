package org.jembi.bsis.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class BsisEmailSender {

  @Autowired
  private GeneralConfigAccessorService generalConfigAccessorService;

  public void sendEmail(SimpleMailMessage message) {
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
    mailSender.send(message);
  }

}
