package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.backingform.DonationTypeBackingForm;
import org.jembi.bsis.backingform.validator.DonationTypeBackingFormValidator;
import org.jembi.bsis.controllerservice.DonationTypeControllerService;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.DonationTypeViewModel;
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
@RequestMapping("donationtypes")
public class DonationTypesController {

  @Autowired
  DonationTypeControllerService donationTypeControllerService;

  @Autowired
  private DonationTypeBackingFormValidator donationTypeBackingFormValidator;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(donationTypeBackingFormValidator);
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_TYPES + "')")
  public Map<String, Object> configureDonationTypesFormGenerator() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("allDonationTypes", donationTypeControllerService.getAllDonationTypes());
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_TYPES + "')")
  public Map<String, Object> getDonationType(@PathVariable UUID id) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donationType", donationTypeControllerService.getDonationType(id));
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_TYPES + "')")
  @ResponseStatus(HttpStatus.CREATED)
  public DonationTypeViewModel saveDonationType(@Valid @RequestBody DonationTypeBackingForm form) {
    return donationTypeControllerService.createDonationType(form);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_TYPES + "')")
  public Map<String, Object> updateDonationType(@Valid @RequestBody DonationTypeBackingForm form, @PathVariable UUID id) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donationType", donationTypeControllerService.updateDonationType(id, form));
    return map;
  }
}
