package backingform.validator;

import static helpers.builders.LocationBuilder.aDistributionSite;
import static helpers.builders.LocationBuilder.aUsageSite;
import static helpers.builders.LocationBuilder.aVenue;
import static helpers.builders.OrderFormBackingFormBuilder.anOrderFormBackingForm;
import static org.mockito.Mockito.when;
import helpers.builders.ComponentTypeBuilder;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.NoResultException;

import model.location.Location;

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
import backingform.ComponentTypeBackingForm;
import backingform.LocationBackingForm;
import backingform.OrderFormBackingForm;
import backingform.OrderFormItemBackingForm;

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

  private OrderFormBackingForm getBaseOrderFormBackingForm() throws ParseException {
    Location dispatchedFrom = aDistributionSite().withName("LocFrom").withId(1l).build();
    Location dispatchedTo = aDistributionSite().withName("LocTo").withId(2l).build();
    Date orderDate = new Date();
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withDispatchedFrom(new LocationBackingForm(dispatchedFrom))
        .withDispatchedTo(new LocationBackingForm(dispatchedTo)).withOrderDate(orderDate).build();
    return backingForm;
  }
  
  private OrderFormItemBackingForm getBaseOrderFormItemBackingForm() throws ParseException {
    OrderFormItemBackingForm backingForm = new OrderFormItemBackingForm();
    backingForm.setBloodGroup("A+");
    backingForm.setNumberOfUnits(22);
    ComponentTypeBackingForm componentType = new ComponentTypeBackingForm();
    componentType.setComponentType(ComponentTypeBuilder.aComponentType().withId(1L).build());
    backingForm.setComponentType(componentType);
    return backingForm;
  }

  @Test
  public void testValid_noErrors() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();
    backingForm.setItems(Arrays.asList(getBaseOrderFormItemBackingForm()));

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(backingForm.getDispatchedFrom().getLocation());
    when(locationRepository.getLocation(2l)).thenReturn(backingForm.getDispatchedTo().getLocation());
    when(formFieldRepository.getRequiredFormFields("OrderForm")).thenReturn(Arrays.asList(new String[] {"orderDate", "status", "type"}));

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
    when(locationRepository.getLocation(1l)).thenReturn(backingForm.getDispatchedFrom().getLocation());
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
    when(locationRepository.getLocation(2l)).thenReturn(backingForm.getDispatchedTo().getLocation());
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
    when(locationRepository.getLocation(1l)).thenReturn(backingForm.getDispatchedFrom().getLocation());
    when(locationRepository.getLocation(2l)).thenReturn(backingForm.getDispatchedTo().getLocation());
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
}
