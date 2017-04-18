package org.jembi.bsis.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.jembi.bsis.backingform.DonationBatchBackingForm;
import org.jembi.bsis.backingform.validator.DonationBatchBackingFormValidator;
import org.jembi.bsis.factory.DonationBatchViewModelFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.DonationBatchCRUDService;
import org.jembi.bsis.service.FormFieldAccessorService;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.DonationBatchFullViewModel;
import org.jembi.bsis.viewmodel.DonationBatchViewModel;
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
@RequestMapping("/donationbatches")
public class DonationBatchController {

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  @Autowired
  private DonationBatchCRUDService donationBatchCRUDService;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private FormFieldAccessorService formFieldAccessorService;

  @Autowired
  private DonationBatchViewModelFactory donationBatchViewModelFactory;

  @Autowired
  DonationBatchBackingFormValidator donationBatchBackingFormValidator;
  
  @Autowired
  private LocationFactory locationFactory;

  public DonationBatchController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(donationBatchBackingFormValidator);
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONATION_BATCH + "')")
  public ResponseEntity<Map<String, Object>> findDonationBatch(HttpServletRequest request,
      @RequestParam(value = "isClosed", required = false) Boolean isClosed,
      @RequestParam(value = "venues", required = false) List<UUID> venues,
      @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {

    if (venues == null) {
      venues = new ArrayList<UUID>();
    }

    List<DonationBatch> donationBatches =
        donationBatchRepository.findDonationBatches(isClosed, venues, startDate, endDate);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donationBatches", donationBatchViewModelFactory.createDonationBatchBasicViewModels(donationBatches));

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONATION_INFORMATION + "')")
  public Map<String, Object> addDonationBatchFormGenerator(HttpServletRequest request) {

    DonationBatchBackingForm form = new DonationBatchBackingForm();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("addDonationBatchForm", form);
    map.put("venues", locationFactory.createFullViewModels(locationRepository.getVenues()));
    // to ensure custom field names are displayed in the form
    Map<String, Map<String, Object>> formFields = formFieldAccessorService.getFormFieldsForForm("donationbatch");
    map.put("donationBatchFields", formFields);
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_DONATION_BATCH + "')")
  public ResponseEntity<DonationBatchViewModel> addDonationBatch(
      @RequestBody @Valid DonationBatchBackingForm form) {
    DonationBatch donationBatch = form.getDonationBatch();
    donationBatch.setIsDeleted(false);
    donationBatchRepository.addDonationBatch(donationBatch);
    return new ResponseEntity<DonationBatchViewModel>(
        donationBatchViewModelFactory.createDonationBatchFullViewModel(donationBatch), HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DONATION_BATCH + "')")
  public ResponseEntity<Map<String, Object>> updateDonationBatch(@PathVariable UUID id,
      @RequestBody @Valid DonationBatchBackingForm form) {
    Map<String, Object> map = new HashMap<String, Object>();

    donationBatchCRUDService.updateDonationBatch(form.getDonationBatch());

    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(form.getId());
    map.put("donationBatch", donationBatchViewModelFactory.createDonationBatchFullViewModel(donationBatch));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_DONATION_BATCH + "')")
  public void deleteDonationBatch(@PathVariable UUID id) {
    donationBatchCRUDService.deleteDonationBatch(id);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONATION_BATCH + "')")
  public ResponseEntity<Map<String, Object>> donationBatchSummaryGenerator(HttpServletRequest request,
      @PathVariable UUID id) {
    Map<String, Object> map = new HashMap<String, Object>();
    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(id);
    DonationBatchFullViewModel donationBatchViewModel = donationBatchViewModelFactory.createDonationBatchFullViewModel(donationBatch);
    map.put("donationBatch", donationBatchViewModel);

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }
}
