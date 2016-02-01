package controller.bloodtesting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestType;
import model.donation.Donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import repository.DonationRepository;
import repository.GenericConfigRepository;
import repository.bloodtesting.BloodTestingRepository;
import utils.PermissionConstants;
import viewmodel.BloodTestViewModel;
import viewmodel.BloodTestingRuleResult;
import viewmodel.DonationViewModel;
import backingform.TestResultBackingForm;

@RestController
@RequestMapping("bloodgroupingtests")
public class BloodTypingController {

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  public BloodTypingController() {
  }
  
  	@RequestMapping(value = "/form", method = RequestMethod.GET)
	@PreAuthorize("hasRole('"+PermissionConstants.ADD_BLOOD_TYPING_OUTCOME+"')")
	public Map<String, Object> getBloodTypingForm(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<BloodTestViewModel> basicBloodTypingTests = getBasicBloodTypingTests();
		map.put("basicBloodTypingTests", basicBloodTypingTests);
		
		List<BloodTestViewModel> advancedBloodTypingTests = getAdvancedBloodTypingTests();
		map.put("advancedBloodTypingTests", advancedBloodTypingTests);
		
		List<BloodTestViewModel> repeatBloodTypingTests = getRepeatBloodTypingTests();
		map.put("repeatBloodTypingTests", repeatBloodTypingTests);
	
		return map;
	}
  
  public List<BloodTestViewModel> getBasicBloodTypingTests() {
    List<BloodTestViewModel> tests = new ArrayList<BloodTestViewModel>();
    for (BloodTest rawBloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)) {
      tests.add(new BloodTestViewModel(rawBloodTest));
    }
    return tests;
  }

  public List<BloodTestViewModel> getAdvancedBloodTypingTests() {
    List<BloodTestViewModel> tests = new ArrayList<BloodTestViewModel>();
    for (BloodTest rawBloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.ADVANCED_BLOODTYPING)) {
      tests.add(new BloodTestViewModel(rawBloodTest));
    }
    return tests;
  }
  
  public List<BloodTestViewModel> getRepeatBloodTypingTests() {
    List<BloodTestViewModel> tests = new ArrayList<BloodTestViewModel>();
    for (BloodTest rawBloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.REPEAT_BLOODTYPING)) {
      tests.add(new BloodTestViewModel(rawBloodTest));
    }
    return tests;
  }

  @RequestMapping(value="/batchresults/{donationIds}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_BLOOD_TYPING_OUTCOME+"')")
  public Map<String, Object> getBloodTypingStatusForDonations(
                        @PathVariable String donationIds) {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> results = bloodTestingRepository.getAllTestsStatusForDonations(Arrays.asList(donationIds.split(",")));
    
    @SuppressWarnings("unchecked")
    LinkedHashMap<String, Donation> donations = (LinkedHashMap<String, Donation>)results.get("donations");

    // depend on the getBloodTypingTestStatus() method to return donations, blood typing output as
    // a linked hashmap so that iteration is done in the same order as the donations in the well
    map.put("donations", getDonationViewModels(donations));
    map.put("overview", results.get("bloodTestingResults"));
    map.put("success", true);
    return map;
  }
  
  private Map<String, DonationViewModel> getDonationViewModels(LinkedHashMap<String, Donation> donations) {
    if (donations == null)
      return null;
    Map<String, DonationViewModel> donationViewModels = new LinkedHashMap<String, DonationViewModel>();    
    for (Map.Entry<String, Donation> entry : donations.entrySet())
    {
        donationViewModels.put(entry.getKey(), new DonationViewModel(entry.getValue()));
    }
    return donationViewModels;
  }

  @RequestMapping(value="/results/{donationId}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_BLOOD_TYPING_OUTCOME+"')")
  public Map<String, Object> showBloodTypingResultsForDonation(
      @PathVariable Long donationId) {
      
    Map<String, Object> map = new HashMap<String, Object>();
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult ruleResult = bloodTestingRepository.getAllTestsStatusForDonation(donationId);
    map.put("donation", new DonationViewModel(donation));
    map.put("overview", ruleResult);

    return map;
  }
  
  @RequestMapping(value="/results/additional", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_BLOOD_TYPING_OUTCOME+"')")
  public ResponseEntity<Map<String, Object>> saveAdditionalBloodTypingTests(
      @RequestBody TestResultBackingForm form) {

    Map<String, Object> m = new HashMap<String, Object>();
    HttpStatus httpStatus = HttpStatus.CREATED;
    
      Map<Long, Map<Long, String>> bloodTypingTestResultsMap = new HashMap<Long, Map<Long,String>>();
      Map<Long, String> saveTestsDataWithLong = new HashMap<Long, String>();
      Map<Long, String> saveTestsData = form.getTestResults();
       Donation donation = donationRepository.verifyDonationIdentificationNumber(form.getDonationIdentificationNumber());
      for (Long testIdStr : saveTestsData.keySet()) {
        saveTestsDataWithLong.put(testIdStr, saveTestsData.get(testIdStr));
      }
      bloodTypingTestResultsMap.put(donation.getId(), saveTestsDataWithLong);
      Map<String, Object> results = bloodTestingRepository.saveBloodTestingResults(bloodTypingTestResultsMap, form.getSaveUninterpretableResults());
      @SuppressWarnings("unchecked")
      Map<Long, Object> errorMap = (Map<Long, Object>) results.get("errors");
      if (errorMap != null && !errorMap.isEmpty()) {
        httpStatus = HttpStatus.BAD_REQUEST;
        @SuppressWarnings("unchecked")
        Map<Long, String> errorsForDonation = (Map<Long, String>) errorMap.get(donation.getId());
        if (errorsForDonation != null && errorsForDonation.size() == 1 && errorsForDonation.containsKey((long)-1))
          m.put("uninterpretable", true);
        else
          m.put("invalidResults", true);
      }

    return new ResponseEntity<Map<String, Object>>(m, httpStatus);
  }
  
}
