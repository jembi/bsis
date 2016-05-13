package backingform.validator;

import static helpers.builders.LocationBuilder.aDistributionSite;
import static helpers.builders.LocationBuilder.aVenue;
import static helpers.builders.OrderFormBackingFormBuilder.anOrderFormBackingForm;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.NoResultException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import backingform.LocationBackingForm;
import backingform.OrderFormBackingForm;
import model.location.Location;
import repository.FormFieldRepository;
import repository.LocationRepository;

@RunWith(MockitoJUnitRunner.class)
public class OrderFormBackingFormValidatorTest {

  @InjectMocks
  private OrderFormBackingFormValidator orderFormBackingFormValidator;

  @Mock
  private LocationRepository locationRepository;

  @Mock
  private FormFieldRepository formFieldRepository;

  private OrderFormBackingForm getBaseOrderFormBackingForm() throws ParseException {
    Location dispatchedFrom = aDistributionSite().withName("LocFrom").withId(1l).build();
    Location dispatchedTo = aDistributionSite().withName("LocTo").withId(2l).build();
    Date orderDate = new Date();
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withDispatchedFrom(new LocationBackingForm(dispatchedFrom))
        .withDispatchedTo(new LocationBackingForm(dispatchedTo)).withOrderDate(orderDate).build();
    return backingForm;
  }

  @Test
  public void testValidateOrderFormBackingForm_noErrors() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(backingForm.getDispatchedFrom().getLocation());
    when(locationRepository.getLocation(2l)).thenReturn(backingForm.getDispatchedTo().getLocation());
    when(formFieldRepository.getRequiredFormFields("orderForm")).thenReturn(Arrays.asList(new String[] {"orderDate"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "orderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidateOrderFormBackingForm_dateRequiredError() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();
    backingForm.setOrderDate(null);

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(backingForm.getDispatchedFrom().getLocation());
    when(locationRepository.getLocation(2l)).thenReturn(backingForm.getDispatchedTo().getLocation());
    when(formFieldRepository.getRequiredFormFields("orderForm")).thenReturn(Arrays.asList(new String[] {"orderDate"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "orderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("This information is required", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("orderForm.orderDate", errors.getFieldErrors().get(0).getField());
  }

  @Test
  public void testValidateOrderFormBackingForm_dispatchedFromAndToRequiredError() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();
    backingForm.setDispatchedFrom(null);
    backingForm.setDispatchedTo(null);

    // set up mocks
    when(formFieldRepository.getRequiredFormFields("orderForm")).thenReturn(Arrays.asList(new String[] {"orderDate"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "orderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("dispatchedFrom is required", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("dispatchedTo is required", errors.getFieldErrors().get(1).getDefaultMessage());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testValidateOrderFormBackingForm_dispatchedFromAndToInvalidError() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();

    // set up mocks
    when(locationRepository.getLocation(1l)).thenThrow(NoResultException.class);
    when(locationRepository.getLocation(2l)).thenThrow(NoResultException.class);
    when(formFieldRepository.getRequiredFormFields("orderForm")).thenReturn(Arrays.asList(new String[] {"orderDate"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "orderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("Invalid dispatchedFrom", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("Invalid dispatchedTo", errors.getFieldErrors().get(1).getDefaultMessage());
  }

  @Test
  public void testValidateOrderFormBackingForm_dispatchedFromAndToInvalidTypeError() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();

    // dispatchedFrom and to can't be a venue
    Location venue1 = aVenue().withId(1l).build();
    Location venue2 = aVenue().withId(1l).build();

    // set up mocks
    when(locationRepository.getLocation(1l)).thenReturn(venue1);
    when(locationRepository.getLocation(2l)).thenReturn(venue2);
    when(formFieldRepository.getRequiredFormFields("orderForm")).thenReturn(Arrays.asList(new String[] {"orderDate"}));

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "orderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("dispatchedFrom must be a distribution site", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("dispatchedTo must be a distribution or usage site", errors.getFieldErrors().get(1).getDefaultMessage());
  }
}
