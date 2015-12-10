package controller;

import backingform.TestResultBackingForm;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.donor.Donor;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import repository.DonationRepository;
import repository.DonorRepository;
import repository.TestBatchRepository;
import repository.bloodtesting.BloodTestingRepository;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.bloodtesting.BloodTypingStatus;
import service.TestBatchStatusChangeService;
import utils.PermissionConstants;
import viewmodel.BloodTestingRuleResult;
import viewmodel.DonationViewModel;
import viewmodel.DonorViewModel;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("testresults")
public class TestResultController {

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private TestBatchRepository testBatchRepository;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private TestBatchStatusChangeService testBatchStatusChangeService;

  public TestResultController() {
  }

  @RequestMapping(value = "{donationIdentificationNumber}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TEST_OUTCOME + "')")
  public ResponseEntity findTestResult(@PathVariable String donationIdentificationNumber) {

    Map<String, Object> map = new HashMap<>();
    Donation c = donationRepository.findDonationByDonationIdentificationNumber(donationIdentificationNumber);
    map.put("donation", new DonationViewModel(c));

    if (c.getPackType().getTestSampleProduced()) {
      BloodTestingRuleResult results = bloodTestingRepository.getAllTestsStatusForDonation(c.getId());
      map.put("testResults", results);
    } else {
      map.put("testResults", null);
    }
    return new ResponseEntity(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TEST_OUTCOME + "')")
  public ResponseEntity findTestResultsForTestBatch(HttpServletRequest request,
                                                    @RequestParam(value = "testBatch", required = true) Long testBatchId) {

    Map<String, Object> map = new HashMap<>();

    TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
    List<DonationBatch> donationBatches = testBatch.getDonationBatches();
    List<Integer> donationBatchIds = new ArrayList<>();
    for (DonationBatch donationBatch : donationBatches) {
      donationBatchIds.add(donationBatch.getId());
    }

    List<BloodTestingRuleResult> ruleResults =
            bloodTestingRepository.getAllTestsStatusForDonationBatches(donationBatchIds);

    map.put("testResults", ruleResults);

    return new ResponseEntity(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/overview", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TEST_OUTCOME + "')")
  public ResponseEntity findTestResultsOverviewForTestBatch(HttpServletRequest request,
                                                            @RequestParam(value = "testBatch", required = true) Long testBatchId) {

    Map<String, Object> map = new HashMap<>();

    TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
    List<DonationBatch> donationBatches = testBatch.getDonationBatches();
    List<Integer> donationBatchIds = new ArrayList<>();
    for (DonationBatch donationBatch : donationBatches) {
      donationBatchIds.add(donationBatch.getId());
    }

    List<BloodTestingRuleResult> ruleResults =
            bloodTestingRepository.getAllTestsStatusForDonationBatches(donationBatchIds);

    Boolean pendingBloodTypingTests = false;
    Boolean pendingTTITests = false;
    Boolean basicBloodTypingComplete = true;
    Boolean basicTTIComplete = true;
    Boolean pendingBloodTypingMatchTests = false;

    for (BloodTestingRuleResult result : ruleResults) {
      if (!result.getBloodTypingStatus().equals(BloodTypingStatus.COMPLETE)) {
        basicBloodTypingComplete = false;
      }
      if (result.getTTIStatus().equals(TTIStatus.NOT_DONE)) {
        basicTTIComplete = false;
      }
      if (result.getPendingBloodTypingTestsIds().size() > 0) {
        pendingBloodTypingTests = true;
      }
      if (result.getPendingTTITestsIds().size() > 0) {
        pendingTTITests = true;
      }
      if (!result.getBloodTypingStatus().equals(BloodTypingStatus.NOT_DONE) && (result.getBloodTypingMatchStatus().equals(BloodTypingMatchStatus.NO_MATCH) ||
              result.getBloodTypingMatchStatus().equals(BloodTypingMatchStatus.AMBIGUOUS))) {
        pendingBloodTypingMatchTests = true;
      }
    }

    map.put("pendingBloodTypingTests", pendingBloodTypingTests);
    map.put("pendingTTITests", pendingTTITests);
    map.put("basicBloodTypingComplete", basicBloodTypingComplete);
    map.put("basicTTIComplete", basicTTIComplete);
    map.put("pendingBloodTypingMatchTests", pendingBloodTypingMatchTests);

    return new ResponseEntity(map, HttpStatus.OK);
  }

  @PreAuthorize("hasRole('" + PermissionConstants.ADD_TEST_OUTCOME + "')")
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> saveTestResults(@RequestBody @Valid TestResultBackingForm form) {

    Donation donation = donationRepository.verifyDonationIdentificationNumber(form.getDonationIdentificationNumber());

    Map<String, Object> results = bloodTestingRepository.saveBloodTestingResults(donation.getId(),
            form.getTestResults(), true);

    Map<Long, String> errorMap = (Map<Long, String>) results.get("errors");

    Map<String, Object> responseMap = new HashMap<>();

    if (errorMap != null && !errorMap.isEmpty()) {
      // Errors found
      responseMap.put("success", false);
      responseMap.put("errorMap", errorMap);
      responseMap.put("uninterpretableResults", results.get("uninterpretableResults"));
      responseMap.put("errorMessage", "There were errors adding tests.");
      return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    // No errors
    responseMap.put("success", true);
    responseMap.put("testresults", results.get("bloodTestingResults"));
    return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
  }

  @PreAuthorize("hasRole('" + PermissionConstants.ADD_TEST_OUTCOME + "')")
  @RequestMapping(value = "/bloodgroupmatches", method = RequestMethod.GET)
  public ResponseEntity<Map<String, Object>> saveBloodGroupMatchTestResults(
          @RequestParam(value = "donationIdentificationNumber", required = true) String donationIdentificationNumber,
          @RequestParam(value = "bloodAbo", required = true) String bloodAbo,
          @RequestParam(value = "bloodRh", required = true) String bloodRh) {

    HttpStatus httpStatus = HttpStatus.CREATED;
    boolean success = true;
    String errorMessage = "";
    Map<Long, Map<Long, String>> errorMap = null;
    Map<String, Object> fieldErrors = new HashMap<>();
    Map<String, Object> map = new HashMap<>();
    //Donation donation = donationRepository.verifyDonationIdentificationNumber(form.getDonationIdentificationNumber());

    Map<String, Object> results = null;

    Donation donation = donationRepository.findDonationByDonationIdentificationNumber(donationIdentificationNumber);
    Donor donor = donation.getDonor();
    donor.setBloodAbo(bloodAbo);
    donor.setBloodRh(bloodRh);
    donation.setBloodAbo(bloodAbo);
    donation.setBloodRh(bloodRh);
    donation.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);

    Donor updatedDonor = donorRepository.updateDonorDetails(donor);
    Donation cs = donationRepository.updateDonationDetails(donation);

    if (cs.getDonationBatch().getTestBatch().getStatus() == TestBatchStatus.RELEASED) {
      testBatchStatusChangeService.handleRelease(cs);
    }

    map.put("donor", getDonorsViewModel(donorRepository.findDonorById(updatedDonor.getId())));
    return new ResponseEntity<>(map, httpStatus);
  }

  private DonorViewModel getDonorsViewModel(Donor donor) {
    DonorViewModel donorViewModel = new DonorViewModel(donor);
    return donorViewModel;
  }

}
