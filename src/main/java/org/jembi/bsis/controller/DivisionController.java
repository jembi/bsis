package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.controllerservice.DivisionControllerService;
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
@RequestMapping("divisions")
public class DivisionController {
  
  @Autowired
  private DivisionControllerService divisionControllerService;

  @RequestMapping(method = RequestMethod.GET, value = "/search")
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DIVISIONS + "')")
  public ResponseEntity<Map<String, Object>> findDivisions(
      @RequestParam(required = true) String name,
      @RequestParam(required = true, defaultValue = "false") boolean includeSimilarResults,
      @RequestParam(required = false) Integer level) {
    
    Map<String, Object> map = new HashMap<>();
    map.put("divisions", divisionControllerService.findDivisions(name, includeSimilarResults, level));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DIVISIONS + "')")
  public ResponseEntity<Map<String, Object>> findDivisionById(@PathVariable("id") long id) {
    Map<String, Object> map = new HashMap<>();
    map.put("division", divisionControllerService.findDivisionById(id));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

}
