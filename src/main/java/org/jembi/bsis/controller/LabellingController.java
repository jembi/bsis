package org.jembi.bsis.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.controllerservice.LabellingControllerService;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.util.BloodGroup;
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
    map.put("locations", labellingControllerService.getLocations());
    map.put("bloodGroups", BloodGroup.getBloodgroups());
    return map;
  }

  @RequestMapping(value = "/donations/{din}/components", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.LABEL_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> findlotRelease(@PathVariable String din,
      @RequestParam(required = true, value = "componentType") long componentTypeId) {
    Map<String, Object> componentMap = new HashMap<String, Object>();
    componentMap.put("donationNumber", din);
    componentMap.put("components", labellingControllerService.getComponentsForLabelling(din, componentTypeId));
    return new ResponseEntity<>(componentMap, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/components", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.LABEL_COMPONENT + "')")
  public Map<String, Object> findSafeComponents(@RequestParam(required = false) String din, 
      @RequestParam(required = false) String componentCode, 
      @RequestParam(required = false) Long componentTypeId, 
      @RequestParam(required = false) Long locationId,
      @RequestParam(required = false) List<String> bloodGroups, 
      @RequestParam(required = false) Date startDate, 
      @RequestParam(required = false) Date endDate, 
      @RequestParam(required = false) InventoryStatus inventoryStatus) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("components", labellingControllerService.findSafeComponents(din, componentCode, componentTypeId, locationId,
        bloodGroups, startDate, endDate, inventoryStatus));
    return map;
  }

  @RequestMapping(value = "/print/packlabel/{componentId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.LABEL_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> printLabel(@PathVariable long componentId) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("labelZPL", labellingControllerService.printPackLabel(componentId));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/verify/packlabel", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.LABEL_COMPONENT + "')")
  public Map<String, Object> verifyLabel(
      @RequestParam(required = true, value = "componentId") long componentId,
      @RequestParam(required = true, value = "prePrintedDIN") String prePrintedDIN,
      @RequestParam(required = true, value = "packLabelDIN") String packLabelDIN) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("labelVerified", labellingControllerService.verifyPackLabel(componentId, prePrintedDIN, packLabelDIN));
    return map;
  }

  @RequestMapping(value = "/print/discardlabel/{componentId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.LABEL_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> printDiscard(@PathVariable long componentId) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("labelZPL", labellingControllerService.printDiscardLabel(componentId));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

}
