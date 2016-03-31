package backingform.validator;

import static helpers.builders.DeferralBackingFormBuilder.aDeferralBackingForm;
import static helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static helpers.builders.LocationBuilder.aLocation;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;

import model.location.Location;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import repository.DonorRepository;
import repository.LocationRepository;
import suites.UnitTestSuite;
import backingform.DeferralBackingForm;

public class DeferralBackingFormValidatorTest extends UnitTestSuite {
  
  private static final Date DEFERRED_UNTIL = new Date();
  private static final Long DEFERRED_DONOR_ID = 88L;
  
  @InjectMocks
  DeferralBackingFormValidator deferralBackingFormValidator;
  @Mock
  LocationRepository locationRepository;
  @Mock
  DonorRepository donorRepository;

  @Test
  public void testValid() throws Exception {
    Location venue = aLocation().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReason().withId(1L).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonorId(DEFERRED_DONOR_ID)
        .build();
    
    when(locationRepository.verifyLocationExists(1L)).thenReturn(true);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidVenueDoesNotExist() throws Exception {
    Location venue = aLocation().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReason().withId(1L).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonorId(DEFERRED_DONOR_ID)
        .build();

    when(locationRepository.verifyLocationExists(1L)).thenReturn(false);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on venue", errors.getFieldError("venue"));
  }

  @Test
  public void testInvalidVenueNotSpecified() throws Exception {
    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReason().withId(1L).build())
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonorId(DEFERRED_DONOR_ID)
        .build();

    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on venue", errors.getFieldError("venue"));
  }

  @Test
  public void testInvalidDeferralReasonNotSpecified() throws Exception {
    Location venue = aLocation().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonorId(DEFERRED_DONOR_ID)
        .build();
    
    when(locationRepository.verifyLocationExists(1L)).thenReturn(true);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on deferralReason", errors.getFieldError("deferralReason"));
  }

  @Test
  public void testInvalidDeferredUntilNotSpecified() throws Exception {
    Location venue = aLocation().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReason().withId(1L).build())
        .withVenue(venue)
        .withDeferredDonorId(DEFERRED_DONOR_ID)
        .build();
    
    when(locationRepository.verifyLocationExists(1L)).thenReturn(true);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on deferredUntil", errors.getFieldError("deferredUntil"));
  }

  @Test
  public void testInvalidDeferredDonorNotSpecified() throws Exception {
    Location venue = aLocation().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReason().withId(1L).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .build();
    
    when(locationRepository.verifyLocationExists(1L)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on deferredDonor", errors.getFieldError("deferredDonor"));
  }

  @Test
  public void testInvalidDeferredDonorDoesNotExist() throws Exception {
    Location venue = aLocation().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReason().withId(1L).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonorId(DEFERRED_DONOR_ID)
        .build();
    
    when(locationRepository.verifyLocationExists(1L)).thenReturn(true);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on deferredDonor", errors.getFieldError("deferredDonor"));
  }

}
