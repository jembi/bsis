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
@RequestMapping("ttitests")
public class TTIController {

  @Autowired
  private BloodTestRepository bloodTestRepository;

  @Autowired
  private BloodTestFactory bloodTestFactory;

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_TTI_OUTCOME + "')")
  public Map<String, Object> getTTIForm(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();

    List<BloodTestFullViewModel> basicTTITestNames = getBasicTTITests();
    map.put("basicTTITestNames", basicTTITestNames);

    List<BloodTestFullViewModel> repeatTTITestNames = getRepeatTTITests();
    map.put("repeatTTITestNames", repeatTTITestNames);

    List<BloodTestFullViewModel> confirmatoryTTITestNames = getConfirmatoryTTITests();
    map.put("confirmatoryTTITestNames", confirmatoryTTITestNames);

    return map;
  }

  private List<BloodTestFullViewModel> getRepeatTTITests() {
    List<BloodTestFullViewModel> tests = new ArrayList<BloodTestFullViewModel>();
    for (BloodTest bloodTest : bloodTestRepository.getBloodTestsOfType(BloodTestType.REPEAT_TTI)) {
      tests.add(bloodTestFactory.createFullViewModel(bloodTest));
    }
    return tests;
  }

  private List<BloodTestFullViewModel> getBasicTTITests() {
    List<BloodTestFullViewModel> tests = new ArrayList<BloodTestFullViewModel>();
    for (BloodTest bloodTest : bloodTestRepository
        .getBloodTestsOfType(BloodTestType.BASIC_TTI)) {
      tests.add(bloodTestFactory.createFullViewModel(bloodTest));
    }
    return tests;
  }

  private List<BloodTestFullViewModel> getConfirmatoryTTITests() {
    List<BloodTestFullViewModel> tests = new ArrayList<BloodTestFullViewModel>();
    for (BloodTest bloodTest : bloodTestRepository
        .getBloodTestsOfType(BloodTestType.CONFIRMATORY_TTI)) {
      tests.add(bloodTestFactory.createFullViewModel(bloodTest));
    }
    return tests;
  }
}
