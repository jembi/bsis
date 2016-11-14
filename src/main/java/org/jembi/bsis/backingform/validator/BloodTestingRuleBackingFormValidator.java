package org.jembi.bsis.backingform.validator;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.BloodTestingRuleBackingForm;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
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
      if (bloodTest != null && !bloodTest.getValidResultsList().contains(pattern)) {
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

    // Validate newInformation
    String newInformation = form.getNewInformation();
    if (StringUtils.isBlank(newInformation)) {
      errors.rejectValue("newInformation", "errors.required", "New Information is required");
    } else if (newInformation.length() > MAX_LENGTH_NEW_INFORMATION) {
      errors.rejectValue("newInformation", "errors.fieldLength",
          "Maximum length for this field is " + MAX_LENGTH_NEW_INFORMATION);
    } else {
      // validate that newInformation matches possible values
      if (validDonationField
          && !DonationField.getNewInformationForDonationField(donationFieldChanged).contains(newInformation)) {
        errors.rejectValue("newInformation", "errors.invalid", "Invalid newInformation, doesn't match donation field");
      }
    }

    // Validate pendingTestsIds
    Set<Long> pendingTestsIds = form.getPendingTestsIds();
    if (pendingTestsIds == null || pendingTestsIds.isEmpty()) {
      errors.rejectValue("pendingTestsIds", "errors.required", "Pending Tests Ids are required");
    } else {
      String pendingTestsIdsError = "";
      for (Long id : pendingTestsIds) {
        pendingTestsIdsError = validatePendingTest(id, bloodTest, pendingTestsIdsError);
      }
      if (pendingTestsIdsError.length() > 0) {
        errors.rejectValue("pendingTestsIds", "errors.invalid",
            "The following pending blood test id(s): [" + pendingTestsIdsError.substring(2) + "] are invalid.");
      }
    }

    // Validate isDeleted
    if (form.getIsDeleted() == null) {
      errors.rejectValue("isDeleted", "errors.required", "isDeleted is required");
    }
  }

  private String validatePendingTest(Long id, BloodTest bloodTest, String pendingTestsError) {
    if (id != null) {
      boolean testExists = bloodTestRepository.verifyBloodTestExists(Long.valueOf(id));
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