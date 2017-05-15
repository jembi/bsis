package org.jembi.bsis.backingform.validator;

import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.backingform.BloodTestingRuleBackingForm;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.bloodtesting.rules.DonationField;
import org.jembi.bsis.repository.BloodTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class BloodTestingRuleBackingFormValidator extends BaseValidator<BloodTestingRuleBackingForm> {

  private static final Integer MAX_LENGTH_PATTERN = 50;
  private static final Integer MAX_LENGTH_NEW_INFORMATION = 30;

  @Autowired
  private BloodTestRepository bloodTestRepository;

  @Override
  public void validateForm(BloodTestingRuleBackingForm form, Errors errors) {

    // Validate bloodTest
    BloodTest bloodTest = null;
    BloodTestCategory category = null;
    if (form.getBloodTest() == null || form.getBloodTest().getId() == null) {
      errors.rejectValue("bloodTest", "errors.required", "Blood Test is required");
    } else {
      try {
        bloodTest = bloodTestRepository.findBloodTestById(form.getBloodTest().getId());
        category = bloodTest.getCategory();
      } catch (Exception e) {
        errors.rejectValue("bloodTest", "errors.invalid", "Blood Test is invalid");
      }
    }

    // Validate pattern
    String pattern = form.getPattern();
    if (StringUtils.isBlank(pattern)) {
      errors.rejectValue("pattern", "errors.required", "Pattern is required");
    } else if (pattern.length() > MAX_LENGTH_PATTERN) {
      errors.rejectValue("pattern", "errors.fieldLength", "Maximum length for this field is " + MAX_LENGTH_PATTERN);
    } else {
      // validate that pattern is one of bloodTest validResults
      if (bloodTest != null && !bloodTest.getValidResultsSet().contains(pattern)) {
        errors.rejectValue("pattern", "errors.invalid", "Invalid pattern, not in blood test valid results");
      }
    }

    // Validate donationFieldChanged
    DonationField donationFieldChanged = form.getDonationFieldChanged();
    boolean validDonationField = false;
    if (donationFieldChanged == null) {
      errors.rejectValue("donationFieldChanged", "errors.required", "Donation Field Changed is required");
    } else {
      // validate that donationFieldChanged matches the bloodTest category
      if (category != null && !DonationField.getDonationFieldsForCategory(category).contains(donationFieldChanged)) {
        errors.rejectValue("donationFieldChanged", "errors.invalid",
            "Invalid donationFieldChanged, doesn't match blood test category");
      } else {
        validDonationField = true;
      }
    }

    // Validate newInformation (if it has been specified)
    String newInformation = form.getNewInformation();
    if (StringUtils.isNotBlank(newInformation)) {
      if (newInformation.length() > MAX_LENGTH_NEW_INFORMATION) {
        errors.rejectValue("newInformation", "errors.fieldLength",
            "Maximum length for this field is " + MAX_LENGTH_NEW_INFORMATION);
      } else {
        // validate that newInformation matches possible values
        if (validDonationField
            && !DonationField.getNewInformationForDonationField(donationFieldChanged).contains(newInformation)) {
          errors.rejectValue("newInformation", "errors.invalid", "Invalid newInformation, doesn't match donation field");
        }
      }
    }

    // Validate pendingTests
    Set<BloodTestBackingForm> pendingTests = form.getPendingTests();
    if (pendingTests != null) {
      String pendingTestsError = "";
      for (BloodTestBackingForm pendingTest : pendingTests) {
        if (bloodTest != null) {
          if (bloodTest.getId().equals(pendingTest.getId())) {
            errors.rejectValue("pendingTests", "errors.invalid", "Selected blood test " + bloodTest.getId()
                + " cannot be included the list of pending blood tests");
          } else if (pendingTest.getBloodTestType().equals(BloodTestType.BASIC_TTI) || pendingTest.getBloodTestType().equals(BloodTestType.BASIC_BLOODTYPING)) {
            errors.rejectValue("pendingTests", "errors.invalid", "Pending tests cannot contain " +
                "blood test of type BASIC_TTI or BASIC_BLOODTYPING");
          } else if (!pendingTest.getCategory().equals(bloodTest.getCategory())) {
            errors.rejectValue("pendingTests", "errors.invalid",  "A pending test with category " +
                pendingTest.getCategory() + " is invalid. The pending test categories must match " +
                "the category of the the blood test i.e. " + bloodTest.getCategory());
          } else {
            pendingTestsError = validatePendingTest(pendingTest.getId(), pendingTestsError);
          }
        }
      }
      if (pendingTestsError.length() > 0) {
        errors.rejectValue("pendingTests", "errors.invalid",
            "The following pending blood test(s): [" + pendingTestsError.substring(2) + "] are invalid.");
      }
    }

    // Validate isDeleted
    if (form.getIsDeleted() == null) {
      errors.rejectValue("isDeleted", "errors.required", "isDeleted is required");
    }
  }

  private String validatePendingTest(UUID id, String pendingTestsError) {
    if (id != null) {
      boolean testExists = bloodTestRepository.verifyBloodTestExists(id);
      if (!testExists) {
        pendingTestsError += ", " + id.toString();
      }
    }
    return pendingTestsError;
  }

  @Override
  public String getFormName() {
    return "BloodTestingRuleBackingForm";
  }  
}