package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;

import model.Location;
import model.Request;
import model.RequestBackingForm;

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
import repository.LocationRepository;
import repository.RecordFieldsConfigRepository;
import repository.RequestRepository;
import utils.ControllerUtil;
import viewmodel.RequestViewModel;

@Controller
public class RequestsController {

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private DisplayNamesRepository displayNamesRepository;
  @Autowired
  private RecordFieldsConfigRepository recordFieldsConfigRepository;

  @RequestMapping("/requestsLandingPage")
  public ModelAndView getRequestsPage(HttpServletRequest request) {

    return new ModelAndView("requestsLandingPage");
  }

  @RequestMapping(value = "/findRequestFormGenerator", method = RequestMethod.GET)
  public ModelAndView findRequestFormInit(Model model) {

    RequestBackingForm form = new RequestBackingForm();
    model.addAttribute("findRequestForm", form);

    ModelAndView mv = new ModelAndView("findRequestForm");
    Map<String, Object> m = model.asMap();

    List<String> sites = locationRepository.getAllCollectionSitesAsString();
    m.put("sites", sites);
    // to ensure custom field names are displayed in the form
    ControllerUtil.addRequestDisplayNamesToModel(m, displayNamesRepository);
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping("/findRequest")
  public ModelAndView findRequest(
      @ModelAttribute("findRequestForm") RequestBackingForm form,
      BindingResult result, Model model) {

    List<Request> requests = requestRepository.findAnyRequestMatching(
        form.getRequestNumber(), form.getDateRequestedFrom(),
        form.getDateRequestedTo(), form.getDateRequiredFrom(),
        form.getDateRequiredTo(), form.getSites(), form.getProductTypes());

    ModelAndView modelAndView = new ModelAndView("requestsTable");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findRequestsTable");

    List<String> sites = locationRepository.getAllCollectionSitesAsString();
    m.put("sites", sites);

    ControllerUtil.addRequestDisplayNamesToModel(m, displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("request", m,
        recordFieldsConfigRepository);
    m.put("allRequests", getRequestViewModels(requests));

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  @RequestMapping(value = "/editRequestFormGenerator", method = RequestMethod.GET)
  public ModelAndView editRequestFormGenerator(
      Model model,
      @RequestParam(value = "requestNumber", required = false) String requestNumber,
      @RequestParam(value = "isDialog", required = false) String isDialog) {

    RequestBackingForm form = new RequestBackingForm();
    Map<String, Object> m = model.asMap();
    m.put("isDialog", isDialog);

    List<String> sites = locationRepository.getAllCollectionSitesAsString();
    m.put("sites", sites);

    if (requestNumber != null) {
      form.setRequestNumber(requestNumber);
      Request request = requestRepository
          .findRequestByRequestNumber(requestNumber);
      Location l = locationRepository.getLocation(request.getSiteId());
      if (l != null)
        m.put("selectedSite", l.getName());
      if (request != null)
        form = new RequestBackingForm(request);
    }

    m.put("editRequestForm", form);
    // to ensure custom field names are displayed in the form
    ControllerUtil.addRequestDisplayNamesToModel(m, displayNamesRepository);
    ModelAndView mv = new ModelAndView("editRequestForm");
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/updateRequest", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> updateOrAddRequest(
      @ModelAttribute("editRequestForm") RequestBackingForm form) {

    boolean success = true;
    String errMsg = "";
    try {
      Request request = form.getRequest();
      String site = form.getSites().get(0);
      Long siteId = locationRepository.getIDByName(site);
      request.setComment("");
      request.setSiteId(siteId);
      requestRepository.updateOrAddRequest(request);
    } catch (EntityExistsException ex) {
      // TODO: Replace with logger
      System.err.println("Entity Already exists");
      System.err.println(ex.getMessage());
      success = false;
      errMsg = "Request Already Exists";
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

  private List<RequestViewModel> getRequestViewModels(
      List<Request> requests) {
    if (requests == null)
      return Arrays.asList(new RequestViewModel[0]);
    List<RequestViewModel> requestViewModels = new ArrayList<RequestViewModel>();
    for (Request request : requests) {
      requestViewModels.add(new RequestViewModel(request));
    }
    return requestViewModels;
  }

}
