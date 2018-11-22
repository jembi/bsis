package org.jembi.bsis.backingform.validator;

import static org.jembi.bsis.helpers.builders.DeferralBackingFormBuilder.aDeferralBackingForm;
import static org.jembi.bsis.helpers.builders.DeferralReasonBackingFormBuilder.aDeferralReasonBackingForm;
import static org.jembi.bsis.helpers.builders.DonorBackingFormBuilder.aDonorBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aLocationBackingForm;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.DeferralBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.repository.DeferralReasonRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class DeferralBackingFormValidatorTest extends UnitTestSuite {
  
  private static final Date DEFERRED_UNTIL = new DateTime().plusDays(1).toDate();
  private static final UUID DEFERRED_DONOR_ID = UUID.randomUUID();
  private static final UUID DEFERRAL_REASON_ID = UUID.randomUUID();
  
  @InjectMocks
  private DeferralBackingFormValidator deferralBackingFormValidator;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private FormFieldRepository formFieldRepository;
  @Mock
  private DeferralReasonRepository deferralReasonRepository;
  
  private void mockFormFields() {
    List<String> requiredFormFields = Arrays.asList("deferredUntil", "deferralReason", "deferralDate", "venue",
        "deferredDonor");
    when(formFieldRepository.getRequiredFormFields("DonorDeferral")).thenReturn(requiredFormFields);
  }

  @Test
  public void testValid() throws Exception {
    UUID locationId = UUID.randomUUID();
    LocationBackingForm venue = aLocationBackingForm().withId(locationId).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(DEFERRAL_REASON_ID).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();
    
    mockFormFields();
    when(locationRepository.verifyLocationExists(locationId)).thenReturn(true);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);
    when(deferralReasonRepository.verifyDeferralReasonExists(DEFERRAL_REASON_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidVenueDoesNotExist() throws Exception {
    UUID locationId = UUID.randomUUID();
    LocationBackingForm venue = aLocationBackingForm().withId(locationId).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(DEFERRAL_REASON_ID).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();

    mockFormFields();
    when(locationRepository.verifyLocationExists(locationId)).thenReturn(false);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);
    when(deferralReasonRepository.verifyDeferralReasonExists(DEFERRAL_REASON_ID)).thenReturn(true);
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
        .withDeferralReason(aDeferralReasonBackingForm().withId(DEFERRAL_REASON_ID).build())
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();

    mockFormFields();
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);
    when(deferralReasonRepository.verifyDeferralReasonExists(DEFERRAL_REASON_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on venue", errors.getFieldError("venue"));
   
  }

  @Test
  public void testInvalidDeferralReasonNotSpecified() throws Exception {
    UUID locationId = UUID.randomUUID();
    LocationBackingForm venue = aLocationBackingForm().withId(locationId).build();
    
    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();
    
    mockFormFields();
    when(locationRepository.verifyLocationExists(locationId)).thenReturn(true);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on deferralReason", errors.getFieldError("deferralReason"));
  }

  @Test
  public void testInvalidDeferralReasonDoesNotExist() throws Exception {
    UUID locationId = UUID.randomUUID();
    LocationBackingForm venue = aLocationBackingForm().withId(locationId).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(DEFERRAL_REASON_ID).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();
    
    mockFormFields();
    when(locationRepository.verifyLocationExists(locationId)).thenReturn(true);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);
    when(deferralReasonRepository.verifyDeferralReasonExists(DEFERRAL_REASON_ID)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on deferralReason", errors.getFieldError("deferralReason"));
  }
  
  @Test
  public void testInvalidDeferredUntilNotSpecified() throws Exception {
    UUID locationId = UUID.randomUUID();
    LocationBackingForm venue = aLocationBackingForm().withId(locationId).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(DEFERRAL_REASON_ID).build())
        .withVenue(venue)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();
    
    mockFormFields();
    when(locationRepository.verifyLocationExists(locationId)).thenReturn(true);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);
    when(deferralReasonRepository.verifyDeferralReasonExists(DEFERRAL_REASON_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on deferredUntil", errors.getFieldError("deferredUntil"));
  }
  
  @Test
  public void testInvalidDeferredUntilBeforeDeferralDate() throws Exception {
    UUID locationId = UUID.randomUUID();
    LocationBackingForm venue = aLocationBackingForm().withId(locationId).build();
    Date deferredUntil = new DateTime().minusDays(1).toDate();
    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(DEFERRAL_REASON_ID).build())
        .withVenue(venue)
        .withDeferredUntil(deferredUntil)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();
    
    mockFormFields();
    when(locationRepository.verifyLocationExists(locationId)).thenReturn(true);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);
    when(deferralReasonRepository.verifyDeferralReasonExists(DEFERRAL_REASON_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertEquals("Error code on deferredUntil", "deferral.deferredUntil.invalid", errors.getFieldError("deferredUntil").getCode()); 
  }

  @Test
  public void testInvalidDeferredDonorNotSpecified() throws Exception {
    UUID locationId = UUID.randomUUID();
    LocationBackingForm venue = aLocationBackingForm().withId(locationId).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(DEFERRAL_REASON_ID).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferralDate(new Date())
        .build();
    
    mockFormFields();
    when(locationRepository.verifyLocationExists(locationId)).thenReturn(true);
    when(deferralReasonRepository.verifyDeferralReasonExists(DEFERRAL_REASON_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on deferredDonor", errors.getFieldError("deferredDonor"));
  }
  
  @Test
  public void testInvalidDeferredDonorDoesNotExist() throws Exception {
    UUID locationId = UUID.randomUUID();
    LocationBackingForm venue = aLocationBackingForm().withId(locationId).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(DEFERRAL_REASON_ID).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .withDeferralDate(new Date())
        .build();
    
    mockFormFields();
    when(locationRepository.verifyLocationExists(locationId)).thenReturn(true);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(false);
    when(deferralReasonRepository.verifyDeferralReasonExists(DEFERRAL_REASON_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on deferredDonor", errors.getFieldError("deferredDonor"));
  }

  @Test
  public void testInvalidDeferralDateNotSpecified() throws Exception {
    UUID locationId = UUID.randomUUID();
    LocationBackingForm venue = aLocationBackingForm().withId(locationId).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReasonBackingForm().withId(DEFERRAL_REASON_ID).build())
        .withVenue(venue)
        .withDeferredUntil(DEFERRED_UNTIL)
        .withDeferredDonor(aDonorBackingForm().withId(DEFERRED_DONOR_ID).build())
        .build();
    
    mockFormFields();
    when(locationRepository.verifyLocationExists(locationId)).thenReturn(true);
    when(donorRepository.verifyDonorExists(DEFERRED_DONOR_ID)).thenReturn(true);
    when(deferralReasonRepository.verifyDeferralReasonExists(DEFERRAL_REASON_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on deferralDate", errors.getFieldError("deferralDate"));
  }

}
