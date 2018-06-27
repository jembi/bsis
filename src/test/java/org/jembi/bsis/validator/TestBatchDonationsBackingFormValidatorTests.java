package org.jembi.bsis.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.jembi.bsis.backingform.TestBatchDonationsBackingForm;
import org.jembi.bsis.backingform.validator.TestBatchDonationsBackingFormValidator;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

public class TestBatchDonationsBackingFormValidatorTests extends UnitTestSuite {

  @InjectMocks
  private TestBatchDonationsBackingFormValidator validator;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private TestBatchRepository testBatchRepository;

  @Test
  public void testValidateNullForm_shouldReturnNoErrors() {
    TestBatchDonationsBackingForm backingForm = new TestBatchDonationsBackingForm();
    Errors errors = new BindException(backingForm, "TestBatchDonationsBackingForm");
    validator.validate(null, errors);
    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateFormWithNullDonationIds_shouldReturnNoErrors() {
    UUID testBatchId = UUID.randomUUID();
    TestBatchDonationsBackingForm backingForm = new TestBatchDonationsBackingForm();
    backingForm.setTestBatchId(testBatchId);
    backingForm.setDonationIds(null);
    TestBatch testBatch =
        aTestBatch().withId(UUID.randomUUID()).withStatus(TestBatchStatus.OPEN).build();

    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);

    Errors errors = new BindException(backingForm, "testBatchDonationsBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateFormWithEmptyDonationIds_shouldReturnNoErrors() {
    UUID testBatchId = UUID.randomUUID();
    TestBatchDonationsBackingForm backingForm = new TestBatchDonationsBackingForm();
    backingForm.setTestBatchId(testBatchId);
    backingForm.setDonationIds(Collections.<UUID>emptyList());
    TestBatch testBatch =
        aTestBatch().withId(UUID.randomUUID()).withStatus(TestBatchStatus.OPEN).build();

    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);

    Errors errors = new BindException(backingForm, "testBatchDonationsBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateFormWithNullTestBatchId_shouldReturnRequiredTestBatchIdError() {
    UUID testBatchId = null;
    TestBatchDonationsBackingForm backingForm = new TestBatchDonationsBackingForm();
    backingForm.setTestBatchId(testBatchId);
    backingForm.setDonationIds(null);

    Errors errors = new BindException(backingForm, "testBatchDonationsBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("testBatchId").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithInvalidTestBatchId_shouldReturnInvalidTestBatchIdError() {
    UUID testBatchId = UUID.randomUUID();
    TestBatchDonationsBackingForm backingForm = new TestBatchDonationsBackingForm();
    backingForm.setTestBatchId(testBatchId);
    backingForm.setDonationIds(null);

    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(null);

    Errors errors = new BindException(backingForm, "testBatchDonationsBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("testBatchId").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateFormWithReleasedTestBatch_shouldReturnTestBatchIsReleasedError() {
    UUID testBatchId = UUID.randomUUID();
    TestBatchDonationsBackingForm backingForm = new TestBatchDonationsBackingForm();
    backingForm.setTestBatchId(testBatchId);
    backingForm.setDonationIds(null);
    TestBatch testBatch =
        aTestBatch().withId(testBatchId).withStatus(TestBatchStatus.RELEASED).build();

    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);

    Errors errors = new BindException(backingForm, "testBatchDonationsBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("testBatchId").getCode(), is("errors.testBatchIsReleased"));
  }

  @Test
  public void testValidateFormWithInvalidDonationId_shouldReturnInvalidDonationIdError() {
    UUID testBatchId = UUID.randomUUID();
    UUID donationId1 = UUID.randomUUID();
    UUID donationId2 = UUID.randomUUID();
    TestBatchDonationsBackingForm backingForm = new TestBatchDonationsBackingForm();
    backingForm.setTestBatchId(testBatchId);
    backingForm.setDonationIds(Arrays.asList(donationId1, donationId2));

    TestBatch testBatch = aTestBatch().withId(testBatchId).withStatus(TestBatchStatus.OPEN).build();
    Donation donation1 = aDonation().withId(donationId1).withTestBatch(testBatch).build();

    when(donationRepository.findDonationById(donationId1)).thenReturn(donation1);
    when(donationRepository.findDonationById(donationId2)).thenReturn(null);
    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);

    Errors errors = new BindException(backingForm, "testBatchDonationsBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("donationIds[1]").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateFormWithDonationFromAnotherBatch_shouldReturnFromAnotherBatchError() {
    UUID testBatchId1 = UUID.randomUUID();
    UUID testBatchId2 = UUID.randomUUID();
    UUID donationId1 = UUID.randomUUID();
    UUID donationId2 = UUID.randomUUID();
    TestBatchDonationsBackingForm backingForm = new TestBatchDonationsBackingForm();
    backingForm.setTestBatchId(testBatchId1);
    backingForm.setDonationIds(Arrays.asList(donationId1, donationId2));

    TestBatch testBatch1 =
        aTestBatch().withId(testBatchId1).withStatus(TestBatchStatus.OPEN).build();
    TestBatch testBatch2 =
        aTestBatch().withId(testBatchId2).withStatus(TestBatchStatus.OPEN).build();
    Donation donation1 = aDonation().withId(donationId1).withTestBatch(testBatch1).build();
    Donation donation2 = aDonation().withId(donationId2).withTestBatch(testBatch2).build();

    when(donationRepository.findDonationById(donationId1)).thenReturn(donation1);
    when(donationRepository.findDonationById(donationId2)).thenReturn(donation2);
    when(testBatchRepository.findTestBatchById(testBatchId1)).thenReturn(testBatch1);

    Errors errors = new BindException(backingForm, "testBatchDonationsBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("donationIds[1]").getCode(),
        is("errors.donationBelongsToAnotherTestBatch"));
  }

}
