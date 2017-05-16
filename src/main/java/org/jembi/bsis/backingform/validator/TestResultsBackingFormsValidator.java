package org.jembi.bsis.backingform.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.backingform.TestResultsBackingForm;
import org.jembi.bsis.backingform.TestResultsBackingForms;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.repository.BloodTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;



@Component
public class TestResultsBackingFormsValidator extends BaseValidator<TestResultsBackingForms> {

  @Autowired
  private BloodTestRepository bloodTestRepository;

  @Override
  public void validateForm(TestResultsBackingForms forms, Errors errors) {
    for (TestResultsBackingForm form : forms.getTestOutcomesForDonations()) {
      validate(form, errors, forms.getTestOutcomesForDonations().indexOf(form));
    }
  }

  @Override
  public String getFormName() {
    return "testOutcomesForDonations";
  }

  private void validate(TestResultsBackingForm form, Errors errors, int index) {
    /**
     * Build a map of active blood test ids to the active blood tests.
     */
    Map<String, BloodTest> activeBloodTestsMap = new HashMap<>();
    for (BloodTest bloodTypingTest : bloodTestRepository.getBloodTests(false, false)) {
      activeBloodTestsMap.put(bloodTypingTest.getId().toString(), bloodTypingTest);
    }

    for (UUID testId : form.getTestResults().keySet()) {

      BloodTest activeBloodTest = activeBloodTestsMap.get(testId.toString());

      if (activeBloodTest == null) {
        // No active test was found for the provided id
        errors.rejectValue("testOutcomesForDonations[" + index + "].testResults", "invalid", "Invalid test");
        return;
      }

      String result = form.getTestResults().get(testId);

      if (!activeBloodTest.getValidResultsSet().contains(result)) {
        // The provided result is not in the list of valid results
        errors.rejectValue("testOutcomesForDonations[" + index + "].testResults", "invalid", "Invalid value specified");
      }
    }
  }
}
