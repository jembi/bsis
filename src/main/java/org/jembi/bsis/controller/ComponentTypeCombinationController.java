package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.backingform.validator.ComponentTypeCombinationBackingFormValidator;
import org.jembi.bsis.controllerservice.ComponentTypeCombinationControllerService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("componenttypecombinations")
public class ComponentTypeCombinationController {

  @Autowired
  private ComponentTypeCombinationControllerService componentTypeCombinationControllerService;

  @Autowired
  private ComponentTypeCombinationBackingFormValidator componentTypeCombinationBackingFormValidator;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(componentTypeCombinationBackingFormValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_COMBINATIONS + "')")
  public ResponseEntity<Map<String, Object>> getComponentTypeCombinations(
          @RequestParam(required = false, defaultValue = "true") boolean includeDeleted) {

    Map<String, Object> map = new HashMap<>();
    map.put("componentTypeCombinations", componentTypeCombinationControllerService.getComponentTypeCombinations(includeDeleted));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
}
