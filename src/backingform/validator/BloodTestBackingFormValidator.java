package backingform.validator;

import backingform.BloodTestBackingForm;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


import java.util.Arrays;

public class BloodTestBackingFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Arrays.asList(BloodTestBackingForm.class).contains(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        errors.rejectValue("BloodTest.testName", "testName.empty", "The testName is required");
        errors.rejectValue("BloodTest.category", "category.empty", "The bloodTestCategory is required");
        errors.rejectValue("BloodTest.validResults", "validResults.empty", "The validResults is required");
        errors.rejectValue("BloodTest.testNameShort", "testNameShort.empty", "The testNameShort is required");
        errors.rejectValue("BloodTest.rankInCategory", "rankInCategory.empty", "The rankInCategory is required");
    }
}
