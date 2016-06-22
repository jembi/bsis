package org.jembi.bsis.controller.bloodtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("ttitests")
public class TTIController {

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_TTI_OUTCOME + "')")
  public Map<String, Object> getTTIForm(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();

    List<BloodTestViewModel> basicTTITests = getBasicTTITests();
    map.put("basicTTITests", basicTTITests);

    List<BloodTestViewModel> pendingTTITests = getConfirmatoryTTITests();
    map.put("pendingTTITests", pendingTTITests);

    return map;
  }

  private List<BloodTestViewModel> getBasicTTITests() {
    List<BloodTestViewModel> tests = new ArrayList<BloodTestViewModel>();
    for (BloodTest rawBloodTest : bloodTestingRepository
        .getBloodTestsOfType(BloodTestType.BASIC_TTI)) {
      tests.add(new BloodTestViewModel(rawBloodTest));
    }
    return tests;
  }

  private List<BloodTestViewModel> getConfirmatoryTTITests() {
    List<BloodTestViewModel> tests = new ArrayList<BloodTestViewModel>();
    for (BloodTest rawBloodTest : bloodTestingRepository
        .getBloodTestsOfType(BloodTestType.CONFIRMATORY_TTI)) {
      tests.add(new BloodTestViewModel(rawBloodTest));
    }
    return tests;
  }
}