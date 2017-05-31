package org.jembi.bsis.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.controllerservice.LabellingControllerService;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    return map;
  }

  @RequestMapping(value = "/donations/{din}/components", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.LABEL_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> findlotRelease(@PathVariable String din,
      @RequestParam(required = true, value = "componentType") UUID componentTypeId) {
    Map<String, Object> componentMap = new HashMap<String, Object>();
    componentMap.put("donationNumber", din);
    componentMap.put("components", labellingControllerService.getComponentsForLabelling(din, componentTypeId));
    return new ResponseEntity<>(componentMap, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/components", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.LABEL_COMPONENT + "')")
  public Map<String, Object> findSafeComponents(@RequestParam(required = false) String donationIdentificationNumber, 
      @RequestParam(required = false) String componentCode, 
      @RequestParam(required = false) UUID componentTypeId, 
      @RequestParam(required = false) UUID locationId,
      @RequestParam(required = false) List<String> bloodGroups, 
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate, 
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate, 
      @RequestParam(required = false) InventoryStatus inventoryStatus) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("components", labellingControllerService.findSafeComponentsToLabel(donationIdentificationNumber, componentCode, componentTypeId, locationId,
        bloodGroups, startDate, endDate, inventoryStatus));
    return map;
  }

  @RequestMapping(value = "/print/packlabel/{componentId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.LABEL_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> printLabel(@PathVariable UUID componentId) throws IOException {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("labelZPL", labellingControllerService.printPackLabel(componentId));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/verify/packlabel", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.LABEL_COMPONENT + "')")
  public Map<String, Object> verifyLabel(
      @RequestParam(required = true, value = "componentId") UUID componentId,
      @RequestParam(required = true, value = "prePrintedDIN") String prePrintedDIN,
      @RequestParam(required = true, value = "packLabelDIN") String packLabelDIN) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("labelVerified", labellingControllerService.verifyPackLabel(componentId, prePrintedDIN, packLabelDIN));
    return map;
  }

  @RequestMapping(value = "/print/discardlabel/{componentId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.LABEL_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> printDiscard(@PathVariable UUID componentId) throws IOException {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("labelZPL", labellingControllerService.printDiscardLabel(componentId));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

}
