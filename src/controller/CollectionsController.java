package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.Collection;
import model.CollectionBackingForm;
import model.DonorBackingForm;
import model.Location;
import model.RecordFieldsConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

  @RequestMapping(value = "/findCollectionFormGenerator", method = RequestMethod.GET)
  public ModelAndView findCollectionFormInit(Model model) {

    CollectionBackingForm form = new CollectionBackingForm();
    model.addAttribute("findCollectionForm", form);

    ModelAndView mv = new ModelAndView("findCollectionForm");
    Map<String, Object> m = model.asMap();
    addCentersToModel(m);
    // to ensure custom field names are displayed in the form
    ControllerUtil.addCollectionDisplayNamesToModel(m, displayNamesRepository);
    mv.addObject("model", m);
    return mv;
  }
  
  @RequestMapping("/viewCollections")
  public ModelAndView viewAllCollections(
      @RequestParam Map<String, String> params, HttpServletRequest request) {

    List<Collection> allCollections = collectionRepository.getAllCollections();
    ModelAndView modelAndView = new ModelAndView("collectionsTable");
    Map<String, Object> model = new HashMap<String, Object>();

    model.put("tableName", "viewAllCollections");
    model.put("allDonors", getCollectionViewModels(allCollections));
    ControllerUtil.addCollectionDisplayNamesToModel(model,
        displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("collection", model,
        recordFieldsConfigRepository);
    ControllerUtil.addFieldsToDisplay("collection", model,
        recordFieldsConfigRepository);
    modelAndView.addObject("model", model);
    return modelAndView;
  }

  private List<CollectionViewModel> getCollectionViewModels(
      List<Collection> collections) {
    List<CollectionViewModel> collectionViewModels = new ArrayList<CollectionViewModel>();
    for (Collection collection : collections) {
      collectionViewModels.add(new CollectionViewModel(collection));
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
  public ModelAndView addDonorFormTabInit() {
    ModelAndView mv = new ModelAndView("addCollections");
    return mv;
  }

  @RequestMapping("/addCollection")
  public ModelAndView addCollection(@RequestParam Map<String, String> params,
      HttpServletRequest request) {

    RecordFieldsConfig collectionFields = recordFieldsConfigRepository
        .getRecordFieldsConfig("collection");
    Collection collection = new Collection(
        params.get("collectionNumber"),
        ControllerUtil.getOptionalParamValue(
            getParam(params, "collectionCenter"), collectionFields, "center"),
        ControllerUtil.getOptionalParamValue(
            getParam(params, "collectionSite"), collectionFields, "site"),
        getDate(params.get("collectionDate")),
        ControllerUtil.getOptionalParamValue(getParam(params, "sampleNumber"),
            collectionFields, "sampleNo"),
        ControllerUtil.getOptionalParamValue(
            getParam(params, "shippingNumber"), collectionFields, "shippingNo"),
        ControllerUtil.getOptionalParamValue(
            params.get("collectionDonorNumber"), collectionFields, "donorNo"),
        ControllerUtil.getOptionalParamValue(params.get("donorType"),
            collectionFields, "donorType"), Boolean.FALSE, ControllerUtil
            .getOptionalParamValue(params.get("collectionComment"),
                collectionFields, "comment"));
    collectionRepository.saveCollection(collection);
    ModelAndView modelAndView = new ModelAndView("collections");
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("addedCollection", true);
    model.put("hasCollection", true);
    model.put("collection", new CollectionViewModel(collection));
    addCentersToModel(model);
    addCollectionSitesToModel(model);
    ControllerUtil.addCollectionDisplayNamesToModel(model,
        displayNamesRepository);

    ControllerUtil.addFieldsToDisplay("collection", model,
        recordFieldsConfigRepository);

    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping("/updateCollection")
  public ModelAndView updateCollection(
      @RequestParam Map<String, String> params, HttpServletRequest request) {
    Long collectionId = getParam(params, "updateCollectionId");
    RecordFieldsConfig collectionFields = recordFieldsConfigRepository
        .getRecordFieldsConfig("collection");

    Collection collection = new Collection(
        params.get("updateCollectionNumber"),
        ControllerUtil.getOptionalParamValue(
            getParam(params, "updateCollectionCenter"), collectionFields,
            "center"),
        ControllerUtil.getOptionalParamValue(
            getParam(params, "updateCollectionSite"), collectionFields, "site"),
        getDate(params.get("updateCollectionDate")), ControllerUtil
            .getOptionalParamValue(getParam(params, "updateSampleNumber"),
                collectionFields, "sampleNo"), ControllerUtil
            .getOptionalParamValue(getParam(params, "updateShippingNumber"),
                collectionFields, "shippingNo"), ControllerUtil
            .getOptionalParamValue(params.get("updateCollectionDonorNumber"),
                collectionFields, "donorNo"), ControllerUtil
            .getOptionalParamValue(params.get("updateDonorType"),
                collectionFields, "donorType"), Boolean.FALSE, ControllerUtil
            .getOptionalParamValue(params.get("updateCollectionComment"),
                collectionFields, "comment"));
    Collection existingCollection = collectionRepository
        .findCollectionById(collectionId);
    collection.setAbo(existingCollection.getAbo());
    collection.setRhd(existingCollection.getRhd());
    existingCollection = collectionRepository.updateCollection(collection,
        collectionId);
    ModelAndView modelAndView = new ModelAndView("collections");
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("updatedCollection", true);
    model.put("hasCollection", true);
    addCentersToModel(model);
    addCollectionSitesToModel(model);
    ControllerUtil.addCollectionDisplayNamesToModel(model,
        displayNamesRepository);

    ControllerUtil.addFieldsToDisplay("collection", model,
        recordFieldsConfigRepository);

    model.put("collection", new CollectionViewModel(existingCollection));
    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping("/deleteCollection")
  public ModelAndView deleteCollection(
      @RequestParam Map<String, String> params, HttpServletRequest request) {

    Long collectionId = getParam(params, "updateCollectionId");
    String collectionNumber = params.get("updateCollectionNumber");

    collectionRepository.deleteCollection(collectionId);
    ModelAndView modelAndView = new ModelAndView("collections");
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("deletedCollection", true);
    model.put("collectionIdDeleted", collectionNumber);
    addCentersToModel(model);
    addCollectionSitesToModel(model);
    ControllerUtil.addCollectionDisplayNamesToModel(model,
        displayNamesRepository);

    ControllerUtil.addFieldsToDisplay("collection", model,
        recordFieldsConfigRepository);

    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping("/findCollection")
  public ModelAndView findCollection(@RequestParam Map<String, String> params,
      HttpServletRequest request) {
    String updateCollectionNumber = params.get("updateCollectionNumber");
    Collection collection = collectionRepository
        .findCollection(updateCollectionNumber);

    ModelAndView modelAndView = new ModelAndView("collections");
    Map<String, Object> model = new HashMap<String, Object>();
    if (collection == null) {
      model.put("collectionNotFound", true);
      model.put("collectionNumber", updateCollectionNumber);
    } else {
      model.put("hasCollection", true);
      model.put("collection", new CollectionViewModel(collection));
    }
    addCentersToModel(model);
    addCollectionSitesToModel(model);
    ControllerUtil.addCollectionDisplayNamesToModel(model,
        displayNamesRepository);

    ControllerUtil.addFieldsToDisplay("collection", model,
        recordFieldsConfigRepository);

    modelAndView.addObject("model", model);
    return modelAndView;
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
