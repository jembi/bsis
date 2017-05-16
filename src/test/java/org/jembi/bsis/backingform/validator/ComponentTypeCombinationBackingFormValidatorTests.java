package org.jembi.bsis.backingform.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentTypeBackingFormBuilder.aComponentTypeBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBackingFormBuilder.aComponentTypeCombinationBackingForm;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.ComponentTypeCombinationBackingForm;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.repository.ComponentTypeCombinationRepository;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import com.google.common.collect.Sets;

public class ComponentTypeCombinationBackingFormValidatorTests extends UnitTestSuite {

  @InjectMocks
  private ComponentTypeCombinationBackingFormValidator componentTypeCombinationBackingFormValidator;
  @Mock
  private ComponentTypeCombinationRepository componentTypeCombinationRepository;
  @Mock
  private ComponentTypeRepository componentTypeRepository;
  
  @Test
  public void testValidateForm_shouldHaveNoErrors() {
    UUID componentTypeId = UUID.randomUUID();
    ComponentTypeBackingForm componentTypeBackingForm = aComponentTypeBackingForm().withId(componentTypeId).build();
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    ComponentTypeCombinationBackingForm backingForm = aComponentTypeCombinationBackingForm()
        .withCombinationName("combination")
        .withComponentTypes(Arrays.asList(componentTypeBackingForm))
        .withSourceComponentTypes(Sets.newHashSet(componentTypeBackingForm))
        .build();

    when(componentTypeRepository.getComponentTypeById(componentTypeId)).thenReturn(componentType);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    when(componentTypeCombinationRepository.isUniqueCombinationName(null, backingForm.getCombinationName())).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentTypeCombination");
    componentTypeCombinationBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(0));
  }
  
  @Test
  public void testValidateFormWithNoCombinationName_shouldHaveOneError() {
    UUID componentTypeId = UUID.randomUUID();
    ComponentTypeBackingForm componentTypeBackingForm = aComponentTypeBackingForm().withId(componentTypeId).build();
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    ComponentTypeCombinationBackingForm backingForm = aComponentTypeCombinationBackingForm()
        .withCombinationName(null)
        .withComponentTypes(Arrays.asList(componentTypeBackingForm))
        .withSourceComponentTypes(Sets.newHashSet(componentTypeBackingForm))
        .build();

    when(componentTypeRepository.getComponentTypeById(componentTypeId)).thenReturn(componentType);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentTypeCombination");
    componentTypeCombinationBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("combinationName").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateFormWithInvalidCombinationNameLength_shouldHaveOneError() {
    UUID componentTypeId = UUID.randomUUID();
    ComponentTypeBackingForm componentTypeBackingForm = aComponentTypeBackingForm().withId(componentTypeId).build();
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    ComponentTypeCombinationBackingForm backingForm = aComponentTypeCombinationBackingForm()
        .withCombinationName(
            "verylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongword")
        .withComponentTypes(Arrays.asList(componentTypeBackingForm))
        .withSourceComponentTypes(Sets.newHashSet(componentTypeBackingForm))
        .build();

    when(componentTypeRepository.getComponentTypeById(componentTypeId)).thenReturn(componentType);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentTypeCombination");
    componentTypeCombinationBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("combinationName").getCode(), is("errors.fieldLength"));
  }
  
  @Test
  public void testValidateFormWithDuplicateCombinationName_shouldHaveOneError() {
    UUID componentTypeId = UUID.randomUUID();
    ComponentTypeBackingForm componentTypeBackingForm = aComponentTypeBackingForm().withId(componentTypeId).build();
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    ComponentTypeCombinationBackingForm backingForm = aComponentTypeCombinationBackingForm()
        .withCombinationName("combination")
        .withComponentTypes(Arrays.asList(componentTypeBackingForm))
        .withSourceComponentTypes(Sets.newHashSet(componentTypeBackingForm))
        .build();

    when(componentTypeRepository.getComponentTypeById(componentTypeId)).thenReturn(componentType);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    when(componentTypeCombinationRepository.isUniqueCombinationName(null, backingForm.getCombinationName())).thenReturn(false);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentTypeCombination");
    componentTypeCombinationBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("combinationName").getCode(), is("errors.nonUnique"));
  }
  
  @Test
  public void testValidateFormWithNullProducedComponentTypes_shouldHaveOneError() {
    UUID componentTypeId = UUID.randomUUID();
    ComponentTypeBackingForm componentTypeBackingForm = aComponentTypeBackingForm().withId(componentTypeId).build();
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    ComponentTypeCombinationBackingForm backingForm = aComponentTypeCombinationBackingForm()
        .withCombinationName("combination")
        .withComponentTypes(null)
        .withSourceComponentTypes(Sets.newHashSet(componentTypeBackingForm))
        .build();

    when(componentTypeRepository.getComponentTypeById(componentTypeId)).thenReturn(componentType);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    when(componentTypeCombinationRepository.isUniqueCombinationName(null, backingForm.getCombinationName())).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentTypeCombination");
    componentTypeCombinationBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("componentTypes").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateFormWithInvalidProducedComponentTypeId_shouldHaveOneError() {
    UUID componentTypeId = UUID.randomUUID();
    ComponentTypeBackingForm componentTypeBackingForm = aComponentTypeBackingForm().withId(componentTypeId).build();
    UUID invalidComponentType = UUID.randomUUID();
    ComponentTypeBackingForm invalidIdComponentTypeBackingForm = aComponentTypeBackingForm().withId(invalidComponentType).build();
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    ComponentTypeCombinationBackingForm backingForm = aComponentTypeCombinationBackingForm()
        .withCombinationName("combination")
        .withComponentTypes(Arrays.asList(invalidIdComponentTypeBackingForm))
        .withSourceComponentTypes(Sets.newHashSet(componentTypeBackingForm))
        .build();

    when(componentTypeRepository.getComponentTypeById(componentTypeId)).thenReturn(componentType);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    when(componentTypeRepository.verifyComponentTypeExists(invalidComponentType)).thenReturn(false);
    when(componentTypeCombinationRepository.isUniqueCombinationName(null, backingForm.getCombinationName())).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentTypeCombination");
    componentTypeCombinationBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("componentTypes[0].id").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateFormWithNullSourceComponentTypes_shouldHaveOneError() {
    UUID componentTypeId = UUID.randomUUID();
    ComponentTypeBackingForm componentTypeBackingForm = aComponentTypeBackingForm().withId(componentTypeId).build();
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    ComponentTypeCombinationBackingForm backingForm = aComponentTypeCombinationBackingForm()
        .withCombinationName("combination")
        .withComponentTypes(Arrays.asList(componentTypeBackingForm))
        .withSourceComponentTypes(null)
        .build();

    when(componentTypeRepository.getComponentTypeById(componentTypeId)).thenReturn(componentType);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    when(componentTypeCombinationRepository.isUniqueCombinationName(null, backingForm.getCombinationName())).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentTypeCombination");
    componentTypeCombinationBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("sourceComponentTypes").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateFormWithInvalidSourceComponentTypeId_shouldHaveOneError() {
    UUID componentTypeId = UUID.randomUUID();
    ComponentTypeBackingForm componentTypeBackingForm = aComponentTypeBackingForm().withId(componentTypeId).build();
    UUID invalidComponentType = UUID.randomUUID();
    ComponentTypeBackingForm invalidIdComponentTypeBackingForm = aComponentTypeBackingForm().withId(invalidComponentType).build();
    ComponentType componentType = aComponentType().withId(componentTypeId).build();
    ComponentTypeCombinationBackingForm backingForm = aComponentTypeCombinationBackingForm()
        .withCombinationName("combination")
        .withComponentTypes(Arrays.asList(componentTypeBackingForm))
        .withSourceComponentTypes(Sets.newHashSet(invalidIdComponentTypeBackingForm))
        .build();

    when(componentTypeRepository.getComponentTypeById(componentTypeId)).thenReturn(componentType);
    when(componentTypeRepository.verifyComponentTypeExists(componentTypeId)).thenReturn(true);
    when(componentTypeRepository.verifyComponentTypeExists(invalidComponentType)).thenReturn(false);
    when(componentTypeCombinationRepository.isUniqueCombinationName(null, backingForm.getCombinationName())).thenReturn(true);
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "ComponentTypeCombination");
    componentTypeCombinationBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("sourceComponentTypes[0].id").getCode(), is("errors.invalid"));
  }
 
}
