package org.jembi.bsis.controller.bloodtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jembi.bsis.factory.BloodTestFactory;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.BloodTestViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bloodgroupingtests")
public class BloodTypingController {

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  @Autowired
  private BloodTestFactory bloodTestFactory;

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
    for (BloodTest bloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)) {
      tests.add(bloodTestFactory.createViewModel(bloodTest));
    }
    return tests;
  }

  private List<BloodTestViewModel> getAdvancedBloodTypingTests() {
    List<BloodTestViewModel> tests = new ArrayList<BloodTestViewModel>();
    for (BloodTest bloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.ADVANCED_BLOODTYPING)) {
      tests.add(bloodTestFactory.createViewModel(bloodTest));
    }
    return tests;
  }

  public List<BloodTestViewModel> getRepeatBloodTypingTests() {
    List<BloodTestViewModel> tests = new ArrayList<BloodTestViewModel>();
    for (BloodTest bloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.REPEAT_BLOODTYPING)) {
      tests.add(bloodTestFactory.createViewModel(bloodTest));
    }
    return tests;
  }
}
