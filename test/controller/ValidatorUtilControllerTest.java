package controller;

import static org.mockito.Mockito.when;
import helpers.builders.FormFieldBuilder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import model.admin.FormField;
import model.donation.Donation;
import model.donor.Donor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import repository.DonationRepository;
import repository.DonorRepository;
import repository.FormFieldRepository;
import scala.actors.threadpool.Arrays;
import backingform.DonorBackingForm;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class ValidatorUtilControllerTest {
  
  @InjectMocks
  UtilController utilController;
  @Mock
  FormFieldRepository formFieldRepository;
  @Mock
  DonorRepository donorRepository;
  @Mock
  DonationRepository donationRepository;

  @Test
  public void testGetFormFieldsForForm() throws Exception {
    // set up data
    FormField formField1 = FormFieldBuilder.aFormField()
        .withForm("Donor")
        .withField("firstName")
        .withDefaultDisplayName("First Name")
        .build();
    FormField formField2 = FormFieldBuilder.aFormField()
        .withForm("Donor")
        .withField("lastName")
        .withDefaultDisplayName("Last Name")
        .build();
    List<FormField> formFields = Arrays.asList(new FormField[] { formField1, formField2 });
    
    // set up mocks
    when(formFieldRepository.getFormFields("Donor")).thenReturn(formFields);
    
    // run test
    Map<String, Map<String, Object>> fields = utilController.getFormFieldsForForm("Donor");
    
    // asserts
    Assert.assertEquals("Two fields returned", 2, fields.size());
    Map<String, Object> firstNameFields = fields.get("firstName");
    Assert.assertNotNull("firstName fields exist", firstNameFields);
    Assert.assertEquals("fields are present", 8, firstNameFields.size());
    Assert.assertEquals("fields are present", "First Name", firstNameFields.get(FormField.DISPLAY_NAME));
    Map<String, Object> lastNameFields = fields.get("lastName");
    Assert.assertNotNull("lastName fields exist", lastNameFields);
    Assert.assertEquals("fields are present", 8, lastNameFields.size());
    Assert.assertEquals("fields are present", "Last Name", lastNameFields.get(FormField.DISPLAY_NAME));
  }
  
  @Test
  public void testCheckFieldLengthsNoError() throws Exception {
    // set up data
    DonorBackingForm donor = new DonorBackingForm();
    donor.setFirstName("Sample");
    donor.setLastName("Donor");
    donor.setCallingName("test");
    donor.setGender("female");
    
    Map<String, Integer> maxLengths = new HashMap<>();
    maxLengths.put("firstName", 10);
    maxLengths.put("lastName", 10);
    maxLengths.put("callingName", -10);
    maxLengths.put("test", 10);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    
    // set up mocks
    when(formFieldRepository.getFieldMaxLengths("Donor")).thenReturn(maxLengths);
    
    // run test
    utilController.checkFieldLengths(donor, "Donor", errors);
    
    // asserts
    Assert.assertEquals("No errors",  0, errors.getErrorCount());
  }
  
  @Test
  public void testCheckFieldLengthsWithError() throws Exception {
    // set up data
    DonorBackingForm donor = new DonorBackingForm();
    donor.setFirstName("SampleSampleSampleSampleSampleSampleSampleSample");
    donor.setLastName("DonorDonorDonorDonorDonorDonorDonorDonorDonorDonor");
    
    Map<String, Integer> maxLengths = new HashMap<>();
    maxLengths.put("firstName", 10);
    maxLengths.put("lastName", 10);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    
    // set up mocks
    when(formFieldRepository.getFieldMaxLengths("Donor")).thenReturn(maxLengths);
    
    // run test
    utilController.checkFieldLengths(donor, "Donor", errors);
    
    // asserts
    Assert.assertEquals("Errors",  2, errors.getErrorCount());
    Assert.assertNotNull("Error on firstName", errors.getFieldError("Donor.firstName"));
    Assert.assertNotNull("Error on lastName", errors.getFieldError("Donor.lastName"));
  }
  
  @Test
  public void testCheckRequiredFields() throws Exception {
    // set up data
    DonorBackingForm donor = new DonorBackingForm();
    donor.setFirstName("Sample");
    donor.setLastName("Donor");
    donor.setCallingName("test");
    
    List<String> requiredValues = Arrays.asList(new String[] { "firstName", "lastName", "test" });
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    
    // set up mocks
    when(formFieldRepository.getRequiredFormFields("Donor")).thenReturn(requiredValues);
    
    // run test
    utilController.checkRequiredFields(donor, "Donor", errors);
    
    // asserts
    Assert.assertEquals("No errors",  0, errors.getErrorCount());
  }
  
  @Test
  public void testCheckRequiredFieldsWithErrors() throws Exception {
    // set up data
    DonorBackingForm donor = new DonorBackingForm();
    donor.setFirstName("Sample");
    donor.setLastName("Donor");
    
    List<String> requiredValues = Arrays.asList(new String[] { "firstName", "lastName", "birthDate" });
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    
    // set up mocks
    when(formFieldRepository.getRequiredFormFields("Donor")).thenReturn(requiredValues);
    
    // run test
    utilController.checkRequiredFields(donor, "Donor", errors);
    
    // asserts
    Assert.assertEquals("Errors",  1, errors.getErrorCount());
    Assert.assertNotNull("Error on firstName", errors.getFieldError("Donor.birthDate"));
  }
  
  @Test
  public void testCommonFieldChecks() throws Exception {
    // set up data
    DonorBackingForm donor = new DonorBackingForm();
    donor.setFirstName("Sample");
    donor.setLastName("Donor");
    
    Map<String, Integer> maxLengths = new HashMap<>();
    maxLengths.put("firstName", 10);
    maxLengths.put("lastName", 10);

    List<String> requiredValues = Arrays.asList(new String[] { "firstName", "lastName" });
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    
    // set up mocks
    when(formFieldRepository.getRequiredFormFields("Donor")).thenReturn(requiredValues);
    when(formFieldRepository.getFieldMaxLengths("Donor")).thenReturn(maxLengths);
    
    // run test
    utilController.commonFieldChecks(donor, "Donor", errors);
    
    // asserts
    Assert.assertEquals("No errors",  0, errors.getErrorCount());
  }
  
  @Test
  public void testIsFieldAutoGenerated() throws Exception {
    // set up data
    FormField formField = FormFieldBuilder.aFormField()
        .withForm("Donor")
        .withField("donorNumber")
        .withAutoGenerate(true)
        .build();
    
    // set up mocks
    when(formFieldRepository.getFormField("Donor", "donorNumber")).thenReturn(formField);
    
    // run test
    boolean result = utilController.isFieldAutoGenerated("Donor", "donorNumber");
    
    // asserts
    Assert.assertTrue("Field is auto generated", result);
  }
  
  @Test
  public void testDoesFieldUseCurrentTime() throws Exception {
    // set up data
    FormField formField = FormFieldBuilder.aFormField()
        .withForm("Donor")
        .withField("createdDate")
        .withAutoGenerate(true)
        .build();
    
    // set up mocks
    when(formFieldRepository.getFormField("Donor", "createdDate")).thenReturn(formField);
    
    // run test
    boolean result = utilController.isFieldAutoGenerated("Donor", "createdDate");
    
    // asserts
    Assert.assertTrue("Field uses auto date", result);
  }
  
  @Test
  public void testDoesFieldUseCurrentTimeNull() throws Exception {   
    // set up mocks
    when(formFieldRepository.getFormField("Donor", "createdDate")).thenReturn(null);
    
    // run test
    boolean result = utilController.isFieldAutoGenerated("Donor", "createdDate");
    
    // asserts
    Assert.assertFalse("Field doesn't use auto date", result);
  }
  
  @Test
  public void testIsFieldAutoGeneratedNull() throws Exception {   
    // set up mocks
    when(formFieldRepository.getFormField("Donor", "blah")).thenReturn(null);
    
    // run test
    boolean result = utilController.isFieldAutoGenerated("Donor", "blah");
    
    // asserts
    Assert.assertFalse("Field is not auto generated", result);
  }
  
  @Test
  public void testIsFutureDateTrue() throws Exception {    
    // set up data
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 2);
    
    // run test
    boolean result = utilController.isFutureDate(cal.getTime());
    
    // asserts
    Assert.assertTrue("Is future date",  result);
  }
  
  @Test
  public void testIsFutureDateFalse() throws Exception {    
    // set up data
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, -2);
    
    // run test
    boolean result = utilController.isFutureDate(cal.getTime());
    
    // asserts
    Assert.assertFalse("Is past date",  result);
  }
  
  @Test
  public void testFindDonorInForm() throws Exception {    
    // set up data
    Map<String, Object> bean = new HashMap<>();
    bean.put("donorNumber", "D123");
    
    Donor donor = new Donor();

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("D123", false)).thenReturn(donor);
    
    // run test
    Donor returnedDonor = utilController.findDonorInForm(bean);
    
    // asserts
    Assert.assertNotNull("Donor found",  returnedDonor);
    Assert.assertEquals("Same donor", donor, returnedDonor);
  }
  
  @Test
  public void testFindDonorInFormNotFound() throws Exception {    
    // set up data
    Map<String, Object> bean = new HashMap<>();
    bean.put("donorNumber", "D123");

    // set up mocks
    when(donorRepository.findDonorByDonorNumber("D123", false)).thenThrow(NoResultException.class);
    
    // run test
    Donor returnedDonor = utilController.findDonorInForm(bean);
    
    // asserts
    Assert.assertNull("No Donor found",  returnedDonor);
  }
  
  @Test
  public void testFindDonorInFormEmptyDonorNumber() throws Exception {    
    // set up data
    Map<String, Object> bean = new HashMap<>();
    bean.put("donorNumber", "");
    
    // run test
    Donor returnedDonor = utilController.findDonorInForm(bean);
    
    // asserts
    Assert.assertNull("No Donor found",  returnedDonor);    
    Mockito.verifyZeroInteractions(donorRepository);
  }
  
  @Test
  public void testFindDonorInFormNoDonorNumber() throws Exception {    
    // set up data
    Map<String, Object> bean = new HashMap<>();
    
    // run test
    Donor returnedDonor = utilController.findDonorInForm(bean);
    
    // asserts
    Assert.assertNull("No Donor found",  returnedDonor);
  }
  
  @Test
  public void testFindDonationInForm() throws Exception {    
    // set up data
    Map<String, Object> bean = new HashMap<>();
    bean.put("donationIdentificationNumber", "DIN123");
    
    Donation donation = new Donation();

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber("DIN123")).thenReturn(donation);
    
    // run test
    Donation returnedDonation = utilController.findDonationInForm(bean);
    
    // asserts
    Assert.assertNotNull("Donation found",  returnedDonation);
    Assert.assertEquals("Same Donation", donation, returnedDonation);
  }
  
  @Test
  public void testFindDonationInFormEmptyDIN() throws Exception {    
    // set up data
    Map<String, Object> bean = new HashMap<>();
    bean.put("donationIdentificationNumber", "");
    
    // run test
    Donation returnedDonation = utilController.findDonationInForm(bean);
    
    // asserts
    Assert.assertNull("No Donation found",  returnedDonation);
    Mockito.verifyZeroInteractions(donationRepository);
  }
}
