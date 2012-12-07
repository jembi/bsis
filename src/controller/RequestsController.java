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

import model.product.Product;
import model.request.FindRequestBackingForm;
import model.request.Request;
import model.request.RequestBackingForm;
import model.request.RequestBackingFormValidator;

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

import repository.LocationRepository;
import repository.ProductRepository;
import repository.ProductTypeRepository;
import repository.RequestRepository;
import viewmodel.RequestViewModel;

@Controller
public class RequestsController {

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private ProductTypeRepository productTypeRepository;

  @Autowired
  private UtilController utilController;

  public RequestsController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new RequestBackingFormValidator(binder.getValidator()));
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "/requestSummary", method = RequestMethod.GET)
  public ModelAndView productSummaryGenerator(HttpServletRequest request, Model model,
      @RequestParam(value = "requestId", required = false) Long requestId) {

    ModelAndView mv = new ModelAndView("requestSummary");
    Map<String, Object> m = model.asMap();

    m.put("requestUrl", getUrl(request));

    Request productRequest = null;
    if (requestId != null) {
      productRequest = requestRepository.findRequestById(requestId);
      if (productRequest != null) {
        m.put("existingRequest", true);
      }
      else {
        m.put("existingRequest", false);
      }
    }

    RequestViewModel requestViewModel = getRequestViewModels(Arrays.asList(productRequest)).get(0);
    m.put("request", requestViewModel);
    m.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    m.put("requestFields", utilController.getFormFieldsForForm("request"));
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/findRequestFormGenerator", method = RequestMethod.GET)
  public ModelAndView findRequestFormGenerator(HttpServletRequest request, Model model) {

    FindRequestBackingForm form = new FindRequestBackingForm();
    model.addAttribute("findRequestForm", form);

    ModelAndView mv = new ModelAndView("findRequestForm");
    Map<String, Object> m = model.asMap();
    addEditSelectorOptions(m);
    // to ensure custom field names are displayed in the form
    m.put("requestFields", utilController.getFormFieldsForForm("request"));
    m.put("refreshUrl", getUrl(request));
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping("/findRequest")
  public ModelAndView findRequest(HttpServletRequest request,
      @ModelAttribute("findRequestForm") FindRequestBackingForm form,
      BindingResult result, Model model) {

    List<Request> productRequests = Arrays.asList(new Request[0]);

    List<String> productTypes = form.getProductTypes();
    String requestedAfter = form.getRequestedAfter();
    String requiredBy = form.getRequiredBy();

    List<Long> siteIds = new ArrayList<Long>();
    // add an invalid ID so that hibernate does not throw an exception
    siteIds.add((long)-1);
    if (form.getRequestSites() != null) {
      for (String siteId : form.getRequestSites()) {
        siteIds.add(Long.parseLong(siteId));
      }
    }

    productRequests = requestRepository.findRequests(productTypes, siteIds, requestedAfter, requiredBy);

    ModelAndView modelAndView = new ModelAndView("requestsTable");
    Map<String, Object> m = model.asMap();
    m.put("requestFields", utilController.getFormFieldsForForm("request"));
    m.put("allRequests", getRequestViewModels(productRequests));
    m.put("refreshUrl", getUrl(request));
    addEditSelectorOptions(m);

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("productTypes", productTypeRepository.getAllProductTypes());
    m.put("sites", locationRepository.getAllUsageSites());
  }

  @RequestMapping(value = "/editRequestFormGenerator", method = RequestMethod.GET)
  public ModelAndView editRequestFormGenerator(HttpServletRequest request,
      Model model,
      @RequestParam(value="requestId", required=false) Long requestId) {

    RequestBackingForm form = new RequestBackingForm(true);

    ModelAndView mv = new ModelAndView("editRequestForm");
    Map<String, Object> m = model.asMap();
    m.put("refreshUrl", getUrl(request));
    m.put("existingRequest", false);
    if (requestId != null) {
      form.setId(requestId);
      Request productRequest = requestRepository.findRequestById(requestId);
      if (productRequest != null) {
        form = new RequestBackingForm(productRequest);
        m.put("existingRequest", true);
      }
      else {
        form = new RequestBackingForm(true);
      }
    }
    addEditSelectorOptions(m);
    m.put("editRequestForm", form);
    m.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    m.put("requestFields", utilController.getFormFieldsForForm("Request"));
    mv.addObject("model", m);
    System.out.println(mv.getViewName());
    return mv;
  }

  @RequestMapping(value = "/addRequest", method = RequestMethod.POST)
  public ModelAndView addRequest(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("editRequestForm") @Valid RequestBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editRequestForm");
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
        Request productRequest = form.getRequest();
        productRequest.setIsDeleted(false);
        productRequest.setFulfilled(false);
        requestRepository.addRequest(productRequest);
        m.put("hasErrors", false);
        success = true;
        message = "Request Successfully Added";
        form = new RequestBackingForm(true);
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
        message = "Request Already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
    }

    m.put("editRequestForm", form);
    m.put("existingRequest", false);
    m.put("success", success);
    m.put("message", message);
    m.put("refreshUrl", "editRequestFormGenerator.html");
    m.put("requestFields", utilController.getFormFieldsForForm("request"));
    addEditSelectorOptions(m);

    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/updateRequest", method = RequestMethod.POST)
  public ModelAndView updateRequest(
      HttpServletResponse response,
      @ModelAttribute("editRequestForm") @Valid RequestBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editRequestForm");
    boolean success = false;
    String message = "";
    Map<String, Object> m = model.asMap();
    addEditSelectorOptions(m);
    // only when the collection is correctly added the existingCollectedSample
    // property will be changed
    m.put("existingRequest", true);

    System.out.println("here");

    if (result.hasErrors()) {
      m.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      message = "Please fix the errors noted above now!";
    }
    else {
      try {

        form.setIsDeleted(false);
        Request existingRequest = requestRepository.updateRequest(form.getRequest());
        if (existingRequest == null) {
          m.put("hasErrors", true);
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          success = false;
          m.put("existingRequest", false);
          message = "Request does not already exist.";
        }
        else {
          m.put("hasErrors", false);
          success = true;
          message = "Request Successfully Updated";
        }
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        success = false;
        message = "Request Already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
   }

    m.put("editRequestForm", form);
    m.put("success", success);
    m.put("message", message);
    m.put("requestFields", utilController.getFormFieldsForForm("request"));

    mv.addObject("model", m);

    return mv;
  }

  private List<RequestViewModel> getRequestViewModels(
      List<Request> productRequests) {
    if (productRequests == null)
      return Arrays.asList(new RequestViewModel[0]);
    List<RequestViewModel> requestViewModels = new ArrayList<RequestViewModel>();
    for (Request productRequest : productRequests) {
      requestViewModels.add(new RequestViewModel(productRequest));
    }
    return requestViewModels;
  }

  @RequestMapping(value = "/deleteRequest", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> deleteProduct(
      @RequestParam("requestId") Long requestId) {

    boolean success = true;
    String errMsg = "";
    try {
      requestRepository.deleteRequest(requestId);
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


  @RequestMapping("/findMatchingProductsForRequest")
  public ModelAndView findMatchingProductsForRequest(HttpServletRequest request,
      Model model,
      @RequestParam(value="requestId", required=false) Long requestId) {

    ModelAndView mv = new ModelAndView("matchingProductsForRequest");

    Map<String, Object> m = model.asMap();
    m.put("refreshUrl", getUrl(request));
    m.put("existingRequest", false);

    Request productRequest = requestRepository.findRequestById(requestId);
    if (productRequest == null) {
      m.put("invalidRequest", true);
      return mv;
    }

    System.out.println("Request found");
    m.put("request", productRequest);
    List<Product> products = productRepository.findMatchingProductsForRequest(productRequest);
    System.out.println(products);
    
    m.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    m.put("productFields", utilController.getFormFieldsForForm("Product"));
    m.put("allProducts", ProductController.getProductViewModels(products));
    mv.addObject("model", m);
    System.out.println(mv.getViewName());
    return mv;
  }

  @RequestMapping("/issueSelectedProducts")
  public @ResponseBody Map<String, Object> issueSelectedProducts(
      HttpServletResponse response,
      @RequestParam("requestId") Long requestId,
      @RequestParam("productsToIssue") String productsToIssue) {
    boolean success = true;
    String errMsg = "";
    try {
      requestRepository.issueProductsToRequest(requestId, productsToIssue);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
