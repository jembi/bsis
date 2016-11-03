package org.jembi.bsis.backingform.validator;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.repository.bloodtesting.BloodTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class BloodTestBackingFormValidator extends BaseValidator<BloodTestBackingForm> {

  private static final Integer MAX_LENGTH_TEST_NAME = 40;
  private static final Integer MAX_LENGTH_TEST_NAME_SHORT = 25;
  
  @Autowired
  private BloodTestRepository bloodTestRepository;
  
  @Override
  public void validateForm(BloodTestBackingForm form, Errors errors) {
    
    // Validate testName
    if (StringUtils.isBlank(form.getTestName())) {
      errors.rejectValue("testName", "errors.required", "Test name is required");
    } else if (form.getTestName().length() > MAX_LENGTH_TEST_NAME) {
      errors.rejectValue("testName", "errors.fieldLength",
          "Maximum length for this field is " + MAX_LENGTH_TEST_NAME);
    } else {
      if (!bloodTestRepository.isUniqueTestName(form.getId(), form.getTestName())) {
        errors.rejectValue("testName", "errors.nonUnique", "Test name already exists");
      }
    }
    //  validate testNameShort
    if (StringUtils.isBlank(form.getTestNameShort())) {
      errors.rejectValue("testNameShort", "errors.required", "Test name short is required");
    } else if (form.getTestNameShort().length() > MAX_LENGTH_TEST_NAME_SHORT) {
      errors.rejectValue("testNameShort", "errors.fieldLength",
          "Maximum length for this field is " + MAX_LENGTH_TEST_NAME_SHORT);
    }
    
    // Validate validOutcomes convert list to set for faster search
    Set<String> validOutcomes = new HashSet<>(form.getValidResults());
    if (validOutcomes.isEmpty()) {
      errors.rejectValue("validOutcomes", "errors.required", "Valid outcomes are required");
    } else {
      // validate that all positive outcomes are inside valid outcomes
      String errorPositiveOutComes = null;
      for (String positiveOutcome : form.getPositiveResults()) {
        if (!validOutcomes.contains(positiveOutcome)) {
          if (errorPositiveOutComes == null) {
            errorPositiveOutComes = positiveOutcome;
          } else {
            errorPositiveOutComes += ", " + positiveOutcome;
          }
        }
      }
      if (errorPositiveOutComes != null) {
        errors.rejectValue("positiveOutcomes", "errors.positiveOutcomesNotInValidOutcomes",
            "Positive outcome(s): [" + errorPositiveOutComes + "] not present in list of valid outcomes.");
      }
      // validate that all negative outcomes are inside valid outcomes  
      String errorNegativeOutComes = null;
      for (String negativeOutcome : form.getNegativeResults()) {
        if (!validOutcomes.contains(negativeOutcome)) {
          if (errorNegativeOutComes == null) {
            errorNegativeOutComes = negativeOutcome;
          } else {
            errorNegativeOutComes += ", " + negativeOutcome;
          }
        }
      }
      if (errorNegativeOutComes != null) {
        errors.rejectValue("negativeOutcomes", "errors.negativeOutcomesNotInValidOutcomes",
            "Negative outcome(s): [" + errorNegativeOutComes + "] not present in list of valid outcomes.");
      }
    }
    
    // validate that positive outcomes are not in negative outcomes
    Set<String> negativeOutcomes = new HashSet<>(form.getNegativeResults());
    Set<String> positiveOutcomes = new HashSet<>(form.getPositiveResults());
    String errorPositiveOutcomesInNegativeOutComes = null;

    for (String positiveOutcome : positiveOutcomes) {
      if (negativeOutcomes.contains(positiveOutcome)) {
        if (errorPositiveOutcomesInNegativeOutComes == null) {
          errorPositiveOutcomesInNegativeOutComes = positiveOutcome;
        } else {
          errorPositiveOutcomesInNegativeOutComes += ", " + positiveOutcome;
        }
      }
    }

    if (errorPositiveOutcomesInNegativeOutComes != null) {
      errors.rejectValue("positiveOutcomesInNegativeOutcomes", "errors.positiveOutcomesAlsoInNegativeOutcomes",
          "The following outcome(s): [" + errorPositiveOutcomesInNegativeOutComes
              + "] appear in both the Negative and Positive list of outcomes.");
    }
    
    // validate blood test and category are related
    if (form.getCategory() == null) {
      errors.rejectValue("bloodTestCategory", "errors.required", "Blood Test Category is required");
    } else {
      if (form.getBloodTestType() == null) {
        errors.rejectValue("bloodTestType", "errors.required", "Blood test type is required");
      } else {
        // check that blood test category and blood test type are matched
        Set<BloodTestType> categoryBloodTestTypes = new HashSet<>(BloodTestType.getBloodTestTypeForCategory(form.getCategory()));
        if (!categoryBloodTestTypes.contains(form.getBloodTestType())) {
          errors.rejectValue("bloodTestType", "errors.bloodTestTypeInconsistentWithCategory",
              "Blood test type is not applicable to current category");
        }
      }
    }
  }

  @Override
  public String getFormName() {
    return "BloodTestBackingForm";
  }  
}