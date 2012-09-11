package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;

import model.Collection;
import model.Donor;
import model.DonorBackingForm;
import model.Location;
import model.RecordFieldsConfig;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Years;
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

import repository.DisplayNamesRepository;
import repository.DonorRepository;
import repository.LocationRepository;
import repository.RecordFieldsConfigRepository;
import utils.ControllerUtil;
import viewmodel.CollectionViewModel;
import viewmodel.DonorViewModel;

@Controller
public class DonorController {

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private DisplayNamesRepository displayNamesRepository;

  @Autowired
  private RecordFieldsConfigRepository recordFieldsConfigRepository;

  public DonorController() {
  }

  @RequestMapping("/donorsLandingPage")
  public ModelAndView getDonorsLandingPage(HttpServletRequest request) {

    ModelAndView modelAndView = new ModelAndView("donorsLandingPage");
    Map<String, Object> model = new HashMap<String, Object>();
    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping("/donors")
  public ModelAndView getDonorsPage(HttpServletRequest request) {

    ModelAndView modelAndView = new ModelAndView("donors");
    Map<String, Object> model = new HashMap<String, Object>();
    ControllerUtil.addDonorDisplayNamesToModel(model, displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("donor", model,
        recordFieldsConfigRepository);
    ControllerUtil.addFieldsToDisplay("collection", model,
        recordFieldsConfigRepository);
    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping(value = "/addDonorFormTab", method = RequestMethod.GET)
  public ModelAndView addDonorFormTabInit() {

    ModelAndView mv = new ModelAndView("addDonor");
    return mv;
  }

  @RequestMapping(value = "/editDonorFormGenerator", method = RequestMethod.GET)
  public ModelAndView editDonorFormGenerator(Model model,
      @RequestParam(value = "donorNumber", required = false) String donorNumber) {

    DonorBackingForm form = new DonorBackingForm();
    ModelAndView mv = new ModelAndView("addDonorForm");
    if (donorNumber != null) {
      form.setDonorNumber(donorNumber);
      Donor donor = donorRepository.findDonorByNumber(donorNumber);
      if (donor != null)
        form = new DonorBackingForm(donor);
      else
        form = new DonorBackingForm();
    }
    model.addAttribute("addDonorForm", form);
    Map<String, Object> m = model.asMap();
    // to ensure custom field names are displayed in the form
    ControllerUtil.addDonorDisplayNamesToModel(m, displayNamesRepository);
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/updateDonor", method = RequestMethod.POST)
  public @ResponseBody
  String updateOrAddDonor(
      @ModelAttribute("addDonorForm") DonorBackingForm form,
      BindingResult result, Model model) {

    boolean success = true;
    String errMsg = "";
    try {
      Donor donor = form.getDonor();
      donorRepository.updateOrAddDonor(donor);
    } catch (EntityExistsException ex) {
      // TODO: Replace with logger
      System.err.println("Entity Already exists");
      System.err.println(ex.getMessage());
      success = false;
      errMsg = "Donor Already Exists";
    } catch (Exception ex) {
      // TODO: Replace with logger
      System.err.println("Internal Exception");
      System.err.println(ex.getMessage());
      success = false;
      errMsg = "Internal Server Error";
    }

    return "{\"success\": \"" + success + "\", \"errMsg\": \"" + errMsg + "\"}";
  }

  @RequestMapping(value = "/addDonor", method = RequestMethod.POST)
  public ModelAndView addDonor(
      @ModelAttribute("addDonorForm") DonorBackingForm form,
      BindingResult result, Model m) {
    ModelAndView modelAndView = new ModelAndView("addDonor");
    return modelAndView;
  }

  @RequestMapping("/updateDonor")
  public ModelAndView updateDonor(@RequestParam Map<String, String> params,
      HttpServletRequest request) {

    Date dob = getDonorDOB(params);

    Integer age = getIntParam(params, "age");

    if (dob == null && age != null) {
      DateTime birthDate = DateTime.now();
      birthDate = birthDate.withDayOfMonth(1);
      birthDate = birthDate.withMonthOfYear(1);
      birthDate = birthDate.withTime(0, 0, 0, 0);
      birthDate = birthDate.minusYears(age);
      dob = birthDate.toDate();
    }
    if (dob != null && age == null) {
      DateMidnight birthDate = new DateMidnight(dob);
      DateTime now = new DateTime();
      age = Years.yearsBetween(birthDate, now).getYears();
    }
    RecordFieldsConfig donorFields = recordFieldsConfigRepository
        .getRecordFieldsConfig("donor");
    Donor donor = new Donor(params.get("donorNumber"),
        ControllerUtil.getOptionalParamValue(params.get("firstName"),
            donorFields, "firstName"), ControllerUtil.getOptionalParamValue(
            params.get("lastName"), donorFields, "lastName"),
        ControllerUtil.getOptionalParamValue(params.get("gender"), donorFields,
            "gender"), ControllerUtil.getOptionalParamValue(
            params.get("bloodType"), donorFields, "bloodType"),
        ControllerUtil.getOptionalParamValue(dob, donorFields, "dateOfBirth"),
        ControllerUtil.getOptionalParamValue(age, donorFields, "age"),
        ControllerUtil.getOptionalParamValue(params.get("address"),
            donorFields, "address"), Boolean.FALSE, "");
    ;
    donor = donorRepository.updateOrAddDonor(donor);
    ModelAndView modelAndView = new ModelAndView("donors");
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("donorUpdated", true);
    model.put("displayDonorNumber", donor.getDonorNumber());
    model.put("displayFirstName", donor.getFirstName());
    model.put("displayLastName", donor.getLastName());
    model.put("hasDonor", true);
    model.put("donor", new DonorViewModel(donor));
    ControllerUtil.addDonorDisplayNamesToModel(model, displayNamesRepository);
    addDonorHistory(donor.getDonorNumber(), model);
    ControllerUtil.addFieldsToDisplay("donor", model,
        recordFieldsConfigRepository);
    ControllerUtil.addFieldsToDisplay("collection", model,
        recordFieldsConfigRepository);
    modelAndView.addObject("model", model);

    return modelAndView;
  }

  @RequestMapping("/deleteDonor")
  public ModelAndView deleteDonor(@RequestParam Map<String, String> params,
      HttpServletRequest request) {

    donorRepository.deleteDonor(Long.valueOf(params.get("donorId")));
    ModelAndView modelAndView = new ModelAndView("donors");
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("donorDeleted", true);
    model.put("donorNumberDeleted", params.get("donorNumber"));
    ControllerUtil.addDonorDisplayNamesToModel(model, displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("donor", model,
        recordFieldsConfigRepository);
    ControllerUtil.addFieldsToDisplay("collection", model,
        recordFieldsConfigRepository);
    modelAndView.addObject("model", model);

    return modelAndView;
  }

  @RequestMapping(value = "/findDonorFormGenerator", method = RequestMethod.GET)
  public ModelAndView findDonorFormInit(Model model) {

    DonorBackingForm form = new DonorBackingForm();
    model.addAttribute("findDonorForm", form);

    ModelAndView mv = new ModelAndView("findDonorForm");
    Map<String, Object> m = model.asMap();
    // to ensure custom field names are displayed in the form
    ControllerUtil.addDonorDisplayNamesToModel(m, displayNamesRepository);
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/findDonor", method = RequestMethod.GET)
  public ModelAndView findDonor(
      @ModelAttribute("findDonorForm") DonorBackingForm form,
      BindingResult result, Model m) {

    String donorNumber = form.getDonorNumber();
    String firstName = form.getFirstName();
    String lastName = form.getLastName();
    List<String> bloodTypes = form.getBloodTypes();

    ModelAndView modelAndView = new ModelAndView("donorsTable");
    List<Donor> donors = donorRepository.findAnyDonor(donorNumber, firstName,
        lastName, bloodTypes);

    Map<String, Object> model = m.asMap();
    model.put("tableName", "findResultsTable");
    ControllerUtil.addDonorDisplayNamesToModel(model, displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("donor", model,
        recordFieldsConfigRepository);
    model.put("allDonors", getDonorsViewModels(donors));
    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping("/selectDonor")
  public ModelAndView selectDonor(@RequestParam Map<String, String> params,
      HttpServletRequest request) {

    Donor donor = donorRepository.findDonorById(getParam(params,
        "selectedDonorId"));
    ModelAndView modelAndView = new ModelAndView("donors");
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("singleDonorFound", true);
    model.put("displayDonorNumber", donor.getDonorNumber());
    model.put("displayFirstName", donor.getFirstName());
    model.put("displayLastName", donor.getLastName());
    model.put("hasDonor", true);
    model.put("donor", new DonorViewModel(donor));
    ControllerUtil.addDonorDisplayNamesToModel(model, displayNamesRepository);
    addDonorHistory(donor.getDonorNumber(), model);
    ControllerUtil.addFieldsToDisplay("donor", model,
        recordFieldsConfigRepository);
    ControllerUtil.addFieldsToDisplay("collection", model,
        recordFieldsConfigRepository);
    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping("/viewDonors")
  public ModelAndView viewAllDonors(@RequestParam Map<String, String> params,
      HttpServletRequest request) {

    List<Donor> allDonors = donorRepository.getAllDonors();
    ModelAndView modelAndView = new ModelAndView("donorsTable");
    Map<String, Object> model = new HashMap<String, Object>();

    model.put("tableName", "viewAllDonors");
    model.put("allDonors", getDonorsViewModels(allDonors));
    ControllerUtil.addDonorDisplayNamesToModel(model, displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("donor", model,
        recordFieldsConfigRepository);
    ControllerUtil.addFieldsToDisplay("collection", model,
        recordFieldsConfigRepository);
    modelAndView.addObject("model", model);
    return modelAndView;
  }

  private List<DonorViewModel> getDonorsViewModels(List<Donor> donors) {
    List<DonorViewModel> donorViewModels = new ArrayList<DonorViewModel>();
    for (Donor donor : donors) {
      donorViewModels.add(new DonorViewModel(donor));
    }
    return donorViewModels;
  }

  private Date getDonorDOB(Map<String, String> params) {

    String dobmonth = params.get("dobmonth");
    String dobday = params.get("dobday");
    String dobyear = params.get("dobyear");

    DateTime dob = new DateTime();
    try {
      dob = dob.withMonthOfYear(Integer.parseInt(dobmonth));
      dob = dob.withDayOfMonth(Integer.parseInt(dobday));
      dob = dob.withYear(Integer.parseInt(dobyear));
      return dob.toDate();
    } catch (NumberFormatException nfe) {
      return null;
    }
  }

  private Long getParam(Map<String, String> params, String paramName) {
    String paramValue = params.get(paramName);
    return paramValue == null || paramValue.isEmpty() ? null : Long
        .parseLong(paramValue);
  }

  private Integer getIntParam(Map<String, String> params, String paramName) {
    String paramValue = params.get(paramName);
    return paramValue == null || paramValue.isEmpty() ? null : Integer
        .parseInt(paramValue);
  }

  private void addDonorHistory(String donorNumber, Map<String, Object> model) {
    List<Collection> donorCollections = donorRepository
        .getDonorHistory(donorNumber);
    if (donorCollections != null) {
      Collections.sort(donorCollections, new Comparator<Collection>() {
        public int compare(Collection collection, Collection collection1) {
          return collection1.getDateCollected().compareTo(
              collection.getDateCollected());
        }
      });
      model.put("donorHistory", getCollectionViewModel(donorCollections));
      ControllerUtil.addCollectionDisplayNamesToModel(model,
          displayNamesRepository);
    }
  }

  private List<CollectionViewModel> getCollectionViewModel(
      List<Collection> donorCollections) {
    ArrayList<CollectionViewModel> collectionViewModels = new ArrayList<CollectionViewModel>();
    List<Location> allCollectionSites = locationRepository
        .getAllCollectionSites();
    List<Location> allCenters = locationRepository.getAllCenters();
    for (Collection donorCollection : donorCollections) {
      collectionViewModels.add(new CollectionViewModel(donorCollection,
          allCollectionSites, allCenters));
    }
    return collectionViewModels;
  }
}
