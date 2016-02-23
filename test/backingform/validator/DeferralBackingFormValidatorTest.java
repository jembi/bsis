package backingform.validator;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;

import javax.persistence.NoResultException;

import backingform.DeferralBackingForm;
import model.donordeferral.DeferralReason;
import model.location.Location;
import repository.LocationRepository;
import suites.UnitTestSuite;

import static helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static helpers.builders.LocationBuilder.aLocation;
import static org.mockito.Mockito.when;

public class DeferralBackingFormValidatorTest extends UnitTestSuite {
  @InjectMocks
  DeferralBackingFormValidator deferralBackingFormValidator;
  @Mock
  LocationRepository locationRepository;

  @Test
  public void testValid() throws Exception {
    DeferralReason deferralReason = aDeferralReason().withId(1L).build();
    Location venue = aLocation().withId(1L).build();

    DeferralBackingForm deferralBackingForm = new DeferralBackingForm();
    deferralBackingForm.setDeferralReason(deferralReason);
    deferralBackingForm.setVenue(venue);

    when(locationRepository.getLocation(1L)).thenReturn(venue);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInValidVenue() throws Exception {
    DeferralReason deferralReason = aDeferralReason().withId(1L).build();
    Location venue = aLocation().withId(1L).build();

    DeferralBackingForm deferralBackingForm = new DeferralBackingForm();
    deferralBackingForm.setDeferralReason(deferralReason);
    deferralBackingForm.setVenue(venue);

    when(locationRepository.getLocation(1L)).thenThrow(new NoResultException());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 1, errors.getErrorCount());
  }

  @Test
  public void testInValid() throws Exception {
    DeferralBackingForm deferralBackingForm = new DeferralBackingForm();
    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 1, errors.getErrorCount());
  }

}
