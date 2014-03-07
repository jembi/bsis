package controller;

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
import model.util.BloodGroup;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import viewmodel.DonorDeferralViewModel;
import viewmodel.DonorViewModel;
import backingform.DonorBackingForm;
import backingform.FindDonorBackingForm;
import backingform.validator.DonorBackingFormValidator;

@Controller
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

  @RequestMapping("/donors")
  public ModelAndView getDonorsPage(HttpServletRequest request) {

    ModelAndView modelAndView = new ModelAndView("donors");
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("requestUrl", getUrl(request));
    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping(value = "/donorSummary", method = RequestMethod.GET)
  @PreAuthorize("hasRole('View Donor')")
  public ModelAndView donorSummaryGenerator(HttpServletRequest request, Model model,
      @RequestParam(value = "donorId", required = false) Long donorId) {

    ModelAndView mv = new ModelAndView("donors/donorSummary");

    mv.addObject("requestUrl", getUrl(request));
    Donor donor = null;
    if (donorId != null) {
      donor = donorRepository.findDonorById(donorId);
    }

    DonorViewModel donorViewModel = getDonorsViewModel(donor);
    mv.addObject("donor", donorViewModel);

    mv.addObject("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    mv.addObject("donorFields", utilController.getFormFieldsForForm("donor"));
    
    
    // include donor deferral status
    List<DonorDeferral> donorDeferrals = null;
    try {
      donorDeferrals = donorRepository.getDonorDeferrals(donorId);  
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    Boolean isCurrentlyDeferred = donorRepository.isCurrentlyDeferred(donorDeferrals);
    mv.addObject("isDonorCurrentlyDeferred", isCurrentlyDeferred);
    if(isCurrentlyDeferred){
    	mv.addObject("donorLatestDeferredUntilDate", donorRepository.getLastDonorDeferralDate(donorId));
    }
    
    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "donors.finddonor.donorsummary");
    mv.addObject("tips", tips);
    return mv;
  }

  @RequestMapping(value = "/viewDonorHistory", method = RequestMethod.GET)
  public ModelAndView viewDonorHistory(HttpServletRequest request, Model model,
      @RequestParam(value = "donorId", required = false) Long donorId) {

    ModelAndView mv = new ModelAndView("donors/collectionsForDonor");

    Donor donor = null;
    if (donorId != null) {
      donor = donorRepository.findDonorById(donorId);
      if (donor != null) {
        mv.addObject("existingDonor", true);
      }
      else {
        mv.addObject("existingDonor", false);
      }
    }

    DonorViewModel donorViewModel = getDonorsViewModels(Arrays.asList(donor)).get(0);
    mv.addObject("donor", donorViewModel);
    mv.addObject("allCollectedSamples", CollectedSampleController.getCollectionViewModels(donor.getCollectedSamples()));
    mv.addObject("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    mv.addObject("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    return mv;
  }

  @RequestMapping(value = "/viewDonorDeferrals", method = RequestMethod.GET)
  public ModelAndView viewDonorDeferrals(HttpServletRequest request, Model model,
      @RequestParam(value = "donorId", required = false) Long donorId) {

    ModelAndView mv = new ModelAndView("donors/deferralsForDonor");
    List<DonorDeferral> donorDeferrals = null;
    List<DonorDeferralViewModel> donorDeferralViewModels;
    try {
      donorDeferrals = donorRepository.getDonorDeferrals(donorId);
      donorDeferralViewModels = getDonorDeferralViewModels(donorDeferrals);
    } catch (Exception ex) {
      ex.printStackTrace();
      donorDeferralViewModels = Arrays.asList(new DonorDeferralViewModel[0]);
    }

    mv.addObject("isDonorCurrentlyDeferred", donorRepository.isCurrentlyDeferred(donorDeferrals));
    mv.addObject("allDonorDeferrals", donorDeferralViewModels);
    mv.addObject("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    mv.addObject("donorDeferralFields", utilController.getFormFieldsForForm("donorDeferral"));
    return mv;
  }

  private List<DonorDeferralViewModel> getDonorDeferralViewModels(List<DonorDeferral> donorDeferrals) {
    List<DonorDeferralViewModel> donorDeferralViewModels = new ArrayList<DonorDeferralViewModel>();
    for (DonorDeferral donorDeferral : donorDeferrals) {
      donorDeferralViewModels.add(new DonorDeferralViewModel(donorDeferral));
    }
    return donorDeferralViewModels;
  }

  @RequestMapping(value = "/editDonorFormGenerator", method = RequestMethod.GET)
  public ModelAndView editDonorFormGenerator(HttpServletRequest request,
      @RequestParam(value="donorId") Long donorId) {

    ModelAndView mv = new ModelAndView("donors/editDonorForm");
    Donor donor = donorRepository.findDonorById(donorId);
    mv.addObject("donorFields", utilController.getFormFieldsForForm("donor"));
    DonorBackingForm donorForm = new DonorBackingForm(donor);
    String dateToken[]=donorForm.getBirthDate().split("/");
    donorForm.setDayOfMonth(dateToken[0]);
    donorForm.setMonth(dateToken[1]);
    donorForm.setYear(dateToken[2]);
    addEditSelectorOptions(mv.getModelMap());
    mv.addObject("editDonorForm", donorForm);
    mv.addObject("refreshUrl", getUrl(request));
    return mv;
  }
  
 

  @RequestMapping(value = "/addDonorFormGenerator", method = RequestMethod.GET)
  public ModelAndView addDonorFormGenerator(HttpServletRequest request) {

    DonorBackingForm form = new DonorBackingForm();

    ModelAndView mv = new ModelAndView("donors/addDonorForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("addDonorForm", form);
    mv.addObject("refreshUrl", getUrl(request));
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donor");
    // to ensure custom field names are displayed in the form
    mv.addObject("donorFields", formFields);
    return mv;
  }

  @RequestMapping(value = {"/addDonor", "/findDonor"}, method = RequestMethod.POST)
  public ModelAndView
        addDonor(HttpServletRequest request,
                 HttpServletResponse response,
                 @ModelAttribute("addDonorForm") @Valid DonorBackingForm form,
                 BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView();
    boolean success = false;
     form.setBirthDate();
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donor");
    mv.addObject("donorFields", formFields);

    Donor savedDonor = null;
    if (result.hasErrors()) {
      addEditSelectorOptions(mv.getModelMap());
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        Donor donor = form.getDonor();
        donor.setIsDeleted(false);        
        // Set the DonorNumber, It was set in the validate method of DonorBackingFormValidator.java
         donor.setDonorNumber(utilController.getNextDonorNumber());
        savedDonor = donorRepository.addDonor(donor);
        mv.addObject("hasErrors", false);
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
      mv.addObject("donorId", savedDonor.getId());
      mv.addObject("donor", getDonorsViewModel(savedDonor));
      if(addDonorBool){
    	  mv.addObject("addAnotherDonorUrl", "addDonorFormGenerator.html");
      }
      else {
    	  mv.addObject("addAnotherDonorUrl", "findDonorFormGenerator.html");
    	  mv.addObject("refreshUrl", "findDonorFormGenerator.html");
      }
      mv.setViewName("donors/addDonorSuccess");
    } else {
      mv.addObject("errorMessage", "Error creating donor. Please fix the errors noted below.");
      mv.addObject("firstTimeRender", false);
      mv.addObject("addDonorForm", form);
      if(addDonorBool){
    	  mv.addObject("addAnotherDonorUrl", "addDonorFormGenerator.html");
      }
      else {
    	  mv.addObject("addAnotherDonorUrl", "findDonorFormGenerator.html");
    	  mv.addObject("refreshUrl", "findDonorFormGenerator.html");
      }
      mv.setViewName("donors/addDonorError");
    }

    mv.addObject("success", success);
    return mv;
  }

  private DonorViewModel getDonorsViewModel(Donor donor) {
    DonorViewModel donorViewModel = new DonorViewModel(donor);
    return donorViewModel;
  }

  @RequestMapping(value = "/deferDonorFormGenerator", method = RequestMethod.GET)
  public ModelAndView deferDonorFormGenerator(HttpServletRequest request,
      @RequestParam("donorId") String donorId) {
    ModelAndView mv = new ModelAndView("donors/deferDonorForm");
    mv.addObject("donorId", donorId);
    mv.addObject("deferralReasons", donorRepository.getDeferralReasons());
    return mv;
  }
  
  @RequestMapping(value = "/editDeferDonorFormGenerator", method = RequestMethod.GET)
  public ModelAndView editDeferDonorFormGenerator(HttpServletRequest request,
      @RequestParam("donorDeferralId") String donorDeferralId) {
    ModelAndView mv = new ModelAndView("donors/deferDonorForm");
    DonorDeferral donorDeferral = donorRepository.getDonorDeferralsId(Long.parseLong(donorDeferralId));
    if(donorDeferral != null){
  		mv.addObject("deferralUntilDate", CustomDateFormatter.getDateString(donorDeferral.getDeferredUntil()));
  		mv.addObject("deferReasonText",donorDeferral.getDeferralReasonText());
  		mv.addObject("deferReasonId",donorDeferral.getDeferralReason().getId());
  		mv.addObject("donorId", donorDeferral.getDeferredDonor().getId());
  		mv.addObject("donorDeferralId", donorDeferral.getId());
    }
    mv.addObject("deferralReasons", donorRepository.getDeferralReasons());
    return mv;
  }
  
  @RequestMapping(value="/updateDeferDonor", method = RequestMethod.POST)
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
  public ModelAndView updateDonor(
      HttpServletResponse response,
      @ModelAttribute(value="editDonorForm") @Valid DonorBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("donors/editDonorForm");
    boolean success = false;
    String message = "";
    // only when the collection is correctly added the existingCollectedSample
    // property will be changed
    mv.addObject("existingDonor", true);

    if (result.hasErrors()) {
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      message = "Please fix the errors noted";
    }
    else {
      try {
        form.setIsDeleted(false);
        
        Donor existingDonor = donorRepository.updateDonor(form.getDonor());
        if (existingDonor == null) {
          mv.addObject("hasErrors", true);
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          success = false;
          mv.addObject("existingDonor", false);
          message = "Donor does not already exist.";
        }
        else {
          mv.addObject("hasErrors", false);
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

    mv.addObject("editDonorForm", form);
    mv.addObject("success", success);
    addEditSelectorOptions(mv.getModelMap());
    mv.addObject("errorMessage", message);
    mv.addObject("donorFields", utilController.getFormFieldsForForm("donor"));
    addEditSelectorOptions(mv.getModelMap());

    return mv;
  }

  @RequestMapping(value = "/deleteDonor", method = RequestMethod.POST)
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
  public ModelAndView findDonorFormGenerator(HttpServletRequest request, Model model) {

    FindDonorBackingForm form = new FindDonorBackingForm();
    form.setCreateDonorSummaryView(true);
    model.addAttribute("findDonorForm", form);
   
    DonorBackingForm dbform = new DonorBackingForm();


    ModelAndView mv = new ModelAndView("donors/findDonorForm");
    Map<String, Object> m = model.asMap();
    utilController.addTipsToModel(model.asMap(), "donors.finddonor");
    // to ensure custom field names are displayed in the form
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));
    m.put("contentLabel", "Find Donors");
    m.put("refreshUrl", "findDonorFormGenerator.html");
    addEditSelectorOptions(mv.getModelMap());
    mv.addObject("model", m);
    mv.addObject("addDonorForm", dbform);
    return mv;
  }

  @RequestMapping(value = "/findDonor", method = RequestMethod.GET)
  public ModelAndView findDonor(HttpServletRequest request,
      @ModelAttribute("findDonorForm") FindDonorBackingForm form,
      BindingResult result, Model model) {

    ModelAndView modelAndView = new ModelAndView("donors/donorsTable");

    Map<String, Object> m = model.asMap();
    m.put("requestUrl", getUrl(request));
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));
    m.put("contentLabel", "Find Donors");
    m.put("nextPageUrl", getNextPageUrl(request));
    m.put("refreshUrl", getUrl(request));
    m.put("donorRowClickUrl", "donorSummary.html");
    m.put("createDonorSummaryView", form.getCreateDonorSummaryView());
    addEditSelectorOptions(m);
    modelAndView.addObject("model", m);
    return modelAndView;
  }
  
  @RequestMapping(value = "/printDonorLabel", method = RequestMethod.GET)
  public ModelAndView printDonorLabel(HttpServletRequest request, Model model,
		  @RequestParam(value="donorNumber") String donorNumber) {
	  
	ModelAndView mv = new ModelAndView("zplBarcode");
	
	mv.addObject("labelZPL",
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
	
	return mv;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("donorPanels", locationRepository.getAllDonorPanels());
    m.put("preferredContactMethods", contactMethodTypeRepository.getAllContactMethodTypes());
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
    List<BloodGroup> bloodGroups = form.getBloodGroups();

    Map<String, Object> pagingParams = utilController.parsePagingParameters(request);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donor");
    int sortColumnId = (Integer) pagingParams.get("sortColumnId");
    pagingParams.put("sortColumn", getSortingColumn(sortColumnId, formFields));

    List<Object> results = new ArrayList<Object>();
    results = donorRepository.findAnyDonor(donorNumber, firstName,
        lastName, bloodGroups, form.getAnyBloodGroup(), pagingParams);

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

  @RequestMapping("/viewDonors")
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

  @RequestMapping(value = "/findDonorSelectorFormGenerator", method = RequestMethod.GET)
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
