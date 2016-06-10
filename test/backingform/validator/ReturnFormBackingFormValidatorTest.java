package backingform.validator;

import static helpers.builders.ComponentBackingFormBuilder.aComponentBackingForm;
import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.LocationBackingFormBuilder.aDistributionSiteBackingForm;
import static helpers.builders.LocationBackingFormBuilder.aUsageSiteBackingForm;
import static helpers.builders.LocationBuilder.aDistributionSite;
import static helpers.builders.LocationBuilder.aUsageSite;
import static helpers.builders.LocationBuilder.aVenue;
import static helpers.builders.ReturnFormBackingFormBuilder.aReturnFormBackingForm;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.NoResultException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import backingform.ComponentBackingForm;
import backingform.LocationBackingForm;
import backingform.ReturnFormBackingForm;
import model.component.Component;
import model.inventory.InventoryStatus;
import model.location.Location;
import model.returnform.ReturnStatus;
import repository.ComponentRepository;
import repository.FormFieldRepository;
import repository.LocationRepository;

@RunWith(MockitoJUnitRunner.class)
public class ReturnFormBackingFormValidatorTest {

  @InjectMocks
  private ReturnFormBackingFormValidator returnFormBackingFormValidator;

  @Mock
  private LocationRepository locationRepository;

  @Mock
  private FormFieldRepository formFieldRepository;

  @Mock
  private ComponentRepository componentRepository;

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

  private ComponentBackingForm getBaseReturnFormComponentBackingForm() {
    ComponentBackingForm component = aComponentBackingForm().withId(1L).build();
    return component;
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

  @Test
  public void testValidateComponentInventoryStatusNotLabelledAndInStock_invalidInventoryStatusForBothComponents() throws Exception {
    // set up data
    ReturnFormBackingForm backingForm = getBaseReturnFormBackingForm();

    // create components with statuses NOT_LABELLED and IN_STOCK
    Component component1 = aComponent().withInventoryStatus(InventoryStatus.NOT_LABELLED).withLocation(getBaseReturnedFrom()).build();
    Component component2 = aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withLocation(getBaseReturnedFrom()).build();
    ComponentBackingForm componentBackingForm1 = aComponentBackingForm().withId(1L).build();
    ComponentBackingForm componentBackingForm2 = aComponentBackingForm().withId(1L).build();
    List<ComponentBackingForm> componentBackingForms = new ArrayList<>();
    componentBackingForms.add(componentBackingForm1);
    componentBackingForms.add(componentBackingForm2);
    backingForm.setComponents(componentBackingForms);

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseReturnedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseReturnedTo());
    when(formFieldRepository.getRequiredFormFields("ReturnForm")).thenReturn(Arrays.asList(new String[] {"returnDate", "status"}));
    when(componentRepository.findComponent(1L)).thenReturn(component1);
    when(componentRepository.findComponent(1L)).thenReturn(component2);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ReturnForm");
    returnFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component inventory status must be REMOVED", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("component inventory status must be REMOVED", errors.getFieldErrors().get(1).getDefaultMessage());
  }

  @Test
  public void testValidateComponentNotFound_invalidComponentIdError() throws Exception {
    // set up data
    ReturnFormBackingForm backingForm = getBaseReturnFormBackingForm();
    backingForm.setComponents(Arrays.asList(getBaseReturnFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseReturnedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseReturnedTo());
    when(formFieldRepository.getRequiredFormFields("ReturnForm")).thenReturn(Arrays.asList(new String[] {"returnDate", "status"}));
    when(componentRepository.findComponent(1L)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ReturnForm");
    returnFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component id is invalid.", errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidateNoComponentId_requiredComponentIdError() throws Exception {
    // set up data
    ReturnFormBackingForm backingForm = getBaseReturnFormBackingForm();

    // component backing form with null id
    ComponentBackingForm componentBackingForm = aComponentBackingForm().withId(null).build();
    backingForm.setComponents(Arrays.asList(componentBackingForm));

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseReturnedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseReturnedTo());
    when(formFieldRepository.getRequiredFormFields("ReturnForm")).thenReturn(Arrays.asList(new String[] {"returnDate", "status"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ReturnForm");
    returnFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component id is required.", errors.getFieldErrors().get(0).getDefaultMessage());
  }
}
