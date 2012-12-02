package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import model.collectedsample.CollectedSample;
import model.collectedsample.CollectedSampleBackingForm;
import model.collectedsample.CollectedSampleBackingFormValidator;
import model.donor.Donor;
import model.location.Location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.BloodBagTypeRepository;
import repository.CollectedSampleRepository;
import repository.DonorRepository;
import repository.DonorTypeRepository;
import repository.LocationRepository;
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
  private DonorTypeRepository donorTypeRepository;

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private UtilController utilController;

  public CollectedSampleController() {
  }

  @InitBinder
  protected void initBinder(DataBinder binder) {
    binder.setValidator(new CollectedSampleBackingFormValidator(binder.getValidator()));
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
  public ModelAndView findCollectionFormGenerator(Model model) {

    CollectedSampleBackingForm form = new CollectedSampleBackingForm();
    model.addAttribute("findCollectionForm", form);

    ModelAndView mv = new ModelAndView("findCollectionForm");
    Map<String, Object> m = model.asMap();
    addEditSelectorOptions(m);
    // to ensure custom field names are displayed in the form
    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping("/findCollection")
  public ModelAndView findCollection(HttpServletRequest request,
      @ModelAttribute("findCollectionForm") CollectedSampleBackingForm form,
      BindingResult result, Model model) {

    List<CollectedSample> collections = collectedSampleRepository
        .findAnyCollectedSampleMatching(form.getCollectionNumber(),
            form.getSampleNumber(), form.getShippingNumber(),
            form.getDateCollectedFrom(), form.getDateCollectedTo(),
            form.getCenters());

    ModelAndView modelAndView = new ModelAndView("collectionsTable");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findCollectionResultsTable");
    m.put("collectedSample", utilController.getFormFieldsForForm("collectedSample"));
    m.put("allCollectedSamples", getCollectionViewModels(collections));
    m.put("requestUrl", getUrl(request));
    addEditSelectorOptions(m);
    addCollectionSitesToModel(m);

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("centers", locationRepository.getAllCenters());
    m.put("donorTypes", donorTypeRepository.getAllDonorTypes());
    m.put("bloodBagTypes", bloodBagTypeRepository.getAllBloodBagTypes());
    m.put("sites", locationRepository.getAllCollectionSites());
  }

  @RequestMapping(value = "/addCollectionFormForDonorGenerator", method = RequestMethod.GET)
  public ModelAndView addCollectionFormForDonorGenerator(HttpServletRequest request,
      Model model,
      @RequestParam(value="donorId", required=true) String donorId) {

    CollectedSampleBackingForm form = new CollectedSampleBackingForm(true);
    Map<String, Object> m = model.asMap();
    addEditSelectorOptions(m);
    m.put("requestUrl", getUrl(request));
    if (donorId != null) {
      form.setDonorIdHidden(donorId);
      form.setDonor(donorRepository.findDonorById(donorId));
    }

    m.put("editCollectedSampleForm", form);
    // to ensure custom field names are displayed in the form
    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    if (form.getDonor() != null)
      m.put("contentLabel", "Add Collection for Donor " + form.getDonor().getDonorNumber());
    ModelAndView mv = new ModelAndView("editCollectedSampleForm");
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/editCollectionFormGenerator", method = RequestMethod.GET)
  public ModelAndView editCollectionFormGenerator(HttpServletRequest request,
      Model model,
      @RequestParam(value="collectionNumber", required=false) String collectionNumber) {

    CollectedSampleBackingForm form = new CollectedSampleBackingForm(true);
    Map<String, Object> m = model.asMap();
    addEditSelectorOptions(m);
    m.put("requestUrl", getUrl(request));
    if (collectionNumber != null) {
      form.setCollectionNumber(collectionNumber);
      CollectedSample collection = collectedSampleRepository
          .findCollectedSampleByNumber(collectionNumber);
      if (collection != null) {
        form = new CollectedSampleBackingForm(collection);
      } else {
        form = new CollectedSampleBackingForm(true);
      }
    }

    m.put("editCollectedSampleForm", form);
    // to ensure custom field names are displayed in the form
    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    ModelAndView mv = new ModelAndView("editCollectedSampleForm");
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/addCollectedSample", method = RequestMethod.POST)
  public ModelAndView addCollectedSample(
      @ModelAttribute("editCollectedSampleForm") @Valid CollectedSampleBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editCollectedSampleForm");
    boolean success = false;
    String message = "";
    Map<String, Object> m = model.asMap();
    addEditSelectorOptions(m);

    if (result.hasErrors()) {
      m.put("hasErrors", true);
      success = false;
      message = "Please fix the errors noted above.";
    } else {
      try {
        String donorId = form.getDonorIdHidden();
        if (donorId != null && !donorId.isEmpty()) {
          Donor donor = donorRepository.findDonorById(Long.parseLong(donorId));
          form.setDonor(donor);
        }
        CollectedSample collectedSample = form.getCollectedSample();
        collectedSample.setIsDeleted(false);
        collectedSampleRepository.addCollectedSample(collectedSample);
        m.put("hasErrors", false);
        success = true;
        message = "Collected Sample Successfully Added";
        form = new CollectedSampleBackingForm(true);
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
        message = "Collected Sample Already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
    }

    m.put("editCollectedSampleForm", form);
    m.put("existingCollectedSample", false);
    m.put("success", success);
    m.put("message", message);
    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));

    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/updateCollectedSample", method = RequestMethod.POST)
  public ModelAndView updateCollectedSample(
      @ModelAttribute("editCollectedSampleForm") @Valid CollectedSampleBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editCollectedSampleForm");
    boolean success = false;
    String message = "";
    Map<String, Object> m = model.asMap();
    if (form == null) {
      form = new CollectedSampleBackingForm(true);
    }
    else {
      if (result.hasErrors()) {
        m.put("hasErrors", true);
        success = false;
        message = "Please fix the errors noted above";
      }
      else {
        try {
          String donorId = form.getDonorIdHidden();
          if (donorId != null && !donorId.isEmpty()) {
            Donor donor = donorRepository.findDonorById(Long.parseLong(donorId));
            form.setDonor(donor);
          }
          form.setIsDeleted(false);
          CollectedSample existingCollectedSample = 
            collectedSampleRepository.updateCollectedSample(form.getCollectedSample());
          if (existingCollectedSample == null) {
            m.put("hasErrors", true);
            success = false;
            m.put("existingCollectedSample", false);
            message = "Collected Sample does not already exist.";
          }
          else {
            m.put("hasErrors", false);
            form = new CollectedSampleBackingForm(true);
            success = true;
            m.put("existingCollectedSample", true);
            message = "Collected Sample Successfully Updated";
          }
        } catch (EntityExistsException ex) {
          ex.printStackTrace();
          success = false;
          message = "Collected Sample Already exists.";
        } catch (Exception ex) {
          ex.printStackTrace();
          success = false;
          message = "Internal Error. Please try again or report a Problem.";
        }
     }
    }

    m.put("editCollectedSampleForm", form);
    m.put("success", success);
    m.put("message", message);
    m.put("collectedSample", utilController.getFormFieldsForForm("collectedSample"));

    mv.addObject("model", m);

    return mv;
  }

  private List<CollectedSampleViewModel> getCollectionViewModels(
      List<CollectedSample> collections) {
    if (collections == null)
      return Arrays.asList(new CollectedSampleViewModel[0]);
    List<CollectedSampleViewModel> collectionViewModels = new ArrayList<CollectedSampleViewModel>();
    for (CollectedSample collection : collections) {
      collectionViewModels.add(new CollectedSampleViewModel(collection,
                               locationRepository.getAllCollectionSites(),
                               locationRepository.getAllCenters()));
    }
    return collectionViewModels;
  }

  @RequestMapping(value = "/deleteCollection", method = RequestMethod.POST)
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

  private void addCollectionSitesToModel(Map<String, Object> model) {
    List<Location> allCollectionSites =
        locationRepository.getAllCollectionSites();
    model.put("collectionSites", allCollectionSites);
  }

}
