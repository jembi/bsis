package backingform.validator;

import static helpers.builders.DeferralBackingFormBuilder.aDeferralBackingForm;
import static helpers.builders.DeferralReasonBackingFormBuilder.aDeferralReasonBackingForm;
import static helpers.builders.DonorBackingFormBuilder.aDonorBackingForm;
import static helpers.builders.LocationBackingFormBuilder.aLocationBackingForm;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import backingform.DeferralBackingForm;
import backingform.LocationBackingForm;
import repository.DonorRepository;
import repository.FormFieldRepository;
import repository.LocationRepository;
import suites.UnitTestSuite;

public class DeferralBackingFormValidatorTest extends UnitTestSuite {
  
  private static final Date DEFERRED_UNTIL = new Date();
  private static final Long DEFERRED_DONOR_ID = 88L;
  
  @InjectMocks
  private DeferralBackingFormValidator deferralBackingFormValidator;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private FormFieldRepository formFieldRepository;
  
  private void mockFormFields() {
    List<String> requiredFormFields = Arrays.asList("deferredUntil", "deferralReason", "deferralDate", "venue",
        "deferredDonor");
    when(formFieldRepository.getRequiredFormFields("DonorDeferral")).thenReturn(requiredFormFields);
  }

  @Test
  public void testValid() throws Exception {
    LocationBackingForm venue = aLocationBackingForm().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(1L).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();
    
    mockFormFields();
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
    LocationBackingForm venue = aLocationBackingForm().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(1L).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();

    mockFormFields();
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
        .withDeferralReason(aDeferralReasonBackingForm().withId(1L).build())
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();

    mockFormFields();
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
    LocationBackingForm venue = aLocationBackingForm().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();
    
    mockFormFields();
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
    LocationBackingForm venue = aLocationBackingForm().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(1L).build())
        .withVenue(venue)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();
    
    mockFormFields();
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
    LocationBackingForm venue = aLocationBackingForm().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(1L).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferralDate(new Date())
        .build();
    
    mockFormFields();
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
    LocationBackingForm venue = aLocationBackingForm().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(1L).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();
    
    mockFormFields();
    when(locationRepository.verifyLocationExists(1L)).thenReturn(true);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on deferredDonor", errors.getFieldError("deferredDonor"));
  }

  @Test
  public void testInvalidDeferralDateNotSpecified() throws Exception {
    LocationBackingForm venue = aLocationBackingForm().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(1L).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .build();
    
    mockFormFields();
    when(locationRepository.verifyLocationExists(1L)).thenReturn(true);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on deferredDonor", errors.getFieldError("deferralDate"));
  }

}
