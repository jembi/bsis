package org.jembi.bsis.backingform.validator;

import static org.jembi.bsis.helpers.builders.ComponentBackingFormBuilder.aComponentBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aDistributionSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.OrderFormBackingFormBuilder.anOrderFormBackingForm;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.OrderFormBackingForm;
import org.jembi.bsis.backingform.OrderFormItemBackingForm;
import org.jembi.bsis.backingform.validator.OrderFormBackingFormValidator;
import org.jembi.bsis.backingform.validator.OrderFormItemBackingFormValidator;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
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
public class OrderFormBackingFormValidatorTest {

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

  private OrderFormBackingForm getBaseOrderFormBackingForm() throws ParseException {
    LocationBackingForm dispatchedFrom = aDistributionSiteBackingForm().withName("LocFrom").withId(1l).build();
    LocationBackingForm dispatchedTo = aDistributionSiteBackingForm().withName("LocTo").withId(2l).build();
    Date orderDate = new Date();
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo).withOrderDate(orderDate).build();
    return backingForm;
  }
  
  private OrderFormItemBackingForm getBaseOrderFormItemBackingForm() throws ParseException {
    OrderFormItemBackingForm backingForm = new OrderFormItemBackingForm();
    backingForm.setBloodGroup("A+");
    backingForm.setNumberOfUnits(22);
    ComponentTypeBackingForm componentType = new ComponentTypeBackingForm();
    componentType.setComponentType(aComponentType().withId(1L).build());
    backingForm.setComponentType(componentType);
    return backingForm;
  }

  private ComponentBackingForm getBaseOrderFormComponentBackingForm() {
    ComponentBackingForm component = aComponentBackingForm().withId(1L).build();
    return component;
  }

  private Location getBaseDispatchedFrom() {
    return aDistributionSite().withName("LocFrom").withId(1l).build();
  }

  private Location getBaseDispatchedTo() {
    return aDistributionSite().withName("LocTo").withId(2l).build();
  }
  
  private Component getBaseComponent() {
    return aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withLocation(getBaseDispatchedFrom()).build();
  }

  @Test
  public void testValid_noErrors() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();
    backingForm.setItems(Arrays.asList(getBaseOrderFormItemBackingForm()));
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseDispatchedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseDispatchedTo());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(1L)).thenReturn(getBaseComponent());

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());

  }

  @Test
  public void testValidateDispatchedFromAndTo_getRequiredError() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();
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

  @SuppressWarnings("unchecked")
  @Test
  public void testValidateDispatchedFromAndToId_getInvalidError() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();

    // set up mocks
    when(locationRepository.getLocation(1l)).thenThrow(NoResultException.class);
    when(locationRepository.getLocation(2l)).thenThrow(NoResultException.class);
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("Invalid dispatchedFrom", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("Invalid dispatchedTo", errors.getFieldErrors().get(1).getDefaultMessage());
  }

  @Test
  public void testValidateDispatchedFromAndToCantBeVenue_getInvalidLocationTypeError() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();

    // dispatchedFrom and to can't be a venue
    Location venue1 = aVenue().withId(1l).build();
    Location venue2 = aVenue().withId(2l).build();

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(venue1);
    when(locationRepository.getLocation(2l)).thenReturn(venue2);
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("dispatchedFrom must be a distribution site", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("dispatchedTo must be a distribution or usage site", errors.getFieldErrors().get(1).getDefaultMessage());
  }

  @Test
  public void testValidateDispatchedToCanBeUsageSite_noErrors() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();

    // dispatchedTo can be a usageSite
    Location usageSite = aDistributionSite().withId(2l).build();

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseDispatchedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(usageSite);
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidateDispatchedFromCantBeUsageSite_getDispatchedFromError() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();

    // dispatchedTo can be a usageSite
    Location usageSite = aUsageSite().withId(1l).build();

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(usageSite);
    when(locationRepository.getLocation(2l)).thenReturn(getBaseDispatchedTo());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("dispatchedFrom must be a distribution site", errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidateOrderFormBackingForm_requiredCommonFieldsErrors() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();
    backingForm.setType(null);
    backingForm.setStatus(null);
    backingForm.setOrderDate(null);

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseDispatchedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseDispatchedTo());
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
  public void testValidateComponentLocation_invalidLocation() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();
    
    // create a component with a different location from dispatchedFrom
    Location differentLocation = aDistributionSite().withName("DifferentLocation").withId(3l).build();
    Component component =
        aComponent().withInventoryStatus(InventoryStatus.IN_STOCK)
        .withLocation(differentLocation).build();
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseDispatchedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseDispatchedTo());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(1L)).thenReturn(component);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component doesn't exist in LocFrom", errors.getFieldErrors().get(0).getDefaultMessage());
  }
  
  @Test
  public void testValidateComponentInventoryStatusRemoved_invalidInventoryStatus() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();
    
    // create a component with a different location from dispatchedFrom
    Component component =
        aComponent().withInventoryStatus(InventoryStatus.REMOVED)
        .withLocation(getBaseDispatchedFrom()).build();
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseDispatchedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseDispatchedTo());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(1L)).thenReturn(component);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component inventory status must be IN_STOCK", errors.getFieldErrors().get(0).getDefaultMessage());
  }
  
  @Test
  public void testValidateComponentInventoryStatusNotLabelled_invalidInventoryStatus() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();
    
    // create a component with a different location from dispatchedFrom
    Component component =
        aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withLocation(getBaseDispatchedFrom()).build();
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseDispatchedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseDispatchedTo());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(1L)).thenReturn(component);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component inventory status must be IN_STOCK", errors.getFieldErrors().get(0).getDefaultMessage());
  }
  
  @Test
  public void testValidateComponentNotFound_invalidComponentIdError() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();
    backingForm.setComponents(Arrays.asList(getBaseOrderFormComponentBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseDispatchedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseDispatchedTo());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));
    when(componentRepository.findComponent(1L)).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component id is invalid.", errors.getFieldErrors().get(0).getDefaultMessage());
  }
  
  @Test
  public void testValidateNoComponentId_requiredComponentIdError() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();
    
    // component backing form with null id
    ComponentBackingForm componentBackingForm = aComponentBackingForm().withId(null).build();
    backingForm.setComponents(Arrays.asList(componentBackingForm));

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(getBaseDispatchedFrom());
    when(locationRepository.getLocation(2l)).thenReturn(getBaseDispatchedTo());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("component id is required.", errors.getFieldErrors().get(0).getDefaultMessage());
  }
}
