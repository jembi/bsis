package validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import backingform.BloodTypingResolutionBackingForm;
import backingform.validator.BloodTypingResolutionBackingFormValidator;

@RunWith(MockitoJUnitRunner.class)
public class BloodTypingResolutionBackingFormValidatorTests {

  @InjectMocks
  private BloodTypingResolutionBackingFormValidator validator;

  @Test
  public void testValidateEmptyFields() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setResolved(true);
    backingForm.setBloodAbo(null);
    backingForm.setBloodRh(null);

    Errors errors = new BindException(backingForm, "bloodTypingResolutionBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(2));
  }

}
