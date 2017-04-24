package org.jembi.bsis.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.jembi.bsis.backingform.TransfusionBackingForm;
import org.jembi.bsis.backingform.validator.TransfusionBackingFormValidator;
import org.jembi.bsis.controllerservice.TransfusionControllerService;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/transfusions")
public class TransfusionController {
  
  @Autowired
  private TransfusionControllerService transfusionControllerService;
  @Autowired
  private TransfusionBackingFormValidator transfusionBackingFormsValidator;

  @InitBinder
  protected void initDonationFormBinder(WebDataBinder binder) {
    binder.setValidator(transfusionBackingFormsValidator);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_TRANSFUSION_DATA + "')")
  public Map<String, Object> addTransfusionFormGenerator(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("addTransfusionForm", new TransfusionBackingForm());
    map.put("componentTypes", transfusionControllerService.getComponentTypes());    
    map.put("usageSites", transfusionControllerService.getUsageSites());
    map.put("transfusionReactionTypes", transfusionControllerService.getTransfusionReactionTypes());
    map.put("transfusionOutcomes", TransfusionOutcome.values());
    return map;
  }
  
  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_TRANSFUSION_DATA + "')")
  @ResponseStatus(HttpStatus.CREATED)
  public Map<String, Object> addTransfusion(@Valid @RequestBody TransfusionBackingForm backingForm) {
    Map<String, Object> map = new HashMap<>();
    map.put("transfusion", transfusionControllerService.createTransfusion(backingForm));
    return map;
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_TRANSFUSION_DATA+"')")
  public Map<String, Object> updateTransfusion(@PathVariable("id") UUID transfusionId,
      @Valid @RequestBody TransfusionBackingForm backingForm) {

    backingForm.setId(transfusionId);
    Map<String, Object> map = new HashMap<>();
    map.put("transfusion", transfusionControllerService.updateTransfusion(backingForm));
    return map;
  }

  @RequestMapping(value = "/search/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TRANSFUSION_DATA + "')")
  public Map<String, Object> findTransfusionsFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("componentTypes", transfusionControllerService.getComponentTypes());
    map.put("usageSites", transfusionControllerService.getUsageSites());
    map.put("transfusionOutcomes", TransfusionOutcome.values());
    return map;
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TRANSFUSION_DATA + "')")
  public Map<String, Object> findTransfusions(@RequestParam(required = false) String donationIdentificationNumber,
      @RequestParam(required = false) String componentCode, 
      @RequestParam(required = false) UUID componentTypeId,
      @RequestParam(required = false) UUID receivedFromId,
      @RequestParam(required = false) TransfusionOutcome transfusionOutcome,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate, 
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("transfusions", transfusionControllerService.findTransfusions(donationIdentificationNumber, componentCode,
        componentTypeId, receivedFromId, transfusionOutcome, startDate, endDate));
    return map;
  }
  
  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TRANSFUSION_DATA + "')")
  public Map<String, Object> getTransfusion(@PathVariable UUID id) {
    Map<String, Object> map = new HashMap<>();
    map.put("transfusion", transfusionControllerService.getTransfusion(id));
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_TRANSFUSION_DATA + "')")
  public void deleteTransfusion(@PathVariable UUID id) {
    transfusionControllerService.deleteTransfusion(id);
  }
}