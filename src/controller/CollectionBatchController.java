package controller;

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
import org.springframework.web.servlet.ModelAndView;

import repository.CollectionBatchRepository;
import repository.LocationRepository;
import utils.PermissionConstants;
import viewmodel.CollectionBatchViewModel;
import backingform.CollectionBatchBackingForm;
import backingform.FindCollectionBatchBackingForm;
import backingform.validator.CollectionBatchBackingFormValidator;

@Controller
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
  public ModelAndView findCollectionFormGenerator(HttpServletRequest request, Model model) {

    FindCollectionBatchBackingForm form = new FindCollectionBatchBackingForm();
    model.addAttribute("findCollectionBatchForm", form);

    ModelAndView mv = new ModelAndView("collectionbatch/findCollectionBatchForm");
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "collectionbatch.find");
    mv.addObject("tips", tips);
    // to ensure custom field names are displayed in the form
    mv.addObject("collectionBatchFields", utilController.getFormFieldsForForm("collectionBatch"));
    mv.addObject("refreshUrl", getUrl(request));
    return mv;
  }

  @RequestMapping("/findCollectionBatch")
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public ModelAndView findCollectionBatch(HttpServletRequest request,
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

    ModelAndView modelAndView = new ModelAndView("collectionbatch/collectionBatchesTable");
    Map<String, Object> m = model.asMap();
    m.put("collectionBatchFields", utilController.getFormFieldsForForm("collectionBatch"));
    m.put("allCollectionBatches", getCollectionBatchViewModels(collectionBatches));
    m.put("refreshUrl", getUrl(request));

    addEditSelectorOptions(m);

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("centers", locationRepository.getAllCenters());
    m.put("sites", locationRepository.getAllCollectionSites());
  }

  @RequestMapping(value = "/addCollectionBatchFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONATION_BATCH+"')")
  public ModelAndView addCollectionBatchFormGenerator(HttpServletRequest request,
      Model model) {

    CollectionBatchBackingForm form = new CollectionBatchBackingForm();

    ModelAndView mv = new ModelAndView("collectionbatch/addCollectionBatchForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("addCollectionBatchForm", form);
    mv.addObject("refreshUrl", getUrl(request));
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("collectionbatch");
    addEditSelectorOptions(mv.getModelMap());
    // to ensure custom field names are displayed in the form
    mv.addObject("collectionBatchFields", formFields);
    return mv;
  }

  @RequestMapping(value = "/addCollectionBatch", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_DONATION_BATCH+"')") 
  public ModelAndView addCollectionBatch(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("addCollectionBatchForm") @Valid CollectionBatchBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView();
    boolean success = false;

    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("collectionBatch");
    mv.addObject("collectionBatchFields", formFields);

    CollectionBatch savedCollectionBatch = null;
    if (result.hasErrors()) {
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        CollectionBatch collectionBatch = form.getCollectionBatch();
        collectionBatch.setIsDeleted(false);
        savedCollectionBatch = collectionBatchRepository.addCollectionBatch(collectionBatch);
        mv.addObject("hasErrors", false);
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
      mv.addObject("collectionBatchId", savedCollectionBatch.getId());
      mv.addObject("collectionBatch", getCollectionBatchViewModel(savedCollectionBatch));
      mv.addObject("addAnotherCollectionBatchUrl", "addCollectionBatchFormGenerator.html");
      mv.setViewName("collectionbatch/addCollectionBatchSuccess");
    } else {
      mv.addObject("errorMessage", "Error creating collection batch. Please fix the errors noted below.");
      mv.addObject("firstTimeRender", false);
      mv.addObject("addCollectionBatchForm", form);
      mv.addObject("refreshUrl", "addCollectionBatchFormGenerator.html");
      mv.setViewName("collectionbatch/addCollectionBatchError");
    }
    addEditSelectorOptions(mv.getModelMap());
    mv.addObject("success", success);
    return mv;
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

  @RequestMapping(value = "/collectionBatchSummary", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_DONATION_BATCH+"')")
  public ModelAndView collectionBatchSummaryGenerator(HttpServletRequest request, Model model,
      @RequestParam(value = "collectionBatchId", required = false) Integer collectionBatchId) {

    ModelAndView mv = new ModelAndView("collectionbatch/collectionBatchSummary");

    mv.addObject("requestUrl", getUrl(request));

    CollectionBatch collectionBatch = null;
    if (collectionBatchId != null) {
      collectionBatch = collectionBatchRepository.findCollectionBatchById(collectionBatchId);
      if (collectionBatch != null) {
        mv.addObject("existingCollectionBatch", true);
      }
      else {
        mv.addObject("existingCollectionBatch", false);
      }
    }

    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "collectionBatches.findcollectionbatch.collectionbatchsummary");
    mv.addObject("tips", tips);

    CollectionBatchViewModel collectionBatchViewModel = getCollectionBatchViewModel(collectionBatch);
    mv.addObject("collectionBatch", collectionBatchViewModel);

    mv.addObject("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    mv.addObject("collectionBatchFields", utilController.getFormFieldsForForm("collectionBatch"));
    return mv;
  }
}
