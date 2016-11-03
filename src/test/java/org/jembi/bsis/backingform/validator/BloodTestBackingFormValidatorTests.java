package org.jembi.bsis.backingform.validator;

import static org.jembi.bsis.helpers.builders.BloodTestBackingFormBuilder.aBloodTestBackingForm;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;

import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

public class BloodTestBackingFormValidatorTests extends UnitTestSuite {

  @InjectMocks
  private BloodTestBackingFormValidator bloodTestBackingFormvalidator;
  
  @Mock
  private BloodTestRepository bloodTestRepository;

  @Mock
  private FormFieldRepository formFieldRepository;

  @Test
  public void testValidateValidForm_shouldHaveNoErrors() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "NT"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG"));
    BloodTestBackingForm backingForm = aBloodTestBackingForm()
        .withTestName("aBasicBloodTypingTest")
        .withTestNameShort("basicBloodTypingShort")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .withValidResults(validResults)
        .withNegativeResults(negativeResults)
        .withPositiveResults(positiveResults)
        .build();
    
    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);
    
    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("No error", 0, errors.getErrorCount());
  }
  
  @Test
  public void testValidateFormWithNoBloodTestName_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "NT"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG"));
    BloodTestBackingForm backingForm = aBloodTestBackingForm()
        .withTestNameShort("basicBloodTypingShort")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .withValidResults(validResults)
        .withNegativeResults(negativeResults)
        .withPositiveResults(positiveResults)
        .build();

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("One Error", 1, errors.getErrorCount());
    Assert.assertEquals("errors.required", errors.getFieldError("testName").getCode());
  }
  
  @Test
  public void testValidateFormWithTooLongBloodTestName_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "NT"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG"));
    BloodTestBackingForm backingForm = aBloodTestBackingForm()
        .withTestName("aBasicBloodTypingTestWithANameThatIsMoreThan40Characters")
        .withTestNameShort("basicBloodTypingShort")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .withValidResults(validResults)
        .withNegativeResults(negativeResults)
        .withPositiveResults(positiveResults)
        .build();

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("One Error", 1, errors.getErrorCount());
    Assert.assertEquals("errors.fieldLength", errors.getFieldError("testName").getCode());
  }
  
  @Test
  public void testValidateFormWithNoBloodTestShortName_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "NT"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG"));
    BloodTestBackingForm backingForm = aBloodTestBackingForm()
        .withTestName("basicBloodTypingShort")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .withValidResults(validResults)
        .withNegativeResults(negativeResults)
        .withPositiveResults(positiveResults)
        .build();
    
    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);
    
    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("One Error", 1, errors.getErrorCount());
    Assert.assertEquals("errors.required", errors.getFieldError("testNameShort").getCode());
  }

  @Test
  public void testValidateFormWithTooLongBloodTestNameShort_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "NT"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG"));
    BloodTestBackingForm backingForm =
        aBloodTestBackingForm()
            .withTestName("aBasicBloodTestName")
            .withTestNameShort("basicBloodTypingShortWithANameThatIsMoreThan25Characters")
            .withCategory(BloodTestCategory.BLOODTYPING)
            .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
            .withValidResults(validResults)
            .withNegativeResults(negativeResults)
            .withPositiveResults(positiveResults)
            .build();

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("One Error", 1, errors.getErrorCount());
    Assert.assertEquals("errors.fieldLength", errors.getFieldError("testNameShort").getCode());
  }

  @Test
  public void testValidateFormWithBloodTestNameThatIsNotUnique_shouldHaveNoErrors() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "NT"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG"));
    BloodTestBackingForm backingForm = aBloodTestBackingForm()
        .withTestName("aBasicBloodTypingTest")
        .withTestNameShort("basicBloodTypingShort")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .withValidResults(validResults)
        .withNegativeResults(negativeResults)
        .withPositiveResults(positiveResults)
        .build();

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(false);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("One Error", 1, errors.getErrorCount());
    Assert.assertEquals("errors.nonUnique", errors.getFieldError("testName").getCode());
  }

  @Test
  public void testValidateFormWithNoValidOutcomes_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>();
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>();
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>();
    BloodTestBackingForm backingForm =
        aBloodTestBackingForm()
            .withTestName("aBasicBloodTestName")
            .withTestNameShort("bloodTypingShort")
            .withCategory(BloodTestCategory.BLOODTYPING)
            .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
            .withValidResults(validResults)
            .withNegativeResults(negativeResults)
            .withPositiveResults(positiveResults)
            .build();

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("One Error", 1, errors.getErrorCount());
    Assert.assertEquals("errors.required", errors.getFieldError("validOutcomes").getCode());
  }
  
  @Test
  public void testValidateFormWithInvalidPositiveOutcomes_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "NT"));
    LinkedHashSet<String> positiveResults =
        new LinkedHashSet<String>(Arrays.asList("POS", "INVALID OUTCOME", "INVALID OUTCOME 2"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG"));
    BloodTestBackingForm backingForm =
        aBloodTestBackingForm()
            .withTestName("aBasicBloodTestName")
            .withTestNameShort("bloodTypingShort")
            .withCategory(BloodTestCategory.BLOODTYPING)
            .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
            .withValidResults(validResults)
            .withNegativeResults(negativeResults)
            .withPositiveResults(positiveResults)
            .build();

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("One Error", 1, errors.getErrorCount());
    Assert.assertEquals("errors.positiveOutcomesNotInValidOutcomes",
        errors.getFieldError("positiveOutcomes").getCode());
  }
  
  @Test
  public void testValidateFormWithInvalidNegativeOutcomes_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "NT"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS"));
    LinkedHashSet<String> negativeResults =
        new LinkedHashSet<String>(Arrays.asList("NEG", "INVALID OUTCOME", "INVALID OUTCOME 2"));
    BloodTestBackingForm backingForm =
        aBloodTestBackingForm()
            .withTestName("aBasicBloodTestName")
            .withTestNameShort("bloodTypingShort")
            .withCategory(BloodTestCategory.BLOODTYPING)
            .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
            .withValidResults(validResults)
            .withNegativeResults(negativeResults)
            .withPositiveResults(positiveResults)
            .build();

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("One Error", 1, errors.getErrorCount());
    Assert.assertEquals("errors.negativeOutcomesNotInValidOutcomes",
        errors.getFieldError("negativeOutcomes").getCode());
  }

  @Test
  public void testValidateFormWithInvalidNegativeAndPositiveOutcomes_shouldHaveTwoErrors() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "NT"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS", "INVALID OUTCOME"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG", "INVALID OUTCOME 2"));
    BloodTestBackingForm backingForm = aBloodTestBackingForm().withTestName("aBasicBloodTestName")
        .withTestNameShort("bloodTypingShort").withCategory(BloodTestCategory.BLOODTYPING)
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING).withValidResults(validResults)
        .withNegativeResults(negativeResults).withPositiveResults(positiveResults).build();

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("Two Errors", 2, errors.getErrorCount());
    Assert.assertEquals("errors.negativeOutcomesNotInValidOutcomes",
        errors.getFieldError("negativeOutcomes").getCode());
    Assert.assertEquals("errors.positiveOutcomesNotInValidOutcomes",
        errors.getFieldError("positiveOutcomes").getCode());
  }
  
  @Test
  public void testValidateFormWithOutcomeInPositiveAndNegativeOutcomeList_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "OUTCOMEINBOTHLISTS"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS", "OUTCOMEINBOTHLISTS"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG", "OUTCOMEINBOTHLISTS"));
    BloodTestBackingForm backingForm =
        aBloodTestBackingForm()
            .withTestName("aBasicBloodTestName")
            .withTestNameShort("bloodTypingShort")
            .withCategory(BloodTestCategory.BLOODTYPING)
            .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
            .withValidResults(validResults)
            .withNegativeResults(negativeResults)
            .withPositiveResults(positiveResults)
            .build();

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("One Error", 1, errors.getErrorCount());
    Assert.assertEquals("errors.positiveOutcomesAlsoInNegativeOutcomes",
        errors.getFieldError("positiveOutcomesInNegativeOutcomes").getCode());
  }
  
  @Test
  public void testValidateFormWithNoBloodTestCategory_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "NT"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG"));
    BloodTestBackingForm backingForm = aBloodTestBackingForm()
        .withTestName("aBasicBloodTestName")
        .withTestNameShort("basicBloodNameShort")
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .withValidResults(validResults)
        .withNegativeResults(negativeResults)
        .withPositiveResults(positiveResults)
        .build();

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("One Error", 1, errors.getErrorCount());
    Assert.assertEquals("errors.required", errors.getFieldError("bloodTestCategory").getCode());
  }
  
  @Test
  public void testValidateFormWithNoBloodTestType_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "NT"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG"));
    BloodTestBackingForm backingForm = aBloodTestBackingForm()
        .withTestName("aBasicBloodTestName")
        .withTestNameShort("basicBloodNameShort")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withValidResults(validResults)
        .withNegativeResults(negativeResults)
        .withPositiveResults(positiveResults)
        .build();

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("One Error", 1, errors.getErrorCount());
    Assert.assertEquals("errors.required", errors.getFieldError("bloodTestType").getCode());
  }
  
  @Test
  public void testValidateFormWithBloodTestTypeWithInvalidBloodTestCategory_shouldHaveOneError() {

    // Set up data
    LinkedHashSet<String> validResults = new LinkedHashSet<String>(Arrays.asList("POS", "NEG", "NT"));
    LinkedHashSet<String> positiveResults = new LinkedHashSet<String>(Arrays.asList("POS"));
    LinkedHashSet<String> negativeResults = new LinkedHashSet<String>(Arrays.asList("NEG"));
    BloodTestBackingForm backingForm = aBloodTestBackingForm()
        .withTestName("aBasicBloodTestName")
        .withTestNameShort("basicBloodNameShort")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withBloodTestType(BloodTestType.BASIC_TTI)
        .withValidResults(validResults)
        .withNegativeResults(negativeResults)
        .withPositiveResults(positiveResults)
        .build();

    // Set up mocks
    when(bloodTestRepository.isUniqueTestName(null, backingForm.getTestName())).thenReturn(true);

    // Run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "BloodTest");
    bloodTestBackingFormvalidator.validateForm(backingForm, errors);

    // Verify
    Assert.assertEquals("One Error", 1, errors.getErrorCount());
    Assert.assertEquals("errors.bloodTestTypeInconsistentWithCategory",
        errors.getFieldError("bloodTestType").getCode());
  }
}
