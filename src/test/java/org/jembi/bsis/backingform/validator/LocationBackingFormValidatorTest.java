package org.jembi.bsis.backingform.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DivisionBackingFormBuilder.aDivisionBackingForm;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aDistributionSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aLocationBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aProcessingSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aReferralSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aTestingSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aUsageSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aVenueBackingForm;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.DivisionBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.DivisionRepository;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class LocationBackingFormValidatorTest extends UnitTestSuite {

  @InjectMocks
  private LocationBackingFormValidator locationBackingFormValidator;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private FormFieldRepository formFieldRepository;
  @Mock
  private DivisionRepository divisionRepository;

  @Test
  public void testValid() throws Exception {
    // set up data
    UUID divisionId = UUID.randomUUID();

    Division divisionLevel3 = aDivision().withId(divisionId).withLevel(3).build();

    DivisionBackingForm divisionForm = aDivisionBackingForm().withId(divisionId).withLevel(3).build();
    LocationBackingForm locationForm = aVenueBackingForm()
        .withName("VENUE")
        .withDivisionLevel3(divisionForm)
        .build();

    // set up mocks
    when(locationRepository.findLocationByName("VENUE")).thenReturn(null);
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(divisionLevel3);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(locationForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidTestingSite() throws Exception {
    // set up data
    UUID divisionId = UUID.randomUUID();

    Division divisionLevel3 = aDivision().withId(divisionId).withLevel(3).build();

    DivisionBackingForm divisionForm = aDivisionBackingForm().withId(divisionId).withLevel(3).build();
    LocationBackingForm locationForm = aTestingSiteBackingForm()
        .withName("TESTING")
        .withDivisionLevel3(divisionForm)
        .build();

    // set up mocks
    when(locationRepository.findLocationByName("TESTING")).thenReturn(null);
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(divisionLevel3);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(locationForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidProcessingSite() throws Exception {
    // set up data
    UUID divisionId = UUID.randomUUID();

    Division divisionLevel3 = aDivision().withId(divisionId).withLevel(3).build();

    DivisionBackingForm divisionForm = aDivisionBackingForm().withId(divisionId).withLevel(3).build();
    LocationBackingForm locationForm = aProcessingSiteBackingForm()
        .withName("PROCESSING")
        .withDivisionLevel3(divisionForm)
        .build();

    // set up mocks
    when(locationRepository.findLocationByName("PROCESSING")).thenReturn(null);
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(divisionLevel3);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(locationForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidDistributionSite() throws Exception {
    // set up data
    UUID divisionId = UUID.randomUUID();

    Division divisionLevel3 = aDivision().withId(divisionId).withLevel(3).build();

    DivisionBackingForm divisionForm = aDivisionBackingForm().withId(divisionId).withLevel(3).build();
    LocationBackingForm locationForm = aDistributionSiteBackingForm()
        .withName("DISTRIBUTION")
        .withDivisionLevel3(divisionForm)
        .build();

    // set up mocks
    when(locationRepository.findLocationByName("DISTRIBUTION")).thenReturn(null);
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(divisionLevel3);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(locationForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidUsageSite() throws Exception {
    // set up data
    UUID divisionId = UUID.randomUUID();

    Division divisionLevel3 = aDivision().withId(divisionId).withLevel(3).build();

    DivisionBackingForm divisionForm = aDivisionBackingForm().withId(divisionId).withLevel(3).build();
    LocationBackingForm locationForm = aUsageSiteBackingForm()
        .withName("USAGE")
        .withDivisionLevel3(divisionForm)
        .build();

    // set up mocks
    when(locationRepository.findLocationByName("USAGE")).thenReturn(null);
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(divisionLevel3);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(locationForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidReferralSite() throws Exception {
    // set up data
    UUID divisionId = UUID.randomUUID();

    Division divisionLevel3 = aDivision().withId(divisionId).withLevel(3).build();

    DivisionBackingForm divisionForm = aDivisionBackingForm().withId(divisionId).withLevel(3).build();
    LocationBackingForm locationForm = aReferralSiteBackingForm()
        .withName("REFERRAL")
        .withDivisionLevel3(divisionForm)
        .build();

    // set up mocks
    when(locationRepository.findLocationByName("REFERRAL")).thenReturn(null);
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(divisionLevel3);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(locationForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidUpdate() throws Exception {
    // set up data
    UUID divisionId = UUID.randomUUID();

    Division divisionLevel3 = aDivision().withId(divisionId).withLevel(3).build();

    UUID locationId = UUID.randomUUID();
    Location location = LocationBuilder.aLocation()
        .withId(locationId)
        .withName("LOCATION")
        .thatIsVenue()
        .withDivisionLevel3(divisionLevel3)
        .build();

    DivisionBackingForm divisionForm = aDivisionBackingForm().withId(divisionId).withLevel(3).build();
    LocationBackingForm locationForm = aLocationBackingForm()
        .withId(locationId)
        .withName("LOCATION")
        .thatIsVenue()
        .withDivisionLevel3(divisionForm)
        .build();

    // set up mocks
    when(locationRepository.findLocationByName("LOCATION")).thenReturn(location);
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(divisionLevel3);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(locationForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidBlankName() throws Exception {
    // set up data
    UUID divisionId = UUID.randomUUID();

    Division divisionLevel3 = aDivision().withId(divisionId).withLevel(3).build();

    DivisionBackingForm divisionForm = aDivisionBackingForm().withId(divisionId).withLevel(3).build();

    UUID locationId = UUID.randomUUID();
    LocationBackingForm locationForm = aLocationBackingForm()
        .withId(locationId)
        .withName("")
        .thatIsVenue()
        .withDivisionLevel3(divisionForm)
        .build();

    // Set up expectations
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(divisionLevel3);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(locationForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: Invalid blank name", errors.getFieldError("name"));
  }

  @Test
  public void testInvalidDuplicate() throws Exception {
    // set up data
    UUID divisionId = UUID.randomUUID();

    Division divisionLevel3 = aDivision().withId(divisionId).withLevel(3).build();
    UUID locationId1 = UUID.randomUUID();
    UUID locationId2 = UUID.randomUUID();
    Location duplicate = LocationBuilder.aLocation()
        .withId(locationId2)
        .withName("LOCATION")
        .thatIsVenue()
        .withDivisionLevel3(divisionLevel3)
        .build();

    DivisionBackingForm divisionForm = aDivisionBackingForm().withId(divisionId).withLevel(3).build();
    LocationBackingForm locationForm = aLocationBackingForm()
        .withId(locationId1)
        .withName("LOCATION")
        .thatIsVenue()
        .withDivisionLevel3(divisionForm)
        .build();

    // set up mocks
    when(locationRepository.findLocationByName("LOCATION")).thenReturn(duplicate);
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(divisionLevel3);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(locationForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: location exists", errors.getFieldError("name"));
  }

  @Test
  public void testNoLocationTypesSpecified() throws Exception {
    // set up data
    UUID divisionId = UUID.randomUUID();

    Division divisionLevel3 = aDivision().withId(divisionId).withLevel(3).build();

    DivisionBackingForm divisionForm = aDivisionBackingForm().withId(divisionId).withLevel(3).build();
    UUID locationId = UUID.randomUUID();
    LocationBackingForm locationForm = aLocationBackingForm()
        .withId(locationId)
        .withName("LOCATION")
        .withDivisionLevel3(divisionForm)
        .build();
    
    // Set up expectations
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(divisionLevel3);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(locationForm, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
  }
  
  @Test
  public void testValidateFormWithNoDivisionLevel3_shouldHaveFieldError() {
    // Set up fixture
    UUID locationId = UUID.randomUUID();
    LocationBackingForm form = aVenueBackingForm()
        .withId(locationId)
        .withName("Location")
        .withDivisionLevel3(null)
        .build();
    
    // Set up expectations
    when(locationRepository.findLocationByName("Location")).thenReturn(null);

    // Exercise SUT
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("divisionLevel3").getCode(), is("required"));
  }
  
  @Test
  public void testValidateFormWithNoDivisionLevel3Id_shouldHaveFieldError() {
    // Set up fixture
    UUID locationId = UUID.randomUUID();
    LocationBackingForm form = aVenueBackingForm()
        .withId(locationId)
        .withName("Location")
        .withDivisionLevel3(aDivisionBackingForm().withId(null).build())
        .build();
    
    // Set up expectations
    when(locationRepository.findLocationByName("Location")).thenReturn(null);

    // Exercise SUT
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("divisionLevel3").getCode(), is("required"));
  }
  
  @Test
  public void testValidateFormWithNoExistingDivisionLevel3_shouldHaveFieldError() {
    // Set up fixture
    UUID divisionId = UUID.randomUUID();
    UUID locationId = UUID.randomUUID();
    LocationBackingForm form = aVenueBackingForm()
        .withId(locationId)
        .withName("Location")
        .withDivisionLevel3(aDivisionBackingForm().withId(divisionId).build())
        .build();
    
    // Set up expectations
    when(locationRepository.findLocationByName("Location")).thenReturn(null);
    when(divisionRepository.findDivisionById(divisionId)).thenThrow(new NoResultException());

    // Exercise SUT
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("divisionLevel3").getCode(), is("invalid"));
  }
  
  @Test
  public void testValidateFormWithWrongDivisionLevel_shouldHaveFieldError() {
    // Set up fixture
    UUID divisionId = UUID.randomUUID();
    UUID locationId = UUID.randomUUID();
    LocationBackingForm form = aVenueBackingForm()
        .withId(locationId)
        .withName("Location")
        .withDivisionLevel3(aDivisionBackingForm().withId(divisionId).build())
        .build();
    
    Division divisionLevel2 = aDivision().withId(divisionId).withLevel(2).build();
    
    // Set up expectations
    when(locationRepository.findLocationByName("Location")).thenReturn(null);
    when(divisionRepository.findDivisionById(divisionId)).thenReturn(divisionLevel2);

    // Exercise SUT
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "location");
    locationBackingFormValidator.validate(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("divisionLevel3").getCode(), is("invalid"));
  }
}
