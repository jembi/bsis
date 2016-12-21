package org.jembi.bsis.backingform.validator;

import java.util.HashMap;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(MockitoJUnitRunner.class)
public class ComponentBackingFormValidatorTest {

  @InjectMocks
  private ComponentBackingFormValidator validator;

  @Test
  public void testValidateComponentWithWeight_hasNoErrors() throws Exception {
    // set up data
    ComponentBackingForm form = new ComponentBackingForm();
    form.setWeight(333);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }

  @Test
  public void testValidateComponentWithNoWeight_hasNoErrors() throws Exception {
    // set up data
    ComponentBackingForm form = new ComponentBackingForm();
    form.setWeight(null);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");

    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }

  @Test
  public void testValidateComponentWithNegativeWeight_hasInvalidWeightError() throws Exception {
    // set up data
    ComponentBackingForm form = new ComponentBackingForm();
    form.setWeight(-4);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");

    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("Invalid weight error", "weight should be between 0 and 1000",
        errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidateComponentWithTooLargeWeight_hasInvalidWeightError() throws Exception {
    // set up data
    ComponentBackingForm form = new ComponentBackingForm();
    form.setWeight(1000);
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");

    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("Invalid weight error", "weight should be between 0 and 1000",
        errors.getFieldErrors().get(0).getDefaultMessage());
  }

}
