package controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import service.LabellingCRUDService;
import utils.PermissionConstants;

@RestController
@RequestMapping("labels")
public class LabellingController {

  @Autowired
  private LabellingCRUDService labellingService;

  @RequestMapping(value = "/status/{donationIdentificationNumber}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DISCARDS + "')")
  public ResponseEntity findlotRelease(HttpServletRequest request, @PathVariable String donationIdentificationNumber,
      @RequestParam(required = true, value = "componentType") long componentTypeId) {
    Map<String, Object> componentMap = new HashMap<String, Object>();
    List<Map<String, Object>> componentStatuses = labellingService.findlotRelease(donationIdentificationNumber, componentTypeId);
    componentMap.put("donationNumber", donationIdentificationNumber);
    componentMap.put("components", new HashSet(componentStatuses));
    return new ResponseEntity(componentMap, HttpStatus.OK);
  }

  @RequestMapping(value = "/print/packlabel/{componentId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.ISSUE_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> printLabel(@PathVariable Long componentId) {
    Map<String, Object> map = new HashMap<String, Object>();
    String labelZPL = labellingService.printPackLabel(componentId);
    map.put("labelZPL", labelZPL);
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/print/discardlabel/{componentId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.ISSUE_COMPONENT + "')")
  public ResponseEntity<Map<String, Object>> printDiscard(@PathVariable Long componentId) {
    Map<String, Object> map = new HashMap<String, Object>();
    String labelZPL = labellingService.printDiscardLabel(componentId);
    map.put("labelZPL", labelZPL);
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

}
