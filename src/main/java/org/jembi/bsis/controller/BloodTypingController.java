package org.jembi.bsis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jembi.bsis.factory.BloodTestFactory;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bloodgroupingtests")
public class BloodTypingController {

  @Autowired
  private BloodTestRepository bloodTestRepository;

  @Autowired
  private BloodTestFactory bloodTestFactory;

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_BLOOD_TYPING_OUTCOME + "')")
  public Map<String, Object> getBloodTypingForm(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();

    List<BloodTestFullViewModel> basicBloodTypingTests = getBasicBloodTypingTests();
    map.put("basicBloodTypingTests", basicBloodTypingTests);

    List<BloodTestFullViewModel> repeatBloodTypingTests = getRepeatBloodTypingTests();
    map.put("repeatBloodTypingTests", repeatBloodTypingTests);

    return map;
  }

  private List<BloodTestFullViewModel> getBasicBloodTypingTests() {
    List<BloodTestFullViewModel> tests = new ArrayList<BloodTestFullViewModel>();
    for (BloodTest bloodTest : bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)) {
      tests.add(bloodTestFactory.createFullViewModel(bloodTest));
    }
    return tests;
  }

  public List<BloodTestFullViewModel> getRepeatBloodTypingTests() {
    List<BloodTestFullViewModel> tests = new ArrayList<BloodTestFullViewModel>();
    for (BloodTest bloodTest : bloodTestRepository.getBloodTestsOfType(BloodTestType.REPEAT_BLOODTYPING)) {
      tests.add(bloodTestFactory.createFullViewModel(bloodTest));
    }
    return tests;
  }
}
