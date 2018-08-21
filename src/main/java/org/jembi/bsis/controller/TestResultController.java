package org.jembi.bsis.controller;

import org.jembi.bsis.backingform.TestResultsBackingForms;
import org.jembi.bsis.backingform.validator.TestResultsBackingFormsValidator;
import org.jembi.bsis.controllerservice.TestResultControllerService;
import org.jembi.bsis.factory.DonationFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.factory.PackTypeFactory;
import org.jembi.bsis.factory.TestBatchFactory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.PackTypeRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.jembi.bsis.service.BloodTestsService;
import org.jembi.bsis.utils.CustomDateFormatter;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestResultFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.jembi.bsis.viewmodel.DonationTestOutcomesReportViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Transactional
@RestController
@RequestMapping("testresults")
public class TestResultController {

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private TestBatchRepository testBatchRepository;

  @Autowired
  private BloodTestsService bloodTestsService;

  @Autowired
  private TestBatchFactory testBatchViewModelFactory;

  @Autowired
  private TestResultsBackingFormsValidator testResultsBackingFormsValidator;

  @Autowired
  private DonationFactory donationFactory;

  @Autowired
  private TestResultControllerService testResultControllerService;

  @Autowired
  private LocationFactory locationFactory;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private PackTypeFactory packTypeFactory;

  @Autowired
  private PackTypeRepository packTypeRepository;

  @InitBinder
  protected void initDonationFormBinder(WebDataBinder binder) {
    binder.setValidator(testResultsBackingFormsValidator);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TEST_OUTCOME + "')")
  public Map<String, Object> form() {
    Map<String, Object> response = new HashMap<>();
    response.put("venues",
        locationRepository.getVenues().stream()
            .map(locationFactory::createViewModel)
            .collect(Collectors.toList()));
    response.put("packTypes",
        packTypeRepository.getAllPackTypesProducingTestSamples().stream()
            .map(packTypeFactory::createViewModel)
            .collect(Collectors.toList()));
    return response;
  }

  @RequestMapping(value = "/{donationIdentificationNumber}/sample", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TEST_OUTCOME + "')")
  public Map<String, Object> findTestSample(@PathVariable String donationIdentificationNumber) {
    return Collections.<String, Object>singletonMap("testSample",
        testResultControllerService.getTestSample(donationIdentificationNumber));
  }

  @RequestMapping(value = "{donationIdentificationNumber}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TEST_OUTCOME + "')")
  public ResponseEntity<Map<String, Object>> findTestResult(@PathVariable String donationIdentificationNumber) {

    Map<String, Object> map = new HashMap<String, Object>();
    Donation donation = donationRepository.findDonationByDonationIdentificationNumber(donationIdentificationNumber);
    map.put("donation", donationFactory.createDonationFullViewModelWithoutPermissions(donation));

    if (donation.getPackType().getTestSampleProduced()) {
      BloodTestingRuleResult results = testResultControllerService.getBloodTestingRuleResult(donation);
      map.put("testResults", results);
    } else {
      map.put("testResults", null);
    }
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TEST_OUTCOME + "')")
  public ResponseEntity<Map<String, Object>> findTestResultsForTestBatch(
      @RequestParam(value = "testBatch", required = true) UUID testBatchId,
      @RequestParam(value = "bloodTestType", required = false) BloodTestType bloodTestType) {

    TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
    List<BloodTestingRuleResult> ruleResults =
        testResultControllerService.getBloodTestingRuleResults(bloodTestType, testBatch);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("testResults", ruleResults);
    map.put("testBatchCreatedDate", CustomDateFormatter.format(testBatch.getCreatedDate()));

    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/report", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TEST_OUTCOME + "')")
  public ResponseEntity<Map<String, Object>> getTestBatchOutcomesReport(@RequestParam(value = "testBatch",
      required = true) UUID testBatchId) {

    TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
    List<DonationTestOutcomesReportViewModel> donationTestOutcomesReports =
        testBatchViewModelFactory.createDonationTestOutcomesReportViewModels(testBatch);

    Map<String, Object> map = bloodTestsService.getBloodTestShortNames();
    map.put("donationTestOutcomesReports", donationTestOutcomesReports);
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/overview", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TEST_OUTCOME + "')")
  public ResponseEntity<Map<String, Object>> findTestResultsOverviewForTestBatch(
      @RequestParam(value = "testBatch", required = true) UUID testBatchId) {

    TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
    List<BloodTestingRuleResult> ruleResults = testResultControllerService.getBloodTestingRuleResults(testBatch);
    Map<String, Object> map = calculateOverviewFlags(ruleResults);
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  private Map<String, Object> calculateOverviewFlags(List<BloodTestingRuleResult> ruleResults) {

    Map<String, Object> overviewFlags = new HashMap<String, Object>();
    overviewFlags.put("hasReEntryRequiredTTITests", false);
    overviewFlags.put("hasReEntryRequiredBloodTypingTests", false);
    overviewFlags.put("hasReEntryRequiredRepeatBloodTypingTests", false);
    overviewFlags.put("hasReEntryRequiredConfirmatoryTTITests", false);
    overviewFlags.put("hasReEntryRequiredRepeatTTITests", false);
    overviewFlags.put("hasRepeatBloodTypingTests", false);
    overviewFlags.put("hasConfirmatoryTTITests", false);
    overviewFlags.put("hasRepeatTTITests", false);
    overviewFlags.put("hasPendingRepeatTTITests", false);
    overviewFlags.put("hasPendingConfirmatoryTTITests", false);
    overviewFlags.put("hasPendingRepeatBloodTypingTests", false);
    overviewFlags.put("hasPendingBloodTypingConfirmations", false);

    for (BloodTestingRuleResult result : ruleResults) {

      Map<UUID, BloodTestResultFullViewModel> resultViewModelMap = result.getRecentTestResults();
      for (UUID key : resultViewModelMap.keySet()) {
        BloodTestResultFullViewModel bloodTestResultFullViewModel = resultViewModelMap.get(key);
        BloodTestFullViewModel bloodTest = bloodTestResultFullViewModel.getBloodTest();
        if (bloodTestResultFullViewModel.getReEntryRequired().equals(true)) {
          if (bloodTest.getBloodTestType().equals(BloodTestType.BASIC_TTI)) {
            overviewFlags.put("hasReEntryRequiredTTITests", true);
          } else if (bloodTest.getBloodTestType().equals(BloodTestType.BASIC_BLOODTYPING)) {
            overviewFlags.put("hasReEntryRequiredBloodTypingTests", true);
          } else if (bloodTest.getBloodTestType().equals(BloodTestType.REPEAT_BLOODTYPING)) {
            overviewFlags.put("hasReEntryRequiredRepeatBloodTypingTests", true);
          } else if (bloodTest.getBloodTestType().equals(BloodTestType.CONFIRMATORY_TTI)) {
            overviewFlags.put("hasReEntryRequiredConfirmatoryTTITests", true);
          } else if (bloodTest.getBloodTestType().equals(BloodTestType.REPEAT_TTI)) {
            overviewFlags.put("hasReEntryRequiredRepeatTTITests", true);
          }
        }
        if (bloodTest.getBloodTestType().equals(BloodTestType.REPEAT_TTI)) {
          overviewFlags.put("hasRepeatTTITests", true);
        } else if (bloodTest.getBloodTestType().equals(BloodTestType.CONFIRMATORY_TTI)) {
          overviewFlags.put("hasConfirmatoryTTITests", true);
        } else if (bloodTest.getBloodTestType().equals(BloodTestType.REPEAT_BLOODTYPING)) {
          overviewFlags.put("hasRepeatBloodTypingTests", true);
        }
      }
      if (result.getPendingBloodTypingTestsIds().size() > 0) {
        overviewFlags.put("hasPendingRepeatBloodTypingTests", true);
        overviewFlags.put("hasRepeatBloodTypingTests", true);
      }
      if (result.getPendingConfirmatoryTTITestsIds().size() > 0) {
        overviewFlags.put("hasPendingConfirmatoryTTITests", true);
        overviewFlags.put("hasConfirmatoryTTITests", true);
      }
      if (result.getPendingRepeatTTITestsIds().size() > 0) {
        overviewFlags.put("hasPendingRepeatTTITests", true);
        overviewFlags.put("hasRepeatTTITests", true);
      }
      if (result.getBloodTypingMatchStatus().equals(BloodTypingMatchStatus.AMBIGUOUS)) {
        // A confirmation is required to resolve the ambiguous result.
        overviewFlags.put("hasPendingBloodTypingConfirmations", true);
      }
    }
    return overviewFlags;
  }

  @PreAuthorize("hasRole('" + PermissionConstants.ADD_TEST_OUTCOME + "')")
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> saveTestResults(
      @RequestBody @Valid TestResultsBackingForms testResultsBackingForms,
      @RequestParam(value = "reEntry", required = false, defaultValue = "false") boolean reEntry) {

    HttpStatus responseStatus = HttpStatus.CREATED;
    Map<String, Object> responseMap = new HashMap<>();
    bloodTestsService.saveBloodTests(testResultsBackingForms.getTestOutcomesForDonations(), reEntry);
    return new ResponseEntity<>(responseMap, responseStatus);
  }

}
