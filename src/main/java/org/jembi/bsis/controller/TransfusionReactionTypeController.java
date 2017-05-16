package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.backingform.TransfusionReactionTypeBackingForm;
import org.jembi.bsis.backingform.validator.TransfusionReactionTypeBackingFormValidator;
import org.jembi.bsis.controllerservice.TransfusionReactionTypeControllerService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transfusionreactiontypes")
public class TransfusionReactionTypeController {

  @Autowired
  private TransfusionReactionTypeControllerService transfusionReactionTypeControllerService;
  @Autowired
  private TransfusionReactionTypeBackingFormValidator transfusionReactionTypeBackingFormValidator;
  
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(transfusionReactionTypeBackingFormValidator);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_TRANSFUSION_REACTION_TYPES + "')")
  public Map<String, Object> getTransfusionReactionTypeById(@PathVariable UUID id) {
    Map<String, Object> map = new HashMap<>();
    map.put("transfusionReactionType", transfusionReactionTypeControllerService.getTransfusionReactionType(id));
    return map;
  }
  
  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasAnyRole('" + PermissionConstants.MANAGE_TRANSFUSION_REACTION_TYPES + "')")
  public Map<String, Object> getTransfusionReactionTypes() {
    Map<String, Object> map = new HashMap<>();
    map.put("transfusionReactionTypes", transfusionReactionTypeControllerService.getTransfusionReactionTypes());
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasAnyRole('" + PermissionConstants.MANAGE_TRANSFUSION_REACTION_TYPES + "')")
  @ResponseStatus(HttpStatus.CREATED)
  public Map<String, Object> addTransfusionReactionTypes(@Valid @RequestBody TransfusionReactionTypeBackingForm backingForm) {
    Map<String, Object> map = new HashMap<>();
    map.put("transfusionReactionType", transfusionReactionTypeControllerService.createTransfusionReactionType(backingForm));
    return map;
  }
  
  @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_TRANSFUSION_REACTION_TYPES + "')")
  public Map<String, Object> updateTransfusionReactionTypes(@PathVariable("id") UUID transfusionReactionTypeId,
      @Valid @RequestBody TransfusionReactionTypeBackingForm backingForm) {
    
    backingForm.setId(transfusionReactionTypeId);
    Map<String, Object> map = new HashMap<>();
    map.put("transfusionReactionType", transfusionReactionTypeControllerService.updateTransfusionReactionType(backingForm));
    return map;
  }

}
