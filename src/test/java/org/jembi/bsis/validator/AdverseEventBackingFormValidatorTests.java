package org.jembi.bsis.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.AdverseEventBackingFormBuilder.anAdverseEventBackingForm;
import static org.jembi.bsis.helpers.builders.AdverseEventTypeBackingFormBuilder.anAdverseEventTypeBackingForm;

import org.jembi.bsis.backingform.AdverseEventBackingForm;
import org.jembi.bsis.backingform.validator.AdverseEventBackingFormValidator;
import org.jembi.bsis.model.donation.Donation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindException;

@RunWith(MockitoJUnitRunner.class)
public class AdverseEventBackingFormValidatorTests {

  @InjectMocks
  private AdverseEventBackingFormValidator adverseEventBackingFormValidator;

  @Test
  public void testSupportsWithUnsupportedClass_shouldReturnFalse() {
    boolean result = adverseEventBackingFormValidator.supports(Donation.class);
    assertThat(result, is(false));
  }

  @Test
  public void testSupportsWithAdverseEventBackingFormClass_shouldReturnTrue() {
    boolean result = adverseEventBackingFormValidator.supports(AdverseEventBackingForm.class);
    assertThat(result, is(true));
  }

  @Test
  public void testValidateWithNoAdverseEventType_shouldHaveErrors() {
    AdverseEventBackingForm adverseEventBackingForm = anAdverseEventBackingForm().build();

    BindException errors = new BindException(adverseEventBackingForm, "AdverseEventBackingForm");
    adverseEventBackingFormValidator.validate(adverseEventBackingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldErrorCount(), is(1));
    assertThat(errors.getFieldError().getDefaultMessage(), is("Adverse event type is required"));
  }

  @Test
  public void testValidateWithValidBackingForm_shouldHaveNoErrors() {
    AdverseEventBackingForm adverseEventBackingForm = anAdverseEventBackingForm()
        .withType(anAdverseEventTypeBackingForm().build())
        .build();

    BindException errors = new BindException(adverseEventBackingForm, "AdverseEventBackingForm");
    adverseEventBackingFormValidator.validate(adverseEventBackingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }
}
