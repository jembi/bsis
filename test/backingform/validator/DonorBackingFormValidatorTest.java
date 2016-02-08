package backingform.validator;

import static org.mockito.Mockito.when;

import helpers.builders.FormFieldBuilder;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import model.admin.FormField;
import model.donor.Donor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import repository.DonorRepository;
import repository.FormFieldRepository;
import repository.SequenceNumberRepository;
import backingform.DonorBackingForm;

@RunWith(MockitoJUnitRunner.class)
public class DonorBackingFormValidatorTest {

  @InjectMocks
  DonorBackingFormValidator donorBackingFormValidator;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private SequenceNumberRepository sequenceNumberRepository;
  @Mock
  FormFieldRepository formFieldRepository;

  @Test
  public void testValid() throws Exception {
    // set up data
    DonorBackingForm donorForm = new DonorBackingForm();
    donorForm.setBirthDate("1977-10-20");
    donorForm.setBloodAbo("AB");
    donorForm.setBloodRh("+");

    FormField donorNumberFormField = FormFieldBuilder.aFormField().withAutoGenerate(true).build();

    // set up mocks
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(donorNumberFormField);
    when(sequenceNumberRepository.getSequenceNumber("Donor", "donorNumber")).thenReturn("DIN123");
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(donorRepository.findDonorByDonorNumber("DIN123", true)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testIsDuplicateDonorNumber1() throws Exception {
    // set up data
    DonorBackingForm donorForm = new DonorBackingForm();
    donorForm.setId(1l);
    donorForm.setDonorNumber("DIN123");
    donorForm.setBirthDate("1977-10-20");
    donorForm.setBloodAbo("AB");
    donorForm.setBloodRh("+");

    Donor donor = new Donor();
    donor.setId(2l);

    // set up mocks
    when(sequenceNumberRepository.getSequenceNumber("Donor", "donorNumber")).thenReturn("DIN123");
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(donorRepository.findDonorByDonorNumber("DIN123", true)).thenReturn(donor);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: duplicate donorNumber", errors.getFieldError("donor.donorNumber"));
  }

  @Test
  public void testIsDuplicateDonorNumber2() throws Exception {
    // set up data
    DonorBackingForm donorForm = new DonorBackingForm();
    donorForm.setBirthDate("1977-10-20");
    donorForm.setBloodAbo("AB");
    donorForm.setBloodRh("+");

    Donor donor = new Donor();
    donor.setId(2l);

    FormField donorNumberFormField = FormFieldBuilder.aFormField().withAutoGenerate(true).build();

    // set up mocks
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(donorNumberFormField);
    when(sequenceNumberRepository.getSequenceNumber("Donor", "donorNumber")).thenReturn("DIN123");
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(donorRepository.findDonorByDonorNumber("DIN123", true)).thenReturn(donor);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: duplicate donorNumber", errors.getFieldError("donor.donorNumber"));
  }

  @Test
  public void testInvalidAge() throws Exception {
    // set up data
    DonorBackingForm donorForm = new DonorBackingForm();
    donorForm.setAge("age");
    donorForm.setBloodAbo("AB");
    donorForm.setBloodRh("+");

    FormField donorNumberFormField = FormFieldBuilder.aFormField().withAutoGenerate(true).build();

    // set up mocks
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(donorNumberFormField);
    when(sequenceNumberRepository.getSequenceNumber("Donor", "donorNumber")).thenReturn("DIN123");
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(donorRepository.findDonorByDonorNumber("DIN123", true)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: invalid age format", errors.getFieldError("age"));
  }

  @Test
  public void testValidAge() throws Exception {
    // set up data
    DonorBackingForm donorForm = new DonorBackingForm();
    donorForm.setAge("18");
    donorForm.setBloodAbo("AB");
    donorForm.setBloodRh("+");

    FormField donorNumberFormField = FormFieldBuilder.aFormField().withAutoGenerate(true).build();

    // set up mocks
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(donorNumberFormField);
    when(sequenceNumberRepository.getSequenceNumber("Donor", "donorNumber")).thenReturn("DIN123");
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(donorRepository.findDonorByDonorNumber("DIN123", true)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testFutureBirthDate() throws Exception {
    // set up data
    DonorBackingForm donorForm = new DonorBackingForm();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, 1);
    donorForm.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
    donorForm.setBloodAbo("AB");
    donorForm.setBloodRh("+");

    FormField donorNumberFormField = FormFieldBuilder.aFormField().withAutoGenerate(true).build();

    // set up mocks
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(donorNumberFormField);
    when(sequenceNumberRepository.getSequenceNumber("Donor", "donorNumber")).thenReturn("DIN123");
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(donorRepository.findDonorByDonorNumber("DIN123", true)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: future date", errors.getFieldError("donor.birthDate"));
  }

  @Test
  public void testInvalidFormatBirthDate() throws Exception {
    // set up data
    DonorBackingForm donorForm = new DonorBackingForm();
    donorForm.setBirthDate("1234");
    donorForm.setBloodAbo("AB");
    donorForm.setBloodRh("+");

    FormField donorNumberFormField = FormFieldBuilder.aFormField().withAutoGenerate(true).build();

    // set up mocks
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(donorNumberFormField);
    when(sequenceNumberRepository.getSequenceNumber("Donor", "donorNumber")).thenReturn("DIN123");
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(donorRepository.findDonorByDonorNumber("DIN123", true)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
    // NOTE: this is because invalid format birth dates are handled in the backing form
  }

  @Test
  public void testInvalidBloodGroupRh() throws Exception {
    // set up data
    DonorBackingForm donorForm = new DonorBackingForm();
    donorForm.setBirthDate("1977-10-20");
    donorForm.setBloodAbo("AB");

    FormField donorNumberFormField = FormFieldBuilder.aFormField().withAutoGenerate(true).build();

    // set up mocks
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(donorNumberFormField);
    when(sequenceNumberRepository.getSequenceNumber("Donor", "donorNumber")).thenReturn("DIN123");
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(donorRepository.findDonorByDonorNumber("DIN123", true)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: blood Rh not set", errors.getFieldError("donor.bloodRh"));
  }

  @Test
  public void testInvalidBloodGroupABO() throws Exception {
    // set up data
    DonorBackingForm donorForm = new DonorBackingForm();
    donorForm.setBirthDate("1977-10-20");
    donorForm.setBloodRh("+");

    FormField donorNumberFormField = FormFieldBuilder.aFormField().withAutoGenerate(true).build();

    // set up mocks
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(donorNumberFormField);
    when(sequenceNumberRepository.getSequenceNumber("Donor", "donorNumber")).thenReturn("DIN123");
    when(formFieldRepository.getRequiredFormFields("user")).thenReturn(Arrays.asList(new String[]{}));
    when(formFieldRepository.getFieldMaxLengths("user")).thenReturn(new HashMap<String, Integer>());
    when(donorRepository.findDonorByDonorNumber("DIN123", true)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "user");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: blood ABO not set", errors.getFieldError("donor.bloodAbo"));
  }
}
