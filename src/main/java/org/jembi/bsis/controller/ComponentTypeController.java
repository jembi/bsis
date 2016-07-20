package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.controllerservice.ComponentTypeControllerService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("componenttypes")
public class ComponentTypeController {

  @Autowired
  private ComponentTypeControllerService componentTypeControllerService;

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity<Map<String, Object>> getComponentTypes(
      @RequestParam(required = false, defaultValue = "true") boolean includeDeleted) {

    Map<String, Object> map = new HashMap<>();
    map.put("componentTypes", componentTypeControllerService.getComponentTypes(includeDeleted));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity<Map<String, Object>> getComponentTypeById(@PathVariable Long id) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("componentType", componentTypeControllerService.getComponentType(id));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity<Map<String, Object>> saveComponentType(@Valid @RequestBody ComponentTypeBackingForm form) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("componentType", componentTypeControllerService.addComponentType(form));
    return new ResponseEntity<>(map, HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_COMPONENT_TYPES + "')")
  public ResponseEntity<Map<String, Object>> updateComponentType(@PathVariable Long id,
      @Valid @RequestBody ComponentTypeBackingForm form) {

    // use the id from the path
    form.setId(id);
    
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("componentType", componentTypeControllerService.updateComponentType(form));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
}
