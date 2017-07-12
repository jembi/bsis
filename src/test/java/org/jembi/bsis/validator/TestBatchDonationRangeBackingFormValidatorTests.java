package org.jembi.bsis.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.jembi.bsis.helpers.builders.TestBatchDonationRangeBackingFormBuilder.aTestBatchDonationRangeBackingForm;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Arrays;
import java.util.UUID;

import org.jembi.bsis.backingform.TestBatchDonationRangeBackingForm;
import org.jembi.bsis.backingform.validator.TestBatchDonationRangeBackingFormValidator;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

public class TestBatchDonationRangeBackingFormValidatorTests extends UnitTestSuite {

  @InjectMocks
  private TestBatchDonationRangeBackingFormValidator validator;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private GeneralConfigAccessorService generalConfigAccessorService;
  @Mock
  private TestBatchRepository testBatchRepository;

  @Test
  public void testValidate_shouldReturnNoErrors() throws ParseException {
    
    String fromDIN = "1000000";
    String toDIN = "2000000";
    UUID testBatchId = UUID.randomUUID();
    TestBatch testBatch = aTestBatch().withId(testBatchId).withStatus(TestBatchStatus.OPEN).build();
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(testBatchId);
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);

    Donation donation1 = aDonation().withDonationIdentificationNumber(fromDIN).build();
    Donation donation2 = aDonation().withDonationIdentificationNumber(toDIN).build();
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);
    when(donationRepository.findDonationByDonationIdentificationNumber(fromDIN)).thenReturn(donation1);
    when(donationRepository.findDonationByDonationIdentificationNumber(toDIN)).thenReturn(donation2);
    when(donationRepository.findDonationsBetweenTwoDins(fromDIN, toDIN)).thenReturn(Arrays.asList(donation1, donation2));
    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateUpdate_shouldReturnNoErrors() throws ParseException {

    UUID testBatchId = UUID.randomUUID();
    String fromDIN = "1000000";
    String toDIN = "2000000";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(testBatchId);
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);

    TestBatch testBatch = aTestBatch().withId(testBatchId).withStatus(TestBatchStatus.OPEN).build();
    Donation donation1 = aDonation().withDonationIdentificationNumber(fromDIN).withTestBatch(testBatch).build();
    Donation donation2 = aDonation().withDonationIdentificationNumber(toDIN).withTestBatch(testBatch).build();
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);
    when(donationRepository.findDonationByDonationIdentificationNumber(fromDIN)).thenReturn(donation1);
    when(donationRepository.findDonationByDonationIdentificationNumber(toDIN)).thenReturn(donation2);
    when(donationRepository.findDonationsBetweenTwoDins(fromDIN, toDIN)).thenReturn(Arrays.asList(donation1, donation2));
    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateWithNoToDIN_shouldReturnNoErrors() throws ParseException {
    
    String fromDIN = "1000000";
    UUID testBatchId = UUID.randomUUID();
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(testBatchId);
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(null);

    
    Donation donation = aDonation().withDonationIdentificationNumber(fromDIN).build();
    TestBatch testBatch = aTestBatch().withId(testBatchId).withStatus(TestBatchStatus.OPEN).build();
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);
    when(donationRepository.findDonationByDonationIdentificationNumber(fromDIN)).thenReturn(donation);
    when(donationRepository.findDonationsBetweenTwoDins(fromDIN, fromDIN)).thenReturn(Arrays.asList(donation));
    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateWithNoFromDIN_shouldReturnOneError() throws ParseException {
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
  public void testValidateWithInvalidDINRangeOrder_shouldReturnOneError() throws ParseException {
    
    String fromDIN = "2000000";
    String toDIN = "1000000";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);

    Donation donation1 = aDonation().withDonationIdentificationNumber(fromDIN).build();
    Donation donation2 = aDonation().withDonationIdentificationNumber(toDIN).build();
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);
    when(donationRepository.findDonationsBetweenTwoDins(fromDIN, toDIN)).thenReturn(Arrays.asList(donation1, donation2));

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("fromDIN"), notNullValue());
    assertThat(errors.getFieldError("fromDIN").getCode(), is("errors.invalid.fromDINBeforeToDIN"));
  }

  @Test
  public void testValidateWithInvalidDINLengthFromDIN1_shouldReturnOneError() throws ParseException {
    String fromDIN = "1";
    String toDIN = "2000000";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);

    Donation donation1 = aDonation().withDonationIdentificationNumber(fromDIN).build();
    Donation donation2 = aDonation().withDonationIdentificationNumber(toDIN).build();
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);
    when(donationRepository.findDonationsBetweenTwoDins(fromDIN, toDIN)).thenReturn(Arrays.asList(donation1, donation2));

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("fromDIN"), notNullValue());
    assertThat(errors.getFieldError("fromDIN").getCode(), is("errors.invalid.length"));
  }

  @Test
  public void testValidateWithInvalidDINLengthFromDIN2_shouldReturnOneError() throws ParseException {
    String fromDIN = "12345678";
    String toDIN = "2000000";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);

    Donation donation1 = aDonation().withDonationIdentificationNumber(fromDIN).build();
    Donation donation2 = aDonation().withDonationIdentificationNumber(toDIN).build();
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);
    when(donationRepository.findDonationsBetweenTwoDins(fromDIN, toDIN)).thenReturn(Arrays.asList(donation1, donation2));

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("fromDIN"), notNullValue());
    assertThat(errors.getFieldError("fromDIN").getCode(), is("errors.invalid.length"));
  }

  @Test
  public void testValidateWithInvalidDINLengthToDIN1_shouldReturnOneError() throws ParseException {
    String fromDIN = "1000000";
    String toDIN = "2";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);

    Donation donation1 = aDonation().withDonationIdentificationNumber(fromDIN).build();
    Donation donation2 = aDonation().withDonationIdentificationNumber(toDIN).build();
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);
    when(donationRepository.findDonationsBetweenTwoDins(fromDIN, toDIN)).thenReturn(Arrays.asList(donation1, donation2));

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("toDIN"), notNullValue());
    assertThat(errors.getFieldError("toDIN").getCode(), is("errors.invalid.length"));
  }

  @Test
  public void testValidateWithInvalidDINLengthToDIN2_shouldReturnOneError() throws ParseException {
    String fromDIN = "1000000";
    String toDIN = "12345678";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);

    Donation donation1 = aDonation().withDonationIdentificationNumber(fromDIN).build();
    Donation donation2 = aDonation().withDonationIdentificationNumber(toDIN).build();
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);
    when(donationRepository.findDonationsBetweenTwoDins(fromDIN, toDIN)).thenReturn(Arrays.asList(donation1, donation2));

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("toDIN"), notNullValue());
    assertThat(errors.getFieldError("toDIN").getCode(), is("errors.invalid.length"));
  }

  @Test
  public void testValidateWithInvalidDINRangeDonationsBelongToAnotherTestBatch_shouldReturnOneError() throws ParseException {
    String fromDIN = "1000000";
    String toDIN = "2000000";
    UUID testBatchId = UUID.randomUUID();
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(testBatchId);
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);

    TestBatch testBatch = aTestBatch().withId(testBatchId).withStatus(TestBatchStatus.OPEN).build();
    TestBatch otherTestBatch = aTestBatch().withId(UUID.randomUUID()).build();
    Donation donation1 = aDonation().withDonationIdentificationNumber(fromDIN).withTestBatch(otherTestBatch).build();
    Donation donation2 = aDonation().withDonationIdentificationNumber(toDIN).build();
    
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);
    when(donationRepository.findDonationByDonationIdentificationNumber(fromDIN)).thenReturn(donation1);
    when(donationRepository.findDonationByDonationIdentificationNumber(toDIN)).thenReturn(donation2);
    when(donationRepository.findDonationsBetweenTwoDins(fromDIN, toDIN)).thenReturn(Arrays.asList(donation1, donation2));
    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getGlobalErrorCount(), is(1));
    assertThat(errors.getGlobalError().getCode(), is("errors.donationsBelongToAnotherTestBatch"));
  }

  @Test
  public void testValidateWithInvalidFromDIN_shouldReturnOneError() throws ParseException {
    String fromDIN = "1000000";
    String toDIN = "2000000";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);

    Donation donation = aDonation().withDonationIdentificationNumber(toDIN).build();
    
    when(donationRepository.findDonationByDonationIdentificationNumber(fromDIN)).thenReturn(null);
    when(donationRepository.findDonationByDonationIdentificationNumber(toDIN)).thenReturn(donation);
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("fromDIN").getCode(), is("errors.invalid.donation"));
  }

  @Test
  public void testValidateWithInvalidToDIN_shouldReturnOneError() throws ParseException {
    String fromDIN = "1000000";
    String toDIN = "2000000";
    TestBatchDonationRangeBackingForm backingForm = new TestBatchDonationRangeBackingForm();
    backingForm.setTestBatchId(UUID.randomUUID());
    backingForm.setFromDIN(fromDIN);
    backingForm.setToDIN(toDIN);

    Donation donation = aDonation().withDonationIdentificationNumber(fromDIN).build();
    
    when(donationRepository.findDonationByDonationIdentificationNumber(fromDIN)).thenReturn(donation);
    when(donationRepository.findDonationByDonationIdentificationNumber(toDIN)).thenReturn(null);
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("toDIN").getCode(), is("errors.invalid.donation"));
  }
  
  @Test
  public void testValidateWithOpenTestBatchStatus_shouldReturnNoErrors() {
    UUID testBatchId = UUID.randomUUID();
    String fromDIN = "1000000";
    String toDIN = "2000000";
    
    Donation fromDonation = aDonation().withDonationIdentificationNumber(fromDIN).build();
    Donation toDonation = aDonation().withDonationIdentificationNumber(toDIN).build();
    TestBatch testBatch = aTestBatch().withId(testBatchId).withStatus(TestBatchStatus.OPEN).build();
    
    TestBatchDonationRangeBackingForm backingForm =
        aTestBatchDonationRangeBackingForm().withTestBatchId(testBatchId).withFromDIN(fromDIN).withToDIN(toDIN).build();

    
    when(donationRepository.findDonationByDonationIdentificationNumber(fromDIN)).thenReturn(fromDonation);
    when(donationRepository.findDonationByDonationIdentificationNumber(toDIN)).thenReturn(toDonation);
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);
    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);
    
    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }
  
  @Test
  public void testValidateWithClosedTestBatchStatus_shouldReturnOneError() {
    UUID testBatchId = UUID.randomUUID();
    String fromDIN = "1000000";
    String toDIN = "2000000";
    
    Donation fromDonation = aDonation().withDonationIdentificationNumber(fromDIN).build();
    Donation toDonation = aDonation().withDonationIdentificationNumber(toDIN).build();
    TestBatch testBatch = aTestBatch().withId(testBatchId).withStatus(TestBatchStatus.CLOSED).build();
    
    TestBatchDonationRangeBackingForm backingForm =
        aTestBatchDonationRangeBackingForm().withTestBatchId(testBatchId).withFromDIN(fromDIN).withToDIN(toDIN).build();

    when(donationRepository.findDonationByDonationIdentificationNumber(fromDIN)).thenReturn(fromDonation);
    when(donationRepository.findDonationByDonationIdentificationNumber(toDIN)).thenReturn(toDonation);
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);
    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getGlobalError().getCode(), is("errors.invalid.testBatchStatus"));
  }
  
  @Test
  public void testValidateWithReleasedTestBatchStatus_shouldReturnOneError() {
    UUID testBatchId = UUID.randomUUID();
    String fromDIN = "1000000";
    String toDIN = "2000000";
    
    Donation fromDonation = aDonation().withDonationIdentificationNumber(fromDIN).build();
    Donation toDonation = aDonation().withDonationIdentificationNumber(toDIN).build();
    TestBatch testBatch = aTestBatch().withId(testBatchId).withStatus(TestBatchStatus.RELEASED).build();
    
    TestBatchDonationRangeBackingForm backingForm =
        aTestBatchDonationRangeBackingForm().withTestBatchId(testBatchId).withFromDIN(fromDIN).withToDIN(toDIN).build();
    
    when(donationRepository.findDonationByDonationIdentificationNumber(fromDIN)).thenReturn(fromDonation);
    when(donationRepository.findDonationByDonationIdentificationNumber(toDIN)).thenReturn(toDonation);
    when(generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH)).thenReturn(7);
    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);

    Errors errors = new BindException(backingForm, "testBatchDonationRangeBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getGlobalError().getCode(), is("errors.invalid.testBatchStatus"));
  }
}
