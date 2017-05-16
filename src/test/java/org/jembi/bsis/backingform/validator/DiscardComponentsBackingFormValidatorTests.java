package org.jembi.bsis.backingform.validator;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.DiscardComponentsBackingForm;
import org.jembi.bsis.backingform.DiscardReasonBackingForm;
import org.jembi.bsis.backingform.validator.DiscardComponentsBackingFormValidator;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.DiscardReasonRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(MockitoJUnitRunner.class)
public class DiscardComponentsBackingFormValidatorTests {

  @InjectMocks
  private DiscardComponentsBackingFormValidator validator;
  
  @Mock
  private DiscardReasonRepository discardReasonRepository;

  @Mock
  private ComponentRepository componentRepository;
  
  private static final UUID COMPONENT_ID_1 = UUID.randomUUID();
  private static final UUID COMPONENT_ID_2 = UUID.randomUUID();

  @Test
  public void testValidate_hasNoErrors() {
    // set up data
    List<UUID> componentIds = Arrays.asList(COMPONENT_ID_1, COMPONENT_ID_2);
    UUID discardReasonId = UUID.randomUUID();
    DiscardReasonBackingForm discardReasonBackingForm = new DiscardReasonBackingForm();
    discardReasonBackingForm.setId(discardReasonId);
    DiscardComponentsBackingForm form = new DiscardComponentsBackingForm();
    form.setComponentIds(componentIds);
    form.setDiscardReason(discardReasonBackingForm);
    form.setDiscardReasonText("text");

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "DiscardComponents");
    
    // set up mocks
    when(componentRepository.verifyComponentExists(COMPONENT_ID_1)).thenReturn(true);
    when(componentRepository.verifyComponentExists(COMPONENT_ID_2)).thenReturn(true);
    when(discardReasonRepository.verifyDiscardReasonExists(discardReasonId)).thenReturn(true);
    
    // run test
    validator.validateForm(form, errors);

    // do checks
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidateNoComponentIds_getComponentIdsRequiredError() {
    // set up data
    UUID discardReasonId = UUID.randomUUID();
    DiscardReasonBackingForm discardReasonBackingForm = new DiscardReasonBackingForm();
    discardReasonBackingForm.setId(discardReasonId);
    DiscardComponentsBackingForm form = new DiscardComponentsBackingForm();
    form.setDiscardReason(discardReasonBackingForm);
    form.setDiscardReasonText("text");

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "DiscardComponents");

    // set up mocks
    when(discardReasonRepository.verifyDiscardReasonExists(discardReasonId)).thenReturn(true);

    // run test
    validator.validateForm(form, errors);

    // do checks
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("componentIds to discard are required", errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidateInvalidComponentId_getInvalidComponentIdError() {
    // set up data
    UUID discardReasonId = UUID.randomUUID();
    List<UUID> componentIds = Arrays.asList(COMPONENT_ID_1, COMPONENT_ID_2);
    DiscardReasonBackingForm discardReasonBackingForm = new DiscardReasonBackingForm();
    discardReasonBackingForm.setId(discardReasonId);
    DiscardComponentsBackingForm form = new DiscardComponentsBackingForm();
    form.setComponentIds(componentIds);
    form.setDiscardReason(discardReasonBackingForm);
    form.setDiscardReasonText("text");

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "DiscardComponents");

    // set up mocks
    when(componentRepository.verifyComponentExists(COMPONENT_ID_1)).thenReturn(true);
    when(componentRepository.verifyComponentExists(COMPONENT_ID_2)).thenReturn(false);
    when(discardReasonRepository.verifyDiscardReasonExists(discardReasonId)).thenReturn(true);

    // run test
    validator.validateForm(form, errors);

    // do checks
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("invalid componentId", errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidateNoDiscardReason_getDiscardReasonRequiredError() {
    // set up data
    List<UUID> componentIds = Arrays.asList(COMPONENT_ID_1, COMPONENT_ID_2);
    DiscardComponentsBackingForm form = new DiscardComponentsBackingForm();
    form.setComponentIds(componentIds);
    form.setDiscardReasonText("text");

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "DiscardComponents");

    // set up mocks
    when(componentRepository.verifyComponentExists(COMPONENT_ID_1)).thenReturn(true);
    when(componentRepository.verifyComponentExists(COMPONENT_ID_2)).thenReturn(true);

    // run test
    validator.validateForm(form, errors);

    // do checks
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("discardReason is required", errors.getFieldErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidateInvalidDiscardReasonId_getInvalidDiscardReasonError() {
    // set up data
    UUID discardReasonId = UUID.randomUUID();
    List<UUID> componentIds = Arrays.asList(COMPONENT_ID_1, COMPONENT_ID_2);
    DiscardReasonBackingForm discardReasonBackingForm = new DiscardReasonBackingForm();
    discardReasonBackingForm.setId(discardReasonId);
    DiscardComponentsBackingForm form = new DiscardComponentsBackingForm();
    form.setComponentIds(componentIds);
    form.setDiscardReason(discardReasonBackingForm);
    form.setDiscardReasonText("text");

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "DiscardComponents");

    // set up mocks
    when(componentRepository.verifyComponentExists(COMPONENT_ID_1)).thenReturn(true);
    when(componentRepository.verifyComponentExists(COMPONENT_ID_2)).thenReturn(true);
    when(discardReasonRepository.verifyDiscardReasonExists(discardReasonId)).thenReturn(false);

    // run test
    validator.validateForm(form, errors);

    // do checks
    Assert.assertEquals("One error", 1, errors.getErrorCount());
    Assert.assertEquals("invalid discardReason", errors.getFieldErrors().get(0).getDefaultMessage());
  }

}
