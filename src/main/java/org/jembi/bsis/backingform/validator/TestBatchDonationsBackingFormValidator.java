package org.jembi.bsis.backingform.validator;

import org.jembi.bsis.backingform.TestBatchDonationsBackingForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class TestBatchDonationsBackingFormValidator extends BaseValidator<TestBatchDonationsBackingForm> {

  @Override
  public void validateForm(TestBatchDonationsBackingForm form, Errors errors) {
    if (form.getTestBatchId() == null) {
      errors.rejectValue("testBatchId", "errors.required", "testBatchId is required");
    }

    if (form.getDonationIds() == null || form.getDonationIds().isEmpty()) {
      errors.rejectValue("donationIds", "errors.required", "donationIds may not be null or empty");
    }
  }

  @Override
  public String getFormName() {
    return "testBatchDonationsBackingForm";
  }

}
