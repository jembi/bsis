package org.jembi.bsis.backingform.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBackingFormBuilder.aComponentBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aDistributionSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aUsageSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.OrderFormBackingFormBuilder.anOrderFormBackingForm;
import static org.jembi.bsis.helpers.builders.PatientBackingFormBuilder.aPatientBackingForm;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.OrderFormBackingForm;
import org.jembi.bsis.backingform.OrderFormItemBackingForm;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.order.OrderType;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.OrderFormRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class OrderFormBackingFormValidatorTest extends UnitTestSuite {

  @InjectMocks
  private OrderFormBackingFormValidator orderFormBackingFormValidator;

  @Mock
  private LocationRepository locationRepository;

  @Mock
  private FormFieldRepository formFieldRepository;
  
  @Mock
  private OrderFormItemBackingFormValidator orderFormItemBackingFormValidator; 

  @Mock
  private ComponentRepository componentRepository;

  @Mock
  private OrderFormRepository orderFormRepository;
  
  private static final UUID COMPONENT_ID = UUID.randomUUID();

  private static final UUID locationId_1 = UUID.randomUUID();
  private static final UUID locationId_2 = UUID.randomUUID();

  private OrderFormBackingForm getTransferOrderFormBackingForm() {
    LocationBackingForm dispatchedFrom =
        aDistributionSiteBackingForm().withName("LocFrom").withId(locationId_1).build();
    LocationBackingForm dispatchedTo = aDistributionSiteBackingForm().withName("LocTo").withId(locationId_2).build();
    Date orderDate = new Date();
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo).withOrderDate(orderDate).withOrderType(OrderType.TRANSFER).build();
    return backingForm;
  }
  
  private OrderFormBackingForm getIssueOrderFormBackingForm() {
    LocationBackingForm dispatchedFrom =
        aDistributionSiteBackingForm().withName("LocFrom").withId(locationId_1).build();
    LocationBackingForm dispatchedTo = aUsageSiteBackingForm().withName("LocTo").withId(locationId_2).build();
    Date orderDate = new Date();
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo).withOrderDate(orderDate).withOrderType(OrderType.ISSUE).build();
    return backingForm;
  }
  
  private OrderFormBackingForm getPatientRequestOrderFormBackingForm() {
    LocationBackingForm dispatchedFrom =
        aDistributionSiteBackingForm().withName("LocFrom").withId(locationId_1).build();
    LocationBackingForm dispatchedTo = aUsageSiteBackingForm().withName("LocTo").withId(locationId_2).build();
    Date orderDate = new Date();
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo).withOrderDate(orderDate).withOrderType(OrderType.PATIENT_REQUEST).build();
    return backingForm;
  }

  private OrderFormItemBackingForm getBaseOrderFormItemBackingForm() {
    OrderFormItemBackingForm backingForm = new OrderFormItemBackingForm();
    backingForm.setBloodGroup("A+");
    backingForm.setNumberOfUnits(22);
    UUID componentTypdId = UUID.randomUUID();
    ComponentTypeBackingForm componentType = new ComponentTypeBackingForm();
    componentType.setId(componentTypdId);
    backingForm.setComponentType(componentType);
    return backingForm;
  }

  private ComponentBackingForm getBaseOrderFormComponentBackingForm() {
    ComponentBackingForm component = aComponentBackingForm().withId(COMPONENT_ID).build();
    return component;
  }

  private Location getDispatchedFromLocation() {
    return aDistributionSite().withName("DispatchedFrom").withId(locationId_1).build();
  }

  private Location getTransferToLocation() {
    return aDistributionSite().withName("TransferTo").withId(locationId_2).build();
  }

  private Location getIssueToLocation() {
    return aUsageSite().withName("IssueTo").withId(locationId_2).build();
  }
  
  private Component getBaseComponent() {
    return aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withLocation(getDispatchedFromLocation()).build();
  }

  @Test
  public void testValidIssue_noErrors() {
    // set up data
    OrderFormBackingForm backingForm = getIssueOrderFormBackingForm();
    backingForm.setItems(Arrays.asList(getBaseOrderFormItemBackingForm()));
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(getBaseComponent());
    when(orderFormRepository.isComponentInAnotherOrderForm(null, COMPONENT_ID)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    assertThat(errors.getFieldErrorCount(), is(0));

  }
  
  @Test
  public void testValidTransfer_noErrors() {
    // set up data
    OrderFormBackingForm backingForm = getTransferOrderFormBackingForm();
    backingForm.setItems(Arrays.asList(getBaseOrderFormItemBackingForm()));
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getTransferToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(getBaseComponent());
    when(orderFormRepository.isComponentInAnotherOrderForm(null, COMPONENT_ID)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    assertThat(errors.getFieldErrorCount(), is(0));

  }
  
  @Test
  public void testValidatePatientRequest_noErrors() {
    // set up data
    OrderFormBackingForm backingForm = getPatientRequestOrderFormBackingForm();
    backingForm.setItems(Arrays.asList(getBaseOrderFormItemBackingForm()));
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));
    backingForm.setPatient(aPatientBackingForm().withName1("First Name").withName2("Last Name").build());

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(getBaseComponent());
    when(orderFormRepository.isComponentInAnotherOrderForm(null, COMPONENT_ID)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    assertThat(errors.getFieldErrorCount(), is(0));

  }

  @Test
  public void testValidatePatientRequestNoPatient_shouldHaveOneErrors() {
    // set up data
    OrderFormBackingForm backingForm = getPatientRequestOrderFormBackingForm();
    backingForm.setItems(Arrays.asList(getBaseOrderFormItemBackingForm()));
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(getBaseComponent());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("patient details are required", errors.getFieldErrors().get(0).getDefaultMessage());

  }

  @Test
  public void testValidatePatientRequestWithPatientNoNames_shouldHaveTwoErrors() {
    // set up data
    OrderFormBackingForm backingForm = getPatientRequestOrderFormBackingForm();
    backingForm.setItems(Arrays.asList(getBaseOrderFormItemBackingForm()));
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));
    backingForm.setPatient(aPatientBackingForm().build());

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(getBaseComponent());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("patient name1 is required", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("patient name2 is required", errors.getFieldErrors().get(1).getDefaultMessage());
  }

  @Test
  public void testValidatePatientRequestWithPatientWithOnlyName1_shouldHaveOneError() {
    // set up data
    OrderFormBackingForm backingForm = getPatientRequestOrderFormBackingForm();
    backingForm.setItems(Arrays.asList(getBaseOrderFormItemBackingForm()));
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));
    backingForm.setPatient(aPatientBackingForm().withName1("name1").build());

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(getBaseComponent());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("patient name2 is required", errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidatePatientRequestWithBirthDateAfterCurrentDate_shouldHaveOneError() {
    // set up data
    OrderFormBackingForm backingForm = getPatientRequestOrderFormBackingForm();
    backingForm.setItems(Arrays.asList(getBaseOrderFormItemBackingForm()));
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));
    backingForm.setPatient(aPatientBackingForm().withName1("First Name").withName2("Last Name").withDateOfBirth(new DateTime().plusDays(20).toDate()).build());
    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm"))
        .thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(getBaseComponent());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("Patient.dateOfBirth must be in the past", errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidatePatientRequestWithBirthDateBeforeCurrentDate_noErrors() {
    // set up data
    OrderFormBackingForm backingForm = getPatientRequestOrderFormBackingForm();
    backingForm.setItems(Arrays.asList(getBaseOrderFormItemBackingForm()));
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));
    backingForm.setPatient(aPatientBackingForm().withName1("First Name").withName2("Last Name").withDateOfBirth(new DateTime().minusDays(20).toDate()).build());
    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm"))
        .thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(getBaseComponent());
    when(orderFormRepository.isComponentInAnotherOrderForm(null, COMPONENT_ID)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    assertThat(errors.getFieldErrorCount(), is(0));
  }

  @Test
  public void testValidatePatientRequestWithPatientWithOnlyName2_shouldHaveOneError() {
    // set up data
    OrderFormBackingForm backingForm = getPatientRequestOrderFormBackingForm();
    backingForm.setItems(Arrays.asList(getBaseOrderFormItemBackingForm()));
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));
    backingForm.setPatient(aPatientBackingForm().withName2("name2").build());

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(getBaseComponent());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("patient name1 is required", errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidatePatientRequestWithPatientWithName1ExceedingMaxLength_shouldHaveOneError() {
    // set up data
    OrderFormBackingForm backingForm = getPatientRequestOrderFormBackingForm();
    backingForm.setItems(Arrays.asList(getBaseOrderFormItemBackingForm()));
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));
    backingForm.setPatient(aPatientBackingForm().withName1("very_long_name_exceeding_maximum_length_allowed").withName2("name2").build());

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(getBaseComponent());
    when(orderFormRepository.isComponentInAnotherOrderForm(null, COMPONENT_ID)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals(errors.getFieldErrors().size(), 1);
    Assert.assertEquals("patient.name1", errors.getFieldErrors().get(0).getField());
    Assert.assertEquals("Maximum length for this field is 20", errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidatePatientRequestWithPatientWithName2ExceedingMaxLength_shouldHaveOneError() {
    // set up data
    OrderFormBackingForm backingForm = getPatientRequestOrderFormBackingForm();
    backingForm.setItems(Arrays.asList(getBaseOrderFormItemBackingForm()));
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));
    backingForm.setPatient(aPatientBackingForm().withName1("name1").withName2("very_long_name_exceeding_maximum_length_allowed").build());

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(getBaseComponent());
    when(orderFormRepository.isComponentInAnotherOrderForm(null, COMPONENT_ID)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals(errors.getFieldErrors().size(), 1);
    Assert.assertEquals("patient.name2", errors.getFieldErrors().get(0).getField());
    Assert.assertEquals("Maximum length for this field is 20", errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidateDispatchedFromAndTo_getRequiredError() {
    // set up data
    OrderFormBackingForm backingForm = getIssueOrderFormBackingForm();
    backingForm.setDispatchedFrom(null);
    backingForm.setDispatchedTo(null);

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("dispatchedFrom is required", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("dispatchedTo is required", errors.getFieldErrors().get(1).getDefaultMessage());
  }

  @Test
  public void testValidateDispatchedFromAndToId_getInvalidError() {
    // set up data
    OrderFormBackingForm backingForm = getIssueOrderFormBackingForm();

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenThrow(new NoResultException());
    when(locationRepository.getLocation(locationId_2)).thenThrow(new NoResultException());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("Invalid dispatchedFrom", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("Invalid dispatchedTo", errors.getFieldErrors().get(1).getDefaultMessage());
  }

  @Test
  public void testValidateDispatchedFromMustBeDistributionSite_getInvalidLocationTypeError() {
    // set up data
    OrderFormBackingForm backingForm = getIssueOrderFormBackingForm();

    // dispatchedFrom can't be a venue
    Location venue1 = aVenue().withId(locationId_1).build();

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(venue1);
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("dispatchedFrom").getCode(), is("invalidType"));
  }

  @Test
  public void testValidateTransferToMustBeDistributionSite_getInvalidLocationTypeError() {
    // set up data
    OrderFormBackingForm backingForm = getTransferOrderFormBackingForm();

    // can't transfer to a usageSite
    Location usageSite = aUsageSite().withId(locationId_2).build();

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(usageSite);
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("dispatchedTo").getCode(), is("invalidType"));
  }
  
  @Test
  public void testValidateIssueToMustBeUsageSite_getInvalidLocationTypeError() {
    // set up data
    OrderFormBackingForm backingForm = getIssueOrderFormBackingForm();

    // can't issue to a distribution site
    Location distributionSite = aDistributionSite().withId(locationId_2).build();

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(distributionSite);
    when(formFieldRepository.getRequiredFormFields("OrderForm"))
        .thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("dispatchedTo").getCode(), is("invalidType"));
  }

  @Test
  public void testValidatePatientRequestIssueToMustBeUsageSite_getInvalidLocationTypeError() {
    // set up data
    OrderFormBackingForm backingForm = getPatientRequestOrderFormBackingForm();
    backingForm.setPatient(aPatientBackingForm().withName1("First Name").withName2("Last Name").build());

    // can't issue a patient request to a distribution site
    Location distributionSite = aDistributionSite().withId(locationId_2).build();

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(distributionSite);
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("dispatchedTo").getCode(), is("invalidType"));
  }
  
  @Test
  public void testValidateOrderFormBackingForm_requiredCommonFieldsErrors() {
    // set up data
    OrderFormBackingForm backingForm = getIssueOrderFormBackingForm();
    backingForm.setType(null);
    backingForm.setStatus(null);
    backingForm.setOrderDate(null);

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "orderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("This information is required", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("orderDate", errors.getFieldErrors().get(0).getField());

    Assert.assertEquals("This information is required", errors.getFieldErrors().get(1).getDefaultMessage());
    Assert.assertEquals("status", errors.getFieldErrors().get(1).getField());

    Assert.assertEquals("This information is required", errors.getFieldErrors().get(2).getDefaultMessage());
    Assert.assertEquals("type", errors.getFieldErrors().get(2).getField());
  }

  @Test
  public void testValidateComponentLocation_invalidLocation() {
    // set up data
    OrderFormBackingForm backingForm = getIssueOrderFormBackingForm();
    
    // create a component with a different location from dispatchedFrom
    Location differentLocation = aDistributionSite().withName("DifferentLocation").withId(UUID.randomUUID()).build();
    Component component =
        aComponent().withInventoryStatus(InventoryStatus.IN_STOCK)
        .withLocation(differentLocation).build();
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(component);
    when(orderFormRepository.isComponentInAnotherOrderForm(null, COMPONENT_ID)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component doesn't exist in DispatchedFrom", errors.getFieldErrors().get(0).getDefaultMessage());
  }
  
  @Test
  public void testValidateComponentInventoryStatusRemoved_invalidInventoryStatus() {
    // set up data
    OrderFormBackingForm backingForm = getIssueOrderFormBackingForm();
    
    // create a component REMOVED
    Component component =
        aComponent().withInventoryStatus(InventoryStatus.REMOVED)
        .withLocation(getDispatchedFromLocation()).build();
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(component);
    when(orderFormRepository.isComponentInAnotherOrderForm(null, COMPONENT_ID)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component inventory status must be IN_STOCK", errors.getFieldErrors().get(0).getDefaultMessage());
  }
  
  @Test
  public void testValidateComponentInventoryStatusNotLabelled_invalidInventoryStatus() {
    // set up data
    OrderFormBackingForm backingForm = getIssueOrderFormBackingForm();
    
    // create a component NOT_IN_STOCK
    Component component =
        aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withLocation(getDispatchedFromLocation()).build();
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(component);
    when(orderFormRepository.isComponentInAnotherOrderForm(null, COMPONENT_ID)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component inventory status must be IN_STOCK", errors.getFieldErrors().get(0).getDefaultMessage());
  }
  
  @Test
  public void testValidateComponentNotFound_invalidComponentIdError() {
    // set up data
    OrderFormBackingForm backingForm = getIssueOrderFormBackingForm();
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component id is invalid.", errors.getFieldErrors().get(0).getDefaultMessage());
  }
  
  @Test
  public void testValidateNoComponentId_requiredComponentIdError() {
    // set up data
    OrderFormBackingForm backingForm = getIssueOrderFormBackingForm();
    
    // component backing form with null id
    ComponentBackingForm componentBackingForm = aComponentBackingForm().withId(null).build();
    backingForm.setComponents(Arrays.asList(componentBackingForm));

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component id is required.", errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidateComponentInAnotherOrderForm_invalidComponentIdError() {
    // set up data
    OrderFormBackingForm backingForm = getIssueOrderFormBackingForm();
    Component component =
        aComponent().withInventoryStatus(InventoryStatus.IN_STOCK)
        .withLocation(getDispatchedFromLocation()).build();
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(locationId_1)).thenReturn(getDispatchedFromLocation());
    when(locationRepository.getLocation(locationId_2)).thenReturn(getIssueToLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(component);
    when(orderFormRepository.isComponentInAnotherOrderForm(null, COMPONENT_ID)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    assertThat(errors.getFieldErrors().get(0).getCode(), is("errors.invalidComponentInAnotherOrderForm"));
  }
}
