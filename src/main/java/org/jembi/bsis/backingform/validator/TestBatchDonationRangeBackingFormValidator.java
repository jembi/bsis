package org.jembi.bsis.backingform.validator;

import java.util.List;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.TestBatchDonationRangeBackingForm;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class TestBatchDonationRangeBackingFormValidator extends BaseValidator<TestBatchDonationRangeBackingForm> {

  @Autowired
  private GeneralConfigAccessorService generalConfigAccessorService;

  @Autowired
  private DonationRepository donationRepository;
  
  @Autowired
  private TestBatchRepository testBatchRepository;

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
    if (!validateDonationRange(form, errors)) {
      return;
    }

    // check that the to/from DIN belong to valid Donations
    if (!validDonations(form, errors)) {
      return;
    }
    
    // check that all Donations in the range aren’t already associated with another TestBatch
    validateDonationsInRange(form, errors);
    
    //check that Donations can be added to the testbatch
    if (!validateTestBatchStatus(form, errors)) {
      return;
    }
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

  private boolean validDonations(TestBatchDonationRangeBackingForm form, Errors errors) {
    try {
      donationRepository.findDonationByDonationIdentificationNumber(form.getFromDIN());
    } catch (NoResultException e) {
      errors.rejectValue("fromDIN", "errors.invalid.donation", "Donation with DIN " + form.getFromDIN() + " does not exist");
      return false;
    }
    if (form.getToDIN() != null) {
      try {
        donationRepository.findDonationByDonationIdentificationNumber(form.getToDIN());
      } catch (NoResultException e) {
        errors.rejectValue("toDIN", "errors.invalid.donation", "Donation with DIN " + form.getFromDIN() + " does not exist");
        return false;
      }
    }
    return true;
  }

  private void validateDonationsInRange(TestBatchDonationRangeBackingForm form, Errors errors) {
    String toDIN = form.getToDIN(); 
    if (toDIN == null) {
      toDIN = form.getFromDIN();
    }
    List<Donation> donations = donationRepository.findDonationsBetweenTwoDins(form.getFromDIN(), toDIN);
    for (Donation donation : donations) {
      if (donation.getTestBatch() != null && !donation.getTestBatch().getId().equals(form.getTestBatchId())) {
        errors.reject("errors.donationsBelongToAnotherTestBatch", "Donation with DIN " + donation.getDonationIdentificationNumber() + " is assigned to a different TestBatch");
        break; // no need to log the same error twice
      }
    }
  }
  
  private boolean validateTestBatchStatus(TestBatchDonationRangeBackingForm form, Errors errors) {
    TestBatch testBatch = testBatchRepository.findTestBatchById(form.getTestBatchId());
    if (!TestBatchStatus.OPEN.equals(testBatch.getStatus())) {
      errors.reject("errors.invalid.testBatchStatus", "Donations can only be added to open test batches");
      return false;
    }
    return true;
  }

  @Override
  public String getFormName() {
    return "testBatchDonationRangeBackingForm";
  }

}
