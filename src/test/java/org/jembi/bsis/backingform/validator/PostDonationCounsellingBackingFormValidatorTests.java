package org.jembi.bsis.backingform.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.helpers.builders.LocationBackingFormBuilder;
import org.jembi.bsis.helpers.builders.PostDonationCounsellingBackingFormBuilder;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class PostDonationCounsellingBackingFormValidatorTests extends UnitTestSuite {
  
  @InjectMocks
  private PostDonationCounsellingBackingFormValidator validator;

  @Mock
  private LocationRepository locationRepository;

  @Test
  public void testValidateValidFormThatIsFlaggedForCounselling_shouldntGetErrors() {
    // Set up data
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(null)
        .withCounsellingStatus(null)
        .thatIsFlaggedForCounselling()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(0));
  }
  
  
  @Test
  public void testValidateValidFormThatIsNotFlaggedForCounselling_shouldntGetErrors() throws ParseException {
    // Set up data
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(fmt.parse("01/02/2017"))
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsNotFlaggedForCounselling()
        .thatIsNotReferred()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(0));
  }
  
  @Test
  public void testValidateInValidFormThatIsNotFlaggedForCounselling_shouldReturnTwoErrors() {
    // Set up data
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(null)
        .withCounsellingStatus(null)
        .thatIsNotFlaggedForCounselling()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(2));
    assertThat(errors.getFieldError("counsellingDate").getCode(), is("errors.required"));
    assertThat(errors.getFieldError("counsellingStatus").getCode(), is("errors.required"));
  }
  

  @Test
  public void testValidateInValidFormThatIsNotFlaggedForCounsellingWithNullCounsellingDate_shouldReturnOneError() {
    // Set up data
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(null)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsNotFlaggedForCounselling()
        .thatIsNotReferred()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("counsellingDate").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateInValidFormThatIsNotFlaggedForCounsellingWithNullCounsellingStatus_shouldReturnOneError() throws ParseException {
    // Set up data
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(fmt.parse("05/02/2017"))
        .withCounsellingStatus(null)
        .thatIsNotFlaggedForCounselling()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("counsellingStatus").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateInValidFormThatIsFlaggedForCounsellingWithNotNullCounsellingStatus_shouldReturnOneError() {
    // Set up data
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(null)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsFlaggedForCounselling()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("counsellingStatus").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateInValidFormThatIsFlaggedForCounsellingWithNotNullCounsellingDate_shouldReturnOneError() throws ParseException {
    // Set up data
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(fmt.parse("01/02/2017"))
        .withCounsellingStatus(null)
        .thatIsFlaggedForCounselling()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("counsellingDate").getCode(), is("errors.invalid"));
  }  

  @Test
  public void testValidateInvalidFormWitStatusReceivedCounseillingAndReferredIsEmpty_shouldReturnOneError() throws ParseException {
    // Set up data
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(fmt.parse("01/02/2017"))
        .thatIsNotFlaggedForCounselling()
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("referred").getCode(), is("errors.invalid"));
    assertThat(errors.getFieldError("referred").getDefaultMessage(), is("Referred is required"));
  }
  
  @Test
  public void testValidateInvalidFormWithReferredDonorAndNullReferralSite_shouldReturnOneError() throws ParseException,NullPointerException {
    // Set up data
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .thatIsNotFlaggedForCounselling()
        .withCounsellingDate(fmt.parse("15/02/2017"))
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsReferred()
        .withReferralSite(null)
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("referralSite").getCode(), is("errors.required"));
    assertThat(errors.getFieldError("referralSite").getDefaultMessage(), is("Referral site is required"));
  }
  
  @Test
  public void testValidateInvalidFormWithLocationThatIsNotReferralSite_shouldReturnOneError() throws ParseException {
    // Set up data
    String locationName = "Not a Referral Site";
    Long locationId = 1L;

    Location location = aLocation()
        .withId(locationId)
        .withName(locationName)
        .build();

    LocationBackingForm locationForm = LocationBackingFormBuilder.aProcessingSiteBackingForm()
        .withId(1L)
        .withName(locationName)
        .build();

    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .thatIsNotFlaggedForCounselling()
        .withCounsellingDate(fmt.parse("15/02/2017"))
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsReferred()
        .withReferralSite(locationForm)
        .build();

    when(locationRepository.getLocation(1L)).thenReturn(location);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("referralSite").getCode(), is("errors.invalid"));
    assertThat(errors.getFieldError("referralSite").getDefaultMessage(), is("Location must be a referral site"));
  }
  
  @Test
  public void testValidatePostDonationCounsellingWithInvalidDate_shouldReturnError() {
    // Set up data
    String locationName = "A Referral Site";
    Long locationId = 1L;

    Location location = aLocation()
        .withId(locationId)
        .withName(locationName)
        .thatIsReferralSite()
        .build();

    LocationBackingForm locationForm = LocationBackingFormBuilder.aProcessingSiteBackingForm()
        .withId(1L)
        .withName(locationName)
        .thatIsReferralSite()
        .build();
    Date dateInTheFuture = new DateTime().plusDays(2).toDate();
    
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .thatIsNotFlaggedForCounselling()
        .withCounsellingDate(dateInTheFuture)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsReferred()
        .withReferralSite(locationForm)
        .build();
    
    when(locationRepository.getLocation(1L)).thenReturn(location);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");

    // Run test
    validator.validateForm(form, errors);
    
    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("counsellingDate").getCode(), is("errors.invalid"));
    assertThat(errors.getFieldError("counsellingDate").getDefaultMessage(), is("Counselling Date should not be in the future"));
  }
  
  @Test
  public void testValidatePostDonationCounsellingWithSameDate_shouldNotReturnError() {
    // Set up data
    String locationName = "A Referral Site";
    Long locationId = 1L;

    Location location = aLocation()
        .withId(locationId)
        .withName(locationName)
        .thatIsReferralSite()
        .build();

    LocationBackingForm locationForm = LocationBackingFormBuilder.aProcessingSiteBackingForm()
        .withId(1L)
        .withName(locationName)
        .thatIsReferralSite()
        .build();
    Date sameDayButLaterTime = new DateTime().plusHours(1).toDate();
    
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .thatIsNotFlaggedForCounselling()
        .withCounsellingDate(sameDayButLaterTime)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsReferred()
        .withReferralSite(locationForm)
        .build();
    
    when(locationRepository.getLocation(1L)).thenReturn(location);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");

    // Run test
    validator.validateForm(form, errors);
    
    // Verify
    assertThat(errors.getErrorCount(), is(0));
  }
  
  @Test
  public void testValidatePostDonationCounsellingWithPastDate_shouldNotReturnError() {
    // Set up data
    String locationName = "A Referral Site";
    Long locationId = 1L;

    Location location = aLocation()
        .withId(locationId)
        .withName(locationName)
        .thatIsReferralSite()
        .build();

    LocationBackingForm locationForm = LocationBackingFormBuilder.aProcessingSiteBackingForm()
        .withId(1L)
        .withName(locationName)
        .thatIsReferralSite()
        .build();
    Date sameDayButLaterTime = new DateTime().minusDays(10).toDate();
    
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .thatIsNotFlaggedForCounselling()
        .withCounsellingDate(sameDayButLaterTime)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsReferred()
        .withReferralSite(locationForm)
        .build();
    
    when(locationRepository.getLocation(1L)).thenReturn(location);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");

    // Run test
    validator.validateForm(form, errors);
    
    // Verify
    assertThat(errors.getErrorCount(), is(0));
  }
}