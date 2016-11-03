package org.jembi.bsis.backingform.validator;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.repository.BloodTestRepository;
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
    } else if (form.getTestName().length() > MAX_LENGTH_TEST_NAME_SHORT) {
      errors.rejectValue("testNameShort", "errors.fieldLength",
          "Maximum length for this field is " + MAX_LENGTH_TEST_NAME_SHORT);
    }
    
    // Validate validOutcomes convert list to set for faster search
    Set<String> validOutcomes = new HashSet<>(form.getValidResults());
    if (validOutcomes.isEmpty()) {
      errors.rejectValue("validResults", "errors.required", "Valid outcomes are required");
    } else {
      // validate that all positive outcomes are inside valid outcomes
      String errorPositiveOutComes = "";
      boolean postitiveOutcomesHasError = false;
      for (String positiveOutcome : form.getPositiveResults()) {
        if (!validOutcomes.contains(positiveOutcome)) {
          errorPositiveOutComes += positiveOutcome +","; 
          postitiveOutcomesHasError = true;
        }
      }
      if (postitiveOutcomesHasError) {
        errors.rejectValue("positiveResults", "positiveResults.notInValidOutcomes",  "positive results: [" + errorPositiveOutComes.substring(0, errorPositiveOutComes.length() -1) + "] is not listed as a validOutcome.");
      }
      // validate that all negative outcomes are inside valid outcomes  
      String errorNegativeOutComes = "";
      boolean negativeOutcomesHasError = false;
      for (String negativeOutcome : form.getPositiveResults()) {
        if (!validOutcomes.contains(negativeOutcome)) {
          errorPositiveOutComes += negativeOutcome +","; 
          negativeOutcomesHasError = true;
        }
      }
      if (negativeOutcomesHasError) {
        errors.rejectValue("negativeResults", "negativeResults.notInValidOutcomes",  "negative results: [" + errorNegativeOutComes.substring(0, errorNegativeOutComes.length() -1) + "] is not listed as a validOutcome.");
      }
    }
    
    // validate that positive outcomes are not in negative outcomes
    Set<String> negativeOutcomes = new HashSet<>(form.getNegativeResults());
    Set<String> positiveOutcomes = new HashSet<>(form.getPositiveResults());
    String errorPositiveOutcomesInNegativeOutComes = "";
    boolean positiveOutComesInNegativeOutcomes = false;
    for (String positiveOutcome : positiveOutcomes) {
      if (negativeOutcomes.contains(positiveOutcome)) {
        errorPositiveOutcomesInNegativeOutComes += positiveOutcome +","; 
        positiveOutComesInNegativeOutcomes = true;
      }
    }
    if (positiveOutComesInNegativeOutcomes) {
      errors.rejectValue("positiveResults", "positiveResults.inNegativeResults",  "positive results: [" + errorPositiveOutcomesInNegativeOutComes.substring(0, errorPositiveOutcomesInNegativeOutComes.length() -1) + "] is also listed in negative results.");
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
    return "BloodTestBackingForm";
  }  
}