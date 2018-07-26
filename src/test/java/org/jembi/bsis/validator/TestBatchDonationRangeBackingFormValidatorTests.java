package org.jembi.bsis.validator;

import org.jembi.bsis.backingform.TestBatchDonationRangeBackingForm;
import org.jembi.bsis.backingform.validator.TestBatchDonationRangeBackingFormValidator;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

public class TestBatchDonationRangeBackingFormValidatorTests extends UnitTestSuite {

  @InjectMocks
  private TestBatchDonationRangeBackingFormValidator validator;
  @Mock
  private GeneralConfigAccessorService generalConfigAccessorService;

  @Test
  public void testValidate_shouldReturnNoErrors() {
    
    String fromDIN = "1000000";
    String toDIN = "2000000";
    UUID testBatchId = UUID.randomUUID();

    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(testBatchId);
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateUpdate_shouldReturnNoErrors() {

    UUID testBatchId = UUID.randomUUID();
    String fromDIN = "1000000";
    String toDIN = "2000000";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(testBatchId);
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateWithNoToDIN_shouldReturnNoErrors() {
    
    String fromDIN = "1000000";
    UUID testBatchId = UUID.randomUUID();
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(testBatchId);
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(null);
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateWithNoFromDIN_shouldReturnOneError() {
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setFromDIN(null);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("fromDIN"), notNullValue());
    assertThat(errors.getFieldError("fromDIN").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateWithInvalidDINRangeOrder_shouldReturnOneError() {
    
    String fromDIN = "2000000";
    String toDIN = "1000000";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("fromDIN"), notNullValue());
    assertThat(errors.getFieldError("fromDIN").getCode(), is("errors.invalid.fromDINBeforeToDIN"));
  }

  @Test
  public void testValidateWithInvalidDINLengthFromDIN1_shouldReturnOneError() {
    String fromDIN = "1";
    String toDIN = "2000000";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("fromDIN"), notNullValue());
    assertThat(errors.getFieldError("fromDIN").getCode(), is("errors.invalid.length"));
  }

  @Test
  public void testValidateWithInvalidDINLengthFromDIN2_shouldReturnOneError() {
    String fromDIN = "12345678";
    String toDIN = "2000000";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("fromDIN"), notNullValue());
    assertThat(errors.getFieldError("fromDIN").getCode(), is("errors.invalid.length"));
  }

  @Test
  public void testValidateWithInvalidDINLengthToDIN1_shouldReturnOneError() {
    String fromDIN = "1000000";
    String toDIN = "2";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("toDIN"), notNullValue());
    assertThat(errors.getFieldError("toDIN").getCode(), is("errors.invalid.length"));
  }

  @Test
  public void testValidateWithInvalidDINLengthToDIN2_shouldReturnOneError() {
    String fromDIN = "1000000";
    String toDIN = "12345678";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("toDIN"), notNullValue());
    assertThat(errors.getFieldError("toDIN").getCode(), is("errors.invalid.length"));
  }
}
