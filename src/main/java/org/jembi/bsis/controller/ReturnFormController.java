package org.jembi.bsis.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.backingform.ReturnFormBackingForm;
import org.jembi.bsis.backingform.validator.ReturnFormBackingFormValidator;
import org.jembi.bsis.controllerservice.ReturnFormControllerService;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.returnform.ReturnStatus;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("returnforms")
public class ReturnFormController {

  @Autowired
  private ReturnFormControllerService returnFormControllerService;
  
  @Autowired
  private ReturnFormBackingFormValidator validator;
  
  @Autowired
  private LocationRepository locationRepository;
  
  @Autowired
  private LocationFactory locationFactory;
  
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/form")
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> getOrderFormForm() {
    List<Location> usageSites = locationRepository.getUsageSites();
    List<Location> distributionSites = locationRepository.getDistributionSites();

    Map<String, Object> map = new HashMap<>();
    map.put("returnForm", new ReturnFormBackingForm());
    map.put("usageSites", locationFactory.createFullViewModels(usageSites));
    map.put("distributionSites", locationFactory.createFullViewModels(distributionSites));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> addReturnForm(@Valid @RequestBody ReturnFormBackingForm backingForm) {
    Map<String, Object> map = new HashMap<>();
    map.put("returnForm", returnFormControllerService.createReturnForm(backingForm));
    return new ResponseEntity<>(map, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> getReturnForm(@PathVariable UUID id) {
    Map<String, Object> map = new HashMap<>();
    map.put("returnForm", returnFormControllerService.findById(id));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> updateReturnForm(@PathVariable("id") UUID id,
      @Valid @RequestBody ReturnFormBackingForm backingForm) {

    // Use the id parameter from the path
    backingForm.setId(id);

    Map<String, Object> map = new HashMap<>();
    map.put("returnForm", returnFormControllerService.updateReturnForm(backingForm));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_ORDER_FORM + "')")
  public ResponseEntity<Map<String, Object>> findReturnForms(
      @RequestParam(value = "returnDateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date returnDateFrom,
      @RequestParam(value = "returnDateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date returnDateTo,
      @RequestParam(value = "returnedFromId", required = false) UUID returnedFromId,
      @RequestParam(value = "returnedToId", required = false) UUID returnedToId,
      @RequestParam(value = "status", required = false) ReturnStatus status) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("returnForms", returnFormControllerService.findReturnForms(returnDateFrom, returnDateTo, 
        returnedFromId, returnedToId, status));

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }
  
  @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_ORDER_FORM + "')")
  public void deleteReturnForm(@PathVariable("id") UUID returnFormId) {
    returnFormControllerService.deleteReturnForm(returnFormId);
  }
}