package controller;

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

import model.collectedsample.CollectedSample;
import model.collectedsample.CollectedSampleBackingForm;
import model.collectedsample.FindCollectedSampleBackingForm;
import model.donor.Donor;
import model.location.Location;

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

//  @InitBinder
//  protected void initBinder(WebDataBinder binder) {
//    binder.setValidator(new CollectedSampleBackingFormValidator(binder.getValidator()));
//  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "/findCollectionFormGenerator", method = RequestMethod.GET)
  public ModelAndView findCollectionFormGenerator(HttpServletRequest request, Model model) {

    FindCollectedSampleBackingForm form = new FindCollectedSampleBackingForm();
    model.addAttribute("findCollectedSampleForm", form);

    ModelAndView mv = new ModelAndView("findCollectionForm");
    Map<String, Object> m = model.asMap();
    addEditSelectorOptions(m);
    // to ensure custom field names are displayed in the form
    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    m.put("refreshUrl", getUrl(request));
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping("/findCollection")
  public ModelAndView findCollection(HttpServletRequest request,
      @ModelAttribute("findCollectedSampleForm") FindCollectedSampleBackingForm form,
      BindingResult result, Model model) {

    List<CollectedSample> collectedSamples = Arrays.asList(new CollectedSample[0]);

    String searchBy = form.getSearchBy();
    String dateCollectedFrom = form.getDateCollectedFrom();
    String dateCollectedTo = form.getDateCollectedTo();
    if (searchBy.equals("collectionNumber")) {
      collectedSamples = collectedSampleRepository.findCollectedSampleByCollectionNumber(
                                          form.getCollectionNumber(),
                                          dateCollectedFrom, dateCollectedTo);
    } else if (searchBy.equals("shippingNumber")) {
      collectedSamples = collectedSampleRepository.findCollectedSampleByShippingNumber(
          form.getShippingNumber(),
          dateCollectedFrom, dateCollectedTo);
    } else if (searchBy.equals("sampleNumber")) {
      collectedSamples = collectedSampleRepository.findCollectedSampleBySampleNumber(
          form.getSampleNumber(),
          dateCollectedFrom, dateCollectedTo);
    } else if (searchBy.equals("collectionCenter")) {

      List<Long> centerIds = new ArrayList<Long>();
      for (String center : form.getCenters()) {
        centerIds.add(Long.parseLong(center));
      }

      collectedSamples = collectedSampleRepository.findCollectedSampleByCenters(
          centerIds,
          dateCollectedFrom, dateCollectedTo);
    }
    
    ModelAndView modelAndView = new ModelAndView("collectionsTable");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findCollectionResultsTable");
    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    m.put("allCollectedSamples", getCollectionViewModels(collectedSamples));
    m.put("refreshUrl", getUrl(request));
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
    m.put("refreshUrl", getUrl(request));
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
      @RequestParam(value="collectionId", required=false) Long collectionId) {

    CollectedSampleBackingForm form = new CollectedSampleBackingForm(true);

    ModelAndView mv = new ModelAndView("editCollectedSampleForm");
    Map<String, Object> m = model.asMap();
    m.put("refreshUrl", getUrl(request));
    m.put("existingCollectedSample", false);
    if (collectionId != null) {
      form.setId(collectionId);
      CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(collectionId);
      if (collectedSample != null) {
        form = new CollectedSampleBackingForm(collectedSample);
        m.put("existingCollectedSample", true);
      }
      else {
        form = new CollectedSampleBackingForm(true);
      }
    }
    addEditSelectorOptions(m);
    m.put("editCollectedSampleForm", form);
    m.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    mv.addObject("model", m);
    System.out.println(mv.getViewName());
    return mv;
  }

  @RequestMapping(value = "/addCollectedSample", method = RequestMethod.POST)
  public ModelAndView addCollectedSample(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("editCollectedSampleForm") @Valid CollectedSampleBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editCollectedSampleForm");
    boolean success = false;
    String message = "";
    Map<String, Object> m = model.asMap();

    // IMPORTANT: Validation code just checks if the ID exists.
    // We still need to store the donor as part of the collected sample.
    String donorId = form.getDonorIdHidden();
    if (donorId != null && !donorId.isEmpty()) {
      try {
        Donor donor = donorRepository.findDonorById(Long.parseLong(donorId));
        form.setDonor(donor);
      } catch (NoResultException ex) {
        ex.printStackTrace();
      }
    }

    if (result.hasErrors()) {
      m.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);      
      success = false;
      message = "Please fix the errors noted above.";
    } else {
      try {


        CollectedSample collectedSample = form.getCollectedSample();
        collectedSample.setIsDeleted(false);
        collectedSampleRepository.addCollectedSample(collectedSample);
        m.put("hasErrors", false);
        success = true;
        message = "Collection Successfully Added";
        form = new CollectedSampleBackingForm(true);
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
        message = "Collection Already exists.";
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
    m.put("refreshUrl", getUrl(request));
    m.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    addEditSelectorOptions(m);

    mv.addObject("model", m);
    return mv;
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

    System.out.println("here");
    System.out.println(form.getCollectionCenter());
    System.out.println(form.getCollectionSite());

    // IMPORTANT: Validation code just checks if the ID exists.
    // We still need to store the donor as part of the collected sample.
    String donorId = form.getDonorIdHidden();
    if (donorId != null && !donorId.isEmpty()) {
      try {
        Donor donor = donorRepository.findDonorById(Long.parseLong(donorId));
        form.setDonor(donor);
      } catch (NoResultException ex) {
        ex.printStackTrace();
      }
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

  private void addCollectionSitesToModel(Map<String, Object> model) {
    List<Location> allCollectionSites =
        locationRepository.getAllCollectionSites();
    model.put("collectionSites", allCollectionSites);
  }
}
