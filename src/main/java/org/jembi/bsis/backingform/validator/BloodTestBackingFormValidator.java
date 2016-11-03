package org.jembi.bsis.backingform.validator;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.repository.bloodtesting.BloodTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class BloodTestBackingFormValidator extends BaseValidator<BloodTest> {

  private static final Integer MAX_TEST_NAME_LENGTH_NAME = 40;
  private static final Integer MAX_TEST_NAME_SHORT_LENGTH_NAME = 25;
  
  @Autowired
  private BloodTestRepository bloodTestRepository;
  
  @Override
  public void validateForm(BloodTest form, Errors errors) {
    
    // Validate testName
    if (StringUtils.isBlank(form.getTestName())) {
      errors.rejectValue("testName", "errors.required", "Test name is required");
    } else if (form.getTestName().length() > MAX_TEST_NAME_LENGTH_NAME) {
      errors.rejectValue("testName", "errors.fieldLength",
          "Maximum length for this field is " + MAX_TEST_NAME_LENGTH_NAME);
    } else {
      if (!bloodTestRepository.isUniqueTestName(form.getId(), form.getTestName())) {
        errors.rejectValue("testName", "errors.nonUnique", "Test name already exists");
      }
    }
    //  validate testNameShort
    if (StringUtils.isBlank(form.getTestNameShort())) {
      errors.rejectValue("testNameShort", "errors.required", "Test name short is required");
    } else if (form.getTestName().length() > MAX_TEST_NAME_SHORT_LENGTH_NAME) {
      errors.rejectValue("testNameShort", "errors.fieldLength",
          "Maximum length for this field is " + MAX_TEST_NAME_SHORT_LENGTH_NAME);
    }
    
    
    
    // Validate validOutcomes convert list to set for faster search
    Set<String> validOutcomes = new HashSet<>(form.getValidResultsList());
    if (validOutcomes == null || validOutcomes.isEmpty()) {
      errors.rejectValue("validResults", "errors.required", "Valid outcomes are required");
    } else {
      // validate that all positive outcomes are inside valid outcomes
      for (String positiveOutome : form.getPositiveResultsList()) {
        if (!validOutcomes.contains(positiveOutome)) {
          errors.rejectValue("positiveResults", "positiveResults.notInValidOutcomes", "Some positive outcomes are not in valid outcomes");
          break;
        }
      }
      // validate that all negative outcomes are inside valid outcomes
      for (String negativeOutcome : form.getNegativeResultsList()) {
        if (!validOutcomes.contains(negativeOutcome)) {
          errors.rejectValue("negativeResults", "negativeResults.notInValidOutcomes", "Some positive outcomes are not in valid outcomes");
          break;
        }
      }
    }
    
    // validate blood test and category are related
    if (form.getCategory() == null) {
      errors.rejectValue("category", "errors.required", "Category is required");
    } else {
      if (form.getBloodTestType() == null) {
        errors.rejectValue("bloodTestType", "errors.required", "Blood test type is required");
      } else {
        // check that blood test category and blood test type are matched
        Set<BloodTestType> categoryBloodTestTypes = new HashSet<>(BloodTestType.getBloodTestTypeForCategory(form.getCategory()));
        if (!categoryBloodTestTypes.contains(form.getBloodTestType())) {
          errors.rejectValue("bloodTestType", "bloodTestType.InconsistentWithCategory", "Blood test type is not applicable to current category");
        }
      }
    }
    
    
  }

  @Override
  public String getFormName() {
    return "BloodTests";
  }  
  
}