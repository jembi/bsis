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
import org.jembi.bsis.util.RandomTestDate;
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
  public void testValidateNullForm_shouldHaveNoError() {
    TestBatchBackingForm backingForm = TestBatchBackingForm.builder().build();
    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(null, errors);
    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateFormWithNonExistantLocation_shouldHaveLocationNotFoundError()
      throws Exception {
    UUID locationId = UUID.randomUUID();
    Date today = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-21");
    TestBatchBackingForm backingForm = TestBatchBackingForm.builder()
        .id(UUID.randomUUID())
        .location(aLocationBackingForm().withId(locationId).build())
        .testBatchDate(today)
        .build();

    when(locationRepository.getLocation(locationId)).thenThrow(new NoResultException());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(new ArrayList<String>());
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate()).thenReturn(today);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("location").getCode(), is("errors.notFound"));
  }

  @Test
  public void testValidateFormWithNonTestingSiteLocation_shouldHaveLocationInvalidError()
      throws Exception {
    UUID locationId = UUID.randomUUID();
    Date today = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-21");
    TestBatchBackingForm backingForm = TestBatchBackingForm.builder()
        .id(UUID.randomUUID())
        .location(aLocationBackingForm().withId(locationId).build())
        .testBatchDate(today)
        .build();

    when(locationRepository.getLocation(locationId)).thenReturn(aDistributionSite().build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(new ArrayList<String>());
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate()).thenReturn(today);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("location").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateFormWithDeletedLocation_shouldHaveLocationDeletedError()
      throws Exception {
    UUID locationId = UUID.randomUUID();
    Date today = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-21");
    TestBatchBackingForm backingForm = TestBatchBackingForm.builder()
        .id(UUID.randomUUID())
        .location(aLocationBackingForm().withId(locationId).build())
        .testBatchDate(today)
        .build();

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().thatIsDeleted().build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(new ArrayList<String>());
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate()).thenReturn(today);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("location").getCode(), is("errors.deleted"));
  }

  @Test
  public void testValidateFormWithNoLocation_shouldHaveLocationRequiredError() throws Exception {
    UUID locationId = UUID.randomUUID();
    Date today = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-21 15:23");
    TestBatchBackingForm backingForm = TestBatchBackingForm.builder()
        .id(UUID.randomUUID())
        .testBatchDate(today)
        .build();

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().thatIsDeleted().build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(Arrays.asList("location"));
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate()).thenReturn(today);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("location").getCode(), is("requiredField.error"));
  }
  
  @Test
  public void testValidateFormWithNullTestBatchDate_shouldHaveDateInvalidError() throws Exception {
    UUID locationId = UUID.randomUUID();
    TestBatchBackingForm backingForm = TestBatchBackingForm.builder()
        .id(UUID.randomUUID())
        .location(aLocationBackingForm().withId(locationId).build())
        .build();

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().withId(locationId).build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(Arrays.asList("location"));
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate()).thenReturn(new RandomTestDate());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("testBatchDate").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateFormWithTestBatchDateAfterToday_shouldHaveDateInvalidError()
      throws Exception {
    UUID locationId = UUID.randomUUID();
    Date today = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-21 15:23");
    Date testBatchDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-06-23 09:17");
    TestBatchBackingForm backingForm = TestBatchBackingForm.builder()
        .id(UUID.randomUUID())
        .location(aLocationBackingForm().withId(locationId).build())
        .testBatchDate(testBatchDate)
        .build();

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().withId(locationId).build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(Arrays.asList("location"));
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate()).thenReturn(today);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("testBatchDate").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateFormWithNotNullIdAndBackEntryTrueAndTestBatchDateBeforeToday_shouldHaveNoError()
      throws Exception {
    UUID locationId = UUID.randomUUID();
    Date today = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-21 15:23");
    Date testBatchDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-20 09:17");
    TestBatchBackingForm backingForm = TestBatchBackingForm.builder()
        .id(null)
        .location(aLocationBackingForm().withId(locationId).build())
        .testBatchDate(testBatchDate)
        .backEntry(true)
        .build();

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().withId(locationId).build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(Arrays.asList("location"));
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate()).thenReturn(today);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateFormWithNullIdAndBackEntryTrueAndTestBatchDateBeforeToday_shouldHaveNoError()
      throws Exception {
    UUID locationId = UUID.randomUUID();
    Date today = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-21 15:23");
    Date testBatchDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-20 09:17");
    TestBatchBackingForm backingForm = TestBatchBackingForm.builder()
        .id(null)
        .location(aLocationBackingForm().withId(locationId).build())
        .testBatchDate(testBatchDate)
        .backEntry(true)
        .build();

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().withId(locationId).build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(Arrays.asList("location"));
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate()).thenReturn(today);

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }
  
  @Test
  public void testValidateFormWithNullIdAndBackEntryFalseAndTestBatchDateBeforeToday_shouldHaveDateInvalidError()
      throws Exception {
    UUID locationId = UUID.randomUUID();
    Date today = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-21 15:23");
    Date testBatchDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-20 09:17");
    TestBatchBackingForm backingForm = TestBatchBackingForm.builder()
        .id(null)
        .location(aLocationBackingForm().withId(locationId).build())
        .testBatchDate(testBatchDate)
        .backEntry(false)
        .build();

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().withId(locationId).build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(Arrays.asList("location"));
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate()).thenReturn(today);
    when(dateGeneratorService.generateLocalDate(today))
        .thenReturn(new DateTime(today).toLocalDate());
    when(dateGeneratorService.generateLocalDate(testBatchDate))
        .thenReturn(new DateTime(testBatchDate).toLocalDate());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("testBatchDate").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateFormWithNotNullIdAndBackEntryFalseAndTestBatchDateBeforeToday_shouldHaveNoError()
      throws Exception {
    UUID locationId = UUID.randomUUID();
    Date today = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-21 15:23");
    Date testBatchDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-20 09:17");
    TestBatchBackingForm backingForm = TestBatchBackingForm.builder()
        .id(UUID.randomUUID())
        .location(aLocationBackingForm().withId(locationId).build())
        .testBatchDate(testBatchDate)
        .backEntry(false)
        .build();

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().withId(locationId).build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(Arrays.asList("location"));
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate()).thenReturn(today);
    when(dateGeneratorService.generateLocalDate(today))
        .thenReturn(new DateTime(today).toLocalDate());
    when(dateGeneratorService.generateLocalDate(testBatchDate))
        .thenReturn(new DateTime(testBatchDate).toLocalDate());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
    assertThat(errors.getFieldErrorCount(), is(0));
  }
  
  @Test
  public void testValidateFormWithNullIdAndBackEntryFalseAndTestBatchDateToday_shouldHaveNoError()
      throws Exception {
    UUID locationId = UUID.randomUUID();
    Date today = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-21 15:23");
    Date testBatchDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-01-21 15:00");
    TestBatchBackingForm backingForm = TestBatchBackingForm.builder()
        .id(null)
        .location(aLocationBackingForm().withId(locationId).build())
        .testBatchDate(testBatchDate)
        .backEntry(false)
        .build();

    when(locationRepository.getLocation(locationId)).thenReturn(aTestingSite().withId(locationId).build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(Arrays.asList("location"));
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());
    when(dateGeneratorService.generateDate()).thenReturn(today);
    when(dateGeneratorService.generateLocalDate(today))
        .thenReturn(new DateTime(today).toLocalDate());
    when(dateGeneratorService.generateLocalDate(testBatchDate))
        .thenReturn(new DateTime(testBatchDate).toLocalDate());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
    assertThat(errors.getFieldErrorCount(), is(0));
  }
}
