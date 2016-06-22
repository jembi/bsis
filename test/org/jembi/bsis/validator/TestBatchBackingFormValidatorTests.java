package org.jembi.bsis.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.backingform.validator.TestBatchBackingFormValidator;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import scala.actors.threadpool.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class TestBatchBackingFormValidatorTests {

  @InjectMocks
  private TestBatchBackingFormValidator validator;
  @Mock
  private DonationBatchRepository donationBatchRepository;

  @Test
  public void testValidateNull() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(null, errors);
    assertThat(errors.getErrorCount(), is(0));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testValidateUpdateWithUnassignedDonationBatch() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(1l);
    backingForm.setDonationBatchIds(Arrays.asList(new Long[]{1l, 2l}));
    backingForm.setCreatedDate("2015-10-10");

    when(donationBatchRepository.findDonationBatchById(1l)).thenReturn(new DonationBatch());
    when(donationBatchRepository.findDonationBatchById(2l)).thenReturn(new DonationBatch());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testValidateUpdateWithDonationBatchAssignedToAnotherBatch() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(1l);
    backingForm.setDonationBatchIds(Arrays.asList(new Long[]{1l, 2l}));
    backingForm.setCreatedDate("2015-10-10");
    TestBatch tb2 = new TestBatch();
    tb2.setId(2l);
    Location venue = new Location();
    venue.setName("Test");
    DonationBatch db1 = new DonationBatch();
    db1.setTestBatch(tb2);
    db1.setVenue(venue);

    when(donationBatchRepository.findDonationBatchById(1l)).thenReturn(db1);
    when(donationBatchRepository.findDonationBatchById(2l)).thenReturn(new DonationBatch());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    Assert.assertNotNull(errors.getFieldError("donationBatchIds"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testValidateUpdateWithDonationBatchAssignedToBatch() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(1l);
    backingForm.setDonationBatchIds(Arrays.asList(new Long[]{1l, 2l}));
    backingForm.setCreatedDate("2015-10-10");
    TestBatch tb1 = new TestBatch();
    tb1.setId(1l);
    Location venue = new Location();
    venue.setName("Test");
    DonationBatch db1 = new DonationBatch();
    db1.setTestBatch(tb1);
    db1.setVenue(venue);

    when(donationBatchRepository.findDonationBatchById(1l)).thenReturn(db1);
    when(donationBatchRepository.findDonationBatchById(2l)).thenReturn(new DonationBatch());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }
}
