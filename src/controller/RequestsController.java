package controller;

import java.lang.reflect.InvocationTargetException;
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
import model.request.Request;

import org.apache.commons.beanutils.BeanUtils;
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

import repository.GenericConfigRepository;
import repository.LocationRepository;
import repository.ProductRepository;
import repository.ProductTypeRepository;
import repository.RequestRepository;
import repository.RequestTypeRepository;
import viewmodel.MatchingProductViewModel;
import viewmodel.ProductViewModel;
import viewmodel.RequestViewModel;
import backingform.FindRequestBackingForm;
import backingform.RequestBackingForm;
import backingform.validator.RequestBackingFormValidator;

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
  private GenericConfigRepository genericConfigRepository;

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
      Model model,
      @ModelAttribute("findRequestForm") FindRequestBackingForm form,
      BindingResult result) {

    List<Request> productRequests = Arrays.asList(new Request[0]);

    ModelAndView modelAndView = new ModelAndView("requests/requestsTable");
    Map<String, Object> m = model.asMap();
    m.put("requestFields", utilController.getFormFieldsForForm("request"));
    m.put("allRequests", getRequestViewModels(productRequests));
    m.put("refreshUrl", getUrl(request));
    m.put("nextPageUrl", getNextPageUrl(request));
    addEditSelectorOptions(m);

    modelAndView.addObject("model", m);
    return modelAndView;

  }

  @RequestMapping("/findRequestPagination")
  public @ResponseBody Map<String, Object> findRequestPagination(HttpServletRequest request,
      @ModelAttribute("findRequestForm") FindRequestBackingForm form,
      BindingResult result, Model model) {

    Map<String, Object> pagingParams = utilController.parsePagingParameters(request);
    int sortColumnId = (Integer) pagingParams.get("sortColumnId");
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("request");
    pagingParams.put("sortColumn", getSortingColumn(sortColumnId, formFields));

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

    List<Object> results = requestRepository.findRequests(
                        form.getRequestNumber(),
                        productTypeIds, siteIds,
                        requestedAfter, requiredBy,
                        includeSatisfiedRequests, pagingParams);

    @SuppressWarnings("unchecked")
    List<Request> productRequests = (List<Request>) results.get(0);
    Long totalRecords = (Long) results.get(1);

    return generateDatatablesMap(productRequests, totalRecords, formFields);
  }

  private String getNextPageUrl(HttpServletRequest request) {
    String reqUrl = request.getRequestURL().toString().replaceFirst("findRequest.html", "findRequestPagination.html");
    String queryString = request.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("productTypes", productTypeRepository.getAllProductTypes());
    m.put("requestTypes", requestTypeRepository.getAllRequestTypes());
    m.put("sites", locationRepository.getAllUsageSites());
  }

  /**
   * Get column name from column id, depends on sequence of columns in collectionsTable.jsp
   */
  private String getSortingColumn(int columnId, Map<String, Map<String, Object>> formFields) {

    List<String> visibleFields = new ArrayList<String>();
    visibleFields.add("id");
    for (String field : Arrays.asList("requestNumber", "patientBloodAbo","patientBloodRh",
                                      "requestDate", "requiredDate", "productType",
                                      "numUnitsRequested", "numUnitsIssued", "requestSite")) {
      Map<String, Object> fieldProperties = (Map<String, Object>) formFields.get(field);
      if (fieldProperties.get("hidden").equals(false))
        visibleFields.add(field);
    }

    Map<String, String> sortColumnMap = new HashMap<String, String>();
    sortColumnMap.put("id", "id");
    sortColumnMap.put("requestNumber", "requestNumber");
    sortColumnMap.put("patientBloodAbo", "patientBloodAbo");
    sortColumnMap.put("patientBloodRh", "patientBloodRh");
    sortColumnMap.put("requestDate", "requestDate");
    sortColumnMap.put("requiredDate", "requiredDate");
    sortColumnMap.put("productType", "productType.productTypeCode");
    sortColumnMap.put("numUnitsRequested", "numUnitsRequested");
    sortColumnMap.put("numUnitsIssued", "numUnitsIssued");
    sortColumnMap.put("requestSite", "requestSite");

    String sortColumn = visibleFields.get(columnId);

    if (sortColumnMap.get(sortColumn) == null)
      return "id";
    else
      return sortColumnMap.get(sortColumn);
  }

  /**
   * Datatables on the client side expects a json response for rendering data from the server
   * in jquery datatables. Remember of columns is important and should match the column headings
   * in requestsTable.jsp.
   */
  private Map<String, Object> generateDatatablesMap(List<Request> productRequests, Long totalRecords, Map<String, Map<String, Object>> formFields) {
    Map<String, Object> collectionsMap = new HashMap<String, Object>();

    ArrayList<Object> requestList = new ArrayList<Object>();

    for (RequestViewModel productRequest : getRequestViewModels(productRequests)) {

      List<Object> row = new ArrayList<Object>();
      
      row.add(productRequest.getId().toString());

      for (String property : Arrays.asList("requestNumber", "patientBloodAbo", "patientBloodRh",
                                           "requestDate", "requiredDate", "productType",
                                           "numUnitsRequested", "numUnitsIssued", "requestSite")) {
        if (formFields.containsKey(property)) {
          Map<String, Object> properties = (Map<String, Object>)formFields.get(property);
          if (properties.get("hidden").equals(false)) {
            String propertyValue = property;
            try {
              propertyValue = BeanUtils.getProperty(productRequest, property);
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              e.printStackTrace();
            } catch (NoSuchMethodException e) {
              e.printStackTrace();
            }
            row.add(propertyValue.toString());
          }
        }
      }

      requestList.add(row);
    }
    collectionsMap.put("aaData", requestList);
    collectionsMap.put("iTotalRecords", totalRecords);
    collectionsMap.put("iTotalDisplayRecords", totalRecords);
    return collectionsMap;
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
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("request");
    // to ensure custom field names are displayed in the form
    mv.addObject("requestFields", formFields);
    return mv;
  }

  @RequestMapping(value = "/editRequestFormGenerator", method = RequestMethod.GET)
  public ModelAndView editRequestFormGenerator(HttpServletRequest request,
      @RequestParam(value="requestId") Long requestId) {

    Request productRequest = requestRepository.findRequestById(requestId);
    RequestBackingForm form = new RequestBackingForm(productRequest);

    ModelAndView mv = new ModelAndView("requests/editRequestForm");
    mv.addObject("editRequestForm", form);
    mv.addObject("refreshUrl", getUrl(request));
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("request");
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
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("request");
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
    ModelAndView mv = new ModelAndView("requests/productsIssuedToRequest");
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
      BindingResult result) {

    ModelAndView mv = new ModelAndView("requests/editRequestForm");
    boolean success = false;
    String message = "";
    addEditSelectorOptions(mv.getModelMap());
    // only when the collection is correctly added the existingCollectedSample
    // property will be changed
    mv.addObject("existingRequest", true);

    if (result.hasErrors()) {
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      message = "Please fix the errors noted";
    }
    else {
      try {

        form.setIsDeleted(false);
        Request existingRequest = requestRepository.updateRequest(form.getRequest());
        if (existingRequest == null) {
          mv.addObject("hasErrors", true);
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          success = false;
          mv.addObject("existingRequest", false);
          message = "Request does not already exist.";
        }
        else {
          mv.addObject("hasErrors", false);
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

    mv.addObject("editRequestForm", form);
    mv.addObject("success", success);
    mv.addObject("errorMessage", message);
    mv.addObject("requestFields", utilController.getFormFieldsForForm("request"));

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
    Map<String, String> configProperties = genericConfigRepository.getConfigProperties("labsetup");
    mv.getModelMap().addAllAttributes(configProperties);
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
