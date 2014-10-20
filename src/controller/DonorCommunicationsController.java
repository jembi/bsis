package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import model.collectedsample.CollectionConstants;
import model.donor.Donor;
import model.location.Location;
import model.util.BloodGroup;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import repository.DonorCommunicationsRepository;
import repository.LocationRepository;
import utils.PermissionConstants;
import viewmodel.DonorViewModel;

@RestController
@RequestMapping("donorcommunications")
public class DonorCommunicationsController {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(DonorCommunicationsController.class);
    @Autowired
    private DonorCommunicationsRepository donorCommunicationsRepository;

    @Autowired
    private UtilController utilController;

    @Autowired
    private LocationRepository locationRepository;

    public DonorCommunicationsController() {
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(BloodGroup.class, null, null);
    }

    @RequestMapping(value = "form", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR + "')")
    public @ResponseBody
    Map<String, Object> donorCommunicationsFormGenerator(
            HttpServletRequest request) {

       // DonorCommunicationsBackingForm dbform = new DonorCommunicationsBackingForm();

        Map<String, Object> map = new HashMap<String, Object>();

        // to ensure custom field names are displayed in the form
        map.put("donorFields", utilController.getFormFieldsForForm("donor"));
        addEditSelectorOptions(map);
       // map.put("donorCommunicationsForm", dbform);
        return map;
    }

//    @RequestMapping(value = "/find", method = RequestMethod.GET)
//    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONOR + "')")
//    public @ResponseBody
//    Map<String, Object> findDonorCommunications(
//            HttpServletRequest request,
//            @ModelAttribute("donorCommunicationsForm") @Valid DonorCommunicationsBackingForm form,
//            BindingResult result) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        addEditSelectorOptions(map);
//        map.put("donorFields", utilController.getFormFieldsForForm("donor"));
//        form.getDonorPanels();
//        if (result.hasErrors()) {
//            map.put("hasErrors", true);
//            map.put("errorMessage", "Missing information for one or more required fields.");
//            map.put("success", Boolean.FALSE);
//            map.put("success", Boolean.FALSE);
//            return map;
//        }
//        form.setCreateDonorSummaryView(true);
//        map.put("contentLabel", "Find Donors");
//        map.put("nextPageUrl", getNextPageUrlForDonorCommunication(request));
//        map.put("donorRowClickUrl", "donorSummary.html");
//        map.put("createDonorSummaryView", form.getCreateDonorSummaryView());
//        return map;
//    }

    public static String getNextPageUrlForDonorCommunication(HttpServletRequest req) {
        String reqUrl = req.getRequestURL().toString().replaceFirst("findDonorCommunicationsForm.html", "findDonorCommunicationsPagination.html");
        String queryString = req.getQueryString();
        if (queryString != null) {
            reqUrl += "?" + queryString;
        }
        return reqUrl;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> findDonorCommunicationsPagination(
            @RequestParam(value="bloodGroups",required=true ) List<String> bloodGroups,
            @RequestParam(value="donorPanels",required=true) List<String> donorPanels,
            @RequestParam(value="clinicDate",required=false ) String clinicDate,
            @RequestParam(value="lastDonationFromDate",required=false ) String lastDonationFromDate,
            @RequestParam(value="lastDonationToDate",required=false ) String lastDonationToDate,
            @RequestParam(value="anyBloodGroup",required=false ) boolean anyBloodGroup) {
       
       LOGGER.debug("Start DonorCommunicationsController:findDonorCommunicationsPagination");
       String eligiblaeClinicDate = getEligibleDonorDate(clinicDate);

        Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("donor");

        Map<String, Object> pagingParams = new HashMap<String, Object>();
        pagingParams.put("sortColumn", "id");
        pagingParams.put("start", "0");
        pagingParams.put("length", "10");
        pagingParams.put("sortDirection", "asc");
        
        List<Object> results = new ArrayList<Object>();
        results = donorCommunicationsRepository.findDonors(setLocations(donorPanels), eligiblaeClinicDate, lastDonationFromDate,
                lastDonationToDate, setBloodGroups(bloodGroups), anyBloodGroup, pagingParams, clinicDate);
        
        System.out.println("\tDONOR COMMS RESULTS: " + results);

        @SuppressWarnings("unchecked")
        List<Donor> donors = (List<Donor>) results.get(0);
        Long totalRecords = (Long) results.get(1);
        
            return generateDatatablesMapForDonorCommunications(donors,
                    totalRecords, formFields);
       
    }

    private static String getEligibleDonorDate(String clinicDate) {

        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            if (clinicDate != null && !clinicDate.trim().equalsIgnoreCase("")) {
                Date dateObj = curFormater.parse(clinicDate);
                @SuppressWarnings({"unused", "deprecation"})
                Date clinicDt = new Date(clinicDate);
                cal.setTime(dateObj);
                cal.add(Calendar.DATE, -(CollectionConstants.BLOCK_BETWEEN_COLLECTIONS));
            }
        } catch (ParseException e) {
            LOGGER.debug("Start DonorCommunicationsController:getEligibleDonorDate:ParseException" + e);
        }
        return clinicDate != null && !clinicDate.trim().equalsIgnoreCase("") ? curFormater
                .format(cal.getTime()) : "";
    }

    private Map<String, Object> generateDatatablesMapForDonorCommunications(List<Donor> donors, Long totalRecords,
            Map<String, Map<String, Object>> formFields) {
        Map<String, Object> donorsMap = new HashMap<String, Object>();
        ArrayList<Object> donorList = new ArrayList<Object>();
        for (DonorViewModel donor : getDonorsViewModels(donors)) {

            List<Object> row = new ArrayList<Object>();
            row.add(donor.getId().toString());
            for (String property : Arrays.asList("donorNumber", "firstName", "lastName", "phoneNumber", "dateOfLastDonation", "bloodGroup", "donorPanel")) {
                if (formFields.containsKey(property)) {
                    Map<String, Object> properties = (Map<String, Object>) formFields.get(property);
                    if (properties.get("hidden").equals(false)) {
                        String propertyValue = property;
                        try {
                            propertyValue = BeanUtils.getProperty(donor, property);
                        } catch (IllegalAccessException e) {
                            LOGGER.debug("DonorCommunicationsController:generateDatatablesMapForDonorCommunications:IllegalAccessException" + e);
                        } catch (InvocationTargetException e) {
                            LOGGER.debug("DonorCommunicationsController:generateDatatablesMapForDonorCommunications:InvocationTargetException" + e);
                        } catch (NoSuchMethodException e) {
                            LOGGER.debug("DonorCommunicationsController:generateDatatablesMapForDonorCommunications:NoSuchMethodException" + e);
                        }
                        row.add(propertyValue != null ? propertyValue.toString() : "");
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

    public static String getUrl(HttpServletRequest req) {
        String reqUrl = req.getRequestURL().toString();
        String queryString = req.getQueryString(); // d=789
        if (queryString != null) {
            reqUrl += "?" + queryString;
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

    private void addEditSelectorOptions(Map<String, Object> m) {
        m.put("donorPanels", locationRepository.getAllDonorPanels());
        m.put("bloodGroups", BloodGroup.getBloodgroups());
    }

    /**
     * Get column name from column id, depends on sequence of columns in
     * donorsCommunicationTable.jsp
     */
    private String getSortingColumn(int columnId,
            Map<String, Map<String, Object>> formFields) {

        List<String> visibleFields = new ArrayList<String>();
        visibleFields.add("id");
        for (String field : Arrays.asList("donorNumber", "firstName", "lastName", "gender", "bloodGroup", "birthDate")) {
            Map<String, Object> fieldProperties = (Map<String, Object>) formFields.get(field);
            if (fieldProperties.get("hidden").equals(false)) {
                visibleFields.add(field);
            }
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

        if (sortColumnMap.get(sortColumn) == null) {
            return "id";
        } else {
            return sortColumnMap.get(sortColumn);
        }
    }


    public List<Location> setLocations(List<String> donorPanels) {

        List<Location> panels = new ArrayList<Location>();

        for (String donorPanelId : donorPanels) {
            Location l = new Location();
            l.setId(Long.parseLong(donorPanelId));
            panels.add(l);
        }
        
        return panels;
    }
    
    public List<BloodGroup> setBloodGroups(List<String> bloodGroups) {

        List<BloodGroup> bloodGroupsList = new ArrayList<BloodGroup>();

        for (String bloodGroup : bloodGroups) {
        	BloodGroup bg = new BloodGroup(bloodGroup);
            bloodGroupsList.add(bg);
        }

        return bloodGroupsList;
    }

}
