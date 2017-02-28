package org.jembi.bsis.backingform.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBackingFormBuilder.aTransfusionReactionTypeBackingForm;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBuilder.aTransfusionReactionType;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.jembi.bsis.backingform.TransfusionReactionTypeBackingForm;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.repository.TransfusionReactionTypeRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class TransfusionReactionTypeBackingFormValidatorTests extends UnitTestSuite {
  
  @InjectMocks
  private TransfusionReactionTypeBackingFormValidator transfusionReactionTypeBackingFormValidator;
  
  @Mock
  private TransfusionReactionTypeRepository transfusionReactionTypeRepository;
  
  @Test
  public void testValidateForm_shouldHaveNoErrors() {
    // Set up fixture
    TransfusionReactionType reactionType = aTransfusionReactionType().withId(1L).build();
    TransfusionReactionTypeBackingForm form = aTransfusionReactionTypeBackingForm()
        .withId(1L)
        .withName("reactionName")
        .withDescription("Transfusion reaction name")
        .withIsDeleted(false)
        .build();
    
    when(transfusionReactionTypeRepository.findById(1L)).thenReturn(reactionType);
    when(transfusionReactionTypeRepository.isUniqueTransfusionReactionTypeName(form.getId(),form.getName()))
        .thenReturn(true);
    
    // Test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "TransfusionReactionType");
    transfusionReactionTypeBackingFormValidator.validate(form, errors);
    
    assertThat(errors.getFieldErrorCount(), is(0));
  }
  
  @Test
  public void testValidateForm_withNoReactionTypeName_shouldHaveOneError() {
    // Set up fixture
    TransfusionReactionType reactionType = aTransfusionReactionType().withId(1L).build();
    TransfusionReactionTypeBackingForm form = aTransfusionReactionTypeBackingForm()
        .withId(1L)
        .withName(null)
        .withIsDeleted(false)
        .build();
    
    when(transfusionReactionTypeRepository.findById(1L)).thenReturn(reactionType);
    when(transfusionReactionTypeRepository.isUniqueTransfusionReactionTypeName(form.getId(),form.getName()))
        .thenReturn(true);
    
    // Test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "TransfusionReactionType");
    transfusionReactionTypeBackingFormValidator.validate(form, errors);
    
    assertThat(errors.getFieldErrorCount(), is(1));
  }
  
  @Test
  public void testValidateFormWithNonUniqueName_shouldHaveOneError() {
    // Set up fixture
    TransfusionReactionType reactionType = aTransfusionReactionType().withId(1L).build();
    TransfusionReactionTypeBackingForm form = aTransfusionReactionTypeBackingForm()
        .withId(1L)
        .withName("reactionName")
        .withDescription("Transfusion reaction name")
        .withIsDeleted(false)
        .build();
    
    when(transfusionReactionTypeRepository.findById(1L)).thenReturn(reactionType);
    when(transfusionReactionTypeRepository.isUniqueTransfusionReactionTypeName(form.getId(),form.getName()))
        .thenReturn(false);
    
    // Test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "TransfusionReactionType");
    transfusionReactionTypeBackingFormValidator.validate(form, errors);
    
    assertThat(errors.getFieldErrorCount(), is(1));
  }
  
  @Test
  public void testValidateFormWithNullIsDeleted_shouldNotHaveErrors() {
    // Set up fixture
    TransfusionReactionType reactionType = aTransfusionReactionType().withId(1L).build();  
    TransfusionReactionTypeBackingForm form = aTransfusionReactionTypeBackingForm()
        .withId(1L)
        .withName("reactionName")
        .withDescription("Transfusion reaction name")
        .withIsDeleted(null)
        .build();
    
    when(transfusionReactionTypeRepository.findById(1L)).thenReturn(reactionType);
    when(transfusionReactionTypeRepository.isUniqueTransfusionReactionTypeName(form.getId(),form.getName()))
        .thenReturn(true);
    
    // Test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "TransfusionReactionType");
    transfusionReactionTypeBackingFormValidator.validate(form, errors);
    
    assertThat(errors.getFieldErrorCount(), is(1));
  }
  
}
