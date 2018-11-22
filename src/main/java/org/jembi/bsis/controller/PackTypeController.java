package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.backingform.PackTypeBackingForm;
import org.jembi.bsis.backingform.validator.PackTypeBackingFormValidator;
import org.jembi.bsis.controllerservice.PackTypeControllerService;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.PackTypeFullViewModel;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("packtypes")
public class PackTypeController {

  @Autowired
  private PackTypeBackingFormValidator packTypeBackingFormValidator;
  @Autowired
  private PackTypeControllerService packTypeControllerService;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(packTypeBackingFormValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_PACK_TYPES + "')")
  public Map<String, Object> getPackTypes() {
    Map<String, Object> map = new HashMap<>();
    map.put("allPackTypes", packTypeControllerService.getAllPackTypes());
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_PACK_TYPES + "')")
  public ResponseEntity<Map<String, Object>> getPackTypeById(@PathVariable UUID id) {
    Map<String, Object> map = new HashMap<>();
    map.put("packtype", packTypeControllerService.getPackTypeById(id));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_PACK_TYPES + "')")
  public ResponseEntity<PackTypeFullViewModel> savePackType(@Valid @RequestBody PackTypeBackingForm formData) {
    PackTypeFullViewModel packType = packTypeControllerService.createPackType(formData);
    return new ResponseEntity<>(packType, HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_PACK_TYPES + "')")
  public ResponseEntity<Map<String, Object>> updatePackType(@Valid @RequestBody PackTypeBackingForm formData,
      @PathVariable UUID id) {
    // Use the id from the path
    formData.setId(id);
    Map<String, Object> map = new HashMap<>();
    map.put("packtype", packTypeControllerService.updatePackType(formData));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

}
