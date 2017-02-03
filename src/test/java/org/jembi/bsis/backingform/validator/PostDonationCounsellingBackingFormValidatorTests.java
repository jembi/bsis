package org.jembi.bsis.backingform.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.helpers.builders.PostDonationCounsellingBackingFormBuilder;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class PostDonationCounsellingBackingFormValidatorTests extends UnitTestSuite {
  
  @InjectMocks
  private PostDonationCounsellingBackingFormValidator validator;
  
  @Test
  public void testValidateValidForm_shouldntGetErrors() throws ParseException {
    // Set up data
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(fmt.parse("01/02/2017"))
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsNotFlaggedForCounselling()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(0));
  }
  
  @Test
  public void testValidateInValidForm_shouldntReturnTwoErrors() {
    // Set up data
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(null)
        .withCounsellingStatus(null)
        .thatIsNotFlaggedForCounselling()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(2));
    assertThat(errors.getFieldError("counsellingDate").getCode(), is("errors.required"));
    assertThat(errors.getFieldError("counsellingStatus").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateInValidForm_shouldntReturnOneError() {
    // Set up data
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(null)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsFlaggedForCounselling()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("flaggedForCounselling").getCode(), is("errors.invalid"));
  }
  
}