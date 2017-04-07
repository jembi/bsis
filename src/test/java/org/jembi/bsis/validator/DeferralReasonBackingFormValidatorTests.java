package org.jembi.bsis.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DeferralReasonBackingFormBuilder.aDeferralReasonBackingForm;
import static org.jembi.bsis.helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.DeferralReasonBackingForm;
import org.jembi.bsis.backingform.validator.DeferralReasonBackingFormValidator;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DurationType;
import org.jembi.bsis.repository.DeferralReasonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@RunWith(MockitoJUnitRunner.class)
public class DeferralReasonBackingFormValidatorTests {
  @InjectMocks
  private DeferralReasonBackingFormValidator validator;
  @Mock
  private DeferralReasonRepository deferralReasonRepository;

  @Test
  public void testValidateWithDeferralReasonBackingFormWithDuplicateDeferralReason_shouldHaveErrors() {
    DeferralReason deferralReason = aDeferralReason()
        .withId(UUID.randomUUID())
        .withReason("test")
        .build();
    DeferralReason deferralReasonDuplicate = aDeferralReason()
        .withId(UUID.randomUUID())
        .withReason("test")
        .build();
    DeferralReasonBackingForm backingForm = aDeferralReasonBackingForm()
        .withDurationType(DurationType.TEMPORARY)
        .withDeferralReason(deferralReason)
        .withDefaultDuration(1)
        .build();

    when(deferralReasonRepository.findDeferralReason("test")).thenReturn(deferralReasonDuplicate);

    Errors errors = new BindException(backingForm, "deferralReasonBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    List<FieldError> fieldErrors = errors.getFieldErrors("reason");
    assertThat(fieldErrors.size(), is(1));
    assertThat(fieldErrors.get(0).getDefaultMessage(), is("Deferral Reason already exists."));
  }

  @Test
  public void testValidateWithDeferralReasonBackingFormWithPermanentDurationTypeAndNoDuration_shouldHaveNoErrors() {
    DeferralReason deferralReason = aDeferralReason()
        .withReason("test")
        .build();
    DeferralReasonBackingForm backingForm = aDeferralReasonBackingForm()
        .withDurationType(DurationType.PERMANENT)
        .withDeferralReason(deferralReason)
        .withDefaultDuration(0)
        .build();

    when(deferralReasonRepository.findDeferralReason("test")).thenReturn(null);

    Errors errors = new BindException(backingForm, "deferralReasonBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateWithDeferralReasonBackingFormWithInvalidDefaultDeferralDays_shouldHaveErrors() {
    DeferralReason deferralReason = aDeferralReason()
        .withReason("test")
        .build();
    DeferralReasonBackingForm backingForm = aDeferralReasonBackingForm()
        .withDurationType(DurationType.TEMPORARY)
        .withDeferralReason(deferralReason)
        .withDefaultDuration(0)
        .build();

    when(deferralReasonRepository.findDeferralReason("test")).thenReturn(null);

    Errors errors = new BindException(backingForm, "deferralReasonBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    List<FieldError> fieldErrors = errors.getFieldErrors("defaultDuration");
    assertThat(fieldErrors.size(), is(1));
    assertThat(fieldErrors.get(0).getDefaultMessage(), is("Default duration must be a positive number of days"));
  }

  @Test
  public void testValidateWithValidDeferralReasonBackingForm_shouldHaveNoErrors() {
    DeferralReason deferralReason = aDeferralReason()
        .withReason("test")
        .build();
    DeferralReasonBackingForm backingForm = aDeferralReasonBackingForm()
        .withDurationType(DurationType.TEMPORARY)
        .withDeferralReason(deferralReason)
        .withDefaultDuration(1)
        .build();

    when(deferralReasonRepository.findDeferralReason("test")).thenReturn(null);

    Errors errors = new BindException(backingForm, "deferralReasonBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

}
