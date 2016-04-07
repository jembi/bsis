package controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import model.donationbatch.DonationBatch;

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

import repository.DonationBatchRepository;
import repository.LocationRepository;
import service.DonationBatchCRUDService;
import service.FormFieldAccessorService;
import utils.PermissionConstants;
import viewmodel.DonationBatchFullViewModel;
import viewmodel.DonationBatchViewModel;
import backingform.DonationBatchBackingForm;
import backingform.validator.DonationBatchBackingFormValidator;
import factory.DonationBatchViewModelFactory;
import factory.LocationViewModelFactory;

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
  private LocationViewModelFactory locationViewModelFactory;

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
                                                               @RequestParam(value = "venues", required = false) List<Long> venues,
                                                               @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
                                                               @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate
  ) {

    if (venues == null) {
      venues = new ArrayList<Long>();
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
    map.put("venues", locationViewModelFactory.createLocationViewModels(locationRepository.getAllVenues()));
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
  public ResponseEntity<Map<String, Object>> updateDonationBatch(@PathVariable Long id,
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
  public void deleteDonationBatch(@PathVariable Long id) {
    donationBatchCRUDService.deleteDonationBatch(id);
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONATION_BATCH + "')")
  public ResponseEntity<Map<String, Object>> donationBatchSummaryGenerator(HttpServletRequest request,
                                                                           @PathVariable Long id) {
    Map<String, Object> map = new HashMap<String, Object>();
    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(id);
    DonationBatchFullViewModel donationBatchViewModel = donationBatchViewModelFactory.createDonationBatchFullViewModel(donationBatch);
    map.put("donationBatch", donationBatchViewModel);

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }
}
