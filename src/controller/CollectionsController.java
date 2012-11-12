package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;

import model.CollectedSample;
import model.CollectedSampleBackingForm;
import model.RecordFieldsConfig;
import model.util.Location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectionRepository;
import repository.DisplayNamesRepository;
import repository.LocationRepository;
import repository.RecordFieldsConfigRepository;
import utils.ControllerUtil;
import viewmodel.CollectionViewModel;

@Controller
public class CollectionsController {
  @Autowired
  private CollectionRepository collectionRepository;

  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private DisplayNamesRepository displayNamesRepository;
  @Autowired
  private RecordFieldsConfigRepository recordFieldsConfigRepository;

  public CollectionsController() {
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "/findCollectionFormGenerator", method = RequestMethod.GET)
  public ModelAndView findCollectionFormInit(Model model) {

    CollectedSampleBackingForm form = new CollectedSampleBackingForm();
    model.addAttribute("findCollectionForm", form);

    ModelAndView mv = new ModelAndView("findCollectionForm");
    Map<String, Object> m = model.asMap();
    addCentersToModel(m);
    // to ensure custom field names are displayed in the form
    ControllerUtil.addCollectionDisplayNamesToModel(m, displayNamesRepository);
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping("/findCollection")
  public ModelAndView findCollection(HttpServletRequest request,
      @ModelAttribute("findCollectionForm") CollectedSampleBackingForm form,
      BindingResult result, Model model) {

    List<CollectedSample> collections = collectionRepository
        .findAnyCollectionMatching(form.getCollectionNumber(),
            form.getSampleNumber(), form.getShippingNumber(),
            form.getDateCollectedFrom(), form.getDateCollectedTo(),
            form.getCenters());

    ModelAndView modelAndView = new ModelAndView("collectionsTable");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findCollectionResultsTable");
    ControllerUtil.addCollectionDisplayNamesToModel(m, displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("collection", m,
        recordFieldsConfigRepository);
    m.put("allCollections", getCollectionViewModels(collections));
    m.put("requestUrl", getUrl(request));
    addCentersToModel(m);
    addCollectionSitesToModel(m);

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  @RequestMapping(value = "/editCollectionFormGenerator", method = RequestMethod.GET)
  public ModelAndView editCollectionFormGenerator(HttpServletRequest request,
      Model model,
      @RequestParam(value = "collectionNumber", required = false) String collectionNumber,
      @RequestParam(value = "donorNumber", required = false) String donorNumber,
      @RequestParam(value = "isDialog", required = false) String isDialog) {

    CollectedSampleBackingForm form = new CollectedSampleBackingForm();
    Map<String, Object> m = model.asMap();
    List<String> centers = locationRepository.getAllCentersAsString();
    m.put("centers", centers);
    m.put("selectedCenter", centers.get(0));
    List<String> sites = locationRepository.getAllCollectionSitesAsString();
    m.put("sites", sites);
    m.put("selectedSite", sites.get(0));
    m.put("isDialog", isDialog);
    m.put("requestUrl", getUrl(request));
    if (collectionNumber != null) {
      form.setCollectionNumber(collectionNumber);
      CollectedSample collection = collectionRepository
          .findCollectionByNumber(collectionNumber);
      if (collection != null) {
        form = new CollectedSampleBackingForm(collection);
        m.put("selectedCenter",collection.getCenter().getName());
        m.put("selectedSite",collection.getSite().getName());
      } else
        form = new CollectedSampleBackingForm();
    }
    else if (donorNumber != null){
      form.setDonor(donorNumber);
    }

    m.put("editCollectionForm", form);
    // to ensure custom field names are displayed in the form
    ControllerUtil.addCollectionDisplayNamesToModel(m, displayNamesRepository);
    ModelAndView mv = new ModelAndView("editCollectionForm");
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/updateCollection", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> updateOrAddCollection(
      @ModelAttribute("editCollectionForm") CollectedSampleBackingForm form,
      BindingResult result, Model model) {

    boolean success = true;
    String errMsg = "";
    try {
      CollectedSample collection = form.getCollection();
      String center = form.getCenters().get(0);
      Long centerId = locationRepository.getIDByName(center);
      collection.setCenter(centerId);
      String site = form.getSites().get(0);
      Long siteId = locationRepository.getIDByName(site);
      collection.setSite(siteId);
      collectionRepository.updateOrAddCollection(collection);
    } catch (EntityExistsException ex) {
      // TODO: Replace with logger
      System.err.println("Entity Already exists");
      System.err.println(ex.getMessage());
      success = false;
      errMsg = "Collection Already Exists";
    } catch (Exception ex) {
      // TODO: Replace with logger
      ex.printStackTrace();
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

  private List<CollectionViewModel> getCollectionViewModels(
      List<CollectedSample> collections) {
    if (collections == null)
      return Arrays.asList(new CollectionViewModel[0]);
    List<CollectionViewModel> collectionViewModels = new ArrayList<CollectionViewModel>();
    for (CollectedSample collection : collections) {
      collectionViewModels.add(new CollectionViewModel(collection,
          locationRepository.getAllCollectionSites(), locationRepository
              .getAllCenters()));
    }
    return collectionViewModels;
  }

  @RequestMapping("/collections")
  public ModelAndView getCollections(HttpServletRequest request) {
    ModelAndView modelAndView = new ModelAndView("collections");
    Map<String, Object> model = new HashMap<String, Object>();
    addCentersToModel(model);
    addCollectionSitesToModel(model);
    ControllerUtil.addCollectionDisplayNamesToModel(model,
        displayNamesRepository);

    ControllerUtil.addFieldsToDisplay("collection", model,
        recordFieldsConfigRepository);
    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping(value = "/addCollectionsFormTab", method = RequestMethod.GET)
  public ModelAndView addCollectionFormTabInit(Model model) {
    ModelAndView mv = new ModelAndView("editCollectionForm");
    Map<String, Object> m = model.asMap();
    m.put("editCollectionForm", new CollectedSampleBackingForm());
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/deleteCollection", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> deleteCollection(
      @RequestParam("collectionNumber") String collectionNumber) {

    boolean success = true;
    String errMsg = "";
    try {
      collectionRepository.deleteCollection(collectionNumber);
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
  
  private Long getParam(Map<String, String> params, String paramName) {
    String paramValue = params.get(paramName);
    return paramValue == null || paramValue.isEmpty() ? null : Long
        .parseLong(paramValue);
  }

  private Date getDate(String dateParam) {
    DateFormat formatter;
    formatter = new SimpleDateFormat("MM/dd/yyyy");
    Date collectionDate = null;
    try {
      String collectionDateEntered = dateParam;
      if (collectionDateEntered.length() > 0) {
        collectionDate = (Date) formatter.parse(collectionDateEntered);
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return collectionDate;
  }

  private void addCentersToModel(Map<String, Object> model) {
    List<String> allCenters = locationRepository.getAllCentersAsString();
    model.put("centers", allCenters);
  }

  private void addCollectionSitesToModel(Map<String, Object> model) {
    List<Location> allCollectionSites = locationRepository
        .getAllCollectionSites();
    model.put("collectionSites", allCollectionSites);
  }

}
