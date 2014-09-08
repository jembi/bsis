package controller;

import backingform.CollectionBatchBackingForm;
import backingform.validator.CollectionBatchBackingFormValidator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import model.collectionbatch.CollectionBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import repository.CollectionBatchRepository;
import repository.LocationRepository;
import utils.PermissionConstants;
import viewmodel.CollectionBatchViewModel;

@Controller
@RequestMapping("/collectionbatch")
public class CollectionBatchController {

  @Autowired
  private CollectionBatchRepository collectionBatchRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private UtilController utilController;

  public CollectionBatchController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new CollectionBatchBackingFormValidator(binder.getValidator(),
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

  @RequestMapping(value = "/findCollectionBatchFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public @ResponseBody Map<String, Object> findCollectionFormGenerator(HttpServletRequest request) {

    Map<String, Object> map = new HashMap<String, Object>();
    addEditSelectorOptions(map);
    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "collectionbatch.find");
    map.put("tips", tips);
    // to ensure custom field names are displayed in the form
    map.put("collectionBatchFields", utilController.getFormFieldsForForm("collectionBatch"));
    map.put("refreshUrl", getUrl(request));
    return map;
  }

  @RequestMapping(value = "/findCollectionBatch", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public @ResponseBody Map<String, Object> findCollectionBatch(HttpServletRequest request,
          @RequestParam(value = "batchNumber", required = false) String batchNumber,
          @RequestParam(value = "collectionCenters", required = false) List<String> collectionCenters,
          @RequestParam(value = "collectionSites", required = false) List<String> collectionSites ) {

    List<Long> centerIds = new ArrayList<Long>();
    centerIds.add((long) -1);
    if (collectionCenters != null) {
      for (String center : collectionCenters) {
        centerIds.add(Long.parseLong(center));
      }
    }

    List<Long> siteIds = new ArrayList<Long>();
    siteIds.add((long) -1);
    if (collectionSites != null) {
      for (String site : collectionSites) {
        siteIds.add(Long.parseLong(site));
      }
    }

    List<CollectionBatch> collectionBatches =
        collectionBatchRepository.findCollectionBatches(batchNumber, centerIds, siteIds);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("collectionBatchFields", utilController.getFormFieldsForForm("collectionBatch"));
    map.put("allCollectionBatches", getCollectionBatchViewModels(collectionBatches));
    map.put("refreshUrl", getUrl(request));

    addEditSelectorOptions(map);

    return map;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("centers", locationRepository.getAllCenters());
    m.put("sites", locationRepository.getAllCollectionSites());
  }

  @RequestMapping(value = "/addform", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONATION_BATCH+"')")
  public @ResponseBody  Map<String, Object> addCollectionBatchFormGenerator(HttpServletRequest request) {

    CollectionBatchBackingForm form = new CollectionBatchBackingForm();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("requestUrl", getUrl(request));
    map.put("firstTimeRender", true);
    map.put("addCollectionBatchForm", form);
    map.put("refreshUrl", getUrl(request));
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("collectionbatch");
    addEditSelectorOptions(map);
    // to ensure custom field names are displayed in the form
    map.put("collectionBatchFields", formFields);
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONATION_BATCH+"')") 
  public @ResponseBody Map<String, Object> addCollectionBatch(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestBody @Valid CollectionBatchBackingForm form) {

    Map<String, Object> map = new HashMap<String, Object>();
    boolean success = false;

    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("collectionBatch");
    map.put("collectionBatchFields", formFields);

    CollectionBatch savedCollectionBatch = null;
   
      try {
        CollectionBatch collectionBatch = form.getCollectionBatch();
        collectionBatch.setIsDeleted(false);
        savedCollectionBatch = collectionBatchRepository.addCollectionBatch(collectionBatch);
        map.put("hasErrors", false);
        success = true;
        form = new CollectionBatchBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
      } 

    if (success) {
      map.put("collectionBatchId", savedCollectionBatch.getId());
      map.put("collectionBatch", getCollectionBatchViewModel(savedCollectionBatch));
      map.put("addAnotherCollectionBatchUrl", "addCollectionBatchFormGenerator.html");
    } else {
      map.put("errorMessage", "Error creating collection batch. Please fix the errors noted below.");
      map.put("firstTimeRender", false);
      map.put("addCollectionBatchForm", form);
      map.put("refreshUrl", "addCollectionBatchFormGenerator.html");
    }
    addEditSelectorOptions(map);
    map.put("success", success);
    return map;
  }

  private CollectionBatchViewModel getCollectionBatchViewModel(CollectionBatch collectionBatch) {
    CollectionBatchViewModel collectionBatchViewModel = new CollectionBatchViewModel(collectionBatch);
    return collectionBatchViewModel;
  }

  public static List<CollectionBatchViewModel> getCollectionBatchViewModels(
      List<CollectionBatch> collectionBatches) {
    if (collectionBatches == null)
      return Arrays.asList(new CollectionBatchViewModel[0]);
    List<CollectionBatchViewModel> collectionBatchViewModels = new ArrayList<CollectionBatchViewModel>();
    for (CollectionBatch collectionBatch : collectionBatches) {
      collectionBatchViewModels.add(new CollectionBatchViewModel(collectionBatch));
    }
    return collectionBatchViewModels;
  }

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public @ResponseBody Map<String, Object> collectionBatchSummaryGenerator(HttpServletRequest request,
      @RequestParam(value = "collectionBatchId", required = true) Integer collectionBatchId) {

    Map<String, Object> map = new HashMap<String, Object>();

    map.put("requestUrl", getUrl(request));

    CollectionBatch collectionBatch = null;
    if (collectionBatchId != null) {
      collectionBatch = collectionBatchRepository.findCollectionBatchById(collectionBatchId);
      if (collectionBatch != null) {
        map.put("existingCollectionBatch", true);
      }
      else {
        map.put("existingCollectionBatch", false);
      }
    }

    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "collectionBatches.findcollectionbatch.collectionbatchsummary");
    map.put("tips", tips);

    CollectionBatchViewModel collectionBatchViewModel = getCollectionBatchViewModel(collectionBatch);
    map.put("collectionBatch", collectionBatchViewModel);

    map.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    map.put("collectionBatchFields", utilController.getFormFieldsForForm("collectionBatch"));
    return map;
  }
  
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException errors) {
        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put("hasErrors", "true");
        errorMap.put("errorMessage", errors.getMessage());
        errors.printStackTrace();
        for (FieldError error : errors.getBindingResult().getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
      
        return errorMap;
    }
}
