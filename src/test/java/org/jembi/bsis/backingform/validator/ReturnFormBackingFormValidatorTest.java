package org.jembi.bsis.backingform.validator;

import static org.jembi.bsis.helpers.builders.ComponentBackingFormBuilder.aComponentBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aDistributionSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aUsageSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.ReturnFormBackingFormBuilder.aReturnFormBackingForm;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.ReturnFormBackingForm;
import org.jembi.bsis.backingform.validator.ReturnFormBackingFormValidator;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.returnform.ReturnStatus;
import org.jembi.bsis.repository.ComponentRepository;
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
  public void testValidateReturnFormBackingForm_getRequiredCommonFieldsErrors() throws Exception {
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
  public void testValidateComponentIssued_getNoErrors() throws Exception {
    // set up data
    ReturnFormBackingForm backingForm = getBaseReturnFormBackingForm();

    // create component that can be returned: status ISSUED
    Component component = aComponent()
        .withStatus(ComponentStatus.ISSUED)
        .withLocation(getBaseReturnedFrom()).withId(1L).build();
 
    backingForm.setComponents(Arrays.asList(getBaseReturnFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseReturnedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseReturnedTo());
    when(formFieldRepository.getRequiredFormFields("ReturnForm")).thenReturn(Arrays.asList(new String[] {"returnDate", "status"}));
    when(componentRepository.findComponent(1L)).thenReturn(component);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ReturnForm");
    returnFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidateComponentWithStatusOtherThanIssued_getInvalidStatusErrors() throws Exception {
    // set up data
    ReturnFormBackingForm backingForm = getBaseReturnFormBackingForm();

    // create components with statuses QUARANTINED, AVAILABLE, EXPIRED, SPLIT, USED, UNSAFE, DISCARDED and PROCESSED
    Component component1 = aComponent().withStatus(ComponentStatus.QUARANTINED).withLocation(getBaseReturnedFrom()).build();
    Component component2 = aComponent().withStatus(ComponentStatus.AVAILABLE).withLocation(getBaseReturnedFrom()).build();
    Component component3 = aComponent().withStatus(ComponentStatus.EXPIRED).withLocation(getBaseReturnedFrom()).build();
    Component component4 = aComponent().withStatus(ComponentStatus.SPLIT).withLocation(getBaseReturnedFrom()).build();
    Component component5 = aComponent().withStatus(ComponentStatus.USED).withLocation(getBaseReturnedFrom()).build();
    Component component6 = aComponent().withStatus(ComponentStatus.UNSAFE).withLocation(getBaseReturnedFrom()).build();
    Component component7 = aComponent().withStatus(ComponentStatus.DISCARDED).withLocation(getBaseReturnedFrom()).build();
    Component component8 = aComponent().withStatus(ComponentStatus.PROCESSED).withLocation(getBaseReturnedFrom()).build();
    ComponentBackingForm componentBackingForm1 = aComponentBackingForm().withId(1L).build();
    ComponentBackingForm componentBackingForm2 = aComponentBackingForm().withId(2L).build();
    ComponentBackingForm componentBackingForm3 = aComponentBackingForm().withId(3L).build();
    ComponentBackingForm componentBackingForm4 = aComponentBackingForm().withId(4L).build();
    ComponentBackingForm componentBackingForm5 = aComponentBackingForm().withId(5L).build();
    ComponentBackingForm componentBackingForm6 = aComponentBackingForm().withId(6L).build();
    ComponentBackingForm componentBackingForm7 = aComponentBackingForm().withId(7L).build();
    ComponentBackingForm componentBackingForm8 = aComponentBackingForm().withId(8L).build();
    List<ComponentBackingForm> componentBackingForms = new ArrayList<>();
    componentBackingForms.add(componentBackingForm1);
    componentBackingForms.add(componentBackingForm2);
    componentBackingForms.add(componentBackingForm3);
    componentBackingForms.add(componentBackingForm4);
    componentBackingForms.add(componentBackingForm5);
    componentBackingForms.add(componentBackingForm6);
    componentBackingForms.add(componentBackingForm7);
    componentBackingForms.add(componentBackingForm8);
    backingForm.setComponents(componentBackingForms);

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseReturnedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseReturnedTo());
    when(formFieldRepository.getRequiredFormFields("ReturnForm")).thenReturn(Arrays.asList(new String[] {"returnDate", "status"}));
    when(componentRepository.findComponent(1L)).thenReturn(component1);
    when(componentRepository.findComponent(2L)).thenReturn(component2);
    when(componentRepository.findComponent(3L)).thenReturn(component3);
    when(componentRepository.findComponent(4L)).thenReturn(component4);
    when(componentRepository.findComponent(5L)).thenReturn(component5);
    when(componentRepository.findComponent(6L)).thenReturn(component6);
    when(componentRepository.findComponent(7L)).thenReturn(component7);
    when(componentRepository.findComponent(8L)).thenReturn(component8);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ReturnForm");
    returnFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("8 errors", 8, errors.getErrorCount());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(1).getDefaultMessage());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(2).getDefaultMessage());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(3).getDefaultMessage());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(4).getDefaultMessage());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(5).getDefaultMessage());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(6).getDefaultMessage());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(7).getDefaultMessage());
  }

  @Test
  public void testValidateComponentNotFound_getInvalidComponentIdError() throws Exception {
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
  public void testValidateNoComponentId_getRequiredComponentIdError() throws Exception {
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
