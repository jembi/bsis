package backingform.validator;

import static helpers.builders.DeferralBackingFormBuilder.aDeferralBackingForm;
import static helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static helpers.builders.LocationBuilder.aLocation;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import javax.persistence.NoResultException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import backingform.DeferralBackingForm;
import model.location.Location;
import repository.LocationRepository;
import suites.UnitTestSuite;

public class DeferralBackingFormValidatorTest extends UnitTestSuite {
  @InjectMocks
  DeferralBackingFormValidator deferralBackingFormValidator;
  @Mock
  LocationRepository locationRepository;

  @Test
  public void testValid() throws Exception {
    Location venue = aLocation().withId(1L).build();

    DeferralBackingForm deferralBackingForm = aDeferralBackingForm()
        .withDeferralReason(aDeferralReason().withId(1L).build())
        .withVenue(venue)
        .build();
    
    when(locationRepository.getLocation(1L)).thenReturn(venue);

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
        .build();

    when(locationRepository.getLocation(1L)).thenThrow(new NoResultException());

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
        .build();

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
        .build();
    
    when(locationRepository.getLocation(1L)).thenReturn(venue);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on deferralReason", errors.getFieldError("deferralReason"));
  }

}
