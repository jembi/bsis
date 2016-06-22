package org.jembi.bsis.backingform.validator;

import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.validator.LocationBackingFormValidator;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(MockitoJUnitRunner.class)
public class LocationBackingFormValidatorTest {

  @InjectMocks
  LocationBackingFormValidator locationBackingFormValidator;
  @Mock
  LocationRepository locationRepository;
  @Mock
  FormFieldRepository formFieldRepository;

  @Test
  public void testValid() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation()
        .withName("LOCATION")
        .thatIsVenue()
        .build();

    LocationBackingForm form = new LocationBackingForm();
    form.setLocation(location);

    // set up mocks
    when(locationRepository.findLocationByName("LOCATION")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidUpdate() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation()
        .withId(1l)
        .withName("LOCATION")
        .thatIsVenue()
        .build();

    LocationBackingForm form = new LocationBackingForm();
    form.setLocation(location);

    // set up mocks
    when(locationRepository.findLocationByName("LOCATION")).thenReturn(location);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidBlankName() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation()
        .withId(1l)
        .withName("")
        .thatIsVenue()
        .build();

    LocationBackingForm form = new LocationBackingForm();
    form.setLocation(location);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: Invalid blank name", errors.getFieldError("name"));
  }

  @Test
  public void testInvalidDuplicate() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation()
        .withId(1l)
        .withName("LOCATION")
        .thatIsVenue()
        .build();

    Location duplicate = LocationBuilder.aLocation()
        .withId(2l)
        .withName("LOCATION")
        .thatIsVenue()
        .build();

    LocationBackingForm form = new LocationBackingForm();
    form.setLocation(location);

    // set up mocks
    when(locationRepository.findLocationByName("LOCATION")).thenReturn(duplicate);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: location exists", errors.getFieldError("name"));
  }

  @Test
  public void testNoLocationTypesSpecified() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation().withId(1l).withName("LOCATION").build();

    LocationBackingForm form = new LocationBackingForm();
    form.setLocation(location);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
  }
}
