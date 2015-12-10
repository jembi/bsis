package controller;

import backingform.PackTypeBackingForm;
import backingform.validator.PackTypeBackingFormValidator;
import model.packtype.PackType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import repository.PackTypeRepository;
import utils.PermissionConstants;
import viewmodel.PackTypeViewModel;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("packtypes")
public class PackTypeController {

  private static final Logger LOGGER = Logger.getLogger(PackTypeController.class);

  @Autowired
  PackTypeRepository packTypeRepository;

  @Autowired
  private UtilController utilController;

  public PackTypeController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new PackTypeBackingFormValidator(binder.getValidator(), utilController));
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_PACK_TYPES + "')")
  public Map<String, Object> getPackTypes() {
    Map<String, Object> map = new HashMap<>();
    addAllPackTypesToModel(map);
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_PACK_TYPES + "')")
  public ResponseEntity<PackType> getPackTypeById(@PathVariable Integer id) {
    Map<String, Object> map = new HashMap<>();
    PackType packType = packTypeRepository.getPackTypeById(id);
    map.put("packtype", new PackTypeViewModel(packType));
    return new ResponseEntity(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_PACK_TYPES + "')")
  public ResponseEntity savePackType(@Valid @RequestBody PackTypeBackingForm formData) {
    PackType packType = formData.getType();
    packType = packTypeRepository.savePackType(packType);
    return new ResponseEntity(new PackTypeViewModel(packType), HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_PACK_TYPES + "')")
  public ResponseEntity updatePackType(@Valid @RequestBody PackTypeBackingForm formData, @PathVariable Integer id) {
    Map<String, Object> map = new HashMap<>();
    PackType packType = formData.getType();
    packType.setId(id);
    packType = packTypeRepository.updatePackType(packType);
    map.put("packtype", new PackTypeViewModel(packType));
    return new ResponseEntity(map, HttpStatus.OK);
  }

  private void addAllPackTypesToModel(Map<String, Object> m) {
    m.put("allPackTypes", getPackTypeViewModels(packTypeRepository.getAllPackTypes()));
  }

  private List<PackTypeViewModel> getPackTypeViewModels(List<PackType> packTypes) {

    List<PackTypeViewModel> viewModels = new ArrayList<>();
    for (PackType packtType : packTypes) {
      viewModels.add(new PackTypeViewModel(packtType));
    }
    return viewModels;
  }

}
