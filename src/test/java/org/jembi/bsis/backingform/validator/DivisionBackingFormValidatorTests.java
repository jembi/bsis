package org.jembi.bsis.backingform.validator;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.DivisionBackingForm;
import org.jembi.bsis.helpers.builders.DivisionBackingFormBuilder;
import org.jembi.bsis.helpers.builders.DivisionBuilder;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.repository.DivisionRepository;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class DivisionBackingFormValidatorTests extends UnitTestSuite {

  @InjectMocks
  private DivisionBackingFormValidator validator;
  
  @Mock
  private DivisionRepository divisionRepository;

  @Mock
  private FormFieldRepository formFieldRepository;

  @Test
  public void testValidateValidForm_shouldntGetErrors() {
    // Set up data
    DivisionBackingForm parentForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(1L).withLevel(1).withName("Level1Division").build();
    
    DivisionBackingForm divisionForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(2L).withLevel(2).withName("Level2Division").withParent(parentForm).build();
    
    Division parent = DivisionBuilder.aDivision()
        .withId(1L).withLevel(1).withName("Level1Division").build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "division");
    
    // Set up mocks
    when(divisionRepository.findDivisionById(1L)).thenReturn(parent);
    when(formFieldRepository.getRequiredFormFields("division")).thenReturn(Arrays.asList(new String[] {"name", "level"}));
    when(divisionRepository.findDivisionByName("Level2Division")).thenReturn(null);
    
    // Run test
    validator.validateForm(divisionForm, errors);

    // Verify
    Assert.assertEquals("No error", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidateFormWithNullName_shouldGetError() throws Exception {
    // Set up data
    DivisionBackingForm divisionForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(1L).withLevel(1).withName(null).build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "division");

    // Set up mocks
    when(formFieldRepository.getRequiredFormFields("division")).thenReturn(Arrays.asList(new String[] {"name", "level"}));

    // Run test
    validator.validate(divisionForm, errors);

    // Verify
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("requiredField.error", errors.getFieldError("name").getCode());
  }

  @Test
  public void testValidateFormWithEmptyName_shouldGetError() throws Exception {
    // Set up data
    DivisionBackingForm divisionForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(1L).withLevel(1).withName("").build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "division");

    // Set up mocks
    when(formFieldRepository.getRequiredFormFields("division")).thenReturn(Arrays.asList(new String[] {"name", "level"}));
    when(divisionRepository.findDivisionByName("")).thenReturn(null);

    // Run test
    validator.validate(divisionForm, errors);

    // Verify
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("requiredField.error", errors.getFieldError("name").getCode());
  }
  
  @Test
  public void testValidateFormWithExistingNameAndNewId_shouldGetError() throws Exception {
    // Set up data
    DivisionBackingForm divisionForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(1L).withLevel(1).withName("Level1Division").build();
    
    Division division = DivisionBuilder.aDivision()
        .withId(2L).withName("Level1Division").build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "division");

    // Set up mocks
    when(formFieldRepository.getRequiredFormFields("division")).thenReturn(Arrays.asList(new String[] {"name", "level"}));
    when(divisionRepository.findDivisionByName("Level1Division")).thenReturn(division);

    // Run test
    validator.validate(divisionForm, errors);

    // Verify
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("duplicate", errors.getFieldError("name").getCode());
  }
  
  @Test
  public void testValidateFormWithExistingNameAndId_shouldntGetError() throws Exception {
    // Set up data
    DivisionBackingForm divisionForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(1L).withLevel(1).withName("Level1Division").build();
    
    Division division = DivisionBuilder.aDivision()
        .withId(1L).withName("Level1Division").build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "division");

    // Set up mocks
    when(formFieldRepository.getRequiredFormFields("division")).thenReturn(Arrays.asList(new String[] {"name", "level"}));
    when(divisionRepository.findDivisionByName("Level1Division")).thenReturn(division);

    // Run test
    validator.validate(divisionForm, errors);

    // Verify
    Assert.assertEquals("No error", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidateFormWithNullLevel_shouldGetError() throws Exception {
    // Set up data
    DivisionBackingForm divisionForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(1L).withLevel(null).withName("Division").build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "division");

    // Set up mocks
    when(formFieldRepository.getRequiredFormFields("division")).thenReturn(Arrays.asList(new String[] {"name", "level"}));
    when(divisionRepository.findDivisionByName("Division")).thenReturn(null);

    // Run test
    validator.validate(divisionForm, errors);

    // Verify
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("requiredField.error", errors.getFieldError("level").getCode());
  }
  
  @Test
  public void testValidateFormWithLevelGreaterThanThree_shouldGetError() throws Exception {
    // Set up data
    DivisionBackingForm divisionForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(1L).withLevel(4).withName("Division").build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "division");

    // Set up mocks
    when(formFieldRepository.getRequiredFormFields("division")).thenReturn(Arrays.asList(new String[] {"name", "level"}));
    when(divisionRepository.findDivisionByName("Division")).thenReturn(null);

    // Run test
    validator.validate(divisionForm, errors);

    // Verify
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("invalid", errors.getFieldError("level").getCode());
  }
  
  @Test
  public void testValidateFormWithLevelLowerThanOne_shouldGetError() throws Exception {
    // Set up data
    DivisionBackingForm divisionForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(1L).withLevel(0).withName("Division").build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "division");

    // Set up mocks
    when(formFieldRepository.getRequiredFormFields("division")).thenReturn(Arrays.asList(new String[] {"name", "level"}));
    when(divisionRepository.findDivisionByName("Division")).thenReturn(null);

    // Run test
    validator.validate(divisionForm, errors);

    // Verify
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("invalid", errors.getFieldError("level").getCode());
  }
  
  @Test
  public void testValidateFormWithNullParentId_shouldGetError() throws Exception {
    // Set up data
    DivisionBackingForm parentForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(null).withLevel(1).withName("Level1Division").build();
    
    DivisionBackingForm divisionForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(2L).withLevel(2).withName("Level2Division").withParent(parentForm).build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "division");
    
    // Set up mocks
    when(divisionRepository.findDivisionByName("Level2Division")).thenReturn(null);
    when(divisionRepository.findDivisionById(1L)).thenThrow(new NoResultException());

    // Run test
    validator.validate(divisionForm, errors);

    // Verify
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("required", errors.getFieldError("parent.id").getCode());
  }
  
  @Test
  public void testValidateFormWithNonExistentParent_shouldGetError() throws Exception {
    // Set up data
    DivisionBackingForm parentForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(1L).withLevel(1).withName("Level1Division").build();
    
    DivisionBackingForm divisionForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(2L).withLevel(2).withName("Level2Division").withParent(parentForm).build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "division");
    
    // Set up mocks
    when(divisionRepository.findDivisionByName("Level2Division")).thenReturn(null);
    when(divisionRepository.findDivisionById(1L)).thenThrow(new NoResultException());

    // Run test
    validator.validate(divisionForm, errors);

    // Verify
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("invalid", errors.getFieldError("parent").getCode());
  }
  
  @Test
  public void testValidateFormWithLevel2AndParentLevelNot1_shouldGetError() throws Exception {
    // Set up data
    DivisionBackingForm parentForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(1L).withLevel(3).withName("Level3Division").build();
    
    DivisionBackingForm divisionForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(2L).withLevel(2).withName("Level2Division").withParent(parentForm).build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "division");
    
    // Set up mocks
    when(divisionRepository.findDivisionByName("Level2Division")).thenReturn(null);
    when(divisionRepository.findDivisionById(1L)).thenThrow(new NoResultException());

    // Run test
    validator.validate(divisionForm, errors);

    // Verify
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("invalid", errors.getFieldError("parent").getCode());
  }
  
  @Test
  public void testValidateFormWithLevel3AndParentLevelNot2_shouldGetError() throws Exception {
    // Set up data
    DivisionBackingForm parentForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(1L).withLevel(1).withName("Level1Division").build();
    
    DivisionBackingForm divisionForm = DivisionBackingFormBuilder.aDivisionBackingForm()
        .withId(2L).withLevel(3).withName("Level3Division").withParent(parentForm).build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "division");
    
    // Set up mocks
    when(divisionRepository.findDivisionByName("Level3Division")).thenReturn(null);
    when(divisionRepository.findDivisionById(1L)).thenThrow(new NoResultException());

    // Run test
    validator.validate(divisionForm, errors);

    // Verify
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("invalid", errors.getFieldError("parent").getCode());
  }

}
