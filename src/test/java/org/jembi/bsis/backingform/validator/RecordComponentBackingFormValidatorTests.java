package org.jembi.bsis.backingform.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBackingFormBuilder.aComponentTypeCombinationBackingForm;
import static org.jembi.bsis.helpers.builders.RecordComponentBackingFormBuilder.aRecordComponentBackingForm;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.jembi.bsis.backingform.ComponentTypeCombinationBackingForm;
import org.jembi.bsis.backingform.RecordComponentBackingForm;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.ComponentTypeCombinationRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class RecordComponentBackingFormValidatorTests extends UnitTestSuite {

  @InjectMocks
  private RecordComponentBackingFormValidator recordComponentBackingFormValidator;
  @Mock
  private ComponentTypeCombinationRepository componentTypeCombinationRepository;
  @Mock
  private ComponentRepository componentRepository;
  
  private static Date processedOn = new Date();
  UUID componentTypeCombinationId = UUID.randomUUID();
  private static final UUID COMPONENT_ID = UUID.randomUUID();

  private RecordComponentBackingForm getBaseBackingForm() {
    ComponentTypeCombinationBackingForm combinationBackingForm =
        aComponentTypeCombinationBackingForm().withId(componentTypeCombinationId).build();

    return aRecordComponentBackingForm()
        .withParentComponentId(COMPONENT_ID)
        .withComponentTypeCombination(combinationBackingForm)
        .withProcessedOn(processedOn)
        .build();
  }
  
  @Test
  public void testValidateForm_shouldHaveNoErrors() {
    // Setup data
    RecordComponentBackingForm backingForm = getBaseBackingForm();

    Component parentComponent = aComponent()
        .withId(COMPONENT_ID)
        .withCreatedOn(new DateTime(processedOn).minusDays(5).toDate())
        .build();

    when(componentTypeCombinationRepository.verifyComponentTypeCombinationExists(componentTypeCombinationId)).thenReturn(true);
    when(componentRepository.verifyComponentExists(COMPONENT_ID)).thenReturn(true);
    when(componentRepository.findComponentById(COMPONENT_ID)).thenReturn(parentComponent);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "RecordComponentBackingForm");
    recordComponentBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(0));
  }
  
  @Test
  public void testValidateFormWithNoProcessedOn_shouldHaveOneError() {
    // Setup data
    RecordComponentBackingForm backingForm = getBaseBackingForm();
    backingForm.setProcessedOn(null);

    when(componentTypeCombinationRepository.verifyComponentTypeCombinationExists(componentTypeCombinationId)).thenReturn(true);
    when(componentRepository.verifyComponentExists(COMPONENT_ID)).thenReturn(true);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "RecordComponentBackingForm");
    recordComponentBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("processedOn").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithFutureProcessedOn_shouldHaveOneError() {
    // Setup data
    RecordComponentBackingForm backingForm = getBaseBackingForm();
    DateTime future = new DateTime(processedOn).plusDays(5);
    backingForm.setProcessedOn(future.toDate());

    Component parentComponent = aComponent()
        .withId(COMPONENT_ID)
        .withCreatedOn(new DateTime(processedOn).minusDays(5).toDate())
        .build();

    when(componentTypeCombinationRepository.verifyComponentTypeCombinationExists(componentTypeCombinationId)).thenReturn(true);
    when(componentRepository.verifyComponentExists(COMPONENT_ID)).thenReturn(true);
    when(componentRepository.findComponentById(COMPONENT_ID)).thenReturn(parentComponent);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "RecordComponentBackingForm");
    recordComponentBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("processedOn").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateFormWithProcessedOnBeforeParentComponentCreatedOn_shouldHaveOneError() {
    // Setup data
    RecordComponentBackingForm backingForm = getBaseBackingForm();
    DateTime dateBeforeCreatedOn = new DateTime(processedOn).minusDays(10);
    backingForm.setProcessedOn(dateBeforeCreatedOn.toDate());

    Component parentComponent = aComponent()
        .withId(COMPONENT_ID)
        .withCreatedOn(new DateTime(processedOn).minusDays(5).toDate())
        .build();

    when(componentTypeCombinationRepository.verifyComponentTypeCombinationExists(componentTypeCombinationId)).thenReturn(true);
    when(componentRepository.verifyComponentExists(COMPONENT_ID)).thenReturn(true);
    when(componentRepository.findComponentById(COMPONENT_ID)).thenReturn(parentComponent);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "RecordComponentBackingForm");
    recordComponentBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("processedOn").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateFormWithNoParentComponentId_shouldHaveOneError() {
    // Setup data
    RecordComponentBackingForm backingForm = getBaseBackingForm();
    backingForm.setParentComponentId(null);

    Component parentComponent = aComponent()
        .withId(COMPONENT_ID)
        .withCreatedOn(new DateTime(processedOn).minusDays(5).toDate())
        .build();

    when(componentTypeCombinationRepository.verifyComponentTypeCombinationExists(componentTypeCombinationId)).thenReturn(true);
    when(componentRepository.verifyComponentExists(COMPONENT_ID)).thenReturn(true);
    when(componentRepository.findComponentById(COMPONENT_ID)).thenReturn(parentComponent);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "RecordComponentBackingForm");
    recordComponentBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("parentComponentId").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithInvalidParentComponentId_shouldHaveOneError() {
    // Setup data
    RecordComponentBackingForm backingForm = getBaseBackingForm();

    Component parentComponent = aComponent()
        .withId(COMPONENT_ID)
        .withCreatedOn(new DateTime(processedOn).minusDays(5).toDate())
        .build();

    when(componentTypeCombinationRepository.verifyComponentTypeCombinationExists(componentTypeCombinationId)).thenReturn(true);
    when(componentRepository.verifyComponentExists(COMPONENT_ID)).thenReturn(false);
    when(componentRepository.findComponentById(COMPONENT_ID)).thenReturn(parentComponent);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "RecordComponentBackingForm");
    recordComponentBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("parentComponentId").getCode(), is("errors.invalid"));
  }

  @Test
  public void testValidateFormWithNoCombination_shouldHaveOneError() {
    // Setup data
    RecordComponentBackingForm backingForm = getBaseBackingForm();
    backingForm.setComponentTypeCombination(null);

    Component parentComponent = aComponent()
        .withId(COMPONENT_ID)
        .withCreatedOn(new DateTime(processedOn).minusDays(5).toDate())
        .build();

    when(componentTypeCombinationRepository.verifyComponentTypeCombinationExists(componentTypeCombinationId)).thenReturn(true);
    when(componentRepository.verifyComponentExists(COMPONENT_ID)).thenReturn(true);
    when(componentRepository.findComponentById(COMPONENT_ID)).thenReturn(parentComponent);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "RecordComponentBackingForm");
    recordComponentBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("componentTypeCombination").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithNoCombinationId_shouldHaveOneError() {
    // Setup data
    RecordComponentBackingForm backingForm = getBaseBackingForm();
    ComponentTypeCombinationBackingForm combinationForm = backingForm.getComponentTypeCombination();
    combinationForm.setId(null);
    backingForm.setComponentTypeCombination(combinationForm);

    Component parentComponent = aComponent()
        .withId(COMPONENT_ID)
        .withCreatedOn(new DateTime(processedOn).minusDays(5).toDate())
        .build();

    when(componentTypeCombinationRepository.verifyComponentTypeCombinationExists(componentTypeCombinationId)).thenReturn(true);
    when(componentRepository.verifyComponentExists(COMPONENT_ID)).thenReturn(true);
    when(componentRepository.findComponentById(COMPONENT_ID)).thenReturn(parentComponent);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "RecordComponentBackingForm");
    recordComponentBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("componentTypeCombination.id").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithInvalidCombinationId_shouldHaveOneError() {
    // Setup data
    RecordComponentBackingForm backingForm = getBaseBackingForm();

    Component parentComponent = aComponent()
                                .withId(COMPONENT_ID)
                                .withCreatedOn(new DateTime(processedOn).minusDays(5).toDate())
                                .build();

    when(componentTypeCombinationRepository.verifyComponentTypeCombinationExists(componentTypeCombinationId)).thenReturn(false);
    when(componentRepository.verifyComponentExists(COMPONENT_ID)).thenReturn(true);
    when(componentRepository.findComponentById(COMPONENT_ID)).thenReturn(parentComponent);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "RecordComponentBackingForm");
    recordComponentBackingFormValidator.validateForm(backingForm, errors);

    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError("componentTypeCombination.id").getCode(), is("errors.invalid"));
  }
 
}
