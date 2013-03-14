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
import model.donor.DonorBackingForm;
import model.donor.DonorBackingFormValidator;
import model.donor.FindDonorBackingForm;
import model.util.BloodGroup;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import repository.DonorRepository;
import repository.SequenceNumberRepository;
import viewmodel.DonorViewModel;

@Controller
public class DonorController {

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private UtilController utilController;

  @Autowired
  private SequenceNumberRepository sequenceNumberRepository;
  
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

    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "donors.finddonor.donorsummary");
    mv.addObject("tips", tips);
    return mv;
  }

  @RequestMapping(value = "/viewDonorHistory", method = RequestMethod.GET)
  public ModelAndView viewDonorHistory(HttpServletRequest request, Model model,
      @RequestParam(value = "donorId", required = false) Long donorId) {

    ModelAndView mv = new ModelAndView("collectionsForDonor");
    Map<String, Object> m = model.asMap();

    m.put("requestUrl", getUrl(request));

    Donor donor = null;
    if (donorId != null) {
      donor = donorRepository.findDonorById(donorId);
      if (donor != null) {
        m.put("existingDonor", true);
      }
      else {
        m.put("existingDonor", false);
      }
    }

    DonorViewModel donorViewModel = getDonorsViewModels(Arrays.asList(donor)).get(0);
    m.put("donor", donorViewModel);
    m.put("allCollectedSamples", CollectedSampleController.getCollectionViewModels(donor.getCollectedSamples()));
    m.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/editDonorFormGenerator", method = RequestMethod.GET)
  public void editDonorFormGenerator(HttpServletRequest request, Model model) {

    Donor donor = donorRepository.findDonorById((long) 2);
    donor.setLastName("value:" + (int)(Math.random()*100));
    donorRepository.updateDonor(donor);
  }

  @RequestMapping(value = "/addDonorFormGenerator", method = RequestMethod.GET)
  public ModelAndView addDonorFormGenerator(HttpServletRequest request, Model model) {

    DonorBackingForm form = new DonorBackingForm();

    ModelAndView mv = new ModelAndView("donors/addDonorForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("addDonorForm", form);
    mv.addObject("refreshUrl", getUrl(request));
    Map<String, Object> formFields = utilController.getFormFieldsForForm("donor");
    // to ensure custom field names are displayed in the form
    mv.addObject("donorFields", formFields);
    return mv;
  }

  @RequestMapping(value = "/addDonor", method = RequestMethod.POST)
  public ModelAndView
        addDonor(HttpServletRequest request,
                 HttpServletResponse response,
                 @ModelAttribute("addDonorForm") @Valid DonorBackingForm form,
                 BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView();
    boolean success = false;

    Map<String, Object> formFields = utilController.getFormFieldsForForm("donor");
    mv.addObject("donorFields", formFields);

    Donor savedDonor = null;
    if (result.hasErrors()) {
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        Donor donor = form.getDonor();
        donor.setIsDeleted(false);
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

    if (success) {
      mv.addObject("donorId", savedDonor.getId());
      mv.addObject("donor", getDonorsViewModel(savedDonor));
      mv.addObject("addAnotherDonorUrl", "addDonorFormGenerator.html");
      mv.setViewName("donors/addDonorSuccess");
    } else {
      mv.addObject("errorMessage", "Error creating donor. Please fix the errors noted below.");
      mv.addObject("firstTimeRender", false);
      mv.addObject("addDonorForm", form);
      mv.addObject("refreshUrl", "addDonorFormGenerator.html");
      mv.setViewName("donors/addDonorError");
    }

    mv.addObject("success", success);
    return mv;
  }

  private DonorViewModel getDonorsViewModel(Donor donor) {
    DonorViewModel donorViewModel = new DonorViewModel(donor);
    return donorViewModel;
  }

  @RequestMapping(value = "/updateDonor", method = RequestMethod.POST)
  public ModelAndView updateDonor(
      HttpServletResponse response,
      @ModelAttribute(value="editDonorForm") @Valid DonorBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editDonorForm");
    boolean success = false;
    String message = "";
    Map<String, Object> m = model.asMap();
    // only when the collection is correctly added the existingCollectedSample
    // property will be changed
    m.put("existingDonor", true);

    if (result.hasErrors()) {
      m.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      message = "Please fix the errors noted above";
    }
    else {
      try {
        form.setIsDeleted(false);
        Donor existingDonor = donorRepository.updateDonor(form.getDonor());
        if (existingDonor == null) {
          m.put("hasErrors", true);
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          success = false;
          m.put("existingDonor", false);
          message = "Donor does not already exist.";
        }
        else {
          m.put("hasErrors", false);
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

    m.put("editDonorForm", form);
    m.put("success", success);
    m.put("message", message);
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));

    mv.addObject("model", m);

    return mv;
  }

  @RequestMapping(value = "/donorTypeAhead", method = RequestMethod.GET)
  public @ResponseBody
  List<DonorViewModel> donorTypeAhead(
      @RequestParam("term") String term) {
    List<Donor> donors = donorRepository.findAnyDonorStartsWith(term);
    return getDonorsViewModels(donors);
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
      // TODO: Replace with logger
      System.err.println("Internal Exception");
      System.err.println(ex.getMessage());
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
    model.addAttribute("findDonorForm", form);

    ModelAndView mv = new ModelAndView("donors/findDonorForm");
    Map<String, Object> m = model.asMap();
    utilController.addTipsToModel(model.asMap(), "donors.finddonor");
    // to ensure custom field names are displayed in the form
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));
    m.put("contentLabel", "Find Donors");
    m.put("refreshUrl", "findDonorFormGenerator.html");
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/findDonor", method = RequestMethod.GET)
  public ModelAndView findDonor(HttpServletRequest request,
      @ModelAttribute("findDonorForm") DonorBackingForm form,
      BindingResult result, Model model) {

    ModelAndView modelAndView = new ModelAndView("donors/donorsTable");

    Map<String, Object> m = model.asMap();
    m.put("tableName", "findDonorResultsTable");
    m.put("requestUrl", getUrl(request));
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));
    m.put("contentLabel", "Find Donors");
    m.put("nextPageUrl", getNextPageUrl(request));
    m.put("refreshUrl", getUrl(request));
    modelAndView.addObject("model", m);
    return modelAndView;
  }

  /**
   * Get column name from column id, depends on sequence of columns in donorsTable.jsp
   */
  private String getSortingColumn(int columnId, Map<String, Object> formFields) {

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
    Map<String, Object> formFields = utilController.getFormFieldsForForm("donor");
    int sortColumnId = (Integer) pagingParams.get("sortColumnId");
    pagingParams.put("sortColumn", getSortingColumn(sortColumnId, formFields));

    List<Object> results = new ArrayList<Object>();
    results = donorRepository.findAnyDonor(donorNumber, firstName,
        lastName, bloodGroups, pagingParams);

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
  private Map<String, Object> generateDatatablesMap(List<Donor> donors, Long totalRecords, Map<String, Object> formFields) {
    Map<String, Object> donorsMap = new HashMap<String, Object>();
    ArrayList<Object> donorList = new ArrayList<Object>();
    for (DonorViewModel donor : getDonorsViewModels(donors)) {

      List<Object> row = new ArrayList<Object>();
      
      row.add(donor.getId().toString());

      for (String property : Arrays.asList("donorNumber", "firstName", "lastName", "gender", "bloodGroup", "birthDate")) {
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

    model.put("tableName", "viewAllDonors");
    model.put("allDonors", getDonorsViewModels(allDonors));
    model.put("donorFields", utilController.getFormFieldsForForm("donor"));
    model.put("contentLabel", "View All Donors");
    model.put("refreshUrl", getUrl(request));
    modelAndView.addObject("model", model);
    return modelAndView;
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

  private void setDonorNumber(DonorBackingForm form,
	      Map<String, Object> donorNumberProperties) {
	    boolean isAutoGeneratable = (Boolean) donorNumberProperties.get("isAutoGeneratable");
	    boolean autoGenerate = (Boolean) donorNumberProperties.get("autoGenerate");
	    if (isAutoGeneratable && autoGenerate)
	      form.setDonorNumber(sequenceNumberRepository.getNextDonorNumber());    
  }
}
