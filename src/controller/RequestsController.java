package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;

import model.Issue;
import model.Product;
import model.Request;
import model.RequestBackingForm;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import repository.DisplayNamesRepository;
import repository.IssueRepository;
import repository.LocationRepository;
import repository.ProductRepository;
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
  private ProductRepository productRepository;

  @Autowired
  private DisplayNamesRepository displayNamesRepository;

  @Autowired
  private RecordFieldsConfigRepository recordFieldsConfigRepository;

  @Autowired
  private IssueRepository issueRepository;
  
  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "/findRequestFormGenerator", method = RequestMethod.GET)
  public ModelAndView findRequestFormInit(HttpServletRequest servletRequest, Model model) {

    RequestBackingForm form = new RequestBackingForm();
    model.addAttribute("findRequestForm", form);

    ModelAndView mv = new ModelAndView("findRequestForm");
    Map<String, Object> m = model.asMap();

//    List<String> sites = locationRepository.getAllCollectionSitesAsString();
//    m.put("sites", sites);
    m.put("requestUrl", getUrl(servletRequest));
    // to ensure custom field names are displayed in the form
    ControllerUtil.addRequestDisplayNamesToModel(m, displayNamesRepository);
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping("/findRequest")
  public ModelAndView findRequest(HttpServletRequest servletRequest,
      @ModelAttribute("findRequestForm") RequestBackingForm form,
      BindingResult result, Model model) {

    List<Request> requests = requestRepository.findAnyRequestMatching(
        form.getRequestNumber(), form.getDateRequestedFrom(),
        form.getDateRequestedTo(), form.getDateRequiredFrom(),
        form.getDateRequiredTo(), form.getSites(), form.getProductTypes(), form.getStatuses());

    ModelAndView modelAndView = new ModelAndView("requestsTable");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findRequestsTable");

//    List<String> sites = locationRepository.getAllCollectionSitesAsString();
//    m.put("sites", sites);
    m.put("requestUrl", getUrl(servletRequest));

    ControllerUtil.addRequestDisplayNamesToModel(m, displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("request", m,
        recordFieldsConfigRepository);
    m.put("allRequests", getRequestViewModels(requests));

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  @RequestMapping("/pendingRequests")
  public ModelAndView findPendingRequests(HttpServletRequest servletRequest, Model model) {

    List<Request> requests = requestRepository.findRequestsNotFulfilled();

    ModelAndView modelAndView = new ModelAndView("requestsTable");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findPendingRequestsTable");

//    List<String> sites = locationRepository.getAllCollectionSitesAsString();
//    m.put("sites", sites);
    m.put("requestUrl", getUrl(servletRequest));

    ControllerUtil.addRequestDisplayNamesToModel(m, displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("request", m,
        recordFieldsConfigRepository);
    m.put("allRequests", getRequestViewModels(requests));

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  @RequestMapping("/findMatchingProductsForRequest")
  public ModelAndView findMatchingProductsForRequest(HttpServletRequest servletRequest,
      @RequestParam(value = "requestNumber", required = true) String requestNumber,
      Model model) {

    Request request = requestRepository.findRequestByRequestNumber(requestNumber);

    List<Product> products = new ArrayList<Product>();
    if (request != null) {
//      products = productRepository.findAnyProductMatching("", "",
//          Arrays.asList(request.getAbo()), Arrays.asList(request.getRhd()),
//          Arrays.asList(request.getProductType()), Arrays.asList("available"));
    }

    ModelAndView modelAndView = new ModelAndView("issueProducts");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findAvailableProductsTable");
    m.put("showAddProductButton", false);
    ControllerUtil.addProductDisplayNamesToModel(m, displayNamesRepository);
    ControllerUtil.addRequestDisplayNamesToModel(m, displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("product", m,
        recordFieldsConfigRepository);
    m.put("allProducts", ProductsController.getProductViewModels(products));
    m.put("requestUrl", getUrl(servletRequest));
    m.put("request", new RequestViewModel(request, locationRepository.getAllCollectionSites()));
    m.put("productsTableRowEditable", "false");
    m.put("productsTableRowSelectableProperty", "multi");

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  @RequestMapping(value = "/issueProductsForRequest", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> issueProductsForRequest(
      @RequestParam(value="products", required=false) String productsJson,
      @RequestParam(value="requestNumber", required=true) String requestNumber 
      ) {

    boolean success = true;
    String errMsg = "";
    try {
      System.out.println("products: " + productsJson);
      System.out.println("requestNumber: " + requestNumber);
      ObjectMapper mapper = new ObjectMapper();
      Map<String, String> products = mapper.readValue(productsJson, Map.class);
      System.out.println(products);
      Request request = requestRepository.issueRequest(requestNumber, "fulfilled");
      for (String productNumber : products.values()) {
        System.out.println("Issuing Product Number: " + productNumber);
        Issue issue = new Issue();
        issue.setDateIssued(new Date());
        issue.setProductNumber(productNumber);
        issue.setSiteId(request.getSiteId());
        issue.setComments("issued product");
        issue.setDeleted(Boolean.FALSE);
        issueRepository.saveIssue(issue);
        productRepository.issueProduct(productNumber);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      success = false;
      errMsg = "Internal Server Error";
    }

    Map<String, Object> m = new HashMap<String, Object>();
    m.put("success", success);
    m.put("errMsg", errMsg);
    return m;
  }

  @RequestMapping(value = "/editRequestFormGenerator", method = RequestMethod.GET)
  public ModelAndView editRequestFormGenerator(
      Model model,
      @RequestParam(value = "requestNumber", required = false) String requestNumber,
      @RequestParam(value = "isDialog", required = false) String isDialog) {

    RequestBackingForm form = new RequestBackingForm();
    Map<String, Object> m = model.asMap();
    m.put("isDialog", isDialog);

//    List<String> sites = locationRepository.getAllCollectionSitesAsString();
//    m.put("sites", sites);
    m.put("selectedSite", "");
    m.put("selectedProductType", "");

    if (requestNumber != null) {
      form.setRequestNumber(requestNumber);
      Request request = requestRepository
          .findRequestByRequestNumber(requestNumber);
      if (request != null) {
        Location l = locationRepository.getLocation(request.getSiteId());
        m.put("selectedProductType", request.getProductType());
        form = new RequestBackingForm(request);
        if (l != null)
          m.put("selectedSite", l.getName());
        m.put("requestNumber", request.getRequestNumber());
      }
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

  private List<RequestViewModel> getRequestViewModels(List<Request> requests) {
    if (requests == null)
      return Arrays.asList(new RequestViewModel[0]);
    List<RequestViewModel> requestViewModels = new ArrayList<RequestViewModel>();
    for (Request request : requests) {
      requestViewModels.add(new RequestViewModel(request));
    }
    return requestViewModels;
  }

  @RequestMapping(value = "/deleteRequest", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> deleteRequest(
      @RequestParam("requestNumber") String requestNumber) {

    boolean success = true;
    String errMsg = "";
    try {
      requestRepository.deleteRequest(requestNumber);
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
}
