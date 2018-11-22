package org.jembi.bsis.backingform.validator;

import static org.jembi.bsis.helpers.builders.DiscardReasonBackingFormBuilder.aDiscardReasonBackingForm;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.UUID;

import org.jembi.bsis.backingform.DiscardReasonBackingForm;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.repository.DiscardReasonRepository;
import org.jembi.bsis.repository.FormFieldRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(MockitoJUnitRunner.class)
public class DiscardReasonBackingFormValidatorTest {

  @InjectMocks
  DiscardReasonBackingFormValidator discardReasonBackingFormValidator;
  @Mock
  DiscardReasonRepository discardReasonRepository;
  @Mock
  FormFieldRepository formFieldRepository;

  @Test
  public void testValid() throws Exception {
    // set up data
    DiscardReasonBackingForm form = aDiscardReasonBackingForm()
        .withReason("REASON")
        .build();

    // set up mocks
    when(discardReasonRepository.findDiscardReason("REASON")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "discardReason");
    discardReasonBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidUpdate() throws Exception {
    // set up data
    UUID discardReasonId = UUID.randomUUID();
    ComponentStatusChangeReason discardReason = new ComponentStatusChangeReason();
    discardReason.setId(discardReasonId);
    discardReason.setStatusChangeReason("REASON");

    DiscardReasonBackingForm form = aDiscardReasonBackingForm()
        .withId(discardReasonId)
        .withReason("REASON")
        .build();

    // set up mocks
    when(discardReasonRepository.findDiscardReason("REASON")).thenReturn(discardReason);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "discardReason");
    discardReasonBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidBlankReason() throws Exception {
    // set up data
    DiscardReasonBackingForm form = aDiscardReasonBackingForm()
        .withReason("")
        .build();

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "discardReason");
    discardReasonBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidDuplicate() throws Exception {
    // set up data
    ComponentStatusChangeReason duplicate = new ComponentStatusChangeReason();
    duplicate.setId(UUID.randomUUID());
    duplicate.setStatusChangeReason("REASON");

    DiscardReasonBackingForm form = aDiscardReasonBackingForm()
        .withId(UUID.randomUUID())
        .withReason("REASON")
        .build();

    // set up mocks
    when(discardReasonRepository.findDiscardReason("REASON")).thenReturn(duplicate);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "discardReason");
    discardReasonBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: discard reason exists", errors.getFieldError("reason"));
  }
}
