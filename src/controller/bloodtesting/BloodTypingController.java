package controller.bloodtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestType;
import model.donation.Donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
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
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_BLOOD_TYPING_OUTCOME + "')")
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

  private List<BloodTestViewModel> getBasicBloodTypingTests() {
    List<BloodTestViewModel> tests = new ArrayList<BloodTestViewModel>();
    for (BloodTest rawBloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)) {
      tests.add(new BloodTestViewModel(rawBloodTest));
    }
    return tests;
  }

  private List<BloodTestViewModel> getAdvancedBloodTypingTests() {
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

  @RequestMapping(value = "/results/{donationId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_BLOOD_TYPING_OUTCOME + "')")
  public Map<String, Object> showBloodTypingResultsForDonation(
      @PathVariable Long donationId) {

    Map<String, Object> map = new HashMap<String, Object>();
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult ruleResult = bloodTestingRepository.getAllTestsStatusForDonation(donationId);
    map.put("donation", new DonationViewModel(donation));
    map.put("overview", ruleResult);

    return map;
  }
}
