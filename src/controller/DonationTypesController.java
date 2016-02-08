package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import model.donationtype.DonationType;

import org.apache.log4j.Logger;
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

import repository.DonationTypeRepository;
import utils.PermissionConstants;
import viewmodel.DonationTypeViewModel;
import backingform.DonationTypeBackingForm;
import backingform.validator.DonationTypeBackingFormValidator;

@RestController
@RequestMapping("donationtypes")
public class DonationTypesController {

  private static final Logger LOGGER = Logger.getLogger(DonationTypesController.class);

  @Autowired
  DonationTypeRepository donationTypesRepository;

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
    addAllDonationTypesToModel(map);
    return map;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_TYPES + "')")
  public ResponseEntity getDonationType(@PathVariable Long id) {
    Map<String, Object> map = new HashMap<String, Object>();
    DonationType donationType = donationTypesRepository.getDonationTypeById(id);
    map.put("donationType", new DonationTypeViewModel(donationType));
    return new ResponseEntity(map, HttpStatus.OK);

  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_TYPES + "')")
  public ResponseEntity saveDonationType(@Valid @RequestBody DonationTypeBackingForm form) {
    DonationType donationType = donationTypesRepository.saveDonationType(form.getDonationType());
    return new ResponseEntity(new DonationTypeViewModel(donationType), HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.MANAGE_DONATION_TYPES + "')")
  public ResponseEntity updateDonationType(@Valid @RequestBody DonationTypeBackingForm form, @PathVariable Long id) {
    Map<String, Object> map = new HashMap<String, Object>();
    DonationType donationType = form.getDonationType();
    donationType.setId(id);
    donationType = donationTypesRepository.updateDonationType(donationType);
    map.put("donationType", new DonationTypeViewModel(donationType));
    return new ResponseEntity(map, HttpStatus.OK);
  }

  private void addAllDonationTypesToModel(Map<String, Object> m) {
    m.put("allDonationTypes", getDonationTypeViewModels(donationTypesRepository.getAllDonationTypes(true)));
  }

  private List<DonationTypeViewModel> getDonationTypeViewModels(List<DonationType> donationTypes) {
    List<DonationTypeViewModel> viewModels = new ArrayList<DonationTypeViewModel>();
    for (DonationType donationType : donationTypes) {
      viewModels.add(new DonationTypeViewModel(donationType));
    }
    return viewModels;
  }
}
