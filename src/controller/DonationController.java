package controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import model.donation.Donation;
import model.donation.HaemoglobinLevel;
import model.packtype.PackType;

import org.apache.commons.beanutils.BeanUtils;
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

import factory.DonationViewModelFactory;
import repository.AdverseEventTypeRepository;
import repository.PackTypeRepository;
import repository.DonationRepository;
import repository.DonationTypeRepository;
import repository.DonorRepository;
import repository.LocationRepository;
import repository.PostDonationCounsellingRepository;
import service.DonationCRUDService;
import utils.PermissionConstants;
import utils.PermissionUtils;
import viewmodel.DonationSummaryViewModel;
import viewmodel.DonationViewModel;
import viewmodel.PackTypeViewModel;
import backingform.DonationBackingForm;
import backingform.validator.AdverseEventBackingFormValidator;
import backingform.validator.DonationBackingFormValidator;

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
  private UtilController utilController;

  @Autowired
  private DonorRepository donorRepository;
  
  @Autowired
  private PostDonationCounsellingRepository postDonationCounsellingRepository;
  
  @Autowired
  private DonationCRUDService donationCRUDService;
  
  @Autowired
  private AdverseEventTypeRepository adverseEventTypeRepository;
  
  @Autowired
  private DonationViewModelFactory donationViewModelFactory;
  
  @Autowired
  private AdverseEventBackingFormValidator adverseEventBackingFormValidator;
  
  public DonationController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new DonationBackingFormValidator(utilController, adverseEventBackingFormValidator));
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  /**
   * Datatables on the client side expects a json response for rendering data from the server
   * in jquery datatables. Remember of columns is important and should match the column headings
   */
  private Map<String, Object> generateDatatablesMap(List<Donation> donations, Long totalRecords, Map<String, Map<String, Object>> formFields) {
    Map<String, Object> donationsMap = new HashMap<String, Object>();

    ArrayList<Object> donationList = new ArrayList<Object>();

    for (DonationViewModel donation : getDonationViewModels(donations)) {

      List<Object> row = new ArrayList<Object>();
      
      row.add(donation.getId().toString());

      for (String property : Arrays.asList("donationIdentificationNumber", "donationDate", "packType", "donorPanel")) {
        if (formFields.containsKey(property)) {
          Map<String, Object> properties = (Map<String, Object>)formFields.get(property);
          if (properties.get("hidden").equals(false)) {
            String propertyValue = property;
            try {
              propertyValue = BeanUtils.getProperty(donation, property);
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              e.printStackTrace();
            } catch (NoSuchMethodException e) {
              e.printStackTrace();
            }
            row.add(propertyValue.toString());
          }
        }
      }

      donationList.add(row);
    }
    donationsMap.put("aaData", donationList);
    donationsMap.put("iTotalRecords", totalRecords);
    donationsMap.put("iTotalDisplayRecords", totalRecords);
    return donationsMap;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
	m.put("donorPanels", locationRepository.getAllDonorPanels());
    m.put("donationTypes", donorTypeRepository.getAllDonationTypes());
    m.put("packTypes", getPackTypeViewModels(packTypeRepository.getAllPackTypes())); 
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

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_INFORMATION+"')")
  public  Map<String, Object> addDonationFormGenerator(HttpServletRequest request) {

    DonationBackingForm form = new DonationBackingForm();

    Map<String, Object> map = new  HashMap<String, Object>();
    map.put("addDonationForm", form);
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donation");
    // to ensure custom field names are displayed in the form
    map.put("donationFields", formFields);
    return map;
  }

  @RequestMapping(value = "{id}/edit/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_DONATION+"')")
  public  Map<String, Object> editDonationFormGenerator(HttpServletRequest request,
      @PathVariable Long id) {

    Donation donation = donationRepository.findDonationById(id);
    DonationBackingForm form = new DonationBackingForm(donation);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("editDonationForm", form);
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donation");
    // to ensure custom field names are displayed in the form
    map.put("donationFields", formFields);
    return map;
  }

  @RequestMapping( method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONATION+"')")
  public  ResponseEntity<Map<String, Object>> addDonation(
      @RequestBody @Valid DonationBackingForm form) {

      Map<String, Object> map = new HashMap<String, Object>();
      addEditSelectorOptions(map);
      Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donation");
      map.put("donationFields", formFields);
      Donation savedDonation = null;
      Donation donation = form.getDonation();
      
      donationCRUDService.updateAdverseEventForDonation(donation, form.getAdverseEvent());

      savedDonation = donationRepository.addDonation(donation);
      map.put("hasErrors", false);
      form = new DonationBackingForm();
	
      map.put("donationId", savedDonation.getId());
      map.put("donation", donationViewModelFactory.createDonationViewModelWithPermissions(savedDonation));
      return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
  }

  private DonationViewModel getDonationViewModel(Donation donation) {
    DonationViewModel donationViewModel = new DonationViewModel(donation);
    return donationViewModel;
  }
  
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('" + PermissionConstants.EDIT_DONATION + "')")
    public ResponseEntity<Map<String, Object>> updateDonation(
            @PathVariable("id") Long donationId,
            @RequestBody @Valid DonationBackingForm donationBackingForm) {

        Donation updatedDonation = donationCRUDService.updateDonation(donationId, donationBackingForm);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("donation", getDonationViewModel(updatedDonation));
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }

  private List<DonationViewModel> getDonationViewModels(
      List<Donation> donations) {
    if (donations == null)
      return Arrays.asList(new DonationViewModel[0]);
    List<DonationViewModel> donationViewModels = new ArrayList<DonationViewModel>();
    for (Donation donation : donations) {
      donationViewModels.add(new DonationViewModel(donation));
    }
    return donationViewModels;
  }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('" + PermissionConstants.VOID_DONATION + "')")
    public void deleteDonation(@PathVariable Long id) {
        donationCRUDService.deleteDonation(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONATION + "')")
    public Map<String, Object> donationSummaryGenerator(
            @PathVariable Long id) {

        Map<String, Object> map = new HashMap<String, Object>();

        Donation donation = null;
        if (id != null) {
            donation = donationRepository.findDonationById(id);
            if (donation != null) {
                map.put("existingDonation", true);
            } else {
                map.put("existingDonation", false);
            }
        }

        DonationViewModel donationViewModel = getDonationViewModel(donation);
        map.put("donation", donationViewModel);

      
        return map;
    }
    
     @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION+"')")
  public  Map<String, Object> findDonationPagination(
     @RequestParam(value = "donationIdentificationNumber", required = false)  String donationIdentificationNumber,
     @RequestParam(value = "panels",required = false)  List<Long> panelIds,
     @RequestParam(value = "packTypes",required = false)  List<Integer> packTypeIds,
     @RequestParam(value = "donationDateFrom", required = false)  String donationDateFrom,
     @RequestParam(value = "donationDateTo", required = false)  String donationDateTo,
     @RequestParam(value = "includeTestedDonations",required = true)  boolean includeTestedDonations)throws  ParseException{
   
      Map<String, Object> pagingParams = new HashMap<String, Object>();
      pagingParams.put("sortColumn", "id");
//      pagingParams.put("start", "0");
//      pagingParams.put("length", "10");
      pagingParams.put("sortDirection", "asc");
      
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("Donation");
  
    if (donationIdentificationNumber != null)
      donationIdentificationNumber = donationIdentificationNumber.trim();

    List<Object> results;
          results = donationRepository.findDonations(
                  donationIdentificationNumber,
                  packTypeIds, panelIds,
                  donationDateFrom, donationDateTo, includeTestedDonations, pagingParams);
  
    @SuppressWarnings("unchecked")
    List<Donation> donations = (List<Donation>) results.get(0);
    Long totalRecords = (Long) results.get(1);

    return generateDatatablesMap(donations, totalRecords, formFields);
  }
  
    @RequestMapping(value = "/summaries", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR + "')")
    public List<DonationSummaryViewModel> getDonationSummaries(
            @RequestParam(value = "flaggedForCounselling", required = true) boolean flaggedForCounselling,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate,
            @RequestParam(value = "donorPanel", required = false) List<Long> donorPanels) {

        if (flaggedForCounselling) {
            
            if (!PermissionUtils.loggedOnUserHasPermission(PermissionConstants.VIEW_POST_DONATION_COUNSELLING_DONORS)) {
                throw new AccessDeniedException("You do not have permission to view post donation counselling donors.");
            }
            
            List<Donation> donors = postDonationCounsellingRepository.findDonationsFlaggedForCounselling(
                    startDate, endDate, donorPanels == null ? null : new HashSet<>(donorPanels));
            return createDonationSummaryViewModels(donors);
        }

        // Just return an empty list for now. This could return the full list of donations if needed.
        return Collections.emptyList();
    }

    private List<DonationSummaryViewModel> createDonationSummaryViewModels(List<Donation> donations) {
        List<DonationSummaryViewModel> donationSummaryViewModels = new ArrayList<>();
        for (Donation donation : donations) {
            donationSummaryViewModels.add(new DonationSummaryViewModel(donation));
        }
        return donationSummaryViewModels;
    }
     
  private List<PackTypeViewModel> getPackTypeViewModels(List<PackType> packTypes){     
       List<PackTypeViewModel> viewModels = new ArrayList<PackTypeViewModel>();
       for(PackType packtType : packTypes){
           viewModels.add(new PackTypeViewModel(packtType));
       }
       return viewModels;
   }
}
