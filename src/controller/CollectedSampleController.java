package controller;

import backingform.CollectedSampleBackingForm;
import backingform.validator.CollectedSampleBackingFormValidator;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import model.collectedsample.CollectedSample;
import org.apache.commons.beanutils.BeanUtils;
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
import org.springframework.web.bind.annotation.RestController;
import repository.BloodBagTypeRepository;
import repository.CollectedSampleRepository;
import repository.DonationTypeRepository;
import repository.DonorRepository;
import repository.LocationRepository;
import utils.PermissionConstants;
import viewmodel.CollectedSampleViewModel;

@RestController
@RequestMapping("/donations")
public class CollectedSampleController {

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private BloodBagTypeRepository bloodBagTypeRepository;

  @Autowired
  private DonationTypeRepository donorTypeRepository;


  @Autowired
  private UtilController utilController;

  @Autowired
  private DonorRepository donorRepository;
  
  public CollectedSampleController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new CollectedSampleBackingFormValidator(binder.getValidator(),
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

  /**
   * Datatables on the client side expects a json response for rendering data from the server
   * in jquery datatables. Remember of columns is important and should match the column headings
   * in collectionsTable.jsp.
   */
  private Map<String, Object> generateDatatablesMap(List<CollectedSample> collectedSamples, Long totalRecords, Map<String, Map<String, Object>> formFields) {
    Map<String, Object> collectionsMap = new HashMap<String, Object>();

    ArrayList<Object> collectionList = new ArrayList<Object>();

    for (CollectedSampleViewModel collection : getCollectionViewModels(collectedSamples)) {

      List<Object> row = new ArrayList<Object>();
      
      row.add(collection.getId().toString());

      for (String property : Arrays.asList("collectionNumber", "collectedOn", "bloodBagType", "collectionCenter", "collectionSite")) {
        if (formFields.containsKey(property)) {
          Map<String, Object> properties = (Map<String, Object>)formFields.get(property);
          if (properties.get("hidden").equals(false)) {
            String propertyValue = property;
            try {
              propertyValue = BeanUtils.getProperty(collection, property);
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

      collectionList.add(row);
    }
    collectionsMap.put("aaData", collectionList);
    collectionsMap.put("iTotalRecords", totalRecords);
    collectionsMap.put("iTotalDisplayRecords", totalRecords);
    return collectionsMap;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("centers", locationRepository.getAllCenters());
    m.put("donationTypes", donorTypeRepository.getAllDonationTypes());
    m.put("packTypes", bloodBagTypeRepository.getAllBloodBagTypes());
    m.put("sites", locationRepository.getAllCollectionSites());
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONATION+"')")
  public  Map<String, Object> addCollectionFormGenerator(HttpServletRequest request) {

    CollectedSampleBackingForm form = new CollectedSampleBackingForm();

    Map<String, Object> map = new  HashMap<String, Object>();
    map.put("addDonationForm", form);
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("collectedSample");
    // to ensure custom field names are displayed in the form
    map.put("donationFields", formFields);
    return map;
  }

  @RequestMapping(value = "{id}/edit/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_DONATION+"')")
  public  Map<String, Object> editCollectionFormGenerator(HttpServletRequest request,
      @PathVariable Long id) {

    CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(id);
    CollectedSampleBackingForm form = new CollectedSampleBackingForm(collectedSample);
    form.getCollectedSampleIntegerProps();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("editDonationForm", form);
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("collectedSample");
    // to ensure custom field names are displayed in the form
    map.put("donationFields", formFields);
    return map;
  }

  @RequestMapping( method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONATION+"')")
  public  ResponseEntity<Map<String, Object>> addCollection(
      @RequestBody @Valid CollectedSampleBackingForm form) {

      Map<String, Object> map = new HashMap<String, Object>();
      addEditSelectorOptions(map);
      Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("collectedSample");
      map.put("donationFields", formFields);
      CollectedSample savedCollection = null;
      form.setCollectedSample();
      CollectedSample collectedSample = form.getCollectedSample();

         if (collectedSample.getDonor().getDateOfFirstDonation() == null) {
          collectedSample.getDonor().setDateOfFirstDonation(collectedSample.getCollectedOn());
      }

      collectedSample.setIsDeleted(false);
      savedCollection = collectedSampleRepository.addCollectedSample(collectedSample);
      map.put("hasErrors", false);
      form = new CollectedSampleBackingForm();
	
      map.put("donationId", savedCollection.getId());
      map.put("donation", getCollectionViewModel(savedCollection));
      return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
  }

  private CollectedSampleViewModel getCollectionViewModel(CollectedSample collection) {
    CollectedSampleViewModel collectionViewModel = new CollectedSampleViewModel(collection);
    return collectionViewModel;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_DONATION+"')")
  public  HttpStatus updateCollectedSample(
      @RequestBody  @Valid CollectedSampleBackingForm form, @PathVariable Long id) {
      form.setId(id);
      form.setIsDeleted(false);
      form.setCollectedSample();
      collectedSampleRepository.updateCollectedSample(form.getCollectedSample());
      return HttpStatus.OK;
  }

  private List<CollectedSampleViewModel> getCollectionViewModels(
      List<CollectedSample> collections) {
    if (collections == null)
      return Arrays.asList(new CollectedSampleViewModel[0]);
    List<CollectedSampleViewModel> collectionViewModels = new ArrayList<CollectedSampleViewModel>();
    for (CollectedSample collection : collections) {
      collectionViewModels.add(new CollectedSampleViewModel(collection));
    }
    return collectionViewModels;
  }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('" + PermissionConstants.VOID_DONATION + "')")
    public HttpStatus deleteCollection(
            @PathVariable Long id) {
        collectedSampleRepository.deleteCollectedSample(id);
        return HttpStatus.OK;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('" + PermissionConstants.VIEW_DONATION + "')")
    public Map<String, Object> collectionSummaryGenerator(
            @PathVariable Long id) {

        Map<String, Object> map = new HashMap<String, Object>();

        CollectedSample collectedSample = null;
        if (id != null) {
            collectedSample = collectedSampleRepository.findCollectedSampleById(id);
            if (collectedSample != null) {
                map.put("existingDonation", true);
            } else {
                map.put("existingDonation", false);
            }
        }

        CollectedSampleViewModel collectionViewModel = getCollectionViewModel(collectedSample);
        map.put("donation", collectionViewModel);

      
        return map;
    }
    
     @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION+"')")
  public  Map<String, Object> findCollectionPagination(
     @RequestParam(value = "collectionNumber", required = false)  String collectionNumber,
     @RequestParam(value = "centers",required = false)  List<Long> centerIds,
     @RequestParam(value = "sites",required = false)  List<Long> siteIds,
     @RequestParam(value = "bloodBagTypes",required = false)  List<Integer> bloodBagTypeIds,
     @RequestParam(value = "dateCollectedFrom", required = false)  String dateCollectedFrom,
     @RequestParam(value = "dateCollectedTo", required = false)  String dateCollectedTo,
     @RequestParam(value = "includeTestedCollections",required = true)  boolean includeTestedCollections)throws  ParseException{
   
      Map<String, Object> pagingParams = new HashMap<String, Object>();
      pagingParams.put("sortColumn", "id");
//      pagingParams.put("start", "0");
//      pagingParams.put("length", "10");
      pagingParams.put("sortDirection", "asc");
      
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("CollectedSample");
  
    if (collectionNumber != null)
      collectionNumber = collectionNumber.trim();


   /* bloodBagTypeIds.add(-1);
    centerIds.add((long)-1);
    siteIds.add((long)-1);*/

    List<Object> results;
          results = collectedSampleRepository.findCollectedSamples(
                  collectionNumber,
                  bloodBagTypeIds, centerIds, siteIds,
                  dateCollectedFrom, dateCollectedTo, includeTestedCollections, pagingParams);
  
    @SuppressWarnings("unchecked")
    List<CollectedSample> collectedSamples = (List<CollectedSample>) results.get(0);
    Long totalRecords = (Long) results.get(1);

    return generateDatatablesMap(collectedSamples, totalRecords, formFields);
  }

  /**
 * issue #209[Adapt_Bsis_To_Expose_Rest_Services]
 * Reason - no worksheets
  @RequestMapping(value="/saveFindCollectionsResultsToWorksheet", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION+"')")
  public  Map<String, Object> saveFindCollectionsResultsToWorksheet(HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("findCollectedSampleForm") WorksheetBackingForm form) {

    String collectionNumber = form.getCollectionNumber();
    if (collectionNumber != null)
      collectionNumber = collectionNumber.trim();
    String dateCollectedFrom = form.getDateCollectedFrom();
    String dateCollectedTo = form.getDateCollectedTo();

    List<Integer> bloodBagTypeIds = new ArrayList<Integer>();
    if (form.getBloodBagTypes() != null) {
      for (String bloodBagTypeId : form.getBloodBagTypes()) {
        bloodBagTypeIds.add(Integer.parseInt(bloodBagTypeId));
      }
    }

    List<Long> centerIds = new ArrayList<Long>();
    if (form.getCollectionCenters() != null) {
      for (String center : form.getCollectionCenters()) {
        centerIds.add(Long.parseLong(center));
      }
    }

    List<Long> siteIds = new ArrayList<Long>();
    if (form.getCollectionSites() != null) {
      for (String site : form.getCollectionSites()) {
        siteIds.add(Long.parseLong(site));
      }
    }

    String worksheetNumber = form.getWorksheetNumber();
    Map<String, Object> map = new  HashMap<String, Object>();
    map.put("worksheetNumber", worksheetNumber);
    try {
      collectedSampleRepository.saveToWorksheet(
                                        form.getCollectionNumber(),
                                        bloodBagTypeIds, centerIds, siteIds,
                                        dateCollectedFrom, dateCollectedTo,
                                        form.getIncludeTestedCollections(),
                                        worksheetNumber);
      map.put("success", true);
    } catch (Exception ex) {
      ex.printStackTrace();
      map.put("success", false);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    map.put("model", map);
    
    return map;
  }
  */
  /**
   * issue #209[Adapt_Bsis_To_Expose_Rest_Services]
   * Reason - not sure is this method going to be included in later versions
   *
  @RequestMapping(value = "/findLastDonationForDonor", method = RequestMethod.GET)  
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION+"')")
  public   
  Map<String, String> findLastDonationForDonor(@ModelAttribute("addCollectionForm")  CollectedSampleBackingForm form) {  
	   
   CollectedSample collectedSample = form.getCollectedSample();
   long diffInDays =0;
   Date dateofLastDonation = null;
   List<String> message = new ArrayList<String>();
   
   Map<String, String> m = new HashMap<String, String>();
   
   try{
	   // if the donor exists
	   if(donorRepository.findDonorByNumber(collectedSample.getDonor().getDonorNumber()) != null){
		   Donor donor = donorRepository.findDonorByNumber(collectedSample.getDonor().getDonorNumber());
		   
		   // if the donor has donated before
		   if(donor.getDateOfLastDonation() != null){
			   dateofLastDonation = donor.getDateOfLastDonation();
			   Date collectedOnDate = collectedSample.getCollectedOn();
			   
			   long diff = collectedOnDate.getTime() - dateofLastDonation.getTime();
			   diffInDays = diff / (24 * 60 * 60 * 1000);
			 	
			   SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			   m.put("diffInDays",String.valueOf(diffInDays));
			   m.put("dateOfLastDonation",formatter.format(dateofLastDonation).toString());
			   m.put("collectedOnDate",formatter.format(collectedOnDate).toString());
		   }  
	   }
   }
   catch(Exception ex){
	 ex.printStackTrace();
   }
   return m;  
  }  
  */
  /*
  issue - #209[Commented out as un being unused]
 *
  private String getNextPageUrl(HttpServletRequest request) {
    String reqUrl = request.getRequestURL().toString().replaceFirst("findCollection.html", "search.html");
    String queryString = request.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }
  
  

  @RequestMapping(value = "/findform", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION+"')")
  public  Map<String, Object> findCollectionFormGenerator(HttpServletRequest request) {

    Map<String, Object> map = new  HashMap<String, Object>();
    addEditSelectorOptions(map);
    // to ensure custom field names are displayed in the form
    map.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    return map;
  }
*/
  /**
 * issue - #209[Adapt_Bsis_To_Expose_Rest_Services]
 * Reason - duplicate method (see findCollectionPagination method) 
  @RequestMapping(value = "/findCollection" , method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION+"')")
  public  Map<String, Object> findCollection(HttpServletRequest request,
      @ModelAttribute("findCollectionForm") FindCollectedSampleBackingForm form) {

    List<CollectedSample> collections = Arrays.asList(new CollectedSample[0]);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    map.put("allCollectedSamples", getCollectionViewModels(collections));
    map.put("nextPageUrl", getNextPageUrl(request));
    map.put("saveToWorksheetUrl", getWorksheetUrl(request));
    addEditSelectorOptions(map);

    map.put("model", map);
    return map;
  }
  */
  
/**
 * issue #209[Adapt_Bsis_To_Expose_Rest_Services]
 * Reason - worksheet concepts are not used in later versions
 * 
  private String getWorksheetUrl(HttpServletRequest request) {
    String worksheetUrl = request.getRequestURL().toString().replaceFirst("findCollection.html", "saveFindCollectionsResultsToWorksheet.html");
    String queryString = request.getQueryString();   // d=789
    if (queryString != null) {
        worksheetUrl += "?" + queryString;
    }
    return worksheetUrl;
  }
  */

  /**
   * Get column name from column id, depends on sequence of columns in collectionsTable.jsp
   
  private String getSortingColumn(int columnId, Map<String, Map<String, Object>> formFields) {

    List<String> visibleFields = new ArrayList<String>();
    visibleFields.add("id");
    for (String field : Arrays.asList("collectionNumber", "collectedOn","bloodBagType", "collectionCenter", "collectionSite")) {
      Map<String, Object> fieldProperties = (Map<String, Object>) formFields.get(field);
      if (fieldProperties.get("hidden").equals(false))
        visibleFields.add(field);
    }

    Map<String, String> sortColumnMap = new HashMap<String, String>();
    sortColumnMap.put("id", "id");
    sortColumnMap.put("collectionNumber", "collectionNumber");
    sortColumnMap.put("collectedOn", "collectedOn");
    sortColumnMap.put("bloodBagType", "bloodBagType.bloodBagType");
    sortColumnMap.put("collectionCenter", "collectionCenter.name");
    sortColumnMap.put("collectionSite", "collectionSite.name");
    String sortColumn = visibleFields.get(columnId);

    if (sortColumnMap.get(sortColumn) == null)
      return "id";
    else
      return sortColumnMap.get(sortColumn);
  }

 
*/
}
