package org.jembi.bsis.controller;

import org.jembi.bsis.backingform.BloodTypingResolutionsBackingForm;
import org.jembi.bsis.backingform.DonationBackingForm;
import org.jembi.bsis.backingform.validator.BloodTypingResolutionsBackingFormValidator;
import org.jembi.bsis.backingform.validator.DonationBackingFormValidator;
import org.jembi.bsis.controllerservice.DonationControllerService;
import org.jembi.bsis.factory.DonationTypeFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.factory.PackTypeFactory;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.repository.AdverseEventTypeRepository;
import org.jembi.bsis.repository.DonationTypeRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.PackTypeRepository;
import org.jembi.bsis.service.DonationCRUDService;
import org.jembi.bsis.service.FormFieldAccessorService;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/donations")
public class DonationController {

  @Autowired
  private DonationControllerService donationControllerService;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private PackTypeRepository packTypeRepository;

  @Autowired
  private DonationTypeRepository donorTypeRepository;

  @Autowired
  private FormFieldAccessorService formFieldAccessorService;

  @Autowired
  private DonationCRUDService donationCRUDService;

  @Autowired
  private AdverseEventTypeRepository adverseEventTypeRepository;

  @Autowired
  private DonationBackingFormValidator donationBackingFormValidator;

  @Autowired
  private BloodTypingResolutionsBackingFormValidator bloodTypingResolutionBackingFormsValidator;
  
  @Autowired
  private DonationTypeFactory donationTypeFactory;

  @Autowired
  private PackTypeFactory packTypeFactory;

  @Autowired
  private LocationFactory locationFactory;

  public DonationController() {
  }

  @InitBinder("donationBackingForm")
  protected void initDonationFormBinder(WebDataBinder binder) {
    binder.setValidator(donationBackingFormValidator);
  }

  @InitBinder("bloodTypingResolutionBackingForms")
  protected void initResolutionBinder(WebDataBinder binder) {
    binder.setValidator(bloodTypingResolutionBackingFormsValidator);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONATION_INFORMATION + "')")
  public Map<String, Object> addDonationFormGenerator(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("addDonationForm", new DonationBackingForm());
    addEditSelectorOptions(map);
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_DONATION + "')")
  public ResponseEntity<Map<String, Object>> addDonation(@RequestBody @Valid DonationBackingForm donationBackingForm) {

    // Create the donation
    DonationFullViewModel donationFullViewModel = donationControllerService.createDonation(donationBackingForm);

    // Populate the response map
    Map<String, Object> map = new HashMap<>();
    addEditSelectorOptions(map);
    map.put("hasErrors", false);
    map.put("donationId", donationFullViewModel.getId());
    map.put("donation", donationFullViewModel);
    map.put("donationFields", formFieldAccessorService.getFormFieldsForForm("donation"));
    return new ResponseEntity<>(map, HttpStatus.CREATED);
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("venues", locationFactory.createViewModels(locationRepository.getVenues()));
    List<DonationType> donationTypes = donorTypeRepository.getAllDonationTypes();
    m.put("donationTypes", donationTypeFactory.createViewModels(donationTypes));
    m.put("packTypes", packTypeFactory.createFullViewModels(packTypeRepository.getAllEnabledPackTypes()));
    List<Map<String, Object>> haemoglobinLevels = new ArrayList<>();
    for (HaemoglobinLevel value : HaemoglobinLevel.values()) {
      Map<String, Object> haemoglobinLevel = new HashMap<>();
      haemoglobinLevel.put("value", value.name());
      haemoglobinLevel.put("label", value.getLabel());
      haemoglobinLevels.add(haemoglobinLevel);
    }
    m.put("haemoglobinLevels", haemoglobinLevels);
    m.put("adverseEventTypes", adverseEventTypeRepository.findNonDeletedAdverseEventTypeViewModels());
  }

  @RequestMapping(value = "{id}/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DONATION + "')")
  public ResponseEntity<Map<String, Object>> getEditDonationForm(@PathVariable("id") UUID donationId) {
    Map<String, Object> map = new HashMap<>();
    map.put("testBatchStatus", donationControllerService.getTestBatchStatusForDonation(donationId));
    map.put("packTypes", packTypeFactory.createFullViewModels(packTypeRepository.getAllEnabledPackTypes()));
    map.put("adverseEventTypes", adverseEventTypeRepository.findNonDeletedAdverseEventTypeViewModels());
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DONATION + "')")
  public ResponseEntity<Map<String, Object>> updateDonation(
      @PathVariable("id") UUID donationId,
      @RequestBody @Valid DonationBackingForm donationBackingForm) {

    donationBackingForm.setId(donationId);

    Map<String, Object> map = new HashMap<>();
    map.put("donation", donationControllerService.updateDonation(donationBackingForm));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_DONATION + "')")
  public void deleteDonation(@PathVariable UUID id) {
    donationCRUDService.deleteDonation(id);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONATION + "')")
  public Map<String, Object> getDonation(@PathVariable("id") UUID donationId) {

    Map<String, Object> map = new HashMap<>();
    map.put("donation", donationControllerService.findDonationById(donationId));
    return map;
  }

  @PreAuthorize("hasRole('" + PermissionConstants.ADD_TEST_OUTCOME + "')")
  @RequestMapping(value = "bloodTypingResolutions", method = RequestMethod.POST)
  public void saveBloodTypingResolutions(@RequestBody @Valid BloodTypingResolutionsBackingForm bloodTypingResolutionBackingForms) {
    donationCRUDService.updateDonationsBloodTypingResolutions(bloodTypingResolutionBackingForms);
  }

  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONATION + "')")
  @RequestMapping(method = RequestMethod.GET)
  public Map<String, Object> getByDin(@RequestParam String din) {
    HashMap<String, Object> response = new HashMap<>();
    response.put("donations", Collections.singleton(donationControllerService.findByDIN(din)));
    return response;
  }

  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONATION + "')")
  @RequestMapping(value = "/search", method = RequestMethod.GET)
  public Map<String, Object> search(@RequestParam(required = false) UUID venueId,
                                    @RequestParam(required = false) UUID packTypeId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    HashMap<String, Object> response = new HashMap<>();
    response.put("donations", donationControllerService.findByVenueAndPackTypeInRange(venueId, packTypeId, startDate,
        endDate));
    return response;
  }
}
