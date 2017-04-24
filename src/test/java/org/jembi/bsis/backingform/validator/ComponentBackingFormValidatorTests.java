package org.jembi.bsis.backingform.validator;

import static org.jembi.bsis.helpers.builders.ComponentBackingFormBuilder.aComponentBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.UUID;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.repository.ComponentRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(MockitoJUnitRunner.class)
public class ComponentBackingFormValidatorTests {

  @InjectMocks
  private ComponentBackingFormValidator validator;

  @Mock
  private ComponentRepository componentRepository;
  
  private static final UUID COMPONENT_ID = UUID.randomUUID();

  @Test
  public void testValidateComponentWithWeight_hasNoErrors() throws Exception {
    // set up data
    ComponentBackingForm form = aComponentBackingForm()
        .withId(COMPONENT_ID)
        .withWeight(333)
        .build();
    Component component = aComponent().build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");

    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(component);

    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }

  @Test
  public void testValidateComponentWithNoWeight_hasNoErrors() throws Exception {
    // set up data
    ComponentBackingForm form = aComponentBackingForm()
        .withId(COMPONENT_ID)
        .withWeight(null)
        .build();
    Component component = aComponent().build();
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");

    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(component);

    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("No errors", 0, errors.getErrorCount());
  }

  @Test
  public void testValidateComponentWithNegativeWeight_hasInvalidWeightError() throws Exception {
    // set up data
    ComponentBackingForm form = aComponentBackingForm()
        .withId(COMPONENT_ID)
        .withWeight(-4)
        .build();
    Component component = aComponent().build();
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");

    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(component);

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
        .withId(COMPONENT_ID)
        .withWeight(1000)
        .build();
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");
    Component component = aComponent().build();

    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(component);

    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("Invalid weight error", "weight should be between 0 and 1000",
        errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidateComponentWithWeightGreaterThanParentWeight_hasInvalidWeightError() {
    // set up data
    ComponentBackingForm form = aComponentBackingForm()
        .withId(COMPONENT_ID)
        .withWeight(500)
        .build();
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "Component");
    Component parentComponent = aComponent().withId(COMPONENT_ID).withWeight(450).build();
    Component component = aComponent().withParentComponent(parentComponent).build();

    when(componentRepository.findComponent(COMPONENT_ID)).thenReturn(component);

    // run test
    validator.validate(form, errors);

    // do checks
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("Invalid weight error", "weight should not be greater than parent component weight",
        errors.getFieldErrors().get(0).getDefaultMessage());    
  }
}
