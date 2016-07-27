package org.jembi.bsis.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.DiscardComponentsBackingForm;
import org.jembi.bsis.backingform.RecordComponentBackingForm;
import org.jembi.bsis.backingform.UndiscardComponentsBackingForm;
import org.jembi.bsis.backingform.validator.DiscardComponentsBackingFormValidator;
import org.jembi.bsis.controllerservice.ComponentControllerService;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.ComponentFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("components")
public class ComponentController {

  @Autowired
  private ComponentControllerService componentControllerService;

  @Autowired
  private DiscardComponentsBackingFormValidator discardComponentsBackingFormValidator;

  @InitBinder("discardComponentsBackingForm")
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(discardComponentsBackingFormValidator);
  }

  @RequestMapping(value = "/discard/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.DISCARD_COMPONENT + "')")
  public Map<String, Object> discardComponentsFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("discardReasons", componentControllerService.getDiscardReasons());
    map.put("discardComponentsForm", new DiscardComponentsBackingForm());
    return map;
  }

  @RequestMapping(value = "/discard", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.DISCARD_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> discardComponents(
      @Valid @RequestBody DiscardComponentsBackingForm discardComponentsBackingForm) {
    componentControllerService.discardComponents(discardComponentsBackingForm);
    return new ResponseEntity<>(HttpStatus.OK);
  }
  
  @RequestMapping(value = "/undiscard", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.DISCARD_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> undiscardComponents(@RequestBody UndiscardComponentsBackingForm backingForm) {
    Map<String, Object> map = new HashMap<>();
    map.put("components", componentControllerService.undiscardComponents(backingForm.getComponentIds()));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> findComponent(
      @RequestParam(required = true) String componentCode,
      @RequestParam(required = true) String donationIdentificationNumber) {

    Map<String, Object> map = new HashMap<>();
    map.put("component", componentControllerService.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber));
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> componentSummaryGenerator(@PathVariable Long id) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("componentTypes", componentControllerService.getComponentTypes());
    map.put("component", componentControllerService.findComponentById(id));
    return map;
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT_INFORMATION + "')")
  public Map<String, Object> getFindComponentForm() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("componentTypes", componentControllerService.getComponentTypes());
    map.put("returnReasons", componentControllerService.getReturnReasons());
    map.put("discardReasons", componentControllerService.getDiscardReasons());
    map.put("recordComponentForm", new RecordComponentBackingForm());
    return map;
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> findComponentPagination(HttpServletRequest request,
      @RequestParam(value = "componentNumber", required = false, defaultValue = "") String componentNumber,
      @RequestParam(value = "donationIdentificationNumber", required = false, defaultValue = "") String donationIdentificationNumber,
      @RequestParam(value = "componentTypes", required = false, defaultValue = "") List<Long> componentTypeIds,
      @RequestParam(value = "status", required = false) List<ComponentStatus> statuses,
      @RequestParam(value = "donationDateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date donationDateFrom,
      @RequestParam(value = "donationDateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date donationDateTo) {

    List<ComponentFullViewModel> components;
    
    if (StringUtils.isBlank(donationIdentificationNumber)) {
      components = componentControllerService.findAnyComponent(componentTypeIds, statuses, donationDateFrom, donationDateTo);
    } else {
      components = componentControllerService.findComponentsByDonationIdentificationNumber(donationIdentificationNumber);
    }

    Map<String, Object> map = new HashMap<>();
    map.put("components", components);
    return map;
  }

  @RequestMapping(value = "/donations/{donationNumber}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_COMPONENT + "')")
  public Map<String, Object> findComponentByDonationIdentificationNumber(
      @PathVariable String donationNumber) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("components", componentControllerService.findManagementComponentsByDonationIdentificationNumber(donationNumber));
    return map;
  }

  @RequestMapping(value = "/recordcombinations", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> recordNewComponentCombinations(
      @RequestBody RecordComponentBackingForm recordComponentForm) throws ParseException {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("components", componentControllerService.processComponent(recordComponentForm));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
  }
  
  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> updateComponent(
      @PathVariable("id") Long componentId,
      @RequestBody @Valid ComponentBackingForm componentBackingForm) {

    componentBackingForm.setId(componentId); // Use the id parameter from the path

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("component", componentControllerService.updateComponent(componentBackingForm));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }
  
  @RequestMapping(value = "{id}/unprocess", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> unprocessComponent(
      @PathVariable("id") Long componentId) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("component", componentControllerService.unprocessComponent(componentId));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }
}
