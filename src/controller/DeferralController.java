package controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

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

import backingform.DeferralBackingForm;
import backingform.EndDeferralBackingForm;
import backingform.validator.DeferralBackingFormValidator;
import factory.DonorDeferralViewModelFactory;
import model.donordeferral.DonorDeferral;
import model.location.LocationType;
import repository.DonorRepository;
import repository.LocationRepository;
import service.DonorDeferralCRUDService;
import utils.PermissionConstants;
import viewmodel.DonorDeferralViewModel;

@RestController
@RequestMapping("deferrals")
public class DeferralController {

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private DonorDeferralCRUDService donorDeferralCRUDService;

  @Autowired
  private DonorDeferralViewModelFactory deferralViewModelFactory;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private DeferralBackingFormValidator deferralBackingFormValidator;

  @InitBinder("deferralBackingForm")
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(deferralBackingFormValidator);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
  public Map<String, Object> deferDonorFormGenerator() {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("deferralReasons", donorRepository.getDeferralReasons());
    map.put("venues", locationRepository.getLocationsByType(LocationType.VENUE));
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DEFERRAL + "')")
  public Map<String, Object> getDonorDeferrals(@PathVariable Long id) {

    Map<String, Object> map = new HashMap<String, Object>();
    DonorDeferral donorDeferral = donorDeferralCRUDService.findDeferralById(id);
    DonorDeferralViewModel donorDeferralViewModel = deferralViewModelFactory.createDonorDeferralViewModel(donorDeferral);
    map.put("deferral", donorDeferralViewModel);
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_DEFERRAL + "')")
  public ResponseEntity<Map<String, Object>> deferDonor(@Valid @RequestBody DeferralBackingForm deferralBackingForm) {

    HttpStatus httpStatus = HttpStatus.CREATED;
    Map<String, Object> map = new HashMap<String, Object>();
    DonorDeferral savedDeferral = null;

    DonorDeferral deferral = deferralViewModelFactory.createEntity(deferralBackingForm);
    deferral.setIsVoided(false);
    deferral.setDeferralDate(new Date());
    savedDeferral = donorRepository.deferDonor(deferral);
    map.put("hasErrors", false);

    map.put("deferralId", savedDeferral.getId());
    map.put("deferral", deferralViewModelFactory.createDonorDeferralViewModel(donorDeferralCRUDService.findDeferralById(savedDeferral.getId())));

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);

  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DEFERRAL + "')")
  public ResponseEntity<Map<String, Object>> updateDeferral(@Valid @RequestBody DeferralBackingForm deferralBackingForm, @PathVariable Long id) {

    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();
    DonorDeferral updatedDeferral = null;
    deferralBackingForm.setId(id);

    DonorDeferral deferral = deferralViewModelFactory.createEntity(deferralBackingForm);
    deferral.setIsVoided(false);

    updatedDeferral = donorDeferralCRUDService.updateDeferral(deferral);

    map.put("deferral", deferralViewModelFactory.createDonorDeferralViewModel(donorDeferralCRUDService.findDeferralById(updatedDeferral.getId())));

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_DEFERRAL + "')")
  public void deleteDonorDeferral(@PathVariable Long id) {
    donorDeferralCRUDService.deleteDeferral(id);
  }

  @RequestMapping(value = "{id}/end", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DEFERRAL + "')")
  public ResponseEntity<Map<String, Object>> endDeferral(@RequestBody EndDeferralBackingForm endDeferralBackingForm, @PathVariable Long id) {

    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();

    DonorDeferral updatedDeferral = donorDeferralCRUDService.endDeferral(id, endDeferralBackingForm.getComment());
    map.put("deferral", deferralViewModelFactory.createDonorDeferralViewModel(updatedDeferral));

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }
}