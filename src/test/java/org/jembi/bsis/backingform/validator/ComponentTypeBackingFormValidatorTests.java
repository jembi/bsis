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

  @InjectMocks
  private ComponentTypeBackingFormValidator componentTypeBackingFormValidator;
  @Mock
  private ComponentTypeRepository componentTypeRepository;
  
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
  public void testValidateFormWithNoCode_shouldHaveOneError() {
    ComponentTypeBackingForm backingForm = aComponentTypeBackingForm()
        .withId(2L)
        .withComponentTypeName("Component Type")
        .withComponentTypeCode(null)
        .withExpiresAfter(35)
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);

    when(componentTypeRepository.findComponentTypeByCode(COMPONENT_TYPE_CODE)).thenThrow(new NoResultException());
    
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("componentTypeCode").getCode(), is("errors.required"));
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
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getErrorCount(), is(0));
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
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentType");
    componentTypeBackingFormValidator.validateForm(backingForm, errors);
    
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("expiresAfter").getCode(), is("errors.nonPositive"));
  }
}
