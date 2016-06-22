package org.jembi.bsis.backingform.validator;

import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.backingform.DonorBackingForm;
import org.jembi.bsis.backingform.validator.BaseValidator;
import org.jembi.bsis.backingform.validator.BaseValidatorRuntimeException;
import org.jembi.bsis.backingform.validator.DonorBackingFormValidator;
import org.jembi.bsis.helpers.builders.FormFieldBuilder;
import org.jembi.bsis.model.admin.FormField;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.FormFieldRepository;
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
public class BaseValidatorTest {

  @InjectMocks
  DonorBackingFormValidator testValidator;

  @Mock
  FormFieldRepository formFieldRepository;

  @Test
  public void testCheckFieldLengthsNoError() throws Exception {
    // set up data
    DonorBackingForm donor = new DonorBackingForm();
    donor.setFirstName("Sample");
    donor.setLastName("Donor");
    donor.setCallingName("test");
    donor.setMiddleName("");
    donor.setGender(Gender.female);

    Map<String, Integer> maxLengths = new HashMap<>();
    maxLengths.put("firstName", 10);
    maxLengths.put("lastName", 10);
    maxLengths.put("bloodAbo", 10);
    maxLengths.put("callingName", -10);
    maxLengths.put("test", 10);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");

    // set up mocks
    when(formFieldRepository.getFieldMaxLengths("Donor")).thenReturn(maxLengths);

    // run test
    testValidator.checkFieldLengths(donor, errors);

    // asserts
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
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
    when(formFieldRepository.getFieldMaxLengths("donor")).thenReturn(maxLengths);

    // run test
    testValidator.checkFieldLengths(donor, errors);

    // asserts
    Assert.assertEquals("Errors", 2, errors.getErrorCount());
    Assert.assertNotNull("Error on firstName", errors.getFieldError("donor.firstName"));
    Assert.assertNotNull("Error on lastName", errors.getFieldError("donor.lastName"));
  }

  @Test
  public void testCheckRequiredFields() throws Exception {
    // set up data
    DonorBackingForm donor = new DonorBackingForm();
    donor.setFirstName("Sample");
    donor.setLastName("Donor");
    donor.setCallingName("test");
    donor.setMiddleName("");
    donor.setBirthDate(CustomDateFormatter.getDateFromString("1977-10-20"));

    List<String> requiredValues = Arrays.asList(new String[]{"firstName", "lastName", "birthDate", "test"});

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("Donor")).thenReturn(requiredValues);

    // run test
    testValidator.checkRequiredFields(donor, errors);

    // asserts
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }

  @Test
  public void testCheckRequiredFieldsWithErrors() throws Exception {
    // set up data
    DonorBackingForm donor = new DonorBackingForm();
    donor.setFirstName("Sample");
    donor.setLastName("Donor");

    List<String> requiredValues = Arrays.asList(new String[]{"firstName", "lastName", "birthDate"});

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("donor")).thenReturn(requiredValues);

    // run test
    testValidator.checkRequiredFields(donor, errors);

    // asserts
    Assert.assertEquals("Errors", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on firstName", errors.getFieldError("donor.birthDate"));
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

    List<String> requiredValues = Arrays.asList(new String[]{"firstName", "lastName"});

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "donor");

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("Donor")).thenReturn(requiredValues);
    when(formFieldRepository.getFieldMaxLengths("Donor")).thenReturn(maxLengths);

    // run test
    testValidator.commonFieldChecks(donor, errors);

    // asserts
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }

  @Test
  public void testIsFieldAutoGenerated() throws Exception {
    // set up data
    FormField formField = FormFieldBuilder.aFormField()
        .withForm("donor")
        .withField("donorNumber")
        .withAutoGenerate(true)
        .build();

    // set up mocks
    when(formFieldRepository.getFormField("donor", "donorNumber")).thenReturn(formField);

    // run test
    boolean result = testValidator.isFieldAutoGenerated("donorNumber");

    // asserts
    Assert.assertTrue("Field is auto generated", result);
  }

  @Test
  public void testDoesFieldUseCurrentTime() throws Exception {
    // set up data
    FormField formField = FormFieldBuilder.aFormField()
        .withForm("donor")
        .withField("createdDate")
        .withUseCurrentTime(true)
        .build();

    // set up mocks
    when(formFieldRepository.getFormField("donor", "createdDate")).thenReturn(formField);

    // run test
    boolean result = testValidator.doesFieldUseCurrentTime("createdDate");

    // asserts
    Assert.assertTrue("Field uses auto date", result);
  }

  @Test
  public void testDoesFieldUseCurrentTimeNull() throws Exception {
    // set up mocks
    when(formFieldRepository.getFormField("donor", "createdDate")).thenReturn(null);

    // run test
    boolean result = testValidator.doesFieldUseCurrentTime("createdDate");

    // asserts
    Assert.assertFalse("Field doesn't use auto date", result);
  }

  @Test
  public void testIsFieldAutoGeneratedNull() throws Exception {
    // set up mocks
    when(formFieldRepository.getFormField("donor", "blah")).thenReturn(null);

    // run test
    boolean result = testValidator.isFieldAutoGenerated("blah");

    // asserts
    Assert.assertFalse("Field is not auto generated", result);
  }

  @Test
  public void testIsFutureDateTrue() throws Exception {
    // set up data
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 2);

    // run test
    boolean result = testValidator.isFutureDate(cal.getTime());

    // asserts
    Assert.assertTrue("Is future date", result);
  }

  @Test
  public void testIsFutureDateFalse() throws Exception {
    // set up data
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, -2);

    // run test
    boolean result = testValidator.isFutureDate(cal.getTime());

    // asserts
    Assert.assertFalse("Is past date", result);
  }

  @Test(expected = BaseValidatorRuntimeException.class)
  public void testValidateException() throws Exception {
    class TestValidator extends BaseValidator<Donor> {
      @Override
      public void validateForm(Donor form, Errors errors) {
        commonFieldChecks(form, errors);
      }

      @Override
      protected void checkRequiredFields(Donor form, Errors errors)
          throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        throw new NoSuchMethodException("test");
      }

      @Override
      public String getFormName() {
        return "test";
      }

    }
    TestValidator validator = new TestValidator();
    validator.validate(new Donor(), new MapBindingResult(new HashMap<String, String>(), "test"));
  }
}
