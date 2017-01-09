package org.jembi.bsis.backingform.validator;

import static org.jembi.bsis.helpers.builders.ComponentBackingFormBuilder.aComponentBackingForm;

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
public class ComponentBackingFormValidatorTests {

  @InjectMocks
  private ComponentBackingFormValidator validator;

  @Test
  public void testValidateComponentWithWeight_hasNoErrors() throws Exception {
    // set up data
    ComponentBackingForm form = aComponentBackingForm()
        .withWeight(333)
        .build();
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");

    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }

  @Test
  public void testValidateComponentWithNoWeight_hasNoErrors() throws Exception {
    // set up data
    ComponentBackingForm form = aComponentBackingForm()
        .withWeight(null)
        .build();
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");

    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }

  @Test
  public void testValidateComponentWithNegativeWeight_hasInvalidWeightError() throws Exception {
    // set up data
    ComponentBackingForm form = aComponentBackingForm()
        .withWeight(-4)
        .build();
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
    ComponentBackingForm form = aComponentBackingForm()
        .withWeight(1000)
        .build();
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");

    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("Invalid weight error", "weight should be between 0 and 1000",
        errors.getFieldErrors().get(0).getDefaultMessage());
  }

}
