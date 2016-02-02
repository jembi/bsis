package controller.bloodtesting;

import backingform.TestResultBackingForm;
import controller.UtilController;
import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestType;
import model.donation.Donation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import repository.DonationRepository;
import repository.GenericConfigRepository;
import repository.bloodtesting.BloodTestingRepository;
import utils.PermissionConstants;
import viewmodel.BloodTestViewModel;
import viewmodel.BloodTestingRuleResult;
import viewmodel.DonationViewModel;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("bloodgroupingtests")
public class BloodTypingController {

  @Autowired
  private UtilController utilController;

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  public BloodTypingController() {
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
      reqUrl += "?" + queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_BLOOD_TYPING_OUTCOME + "')")
  public Map<String, Object> getBloodTypingForm(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<>();

    List<BloodTestViewModel> basicBloodTypingTests = getBasicBloodTypingTests();
    map.put("basicBloodTypingTests", basicBloodTypingTests);

    List<BloodTestViewModel> advancedBloodTypingTests = getAdvancedBloodTypingTests();
    map.put("advancedBloodTypingTests", advancedBloodTypingTests);

    return map;
  }

  private List<BloodTestViewModel> getBasicBloodTypingTests() {
    List<BloodTestViewModel> tests = new ArrayList<>();
    for (BloodTest rawBloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)) {
      tests.add(new BloodTestViewModel(rawBloodTest));
    }
    return tests;
  }

  private List<BloodTestViewModel> getAdvancedBloodTypingTests() {
    List<BloodTestViewModel> tests = new ArrayList<>();
    for (BloodTest rawBloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.ADVANCED_BLOODTYPING)) {
      tests.add(new BloodTestViewModel(rawBloodTest));
    }
    return tests;
  }

  @RequestMapping(value = "/batchresults/{donationIds}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_BLOOD_TYPING_OUTCOME + "')")
  public Map<String, Object> getBloodTypingStatusForDonations(
      @PathVariable String donationIds) {
    Map<String, Object> map = new HashMap<>();
    Map<String, Object> results = bloodTestingRepository.getAllTestsStatusForDonations(Arrays.asList(donationIds.split(",")));

    LinkedHashMap<String, Donation> donations = (LinkedHashMap<String, Donation>) results.get("donations");

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
    Map<String, DonationViewModel> donationViewModels = new LinkedHashMap<>();
    for (Map.Entry<String, Donation> entry : donations.entrySet()) {
      donationViewModels.put(entry.getKey(), new DonationViewModel(entry.getValue()));
    }
    return donationViewModels;
  }

  @RequestMapping(value = "/results/{donationId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_BLOOD_TYPING_OUTCOME + "')")
  public Map<String, Object> showBloodTypingResultsForDonation(
      @PathVariable Long donationId) {

    Map<String, Object> map = new HashMap<>();
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult ruleResult = bloodTestingRepository.getAllTestsStatusForDonation(donationId);
    map.put("donation", new DonationViewModel(donation));
    map.put("overview", ruleResult);

    return map;
  }

  @RequestMapping(value = "/results/additional", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_BLOOD_TYPING_OUTCOME + "')")
  public ResponseEntity<Map<String, Object>> saveAdditionalBloodTypingTests(
      @RequestBody TestResultBackingForm form) {

    Map<String, Object> m = new HashMap<>();
    HttpStatus httpStatus = HttpStatus.CREATED;

    Map<Long, Map<Long, String>> bloodTypingTestResultsMap = new HashMap<Long, Map<Long, String>>();
    Map<Long, String> saveTestsDataWithLong = new HashMap<Long, String>();
    @SuppressWarnings("unchecked")
    Map<Long, String> saveTestsData = null;
    saveTestsData = form.getTestResults();
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
      if (errorsForDonation != null && errorsForDonation.size() == 1 && errorsForDonation.containsKey((long) -1))
        m.put("uninterpretable", true);
      else
        m.put("invalidResults", true);
    }

    return new ResponseEntity<>(m, httpStatus);
  }

}
