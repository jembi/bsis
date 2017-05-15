package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.jembi.bsis.backingform.BloodTestingRuleBackingForm;
import org.jembi.bsis.backingform.validator.BloodTestingRuleBackingFormValidator;
import org.jembi.bsis.controllerservice.BloodTestingRuleControllerService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bloodtestingrules")
public class BloodTestingRuleController {

  @Autowired
  private BloodTestingRuleControllerService bloodTestingRuleControllerService;

  @Autowired
  private BloodTestingRuleBackingFormValidator bloodTestingRuleBackingFormValidator;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(bloodTestingRuleBackingFormValidator);
  }
  
  @RequestMapping(method = RequestMethod.GET, value = "/search")
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTING_RULES + "')")
  public Map<String, Object> getBloodTestingRules() {
    Map<String, Object> map = new HashMap<>();
    map.put("bloodTestingRules", bloodTestingRuleControllerService.getAllBloodTestingRules());
    return map;
  }
  
  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTING_RULES + "')")
  public Map<String, Object> getBloodTestingRuleById(@PathVariable UUID id) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("bloodTestingRule", bloodTestingRuleControllerService.findBloodTestingRuleById(id));
    return map;
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTING_RULES + "')")
  public Map<String, Object> getBloodTestingRuleForm(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("bloodTests", bloodTestingRuleControllerService.getAllBloodTests());
    map.put("donationFields", bloodTestingRuleControllerService.getDonationFieldsForBloodTestCategory());
    map.put("newInformation", bloodTestingRuleControllerService.getNewInformationForDonationFields());
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTING_RULES + "')")
  public Map<String, Object> createBloodTestingRule(@Valid @RequestBody BloodTestingRuleBackingForm bloodTestingRuleBackingForm) {
    Map<String, Object> map = new HashMap<>();
    map.put("bloodTestingRule", bloodTestingRuleControllerService.createBloodTestingRule(bloodTestingRuleBackingForm));
    return map;
  }
  
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_BLOOD_TESTING_RULES + "')")
  public Map<String, Object> updateBloodTestingRule(@PathVariable("id") UUID id, 
      @Valid @RequestBody BloodTestingRuleBackingForm bloodTestingRuleBackingform) {
    // set the id parameter from the path
    bloodTestingRuleBackingform.setId(id);
    Map<String, Object> map = new HashMap<>();
    map.put("bloodTestingRule", bloodTestingRuleControllerService.updateBloodTestinRule(bloodTestingRuleBackingform));
    return map;
  }
}