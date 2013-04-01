package controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import model.admin.ConfigPropertyConstants;
import model.collectedsample.CollectedSample;
import model.collectedsample.CollectedSampleBackingForm;
import model.collectedsample.CollectedSampleBackingFormValidator;
import model.collectedsample.FindCollectedSampleBackingForm;
import model.donor.Donor;
import model.worksheet.WorksheetBackingForm;

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

import repository.BloodBagTypeRepository;
import repository.CollectedSampleRepository;
import repository.DonationTypeRepository;
import repository.DonorRepository;
import repository.GenericConfigRepository;
import repository.LocationRepository;
import repository.PreDonationTestRepository;
import viewmodel.CollectedSampleViewModel;

@Controller
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
  private DonorRepository donorRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository;

  @Autowired
  private PreDonationTestRepository preDonationTestRepository;

  @Autowired
  private UtilController utilController;

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

  private String getNextPageUrl(HttpServletRequest request) {
    String reqUrl = request.getRequestURL().toString().replaceFirst("findCollection.html", "findCollectionPagination.html");
    String queryString = request.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "/findCollectionFormGenerator", method = RequestMethod.GET)
  public ModelAndView findCollectionFormGenerator(HttpServletRequest request, Model model) {

    FindCollectedSampleBackingForm form = new FindCollectedSampleBackingForm();
    model.addAttribute("findCollectedSampleForm", form);

    ModelAndView mv = new ModelAndView("collections/findCollectionForm");
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "collectedSamples.find");
    mv.addObject("tips", tips);
    // to ensure custom field names are displayed in the form
    mv.addObject("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    mv.addObject("refreshUrl", getUrl(request));
    return mv;
  }

  @RequestMapping("/findCollection")
  public ModelAndView findCollection(HttpServletRequest request,
      @ModelAttribute("findCollectionForm") FindCollectedSampleBackingForm form,
      BindingResult result, Model model) {

    List<CollectedSample> collections = Arrays.asList(new CollectedSample[0]);

    ModelAndView modelAndView = new ModelAndView("collections/collectionsTable");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findProductsTable");
    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    m.put("allCollectedSamples", getCollectionViewModels(collections));
    m.put("refreshUrl", getUrl(request));
    m.put("nextPageUrl", getNextPageUrl(request));
    m.put("saveToWorksheetUrl", getWorksheetUrl(request));
    addEditSelectorOptions(m);

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  private String getWorksheetUrl(HttpServletRequest request) {
    String worksheetUrl = request.getRequestURL().toString().replaceFirst("findCollection.html", "saveFindCollectionsResultsToWorksheet.html");
    String queryString = request.getQueryString();   // d=789
    if (queryString != null) {
        worksheetUrl += "?" + queryString;
    }
    return worksheetUrl;
  }

  /**
   * Get column name from column id, depends on sequence of columns in collectionsTable.jsp
   */
  private String getSortingColumn(int columnId, Map<String, Object> formFields) {

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

  @RequestMapping("/findCollectionPagination")
  public @ResponseBody Map<String, Object> findCollectionPagination(HttpServletRequest request,
      @ModelAttribute("findCollectedSampleForm") FindCollectedSampleBackingForm form,
      BindingResult result, Model model) {

    Map<String, Object> pagingParams = utilController.parsePagingParameters(request);
    int sortColumnId = (Integer) pagingParams.get("sortColumnId");
    Map<String, Object> formFields = utilController.getFormFieldsForForm("collectedSample");
    pagingParams.put("sortColumn", getSortingColumn(sortColumnId, formFields));

    String collectionNumber = form.getCollectionNumber();
    boolean includeUntestedCollections = form.getIncludeTestedCollections();
    if (collectionNumber != null)
      collectionNumber = collectionNumber.trim();
    String dateCollectedFrom = form.getDateCollectedFrom();
    String dateCollectedTo = form.getDateCollectedTo();

    List<Integer> bloodBagTypeIds = new ArrayList<Integer>();
    bloodBagTypeIds.add(-1);
    if (form.getBloodBagTypes() != null) {
      for (String bloodBagTypeId : form.getBloodBagTypes()) {
        bloodBagTypeIds.add(Integer.parseInt(bloodBagTypeId));
      }
    }

    List<Long> centerIds = new ArrayList<Long>();
    centerIds.add((long) -1);
    if (form.getCollectionCenters() != null) {
      for (String center : form.getCollectionCenters()) {
        centerIds.add(Long.parseLong(center));
      }
    }

    List<Long> siteIds = new ArrayList<Long>();
    siteIds.add((long) -1);
    if (form.getCollectionSites() != null) {
      for (String site : form.getCollectionSites()) {
        siteIds.add(Long.parseLong(site));
      }
    }

    List<Object> results = collectedSampleRepository.findCollectedSamples(
                                        form.getCollectionNumber(),
                                        bloodBagTypeIds, centerIds, siteIds,
                                        dateCollectedFrom, dateCollectedTo, includeUntestedCollections, pagingParams);

    @SuppressWarnings("unchecked")
    List<CollectedSample> collectedSamples = (List<CollectedSample>) results.get(0);
    Long totalRecords = (Long) results.get(1);

    return generateDatatablesMap(collectedSamples, totalRecords, formFields);
  }

  /**
   * Datatables on the client side expects a json response for rendering data from the server
   * in jquery datatables. Remember of columns is important and should match the column headings
   * in collectionsTable.jsp.
   */
  private Map<String, Object> generateDatatablesMap(List<CollectedSample> collectedSamples, Long totalRecords, Map<String, Object> formFields) {
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
    m.put("bloodBagTypes", bloodBagTypeRepository.getAllBloodBagTypes());
    m.put("sites", locationRepository.getAllCollectionSites());
  }

  @RequestMapping(value = "/addCollectionFormGenerator", method = RequestMethod.GET)
  public ModelAndView addCollectionFormGenerator(HttpServletRequest request,
      Model model) {

    CollectedSampleBackingForm form = new CollectedSampleBackingForm();

    ModelAndView mv = new ModelAndView("collections/addCollectionForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("addCollectionForm", form);
    mv.addObject("refreshUrl", getUrl(request));
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> formFields = utilController.getFormFieldsForForm("collectedSample");
    // to ensure custom field names are displayed in the form
    mv.addObject("collectionFields", formFields);
    mv.addObject("preDonationTests", preDonationTestRepository.getAllConfiguredPreDonationTests());
    return mv;
  }

  @RequestMapping(value = "/addCollection", method = RequestMethod.POST)
  public ModelAndView addCollection(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("addCollectionForm") @Valid CollectedSampleBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView();
    boolean success = false;

    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> formFields = utilController.getFormFieldsForForm("collectedSample");
    mv.addObject("collectionFields", formFields);

    CollectedSample savedCollection = null;
    if (result.hasErrors()) {
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        CollectedSample collectedSample = form.getCollectedSample();
        collectedSample.setIsDeleted(false);
        savedCollection = collectedSampleRepository.addCollectedSample(collectedSample);
        mv.addObject("hasErrors", false);
        success = true;
        form = new CollectedSampleBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
      }
    }

    if (success) {
      mv.addObject("collectionId", savedCollection.getId());
      mv.addObject("collectedSample", getCollectionViewModel(savedCollection));
      mv.addObject("addAnotherCollectionUrl", "addCollectionFormGenerator.html");
      mv.setViewName("collections/addCollectionSuccess");
    } else {
      mv.addObject("errorMessage", "Error creating collection. Please fix the errors noted below.");
      mv.addObject("firstTimeRender", false);
      mv.addObject("addCollectionForm", form);
      mv.addObject("refreshUrl", "addCollectionFormGenerator.html");
      mv.setViewName("collections/addCollectionError");
    }

    mv.addObject("success", success);
    return mv;
  }

  private CollectedSampleViewModel getCollectionViewModel(CollectedSample collection) {
    CollectedSampleViewModel collectionViewModel = new CollectedSampleViewModel(collection);
    return collectionViewModel;
  }

  @RequestMapping(value = "/updateCollectedSample", method = RequestMethod.POST)
  public ModelAndView updateCollectedSample(
      HttpServletResponse response,
      @ModelAttribute("editCollectedSampleForm") @Valid CollectedSampleBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editCollectedSampleForm");
    boolean success = false;
    String message = "";
    Map<String, Object> m = model.asMap();
    addEditSelectorOptions(m);
    // only when the collection is correctly added the existingCollectedSample
    // property will be changed
    m.put("existingCollectedSample", true);

    // IMPORTANT: Validation code just checks if the ID exists.
    // We still need to store the collected sample as part of the product.
    String donorNumber = form.getDonorNumber();
    if (donorNumber != null && !donorNumber.isEmpty()) {
      try {
        Donor donor = donorRepository.findDonorByDonorNumber(donorNumber);
        form.setDonor(donor);
      } catch (NoResultException ex) {
        form.setDonor(null);
        ex.printStackTrace();
      }
    } else {
      form.setDonor(null);
    }

    if (result.hasErrors()) {
      m.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      message = "Please fix the errors noted above now!";
    }
    else {
      try {

        form.setIsDeleted(false);
        CollectedSample existingCollectedSample = collectedSampleRepository.updateCollectedSample(form.getCollectedSample());
        if (existingCollectedSample == null) {
          m.put("hasErrors", true);
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          success = false;
          m.put("existingCollectedSample", false);
          message = "Collection does not already exist.";
        }
        else {
          m.put("hasErrors", false);
          success = true;
          message = "Collection Successfully Updated";
        }
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        success = false;
        message = "Collection Already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
   }

    m.put("editCollectedSampleForm", form);
    m.put("success", success);
    m.put("message", message);
    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));

    mv.addObject("model", m);

    return mv;
  }

  public static List<CollectedSampleViewModel> getCollectionViewModels(
      List<CollectedSample> collections) {
    if (collections == null)
      return Arrays.asList(new CollectedSampleViewModel[0]);
    List<CollectedSampleViewModel> collectionViewModels = new ArrayList<CollectedSampleViewModel>();
    for (CollectedSample collection : collections) {
      collectionViewModels.add(new CollectedSampleViewModel(collection));
    }
    return collectionViewModels;
  }

  @RequestMapping(value = "/deleteCollectedSample", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> deleteCollection(
      @RequestParam("collectedSampleId") Long collectionSampleId) {

    boolean success = true;
    String errMsg = "";
    try {
      collectedSampleRepository.deleteCollectedSample(collectionSampleId);
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

  @RequestMapping(value = "/collectionSummary", method = RequestMethod.GET)
  public ModelAndView collectionSummaryGenerator(HttpServletRequest request, Model model,
      @RequestParam(value = "collectionId", required = false) Long collectedSampleId) {

    ModelAndView mv = new ModelAndView("collections/collectionSummary");

    mv.addObject("requestUrl", getUrl(request));

    CollectedSample collectedSample = null;
    if (collectedSampleId != null) {
      collectedSample = collectedSampleRepository.findCollectedSampleById(collectedSampleId);
      if (collectedSample != null) {
        mv.addObject("existingCollectedSample", true);
      }
      else {
        mv.addObject("existingCollectedSample", false);
      }
    }

    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "collections.findcollection.collectionsummary");
    mv.addObject("tips", tips);

    CollectedSampleViewModel collectionViewModel = getCollectionViewModel(collectedSample);
    mv.addObject("collectedSample", collectionViewModel);

    mv.addObject("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    mv.addObject("collectionFields", utilController.getFormFieldsForForm("collectedSample"));
    return mv;
  }

  @RequestMapping(value="/saveFindCollectionsResultsToWorksheet", method = RequestMethod.GET)
  public ModelAndView saveFindCollectionsResultsToWorksheet(HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("findCollectedSampleForm") WorksheetBackingForm form,
      BindingResult result, Model model) {

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
    System.out.println(worksheetNumber);
    ModelAndView mv = new ModelAndView("worksheetSaved");
    Map<String, Object> m = model.asMap();
    m.put("worksheetBatchId", worksheetNumber);
    try {
      collectedSampleRepository.saveToWorksheet(
                                        form.getCollectionNumber(),
                                        bloodBagTypeIds, centerIds, siteIds,
                                        dateCollectedFrom, dateCollectedTo,
                                        form.getIncludeTestedCollections(),
                                        worksheetNumber);
      m.put("success", true);
    } catch (Exception ex) {
      ex.printStackTrace();
      m.put("success", false);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    mv.addObject("model", m);
    
    return mv;
  }

  @RequestMapping(value="/findCollectionsWorksheet", method=RequestMethod.GET)
  public ModelAndView findCollectionsWorksheet(HttpServletRequest request, Model model,
      @RequestParam(value="worksheetBatchId") String worksheetBatchId) {

    List<CollectedSample> collectedSamples = collectedSampleRepository.findCollectionsInWorksheet(worksheetBatchId);
    ModelAndView mv = new ModelAndView("collectionsWorksheet");

    Map<String, Object> m = new HashMap<String, Object>();
    if (collectedSamples == null) {
      m.put("worksheetFound", false);
    } else {
      m.put("worksheetFound", true);
      m.put("allCollectedSamples", getCollectionViewModels(collectedSamples));
//      m.put("bloodTests", bloodTestRepository.getAllBloodTests());
      List<String> propertyOwners = Arrays.asList(ConfigPropertyConstants.COLLECTIONS_WORKSHEET);
      m.put("worksheetConfig", genericConfigRepository.getConfigProperties(propertyOwners));
    }

    m.put("worksheetBatchId", worksheetBatchId);
    mv.addObject("model", m);

    return mv;
  }
}
