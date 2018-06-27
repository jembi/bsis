package org.jembi.bsis.backingform.validator;

import java.util.UUID;

import org.jembi.bsis.backingform.TestBatchDonationsBackingForm;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class TestBatchDonationsBackingFormValidator extends BaseValidator<TestBatchDonationsBackingForm> {

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private TestBatchRepository testBatchRepository;

  @Override
  public void validateForm(TestBatchDonationsBackingForm form, Errors errors) {

    TestBatch testBatch = null;
    if (form.getTestBatchId() == null) {
      errors.rejectValue("testBatchId", "errors.required", "testBatchId is required");
    } else {
      testBatch = testBatchRepository.findTestBatchById(form.getTestBatchId());
      if (testBatch == null) {
        errors.rejectValue("testBatchId", "errors.invalid", "invalid testBatchId");
      } else if (testBatch.getStatus().equals(TestBatchStatus.RELEASED)) {
        errors.rejectValue("testBatchId", "errors.testBatchIsReleased", "testBatch has been released");
      }
    }

    if (form.getDonationIds() != null) {
      for (UUID id : form.getDonationIds()) {
        validateDonation(donationRepository.findDonationById(id), testBatch, errors,
            form.getDonationIds().indexOf(id));
      }
    }
  }

  private void validateDonation(Donation donation, TestBatch testBatch, Errors errors, int index) {
    if (donation == null) {
      errors.rejectValue("donationIds[" + index + "]", "errors.invalid", "invalid donationId");
    } else if (testBatch != null && !donation.getTestBatch().equals(testBatch)) {
      errors.rejectValue("donationIds[" + index + "]", "errors.donationBelongsToAnotherTestBatch",
          "This donation belongs to another test batch");
    }
  }

  @Override
  public String getFormName() {
    return "testBatchDonationsBackingForm";
  }

}
