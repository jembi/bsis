package org.jembi.bsis.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aLocationBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aTestingSite;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.backingform.validator.TestBatchBackingFormValidator;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.DateGeneratorService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

public class TestBatchBackingFormValidatorTests extends UnitTestSuite {

  @InjectMocks
  private TestBatchBackingFormValidator validator;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private FormFieldRepository formFieldRepository;
  @Mock
  private DateGeneratorService dateGeneratorService;

  @Test
  public void testValidateNull() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(null, errors);
    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateWithNonExistentLocation() throws Exception {

    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(UUID.randomUUID());
    UUID locationId = UUID.randomUUID();
    backingForm.setLocation(aLocationBackingForm().withId(locationId).build());
    Date testBatchDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-20 09:17");
    backingForm.setTestBatchDate(testBatchDate);

    when(locationRepository.getLocation(locationId)).thenThrow(new NoResultException());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(new ArrayList<String>());
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate(backingForm.getTestBatchDate())).thenReturn(testBatchDate);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("location").getCode(), is("errors.notFound"));
  }

  @Test
  public void testValidateWithNonTestingSiteLocation() throws Exception {

    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(UUID.randomUUID());
    UUID locationId = UUID.randomUUID();
    backingForm.setLocation(aLocationBackingForm().withId(locationId).build());
    Date testBatchDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-20 09:17");
    backingForm.setTestBatchDate(testBatchDate);

    when(locationRepository.getLocation(locationId)).thenReturn(aDistributionSite().build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(new ArrayList<String>());
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate(backingForm.getTestBatchDate())).thenReturn(testBatchDate);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("location").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateWithDeletedLocation() throws Exception {

    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(UUID.randomUUID());
    UUID locationId = UUID.randomUUID();
    backingForm.setLocation(aLocationBackingForm().withId(locationId).build());
    Date testBatchDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-20 09:17");
    backingForm.setTestBatchDate(testBatchDate);

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().thatIsDeleted().build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(new ArrayList<String>());
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate(backingForm.getTestBatchDate())).thenReturn(testBatchDate);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("location").getCode(), is("errors.deleted"));
  }

  @Test
  public void testValidateWithNoLocation() throws Exception {

    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(UUID.randomUUID());
    UUID locationId = UUID.randomUUID();
    Date testBatchDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-20 09:17");
    backingForm.setTestBatchDate(testBatchDate);

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().thatIsDeleted().build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(Arrays.asList("location"));
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate(backingForm.getTestBatchDate())).thenReturn(testBatchDate);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("location").getCode(), is("requiredField.error"));
  }
  
  @Test
  public void testValidTestBatchDateCurrentDate_shouldHaveNoError() throws Exception {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(UUID.randomUUID());
    UUID locationId = UUID.randomUUID();
    backingForm.setLocation(aLocationBackingForm().withId(locationId).build());
    Date testBatchDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-20 09:17");
    backingForm.setTestBatchDate(testBatchDate);

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().withId(locationId).build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(Arrays.asList("location"));
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate(backingForm.getTestBatchDate())).thenReturn(testBatchDate);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
    assertThat(errors.getFieldErrorCount(), is(0));
  }
  
  @Test
  public void testValidateTestBatchWithNullDate_shouldThrowAnError() throws Exception{
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(UUID.randomUUID());
    UUID locationId = UUID.randomUUID();
    backingForm.setLocation(aLocationBackingForm().withId(locationId).build());

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().withId(locationId).build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(Arrays.asList("location"));
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate(backingForm.getTestBatchDate())).thenReturn(null);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("testBatchDate").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateTestBatchWithFutureDate_shouldThrowAnError() throws Exception{
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(UUID.randomUUID());
    UUID locationId = UUID.randomUUID();
    backingForm.setLocation(aLocationBackingForm().withId(locationId).build());
    backingForm.setTestBatchDate(null);
    Date today = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-06-23 09:17");
    Date testBatchDate = new DateTime(today).plusDays(5).toDate();
    backingForm.setTestBatchDate(testBatchDate);

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().withId(locationId).build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(Arrays.asList("location"));
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate(backingForm.getTestBatchDate())).thenReturn(testBatchDate);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("testBatchDate").getCode(), is("errors.invalid"));
  }
}
