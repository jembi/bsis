package backingform.validator;

import static helpers.builders.LocationBuilder.aLocation;
import static helpers.builders.OrderFormBackingFormBuilder.anOrderFormBackingForm;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import backingform.OrderFormBackingForm;
import model.location.Location;
import repository.LocationRepository;

@RunWith(MockitoJUnitRunner.class)
public class OrderFormBackingFormValidatorTest {

  @InjectMocks
  private OrderFormBackingFormValidator orderFormBackingFormValidator;

  @Mock
  private LocationRepository locationRepository;

  private OrderFormBackingForm getBaseOrderFormBackingForm() throws ParseException {
    Location dispatchedFrom = aLocation().withName("LocFrom").withId(1l).build();
    Location dispatchedTo = aLocation().withName("LocTo").withId(2l).build();
    Date orderDate = new Date();
    OrderFormBackingForm backingForm = anOrderFormBackingForm().withDispatchedFrom(dispatchedFrom)
        .withDispatchedTo(dispatchedTo).withOrderDate(orderDate).build();
    return backingForm;
  }

  @Test
  public void testValidateOrderFormBackingForm_noErrors() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();

    // // set up mocks
    when(locationRepository.verifyLocationExists(1l)).thenReturn(true);
    when(locationRepository.verifyLocationExists(2l)).thenReturn(true);

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

    // // set up mocks
    when(locationRepository.verifyLocationExists(1l)).thenReturn(true);
    when(locationRepository.verifyLocationExists(2l)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "orderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("orderDate is required", errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidateOrderFormBackingForm_dispatchedFromAndToRequiredError() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();
    backingForm.setDispatchedFrom(null);
    backingForm.setDispatchedTo(null);

    // // set up mocks
    when(locationRepository.verifyLocationExists(1l)).thenReturn(true);
    when(locationRepository.verifyLocationExists(2l)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "orderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("dispatchedFrom is required", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("dispatchedTo is required", errors.getFieldErrors().get(1).getDefaultMessage());
  }

  @Test
  public void testValidateOrderFormBackingForm_dispatchedFromAndToInvalidError() throws Exception {
    // set up data
    OrderFormBackingForm backingForm = getBaseOrderFormBackingForm();

    // // set up mocks
    when(locationRepository.verifyLocationExists(1l)).thenReturn(false);
    when(locationRepository.verifyLocationExists(2l)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "orderForm");
    orderFormBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("Invalid dispatchedFrom", errors.getFieldErrors().get(0).getDefaultMessage());
    Assert.assertEquals("Invalid dispatchedTo", errors.getFieldErrors().get(1).getDefaultMessage());
  }
}
