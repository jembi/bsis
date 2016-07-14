package org.jembi.bsis.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aLocationBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aTestingSite;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.backingform.validator.TestBatchBackingFormValidator;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

public class TestBatchBackingFormValidatorTests extends UnitTestSuite {

  @InjectMocks
  private TestBatchBackingFormValidator validator;
  @Mock
  private DonationBatchRepository donationBatchRepository;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private FormFieldRepository formFieldRepository;

  @Test
  public void testValidateNull() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(null, errors);
    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateUpdateWithUnassignedDonationBatch() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(1l);
    backingForm.setDonationBatchIds(Arrays.asList(1l, 2l));
    backingForm.setCreatedDate(new Date());

    when(donationBatchRepository.findDonationBatchById(1l)).thenReturn(new DonationBatch());
    when(donationBatchRepository.findDonationBatchById(2l)).thenReturn(new DonationBatch());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(new ArrayList<String>());
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateUpdateWithDonationBatchAssignedToAnotherBatch() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(1l);
    backingForm.setDonationBatchIds(Arrays.asList(1l, 2l));
    backingForm.setCreatedDate(new Date());
    TestBatch tb2 = new TestBatch();
    tb2.setId(2l);
    Location venue = new Location();
    venue.setName("Test");
    DonationBatch db1 = new DonationBatch();
    db1.setTestBatch(tb2);
    db1.setVenue(venue);

    when(donationBatchRepository.findDonationBatchById(1l)).thenReturn(db1);
    when(donationBatchRepository.findDonationBatchById(2l)).thenReturn(new DonationBatch());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(new ArrayList<String>());
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    Assert.assertNotNull(errors.getFieldError("donationBatchIds"));
  }

  @Test
  public void testValidateUpdateWithDonationBatchAssignedToBatch() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(1l);
    backingForm.setDonationBatchIds(Arrays.asList(1l, 2l));
    backingForm.setCreatedDate(new Date());
    TestBatch tb1 = new TestBatch();
    tb1.setId(1l);
    Location venue = new Location();
    venue.setName("Test");
    DonationBatch db1 = new DonationBatch();
    db1.setTestBatch(tb1);
    db1.setVenue(venue);

    when(donationBatchRepository.findDonationBatchById(1l)).thenReturn(db1);
    when(donationBatchRepository.findDonationBatchById(2l)).thenReturn(new DonationBatch());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(new ArrayList<String>());
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateWithNonExistentLocation() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(1l);
    backingForm.setLocation(aLocationBackingForm().withId(1L).build());

    when(donationBatchRepository.findDonationBatchById(1l)).thenReturn(new DonationBatch());
    when(donationBatchRepository.findDonationBatchById(2l)).thenReturn(new DonationBatch());
    when(locationRepository.getLocation(1L)).thenThrow(new NoResultException());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(new ArrayList<String>());
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("location").getCode(), is("errors.notFound"));
  }

  @Test
  public void testValidateWithNonTestingSiteLocation() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(1l);
    backingForm.setLocation(aLocationBackingForm().withId(1L).build());

    when(donationBatchRepository.findDonationBatchById(1l)).thenReturn(new DonationBatch());
    when(donationBatchRepository.findDonationBatchById(2l)).thenReturn(new DonationBatch());
    when(locationRepository.getLocation(1L)).thenReturn(aDistributionSite().build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(new ArrayList<String>());
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("location").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateWithDeletedLocation() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(1l);
    backingForm.setLocation(aLocationBackingForm().withId(1L).build());

    when(donationBatchRepository.findDonationBatchById(1l)).thenReturn(new DonationBatch());
    when(donationBatchRepository.findDonationBatchById(2l)).thenReturn(new DonationBatch());
    when(locationRepository.getLocation(1L)).thenReturn(aTestingSite().thatIsDeleted().build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(new ArrayList<String>());
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("location").getCode(), is("errors.deleted"));
  }

  @Test
  public void testValidateWithNoLocation() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(1l);

    when(donationBatchRepository.findDonationBatchById(1l)).thenReturn(new DonationBatch());
    when(donationBatchRepository.findDonationBatchById(2l)).thenReturn(new DonationBatch());
    when(locationRepository.getLocation(1L)).thenReturn(aTestingSite().thatIsDeleted().build());
    when(formFieldRepository.getRequiredFormFields("testBatch")).thenReturn(Arrays.asList("location"));
    when(formFieldRepository.getFieldMaxLengths("testBatch")).thenReturn(new HashMap<String, Integer>());

    Errors errors = new BindException(backingForm, "testBatchBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("testBatch.location").getCode(), is("requiredField.error"));
  }
}
