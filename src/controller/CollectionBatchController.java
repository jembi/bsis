package controller;

import backingform.CollectionBatchBackingForm;
import backingform.FindCollectionBatchBackingForm;
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
import repository.CollectionBatchRepository;
import repository.LocationRepository;
import utils.PermissionConstants;
import viewmodel.CollectionBatchViewModel;

@Controller
@RequestMapping("/collectionBatch")
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
  public Map<String, Object> findCollectionFormGenerator(HttpServletRequest request, Model model) {

    FindCollectionBatchBackingForm form = new FindCollectionBatchBackingForm();
    model.addAttribute("findCollectionBatchForm", form);

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

  @RequestMapping("/findCollectionBatch")
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public Map<String, Object> findCollectionBatch(HttpServletRequest request,
      @ModelAttribute("findCollectionBatchForm") FindCollectionBatchBackingForm form,
      BindingResult result, Model model) {

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

    List<CollectionBatch> collectionBatches =
        collectionBatchRepository.findCollectionBatches(form.getBatchNumber(), centerIds, siteIds);

    Map<String, Object> modelAndView = new HashMap<String, Object>();
    Map<String, Object> m = model.asMap();
    m.put("collectionBatchFields", utilController.getFormFieldsForForm("collectionBatch"));
    m.put("allCollectionBatches", getCollectionBatchViewModels(collectionBatches));
    m.put("refreshUrl", getUrl(request));

    addEditSelectorOptions(m);

    modelAndView.put("model", m);
    return modelAndView;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("centers", locationRepository.getAllCenters());
    m.put("sites", locationRepository.getAllCollectionSites());
  }

  @RequestMapping(value = "/addform", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONATION_BATCH+"')")
  public @ResponseBody  Map<String, Object> addCollectionBatchFormGenerator(HttpServletRequest request,
      Model model) {

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
      @ModelAttribute("addCollectionBatchForm") @Valid CollectionBatchBackingForm form,
      BindingResult result, Model model) {

    Map<String, Object> map = new HashMap<String, Object>();
    boolean success = false;

    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("collectionBatch");
    map.put("collectionBatchFields", formFields);

    CollectionBatch savedCollectionBatch = null;
    if (result.hasErrors()) {
      map.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
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
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
      }
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
  public @ResponseBody Map<String, Object> collectionBatchSummaryGenerator(HttpServletRequest request, Model model,
      @RequestParam(value = "collectionBatchId", required = false) Integer collectionBatchId) {

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
}
