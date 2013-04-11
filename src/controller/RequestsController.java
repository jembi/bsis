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
import repository.RequestTypeRepository;
import repository.SequenceNumberRepository;
import viewmodel.MatchingProductViewModel;
import viewmodel.ProductViewModel;
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
  private RequestTypeRepository requestTypeRepository;

  @Autowired
  private SequenceNumberRepository sequenceNumberRepository;
  
  @Autowired
  private UtilController utilController;

  public RequestsController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new RequestBackingFormValidator(binder.getValidator(), utilController));
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
  public ModelAndView requestSummaryGenerator(HttpServletRequest request,
      @RequestParam(value = "requestId", required = false) Long requestId) {

    ModelAndView mv = new ModelAndView("requests/requestSummary");

    mv.addObject("requestUrl", getUrl(request));

    Request productRequest = null;
    if (requestId != null) {
      productRequest = requestRepository.findRequestById(requestId);
      if (productRequest != null) {
        mv.addObject("existingRequest", true);
      }
      else {
        mv.addObject("existingRequest", false);
      }
    }

    RequestViewModel requestViewModel = getRequestViewModels(Arrays.asList(productRequest)).get(0);
    mv.addObject("request", requestViewModel);
    mv.addObject("refreshUrl", getUrl(request));
    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "requests.findpending.requestsummary");
    mv.addObject("tips", tips);
    // to ensure custom field names are displayed in the form
    mv.addObject("requestFields", utilController.getFormFieldsForForm("request"));
    return mv;
  }

  @RequestMapping(value = "/findRequestFormGenerator", method = RequestMethod.GET)
  public ModelAndView findRequestFormGenerator(HttpServletRequest request, Model model) {

    FindRequestBackingForm form = new FindRequestBackingForm();
    model.addAttribute("findRequestForm", form);

    ModelAndView mv = new ModelAndView("requests/findRequestForm");
    addEditSelectorOptions(mv.getModelMap());
    // to ensure custom field names are displayed in the form
    mv.addObject("requestFields", utilController.getFormFieldsForForm("request"));
    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "requests.findpending");
    mv.addObject("tips", tips);
    mv.addObject("refreshUrl", getUrl(request));
    return mv;
  }

  @RequestMapping("/findRequest")
  public ModelAndView findRequest(HttpServletRequest request,
      @ModelAttribute("findRequestForm") FindRequestBackingForm form,
      BindingResult result) {

    List<Request> productRequests = Arrays.asList(new Request[0]);

    String requestedAfter = form.getRequestedAfter();
    String requiredBy = form.getRequiredBy();

    Boolean includeSatisfiedRequests = form.getIncludeSatisfiedRequests();

    List<Integer> productTypeIds = new ArrayList<Integer>();
    productTypeIds.add(-1);
    if (form.getProductTypes() != null) {
      for (String productTypeId : form.getProductTypes()) {
        productTypeIds.add(Integer.parseInt(productTypeId));
      }
    }

    List<Long> siteIds = new ArrayList<Long>();
    // add an invalid ID so that hibernate does not throw an exception
    siteIds.add((long)-1);
    if (form.getRequestSites() != null) {
      for (String siteId : form.getRequestSites()) {
        siteIds.add(Long.parseLong(siteId));
      }
    }

    productRequests = requestRepository.findRequests(
                        productTypeIds, siteIds,
                        requestedAfter, requiredBy,
                        includeSatisfiedRequests);

    ModelAndView mv = new ModelAndView("requests/requestsTable");
    mv.addObject("requestFields", utilController.getFormFieldsForForm("request"));
    mv.addObject("allRequests", getRequestViewModels(productRequests));
    mv.addObject("refreshUrl", getUrl(request));
    addEditSelectorOptions(mv.getModelMap());

    return mv;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("productTypes", productTypeRepository.getAllProductTypes());
    m.put("requestTypes", requestTypeRepository.getAllRequestTypes());
    m.put("sites", locationRepository.getAllUsageSites());
  }

  @RequestMapping(value = "/addRequestFormGenerator", method = RequestMethod.GET)
  public ModelAndView addRequestFormGenerator(HttpServletRequest request) {

    RequestBackingForm form = new RequestBackingForm();

    ModelAndView mv = new ModelAndView("requests/addRequestForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("addRequestForm", form);
    mv.addObject("refreshUrl", getUrl(request));
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> formFields = utilController.getFormFieldsForForm("request");
    // to ensure custom field names are displayed in the form
    mv.addObject("requestFields", formFields);
    return mv;
  }

  @RequestMapping(value = "/addRequest", method = RequestMethod.POST)
  public ModelAndView addRequest(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("addRequestForm") @Valid RequestBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView();
    boolean success = false;

    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> formFields = utilController.getFormFieldsForForm("request");
    mv.addObject("requestFields", formFields);

    Request savedRequest = null;
    if (result.hasErrors()) {
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        Request productRequest = form.getRequest();
        productRequest.setIsDeleted(false);
        savedRequest = requestRepository.addRequest(productRequest);
        mv.addObject("hasErrors", false);
        success = true;
        form = new RequestBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
      }
    }

    if (success) {
      mv.addObject("requestId", savedRequest.getId());
      mv.addObject("request",  new RequestViewModel(savedRequest));
      mv.addObject("addAnotherRequestUrl", "addRequestFormGenerator.html");
      mv.setViewName("requests/addRequestSuccess");
    } else {
      mv.addObject("errorMessage", "Error creating request. Please fix the errors noted below.");
      mv.addObject("firstTimeRender", false);
      mv.addObject("addRequestForm", form);
      mv.addObject("refreshUrl", "addRequestFormGenerator.html");
      mv.setViewName("requests/addRequestError");
    }

    mv.addObject("success", success);
    return mv;
  }

  @RequestMapping(value="/listIssuedProductsForRequest", method=RequestMethod.GET)
  public ModelAndView listIssuedProductsForRequest(HttpServletRequest request,
      HttpServletResponse response, Model model,
      @RequestParam(value="requestId") Long requestId) {
    ModelAndView mv = new ModelAndView("productsIssuedToRequest");
    Map<String, Object> m = model.asMap();
    System.out.println(m);
    addEditSelectorOptions(m);
    List<Product> issuedProducts = requestRepository.getIssuedProductsForRequest(requestId);
    List<ProductViewModel> issuedProductViewModels = null;
    if (request == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } else {
      issuedProductViewModels = ProductController.getProductViewModels(issuedProducts);
    }

    m.put("issuedProducts", issuedProductViewModels);
    m.put("productFields", utilController.getFormFieldsForForm("Product"));
    m.put("productTypeFields", utilController.getFormFieldsForForm("ProductType"));
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
      @RequestParam(value="requestId", required=false) Long requestId) {

    ModelAndView mv = new ModelAndView("requests/matchingProductsForRequest");

    mv.addObject("refreshUrl", getUrl(request));
    mv.addObject("existingRequest", false);

    mv.addObject("requestId", requestId);
    List<MatchingProductViewModel> products = productRepository.findMatchingProductsForRequest(requestId);
    mv.addObject("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    mv.addObject("productFields", utilController.getFormFieldsForForm("Product"));
    mv.addObject("compatibilityTestFields", utilController.getFormFieldsForForm("CompatibilityTest"));
    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "requests.findpending.findmatchingproducts");
    mv.addObject("tips", tips);
    mv.addObject("allProducts", products);
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

  private void setRequestNumber(RequestBackingForm form,
      Map<String, Object> requestNumberProperties) {
    boolean isAutoGeneratable = (Boolean) requestNumberProperties.get("isAutoGeneratable");
    boolean autoGenerate = (Boolean) requestNumberProperties.get("autoGenerate");
    if (isAutoGeneratable && autoGenerate)
      form.setRequestNumber(sequenceNumberRepository.getNextRequestNumber());    
  }
}
