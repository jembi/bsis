package org.jembi.bsis.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.DonorBackingForm;
import org.jembi.bsis.backingform.DuplicateDonorsBackingForm;
import org.jembi.bsis.backingform.validator.DonorBackingFormValidator;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.controllerservice.DonorControllerService;
import org.jembi.bsis.dto.DuplicateDonorDTO;
import org.jembi.bsis.factory.DonationFactory;
import org.jembi.bsis.factory.DonorDeferralFactory;
import org.jembi.bsis.factory.DonorViewModelFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.factory.PostDonationCounsellingFactory;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.repository.AdverseEventRepository;
import org.jembi.bsis.repository.ContactMethodTypeRepository;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.repository.SequenceNumberRepository;
import org.jembi.bsis.service.DonorCRUDService;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.service.DonorDeferralStatusCalculator;
import org.jembi.bsis.service.DuplicateDonorService;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.jembi.bsis.utils.CustomDateFormatter;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.jembi.bsis.viewmodel.DonorDeferralViewModel;
import org.jembi.bsis.viewmodel.DonorSummaryViewModel;
import org.jembi.bsis.viewmodel.DonorViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("donors")
public class DonorController {

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private SequenceNumberRepository sequenceNumberRepository;

  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private LocationFactory locationFactory;

  @Autowired
  private ContactMethodTypeRepository contactMethodTypeRepository;

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  @Autowired
  private GeneralConfigAccessorService generalConfigAccessorService;

  @Autowired
  private PostDonationCounsellingRepository postDonationCounsellingRepository;

  @Autowired
  private DonorCRUDService donorCRUDService;

  @Autowired
  private DonorViewModelFactory donorViewModelFactory;

  @Autowired
  private DonationFactory donationFactory;

  @Autowired
  private DonorDeferralFactory donorDeferralFactory;

  @Autowired
  private AdverseEventRepository adverseEventRepository;
  @Autowired
  private DonorConstraintChecker donorConstraintChecker;
  @Autowired
  private DonorDeferralStatusCalculator donorDeferralStatusCalculator;

  @Autowired
  private DuplicateDonorService duplicateDonorService;

  @Autowired
  PostDonationCounsellingFactory postDonationCounsellingFactory;

  @Autowired
  private DonorBackingFormValidator donorBackingFormValidator;

  @Autowired
  private DonorControllerService donorControllerService;

  public DonorController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(donorBackingFormValidator);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR + "')")
  public ResponseEntity<Map<String, Object>> donorSummaryGenerator(HttpServletRequest request,
                                                                   @PathVariable UUID id) {

    Map<String, Object> map = new HashMap<String, Object>();
    Donor donor = donorRepository.findDonorById(id);

    map.put("donor", donorViewModelFactory.createDonorViewModelWithPermissions(donor));

    Boolean isCurrentlyDeferred = donorDeferralStatusCalculator.isDonorCurrentlyDeferred(id);
    map.put("isDonorCurrentlyDeferred", isCurrentlyDeferred);
    if (isCurrentlyDeferred) {
      map.put("donorLatestDeferredUntilDate", donorRepository.getLastDonorDeferralDate(id));
      map.put("donorLatestDeferral", donorControllerService.getLastDeferral(id));
    }

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}/overview", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR + "')")
  public ResponseEntity<Map<String, Object>> viewDonorOverview(HttpServletRequest request,
                                                               @PathVariable UUID id) {

    Map<String, Object> map = new HashMap<String, Object>();
    Donor donor = donorRepository.findDonorById(id);
    List<Donation> donations = donor.getDonations();

   
    boolean flaggedForCounselling = postDonationCounsellingRepository
        .countFlaggedPostDonationCounsellingsForDonor(donor.getId()) > 0;

    boolean hasCounselling = postDonationCounsellingRepository
        .countNotFlaggedPostDonationCounsellingsForDonor(donor.getId()) > 0;

    map.put("currentlyDeferred", donorDeferralStatusCalculator.isDonorCurrentlyDeferred(id));
    map.put("flaggedForCounselling", flaggedForCounselling);
    map.put("hasCounselling", hasCounselling);
    map.put("deferredUntil", CustomDateFormatter.getDateTimeString(donorRepository.getLastDonorDeferralDate(id)));
    map.put("deferral", donorControllerService.getLastDeferral(id));
    map.put("canDelete", donorConstraintChecker.canDeleteDonor(id));
    map.put("isEligible", donorConstraintChecker.isDonorEligibleToDonate(id));
    map.put("birthDate", CustomDateFormatter.getDateString(donor.getBirthDate()));
    if (donations.size() > 0) {
      map.put("lastDonation", donationFactory.createDonationFullViewModelWithoutPermissions(donations.get(donations.size() - 1)));
      map.put("dateOfFirstDonation", CustomDateFormatter.getDateTimeString(donations.get(0).getDonationDate()));
      map.put("totalDonations", getNumberOfDonations(donations));
      map.put("dueToDonate", CustomDateFormatter.getDateTimeString(donor.getDueToDonate()));
      map.put("totalAdverseEvents", adverseEventRepository.countAdverseEventsForDonor(donor));
    } else {
      map.put("lastDonation", "");
      map.put("dateOfFirstDonation", "");
      map.put("totalDonations", 0);
      map.put("dueToDonate", "");
      map.put("totalAdverseEvents", 0);
    }
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/summaries", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR + "')")
  public ResponseEntity<Map<String, Object>> viewDonorSummary(HttpServletRequest request,
                                                              @RequestParam(value = "donorNumber", required = true) String donorNumber) {

    Map<String, Object> map = new HashMap<String, Object>();

    DonorSummaryViewModel donorSummary = donorRepository.findDonorSummaryByDonorNumber(donorNumber);
    map.put("donor", donorSummary);
    map.put("eligible", donorConstraintChecker.isDonorEligibleToDonate(donorSummary.getId()));

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}/donations", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONATION + "')")
  public ResponseEntity<Map<String, Object>> viewDonorHistory(HttpServletRequest request,
                                                              @PathVariable UUID id) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("allDonations", donorControllerService.findDonationsForDonor(id));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_DONOR + "')")
  public Map<String, Object> addDonorFormGenerator(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("addDonorForm", new DonorBackingForm());
    map.put("venues", locationFactory.createFullViewModels(locationRepository.getVenues()));
    // FIXME: preferredContactMethods, languages, idTypes and addressTypes are lists containing entities.
    // Only ViewModels should be returned because they are designed to only return the required information/
    // Returning entities can cause issues when the entities contain tracking fields. See BSIS-2469
    map.put("preferredContactMethods", contactMethodTypeRepository.getAllContactMethodTypes());
    map.put("languages", donorRepository.getAllLanguages());
    map.put("idTypes", donorRepository.getAllIdTypes());
    map.put("addressTypes", donorRepository.getAllAddressTypes());
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR_INFORMATION + "')")
  public ResponseEntity<Map<String, Object>>
  addDonor(@Valid @RequestBody DonorBackingForm form) {

    Map<String, Object> map = new HashMap<String, Object>();

    if (!canAddDonors()) {
      // Donor registration is blocked
      map.put("hasErrors", true);
      map.put("developerMessage", "Donor Registration Blocked");
      map.put("userMessage", "Donor Registration Blocked - No Open Donation Batches");
      map.put("moreInfo", null);
      map.put("errorCode", HttpStatus.METHOD_NOT_ALLOWED);
      return new ResponseEntity<Map<String, Object>>(map, HttpStatus.METHOD_NOT_ALLOWED);
    }

    Donor donor = form.getDonor();
    donor.setIsDeleted(false);
    donor.setContact(form.getContact());
    donor.setAddress(form.getAddress());
    donor.setDonorNumber(sequenceNumberRepository.getNextDonorNumber());
    Donor savedDonor = donorRepository.addDonor(donor);
    map.put("hasErrors", false);

    map.put("donorId", savedDonor.getId());
    map.put("donor", donorViewModelFactory.createDonorViewModelWithPermissions(savedDonor));

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DONOR + "')")
  public ResponseEntity<Map<String, Object>>
  updateDonor(@Valid @RequestBody DonorBackingForm form, @PathVariable UUID id) {

    HttpStatus httpStatus = HttpStatus.OK;
    Map<String, Object> map = new HashMap<String, Object>();
    Donor updatedDonor = null;

    form.setIsDeleted(false);
    Donor donor = form.getDonor();
    donor.setId(id);
    donor.setContact(form.getContact());
    donor.setAddress(form.getAddress());

    updatedDonor = donorRepository.updateDonorDetails(donor);

    map.put("donor", donorViewModelFactory.createDonorViewModelWithPermissions(donorRepository.findDonorById(updatedDonor.getId())));
    return new ResponseEntity<Map<String, Object>>(map, httpStatus);

  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_DONOR + "')")
  public void deleteDonor(@PathVariable UUID id) {
    donorCRUDService.deleteDonor(id);
  }

  @RequestMapping(value = "{id}/print", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR + "')")
  public Map<String, Object> printDonorLabel(@PathVariable UUID id) {

    String donorNumber = donorRepository.findDonorById(id).getDonorNumber();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("labelZPL",
        "^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR2,2~SD30^JUS^LRN^CI0^XZ" +
            "^XA" +
            "^MMT" +
            "^PW360" +
            "^LL0120" +
            "^LS0" +
            "^BY2,3,52^FT63,69^BCN,,Y,N" +
            "^FD>:" + donorNumber + "^FS" +
            "^PQ1,0,1,Y^XZ"
    );

    return map;
  }

  @RequestMapping(value = "{id}/deferrals", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DEFERRAL + "')")
  public Map<String, Object> viewDonorDeferrals(@PathVariable("id") UUID donorId) {
    List<DonorDeferral> donorDeferrals = donorRepository.getDonorDeferrals(donorId);
    Map<String, Object> map = new HashMap<>();
    map.put("allDonorDeferrals", donorDeferralFactory.createDonorDeferralViewModels(donorDeferrals));
    map.put("isDonorCurrentlyDeferred", donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donorId));
    return map;
  }


  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR + "')")
  public Map<String, Object> findDonors(
      @RequestParam(value = "firstName", required = false, defaultValue = "") String firstName,
      @RequestParam(value = "lastName", required = false, defaultValue = "") String lastName,
      @RequestParam(value = "donorNumber", required = false) String donorNumber,
      @RequestParam(value = "usePhraseMatch", required = false) boolean usePhraseMatch,
      @RequestParam(value = "donationIdentificationNumber", required = false) String donationIdentificationNumber) {

    List<Donor> donors = new ArrayList<>();

    if (StringUtils.isNotBlank(donorNumber)) {
      try {
        donors = Arrays.asList(donorRepository.findDonorByDonorNumber(donorNumber));
      } catch (NoResultException nre) {
        // Do nothing
      }
    } else if (StringUtils.isNotBlank(donationIdentificationNumber)) {
      try {
        donors = Arrays.asList(donorRepository.findDonorByDonationIdentificationNumber(donationIdentificationNumber));
      } catch (NoResultException nre) {
        // Do nothing
      }
    } else {
      donors = donorRepository.findAnyDonor(firstName, lastName, usePhraseMatch);
    }

    Map<String, Object> map = new HashMap<>();
    map.put("donors", donorViewModelFactory.createDonorSummaryViewModels(donors));
    map.put("canAddDonors", canAddDonors());
    return map;
  }

  @RequestMapping(value = "/duplicates", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR + "')")
  public Map<String, Object> findDuplicateDonors(@RequestParam(value = "donorNumber", required = true) String donorNumber) {

    Map<String, Object> map = new HashMap<String, Object>();

    Donor donor = donorRepository.findDonorByDonorNumber(donorNumber, false);
    List<Donor> duplicates = duplicateDonorService.findDuplicateDonors(donor);
    List<DonorViewModel> donorViewModels = donorViewModelFactory.createDonorViewModels(duplicates);
    map.put("duplicates", donorViewModels);

    return map;
  }

  @RequestMapping(value = "/duplicates/all", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DUPLICATE_DONORS + "')")
  public Map<String, Object> findDuplicateDonors() {
    Map<String, Object> map = new HashMap<String, Object>();
    List<DuplicateDonorDTO> duplicates = duplicateDonorService.findDuplicateDonors();
    map.put("duplicates", donorViewModelFactory.createDuplicateDonorViewModels(duplicates));
    return map;
  }

  @RequestMapping(value = "/duplicates/merge/preview", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MERGE_DONORS + "')")
  public Map<String, Object> findDuplicateDonorsDonations(@RequestParam(value = "donorNumber", required = true) String donorNumber,
                                                          @Valid @RequestBody DuplicateDonorsBackingForm form) {

    Map<String, Object> map = new HashMap<String, Object>();

    // create new donor
    Donor newDonor = form.getDonor();
    newDonor.setIsDeleted(false);
    newDonor.setContact(form.getContact());
    newDonor.setAddress(form.getAddress());

    List<String> donorNumbers = form.getDuplicateDonorNumbers();

    // Get all the Donations, process the Test Results and update necessary newDonor and Donation fields
    List<Donation> donations = duplicateDonorService.getAllDonationsToMerge(newDonor, donorNumbers);
    List<DonationFullViewModel> donationFullViewModels = donationFactory
        .createDonationFullViewModelsWithoutPermissions(donations);

    // gather all Deferrals
    List<DonorDeferral> donorDeferrals = duplicateDonorService.getAllDeferralsToMerge(newDonor, donorNumbers);
    List<DonorDeferralViewModel> donorDeferralViewModels = donorDeferralFactory
        .createDonorDeferralViewModels(donorDeferrals);

    form = new DuplicateDonorsBackingForm(newDonor);
    form.setContact(newDonor.getContact());
    form.setAddress(newDonor.getAddress());

    map.put("allDonations", donationFullViewModels);
    map.put("allDeferrals", donorDeferralViewModels);
    map.put("mergedDonor", form);

    return map;
  }

  @RequestMapping(value = "/duplicates/merge", method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.MERGE_DONORS + "')")
  public ResponseEntity<Map<String, Object>> mergeDuplicateDonors(@RequestParam(value = "donorNumber", required = true) String donorNumber,
                                                                  @Valid @RequestBody DuplicateDonorsBackingForm form) {

    Map<String, Object> map = new HashMap<String, Object>();

    // create new donor
    Donor newDonor = form.getDonor();
    newDonor.setIsDeleted(false);
    newDonor.setContact(form.getContact());
    newDonor.setAddress(form.getAddress());

    Donor savedDonor = duplicateDonorService.mergeAndSaveDonors(newDonor, form.getDuplicateDonorNumbers());

    map.put("hasErrors", false);
    map.put("donorId", savedDonor.getId());
    map.put("donor", donorViewModelFactory.createDonorViewModelWithPermissions(savedDonor));

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}/postdonationcounselling", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_POST_DONATION_COUNSELLING + "')")
  public PostDonationCounsellingViewModel getPostDonationCounsellingForDonor(
      @PathVariable("id") UUID donorId) {

    PostDonationCounselling postDonationCounselling = postDonationCounsellingRepository
        .findPostDonationCounsellingForDonor(donorId);
    return postDonationCounsellingFactory
        .createViewModel(postDonationCounselling);
  }

  private int getNumberOfDonations(List<Donation> donations) {
    int count = 0;
    for (Donation donation : donations) {
      if (donation.getPackType().getCountAsDonation() == true)
        count = count + 1;
    }
    return count;
  }

  /**
   * Check if donor registration is allowed based on the "open batch required" config and the number
   * of open donation batches.
   *
   * @return true if donor registration is allowed, otherwise false.
   */
  private boolean canAddDonors() {
    boolean openBatchRequired = generalConfigAccessorService.getBooleanValue(
        GeneralConfigConstants.DONOR_REGISTRATION_OPEN_BATCH_REQUIRED);
    return !openBatchRequired || donationBatchRepository.countOpenDonationBatches() > 0;
  }
}
