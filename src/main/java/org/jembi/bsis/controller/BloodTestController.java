package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.backingform.BloodTestBackingForm;
import org.jembi.bsis.backingform.validator.BloodTestBackingFormValidator;
import org.jembi.bsis.controllerservice.BloodTestControllerService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bloodtests")
public class BloodTestController {

  @Autowired
  private BloodTestControllerService bloodTestControllerService;
  @Autowired
  private BloodTestBackingFormValidator bloodTestBackingFormValidator;
  
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(bloodTestBackingFormValidator);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/search")
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTS + "')")
  public ResponseEntity<Map<String, Object>> getBloodTests() {
    Map<String, Object> map = new HashMap<>();
    map.put("bloodTests", bloodTestControllerService.getAllBloodTests());
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTS + "')")
  public ResponseEntity<Map<String, Object>> getBloodTestById(@PathVariable UUID id) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("bloodTest", bloodTestControllerService.getBloodTestById(id));
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
  
  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTS + "')")
  public ResponseEntity<Map<String, Object>> createBloodTest(@Valid @RequestBody BloodTestBackingForm backingForm) {
    Map<String, Object> map = new HashMap<>();
    map.put("bloodTest", bloodTestControllerService.createBloodTest(backingForm));
    return new ResponseEntity<>(map, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTS + "')")
  public ResponseEntity<Map<String, Object>> updateBloodTest(@PathVariable("id") UUID id,
      @Valid @RequestBody BloodTestBackingForm backingForm) {

    // Use the id parameter from the path
    backingForm.setId(id);

    Map<String, Object> map = new HashMap<>();
    map.put("bloodTest", bloodTestControllerService.updateBloodTest(backingForm));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
}
