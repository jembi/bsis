package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import repository.DonationRepository;
import repository.DonorRepository;
import repository.TestBatchRepository;
import repository.bloodtesting.BloodTestingRepository;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.bloodtesting.BloodTypingStatus;
import service.BloodTestsService;
import service.TestBatchStatusChangeService;
import utils.PermissionConstants;
import viewmodel.BloodTestingRuleResult;
import viewmodel.DonationViewModel;
import viewmodel.DonorViewModel;
import backingform.TestResultBackingForm;

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
  
  @Autowired
  private BloodTestsService bloodTestsService;
  
  public TestResultController() {
  }

  @RequestMapping(value = "{donationIdentificationNumber}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_OUTCOME+"')")
  public ResponseEntity<Map<String, Object>> findTestResult(@PathVariable String donationIdentificationNumber ) {

    Map<String, Object> map = new HashMap<String, Object>();
    Donation c = donationRepository.findDonationByDonationIdentificationNumber(donationIdentificationNumber);
    map.put("donation", new DonationViewModel(c));
    
    if (c.getPackType().getTestSampleProduced()) {
        BloodTestingRuleResult results = bloodTestingRepository.getAllTestsStatusForDonation(c.getId());
        map.put("testResults", results);
    } else {
        map.put("testResults", null);
    }
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_OUTCOME+"')")
  public ResponseEntity<Map<String, Object>> findTestResultsForTestBatch(HttpServletRequest request,
		@RequestParam(value = "testBatch", required = true) Long testBatchId) {
	  
		Map<String, Object> map = new HashMap<String, Object>();
		
		TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
		List<DonationBatch> donationBatches = testBatch.getDonationBatches();
		List<Long> donationBatchIds = new ArrayList<Long>();
		for(DonationBatch donationBatch : donationBatches){
			donationBatchIds.add(donationBatch.getId());
		}
	
	    List<BloodTestingRuleResult> ruleResults =
	    		bloodTestingRepository.getAllTestsStatusForDonationBatches(donationBatchIds);
	
		map.put("testResults", ruleResults);
	
		return new ResponseEntity<>(map, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/overview", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_OUTCOME+"')")
  public ResponseEntity<Map<String, Object>> findTestResultsOverviewForTestBatch(HttpServletRequest request,
		@RequestParam(value = "testBatch", required = true) Long testBatchId) {
	  
		TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
		List<DonationBatch> donationBatches = testBatch.getDonationBatches();
		List<Long> donationBatchIds = new ArrayList<Long>();
		for(DonationBatch donationBatch : donationBatches){
			donationBatchIds.add(donationBatch.getId());
		}
	
	    List<BloodTestingRuleResult> ruleResults =
	    		bloodTestingRepository.getAllTestsStatusForDonationBatches(donationBatchIds);
	    
	    Boolean pendingBloodTypingTests = false;
	    Boolean pendingTTITests = false;
	    Boolean basicBloodTypingComplete = true;
	    Boolean basicTTIComplete = true;
	    boolean pendingBloodTypingConfirmations = false;
	    
	    for(BloodTestingRuleResult result : ruleResults){
	    	if(!result.getBloodTypingStatus().equals(BloodTypingStatus.COMPLETE)){
	    		basicBloodTypingComplete = false;
	    	}
	    	if(result.getTTIStatus().equals(TTIStatus.NOT_DONE)){
	    		basicTTIComplete = false;
	    	}
	    	if(result.getPendingBloodTypingTestsIds().size() > 0){
	    		pendingBloodTypingTests = true;
	    	}
	    	if(result.getPendingTTITestsIds().size() > 0){
	    		pendingTTITests = true;
	    	}
	    	if (result.getBloodTypingMatchStatus().equals(BloodTypingMatchStatus.AMBIGUOUS)) {
	    	  // A confirmation is required to resolve the ambiguous result.
	    	  pendingBloodTypingConfirmations = true;
	    	}
	    }
	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pendingBloodTypingTests", pendingBloodTypingTests);
		map.put("pendingTTITests", pendingTTITests);
		map.put("basicBloodTypingComplete", basicBloodTypingComplete);
		map.put("basicTTIComplete", basicTTIComplete);
		map.put("pendingBloodTypingConfirmations", pendingBloodTypingConfirmations);
	
		return new ResponseEntity<>(map, HttpStatus.OK);
  }
  
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_TEST_OUTCOME + "')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> saveTestResults(@RequestBody @Valid TestResultBackingForm form) {

      HttpStatus responseStatus = HttpStatus.CREATED;
      Map<String, Object> responseMap = new HashMap<>();

      Donation donation = donationRepository.verifyDonationIdentificationNumber(form.getDonationIdentificationNumber());
      if (donation == null) {
        responseStatus = HttpStatus.NOT_FOUND;
      } else {
        Map<Long, String> testResults = form.getTestResults();
        Map<Long, String> errors = bloodTestsService.validateTestResultValues(testResults);
        if (errors.isEmpty()) {
          // No errors
          BloodTestingRuleResult ruleResult = bloodTestsService.saveBloodTests(donation.getId(), form.getTestResults());
          responseMap.put("success", true);
          responseMap.put("testresults", ruleResult);
        } else {
          // Errors found
          responseMap.put("success", false);
          responseMap.put("errorMap", errors);
          responseMap.put("errorMessage", "There were errors adding tests.");
          responseStatus = HttpStatus.BAD_REQUEST;
        }
      }
      return new ResponseEntity<>(responseMap, responseStatus);
    }
  
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_TEST_OUTCOME+"')")
  @RequestMapping(value = "/bloodgroupmatches", method = RequestMethod.GET)
  public ResponseEntity<Map<String, Object>> saveBloodGroupMatchTestResults(
		  @RequestParam(value = "donationIdentificationNumber", required = true) String donationIdentificationNumber,
		  @RequestParam(value = "bloodAbo", required = true) String bloodAbo,
		  @RequestParam(value = "bloodRh", required = true) String bloodRh) {
	  
		HttpStatus httpStatus = HttpStatus.CREATED;        
		Map<String, Object> map = new HashMap<String, Object>();

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
        return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }
  
  private DonorViewModel getDonorsViewModel(Donor donor) {
    DonorViewModel donorViewModel = new DonorViewModel(donor);
    return donorViewModel;
  }

}
