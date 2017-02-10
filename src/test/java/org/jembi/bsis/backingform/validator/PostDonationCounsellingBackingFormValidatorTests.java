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
  public void testValidateValidFormThatIsFlaggedForCounselling_shouldntGetErrors() {
    // Set up data
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(null)
        .withCounsellingStatus(null)
        .thatIsFlaggedForCounselling()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(0));
  }
  
  
  @Test
  public void testValidateValidFormThatIsNotFlaggedForCounselling_shouldntGetErrors() throws ParseException {
    // Set up data
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(fmt.parse("01/02/2017"))
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsNotFlaggedForCounselling()
        .thatIsNotReferred()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(0));
  }
  
  @Test
  public void testValidateInValidFormThatIsNotFlaggedForCounselling_shouldReturnTwoErrors() {
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
  public void testValidateInValidFormThatIsNotFlaggedForCounsellingWithNullCounsellingDate_shouldReturnOneError() {
    // Set up data
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(null)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsNotFlaggedForCounselling()
        .thatIsNotReferred()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("counsellingDate").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateInValidFormThatIsNotFlaggedForCounsellingWithNullCounsellingStatus_shouldReturnOneError() throws ParseException {
    // Set up data
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(fmt.parse("05/02/2017"))
        .withCounsellingStatus(null)
        .thatIsNotFlaggedForCounselling()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("counsellingStatus").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateInValidFormThatIsFlaggedForCounsellingWithNotNullCounsellingStatus_shouldReturnOneError() {
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
    assertThat(errors.getFieldError("counsellingStatus").getCode(), is("errors.invalid"));
  }
  
  @Test
  public void testValidateInValidFormThatIsFlaggedForCounsellingWithNotNullCounsellingDate_shouldReturnOneError() throws ParseException {
    // Set up data
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(fmt.parse("01/02/2017"))
        .withCounsellingStatus(null)
        .thatIsFlaggedForCounselling()
        .build();
    
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");
    
    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("counsellingDate").getCode(), is("errors.invalid"));
  }  

  @Test
  public void testValidateInvalidFormWitStatusReceivedCounseillingAndReferredIsEmpty_shouldReturnOneError() throws ParseException {
    // Set up data
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    PostDonationCounsellingBackingForm form = PostDonationCounsellingBackingFormBuilder
        .aPostDonationCounsellingBackingForm()
        .withCounsellingDate(fmt.parse("01/02/2017"))
        .thatIsNotFlaggedForCounselling()
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .build();

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "postDonationCounselling");

    // Run test
    validator.validateForm(form, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("referred").getCode(), is("errors.invalid"));
    assertThat(errors.getFieldError("referred").getDefaultMessage(), is("Referred is required"));
  }
}