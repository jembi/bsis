package backingform.validator;

import static org.mockito.Mockito.when;
import helpers.builders.ComponentTypeBuilder;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import repository.ComponentTypeRepository;
import backingform.ComponentTypeBackingForm;
import backingform.OrderFormItemBackingForm;

@RunWith(MockitoJUnitRunner.class)
public class OrderFormItemBackingFormValidatorTest {

  @InjectMocks
  private OrderFormItemBackingFormValidator orderFormItemBackingFormValidator;

  @Mock
  private ComponentTypeRepository componentTypeRepository;

  @Test
  public void testValid_noErrors() throws Exception {
    // set up data
    OrderFormItemBackingForm backingForm = new OrderFormItemBackingForm();
    ComponentTypeBackingForm componentType = new ComponentTypeBackingForm();
    componentType.setComponentType(ComponentTypeBuilder.aComponentType().withId(1L).build());
    backingForm.setComponentType(componentType);
    backingForm.setBloodGroup("A+");
    backingForm.setNumberOfUnits(12);

    // set up mocks
    when(componentTypeRepository.verifyComponentTypeExists(1L)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderFormItem");
    orderFormItemBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidateNullComponentType_getRequiredError() throws Exception {
    // set up data
    OrderFormItemBackingForm backingForm = new OrderFormItemBackingForm();
    backingForm.setBloodGroup("A+");
    backingForm.setNumberOfUnits(12);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderFormItem");
    orderFormItemBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("componentType is required", errors.getFieldErrors().get(0).getDefaultMessage());
  }
  
  @Test
  public void testValidateNullComponentTypeEntity_getRequiredError() throws Exception {
    // set up data
    OrderFormItemBackingForm backingForm = new OrderFormItemBackingForm();
    ComponentTypeBackingForm componentType = new ComponentTypeBackingForm();
    componentType.setComponentType(null);
    backingForm.setComponentType(componentType);
    backingForm.setBloodGroup("A+");
    backingForm.setNumberOfUnits(12);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderFormItem");
    orderFormItemBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("componentType is required", errors.getFieldErrors().get(0).getDefaultMessage());
  }
  
  @Test
  public void testValidateNullIdComponentType_getRequiredError() throws Exception {
    // set up data
    OrderFormItemBackingForm backingForm = new OrderFormItemBackingForm();
    ComponentTypeBackingForm componentType = new ComponentTypeBackingForm();
    backingForm.setComponentType(componentType);
    backingForm.setBloodGroup("A+");
    backingForm.setNumberOfUnits(12);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderFormItem");
    orderFormItemBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("componentType is required", errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidateInvalidIdComponentType_getInvalidError() throws Exception {
    // set up data
    OrderFormItemBackingForm backingForm = new OrderFormItemBackingForm();
    ComponentTypeBackingForm componentType = new ComponentTypeBackingForm();
    componentType.setComponentType(ComponentTypeBuilder.aComponentType().withId(1L).build());
    backingForm.setComponentType(componentType);
    backingForm.setBloodGroup("A+");
    backingForm.setNumberOfUnits(12);

    // set up mocks
    when(componentTypeRepository.verifyComponentTypeExists(1L)).thenReturn(false);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderFormItem");
    orderFormItemBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("Invalid componentType", errors.getFieldErrors().get(0).getDefaultMessage());
  }
  
  @Test
  public void testValidateBloodGroup_getRequiredError() throws Exception {
    // set up data
    OrderFormItemBackingForm backingForm = new OrderFormItemBackingForm();
    ComponentTypeBackingForm componentType = new ComponentTypeBackingForm();
    componentType.setComponentType(ComponentTypeBuilder.aComponentType().withId(1L).build());
    backingForm.setComponentType(componentType);
    backingForm.setNumberOfUnits(12);

    // set up mocks
    when(componentTypeRepository.verifyComponentTypeExists(1L)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderFormItem");
    orderFormItemBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("bloodGroup is required", errors.getFieldErrors().get(0).getDefaultMessage());
  }
  
  @Test
  public void testValidateInvalidBloodGroup_getInvalidError() throws Exception {
    // set up data
    OrderFormItemBackingForm backingForm = new OrderFormItemBackingForm();
    ComponentTypeBackingForm componentType = new ComponentTypeBackingForm();
    componentType.setComponentType(ComponentTypeBuilder.aComponentType().withId(1L).build());
    backingForm.setComponentType(componentType);
    backingForm.setBloodGroup("A*");
    backingForm.setNumberOfUnits(12);

    // set up mocks
    when(componentTypeRepository.verifyComponentTypeExists(1L)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderFormItem");
    orderFormItemBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("Invalid bloodGroup", errors.getFieldErrors().get(0).getDefaultMessage());
  }
  
  @Test
  public void testValidateInvalidNumberOfUnits_getInvalidError() throws Exception {
    // set up data
    OrderFormItemBackingForm backingForm = new OrderFormItemBackingForm();
    ComponentTypeBackingForm componentType = new ComponentTypeBackingForm();
    componentType.setComponentType(ComponentTypeBuilder.aComponentType().withId(1L).build());
    backingForm.setComponentType(componentType);
    backingForm.setBloodGroup("A+");
    backingForm.setNumberOfUnits(-12);

    // set up mocks
    when(componentTypeRepository.verifyComponentTypeExists(1L)).thenReturn(true);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "OrderFormItem");
    orderFormItemBackingFormValidator.validate(backingForm, errors);

    // check asserts
    Assert.assertEquals("numberOfUnits should be greater than 0", errors.getFieldErrors().get(0).getDefaultMessage());
  }
}
