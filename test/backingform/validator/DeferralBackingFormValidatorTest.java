package backingform.validator;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;

import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;

import backingform.DeferralBackingForm;
import model.donordeferral.DeferralReason;
import model.location.Location;
import suites.UnitTestSuite;

import static helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static helpers.builders.LocationBuilder.aLocation;

public class DeferralBackingFormValidatorTest extends UnitTestSuite {
  @InjectMocks
  DeferralBackingFormValidator deferralBackingFormValidator;

  @Test
  public void testValid() throws Exception {
    DeferralReason deferralReason = aDeferralReason().withId(1L).build();
    Location venue = aLocation().withId(1L).build();

    DeferralBackingForm deferralBackingForm = new DeferralBackingForm();
    deferralBackingForm.setDeferralReason(deferralReason);
    deferralBackingForm.setVenue(venue);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "deferral");
    deferralBackingFormValidator.validate(deferralBackingForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

}
