package backingform.validator;

import backingform.BloodTestBackingForm;
import controller.UtilController;
import model.bloodtesting.BloodTest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import viewmodel.BloodTestViewModel;

import java.util.Arrays;

public class BloodTestBackingFormValidator implements Validator {

    private Validator validator;
    private UtilController utilController;


    public BloodTestBackingFormValidator(Validator validator, UtilController utilController){
        super();
        this.validator = validator;
        this.utilController = utilController;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Arrays.asList(BloodTestBackingForm.class, BloodTest.class, String.class, Integer.class, Boolean.class, BloodTestViewModel.class).contains(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.invokeValidator(validator, target, errors);
        BloodTestBackingForm form = (BloodTestBackingForm) target;

        ValidationUtils.rejectIfEmpty(errors, "BloodTest.testName", "testName.empty", "The testName is required");
        ValidationUtils.rejectIfEmpty(errors, "BloodTest.category", "category.empty", "The bloodTestCategory is required");
        ValidationUtils.rejectIfEmpty(errors, "BloodTest.validResults", "validResults.empty", "The validResults is required");
//        ValidationUtils.rejectIfEmpty(errors, "BloodTest.positiveResults", "positiveResults.empty", "The positiveResults is required");
//        ValidationUtils.rejectIfEmpty(errors, "BloodTest.negativeResults", "negativeResults.empty", "The negativeResults is required");
        ValidationUtils.rejectIfEmpty(errors, "BloodTest.testNameShort", "testNameShort.empty", "The testNameShort is required");
        ValidationUtils.rejectIfEmpty(errors, "BloodTest.rankInCategory", "rankInCategory.empty", "The rankInCategory is required");
    }
}
