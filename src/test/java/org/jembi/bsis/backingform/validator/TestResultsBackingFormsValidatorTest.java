package org.jembi.bsis.backingform.validator;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jembi.bsis.backingform.TestResultsBackingForm;
import org.jembi.bsis.backingform.TestResultsBackingForms;
import org.jembi.bsis.helpers.builders.BloodTestBuilder;
import org.jembi.bsis.helpers.builders.TestResultsBackingFormBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.repository.bloodtesting.BloodTestRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(MockitoJUnitRunner.class)
public class TestResultsBackingFormsValidatorTest {
  
  @InjectMocks
  TestResultsBackingFormsValidator testResultsBackingFormsValidator;

  @Mock
  EntityManager entityManager;

  @Mock
  TypedQuery typedQuery;

  @Mock
  BloodTestRepository bloodTestRepository;

  @Test
  public void testValidateTestResultValuesValidResult() throws Exception {
    // set up data
    Map<Long, String> bloodTypingTestResults = new HashMap<>();
    bloodTypingTestResults.put(1l, "A"); // invalid result
    bloodTypingTestResults.put(2l, "POS"); // invalid result

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(1l).withValidResults("A,B,AB,O").withIsEmptyAllowed(true).build());
    tests.add(BloodTestBuilder.aBloodTest().withId(2l).withValidResults("POS,NEG").withIsEmptyAllowed(true).build());

    TestResultsBackingForm testResultsBackingForm = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber("1111111")
        .withTestResults(bloodTypingTestResults)
        .build();
    TestResultsBackingForms testResultsBackingForms = new TestResultsBackingForms();
    ArrayList<TestResultsBackingForm> testOutcomesForDonations = new ArrayList<>();
    testOutcomesForDonations.add(testResultsBackingForm);
    testResultsBackingForms.setTestOutcomesForDonations(testOutcomesForDonations);

    // set up mocks
    when(bloodTestRepository.getBloodTests(false, false)).thenReturn(tests);
    when(entityManager.createQuery("SELECT b FROM BloodTest b WHERE b.isActive = :isActive ", BloodTest.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("isActive", true)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(tests);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "testOutcomesForDonations");
    testResultsBackingFormsValidator.validateForm(testResultsBackingForms, errors);

    // do asserts
    Assert.assertEquals("errors found", 0, errors.getErrorCount());
  }

  @Test
  public void testValidateTestResultValuesInvalidResult() throws Exception {
    // set up data
    Map<Long, String> bloodTypingTestResults = new HashMap<>();
    bloodTypingTestResults.put(1l, "G"); // invalid result
    bloodTypingTestResults.put(2l, "FALSE"); // invalid result

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(1l).withValidResults("A,B,AB,O").withIsEmptyAllowed(true).build());
    tests.add(BloodTestBuilder.aBloodTest().withId(2l).withValidResults("POS,NEG").withIsEmptyAllowed(true).build());

    TestResultsBackingForm testResultsBackingForm = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber("1111111")
        .withTestResults(bloodTypingTestResults)
        .build();
    TestResultsBackingForms testResultsBackingForms = new TestResultsBackingForms();
    ArrayList<TestResultsBackingForm> testOutcomesForDonations = new ArrayList<>();
    testOutcomesForDonations.add(testResultsBackingForm);
    testResultsBackingForms.setTestOutcomesForDonations(testOutcomesForDonations);

    // set up mocks
    when(bloodTestRepository.getBloodTests(false, false)).thenReturn(tests);
    when(entityManager.createQuery("SELECT b FROM BloodTest b WHERE b.isActive = :isActive ", BloodTest.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("isActive", true)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(tests);

    Errors errors = new MapBindingResult(new HashMap<String, String>(), "testOutcomesForDonations");
    testResultsBackingFormsValidator.validateForm(testResultsBackingForms, errors);

    // do asserts
    Assert.assertEquals("errors found", 2, errors.getErrorCount());
    Assert.assertEquals("error message correct", "Invalid value specified",
        errors.getAllErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidateTestResultValuesNoTestResults() throws Exception {
    // set up data
    Map<Long, String> bloodTypingTestResults = new HashMap<>();

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(1l).withValidResults("A,B,AB,O").withIsEmptyAllowed(true).build());
    tests.add(BloodTestBuilder.aBloodTest().withId(2l).withValidResults("POS,NEG").withIsEmptyAllowed(true).build());

    TestResultsBackingForm testResultsBackingForm =
        TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber("1111111")
        .withTestResults(bloodTypingTestResults)
        .build();
    TestResultsBackingForms testResultsBackingForms = new TestResultsBackingForms();
    ArrayList<TestResultsBackingForm> testOutcomesForDonations = new ArrayList<>();
    testOutcomesForDonations.add(testResultsBackingForm);
    testResultsBackingForms.setTestOutcomesForDonations(testOutcomesForDonations);

    // set up mocks
    when(bloodTestRepository.getBloodTests(false, false)).thenReturn(tests);
    when(entityManager.createQuery("SELECT b FROM BloodTest b WHERE b.isActive = :isActive ", BloodTest.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("isActive", true)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(tests);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "testOutcomesForDonations");
    testResultsBackingFormsValidator.validateForm(testResultsBackingForms, errors);

    // do asserts
    Assert.assertEquals("errors found", 0, errors.getErrorCount());

  }

  @Test
  public void testValidateTestResultValuesNoValueSpecifiedEmptyNotAllowed() throws Exception {
    // set up data
    Map<Long, String> bloodTypingTestResults = new HashMap<>();
    bloodTypingTestResults.put(2l, "");

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(1l).withValidResults("A,B,AB,O").withIsEmptyAllowed(false).build());
    tests.add(BloodTestBuilder.aBloodTest().withId(2l).withValidResults("POS,NEG").withIsEmptyAllowed(false).build());

    TestResultsBackingForm testResultsBackingForm = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber("1111111").withTestResults(bloodTypingTestResults).build();
    TestResultsBackingForms testResultsBackingForms = new TestResultsBackingForms();
    ArrayList<TestResultsBackingForm> testOutcomesForDonations = new ArrayList<>();
    testOutcomesForDonations.add(testResultsBackingForm);
    testResultsBackingForms.setTestOutcomesForDonations(testOutcomesForDonations);

    // set up mocks
    when(bloodTestRepository.getBloodTests(false, false)).thenReturn(tests);
    when(entityManager.createQuery("SELECT b FROM BloodTest b WHERE b.isActive = :isActive ", BloodTest.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("isActive", true)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(tests);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "testOutcomesForDonations");
    testResultsBackingFormsValidator.validateForm(testResultsBackingForms, errors);

    // do asserts
    Assert.assertEquals("errors found", 1, errors.getErrorCount());
    Assert.assertEquals("error message correct", "No value specified",
        errors.getAllErrors().get(0).getDefaultMessage());
  }

  @Test
  public void testValidateTestResultValuesNoValueSpecifiedEmptyAllowed() throws Exception {
    // set up data
    Map<Long, String> bloodTypingTestResults = new HashMap<>();
    bloodTypingTestResults.put(2l, "");

    List<BloodTest> tests = new ArrayList<>();
    // FIXME: had to force an empty string into the valid results
    tests.add(BloodTestBuilder.aBloodTest().withId(2l).withValidResults("POS,,NEG").withIsEmptyAllowed(true).build());

    TestResultsBackingForm testResultsBackingForm = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber("1111111").withTestResults(bloodTypingTestResults).build();
    TestResultsBackingForms testResultsBackingForms = new TestResultsBackingForms();
    ArrayList<TestResultsBackingForm> testOutcomesForDonations = new ArrayList<>();
    testOutcomesForDonations.add(testResultsBackingForm);
    testResultsBackingForms.setTestOutcomesForDonations(testOutcomesForDonations);

    // set up mocks
    when(bloodTestRepository.getBloodTests(false, false)).thenReturn(tests);
    when(entityManager.createQuery("SELECT b FROM BloodTest b WHERE b.isActive = :isActive ", BloodTest.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("isActive", true)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(tests);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "testOutcomesForDonations");
    testResultsBackingFormsValidator.validateForm(testResultsBackingForms, errors);

    // do asserts
    Assert.assertEquals("errors found", 0, errors.getErrorCount());
  }

  @Test
  public void testValidateTestResultValuesInvalidTest() throws Exception {
    // set up data
    Map<Long, String> bloodTypingTestResults = new HashMap<>();
    bloodTypingTestResults.put(123l, "FALSE");

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(1l).withIsEmptyAllowed(false).build());

    TestResultsBackingForm testResultsBackingForm = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber("1111111").withTestResults(bloodTypingTestResults).build();
    TestResultsBackingForms testResultsBackingForms = new TestResultsBackingForms();
    ArrayList<TestResultsBackingForm> testOutcomesForDonations = new ArrayList<>();
    testOutcomesForDonations.add(testResultsBackingForm);
    testResultsBackingForms.setTestOutcomesForDonations(testOutcomesForDonations);

    // set up mocks
    when(bloodTestRepository.getBloodTests(false, false)).thenReturn(tests);
    when(entityManager.createQuery("SELECT b FROM BloodTest b WHERE b.isActive = :isActive ", BloodTest.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("isActive", true)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(tests);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "testOutcomesForDonations");
    testResultsBackingFormsValidator.validateForm(testResultsBackingForms, errors);

    // check asserts
    Assert.assertEquals("errors found", 1, errors.getErrorCount());
    Assert.assertEquals("error message correct", "Invalid test", errors.getAllErrors().get(0).getDefaultMessage());
  }

}
