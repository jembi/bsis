package org.jembi.bsis.backingform.validator;

import org.jembi.bsis.backingform.TestBatchDonationRangeBackingForm;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class TestBatchDonationRangeBackingFormValidator extends BaseValidator<TestBatchDonationRangeBackingForm> {

  @Autowired
  private GeneralConfigAccessorService generalConfigAccessorService;

  @Override
  public void validateForm(TestBatchDonationRangeBackingForm form, Errors errors) {
    // check fromDIN has been entered
    if (form.getFromDIN() == null) {
      errors.rejectValue("fromDIN", "errors.required", "fromDIN is required");
      return;
    }

    // check the to/from DIN are the correct length
    if (!validateDINLength(form, errors)) {
      return;
    }
    
    // check the range is valid - fromDIN must be before toDIN - using a simple alphabetic comparison. Logged on fromDIN
    validateDonationRange(form, errors);
  }

  private boolean validateDINLength(TestBatchDonationRangeBackingForm form, Errors errors) {
    boolean valid = true;
    int dinLength = generalConfigAccessorService.getIntValue(GeneralConfigConstants.DIN_LENGTH);
    if (form.getFromDIN().length() != dinLength) {
      errors.rejectValue("fromDIN", "errors.invalid.length", "The fromDIN length must be " + dinLength + " characters");
      valid = false;
    }
    if (form.getToDIN() != null && form.getToDIN().length() != dinLength) {
      errors.rejectValue("toDIN", "errors.invalid.length", "The toDIN length must be " + dinLength + " characters");
      valid = false;
    }
    return valid;
  }

  private boolean validateDonationRange(TestBatchDonationRangeBackingForm form, Errors errors) {
    if (form.getToDIN() != null && form.getFromDIN().compareTo(form.getToDIN()) > 0 ) {
      errors.rejectValue("fromDIN", "errors.invalid.fromDINBeforeToDIN", "The fromDIN must be before the toDIN");
      return false;
    }
    return true;
  }


  @Override
  public String getFormName() {
    return "testBatchDonationRangeBackingForm";
  }

}
