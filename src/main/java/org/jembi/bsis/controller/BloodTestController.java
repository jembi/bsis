package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.controllerservice.BloodTestControllerService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bloodtests")
public class BloodTestController {

  @Autowired
  private BloodTestControllerService bloodTestControllerService;

  @RequestMapping(method = RequestMethod.GET, value = "/search")
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTS + "')")
  public ResponseEntity<Map<String, Object>> getBloodTests() {
    Map<String, Object> map = new HashMap<>();
    map.put("bloodTests", bloodTestControllerService.getAllBloodTests());
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/form")
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTS + "')")
  public ResponseEntity<Map<String, Object>> getBloodTestForm() {
    Map<String, Object> map = new HashMap<>();
    map.put("categories", bloodTestControllerService.getCategories());
    map.put("types", bloodTestControllerService.getTypes());
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
}
