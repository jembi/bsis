package org.jembi.bsis.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.jembi.bsis.backingform.DeferralBackingForm;
import org.jembi.bsis.backingform.EndDeferralBackingForm;
import org.jembi.bsis.backingform.validator.DeferralBackingFormValidator;
import org.jembi.bsis.controllerservice.DeferralControllerService;
import org.jembi.bsis.factory.DonorDeferralFactory;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.service.DonorDeferralCRUDService;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.DonorDeferralViewModel;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("deferrals")
public class DeferralController {

  @Autowired
  private DonorDeferralCRUDService donorDeferralCRUDService;

  @Autowired
  private DonorDeferralFactory deferralFactory;

  @Autowired
  private DeferralBackingFormValidator deferralBackingFormValidator;

  @Autowired
  private DeferralControllerService deferralControllerService;

  @InitBinder("deferralBackingForm")
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(deferralBackingFormValidator);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
  public Map<String, Object> deferDonorFormGenerator() {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("deferralReasons", deferralControllerService.getDeferralReasons());
    map.put("venues", deferralControllerService.getVenues());
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DEFERRAL + "')")
  public Map<String, Object> getDonorDeferrals(@PathVariable UUID id) {

    Map<String, Object> map = new HashMap<String, Object>();
    DonorDeferral donorDeferral = donorDeferralCRUDService.findDeferralById(id);
    DonorDeferralViewModel donorDeferralViewModel = deferralFactory.createDonorDeferralViewModel(donorDeferral);
    map.put("deferral", donorDeferralViewModel);
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_DEFERRAL + "')")
  public ResponseEntity<Map<String, Object>> deferDonor(@Valid @RequestBody DeferralBackingForm deferralBackingForm) {
    HttpStatus httpStatus = HttpStatus.CREATED;
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("hasErrors", false);
    map.put("deferral", deferralControllerService.createDeferral(deferralBackingForm));
    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DEFERRAL + "')")
  public ResponseEntity<Map<String, Object>> updateDeferral(@Valid @RequestBody DeferralBackingForm deferralBackingForm,
      @PathVariable UUID id) {

    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();
    DonorDeferral updatedDeferral = null;
    deferralBackingForm.setId(id);

    DonorDeferral deferral = deferralFactory.createEntity(deferralBackingForm);
    deferral.setIsVoided(false);

    updatedDeferral = donorDeferralCRUDService.updateDeferral(deferral);

    map.put("deferral", deferralFactory.createDonorDeferralViewModel(donorDeferralCRUDService.findDeferralById(updatedDeferral.getId())));

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_DEFERRAL + "')")
  public void deleteDonorDeferral(@PathVariable UUID id) {
    donorDeferralCRUDService.deleteDeferral(id);
  }

  @RequestMapping(value = "{id}/end", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DEFERRAL + "')")
  public ResponseEntity<Map<String, Object>> endDeferral(@RequestBody EndDeferralBackingForm endDeferralBackingForm,
      @PathVariable UUID id) {

    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();

    DonorDeferral updatedDeferral = donorDeferralCRUDService.endDeferral(id, endDeferralBackingForm.getComment());
    map.put("deferral", deferralFactory.createDonorDeferralViewModel(updatedDeferral));

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }
}