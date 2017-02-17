package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.controllerservice.TransfusionReactionTypeControllerService;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transfusionreactiontypes")
public class TransfusionReactionTypeController {

  @Autowired
  private TransfusionReactionTypeControllerService transfusionReactionTypeControllerService;

  @RequestMapping(method = RequestMethod.GET, value = "/search")
  @PreAuthorize("hasAnyRole('" + PermissionConstants.MANAGE_TRANSFUSION_REACTION_TYPES + "')")
  public ResponseEntity<Map<String, Object>> getTransfusionReactionTypes() {
    Map<String, Object> map = new HashMap<>();
    map.put("transfusionReactionTypes", transfusionReactionTypeControllerService.getTransfusionReactionTypes());
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

}
