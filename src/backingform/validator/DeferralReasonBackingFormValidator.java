package backingform.validator;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import model.donordeferral.DeferralReason;
import repository.DeferralReasonRepository;
import viewmodel.DeferralReasonViewModel;
import backingform.DeferralReasonBackingForm;
import controller.UtilController;

public class DeferralReasonBackingFormValidator implements Validator {

    private Validator validator;
    private UtilController utilController;
    private DeferralReasonRepository deferralReasonRepository;

    public DeferralReasonBackingFormValidator(Validator validator, UtilController utilController,DeferralReasonRepository deferralReasonRepository) {
        super();
        this.validator = validator;
        this.utilController = utilController;
        this.deferralReasonRepository=deferralReasonRepository;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean supports(Class<?> clazz) {
        return Arrays.asList(DeferralReasonBackingForm.class, DeferralReason.class, DeferralReasonViewModel.class).contains(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        if (obj == null || validator == null)
            return;

        ValidationUtils.invokeValidator(validator, obj, errors);
        DeferralReasonBackingForm form = (DeferralReasonBackingForm) obj;

        if (utilController.isDuplicateDeferralReason(form.getDeferralReason())){
            errors.rejectValue("reason", "400",
                    "Deferral Reason already exists.");
        }


    }
}