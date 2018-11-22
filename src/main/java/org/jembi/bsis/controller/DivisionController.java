package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.backingform.DivisionBackingForm;
import org.jembi.bsis.backingform.validator.DivisionBackingFormValidator;
import org.jembi.bsis.controllerservice.DivisionControllerService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("divisions")
public class DivisionController {
  
  @Autowired
  private DivisionControllerService divisionControllerService;

  @Autowired
  private DivisionBackingFormValidator divisionBackingFormValidator;

  @InitBinder("divisionBackingForm")
  protected void initDivisionFormBinder(WebDataBinder binder) {
    binder.setValidator(divisionBackingFormValidator);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DIVISIONS + "')")
  public ResponseEntity<Map<String, Object>> addDivision(
      @RequestBody @Valid DivisionBackingForm form) {
    Map<String, Object> map = new HashMap<>();
    map.put("division", divisionControllerService.createDivision(form));
    return new ResponseEntity<>(map, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/search")
  @PreAuthorize("hasAnyRole('" + PermissionConstants.MANAGE_DIVISIONS + "', '" + PermissionConstants.MANAGE_LOCATIONS + "')")
  public ResponseEntity<Map<String, Object>> findDivisions(
      @RequestParam(required = false) String name,
      @RequestParam(required = true, defaultValue = "false") boolean includeSimilarResults,
      @RequestParam(required = false) Integer level,
      @RequestParam(required = false) UUID parentId) {
    Map<String, Object> map = new HashMap<>();
    map.put("divisions", divisionControllerService.findDivisions(name, includeSimilarResults, level, parentId));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DIVISIONS + "')")
  public ResponseEntity<Map<String, Object>> findDivisionById(@PathVariable("id") UUID id) {
    Map<String, Object> map = new HashMap<>();
    map.put("division", divisionControllerService.findDivisionById(id));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DIVISIONS + "')")
  public ResponseEntity<Map<String, Object>> updateDivision(@PathVariable("id") UUID id,
      @Valid @RequestBody DivisionBackingForm backingForm) {
    
    // Update backing form id to match path
    backingForm.setId(id);
    
    Map<String, Object> map = new HashMap<>();
    map.put("division", divisionControllerService.updateDivision(backingForm));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

}
