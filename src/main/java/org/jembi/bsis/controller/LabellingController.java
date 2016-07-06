package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.controllerservice.LabellingControllerService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("labels")
public class LabellingController {

  @Autowired
  private LabellingControllerService labellingControllerService;
  
  @RequestMapping(value = "/components/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.LABEL_COMPONENT + "')")
  public Map<String, Object> findComponentFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("componentTypes", labellingControllerService.getComponentTypes());
    return map;
  }

  @RequestMapping(value = "/components", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.LABEL_COMPONENT + "')")
  public ResponseEntity findlotRelease(
      @RequestParam(required = true, value = "donationIdentificationNumber") String donationIdentificationNumber,
      @RequestParam(required = true, value = "componentType") long componentTypeId) {
    Map<String, Object> componentMap = new HashMap<String, Object>();
    componentMap.put("donationNumber", donationIdentificationNumber);
    componentMap.put("components", labellingControllerService.getComponentsLabelling(donationIdentificationNumber, componentTypeId));
    return new ResponseEntity(componentMap, HttpStatus.OK);
  }

  @RequestMapping(value = "/print/packlabel/{componentId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.LABEL_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> printLabel(@PathVariable Long componentId) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("labelZPL", labellingControllerService.printPackLabel(componentId));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/print/discardlabel/{componentId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DISCARDS + "')")
  public ResponseEntity<Map<String, Object>> printDiscard(@PathVariable Long componentId) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("labelZPL", labellingControllerService.printDiscardLabel(componentId));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

}
