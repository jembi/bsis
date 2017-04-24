package org.jembi.bsis.backingform.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBackingFormBuilder.aBloodTestBackingForm;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;

import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class BloodTestBackingFormValidatorTests extends UnitTestSuite {

  @InjectMocks
  private BloodTestBackingFormValidator bloodTestBackingFormValidator;
  
  @Mock
  private BloodTestRepository bloodTestRepository;
  
  private BloodTestBackingForm getBaseBloodTestBackingForm() {
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "NT"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Collections.singletonList("POS"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Collections.singletonList("NEG"));
    return aBloodTestBackingForm()
        .withTestName("aBasicBloodTypingTest")
        .withTestNameShort("basicBloodTypingShort")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .withValidResults(validResults)
        .withNegativeResults(negativeResults)
        .withPositiveResults(positiveResults)
        .thatIsActive()
        .thatIsNotDeleted()
        .thatShouldFlagComponentsForDiscard()
        .thatShouldNotFlagComponentsContainingPlasmaForDiscard()
        .build();
  }

  @Test
  public void testValidateValidForm_shouldHaveNoErrors() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    
    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);
    
    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(0));
  }
  
  @Test
  public void testValidateFormWithNoBloodTestName_shouldHaveOneError() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setTestName(null);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("testName").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateFormWithTooLongBloodTestName_shouldHaveOneError() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setTestName("aBasicBloodTypingTestWithANameThatIsMoreThan40Characters");

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("testName").getCode(), is("errors.fieldLength"));
  }
  
  @Test
  public void testValidateFormWithNoBloodTestShortName_shouldHaveOneError() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setTestNameShort(null);
    
    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);
    
    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("testNameShort").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithTooLongBloodTestNameShort_shouldHaveOneError() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setTestNameShort("basicBloodTypingShortWithANameThatIsMoreThan25Characters");

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("testNameShort").getCode(), is("errors.fieldLength"));
  }

  @Test
  public void testValidateFormWithBloodTestNameThatIsNotUnique_shouldHaveNoErrors() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(false);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("testName").getCode(), is("errors.nonUnique"));
  }

  @Test
  public void testValidateFormWithEmptyValidOutcomes_shouldHaveOneError() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setValidResults(new LinkedHashSet<String>());

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("validResults").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithNoValidOutcomes_shouldHaveOneError() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setValidResults(null);

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("validResults").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithValidOutcomeExceedingTenCharacters_shouldHaveOneError() {
    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.getValidResults().add("this is too long");

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("validResults").getCode(), is("errors.validResultsLong"));
  }
  
  @Test
  public void testValidateFormWithInvalidPositiveOutcomes_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> positiveResults =
        new LinkedHashSet<String>(Arrays.asList("POS", "INVALID OUTCOME", "INVALID OUTCOME 2"));
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setPositiveResults(positiveResults);

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("positiveResults").getCode(), is("errors.positiveOutcomesNotInValidOutcomes"));
  }
  
  @Test
  public void testValidateFormWithInvalidNegativeOutcomes_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> negativeResults =
        new LinkedHashSet<String>(Arrays.asList("NEG", "INVALID OUTCOME", "INVALID OUTCOME 2"));
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setNegativeResults(negativeResults);

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("negativeResults").getCode(), is("errors.negativeOutcomesNotInValidOutcomes"));
  }

  @Test
  public void testValidateFormWithInvalidNegativeAndPositiveOutcomes_shouldHaveTwoErrors() {

    // Set up data
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS", "INVALID OUTCOME"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG", "INVALID OUTCOME 2"));
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setPositiveResults(positiveResults);
    backingForm.setNegativeResults(negativeResults);

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(2));
    assertThat(errors.getFieldError("negativeResults").getCode(), is("errors.negativeOutcomesNotInValidOutcomes"));
    assertThat(errors.getFieldError("positiveResults").getCode(), is("errors.positiveOutcomesNotInValidOutcomes"));
  }
  
  @Test
  public void testValidateFormWithOutcomeInPositiveAndNegativeOutcomeList_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "INBOTH"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS", "INBOTH"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG", "INBOTH"));
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setValidResults(validResults);
    backingForm.setPositiveResults(positiveResults);
    backingForm.setNegativeResults(negativeResults);

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("positiveResults").getCode(), is("errors.positiveOutcomesAlsoInNegativeOutcomes"));
  }
  
  @Test
  public void testValidateFormWithNoCategory_shouldHaveOneError() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setCategory(null);

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("category").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateFormWithNoBloodTestType_shouldHaveOneError() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setBloodTestType(null);

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("bloodTestType").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateFormWithBloodTestTypeWithInvalidBloodTestCategory_shouldHaveOneError() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setCategory(BloodTestCategory.BLOODTYPING);
    backingForm.setBloodTestType(BloodTestType.BASIC_TTI);

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("bloodTestType").getCode(), is("errors.bloodTestTypeInconsistentWithCategory"));
  }

  @Test
  public void testValidateFormWithNoIsDeletedFlag_shouldHaveOneError() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setIsDeleted(null);

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("isDeleted").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithNoIsActiveFlag_shouldHaveOneError() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setIsActive(null);

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("isActive").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithNoFlagForDiscard_shouldHaveOneError() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setFlagComponentsForDiscard(null);

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("flagComponentsForDiscard").getCode(), is("errors.required"));
  }

  @Test
  public void testValidateFormWithNoFlagForDiscardIfPlasma_shouldHaveOneError() {

    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setFlagComponentsContainingPlasmaForDiscard(null);

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("flagComponentsContainingPlasmaForDiscard").getCode(), is("errors.required"));
  }
  
  @Test
  public void testValidateFormWithInvalidRankInCategory_shouldHaveOneError() {
    
    // Set up data
    BloodTestBackingForm backingForm = getBaseBloodTestBackingForm();
    backingForm.setRankInCategory(-1);

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormValidator.validateForm(backingForm, errors);

    // Verify
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError("RankInCategory").getCode(), is("errors.invalid"));
    
  }
}
