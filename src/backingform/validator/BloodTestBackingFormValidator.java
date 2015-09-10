package backingform.validator;

import backingform.BloodTestBackingForm;

import org.apache.commons.lang3.StringUtils;
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
        BloodTestBackingForm form = (BloodTestBackingForm) target;
        if (StringUtils.isBlank(form.getTestName()))
            errors.rejectValue("BloodTest.testName", "testName.empty", "The testName is required");

        if (form.getBloodTestCategory() == null)
            errors.rejectValue("BloodTest.category", "category.empty", "The bloodTestCategory is required");

        if (StringUtils.isBlank(form.getValidResults()))
            errors.rejectValue("BloodTest.validResults", "validResults.empty", "The validResults is required");

        if (StringUtils.isBlank(form.getTestNameShort()))
            errors.rejectValue("BloodTest.testNameShort", "testNameShort.empty", "The testNameShort is required");

        if (form.getRankInCategory() == null)
            errors.rejectValue("BloodTest.rankInCategory", "rankInCategory.empty", "The rankInCategory is required");

    }
}
