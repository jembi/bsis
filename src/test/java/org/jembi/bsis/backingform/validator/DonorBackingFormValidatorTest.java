package org.jembi.bsis.backingform.validator;

import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aLocationBackingForm;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.jembi.bsis.backingform.DonorBackingForm;
import org.jembi.bsis.helpers.builders.FormFieldBuilder;
import org.jembi.bsis.model.admin.FormField;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.preferredlanguage.PreferredLanguage;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.repository.SequenceNumberRepository;
import org.jembi.bsis.utils.CustomDateFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

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

  private final String[] requiredFields =
      new String[] {"birthDate", "donorNumber", "firstName", "lastName", "gender", "venue", "preferredLanguage"};

  private DonorBackingForm getBaseDonorBackingForm() throws ParseException {
    PreferredLanguage preferredLanguage = new PreferredLanguage();
    UUID preferredLanguageId = UUID.randomUUID();
    preferredLanguage.setId(preferredLanguageId);
    preferredLanguage.setPreferredLanguage("English");
    Date validBirthDate = CustomDateFormatter.getDateFromString("1977-10-20");
    DonorBackingForm donorForm = new DonorBackingForm();
    donorForm.setId(UUID.randomUUID());
    donorForm.setBirthDate(validBirthDate);
    donorForm.setDonorNumber("DIN123");
    donorForm.setFirstName("First");
    donorForm.setLastName("Last");
    donorForm.setGender(Gender.female);
    donorForm.setVenue(aLocationBackingForm().withId(UUID.randomUUID()).withName("Venue").thatIsVenue().build());
    donorForm.setPreferredLanguage(preferredLanguage);
    return donorForm;
  }

  @Test
  public void testValid() throws Exception {
    // set up data
    DonorBackingForm donorForm = getBaseDonorBackingForm();

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("donor")).thenReturn(Arrays.asList(requiredFields));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testIsDuplicateDonorNumber1() throws Exception {
    // set up data
    DonorBackingForm donorForm = getBaseDonorBackingForm();

    Donor donor = new Donor();
    donor.setId(UUID.randomUUID());

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("donor")).thenReturn(Arrays.asList(requiredFields));
    when(donorRepository.findDonorByDonorNumber("DIN123", true)).thenReturn(donor);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: duplicate donorNumber", errors.getFieldError("donor.donorNumber"));
  }

  @Test
  public void testIsDuplicateDonorNumber2() throws Exception {
    // set up data
    DonorBackingForm donorForm = getBaseDonorBackingForm();
    donorForm.setDonorNumber("");

    Donor donor = new Donor();
    donor.setId(UUID.randomUUID());

    FormField donorNumberFormField = FormFieldBuilder.aFormField().withAutoGenerate(true).build();

    // set up mocks
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(donorNumberFormField);
    when(sequenceNumberRepository.getSequenceNumber("Donor", "donorNumber")).thenReturn("DIN123");
    when(formFieldRepository.getRequiredFormFields("donor")).thenReturn(Arrays.asList(requiredFields));
    when(donorRepository.findDonorByDonorNumber("DIN123", true)).thenReturn(donor);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: duplicate donorNumber", errors.getFieldError("donor.donorNumber"));
  }

  @Test
  public void testInvalidAge() throws Exception {
    // set up data
    DonorBackingForm donorForm = getBaseDonorBackingForm();
    donorForm.setAge("age");

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("donor")).thenReturn(Arrays.asList(requiredFields));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: invalid age format", errors.getFieldError("age"));
  }

  @Test
  public void testValidAge() throws Exception {
    // set up data
    DonorBackingForm donorForm = getBaseDonorBackingForm();
    donorForm.setAge("18");

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("donor")).thenReturn(Arrays.asList(requiredFields));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testFutureBirthDate() throws Exception {
    // set up data
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, 1);
    Date futureBirthDate = cal.getTime();
    DonorBackingForm donorForm = getBaseDonorBackingForm();
    donorForm.setBirthDate(futureBirthDate);

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("donor")).thenReturn(Arrays.asList(requiredFields));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: future date", errors.getFieldError("donor.birthDate"));
  }

  @Test
  public void testInvalidBloodGroupRh() throws Exception {
    // set up data
    DonorBackingForm donorForm = getBaseDonorBackingForm();
    donorForm.setBloodAbo("AB");

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("donor")).thenReturn(Arrays.asList(requiredFields));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: blood Rh not set", errors.getFieldError("donor.bloodRh"));
  }

  @Test
  public void testInvalidBloodGroupABO() throws Exception {
    // set up data
    DonorBackingForm donorForm = getBaseDonorBackingForm();
    donorForm.setBloodRh("+");

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("donor")).thenReturn(Arrays.asList(requiredFields));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: blood ABO not set", errors.getFieldError("donor.bloodAbo"));
  }

  @Test
  public void testInvalidFieldLengthUsingCommonFieldChecks() throws Exception {
    // set up data
    DonorBackingForm donorForm = getBaseDonorBackingForm();
    donorForm.setMiddleName("012345678912345678901");

    // set up max lengths
    HashMap<String, Integer> maxLengthFields = new HashMap<String, Integer>();
    maxLengthFields.put("firstName", 20);
    maxLengthFields.put("lastName", 20);
    maxLengthFields.put("middleName", 20);

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("donor")).thenReturn(Arrays.asList(requiredFields));
    when(formFieldRepository.getFieldMaxLengths("donor")).thenReturn(maxLengthFields);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");
    donorBackingFormValidator.validate(donorForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: middleName length is more than 20", errors.getFieldError("donor.middleName"));
  }


}
