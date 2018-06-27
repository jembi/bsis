package org.jembi.bsis.controller;

import static org.jembi.bsis.helpers.builders.BloodTestFullViewModelBuilder.aBasicBloodTypingBloodTestFullViewModel;
import static org.jembi.bsis.helpers.builders.BloodTestFullViewModelBuilder.aBasicTTIBloodTestFullViewModel;
import static org.jembi.bsis.helpers.builders.BloodTestFullViewModelBuilder.aConfirmatoryTTIBloodTestFullViewModel;
import static org.jembi.bsis.helpers.builders.BloodTestFullViewModelBuilder.aRepeatBloodTypingBloodTestFullViewModel;
import static org.jembi.bsis.helpers.builders.BloodTestFullViewModelBuilder.aRepeatTTIBloodTestFullViewModel;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleResultBuilder.aBloodTestingRuleResult;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.jembi.bsis.controllerservice.TestResultControllerService;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.TestBatchRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestResultFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.http.ResponseEntity;

public class TestResultControllerTests extends UnitTestSuite {
  
  @Spy
  @InjectMocks
  private TestResultController testResultController;
  @Mock
  private TestBatchRepository testBatchRepository;
  @Mock
  private TestResultControllerService testResultControllerService;

  // TODO these testcases can be improved to have better and more descriptive names, and for the
  // tests not to test too many logical combinations at the same time.

  @Test
  public void testFindTestResultsOverviewForTestBatchWithReEntryRequiredForTTITestsOnly_shouldReturnCorrectResults() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    
    TestBatch aTestBatch = aTestBatch()
        .withId(UUID.randomUUID())
        .withBatchNumber("00001")
        .withTestBatchDate(new Date())
        .withStatus(TestBatchStatus.OPEN)
        .build();
    
    UUID basicTTIBloodTestId = UUID.randomUUID();
    BloodTestFullViewModel basicTTIBloodFullTestViewModel = aBasicTTIBloodTestFullViewModel()
        .withId(basicTTIBloodTestId)
        .withBloodTestType(BloodTestType.BASIC_TTI)
        .build();
    
    BloodTestResultFullViewModel basicTTIBloodTestResultFullViewModel =
        BloodTestResultFullViewModel.builder()
        .id(UUID.randomUUID())
        .bloodTest(basicTTIBloodFullTestViewModel)
        .reEntryRequired(true)
        .build();
            
    Map<UUID, BloodTestResultFullViewModel> recentTestResults = new HashMap<>();
    recentTestResults.put(basicTTIBloodTestId, basicTTIBloodTestResultFullViewModel);
    
    List<UUID> noTestIds = new ArrayList<>();
    List<BloodTestingRuleResult> bloodTestingRuleResult = Arrays.asList(
        aBloodTestingRuleResult()
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withPendingRepeatAndConfirmatoryTtiTestsIds(noTestIds)
            .withRecentResults(recentTestResults)
            .withPendingBloodTypingTestsIds(noTestIds)
            .withPendingConfirmatoryTTITestsIds(noTestIds)
            .withPendingRepeatTTITestsIds(noTestIds)
            .build());
  
    // Expected Data.
    Map<String, Object> expectedOverviewFlags = new HashMap<String, Object>();
    expectedOverviewFlags.put("hasReEntryRequiredTTITests", true);
    expectedOverviewFlags.put("hasReEntryRequiredBloodTypingTests", false);
    expectedOverviewFlags.put("hasReEntryRequiredRepeatBloodTypingTests", false);
    expectedOverviewFlags.put("hasReEntryRequiredConfirmatoryTTITests", false);
    expectedOverviewFlags.put("hasReEntryRequiredRepeatTTITests", false);
    expectedOverviewFlags.put("hasRepeatBloodTypingTests", false);
    expectedOverviewFlags.put("hasConfirmatoryTTITests", false);
    expectedOverviewFlags.put("hasRepeatTTITests", false);
    expectedOverviewFlags.put("hasPendingRepeatTTITests", false);
    expectedOverviewFlags.put("hasPendingConfirmatoryTTITests", false);
    expectedOverviewFlags.put("hasPendingRepeatBloodTypingTests", false);
    expectedOverviewFlags.put("hasPendingBloodTypingConfirmations", false);
    
    when(testBatchRepository.findTestBatchById(aTestBatch.getId())).thenReturn(aTestBatch);
    when(testResultControllerService.getBloodTestingRuleResults(aTestBatch)).thenReturn(bloodTestingRuleResult);

    // Test
    ResponseEntity<Map<String, Object>> returnedResponse = testResultController.findTestResultsOverviewForTestBatch(
        aTestBatch.getId());

    Map<String, Object> overViewFlags = returnedResponse.getBody();
    Assert.assertNotNull("map is returned", overViewFlags);
    Assert.assertEquals(expectedOverviewFlags,overViewFlags);
  }
  
  @Test
  public void testFindTestResultsOverviewForTestBatchWithMultipleRuleResultsWithReEntryRequiredForBasicAndRepeatTTITests_shouldReturnCorrectResults() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    
    TestBatch aTestBatch = aTestBatch().withId(UUID.randomUUID()).withBatchNumber("00001").withTestBatchDate(new Date())
        .withStatus(TestBatchStatus.OPEN).build();
    
    UUID basicTTIBloodTestId = UUID.randomUUID();
    BloodTestFullViewModel basicTTIBloodFullTestViewModel = aBasicBloodTypingBloodTestFullViewModel()
        .withId(basicTTIBloodTestId)
        .withBloodTestType(BloodTestType.BASIC_TTI)
        .build();
    
    BloodTestResultFullViewModel basicTTIBloodTestResultFullViewModel =
        BloodTestResultFullViewModel.builder()
        .id(UUID.randomUUID())
        .bloodTest(basicTTIBloodFullTestViewModel)
        .reEntryRequired(true)
        .build();
            
    Map<UUID, BloodTestResultFullViewModel> recentTestResults1 = new HashMap<>();
    recentTestResults1.put(basicTTIBloodTestId, basicTTIBloodTestResultFullViewModel);

    UUID repeatTTIBloodTestId = UUID.randomUUID();
    BloodTestFullViewModel repeatTTIBloodFullTestViewModel = aBasicBloodTypingBloodTestFullViewModel()
        .withId(repeatTTIBloodTestId)
        .withBloodTestType(BloodTestType.REPEAT_TTI)
        .build();
    
    BloodTestResultFullViewModel repeatTTIBloodTestResultFullViewModel =
        BloodTestResultFullViewModel.builder()
        .id(UUID.randomUUID())
        .bloodTest(repeatTTIBloodFullTestViewModel)
        .reEntryRequired(true)
        .build();
            
    Map<UUID, BloodTestResultFullViewModel> recentTestResults2 = new HashMap<>();
    recentTestResults2.put(repeatTTIBloodTestId, repeatTTIBloodTestResultFullViewModel);
   
    List<UUID> noTestIds = new ArrayList<>();

    List<BloodTestingRuleResult> bloodTestingRuleResult = Arrays.asList(
        aBloodTestingRuleResult()
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withPendingRepeatAndConfirmatoryTtiTestsIds(noTestIds)
            .withRecentResults(recentTestResults1)
            .withPendingBloodTypingTestsIds(noTestIds)
            .withPendingConfirmatoryTTITestsIds(noTestIds)
            .withPendingRepeatTTITestsIds(noTestIds)
            .build(),
        aBloodTestingRuleResult()
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withPendingRepeatAndConfirmatoryTtiTestsIds(noTestIds)
            .withRecentResults(recentTestResults2)
            .withPendingBloodTypingTestsIds(noTestIds)
            .withPendingConfirmatoryTTITestsIds(noTestIds)
            .withPendingRepeatTTITestsIds(noTestIds)
            .build());
    
    // Expected Data.
    Map<String, Object> expectedOverviewFlags = new HashMap<String, Object>();
    expectedOverviewFlags.put("hasReEntryRequiredTTITests", true);
    expectedOverviewFlags.put("hasReEntryRequiredBloodTypingTests", false);
    expectedOverviewFlags.put("hasReEntryRequiredRepeatBloodTypingTests", false);
    expectedOverviewFlags.put("hasReEntryRequiredConfirmatoryTTITests", false);
    expectedOverviewFlags.put("hasReEntryRequiredRepeatTTITests", true);
    expectedOverviewFlags.put("hasRepeatBloodTypingTests", false);
    expectedOverviewFlags.put("hasConfirmatoryTTITests", false);
    expectedOverviewFlags.put("hasRepeatTTITests", true);
    expectedOverviewFlags.put("hasPendingRepeatTTITests", false);
    expectedOverviewFlags.put("hasPendingConfirmatoryTTITests", false);
    expectedOverviewFlags.put("hasPendingRepeatBloodTypingTests", false);
    expectedOverviewFlags.put("hasPendingBloodTypingConfirmations", false);
    
    when(testBatchRepository.findTestBatchById(aTestBatch.getId())).thenReturn(aTestBatch);
    when(testResultControllerService.getBloodTestingRuleResults(aTestBatch)).thenReturn(bloodTestingRuleResult);
    // Test
    ResponseEntity<Map<String, Object>> returnedResponse = testResultController.findTestResultsOverviewForTestBatch(
        aTestBatch.getId());

    Map<String, Object> overViewFlags = returnedResponse.getBody();
    Assert.assertNotNull("map is returned", overViewFlags);
    Assert.assertEquals(expectedOverviewFlags,overViewFlags);
  }
  
  @Test
  public void testFindTestResultsOverviewForTestBatchWithPendingBloodTypingTests_shouldReturnCorrectResults() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    
    TestBatch aTestBatch = aTestBatch().withId(UUID.randomUUID()).withBatchNumber("00001").withTestBatchDate(new Date())
        .withStatus(TestBatchStatus.OPEN).build();
    
    UUID basicBloodTypingBloodTestId = UUID.randomUUID();
    BloodTestFullViewModel basicBloodTypingBloodFullTestViewModel = aBasicBloodTypingBloodTestFullViewModel()
        .withId(basicBloodTypingBloodTestId)
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .build();
    
    BloodTestResultFullViewModel basicBloodTypingBloodTestResultFullViewModel =
        BloodTestResultFullViewModel.builder()
        .id(UUID.randomUUID())
        .bloodTest(basicBloodTypingBloodFullTestViewModel)
        .reEntryRequired(false)
        .build();
            
    Map<UUID, BloodTestResultFullViewModel> recentTestResults = new HashMap<>();
    recentTestResults.put(basicBloodTypingBloodTestId,
        basicBloodTypingBloodTestResultFullViewModel);

    List<UUID> bloodTypeTestsIds = Arrays.asList(basicBloodTypingBloodTestId, UUID.randomUUID(), UUID.randomUUID());
    List<UUID> noTestIds = new ArrayList<>();
    List<BloodTestingRuleResult> bloodTestingRuleResult = Arrays.asList(aBloodTestingRuleResult()
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withPendingRepeatAndConfirmatoryTtiTestsIds(noTestIds)
        .withRecentResults(recentTestResults)
        .withPendingBloodTypingTestsIds(bloodTypeTestsIds)
        .withPendingConfirmatoryTTITestsIds(noTestIds)
        .withPendingRepeatTTITestsIds(noTestIds)
        .build());

    // Expected Data.
    Map<String, Object> expectedOverviewFlags = new HashMap<String, Object>();
    expectedOverviewFlags.put("hasReEntryRequiredTTITests", false);
    expectedOverviewFlags.put("hasReEntryRequiredBloodTypingTests", false);
    expectedOverviewFlags.put("hasReEntryRequiredRepeatBloodTypingTests", false);
    expectedOverviewFlags.put("hasReEntryRequiredConfirmatoryTTITests", false);
    expectedOverviewFlags.put("hasReEntryRequiredRepeatTTITests", false);
    expectedOverviewFlags.put("hasRepeatBloodTypingTests", true);
    expectedOverviewFlags.put("hasConfirmatoryTTITests", false);
    expectedOverviewFlags.put("hasRepeatTTITests", false);
    expectedOverviewFlags.put("hasPendingRepeatTTITests", false);
    expectedOverviewFlags.put("hasPendingConfirmatoryTTITests", false);
    expectedOverviewFlags.put("hasPendingRepeatBloodTypingTests", true);
    expectedOverviewFlags.put("hasPendingBloodTypingConfirmations", false);

    when(testBatchRepository.findTestBatchById(aTestBatch.getId())).thenReturn(aTestBatch);
    when(testResultControllerService.getBloodTestingRuleResults(aTestBatch)).thenReturn(bloodTestingRuleResult);

    // Test
    ResponseEntity<Map<String, Object>> returnedResponse =
        testResultController.findTestResultsOverviewForTestBatch(aTestBatch.getId());

    Map<String, Object> overViewFlags = returnedResponse.getBody();
    Assert.assertNotNull("map is returned", overViewFlags);
    Assert.assertEquals(expectedOverviewFlags, overViewFlags);
  }

  @Test
  public void testFindTestResultsOverviewForTestBatchWithAllPendingTestsAndNoRecentResults_shouldReturnCorrectResults() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

    TestBatch aTestBatch = aTestBatch().withId(UUID.randomUUID()).withBatchNumber("00001").withTestBatchDate(new Date())
        .withStatus(TestBatchStatus.OPEN).build();

    Map<UUID, BloodTestResultFullViewModel> recentTestResults = new HashMap<>();

    List<UUID> testsIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    List<BloodTestingRuleResult> bloodTestingRuleResult = Arrays.asList(
        aBloodTestingRuleResult()
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS)
            .withPendingRepeatAndConfirmatoryTtiTestsIds(testsIds)
            .withRecentResults(recentTestResults)
            .withPendingBloodTypingTestsIds(testsIds)
            .withPendingConfirmatoryTTITestsIds(testsIds)
            .withPendingRepeatTTITestsIds(testsIds)
            .build());

    // Expected Data.
    Map<String, Object> expectedOverviewFlags = new HashMap<String, Object>();
    expectedOverviewFlags.put("hasReEntryRequiredTTITests", false);
    expectedOverviewFlags.put("hasReEntryRequiredBloodTypingTests", false);
    expectedOverviewFlags.put("hasReEntryRequiredRepeatBloodTypingTests", false);
    expectedOverviewFlags.put("hasReEntryRequiredConfirmatoryTTITests", false);
    expectedOverviewFlags.put("hasReEntryRequiredRepeatTTITests", false);
    expectedOverviewFlags.put("hasRepeatBloodTypingTests", true);
    expectedOverviewFlags.put("hasConfirmatoryTTITests", true);
    expectedOverviewFlags.put("hasRepeatTTITests", true);
    expectedOverviewFlags.put("hasPendingRepeatTTITests", true);
    expectedOverviewFlags.put("hasPendingConfirmatoryTTITests", true);
    expectedOverviewFlags.put("hasPendingRepeatBloodTypingTests", true);
    expectedOverviewFlags.put("hasPendingBloodTypingConfirmations", true);
    
    when(testBatchRepository.findTestBatchById(aTestBatch.getId())).thenReturn(aTestBatch);
    when(testResultControllerService.getBloodTestingRuleResults(aTestBatch)).thenReturn(bloodTestingRuleResult);

    // Test
    ResponseEntity<Map<String, Object>> returnedResponse = testResultController.findTestResultsOverviewForTestBatch(
        aTestBatch.getId());

    Map<String, Object> overViewFlags = returnedResponse.getBody();
    Assert.assertNotNull("map is returned", overViewFlags);
    Assert.assertEquals(expectedOverviewFlags,overViewFlags);
  }
  
  @Test
  public void testFindTestResultsOverviewForTestBatchWithAllTestsRequiringReEntry_shouldReturnCorrectResults() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    
    TestBatch aTestBatch = aTestBatch().withId(UUID.randomUUID()).withBatchNumber("00001").withTestBatchDate(new Date())
        .withStatus(TestBatchStatus.OPEN).build();
    
    UUID basicBloodTypingBloodTestId = UUID.randomUUID();
    BloodTestFullViewModel basicBloodTypingBloodFullTestViewModel = aBasicBloodTypingBloodTestFullViewModel()
        .withId(basicBloodTypingBloodTestId)
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .build();
    
    BloodTestResultFullViewModel bloodTypingTestResultViewModel = BloodTestResultFullViewModel.builder()
        .id(UUID.randomUUID())
        .bloodTest(basicBloodTypingBloodFullTestViewModel)
        .reEntryRequired(true)
        .build();

    UUID basicTTIBloodTestId = UUID.randomUUID();
    BloodTestFullViewModel basicTTIBloodFullTestViewModel = aBasicTTIBloodTestFullViewModel()
        .withId(basicTTIBloodTestId)
        .withBloodTestType(BloodTestType.BASIC_TTI)
        .build();
    
    BloodTestResultFullViewModel bloodTTITestResultViewModel = BloodTestResultFullViewModel.builder()
        .id(UUID.randomUUID())
        .bloodTest(basicTTIBloodFullTestViewModel)
        .reEntryRequired(true)
        .build();

    UUID repeatBloodTypingTestId = UUID.randomUUID();
    BloodTestFullViewModel repeatBloodTypingFullTestViewModel = aRepeatBloodTypingBloodTestFullViewModel().withId(repeatBloodTypingTestId)
        .withBloodTestType(BloodTestType.REPEAT_BLOODTYPING).build();

    BloodTestResultFullViewModel repeatBloodTypingTestResultViewModel = BloodTestResultFullViewModel.builder()
        .id(UUID.randomUUID())
        .bloodTest(repeatBloodTypingFullTestViewModel).reEntryRequired(true).build();

    UUID confirmatoryTTIBloodTestId = UUID.randomUUID();
    BloodTestFullViewModel confirmatoryTTIBloodFullTestViewModel =
        aConfirmatoryTTIBloodTestFullViewModel().withId(confirmatoryTTIBloodTestId).withBloodTestType(BloodTestType.CONFIRMATORY_TTI).build();

    BloodTestResultFullViewModel confirmatoryTTITestResultViewModel = BloodTestResultFullViewModel.builder()
        .id(UUID.randomUUID())
        .bloodTest(confirmatoryTTIBloodFullTestViewModel).reEntryRequired(true).build();

    UUID repeatTTIBloodTestId = UUID.randomUUID();
    BloodTestFullViewModel repeatTTIFullTestViewModel =
        aRepeatTTIBloodTestFullViewModel().withId(repeatTTIBloodTestId).withBloodTestType(BloodTestType.REPEAT_TTI)
        .build();
    
    BloodTestResultFullViewModel repeatTTITestResultViewModel =
        BloodTestResultFullViewModel.builder().id(UUID.randomUUID()).bloodTest(repeatTTIFullTestViewModel)
        .reEntryRequired(true)
        .build();
    
    Map<UUID, BloodTestResultFullViewModel> recentTestResults = new HashMap<>();
    recentTestResults.put(basicBloodTypingBloodTestId, bloodTypingTestResultViewModel);
    recentTestResults.put(basicTTIBloodTestId, bloodTTITestResultViewModel);
    recentTestResults.put(repeatBloodTypingTestId, repeatBloodTypingTestResultViewModel);
    recentTestResults.put(confirmatoryTTIBloodTestId, confirmatoryTTITestResultViewModel);
    recentTestResults.put(repeatTTIBloodTestId, repeatTTITestResultViewModel);


    List<UUID> noTestIds = new ArrayList<>();
    List<BloodTestingRuleResult> bloodTestingRuleResult = Arrays.asList(
        aBloodTestingRuleResult()
            .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
            .withPendingRepeatAndConfirmatoryTtiTestsIds(noTestIds)
            .withRecentResults(recentTestResults)
            .withPendingBloodTypingTestsIds(noTestIds)
            .withPendingConfirmatoryTTITestsIds(noTestIds)
            .withPendingRepeatTTITestsIds(noTestIds)
            .build());
  
    // Expected Data.
    Map<String, Object> expectedOverviewFlags = new HashMap<String, Object>();
    expectedOverviewFlags.put("hasReEntryRequiredTTITests", true);
    expectedOverviewFlags.put("hasReEntryRequiredBloodTypingTests", true);
    expectedOverviewFlags.put("hasReEntryRequiredRepeatBloodTypingTests", true);
    expectedOverviewFlags.put("hasReEntryRequiredConfirmatoryTTITests", true);
    expectedOverviewFlags.put("hasReEntryRequiredRepeatTTITests", true);
    expectedOverviewFlags.put("hasRepeatBloodTypingTests", true);
    expectedOverviewFlags.put("hasConfirmatoryTTITests", true);
    expectedOverviewFlags.put("hasRepeatTTITests", true);
    expectedOverviewFlags.put("hasPendingRepeatTTITests", false);
    expectedOverviewFlags.put("hasPendingConfirmatoryTTITests", false);
    expectedOverviewFlags.put("hasPendingRepeatBloodTypingTests", false);
    expectedOverviewFlags.put("hasPendingBloodTypingConfirmations", false);
    
    when(testBatchRepository.findTestBatchById(aTestBatch.getId())).thenReturn(aTestBatch);
    when(testResultControllerService.getBloodTestingRuleResults(aTestBatch)).thenReturn(bloodTestingRuleResult);
    // Test
    ResponseEntity<Map<String, Object>> returnedResponse = testResultController.findTestResultsOverviewForTestBatch(
        aTestBatch.getId());

    Map<String, Object> overViewFlags = returnedResponse.getBody();
    Assert.assertNotNull("map is returned", overViewFlags);
    Assert.assertEquals(expectedOverviewFlags,overViewFlags);
  }
}