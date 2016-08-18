package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.controllerservice.LocationDivisionControllerService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("locationdivisions")
public class LocationDivisionController {
  
  @Autowired
  private LocationDivisionControllerService locationDivisionControllerService;

  @RequestMapping(method = RequestMethod.GET, value = "/search")
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_LOCATION_DIVISIONS + "')")
  public ResponseEntity<Map<String, Object>> findLocationDivisions(
      @RequestParam(required = true) String name,
      @RequestParam(required = true, defaultValue = "false") boolean includeSimilarResults,
      @RequestParam(required = false) Integer level) {
    
    Map<String, Object> map = new HashMap<>();
    map.put("locationDivisions", locationDivisionControllerService.findLocationDivisions(name, includeSimilarResults,
        level));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

}
