package controller;

import backingform.DonationBatchBackingForm;
import backingform.validator.DonationBatchBackingFormValidator;

import java.util.*;

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
import org.springframework.web.bind.annotation.RestController;

import factory.DonationBatchViewModelFactory;
import repository.DonationBatchRepository;
import repository.LocationRepository;
import utils.PermissionConstants;
import viewmodel.DonationBatchViewModel;

@RestController
@RequestMapping("/donationbatches")
public class DonationBatchController {

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private UtilController utilController;
  
  @Autowired
  private DonationBatchViewModelFactory donationBatchViewModelFactory;

  public DonationBatchController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new DonationBatchBackingFormValidator(binder.getValidator(),
                        utilController));
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public ResponseEntity<Map<String, Object>> findDonationBatch(HttpServletRequest request,
          @RequestParam(value = "isClosed", required = false) Boolean isClosed,
          @RequestParam(value = "venues", required = false) List<Long> venues,
          @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
          @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate
  ) {

	if(venues == null){
		venues = new ArrayList<Long>();
	}

    List<DonationBatch> donationBatches =
        donationBatchRepository.findDonationBatches(isClosed, venues, startDate, endDate);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donationBatches", getDonationBatchViewModels(donationBatches));

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_INFORMATION+"')")
  public Map<String, Object> addDonationBatchFormGenerator(HttpServletRequest request) {

    DonationBatchBackingForm form = new DonationBatchBackingForm();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("addDonationBatchForm", form);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donationbatch");
    addEditSelectorOptions(map);
    // to ensure custom field names are displayed in the form
    map.put("donationBatchFields", formFields);
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONATION_BATCH+"')") 
  public ResponseEntity<DonationBatchViewModel> addDonationBatch(
      @RequestBody @Valid DonationBatchBackingForm form) {
        DonationBatch donationBatch = form.getDonationBatch();
        donationBatch.setIsDeleted(false);
        donationBatchRepository.addDonationBatch(donationBatch);
		return new ResponseEntity<DonationBatchViewModel>(donationBatchViewModelFactory.createDonationBatchViewModel(
		        donationBatch), HttpStatus.CREATED);
  }
  
  @RequestMapping(value = "{id}",method = RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_DONATION_BATCH+"')")
  public ResponseEntity<DonationBatchViewModel> updateDonationBatch(@PathVariable Long id,
          @RequestBody @Valid DonationBatchBackingForm form){
      
      DonationBatch donationBatch = donationBatchRepository.updateDonationBatch(form.getDonationBatch());
      return new ResponseEntity<DonationBatchViewModel>(donationBatchViewModelFactory.createDonationBatchViewModel(
              donationBatch), HttpStatus.OK);
  }

  @RequestMapping(value = "{id}" ,method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public ResponseEntity<Map<String, Object>> donationBatchSummaryGenerator(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object>();
    DonationBatch  donationBatch = donationBatchRepository.findDonationBatchById(id);
    DonationBatchViewModel donationBatchViewModel = donationBatchViewModelFactory.createDonationBatchViewModel(
            donationBatch);
    map.put("donationBatch", donationBatchViewModel);

    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  
  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("venues", locationRepository.getAllVenues());
  }

  private List<DonationBatchViewModel> getDonationBatchViewModels(List<DonationBatch> donationBatches) {
    if (donationBatches == null)
      return Arrays.asList(new DonationBatchViewModel[0]);
    List<DonationBatchViewModel> donationBatchViewModels = new ArrayList<DonationBatchViewModel>();
    for (DonationBatch donationBatch : donationBatches) {
      donationBatchViewModels.add(donationBatchViewModelFactory.createDonationBatchViewModel(donationBatch));
    }
    return donationBatchViewModels;
  }
}
