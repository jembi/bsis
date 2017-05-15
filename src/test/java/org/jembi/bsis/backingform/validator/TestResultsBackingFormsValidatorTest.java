package org.jembi.bsis.backingform.validator;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jembi.bsis.backingform.TestResultsBackingForm;
import org.jembi.bsis.backingform.TestResultsBackingForms;
import org.jembi.bsis.helpers.builders.BloodTestBuilder;
import org.jembi.bsis.helpers.builders.TestResultsBackingFormBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.repository.BloodTestRepository;
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

  @SuppressWarnings("rawtypes")
  @Mock
  TypedQuery typedQuery;

  @Mock
  BloodTestRepository bloodTestRepository;

  @SuppressWarnings("unchecked")
  @Test
  public void testValidateTestResultValuesValidResult() throws Exception {
    // set up data
    UUID bloodTest1Id = UUID.randomUUID();
    UUID bloodTest2Id = UUID.randomUUID();
    Map<UUID, String> bloodTypingTestResults = new HashMap<>();
    bloodTypingTestResults.put(bloodTest1Id, "A"); // invalid result
    bloodTypingTestResults.put(bloodTest2Id, "POS"); // invalid result

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(bloodTest1Id).withValidResults("A,B,AB,O").build());
    tests.add(BloodTestBuilder.aBloodTest().withId(bloodTest2Id).withValidResults("POS,NEG").build());

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

  @SuppressWarnings("unchecked")
  @Test
  public void testValidateTestResultValuesInvalidResult() throws Exception {
    // set up data
    UUID bloodTest1Id = UUID.randomUUID();
    UUID bloodTest2Id = UUID.randomUUID();
    Map<UUID, String> bloodTypingTestResults = new HashMap<>();
    bloodTypingTestResults.put(bloodTest1Id, "G"); // invalid result
    bloodTypingTestResults.put(bloodTest2Id, "FALSE"); // invalid result

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(bloodTest1Id).withValidResults("A,B,AB,O").build());
    tests.add(BloodTestBuilder.aBloodTest().withId(bloodTest2Id).withValidResults("POS,NEG").build());

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

  @SuppressWarnings("unchecked")
  @Test
  public void testValidateTestResultValuesNoTestResults() throws Exception {
    // set up data
    UUID bloodTest1Id = UUID.randomUUID();
    UUID bloodTest2Id = UUID.randomUUID();
    Map<UUID, String> bloodTypingTestResults = new HashMap<>();

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(bloodTest1Id).withValidResults("A,B,AB,O").build());
    tests.add(BloodTestBuilder.aBloodTest().withId(bloodTest2Id).withValidResults("POS,NEG").build());

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

  @SuppressWarnings("unchecked")
  @Test
  public void testValidateTestResultValuesInvalidTest() throws Exception {
    // set up data
    UUID bloodTest1Id = UUID.randomUUID();
    Map<UUID, String> bloodTypingTestResults = new HashMap<>();
    bloodTypingTestResults.put(UUID.randomUUID(), "FALSE");

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(bloodTest1Id).build());

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
