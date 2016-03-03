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
import backingform.BloodTypingResolutionBackingForm.FinalBloodTypingMatchStatus;
import backingform.validator.BloodTypingResolutionBackingFormValidator;

@RunWith(MockitoJUnitRunner.class)
public class BloodTypingResolutionBackingFormValidatorTests {

  @InjectMocks
  private BloodTypingResolutionBackingFormValidator validator;

  @Test
  public void testValidateEmptyAboAndRhFields() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setStatus(FinalBloodTypingMatchStatus.RESOLVED);
    backingForm.setBloodAbo(null);
    backingForm.setBloodRh(null);

    Errors errors = new BindException(backingForm, "bloodTypingResolutionBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(2));
  }

  @Test
  public void testValidateEmptyBloodRh() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setStatus(FinalBloodTypingMatchStatus.RESOLVED);
    backingForm.setBloodAbo("O");
    backingForm.setBloodRh(null);

    Errors errors = new BindException(backingForm, "bloodTypingResolutionBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
  }

  @Test
  public void testValidateEmptyBloodAbo() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setStatus(FinalBloodTypingMatchStatus.RESOLVED);
    backingForm.setBloodAbo(null);
    backingForm.setBloodRh("+");

    Errors errors = new BindException(backingForm, "bloodTypingResolutionBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
  }

  @Test
  public void testValidateNoErrors() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setStatus(FinalBloodTypingMatchStatus.RESOLVED);
    backingForm.setBloodAbo("O");
    backingForm.setBloodRh("+");

    Errors errors = new BindException(backingForm, "bloodTypingResolutionBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateEmptyStatus() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setStatus(null);
    backingForm.setBloodAbo("O");
    backingForm.setBloodRh("+");

    Errors errors = new BindException(backingForm, "bloodTypingResolutionBackingForm");
    validator.validate(backingForm, errors);

    assertThat(errors.getErrorCount(), is(1));
  }

}
