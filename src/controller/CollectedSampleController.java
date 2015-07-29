package controller;

import backingform.CollectedSampleBackingForm;
import backingform.validator.CollectedSampleBackingFormValidator;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import model.collectedsample.CollectedSample;
import model.collectedsample.HaemoglobinLevel;
import model.donor.Donor;
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
import viewmodel.PackTypeViewModel;
import model.bloodbagtype.BloodBagType;

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

      for (String property : Arrays.asList("collectionNumber", "collectedOn", "bloodBagType", "donorPanel")) {
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
	m.put("donorPanels", locationRepository.getAllDonorPanels());
    m.put("donationTypes", donorTypeRepository.getAllDonationTypes());
    m.put("packTypes", getPackTypeViewModels(bloodBagTypeRepository.getAllBloodBagTypes())); 
    List<Map<String, Object>> haemoglobinLevels = new ArrayList<>();
    for (HaemoglobinLevel value : HaemoglobinLevel.values()) {
        Map<String, Object> haemoglobinLevel = new HashMap<>();
        haemoglobinLevel.put("value", value.name());
        haemoglobinLevel.put("label", value.getLabel());
        haemoglobinLevels.add(haemoglobinLevel);
    }
    m.put("haemoglobinLevels", haemoglobinLevels);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_INFORMATION+"')")
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
      CollectedSample collectedSample = form.getCollectedSample();

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
  public ResponseEntity<Map<String, Object>>
  	  updateCollectedSample(@RequestBody  @Valid CollectedSampleBackingForm form, @PathVariable Long id) {
	  
	  HttpStatus httpStatus = HttpStatus.OK;
	  Map<String, Object> map = new HashMap<String, Object>();
	  CollectedSample updatedCollectedSample = null;
	  
      form.setId(id);
      form.setIsDeleted(false);
      updatedCollectedSample = collectedSampleRepository.updateCollectedSample(form.getCollectedSample());
            
      map.put("donation", getCollectionViewModel(collectedSampleRepository.findCollectedSampleById(updatedCollectedSample.getId())));
      return new ResponseEntity<Map<String, Object>>(map, httpStatus);

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
     @RequestParam(value = "panels",required = false)  List<Long> panelIds,
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
                  bloodBagTypeIds, panelIds,
                  dateCollectedFrom, dateCollectedTo, includeTestedCollections, pagingParams);
  
    @SuppressWarnings("unchecked")
    List<CollectedSample> collectedSamples = (List<CollectedSample>) results.get(0);
    Long totalRecords = (Long) results.get(1);

    return generateDatatablesMap(collectedSamples, totalRecords, formFields);
  }
     
  private List<PackTypeViewModel> getPackTypeViewModels(List<BloodBagType> packTypes){     
       List<PackTypeViewModel> viewModels = new ArrayList<PackTypeViewModel>();
       for(BloodBagType packtType : packTypes){
           viewModels.add(new PackTypeViewModel(packtType));
       }
       return viewModels;
   }
}
