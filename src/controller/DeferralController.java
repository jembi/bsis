package controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import backingform.validator.DeferralBackingFormValidator;
import model.donordeferral.DonorDeferral;

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

import repository.DonorRepository;
import repository.LocationRepository;
import service.DonorDeferralCRUDService;
import utils.PermissionConstants;
import viewmodel.DonorDeferralViewModel;
import backingform.DeferralBackingForm;
import backingform.EndDeferralBackingForm;
import factory.DonorDeferralViewModelFactory;

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

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(deferralBackingFormValidator);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
  public Map<String, Object> deferDonorFormGenerator() {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("deferralReasons", donorRepository.getDeferralReasons());
    map.put("venues", locationRepository.getAllVenues());
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
  public ResponseEntity<Map<String, Object>> deferDonor(@Valid @RequestBody DeferralBackingForm form) {

    HttpStatus httpStatus = HttpStatus.CREATED;
    Map<String, Object> map = new HashMap<String, Object>();
    DonorDeferral savedDeferral = null;

    DonorDeferral deferral = form.getDonorDeferral();
    deferral.setIsVoided(false);
    savedDeferral = donorRepository.deferDonor(deferral);
    map.put("hasErrors", false);

    map.put("deferralId", savedDeferral.getId());
    map.put("deferral", deferralViewModelFactory.createDonorDeferralViewModel(donorDeferralCRUDService.findDeferralById(savedDeferral.getId())));

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);

  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DEFERRAL + "')")
  public ResponseEntity<Map<String, Object>> updateDeferral(@Valid @RequestBody DeferralBackingForm form, @PathVariable Long id) {

    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();
    DonorDeferral updatedDeferral = null;

    DonorDeferral deferral = form.getDonorDeferral();
    deferral.setIsVoided(false);
    deferral.setId(id);

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
  public ResponseEntity<Map<String, Object>> endDeferral(@RequestBody EndDeferralBackingForm form, @PathVariable Long id) {

    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();

    DonorDeferral updatedDeferral = donorDeferralCRUDService.endDeferral(id, form.getComment());
    map.put("deferral", deferralViewModelFactory.createDonorDeferralViewModel(updatedDeferral));

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
  }
}