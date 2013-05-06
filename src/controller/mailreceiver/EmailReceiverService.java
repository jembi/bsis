package controller.mailreceiver;

import java.text.ParseException;
import java.util.Date;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import model.location.Location;
import model.producttype.ProductType;
import model.request.Request;
import model.requesttype.RequestType;
import model.util.BloodGroup;
import model.util.Gender;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import repository.LocationRepository;
import repository.ProductTypeRepository;
import repository.RequestRepository;
import repository.RequestTypeRepository;
import repository.VerifiedSenderRepository;
import utils.CustomDateFormatter;
import backingform.RequestBackingForm;
import backingform.validator.RequestBackingFormValidator;
import controller.UtilController;

@Service
public class EmailReceiverService {

  private static final String CREATE_REQUEST_PREFIX = "V2V Request:";

  @Autowired
  private VerifiedSenderRepository verifiedSenderRepository;

  @Autowired
  private RequestTypeRepository requestTypeRepository;

  @Autowired
  private ProductTypeRepository productTypeRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private UtilController utilController;

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  MailSender mailSender;
  
  public void receive(MimeMessage mimeMessage) throws MessagingException {
    System.out.println("Email Received");
    String subject = mimeMessage.getSubject();
    Address[] from = mimeMessage.getFrom();
    Address[] replyTo = mimeMessage.getReplyTo();

    System.out.println("Subject: " + subject);
    if (StringUtils.isBlank(subject) ||
        from == null || from.length != 1 || StringUtils.isBlank(from[0].toString()) ||
        replyTo == null || replyTo.length != 1 || StringUtils.isBlank(replyTo[0].toString())) {
      System.out.println("Message has required information missing");
      return;
    }

    String fromStr = ((InternetAddress)from[0]).toString();
    String replyToStr = ((InternetAddress)from[0]).toString();

    if (isUnverifiedSender(fromStr)) {
      System.out.println("Email Received from unverified sender. Ignoring.");
      return;
    }

    System.out.println("From: " + fromStr);
    System.out.println("ReplyTo: " + replyToStr);
    
    ParsedEmailSubject parseResults = parseEmailSubject(subject);
    switch (parseResults.getEmailType()) {
    case CREATE_REQUEST:
      Request request = (Request) parseResults.getParsedObject();
      sendResponse(fromStr, "c4gv2v@gmail.com",
          "Your request has been received for " +
          request.getPatientBloodAbo() + request.getPatientBloodRh() +
          " (" + request.getNumUnitsRequested() + " units, " +
          request.getProductType().getProductTypeNameShort() + "). " +
      		"Request number: " + request.getRequestNumber() + ". Thank you.");
      break;
    case INVALID:
      sendResponse(fromStr, "c4gv2v@gmail.com", "Invalid message." + subject);
      break;
    }
  }

  private boolean isUnverifiedSender(String fromStr) {

    return false;
  }

  private ParsedEmailSubject parseEmailSubject(String subject) {
    ParsedEmailSubject parsedSubject = new ParsedEmailSubject();
    parsedSubject.setEmailType(EmailType.INVALID);

    Request savedRequest = null;
    boolean success = false;
    if (subject.startsWith(CREATE_REQUEST_PREFIX)) {
      String requestPortion = subject.substring(CREATE_REQUEST_PREFIX.length());
      System.out.println(subject);
      System.out.println(requestPortion);
      String[] requestParams = requestPortion.split(",");
      // blood abo, blood rh, patient gender, required date, request type, product type,
      // no. of units requested, request site - Eight parameters
      System.out.println(requestParams.length);
      if (requestParams != null && requestParams.length == 8) {

        System.out.println("here");
        for (String requestParam : requestParams) {
          System.out.println(requestParam);
        }

        BloodGroup bg = new BloodGroup(requestParams[0], requestParams[1]);
        if (bg.getBloodAbo() != null && bg.getBloodRh() != null) {
          String bloodAbo = requestParams[0];
          String bloodRh = requestParams[1];
          Gender gender = Gender.valueOf(requestParams[2]);
          Date requestDate = new Date();
          Date requiredDate = null;
          try {
            requiredDate = CustomDateFormatter.getDateFromString(requestParams[3]);
          } catch (ParseException e) {
            e.printStackTrace();
          }
          RequestType requestType = requestTypeRepository.getRequestTypeByName(requestParams[4]);
          ProductType productType = productTypeRepository.getProductTypeByName(requestParams[5]);
          Integer numUnitsRequested = Integer.parseInt(requestParams[6]);
          Location requestSite = locationRepository.findLocationByName(requestParams[7]);

          RequestBackingFormValidator validator = new RequestBackingFormValidator(null, utilController);
          Request request = new Request();
          request.setPatientBloodAbo(bloodAbo);
          request.setPatientBloodRh(bloodRh);
          request.setPatientGender(gender);
          request.setRequestDate(requestDate);
          request.setRequiredDate(requiredDate);
          request.setRequestType(requestType);
          request.setProductType(productType);
          request.setNumUnitsRequested(numUnitsRequested);
          request.setRequestSite(requestSite);
          request.setIsDeleted(false);
          RequestBackingForm requestBackingForm = new RequestBackingForm(request);

          Errors errors = new BeanPropertyBindingResult(requestBackingForm, "request");
          validator.validate(requestBackingForm, errors);

          if (errors.hasErrors()) {
            System.out.println("Request has errors");
            for (ObjectError error : errors.getAllErrors()) {
              System.out.println(error.getObjectName() + " " + error.getCode() + ": " + error.getDefaultMessage());
            }
          } else {
            System.out.println("Adding Request");
            try {
              savedRequest = requestRepository.addRequest(requestBackingForm.getRequest());
              parsedSubject.setEmailType(EmailType.CREATE_REQUEST);
              success = true;
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }

        }
      }
    }

    if (success) {
      System.out.println("Request saved successfully");
      System.out.println("Request number is: " + savedRequest.getRequestNumber());
      parsedSubject.setParsedObject(savedRequest);
    } else {
      System.out.println("There was an error saving your request");
    }
    
    return parsedSubject;
  }

  private enum EmailType {
    CREATE_REQUEST, INVALID
  }

  private class ParsedEmailSubject {

    private Object parsedObject;
    private String subjectJSON;
    private EmailType emailType;

    public ParsedEmailSubject() {
      this.setEmailType(EmailType.INVALID);
    }

    public Object getParsedObject() {
      return parsedObject;
    }

    public void setParsedObject(Object parsedObject) {
      this.parsedObject = parsedObject;
    }

    public String getSubjectJSON() {
      return subjectJSON;
    }

    public void setSubjectJSON(String subjectJSON) {
      this.subjectJSON = subjectJSON;
    }

    public EmailType getEmailType() {
      return emailType;
    }

    public void setEmailType(EmailType emailType) {
      this.emailType = emailType;
    }

  }

  private void sendResponse(String to, String from, String subject) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(to);
    mailMessage.setFrom(from);
    mailMessage.setSubject(subject);
    mailMessage.setText("");
    mailMessage.setSentDate(new Date());
    mailSender.send(mailMessage);
  }
}
