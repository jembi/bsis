package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.jembi.bsis.backingform.PackTypeBackingForm;
import org.jembi.bsis.backingform.validator.PackTypeBackingFormValidator;
import org.jembi.bsis.factory.PackTypeFactory;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.PackTypeRepository;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.PackTypeViewFullModel;
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
  PackTypeRepository packTypeRepository;

  @Autowired
  private PackTypeBackingFormValidator packTypeBackingFormValidator;

  @Autowired
  private PackTypeFactory packTypeFactory;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(packTypeBackingFormValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_PACK_TYPES + "')")
  public Map<String, Object> getPackTypes() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("allPackTypes", packTypeFactory.createFullViewModels(packTypeRepository.getAllPackTypes()));
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_PACK_TYPES + "')")
  public ResponseEntity<Map<String, Object>> getPackTypeById(@PathVariable Long id) {
    Map<String, Object> map = new HashMap<String, Object>();
    PackType packType = packTypeRepository.getPackTypeById(id);
    map.put("packtype", packTypeFactory.createFullViewModel(packType));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_PACK_TYPES + "')")
  public ResponseEntity<PackTypeViewFullModel> savePackType(@Valid @RequestBody PackTypeBackingForm formData) {
    PackType packType = packTypeFactory.createEntity(formData);
    packType = packTypeRepository.savePackType(packType);
    return new ResponseEntity<>(packTypeFactory.createFullViewModel(packType), HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_PACK_TYPES + "')")
  public ResponseEntity<Map<String, Object>> updatePackType(@Valid @RequestBody PackTypeBackingForm formData,
      @PathVariable Long id) {
    Map<String, Object> map = new HashMap<String, Object>();
    PackType packType = packTypeFactory.createEntity(formData);
    packType.setId(id);
    packType = packTypeRepository.updatePackType(packType);
    map.put("packtype", packTypeFactory.createFullViewModel(packType));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

}
