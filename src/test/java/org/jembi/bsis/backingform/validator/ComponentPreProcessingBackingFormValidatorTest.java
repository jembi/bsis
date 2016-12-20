package org.jembi.bsis.backingform.validator;

import static org.jembi.bsis.helpers.builders.ComponentPreProcessingBackingFormBuilder.aComponentBackingForm;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.jembi.bsis.backingform.ComponentPreProcessingBackingForm;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(MockitoJUnitRunner.class)
public class ComponentPreProcessingBackingFormValidatorTest {

  @InjectMocks
  private ComponentPreProcessingBackingFormValidator validator;

  @Test
  public void testValidateComponentWithWeight_hasNoErrors() throws Exception {
    // set up data
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    ComponentPreProcessingBackingForm form = aComponentBackingForm()
        .withBleedStartTime(sdf.parse("2016-01-01 13:00"))
        .withBleedEndTime(sdf.parse("2016-01-01 13:16"))
        .withWeight(333)
        .build();
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");
    
    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }

  @Test
  public void testValidateBleedtime_shouldNotBeTheSame() throws Exception {
    // set up data
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    ComponentPreProcessingBackingForm form = aComponentBackingForm()
        .withBleedStartTime(sdf.parse("2016-01-01 13:00"))
        .withBleedEndTime(sdf.parse("2016-01-01 13:00"))
        .build();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field bleedEndTime", errors.getFieldError("bleedEndTime"));
  }

  @Test
  public void testValidateBleedtime_shouldNotBeZeroMinutes() throws Exception {
    // set up data
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    ComponentPreProcessingBackingForm form = aComponentBackingForm()
        .withBleedStartTime(sdf.parse("2016-01-01 13:00:10"))
        .withBleedEndTime(sdf.parse("2016-01-01 13:00:45"))
        .build();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field bleedEndTime", errors.getFieldError("bleedEndTime"));
  }

  @Test
  public void testInvalidNullBleedStartTime() throws Exception {
    // set up data
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    ComponentPreProcessingBackingForm form = aComponentBackingForm()
        .withBleedStartTime(null)
        .withBleedEndTime(sdf.parse("2016-01-01 13:00"))
        .build();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field bleedStartTime", errors.getFieldError("bleedStartTime"));
  }

  @Test
  public void testInvalidNullBleedEndTime() throws Exception {
    // set up data
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    ComponentPreProcessingBackingForm form = aComponentBackingForm()
        .withBleedStartTime(sdf.parse("2016-01-01 13:00"))
        .withBleedEndTime(null)
        .build();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field bleedEndTime", errors.getFieldError("bleedEndTime"));
  }

  @Test
  public void testInvalidBleedStartTimeAfterEndTime() throws Exception {
    // set up data
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    ComponentPreProcessingBackingForm form = aComponentBackingForm()
        .withBleedStartTime(sdf.parse("2016-01-01 15:00"))
        .withBleedEndTime(sdf.parse("2016-01-01 13:00"))
        .build();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");
    validator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error on field bleedEndTime", errors.getFieldError("bleedEndTime"));
  }

  @Test
  public void testValidateComponentWithNoWeight_hasNoErrors() throws Exception {
    // set up data
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    ComponentPreProcessingBackingForm form = aComponentBackingForm()
        .withBleedStartTime(sdf.parse("2016-01-01 13:00"))
        .withBleedEndTime(sdf.parse("2016-01-01 13:16"))
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
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    ComponentPreProcessingBackingForm form = aComponentBackingForm()
        .withBleedStartTime(sdf.parse("2016-01-01 13:00"))
        .withBleedEndTime(sdf.parse("2016-01-01 13:16"))
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
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    ComponentPreProcessingBackingForm form = aComponentBackingForm()
        .withBleedStartTime(sdf.parse("2016-01-01 13:00"))
        .withBleedEndTime(sdf.parse("2016-01-01 13:16"))
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
