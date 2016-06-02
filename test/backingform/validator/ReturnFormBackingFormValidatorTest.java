package backingform.validator;

import static helpers.builders.LocationBackingFormBuilder.aDistributionSiteBackingForm;
import static helpers.builders.LocationBackingFormBuilder.aUsageSiteBackingForm;
import static helpers.builders.LocationBuilder.aDistributionSite;
import static helpers.builders.LocationBuilder.aUsageSite;
import static helpers.builders.LocationBuilder.aVenue;
import static helpers.builders.ReturnFormBackingFormBuilder.aReturnFormBackingForm;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.NoResultException;

import model.location.Location;
import model.returnform.ReturnStatus;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import repository.FormFieldRepository;
import repository.LocationRepository;
import backingform.LocationBackingForm;
import backingform.ReturnFormBackingForm;

@RunWith(MockitoJUnitRunner.class)
public class ReturnFormBackingFormValidatorTest {

  @InjectMocks
  private ReturnFormBackingFormValidator returnFormBackingFormValidator;

  @Mock
  private LocationRepository locationRepository;

  @Mock
  private FormFieldRepository formFieldRepository;

  private ReturnFormBackingForm getBaseReturnFormBackingForm() throws ParseException {
    LocationBackingForm returnedFrom = aUsageSiteBackingForm().withName("LocFrom").withId(1l).build();
    LocationBackingForm returnedTo = aDistributionSiteBackingForm().withName("LocTo").withId(2l).build();
    ReturnFormBackingForm backingForm = aReturnFormBackingForm()
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnDate(new Date())
        .withReturnStatus(ReturnStatus.CREATED)
        .build();
    return backingForm;
  }

  private Location getBaseReturnedFrom() {
    return aUsageSite().withName("LocFrom").withId(1l).build();
  }

  private Location getBaseReturnedTo() {
    return aDistributionSite().withName("LocTo").withId(2l).build();
  }

  @Test
  public void testValid_noErrors() throws Exception {
    // set up data
    ReturnFormBackingForm backingForm = getBaseReturnFormBackingForm();

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseReturnedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseReturnedTo());
    when(formFieldRepository.getRequiredFormFields("ReturnForm")).thenReturn(Arrays.asList(new String[] {"returnDate", "status"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ReturnForm");
    returnFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());

  }

  @Test
  public void testValidateReturnedFromAndTo_getRequiredError() throws Exception {
    // set up data
    ReturnFormBackingForm backingForm = getBaseReturnFormBackingForm();
    backingForm.setReturnedFrom(null);
    backingForm.setReturnedTo(null);

    // set up mocks

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ReturnForm");
    returnFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("returnedFrom is required", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("returnedTo is required", errors.getFieldErrors().get(1).getDefaultMessage());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testValidateReturnedFromAndToId_getInvalidError() throws Exception {
    // set up data
    ReturnFormBackingForm backingForm = getBaseReturnFormBackingForm();

    // set up mocks
    when(locationRepository.getLocation(1l)).thenThrow(NoResultException.class);
    when(locationRepository.getLocation(2l)).thenThrow(NoResultException.class);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ReturnForm");
    returnFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("Invalid returnedFrom", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("Invalid returnedTo", errors.getFieldErrors().get(1).getDefaultMessage());
  }

  @Test
  public void testValidateReturnFromAndToCantBeVenue_getInvalidLocationTypeError() throws Exception {
    // set up data
    ReturnFormBackingForm backingForm = getBaseReturnFormBackingForm();

    // returnedFrom and to can't be a venue
    Location venue1 = aVenue().withId(1l).build();
    Location venue2 = aVenue().withId(2l).build();

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(venue1);
    when(locationRepository.getLocation(2l)).thenReturn(venue2);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ReturnForm");
    returnFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("returnedFrom must be a usage site", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("returnedTo must be a distribution site", errors.getFieldErrors().get(1).getDefaultMessage());
  }

  @Test
  public void testValidateReturnFormBackingForm_requiredCommonFieldsErrors() throws Exception {
    // set up data
    ReturnFormBackingForm backingForm = getBaseReturnFormBackingForm();
    backingForm.setStatus(null);
    backingForm.setReturnDate(null);

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseReturnedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseReturnedTo());
    when(formFieldRepository.getRequiredFormFields("ReturnForm")).thenReturn(Arrays.asList(new String[] {"returnDate", "status"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ReturnForm");
    returnFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("This information is required", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("returnDate", errors.getFieldErrors().get(0).getField());

    Assert.assertEquals("This information is required", errors.getFieldErrors().get(1).getDefaultMessage());
    Assert.assertEquals("status", errors.getFieldErrors().get(1).getField());
  }
}
