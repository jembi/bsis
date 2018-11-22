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
  private static final  Integer MAX_LENGTH_VALID_RESULTS = 10;

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

    // Validate testNameShort
    if (StringUtils.isBlank(form.getTestNameShort())) {
      errors.rejectValue("testNameShort", "errors.required", "Test name short is required");
    } else if (form.getTestNameShort().length() > MAX_LENGTH_TEST_NAME_SHORT) {
      errors.rejectValue("testNameShort", "errors.fieldLength",
          "Maximum length for this field is " + MAX_LENGTH_TEST_NAME_SHORT);
    }

    // Validate Results (also called Outcomes)
    Set<String> validResults = form.getValidResults();
    Set<String> negativeResults = form.getNegativeResults();
    Set<String> positiveResults = form.getPositiveResults();
    if (validResults == null || validResults.isEmpty()) {
      errors.rejectValue("validResults", "errors.required", "Valid outcomes are required");
    } else {
      for (String validResult : form.getValidResults()) {
        if (validResult.length() > MAX_LENGTH_VALID_RESULTS) {
          errors.rejectValue("validResults", "errors.validResultsLong",
              "Valid results entry : ["+validResult+"] is too long. Maximum length for this field is " + MAX_LENGTH_VALID_RESULTS);
        }
      }
      if (positiveResults != null) {
        // validate that all positive results are inside valid results.
        String errorPositiveResults = null;
        for (String positiveResult : positiveResults) {
          if (!validResults.contains(positiveResult)) {
            if (errorPositiveResults == null) {
              errorPositiveResults = positiveResult;
            } else {
              errorPositiveResults += ", " + positiveResult;
            }
          }
        }
        if (errorPositiveResults != null) {
          // Note that the results are called Outcomes in the front-end.
          errors.rejectValue("positiveResults", "errors.positiveOutcomesNotInValidOutcomes",
              "Positive outcome(s): [" + errorPositiveResults + "] not present in list of valid outcomes.");
        }
      }
      if (negativeResults != null) {
        // validate that all negative outcomes are inside valid outcomes
        String errorNegativeResults = null;
        for (String negativeResult : negativeResults) {
          if (!validResults.contains(negativeResult)) {
            if (errorNegativeResults == null) {
              errorNegativeResults = negativeResult;
            } else {
              errorNegativeResults += ", " + negativeResult;
            }
          }
        }
        if (errorNegativeResults != null) {
          errors.rejectValue("negativeResults", "errors.negativeOutcomesNotInValidOutcomes",
              "Negative outcome(s): [" + errorNegativeResults + "] not present in list of valid outcomes.");
        }
      }
    }

    // validate that positive results are not in negative results
    if (positiveResults != null && negativeResults != null) {
      String errorPositiveResultsInNegativeResults = null;
      for (String positiveResult : positiveResults) {
        if (negativeResults.contains(positiveResult)) {
          if (errorPositiveResultsInNegativeResults == null) {
            errorPositiveResultsInNegativeResults = positiveResult;
          } else {
            errorPositiveResultsInNegativeResults += ", " + positiveResult;
          }
        }
      }

      if (errorPositiveResultsInNegativeResults != null) {
        // Note that the results are called Outcomes in the front-end.
        errors.rejectValue("positiveResults", "errors.positiveOutcomesAlsoInNegativeOutcomes",
            "The following outcome(s): [" + errorPositiveResultsInNegativeResults
                + "] appear in both the Negative and Positive list of outcomes.");
      }
    }

    // Validate category
    if (form.getCategory() == null) {
      errors.rejectValue("category", "errors.required", "Blood Test Category is required");
    }

    // Validate bloodTestType
    if (form.getBloodTestType() == null) {
      errors.rejectValue("bloodTestType", "errors.required", "Blood test type is required");
    }

    // Validate that blood test and category are related
    if (form.getCategory() != null && form.getBloodTestType() != null) {
      // check that blood test category and blood test type are matched
      Set<BloodTestType> categoryBloodTestTypes =
          new HashSet<>(BloodTestType.getBloodTestTypeForCategory(form.getCategory()));
      if (!categoryBloodTestTypes.contains(form.getBloodTestType())) {
        errors.rejectValue("bloodTestType", "errors.bloodTestTypeInconsistentWithCategory",
            "Blood test type is not applicable to current category");
      }
    }

    // Validate isDeleted
    if (form.getIsDeleted() == null) {
      errors.rejectValue("isDeleted", "errors.required", "isDeleted is required");
    }

    // Validate isActive
    if (form.getIsActive() == null) {
      errors.rejectValue("isActive", "errors.required", "isActive is required");
    }

    // Validate flagComponentsForDiscard
    if (form.getFlagComponentsForDiscard() == null) {
      errors.rejectValue("flagComponentsForDiscard", "errors.required", "flagComponentsForDiscard is required");
    }

    // Validate flagComponentsContainingPlasmaForDiscard
    if (form.getFlagComponentsContainingPlasmaForDiscard() == null) {
      errors.rejectValue("flagComponentsContainingPlasmaForDiscard", "errors.required",
          "flagComponentsContainingPlasmaForDiscard is required");
    }
    
    // Validate rankInCategory
    if (form.getRankInCategory() !=null && form.getRankInCategory() < 1) {
      errors.rejectValue("RankInCategory", "errors.invalid", "Rank in Category is invalid");
    }

  }

  @Override
  public String getFormName() {
    return "BloodTestBackingForm";
  }
}