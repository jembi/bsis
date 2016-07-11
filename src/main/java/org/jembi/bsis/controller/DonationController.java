package org.jembi.bsis.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.jembi.bsis.backingform.BloodTypingResolutionsBackingForm;
import org.jembi.bsis.backingform.DonationBackingForm;
import org.jembi.bsis.backingform.validator.BloodTypingResolutionsBackingFormValidator;
import org.jembi.bsis.backingform.validator.DonationBackingFormValidator;
import org.jembi.bsis.factory.DonationSummaryViewModelFactory;
import org.jembi.bsis.factory.DonationTypeFactory;
import org.jembi.bsis.factory.DonationViewModelFactory;
import org.jembi.bsis.factory.PackTypeFactory;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.AdverseEventTypeRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonationTypeRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.PackTypeRepository;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.service.DonationCRUDService;
import org.jembi.bsis.service.FormFieldAccessorService;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.utils.PermissionUtils;
import org.jembi.bsis.viewmodel.DonationSummaryViewModel;
import org.jembi.bsis.viewmodel.PackTypeFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
@RequestMapping("/donations")
public class DonationController {

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private PackTypeRepository packTypeRepository;

  @Autowired
  private DonationTypeRepository donorTypeRepository;

  @Autowired
  private FormFieldAccessorService formFieldAccessorService;

  @Autowired
  private PostDonationCounsellingRepository postDonationCounsellingRepository;

  @Autowired
  private DonationCRUDService donationCRUDService;

  @Autowired
  private AdverseEventTypeRepository adverseEventTypeRepository;

  @Autowired
  private DonationViewModelFactory donationViewModelFactory;

  @Autowired
  private DonationBackingFormValidator donationBackingFormValidator;

  @Autowired
  private DonationSummaryViewModelFactory donationSummaryViewModelFactory;

  @Autowired
  private BloodTypingResolutionsBackingFormValidator bloodTypingResolutionBackingFormsValidator;
  
  @Autowired
  private DonationTypeFactory donationTypeFactory;

  @Autowired
  private PackTypeFactory packTypeFactory;

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

    DonationBackingForm form = new DonationBackingForm();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("addDonationForm", form);
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = formFieldAccessorService.getFormFieldsForForm("donation");
    // to ensure custom field names are displayed in the form
    map.put("donationFields", formFields);
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_DONATION + "')")
  public ResponseEntity<Map<String, Object>> addDonation(@RequestBody @Valid DonationBackingForm donationBackingForm) {

    // Create the donation
    Donation savedDonation = donationCRUDService.createDonation(donationBackingForm);

    // Populate the response map
    Map<String, Object> map = new HashMap<>();
    addEditSelectorOptions(map);
    map.put("hasErrors", false);
    map.put("donationId", savedDonation.getId());
    map.put("donation", donationViewModelFactory.createDonationViewModelWithPermissions(savedDonation));
    map.put("donationFields", formFieldAccessorService.getFormFieldsForForm("donation"));
    return new ResponseEntity<>(map, HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DONATION + "')")
  public ResponseEntity<Map<String, Object>> updateDonation(
      @PathVariable("id") Long donationId,
      @RequestBody @Valid DonationBackingForm donationBackingForm) {

    Donation updatedDonation = donationCRUDService.updateDonation(donationId, donationBackingForm);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donation", donationViewModelFactory.createDonationViewModelWithPermissions(updatedDonation));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_DONATION + "')")
  public void deleteDonation(@PathVariable Long id) {
    donationCRUDService.deleteDonation(id);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONATION + "')")
  public Map<String, Object> getDonation(@PathVariable("id") Long donationId) {
    
    Donation donation = donationRepository.findDonationById(donationId);

    Map<String, Object> map = new HashMap<>();
    map.put("donation", donationViewModelFactory.createDonationViewModelWithPermissions(donation));
    return map;
  }

  @RequestMapping(value = "/summaries", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR + "')")
  public List<DonationSummaryViewModel> getDonationSummaries(
      @RequestParam(value = "flaggedForCounselling", required = true) boolean flaggedForCounselling,
      @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate,
      @RequestParam(value = "venue", required = false) List<Long> venues) {

    if (flaggedForCounselling) {

      if (!PermissionUtils.loggedOnUserHasPermission(PermissionConstants.VIEW_POST_DONATION_COUNSELLING_DONORS)) {
        throw new AccessDeniedException("You do not have permission to view post donation counselling donors.");
      }

      List<Donation> donors = postDonationCounsellingRepository.findDonationsFlaggedForCounselling(
          startDate, endDate, venues == null ? null : new HashSet<>(venues));
      return donationSummaryViewModelFactory.createFullDonationSummaryViewModels(donors);
    }

    // Just return an empty list for now. This could return the full list of donations if needed.
    return Collections.emptyList();
  }

  @PreAuthorize("hasRole('" + PermissionConstants.ADD_TEST_OUTCOME + "')")
  @RequestMapping(value = "bloodTypingResolutions", method = RequestMethod.POST)
  public void saveBloodTypingResolutions(@RequestBody @Valid BloodTypingResolutionsBackingForm bloodTypingResolutionBackingForms) {
    donationCRUDService.updateDonationsBloodTypingResolutions(bloodTypingResolutionBackingForms);
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("venues", locationRepository.getVenues());
    List<DonationType> donationTypes = donorTypeRepository.getAllDonationTypes();
    m.put("donationTypes", donationTypeFactory.createDonationTypeViewModels(donationTypes));
    m.put("packTypes", getPackTypeViewModels(packTypeRepository.getAllEnabledPackTypes()));
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
  
  private List<PackTypeFullViewModel> getPackTypeViewModels(List<PackType> packTypes) {
    // FIXME: use a factory
    List<PackTypeFullViewModel> viewModels = new ArrayList<PackTypeFullViewModel>();
    for (PackType packtType : packTypes) {
      viewModels.add(packTypeFactory.createFullViewModel(packtType));
    }
    return viewModels;
  }
}
