package org.jembi.bsis.controller.bloodtesting;

import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.controllerservice.BloodTestingRuleControllerService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bloodtestingrules")
public class BloodTestingRuleController {

  @Autowired
  private BloodTestingRuleControllerService bloodTestingRuleControllerService;
  
  @RequestMapping(method = RequestMethod.GET, value = "/search")
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTING_RULES + "')")
  public ResponseEntity<Map<String, Object>> getBloodTestingRules() {

    Map<String, Object> map = new HashMap<>();
    map.put("bloodtestingrules", bloodTestingRuleControllerService.getAllBloodTestingRules(true));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
}