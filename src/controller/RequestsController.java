package controller;

import backingform.RequestBackingForm;
import backingform.validator.RequestBackingFormValidator;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import model.product.Product;
import model.request.Request;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repository.GenericConfigRepository;
import repository.LocationRepository;
import repository.ProductRepository;
import repository.ProductTypeRepository;
import repository.RequestRepository;
import repository.RequestTypeRepository;
import utils.PermissionConstants;
import viewmodel.MatchingProductViewModel;
import viewmodel.ProductViewModel;
import viewmodel.RequestViewModel;

@RestController
@RequestMapping("request")
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

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_REQUEST+"')")
  public  Map<String, Object> requestSummaryGenerator(HttpServletRequest request,
      @PathVariable Long id) {

    Map<String, Object> map = new HashMap<String, Object>();

    map.put("requestUrl", getUrl(request));

    Request productRequest = null;
    if (id != null) {
      productRequest = requestRepository.findRequestById(id);
      if (productRequest != null) {
        map.put("existingRequest", true);
      }
      else {
        map.put("existingRequest", false);
      }
    }

    RequestViewModel requestViewModel = getRequestViewModels(Arrays.asList(productRequest)).get(0);
    map.put("request", requestViewModel);
    map.put("refreshUrl", getUrl(request));
    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "requests.findpending.requestsummary");
    map.put("tips", tips);
    // to ensure custom field names are displayed in the form
    map.put("requestFields", utilController.getFormFieldsForForm("request"));
    return map;
  }

  @RequestMapping(value = "find/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_REQUEST+"')")
  public  Map<String, Object> findRequestFormGenerator(HttpServletRequest request) {

    Map<String, Object> map = new HashMap<String, Object>();
    addEditSelectorOptions(map);
    // to ensure custom field names are displayed in the form
    map.put("requestFields", utilController.getFormFieldsForForm("request"));
    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "requests.findpending");
    map.put("tips", tips);
    map.put("refreshUrl", getUrl(request));
    return map;
  }

 

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_REQUEST+"')")
  public  Map<String, Object> findRequestPagination(
          @RequestParam(value = "requestNumber", required = false) String requestNumber,
          @RequestParam(value = "requestedAfter", required = false) String requestedAfter,
          @RequestParam(value = "requiredBy", required = false) String requiredBy,
          @RequestParam(value = "requestSites", required = false) List<String> requestSites,
          @RequestParam(value = "productTypes", required = false) List<String> productTypes,
          @RequestParam(value = "includeSatisfiedRequests", required = false) Boolean includeSatisfiedRequests) {

      Map<String, Object> pagingParams = new HashMap<String, Object>();
      pagingParams.put("sortColumn", "id");
      pagingParams.put("start", "0");
      pagingParams.put("length", "10");
      pagingParams.put("sortDirection", "asc");
      
      int sortColumnId = (Integer) pagingParams.get("sortColumnId");
      Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("request");
      pagingParams.put("sortColumn", getSortingColumn(sortColumnId, formFields));


    List<Integer> productTypeIds = new ArrayList<Integer>();
    productTypeIds.add(-1);
    if (productTypes != null) {
      for (String productTypeId : productTypes) {
        productTypeIds.add(Integer.parseInt(productTypeId));
      }
    }

    List<Long> siteIds = new ArrayList<Long>();
    // add an invalid ID so that hibernate does not throw an exception
    siteIds.add((long)-1);
    if (requestSites != null) {
      for (String siteId : requestSites) {
        siteIds.add(Long.parseLong(siteId));
      }
    }

    List<Object> results = requestRepository.findRequests(
                        requestNumber,
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
    sortColumnMap.put("productType", "productType.productTypeNameShort");
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

  @RequestMapping(value = "/add/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_REQUEST+"')")
  public  Map<String, Object> addRequestFormGenerator(HttpServletRequest request) {

    RequestBackingForm form = new RequestBackingForm();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("requestUrl", getUrl(request));
    map.put("firstTimeRender", true);
    map.put("addRequestForm", form);
    map.put("refreshUrl", getUrl(request));
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("request");
    // to ensure custom field names are displayed in the form
    map.put("requestFields", formFields);
    return map;
  }

  @RequestMapping(value = "{id}/edit/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_REQUEST+"')")
  public  Map<String, Object> editRequestFormGenerator(HttpServletRequest request,
      @PathVariable Long id) {

    Request productRequest = requestRepository.findRequestById(id);
    RequestBackingForm form = new RequestBackingForm(productRequest);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("editRequestForm", form);
    map.put("refreshUrl", getUrl(request));
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("request");
    // to ensure custom field names are displayed in the form
    map.put("requestFields", formFields);
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_REQUEST+"')")
  public ResponseEntity<Map<String, Object>> addRequest(@Valid @RequestBody RequestBackingForm form) {

    HttpStatus httpStatus = HttpStatus.CREATED;
    Map<String, Object> map = new HashMap<String, Object>();
    boolean success = false;

        addEditSelectorOptions(map);
        Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("request");
        map.put("requestFields", formFields);

        Request savedRequest = null;

        Request productRequest = form.getRequest();
        productRequest.setIsDeleted(false);
        savedRequest = requestRepository.addRequest(productRequest);
        map.put("hasErrors", false);
        success = true;
        form = new RequestBackingForm();

        map.put("requestId", savedRequest.getId());
        map.put("request", new RequestViewModel(savedRequest));
        map.put("addAnotherRequestUrl", "addRequestFormGenerator.html");

        map.put("success", success);
        return new ResponseEntity<Map<String, Object>>(map, httpStatus);
    }

  @RequestMapping(value="/components/issued/list", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ISSUE_COMPONENT+"')")
  public  Map<String, Object> listIssuedProductsForRequest(@RequestParam(value="requestId") Long requestId) {
    Map<String, Object> map = new HashMap<String, Object>();
    addEditSelectorOptions(map);
    List<Product> issuedProducts = requestRepository.getIssuedProductsForRequest(requestId);
    List<ProductViewModel> issuedProductViewModels = null;
    issuedProductViewModels = ProductController.getProductViewModels(issuedProducts);
    map.put("issuedProducts", issuedProductViewModels);
    map.put("productFields", utilController.getFormFieldsForForm("Product"));
    map.put("productTypeFields", utilController.getFormFieldsForForm("ProductType"));
    return map;
  }
  
  @RequestMapping(method = RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_REQUEST+"')")
  public  ResponseEntity<Map<String, Object>> updateRequest(@Valid @RequestBody RequestBackingForm form) {

   
      HttpStatus httpStatus = HttpStatus.OK;
      Map<String, Object> map = new HashMap<String, Object>();
      boolean success = false;
      String message = "";
      addEditSelectorOptions(map);
    // only when the collection is correctly added the existingCollectedSample
      // property will be changed
      map.put("existingRequest", true);

      form.setIsDeleted(false);
      Request existingRequest = requestRepository.updateRequest(form.getRequest());
      if (existingRequest == null) {
          map.put("hasErrors", true);
          httpStatus = HttpStatus.BAD_REQUEST;
          success = false;
          map.put("existingRequest", false);
          message = "Request does not already exist.";
      } else {
          map.put("hasErrors", false);
          success = true;
          message = "Request Successfully Updated";
      }


    map.put("editRequestForm", form);
    map.put("success", success);
    map.put("errorMessage", message);
    map.put("requestFields", utilController.getFormFieldsForForm("request"));

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
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

  @RequestMapping(method = RequestMethod.DELETE)
  public 
  HttpStatus deleteProduct(
      @RequestParam("requestId") Long requestId) {

      requestRepository.deleteRequest(requestId);

      return HttpStatus.OK;
  }


  @RequestMapping(value = "{id}/components/matching/find", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.BLOOD_CROSS_MATCH_CHECK+"')")
  public  Map<String, Object> findMatchingProductsForRequest(HttpServletRequest request,
      @PathVariable Long id) {

    Map<String, Object> map = new HashMap<String, Object>();

    map.put("refreshUrl", getUrl(request));
    map.put("existingRequest", false);

    map.put("requestId", id);
    List<MatchingProductViewModel> products = productRepository.findMatchingProductsForRequest(id);
    map.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    map.put("productFields", utilController.getFormFieldsForForm("Product"));
    map.put("compatibilityTestFields", utilController.getFormFieldsForForm("CompatibilityTest"));
    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "requests.findpending.findmatchingproducts");
    map.put("tips", tips);
    map.put("allProducts", products);
    map.put("labProperties", genericConfigRepository.getConfigProperties("labsetup"));
    return map;
  }

  @RequestMapping(value = "/components/issue", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ISSUE_COMPONENT+"')")
  public  HttpStatus issueSelectedProducts(
      HttpServletResponse response,
      @RequestParam("requestId") Long requestId,
      @RequestParam("productsToIssue") String productsToIssue) {
   
      requestRepository.issueProductsToRequest(requestId, productsToIssue);
    
      return HttpStatus.OK;
  }
  
  /**
  * 
  *
  @RequestMapping("/findRequest")
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_REQUEST+"')")
  public  Map<String, Object> findRequest(HttpServletRequest request,
      ,
      @ModelAttribute("findRequestForm") FindRequestBackingForm form,
      BindingResult result) {

    List<Request> productRequests = Arrays.asList(new Request[0]);

    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> m = model.asMap();
    m.put("requestFields", utilController.getFormFieldsForForm("request"));
    m.put("allRequests", getRequestViewModels(productRequests));
    m.put("refreshUrl", getUrl(request));
    m.put("nextPageUrl", getNextPageUrl(request));
    addEditSelectorOptions(m);

    map.put("model", m);
    return map;

  }
  * */
  
}
