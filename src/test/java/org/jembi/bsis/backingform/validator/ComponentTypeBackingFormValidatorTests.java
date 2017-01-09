package org.jembi.bsis.backingform.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentTypeBackingFormBuilder.aComponentTypeBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class ComponentTypeBackingFormValidatorTests extends UnitTestSuite {
  
  private static final String COMPONENT_TYPE_CODE = "666";
  private static final String COMPONENT_TYPE_NAME = "Blood";

  @InjectMocks
  private ComponentTypeBackingFormValidator componentTypeBackingFormValidator;
  @Mock
  private ComponentTypeRepository componentTypeRepository;

  @Test
  public void testValidateFormComplete_shouldHaveNoErrors() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(10)
        .withMaxBleedTime(10)
        .withMaxTimeSinceDonation(5)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(0));
  }
  
  @Test
  public void testValidateFormWithNoName_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(2L)
        .withComponentTypeName(null)
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(35)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("componentTypeName").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateFormWithTooLongName_shouldHaveOneError() {
    String tooLongName = "BloodBloodBloodBloodBloodBloodBloodBloodBloodBloodBlood";
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName(tooLongName)
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(35)
        .build();
    
    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("componentTypeName").getCode(), is("fieldLength.error"));
  }
  
  @Test
  public void testValidateFormWithLongName_shouldHaveNoError() {
    String longName = "10101010101010101010101010101010101010101010101010";
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName(longName)
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(35)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, longName)).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getFieldErrorCount(), is(0));
  }
  
  @Test
  public void testValidateFormWithNoCode_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(2L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(null)
        .withExpiresAfter(35)
        .build();
    
    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(2L, "Component Type")).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("componentTypeCode").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithTooLongCode_shouldHaveOneError() {
    String tooLongCode = "1010101010101010101010101010101";
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName(COMPONENT_TYPE_NAME)
        .withComponentTypeCode(tooLongCode)
        .withExpiresAfter(35)
        .build();
    
    when(componentTypeRepository.findComponentTypeByCode(tooLongCode)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, COMPONENT_TYPE_NAME)).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("componentTypeCode").getCode(), is("fieldLength.error"));
  }
  
  @Test
  public void testValidateFormWithLongCode_shouldHaveNoErrors() {
    String longCode = "101010101010101010101010101010";
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName(COMPONENT_TYPE_NAME)
        .withComponentTypeCode(longCode)
        .withExpiresAfter(35)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(longCode)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, COMPONENT_TYPE_NAME)).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getFieldErrorCount(), is(0));
  }
  
  @Test
  public void testValidateFormWithDuplicateCode_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(2L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(35)
        .build();
    
    ComponentType existingComponentType = aComponentType().withId(7L).withComponentTypeCode(COMPONENT_TYPE_CODE).build();
    
    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenReturn(existingComponentType);
    when(componentTypeRepository.isUniqueComponentTypeName(2L, "Component Type")).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("componentTypeCode").getCode(), is("errors.nonUnique"));
  }
  
  @Test
  public void testValidateFormWithDuplicateCodeForSameComponentType_shouldHaveNoErrors() {
    Long componentTypeId = 2L;
    
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(componentTypeId)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(35)
        .build();
    
    ComponentType existingComponentType = aComponentType()
        .withId(componentTypeId)
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .build();
    
    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenReturn(existingComponentType);
    when(componentTypeRepository.isUniqueComponentTypeName(componentTypeId, "Component Type")).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getErrorCount(), is(0));
  }
  
  @Test
  public void testValidateFormWithDuplicateName_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(2L)
        .withComponentTypeName(COMPONENT_TYPE_NAME)
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(35)
        .build();
    
    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(2L, COMPONENT_TYPE_NAME)).thenReturn(false);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("componentTypeName").getCode(), is("errors.nonUnique"));
  }
  
  @Test
  public void testValidateFormWithNoExpiresAfter_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(2L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(null)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(2L, "Component Type")).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("expiresAfter").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateFormWithNegativeExpiresAfter_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(2L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(-1)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(2L, "Component Type")).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("expiresAfter").getCode(), is("errors.nonPositive"));
  }
  
  @Test
  public void testValidateFormWithZeroExpiresAfter_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(2L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(0)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(2L, "Component Type")).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("expiresAfter").getCode(), is("errors.nonPositive"));
  }

  @Test
  public void testValidateFormWithBelow0MaxBleedTime_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(10)
        .withMaxBleedTime(-10)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("maxBleedTime").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateFormWith0MaxBleedTime_shouldHaveNoErrors() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(10)
        .withMaxBleedTime(0)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(0));
  }

  @Test
  public void testValidateFormWith60MaxBleedTime_shouldHaveNoErrors() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(10)
        .withMaxBleedTime(60)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(0));
  }

  @Test
  public void testValidateFormWithGreater60MaxBleedTime_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(10)
        .withMaxBleedTime(61)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("maxBleedTime").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateFormWithSuperLargeMaxBleedTime_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(10)
        .withMaxBleedTime(600)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("maxBleedTime").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateFormWithBelow0MaxTimeSinceDonation_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(10)
        .withMaxTimeSinceDonation(-5)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("maxTimeSinceDonation").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateFormWith0MaxTimeSinceDonation_shouldHaveNoError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(10)
        .withMaxTimeSinceDonation(0)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(0));
  }

  @Test
  public void testValidateFormWith30MaxTimeSinceDonation_shouldHaveNoError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(10)
        .withMaxTimeSinceDonation(30)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(0));
  }

  @Test
  public void testValidateFormWithGreater30MaxTimeSinceDonation_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(10)
        .withMaxTimeSinceDonation(60)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("maxTimeSinceDonation").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateFormWithCorrectGravity_shouldHaveNoError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(35)
        .withGravity(1.004)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(0));
  }
  
  @Test
  public void testValidateFormWithLessThanMinimumGravity_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(35)
        .withGravity(0.000)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("gravity").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateFormWithMoreThanMinimumGravity_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(35)
        .withGravity(2.015)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("gravity").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateFormWithMoreThanThreeGravityDecimalPlaces_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(35)
        .withGravity(1.00567)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("gravity").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateFormWithReallyLargeMaxTimeSinceDonation_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(1L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(COMPONENT_TYPE_CODE)
        .withExpiresAfter(10)
        .withMaxTimeSinceDonation(6009)
        .build();

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    when(componentTypeRepository.isUniqueComponentTypeName(1L, "Component Type")).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("maxTimeSinceDonation").getCode(), is("errors.invalid"));
  }
}
