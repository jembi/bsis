package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import model.donor.Donor;
import model.donor.DonorBackingForm;
import model.donor.DonorBackingFormValidator;
import model.util.BloodGroup;

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

import repository.DonorRepository;
import viewmodel.donor.DonorViewModel;

@Controller
public class DonorController {

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private UtilController utilController;
  
  public DonorController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new DonorBackingFormValidator(binder.getValidator()));
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping("/donors")
  public ModelAndView getDonorsPage(HttpServletRequest request) {

    ModelAndView modelAndView = new ModelAndView("donors");
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("requestUrl", getUrl(request));
    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping(value = "/editDonorFormGenerator", method = RequestMethod.GET)
  public ModelAndView editDonorFormGenerator(HttpServletRequest request, Model model,
      @RequestParam(value = "donorId", required = false) Long donorId) {

    DonorBackingForm form = new DonorBackingForm(true);

    ModelAndView mv = new ModelAndView("editDonorForm");
    Map<String, Object> m = model.asMap();
    m.put("requestUrl", getUrl(request));
    if (donorId != null) {
      form.setId(donorId);
      Donor donor = donorRepository.findDonorById(donorId);
      if (donor != null) {
        form = new DonorBackingForm(donor);
        m.put("existingDonor", true);
      }
      else {
        form = new DonorBackingForm(true);
        m.put("existingDonor", false);
      }
    }
    m.put("editDonorForm", form);
    m.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/addDonor", method = RequestMethod.POST)
  public ModelAndView
        addDonor(HttpServletRequest request,
                 HttpServletResponse response,
                 @ModelAttribute("editDonorForm") @Valid DonorBackingForm form,
                 BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editDonorForm");
    boolean success = false;
    String message = "";
    Map<String, Object> m = model.asMap();

    if (result.hasErrors()) {
      m.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);      
      success = false;
      message = "Please fix the errors noted above.";
    } else {
      try {
        Donor donor = form.getDonor();
        donor.setIsDeleted(false);
        donorRepository.addDonor(donor);
        m.put("hasErrors", false);
        success = true;
        message = "Donor Successfully Added";
        form = new DonorBackingForm(true);
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
        message = "Donor Already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
    }

    m.put("editDonorForm", form);
    m.put("existingDonor", false);
    m.put("refreshUrl", "editDonorFormGenerator.html");
    m.put("success", success);
    m.put("message", message);
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));

    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/updateDonor", method = RequestMethod.POST)
  public ModelAndView updateDonor(
      HttpServletResponse response,
      @ModelAttribute(value="editDonorForm") @Valid DonorBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editDonorForm");
    boolean success = false;
    String message = "";
    Map<String, Object> m = model.asMap();
    // only when the collection is correctly added the existingCollectedSample
    // property will be changed
    m.put("existingDonor", true);

    if (result.hasErrors()) {
      m.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      message = "Please fix the errors noted above";
    }
    else {
      try {
        form.setIsDeleted(false);
        Donor existingDonor = donorRepository.updateDonor(form.getDonor());
        if (existingDonor == null) {
          m.put("hasErrors", true);
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          success = false;
          m.put("existingDonor", false);
          message = "Donor does not already exist.";
        }
        else {
          m.put("hasErrors", false);
          success = true;
          message = "Donor Successfully Updated";
        }
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        success = false;
        message = "Donor Already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
   }

    m.put("editDonorForm", form);
    m.put("success", success);
    m.put("message", message);
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));

    mv.addObject("model", m);

    return mv;
  }

  @RequestMapping(value = "/donorTypeAhead", method = RequestMethod.GET)
  public @ResponseBody
  List<DonorViewModel> donorTypeAhead(
      @RequestParam("term") String term) {
    List<Donor> donors = donorRepository.findAnyDonorStartsWith(term);
    return getDonorsViewModels(donors);
  }
  
  @RequestMapping(value = "/deleteDonor", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> deleteDonor(
      @RequestParam("donorId") Long donorId) {

    boolean success = true;
    String errMsg = "";
    try {
      donorRepository.deleteDonor(donorId);
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
  
  @RequestMapping(value = "/findDonorFormGenerator", method = RequestMethod.GET)
  public ModelAndView findDonorFormGenerator(HttpServletRequest request, Model model) {

    DonorBackingForm form = new DonorBackingForm();
    model.addAttribute("findDonorForm", form);

    ModelAndView mv = new ModelAndView("findDonorForm");
    Map<String, Object> m = model.asMap();
    // to ensure custom field names are displayed in the form
    m.put("donorFields", utilController.getFormFieldsForForm("donor"));
    m.put("contentLabel", "Find Donors");
    m.put("refreshUrl", "findDonorFormGenerator.html");
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/findDonor", method = RequestMethod.GET)
  public ModelAndView findDonor(HttpServletRequest request,
      @ModelAttribute("findDonorForm") DonorBackingForm form,
      BindingResult result, Model m) {

    String donorNumber = form.getDonorNumber();
    String firstName = form.getFirstName();
    String lastName = form.getLastName();
    List<BloodGroup> bloodGroups = form.getBloodGroups();

    ModelAndView modelAndView = new ModelAndView("donorsTable");
    List<Donor> donors = donorRepository.findAnyDonor(donorNumber, firstName,
        lastName, bloodGroups);

    Map<String, Object> model = m.asMap();
    model.put("tableName", "findDonorResultsTable");
    model.put("requestUrl", getUrl(request));
    model.put("donorFields", utilController.getFormFieldsForForm("donor"));
    model.put("allDonors", getDonorsViewModels(donors));
    model.put("contentLabel", "Find Donors");
    model.put("refreshUrl", getUrl(request));
    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping("/viewDonors")
  public ModelAndView viewDonors(@RequestParam Map<String, String> params,
      HttpServletRequest request) {

    List<Donor> allDonors = donorRepository.getAllDonors();
    ModelAndView modelAndView = new ModelAndView("donorsTable");
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("requestUrl", getUrl(request));

    model.put("tableName", "viewAllDonors");
    model.put("allDonors", getDonorsViewModels(allDonors));
    model.put("donorFields", utilController.getFormFieldsForForm("donor"));
    model.put("contentLabel", "View All Donors");
    model.put("refreshUrl", getUrl(request));
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
}
