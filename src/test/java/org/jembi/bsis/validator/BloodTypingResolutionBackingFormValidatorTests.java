package org.jembi.bsis.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTypingResolutionsBackingFormBuilder.aBloodTypingResolutionsBackingForm;

import org.jembi.bsis.backingform.BloodTypingResolutionBackingForm;
import org.jembi.bsis.backingform.BloodTypingResolutionsBackingForm;
import org.jembi.bsis.backingform.validator.BloodTypingResolutionsBackingFormValidator;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

@RunWith(MockitoJUnitRunner.class)
public class BloodTypingResolutionBackingFormValidatorTests {

  @InjectMocks
  private BloodTypingResolutionsBackingFormValidator validator;

  @Test
  public void testValidateEmptyAboAndRhFieldsAndResolved() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setStatus(BloodTypingMatchStatus.RESOLVED);
    backingForm.setBloodAbo(null);
    backingForm.setBloodRh(null);
    
    BloodTypingResolutionsBackingForm backingFormList = aBloodTypingResolutionsBackingForm()
        .withBloodTypingResolution(backingForm)
        .build();

    Errors errors = new BindException(backingFormList, "bloodTypingResolutionBackingFormList");
    validator.validate(backingFormList, errors);

    assertThat(errors.getErrorCount(), is(2));
  }

  @Test
  public void testValidateEmptyAboAndRhFieldsAndNoTypeDetermined() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setStatus(BloodTypingMatchStatus.NO_TYPE_DETERMINED);
    backingForm.setBloodAbo(null);
    backingForm.setBloodRh(null);
    
    BloodTypingResolutionsBackingForm backingFormList = aBloodTypingResolutionsBackingForm()
        .withBloodTypingResolution(backingForm)
        .build();

    Errors errors = new BindException(backingFormList, "bloodTypingResolutionBackingFormList");
    validator.validate(backingFormList, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateEmptyBloodRh() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setStatus(BloodTypingMatchStatus.RESOLVED);
    backingForm.setBloodAbo("O");
    backingForm.setBloodRh(null);
    
    BloodTypingResolutionsBackingForm backingFormList = aBloodTypingResolutionsBackingForm()
        .withBloodTypingResolution(backingForm)
        .build();

    Errors errors = new BindException(backingFormList, "bloodTypingResolutionBackingFormList");
    validator.validate(backingFormList, errors);

    assertThat(errors.getErrorCount(), is(1));
  }

  @Test
  public void testValidateEmptyBloodAbo() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setStatus(BloodTypingMatchStatus.RESOLVED);
    backingForm.setBloodAbo(null);
    backingForm.setBloodRh("+");
    
    BloodTypingResolutionsBackingForm backingFormList = aBloodTypingResolutionsBackingForm()
        .withBloodTypingResolution(backingForm)
        .build();

    Errors errors = new BindException(backingFormList, "bloodTypingResolutionBackingFormList");
    validator.validate(backingFormList, errors);

    assertThat(errors.getErrorCount(), is(1));
  }

  @Test
  public void testValidateNoErrors() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setStatus(BloodTypingMatchStatus.RESOLVED);
    backingForm.setBloodAbo("O");
    backingForm.setBloodRh("+");
    
    BloodTypingResolutionsBackingForm backingFormList = aBloodTypingResolutionsBackingForm()
        .withBloodTypingResolution(backingForm)
        .build();

    Errors errors = new BindException(backingFormList, "bloodTypingResolutionBackingFormList");
    validator.validate(backingFormList, errors);

    assertThat(errors.getErrorCount(), is(0));
  }

  @Test
  public void testValidateEmptyStatus() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setStatus(null);
    backingForm.setBloodAbo("O");
    backingForm.setBloodRh("+");
    
    BloodTypingResolutionsBackingForm backingFormList = aBloodTypingResolutionsBackingForm()
        .withBloodTypingResolution(backingForm)
        .build();

    Errors errors = new BindException(backingFormList, "bloodTypingResolutionBackingFormList");
    validator.validate(backingFormList, errors);

    assertThat(errors.getErrorCount(), is(1));
  }

  @Test
  public void testValidateInvalidStatus() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setStatus(BloodTypingMatchStatus.AMBIGUOUS);
    backingForm.setBloodAbo("O");
    backingForm.setBloodRh("+");
    
    BloodTypingResolutionsBackingForm backingFormList = aBloodTypingResolutionsBackingForm()
        .withBloodTypingResolution(backingForm)
        .build();

    Errors errors = new BindException(backingFormList, "bloodTypingResolutionBackingFormList");
    validator.validate(backingFormList, errors);

    assertThat(errors.getErrorCount(), is(1));
  }

}
