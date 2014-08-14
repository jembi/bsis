package controller;

import backingform.DonorBackingForm;
import backingform.FindDonorBackingForm;
import backingform.validator.DonorBackingFormValidator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import model.donor.Donor;
import model.donordeferral.DonorDeferral;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import repository.ContactMethodTypeRepository;
import repository.DonorRepository;
import repository.LocationRepository;
import utils.CustomDateFormatter;
import utils.PermissionConstants;
import viewmodel.DonorDeferralViewModel;
import viewmodel.DonorViewModel;

@Controller
@RequestMapping(value = "/donor", produces = MediaType.APPLICATION_JSON_VALUE)
public class DonorController {

	/**
	 * The Constant LOGGER.
	 */	
  private static final Logger LOGGER = Logger.getLogger(DonorController.class);
  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private UtilController utilController;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private ContactMethodTypeRepository contactMethodTypeRepository;
  
  public DonorController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new DonorBackingFormValidator(binder.getValidator(), utilController));
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @Deprecated
  @RequestMapping("/donors")
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONOR_INFORMATION+"')")
  public ModelAndView getDonorsPage(HttpServletRequest request) {
    ModelAndView modelAndView = new ModelAndView("donors");
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("requestUrl", getUrl(request));
    modelAndView.addObject("model", model);
    return modelAndView;
  }

    
   @ApiOperation(value = "Find all accounts")
  @RequestMapping(value = "/donorSummary", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONOR+"')")
  public @ResponseBody Map<String, Object> donorSummaryGenerator(HttpServletRequest request, Model model,
      @RequestParam(value = "donorId", required = false) Long donorId) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("requestUrl", getUrl(request));
    Donor donor = null;
    if (donorId != null) {
      donor = donorRepository.findDonorById(donorId);
    }

    DonorViewModel donorViewModel = getDonorsViewModel(donor);
    map.put("donor", donorViewModel);

    map.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    map.put("donorFields", utilController.getFormFieldsForForm("donor"));
    
    
    // include donor deferral status
    List<DonorDeferral> donorDeferrals = null;
    try {
      donorDeferrals = donorRepository.getDonorDeferrals(donorId);  
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    Boolean isCurrentlyDeferred = donorRepository.isCurrentlyDeferred(donorDeferrals);
    map.put("isDonorCurrentlyDeferred", isCurrentlyDeferred);
    if(isCurrentlyDeferred){
    	map.put("donorLatestDeferredUntilDate", donorRepository.getLastDonorDeferralDate(donorId));
    }
    
    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "donors.finddonor.donorsummary");
    map.put("tips", tips);
    map.put("donorCodeGroups", donorRepository.findDonorCodeGroupsByDonorId(donor.getId()));
    return map;
  }

  @RequestMapping(value = "/viewDonorHistory", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION+"')")
  public @ResponseBody Map<String, Object> viewDonorHistory(HttpServletRequest request, Model model,
      @RequestParam(value = "donorId", required = false) Long donorId) {

    Map<String, Object> map = new HashMap<String, Object>();
    Donor donor = null;
    if (donorId != null) {
      donor = donorRepository.findDonorById(donorId);
      if (donor != null) {
        map.put("existingDonor", true);
      }
      else {
        map.put("existingDonor", false);
      }
    }

    DonorViewModel donorViewModel = getDonorsViewModels(Arrays.asList(donor)).get(0);
    map.put("donor", donorViewModel);
    map.put("allCollectedSamples", CollectedSampleController.getCollectionViewModels(donor.getCollectedSamples()));
    map.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    map.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    return map;
  }

  @RequestMapping(value = "/viewDonorDeferrals", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DEFERRAL+"')")
  public @ResponseBody Map<String, Object> viewDonorDeferrals(HttpServletRequest request, Model model,
      @RequestParam(value = "donorId", required = false) Long donorId) {

    Map<String, Object> map = new HashMap<String, Object>();
    List<DonorDeferral> donorDeferrals = null;
    List<DonorDeferralViewModel> donorDeferralViewModels;
    try {
      donorDeferrals = donorRepository.getDonorDeferrals(donorId);
      donorDeferralViewModels = getDonorDeferralViewModels(donorDeferrals);
    } catch (Exception ex) {
      ex.printStackTrace();
      donorDeferralViewModels = Arrays.asList(new DonorDeferralViewModel[0]);
    }
    
    map.put("isDonorCurrentlyDeferred", donorRepository.isCurrentlyDeferred(donorDeferrals));
    map.put("allDonorDeferrals", donorDeferralViewModels);
    map.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    map.put("donorDeferralFields", utilController.getFormFieldsForForm("donorDeferral"));
    return map;
  }

  private List<DonorDeferralViewModel> getDonorDeferralViewModels(List<DonorDeferral> donorDeferrals) {
    List<DonorDeferralViewModel> donorDeferralViewModels = new ArrayList<DonorDeferralViewModel>();
    for (DonorDeferral donorDeferral : donorDeferrals) {
      donorDeferralViewModels.add(new DonorDeferralViewModel(donorDeferral));
    }
    return donorDeferralViewModels;
  }

  @RequestMapping(value = "/editDonorFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_DONOR+"')")
  public @ResponseBody Map<String, Object> editDonorFormGenerator(HttpServletRequest request,
      @RequestParam(value="donorId") Long donorId) {

    Map<String, Object> map = new HashMap<String, Object>();
    Donor donor = donorRepository.findDonorById(donorId);
    map.put("donorFields", utilController.getFormFieldsForForm("donor"));
    DonorBackingForm donorForm = new DonorBackingForm(donor);
    String dateToken[]=donorForm.getBirthDate().split("/");
    donorForm.setContact(donor.getContact());
    donorForm.setAddress(donor.getAddress());
    donorForm.setDayOfMonth(dateToken[0]);
    donorForm.setMonth(dateToken[1]);
    donorForm.setYear(dateToken[2]);
    addEditSelectorOptions(map);
    map.put("editDonorForm", donorForm);
    map.put("refreshUrl", getUrl(request));
    return map;
  }
  
 

  @RequestMapping(value = "/addDonorFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONOR+"')")
  public @ResponseBody Map<String, Object> addDonorFormGenerator(HttpServletRequest request) {

    Map<String, Object> map = new HashMap<String, Object>();
    DonorBackingForm form = new DonorBackingForm();

    map.put("requestUrl", getUrl(request));
    map.put("firstTimeRender", true);
    map.put("addDonorForm", form);
     map.put("refreshUrl", getUrl(request));
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donor");
    // to ensure custom field names are displayed in the form
     map.put("donorFields", formFields);
    return map;
  }

    @RequestMapping(value = {"/addDonor", "/findDonor"}, method = RequestMethod.POST)
    @PreAuthorize("hasRole('" + PermissionConstants.ADD_DONOR + "')")
    public @ResponseBody
    Map<String, Object>
            addDonor(HttpServletRequest request,
                    HttpServletResponse response,
                    @ModelAttribute("addDonorForm") @Valid DonorBackingForm form,
                    BindingResult result, Model model) {

        Map<String, Object> map = new HashMap<String, Object>();
        boolean success = false;
        System.out.println("name..." + form.getFirstName());
        form.setBirthDate();
        Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donor");
        map.put("donorFields", formFields);

        Donor savedDonor = null;
        if (result.hasErrors()) {
            addEditSelectorOptions(map);
            map.put("hasErrors", true);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            success = false;
        } else {
            try {
                Donor donor = form.getDonor();
                donor.setIsDeleted(false);
                // Set the DonorNumber, It was set in the validate method of DonorBackingFormValidator.java
                donor.setDonorNumber(utilController.getNextDonorNumber());
                donor.setContact(form.getContact());
                donor.setAddress(form.getAddress());
                savedDonor = donorRepository.addDonor(donor);
                map.put("hasErrors", false);
                success = true;
                form = new DonorBackingForm();
            } catch (EntityExistsException ex) {
                ex.printStackTrace();
                success = false;
            } catch (Exception ex) {
                ex.printStackTrace();
                success = false;
            }
        }

    // check if method originates from /addDonor or /findDonor
    // if true - addDonor, if false - findDonor
    Boolean addDonorBool = false;
    if (request.getServletPath().contains("addDonor")){
    	addDonorBool = true;
    }
    
    if (success) {
      map.put("donorId", savedDonor.getId());
      map.put("donor", getDonorsViewModel(donorRepository.findDonorById(savedDonor.getId())));
      if(addDonorBool){
    	  map.put("addAnotherDonorUrl", "addDonorFormGenerator.html");
      }
      else {
    	 map.put("addAnotherDonorUrl", "findDonorFormGenerator.html");
    	 map.put("refreshUrl", "findDonorFormGenerator.html");
      }
    } else {
      map.put("errorMessage", "Error creating donor. Please fix the errors noted below.");
      map.put("firstTimeRender", false);
      map.put("addDonorForm", form);
      if(addDonorBool){
    	   map.put("addAnotherDonorUrl", "addDonorFormGenerator.html");
      }
      else {
    	  map.put("addAnotherDonorUrl", "findDonorFormGenerator.html");
    	map.put("refreshUrl", "findDonorFormGenerator.html");
      }
     
    }

   map.put("success", success);
    return map;
  }

  private DonorViewModel getDonorsViewModel(Donor donor) {
    DonorViewModel donorViewModel = new DonorViewModel(donor);
    return donorViewModel;
  }

  @RequestMapping(value = "/deferDonorFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DEFERRAL+"')")
  public @ResponseBody Map<String, Object> deferDonorFormGenerator(HttpServletRequest request,
      @RequestParam("donorId") String donorId) {
   
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donorId", donorId);
    map.put("deferralReasons", donorRepository.getDeferralReasons());
    return map;
  }
  
  @RequestMapping(value = "/editDeferDonorFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_DEFERRAL+"')")
  public @ResponseBody Map<String, Object> editDeferDonorFormGenerator(HttpServletRequest request,
      @RequestParam("donorDeferralId") String donorDeferralId) {
  
    Map<String, Object> map = new HashMap<String, Object>();
    DonorDeferral donorDeferral = donorRepository.getDonorDeferralsId(Long.parseLong(donorDeferralId));
    if(donorDeferral != null){
  		map.put("deferralUntilDate", CustomDateFormatter.getDateString(donorDeferral.getDeferredUntil()));
  		map.put("deferReasonText",donorDeferral.getDeferralReasonText());
  		map.put("deferReasonId",donorDeferral.getDeferralReason().getId());
  		map.put("donorId", donorDeferral.getDeferredDonor().getId());
  		map.put("donorDeferralId", donorDeferral.getId());
    }
    map.put("deferralReasons", donorRepository.getDeferralReasons());
    return map;
  }
  
  @RequestMapping(value="/updateDeferDonor", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_DEFERRAL+"')")
  public @ResponseBody Map<String, Object> updateDeferDonor(HttpServletRequest request,
         HttpServletResponse response,
         @RequestParam("donorDeferralId") String donorDeferralId,
         @RequestParam("donorId") String donorId,
         @RequestParam("deferUntil") String deferUntil,
         @RequestParam("deferralReasonId") String deferralReasonId,
         @RequestParam("deferralReasonText") String deferralReasonText) {

    Map<String, Object> donorDeferralResult = new HashMap<String, Object>();

    try {
      donorRepository.updatedeferDonor(donorDeferralId,donorId, deferUntil, deferralReasonId, deferralReasonText);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    return donorDeferralResult;
  }
  
  @RequestMapping(value="/cancelDeferDonor", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.VOID_DEFERRAL+"')")
  public @ResponseBody Map<String, Object> cancelDeferDonor(HttpServletRequest request,
         HttpServletResponse response,
         @RequestParam("donorDeferralId") String donorDeferralId) {

    Map<String, Object> donorDeferralResult = new HashMap<String, Object>();

    try {
      donorRepository.cancelDeferDonor(donorDeferralId);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    return donorDeferralResult;
  }

  @RequestMapping(value="/deferDonor", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DEFERRAL+"')")
  public @ResponseBody Map<String, Object> deferDonor(HttpServletRequest request,
         HttpServletResponse response,
         @RequestParam("donorId") String donorId,
         @RequestParam("deferUntil") String deferUntil,
         @RequestParam("deferralReasonId") String deferralReasonId,
         @RequestParam("deferralReasonText") String deferralReasonText) {

    Map<String, Object> donorDeferralResult = new HashMap<String, Object>();

    try {
      donorRepository.deferDonor(donorId, deferUntil, deferralReasonId, deferralReasonText);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    return donorDeferralResult;
  }

  @RequestMapping(value = "/updateDonor", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_DONOR+"')")
  public @ResponseBody Map<String,Object>  updateDonor(
      HttpServletResponse response,
      @ModelAttribute(value="editDonorForm") @Valid DonorBackingForm form,
      BindingResult result, Model model) {

    Map<String, Object> map = new HashMap<String, Object>();
    boolean success = false;
    String message = "";
    // only when the collection is correctly added the existingCollectedSample
    // property will be changed
    map.put("existingDonor", true);

    if (result.hasErrors()) {
      map.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      message = "Please fix the errors noted";
    }
    else {
      try {
        form.setIsDeleted(false);
        Donor donor = form.getDonor();
        donor.setContact(form.getContact());
        donor.setAddress(form.getAddress());
        Donor existingDonor = donorRepository.updateDonor(donor);
        if (existingDonor == null) {
          map.put("hasErrors", true);
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          success = false;
          map.put("existingDonor", false);
          message = "Donor does not already exist.";
        }
        else {
          map.put("hasErrors", false);
          success = true;
          message = "Donor Successfully Updated";
        }
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        success = false;
        message = "Donor Already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
   }

    map.put("editDonorForm", form);
    map.put("success", success);
    addEditSelectorOptions(map);
    map.put("errorMessage", message);
    map.put("donorFields", utilController.getFormFieldsForForm("donor"));
    addEditSelectorOptions(map);

    return map;
  }

  @RequestMapping(value = "/deleteDonor", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.VOID_DONOR+"')")
  public @ResponseBody
  Map<String, ? extends Object> deleteDonor(
      @RequestParam("donorId") Long donorId) {

    boolean success = true;
    String errMsg = "";
    try {
      donorRepository.deleteDonor(donorId);
    } catch (Exception ex) {
    	LOGGER.error("Internal Exception");
    	LOGGER.error(ex.getMessage());    	      
      success = false;
      errMsg = "Internal Server Error";
    }

    Map<String, Object> m = new HashMap<String, Object>();
    m.put("success", success);
    m.put("errMsg", errMsg);
    return m;
  }

  @RequestMapping(value = "/findDonorFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONOR+"')")
  public @ResponseBody Map<String, Object> findDonorFormGenerator(HttpServletRequest request, Model model) {

    FindDonorBackingForm form = new FindDonorBackingForm();
    form.setCreateDonorSummaryView(true);
    model.addAttribute("findDonorForm", form);
   
    DonorBackingForm dbform = new DonorBackingForm();

    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> m = model.asMap();
    utilController.addTipsToModel(model.asMap(), "donors.finddonor");
    // to ensure custom field names are displayed in the form
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));
    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    m.put("contentLabel", "Find Donors");
    m.put("refreshUrl", "findDonorFormGenerator.html");
    addEditSelectorOptions(map);
    map.put("model", m);
    map.put("addDonorForm", dbform);
    return map;
  }

  
  @RequestMapping(value = "/findDonor", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONOR+"')")
  public @ResponseBody Map<String, Object> findDonor(HttpServletRequest request,
      @ModelAttribute("findDonorForm") FindDonorBackingForm form,
      BindingResult result, Model model) {

    

    Map<String, Object> m = new HashMap<String, Object>();
    m.put("requestUrl", getUrl(request));
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));
    m.put("contentLabel", "Find Donors");
    m.put("nextPageUrl", getNextPageUrl(request));
    m.put("refreshUrl", getUrl(request));
    m.put("donorRowClickUrl", "donorSummary.html");
    m.put("createDonorSummaryView", form.getCreateDonorSummaryView());
    addEditSelectorOptions(m);
    return m;
  }
  
  @RequestMapping(value = "/printDonorLabel", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONOR+"')")
  public @ResponseBody Map<String, Object> printDonorLabel(HttpServletRequest request, Model model,
		  @RequestParam(value="donorNumber") String donorNumber) {
	  
        Map<String, Object> map = new HashMap<String, Object>();	
	map.put("labelZPL",
		"^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR2,2~SD30^JUS^LRN^CI0^XZ"+
		"^XA"+
		"^MMT"+
		"^PW360"+
		"^LL0120"+
		"^LS0"+
		"^BY2,3,52^FT63,69^BCN,,Y,N"+
		"^FD>:" + donorNumber + "^FS"+
		"^PQ1,0,1,Y^XZ"
	);
	
	return map;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("donorPanels", locationRepository.getAllDonorPanels());
    m.put("preferredContactMethods", contactMethodTypeRepository.getAllContactMethodTypes());
    m.put("languages", donorRepository.getAllLanguages());
    m.put("idTypes", donorRepository.getAllIdTypes());
    m.put("addressTypes", donorRepository.getAllAddressTypes());
  }

  /**
   * Get column name from column id, depends on sequence of columns in donorsTable.jsp
   */
  private String getSortingColumn(int columnId, Map<String, Map<String, Object>> formFields) {

    List<String> visibleFields = new ArrayList<String>();
    visibleFields.add("id");
    for (String field : Arrays.asList("donorNumber", "firstName","lastName", "gender", "bloodGroup", "birthDate")) {
      Map<String, Object> fieldProperties = (Map<String, Object>) formFields.get(field);
      if (fieldProperties.get("hidden").equals(false))
        visibleFields.add(field);
    }

    Map<String, String> sortColumnMap = new HashMap<String, String>();
    sortColumnMap.put("id", "id");
    sortColumnMap.put("donorNumber", "donorNumber");
    sortColumnMap.put("firstName", "firstName");
    sortColumnMap.put("lastName", "lastName");
    sortColumnMap.put("gender", "gender");
    sortColumnMap.put("bloodGroup", "bloodAbo");
    sortColumnMap.put("birthDate", "birthDate");
    String sortColumn = visibleFields.get(columnId);

    if (sortColumnMap.get(sortColumn) == null)
      return "id";
    else
      return sortColumnMap.get(sortColumn);
  }

  @RequestMapping("/findDonorPagination")
  public @ResponseBody Map<String, Object> findDonorPagination(HttpServletRequest request,
      @ModelAttribute("findDonorForm") FindDonorBackingForm form,
      BindingResult result, Model model) {


    String donorNumber = form.getDonorNumber();
    String firstName = form.getFirstName();
    String lastName = form.getLastName();
    String donationIdentificationNumber = form.getDonationIdentificationNumber();

    Map<String, Object> pagingParams = utilController.parsePagingParameters(request);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donor");
    int sortColumnId = (Integer) pagingParams.get("sortColumnId");
    pagingParams.put("sortColumn", getSortingColumn(sortColumnId, formFields));

    List<Object> results = new ArrayList<Object>();
    results = donorRepository.findAnyDonor(donorNumber, firstName,
            lastName, pagingParams, form.isUsePhraseMatch(), donationIdentificationNumber);
    @SuppressWarnings("unchecked")
    List<Donor> donors = (List<Donor>) results.get(0);
    System.out.println(donors);
    Long totalRecords = (Long) results.get(1);
    return generateDatatablesMap(donors, totalRecords, formFields);
  }
  
  /**
   * Datatables on the client side expects a json response for rendering data from the server
   * in jquery datatables. Remember of columns is important and should match the column headings
   * in donorsTable.jsp.
   */
  private Map<String, Object> generateDatatablesMap(List<Donor> donors, Long totalRecords, Map<String, Map<String, Object>> formFields) {
    Map<String, Object> donorsMap = new HashMap<String, Object>();
    ArrayList<Object> donorList = new ArrayList<Object>();
    for (DonorViewModel donor : getDonorsViewModels(donors)) {

      List<Object> row = new ArrayList<Object>();
      
      row.add(donor.getId().toString());

      for (String property : Arrays.asList("donorNumber", "firstName", "lastName", "gender", "bloodGroup", "birthDate", "dateOfLastDonation")) {
        if (formFields.containsKey(property)) {
          Map<String, Object> properties = (Map<String, Object>)formFields.get(property);
          if (properties.get("hidden").equals(false)) {
            String propertyValue = property;
            try {
              propertyValue = BeanUtils.getProperty(donor, property);
            } catch (IllegalAccessException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (NoSuchMethodException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            row.add(propertyValue.toString());
          }
        }
      }

      donorList.add(row);
    }
    donorsMap.put("aaData", donorList);
    donorsMap.put("iTotalRecords", totalRecords);
    donorsMap.put("iTotalDisplayRecords", totalRecords);
    return donorsMap;
  }

  /**
   * The method is not used anywhere in the application 
   * @param params
   * @param request
   * @return 
   */
  @Deprecated
  @RequestMapping("/viewDonors")
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONOR+"')")
  public ModelAndView viewDonors(@RequestParam Map<String, String> params,
      HttpServletRequest request) {

    List<Donor> allDonors = donorRepository.getAllDonors();
    ModelAndView modelAndView = new ModelAndView("donorsTable");
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("requestUrl", getUrl(request));

    model.put("allDonors", getDonorsViewModels(allDonors));
    model.put("donorFields", utilController.getFormFieldsForForm("donor"));
    model.put("contentLabel", "View All Donors");
    model.put("refreshUrl", getUrl(request));
    modelAndView.addObject("model", model);
    return modelAndView;
  }
  
  @Deprecated
  @RequestMapping(value = "/findDonorSelectorFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONOR+"')")
  public ModelAndView findDonorSelectorFormGenerator(HttpServletRequest request, Model model) {

    FindDonorBackingForm form = new FindDonorBackingForm();
    form.setCreateDonorSummaryView(false);
    model.addAttribute("findDonorForm", form);

    ModelAndView mv = new ModelAndView("donors/findDonorForm");
    Map<String, Object> m = model.asMap();
    utilController.addTipsToModel(model.asMap(), "donors.finddonor");
    // to ensure custom field names are displayed in the form
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));
    m.put("contentLabel", "Find Donors");
    m.put("refreshUrl", "findDonorSelectorFormGenerator.html");
   
    addEditSelectorOptions(mv.getModelMap());
    mv.addObject("model", m);
    return mv;
  }

  private List<DonorViewModel> getDonorsViewModels(List<Donor> donors) {
    List<DonorViewModel> donorViewModels = new ArrayList<DonorViewModel>();
    for (Donor donor : donors) {
      donorViewModels.add(new DonorViewModel(donor));
    }
    return donorViewModels;
  }

  public static String getNextPageUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString().replaceFirst("findDonor.html", "findDonorPagination.html");
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }
}
