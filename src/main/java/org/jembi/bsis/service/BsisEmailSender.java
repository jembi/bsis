package org.jembi.bsis.service;

import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class BsisEmailSender {

  @Autowired
  private GeneralConfigAccessorService generalConfigAccessorService;

  public void sendEmail(String to, String subject, String message) throws Exception {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
    helper.setFrom(generalConfigAccessorService.getGeneralConfigValueByName("smtp.auth.username"));
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(message);

    Properties mailProperties = new Properties();
    mailProperties.put("mail.smtp.auth", true);
    mailProperties.put("mail.smtp.starttls.enable", true);

    mailSender.setJavaMailProperties(mailProperties);
    mailSender.setHost(generalConfigAccessorService.getGeneralConfigValueByName("smtp.host"));
    mailSender.setPort(generalConfigAccessorService.getIntValue("smtp.port"));
    mailSender.setProtocol("smtp");
    mailSender.setUsername(generalConfigAccessorService.getGeneralConfigValueByName("smtp.auth.username"));
    mailSender.setPassword(generalConfigAccessorService.getGeneralConfigValueByName("smtp.auth.password"));
    mailSender.send(mimeMessage);
  }
}
