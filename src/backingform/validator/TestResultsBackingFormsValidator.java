package backingform.validator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.TestResultsBackingForm;
import backingform.TestResultsBackingForms;
import model.bloodtesting.BloodTest;
import repository.bloodtesting.BloodTestingRepository;



@Component
public class TestResultsBackingFormsValidator extends BaseValidator<TestResultsBackingForms> {

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

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
    for (BloodTest bloodTypingTest : bloodTestingRepository.findActiveBloodTests()) {
      activeBloodTestsMap.put(bloodTypingTest.getId().toString(), bloodTypingTest);
    }

    for (Long testId : form.getTestResults().keySet()) {

      BloodTest activeBloodTest = activeBloodTestsMap.get(testId.toString());

      if (activeBloodTest == null) {
        // No active test was found for the provided id
        errors.rejectValue("testOutcomesForDonations[" + index + "].testResults", "invalid", "Invalid test");
        return;
      }

      String result = form.getTestResults().get(testId);

      if (!activeBloodTest.getIsEmptyAllowed() && StringUtils.isBlank(result)) {
        // Empty results are not allowed for this test and the provided result is empty
        errors.rejectValue("testOutcomesForDonations[" + index + "].testResults", "required", "No value specified");
        return;
      }

      if (!activeBloodTest.getValidResultsList().contains(result)) {
        // The provided result is not in the list of valid results
        errors.rejectValue("testOutcomesForDonations[" + index + "].testResults", "invalid", "Invalid value specified");
      }
    }
  }
}