package validator;

import static helpers.builders.DeferralReasonBackingFormBuilder.aDeferralReasonBackingForm;
import static helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.List;

import model.donordeferral.DeferralReason;
import model.donordeferral.DurationType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import backingform.DeferralReasonBackingForm;
import backingform.validator.DeferralReasonBackingFormValidator;
import controller.UtilController;

@RunWith(MockitoJUnitRunner.class)
public class DeferralReasonBackingFormValidatorTests {
    
    private DeferralReasonBackingFormValidator validator;
    @Mock
    private UtilController utilController;
    
    @Before
    public void setUpFixture() {
        validator = new DeferralReasonBackingFormValidator(utilController);
    }
    
    @Test
    public void testValidateWithDeferralReasonBackingFormWithDuplicateDeferralReason_shouldHaveErrors() {
        DeferralReason deferralReason = aDeferralReason().build();
        DeferralReasonBackingForm backingForm = aDeferralReasonBackingForm()
                .withDurationType(DurationType.TEMPORARY)
                .withDeferralReason(deferralReason)
                .withDefaultDuration(1)
                .build();
        
        when(utilController.isDuplicateDeferralReason(deferralReason)).thenReturn(true);

        Errors errors = new BindException(backingForm, "deferralReasonBackingForm");
        validator.validate(backingForm, errors);
        
        assertThat(errors.getErrorCount(), is(1));
        List<FieldError> fieldErrors = errors.getFieldErrors("reason");
        assertThat(fieldErrors.size(), is(1));
        assertThat(fieldErrors.get(0).getDefaultMessage(), is("Deferral Reason already exists."));
    }
    
    @Test
    public void testValidateWithDeferralReasonBackingFormWithPermanentDurationTypeAndNoDuration_shouldHaveNoErrors() {
        DeferralReason deferralReason = aDeferralReason().build();
        DeferralReasonBackingForm backingForm = aDeferralReasonBackingForm()
                .withDurationType(DurationType.PERMANENT)
                .withDeferralReason(deferralReason)
                .withDefaultDuration(0)
                .build();
        
        when(utilController.isDuplicateDeferralReason(deferralReason)).thenReturn(false);

        Errors errors = new BindException(backingForm, "deferralReasonBackingForm");
        validator.validate(backingForm, errors);
        
        assertThat(errors.getErrorCount(), is(0));
    }
    
    @Test
    public void testValidateWithDeferralReasonBackingFormWithInvalidDefaultDeferralDays_shouldHaveErrors() {
        DeferralReason deferralReason = aDeferralReason().build();
        DeferralReasonBackingForm backingForm = aDeferralReasonBackingForm()
                .withDurationType(DurationType.TEMPORARY)
                .withDeferralReason(deferralReason)
                .withDefaultDuration(0)
                .build();
        
        when(utilController.isDuplicateDeferralReason(deferralReason)).thenReturn(false);

        Errors errors = new BindException(backingForm, "deferralReasonBackingForm");
        validator.validate(backingForm, errors);
        
        assertThat(errors.getErrorCount(), is(1));
        List<FieldError> fieldErrors = errors.getFieldErrors("defaultDuration");
        assertThat(fieldErrors.size(), is(1));
        assertThat(fieldErrors.get(0).getDefaultMessage(), is("Default duration must be a positive number of days"));
    }
    
    @Test
    public void testValidateWithValidDeferralReasonBackingForm_shouldHaveNoErrors() {
        DeferralReason deferralReason = aDeferralReason().build();
        DeferralReasonBackingForm backingForm = aDeferralReasonBackingForm()
                .withDurationType(DurationType.TEMPORARY)
                .withDeferralReason(deferralReason)
                .withDefaultDuration(1)
                .build();
        
        when(utilController.isDuplicateDeferralReason(deferralReason)).thenReturn(false);

        Errors errors = new BindException(backingForm, "deferralReasonBackingForm");
        validator.validate(backingForm, errors);
        
        assertThat(errors.getErrorCount(), is(0));
    }

}
