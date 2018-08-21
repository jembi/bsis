package org.jembi.bsis.validator;

import org.jembi.bsis.backingform.TestBatchDonationsBackingForm;
import org.jembi.bsis.backingform.validator.TestBatchDonationsBackingFormValidator;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestBatchDonationsBackingFormValidatorTests extends UnitTestSuite {

  @InjectMocks
  private TestBatchDonationsBackingFormValidator validator;

  @Test
  public void testValidateNullForm_shouldReturnNoErrors() {
    TestBatchDonationsBackingForm backingForm = new TestBatchDonationsBackingForm();
    Errors errors = new BindException(backingForm, "TestBatchDonationsBackingForm");
    validator.validate(null, errors);
    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateFormWithNullTestBatchId_shouldReturnRequiredTestBatchIdError() {
    TestBatchDonationsBackingForm backingForm = new TestBatchDonationsBackingForm();
    backingForm.setTestBatchId(null);
    backingForm.setDonationIds(Collections.singletonList(UUID.randomUUID()));

    Errors errors = new BindException(backingForm, "testBatchDonationsBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("testBatchId").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithNullDonationIds_shouldReturnRequiredDonationIdsError() {
    TestBatchDonationsBackingForm backingForm = new TestBatchDonationsBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setDonationIds(null);

    Errors errors = new BindException(backingForm, "testBatchDonationsBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("donationIds").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithEmptyDonationIds_shouldReturnRequiredDonationIdsError() {
    TestBatchDonationsBackingForm backingForm = new TestBatchDonationsBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setDonationIds(Collections.<UUID>emptyList());

    Errors errors = new BindException(backingForm, "testBatchDonationsBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("donationIds").getCode(), is("errors.required"));
  }
}
