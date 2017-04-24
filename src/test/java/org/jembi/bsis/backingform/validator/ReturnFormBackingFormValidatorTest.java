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
import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.ReturnFormBackingForm;
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
  
  private static final UUID COMPONENT_ID = UUID.randomUUID();

  private static final UUID locationId1 = UUID.randomUUID();
  private static final UUID locationId2 = UUID.randomUUID();

  private ReturnFormBackingForm getBaseReturnFormBackingForm() throws ParseException {
    LocationBackingForm returnedFrom = aUsageSiteBackingForm().withName("LocFrom").withId(locationId1).build();
    LocationBackingForm returnedTo = aDistributionSiteBackingForm().withName("LocTo").withId(locationId2).build();
    ReturnFormBackingForm backingForm = aReturnFormBackingForm()
        .withReturnedFrom(returnedFrom)
        .withReturnedTo(returnedTo)
        .withReturnDate(new Date())
        .withReturnStatus(ReturnStatus.CREATED)
        .build();
    return backingForm;
  }

  private Location getBaseReturnedFrom() {
    return aUsageSite().withName("LocFrom").withId(locationId1).build();
  }

  private Location getBaseReturnedTo() {
    return aDistributionSite().withName("LocTo").withId(locationId2).build();
  }

  private ComponentBackingForm getBaseReturnFormComponentBackingForm() {
    ComponentBackingForm component = aComponentBackingForm().withId(COMPONENT_ID).build();
    return component;
  }

  @Test
  public void testValid_noErrors() throws Exception {
    // set up data
    ReturnFormBackingForm backingForm = getBaseReturnFormBackingForm();

    // set up mocks
    when(locationRepository.getLocation(locationId1)).thenReturn(getBaseReturnedFrom());
    when(locationRepository.getLocation(locationId2)).thenReturn(getBaseReturnedTo());
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
    when(locationRepository.getLocation(locationId1)).thenThrow(NoResultException.class);
    when(locationRepository.getLocation(locationId2)).thenThrow(NoResultException.class);

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
    Location venue1 = aVenue().withId(locationId1).build();
    Location venue2 = aVenue().withId(locationId2).build();

    // set up mocks
    when(locationRepository.getLocation(locationId1)).thenReturn(venue1);
    when(locationRepository.getLocation(locationId2)).thenReturn(venue2);

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
    when(locationRepository.getLocation(locationId1)).thenReturn(getBaseReturnedFrom());
    when(locationRepository.getLocation(locationId2)).thenReturn(getBaseReturnedTo());
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
        .withLocation(getBaseReturnedFrom()).withId(COMPONENT_ID).build();
 
    backingForm.setComponents(Arrays.asList(getBaseReturnFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(locationId1)).thenReturn(getBaseReturnedFrom());
    when(locationRepository.getLocation(locationId2)).thenReturn(getBaseReturnedTo());
    when(formFieldRepository.getRequiredFormFields("ReturnForm")).thenReturn(Arrays.asList(new String[] {"returnDate", "status"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(component);

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
    UUID componentId2 = UUID.randomUUID();
    UUID componentId3 = UUID.randomUUID();
    UUID componentId4 = UUID.randomUUID();
    UUID componentId5 = UUID.randomUUID();
    UUID componentId6 = UUID.randomUUID();
    UUID componentId7 = UUID.randomUUID();
    UUID componentId8 = UUID.randomUUID();
    // create components with statuses QUARANTINED, AVAILABLE, EXPIRED, TRANSFUSED, UNSAFE, DISCARDED and PROCESSED
    Component component1 = aComponent().withStatus(ComponentStatus.QUARANTINED).withLocation(getBaseReturnedFrom()).build();
    Component component2 = aComponent().withStatus(ComponentStatus.AVAILABLE).withLocation(getBaseReturnedFrom()).build();
    Component component3 = aComponent().withStatus(ComponentStatus.EXPIRED).withLocation(getBaseReturnedFrom()).build();
    Component component5 = aComponent().withStatus(ComponentStatus.TRANSFUSED).withLocation(getBaseReturnedFrom()).build();
    Component component6 = aComponent().withStatus(ComponentStatus.UNSAFE).withLocation(getBaseReturnedFrom()).build();
    Component component7 = aComponent().withStatus(ComponentStatus.DISCARDED).withLocation(getBaseReturnedFrom()).build();
    Component component8 = aComponent().withStatus(ComponentStatus.PROCESSED).withLocation(getBaseReturnedFrom()).build();
    ComponentBackingForm componentBackingForm1 = aComponentBackingForm().withId(COMPONENT_ID).build();
    ComponentBackingForm componentBackingForm2 = aComponentBackingForm().withId(componentId2).build();
    ComponentBackingForm componentBackingForm3 = aComponentBackingForm().withId(componentId3).build();
    ComponentBackingForm componentBackingForm5 = aComponentBackingForm().withId(componentId5).build();
    ComponentBackingForm componentBackingForm6 = aComponentBackingForm().withId(componentId6).build();
    ComponentBackingForm componentBackingForm7 = aComponentBackingForm().withId(componentId7).build();
    ComponentBackingForm componentBackingForm8 = aComponentBackingForm().withId(componentId8).build();
    List<ComponentBackingForm> componentBackingForms = new ArrayList<>();
    componentBackingForms.add(componentBackingForm1);
    componentBackingForms.add(componentBackingForm2);
    componentBackingForms.add(componentBackingForm3);
    componentBackingForms.add(componentBackingForm5);
    componentBackingForms.add(componentBackingForm6);
    componentBackingForms.add(componentBackingForm7);
    componentBackingForms.add(componentBackingForm8);
    backingForm.setComponents(componentBackingForms);

    // set up mocks
    when(locationRepository.getLocation(locationId1)).thenReturn(getBaseReturnedFrom());
    when(locationRepository.getLocation(locationId2)).thenReturn(getBaseReturnedTo());
    when(formFieldRepository.getRequiredFormFields("ReturnForm")).thenReturn(Arrays.asList(new String[] {"returnDate", "status"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(component1);
    when(componentRepository.findComponent(componentId2)).thenReturn(component2);
    when(componentRepository.findComponent(componentId3)).thenReturn(component3);
    when(componentRepository.findComponent(componentId5)).thenReturn(component5);
    when(componentRepository.findComponent(componentId6)).thenReturn(component6);
    when(componentRepository.findComponent(componentId7)).thenReturn(component7);
    when(componentRepository.findComponent(componentId8)).thenReturn(component8);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ReturnForm");
    returnFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("8 errors", 7, errors.getErrorCount());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(1).getDefaultMessage());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(2).getDefaultMessage());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(3).getDefaultMessage());  
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(4).getDefaultMessage());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(5).getDefaultMessage());
    Assert.assertEquals("component status must be ISSUED", errors.getFieldErrors().get(6).getDefaultMessage());
  }

  @Test
  public void testValidateComponentNotFound_getInvalidComponentIdError() throws Exception {
    // set up data
    ReturnFormBackingForm backingForm = getBaseReturnFormBackingForm();
    backingForm.setComponents(Arrays.asList(getBaseReturnFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(locationId1)).thenReturn(getBaseReturnedFrom());
    when(locationRepository.getLocation(locationId2)).thenReturn(getBaseReturnedTo());
    when(formFieldRepository.getRequiredFormFields("ReturnForm")).thenReturn(Arrays.asList(new String[] {"returnDate", "status"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(null);

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
    when(locationRepository.getLocation(locationId1)).thenReturn(getBaseReturnedFrom());
    when(locationRepository.getLocation(locationId2)).thenReturn(getBaseReturnedTo());
    when(formFieldRepository.getRequiredFormFields("ReturnForm")).thenReturn(Arrays.asList(new String[] {"returnDate", "status"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ReturnForm");
    returnFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component id is required.", errors.getFieldErrors().get(0).getDefaultMessage());
  }
}
