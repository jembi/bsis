package controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import model.donor.Donor;
import model.location.Location;
import model.util.BloodGroup;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.ContactMethodTypeRepository;
import repository.DonorRepository;
import repository.LocationRepository;
import viewmodel.DonorViewModel;
import backingform.DonorCommunicationsBackingForm;
import backingform.validator.DonorCommunicationsBackingFormValidator;

@Controller
public class DonorCommunicationsController {

	/**
	 * The Constant LOGGER.
	 */	
  private static final Logger LOGGER = Logger.getLogger(DonorCommunicationsController.class);
  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private UtilController utilController;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private ContactMethodTypeRepository contactMethodTypeRepository;
  
  public DonorCommunicationsController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new DonorCommunicationsBackingFormValidator(binder.getValidator(), utilController));
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
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
  
  private void addEditSelectorOptions(Map<String, Object> m) {
	    m.put("donorPanels", locationRepository.getAllDonorPanels());
	    m.put("preferredContactMethods", contactMethodTypeRepository.getAllContactMethodTypes());
	  }
  
  /**
   * Get column name from column id, depends on sequence of columns in donorsCommunicationTable.jsp
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
    sortColumnMap.put("phoneNumber", "phoneNumber");
    sortColumnMap.put("dateOfLastDonation", "dateOfLastDonation");
    sortColumnMap.put("bloodGroup", "bloodAbo");
    sortColumnMap.put("donorPanel", "donorPanel");
    String sortColumn = visibleFields.get(columnId);

    if (sortColumnMap.get(sortColumn) == null)
      return "id";
    else
      return sortColumnMap.get(sortColumn);
  }
   
  @RequestMapping(value = "/donorCommunicFormGenerator", method = RequestMethod.GET)
  public ModelAndView donorCommunicationFormGenerator(HttpServletRequest request, Model model) {
  
	DonorCommunicationsBackingForm dbform = new DonorCommunicationsBackingForm();

    ModelAndView mv = new ModelAndView("donors/donorCommunicationsForm");
    Map<String, Object> m = model.asMap();
    utilController.addTipsToModel(model.asMap(), "donors.finddonor");
    // to ensure custom field names are displayed in the form
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));
    m.put("contentLabel", "Find Donors");
    m.put("refreshUrl", "donorCommunicFormGenerator.html");
    m.put("donorPanels", locationRepository.getAllDonorPanels());
    m.put("bloodGroups", BloodGroup.getBloodgroups());
    addEditSelectorOptions(mv.getModelMap());
    mv.addObject("model", m);
    mv.addObject("donorCommunicationsForm", dbform);
    return mv;
  }
  
  @RequestMapping(value = "/findDonorCommunicationForm", method = RequestMethod.GET)
  public ModelAndView findDonorFromDonorCommunication(HttpServletRequest request,
      @ModelAttribute("donorCommunicationsForm")  @Valid DonorCommunicationsBackingForm form,
      BindingResult result, Model model) {
    ModelAndView modelAndView = new ModelAndView("donors/donorsCommunicationTable");
    Map<String, Object> m                       = model.asMap();
    
    addEditSelectorOptions(m);
    m.put("bloodGroups", BloodGroup.getBloodgroups());
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));
    if (result.hasErrors()) {
    	modelAndView.addObject("hasErrors", true);
    	modelAndView = new ModelAndView("donors/donorCommunicationsForm");
    	modelAndView.addObject("errorMessage", "Missing information for one or more required fields.");
    	m.put("success", Boolean.FALSE);
      	m.put("requestUrl", getUrl(request));
    	m.put("refreshUrl", "donorCommunicFormGenerator.html");
    	modelAndView.addObject("model", m);
    	modelAndView.addObject("success", Boolean.FALSE);
    	return modelAndView;
      } 
    form.setCreateDonorSummaryView(true);
    m.put("requestUrl", getUrl(request));
    m.put("contentLabel", "Find Donors");
    m.put("nextPageUrl", getNextPageUrlForDonorCommunication(request));
    m.put("refreshUrl", getUrl(request));
    m.put("donorRowClickUrl", "donorSummary.html");
    m.put("createDonorSummaryView", form.getCreateDonorSummaryView());
    modelAndView.addObject("model", m);
    return modelAndView;
  } 
  
  public static String getNextPageUrlForDonorCommunication(HttpServletRequest req) {
	    String reqUrl = req.getRequestURL().toString().replaceFirst("findDonorCommunicationForm.html", "findDonorCommunicationPagination.html");
	    String queryString = req.getQueryString();
	    if (queryString != null) {
	        reqUrl += "?"+queryString;
	    }
	    return reqUrl;
	  }
  
  @RequestMapping("/findDonorCommunicationPagination")
  public @ResponseBody Map<String, Object> findDonorCommunicationPagination(HttpServletRequest request,
      @ModelAttribute("donorCommunicationsForm") DonorCommunicationsBackingForm form,
      BindingResult result, Model model) {
	
	LOGGER.debug("Start DonorCommunicationController:findDonorCommunicationPagination");
	List<Location> donorPanel                        = form.getDonorPanels();
    String clinicDate                                        = getEligibleDonorDate(form.getClinicDate());
    String clinicDateToCheckdeferredDonor   = form.getClinicDate();
    String lastDonationFromDate                    = form.getLastDonationFromDate();
    String lastDonationToDate                        = form.getLastDonationToDate();
    List<BloodGroup> bloodGroups                = form.getBloodGroups();
    
    Map<String, Object> pagingParams = utilController.parsePagingParameters(request);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donor");
    int sortColumnId = (Integer) pagingParams.get("sortColumnId");
    pagingParams.put("sortColumn", getSortingColumn(sortColumnId, formFields));

    List<Object> results = new ArrayList<Object>();
    results = donorRepository.findDonorFromDonorCommunication(donorPanel, clinicDate, lastDonationFromDate, lastDonationToDate, bloodGroups , form.getAnyBloodGroup() , pagingParams, clinicDateToCheckdeferredDonor);

    @SuppressWarnings("unchecked")
    List<Donor> donors = (List<Donor>) results.get(0);
    Long totalRecords = (Long) results.get(1);
    return generateDatatablesMapForDonorCommunication(donors, totalRecords, formFields);
  }
  
  private static String getEligibleDonorDate(String clinicDate)
  {
	  
	  SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy"); 
	  Calendar cal = Calendar.getInstance();
	  try {
		  if(clinicDate != null && !clinicDate.trim().equalsIgnoreCase(""))
		  {
			  Date dateObj = curFormater.parse(clinicDate);
			  @SuppressWarnings({ "unused", "deprecation" })
			  Date clinicDt = new Date(clinicDate);
			  cal .setTime(dateObj);
			  cal.add(Calendar.DATE, -56);
		}
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	  
	  return clinicDate != null && !clinicDate.trim().equalsIgnoreCase("") ? curFormater.format(cal.getTime()) : "";
  }
  
  private Map<String, Object> generateDatatablesMapForDonorCommunication(List<Donor> donors, Long totalRecords, Map<String, Map<String, Object>> formFields) {
	    Map<String, Object> donorsMap = new HashMap<String, Object>();
	    ArrayList<Object> donorList = new ArrayList<Object>();
	    for (DonorViewModel donor : getDonorsViewModels(donors)) {

	      List<Object> row = new ArrayList<Object>();	      
	      row.add(donor.getId().toString());
	      for (String property : Arrays.asList("donorNumber", "firstName", "lastName", "phoneNumber", "dateOfLastDonation" , "bloodGroup", "donorPanel")) {
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
	            row.add(propertyValue != null ?propertyValue.toString():"");
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
}
