package controller;

import backingform.RequestBackingForm;
import backingform.validator.RequestBackingFormValidator;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
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
@RequestMapping("requests")
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

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_REQUEST+"')")
  public  Map<String, Object> requestSummaryGenerator(HttpServletRequest request,
      @PathVariable Long id) {

    Map<String, Object> map = new HashMap<String, Object>();

    Request productRequest = requestRepository.findRequestById(id);

    RequestViewModel requestViewModel = getRequestViewModels(Arrays.asList(productRequest)).get(0);
    map.put("request", requestViewModel);
    return map;
  }

  /**
   * 
   *issue - #209 
   * Reason - duplicate method refer /form
   *
  @RequestMapping(value = "find/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_REQUEST+"')")
  public  Map<String, Object> findRequestFormGenerator(HttpServletRequest request) {

    Map<String, Object> map = new HashMap<String, Object>();
    addEditSelectorOptions(map);
    // to ensure custom field names are displayed in the form
    map.put("requestFields", utilController.getFormFieldsForForm("request"));
    return map;
  }
*/
 

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_REQUEST+"')")
  public  Map<String, Object> findRequestPagination(
          @RequestParam(value = "requestNumber", required = false) String requestNumber,
          @RequestParam(value = "requestedAfter", required = false) String requestedAfter,
          @RequestParam(value = "requiredBy", required = false) String requiredBy,
          @RequestParam(value = "requestSites", required = false) List<String> requestSites,
          @RequestParam(value = "productTypes", required = false) List<String> productTypes,
          @RequestParam(value = "includeSatisfiedRequests", required = false) Boolean includeSatisfiedRequests) throws ParseException {

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

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_REQUEST+"')")
  public  Map<String, Object> addRequestFormGenerator(HttpServletRequest request) {
    RequestBackingForm form = new RequestBackingForm();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("addRequestForm", form);
    addEditSelectorOptions(map);
    return map;
  }
 
  /*
    issue - dupicat eend point , refer /form
 
  @RequestMapping(value = "{id}/edit/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_REQUEST+"')")
  public  Map<String, Object> editRequestFormGenerator(HttpServletRequest request,
      @PathVariable Long id) {

    Request productRequest = requestRepository.findRequestById(id);
    RequestBackingForm form = new RequestBackingForm(productRequest);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("editRequestForm", form);
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("request");
    // to ensure custom field names are displayed in the form
    map.put("requestFields", formFields);
    return map;
  }
*/
  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_REQUEST+"')")
  public ResponseEntity<Map<String, Object>> addRequest(@Valid @RequestBody RequestBackingForm form) {

    HttpStatus httpStatus = HttpStatus.CREATED;
    Map<String, Object> map = new HashMap<String, Object>();

    Request savedRequest = null;
    Request productRequest = form.getRequest();
    productRequest.setIsDeleted(false);
    savedRequest = requestRepository.addRequest(productRequest);
    map.put("hasErrors", false);
    form = new RequestBackingForm();
    map.put("requestId", savedRequest.getId());
    map.put("request", new RequestViewModel(savedRequest));
    map.put("addAnotherRequestUrl", "addRequestFormGenerator.html");
    return new ResponseEntity < Map<String, Object> > (map, httpStatus);
  }

  @RequestMapping(value="{id}/issuedcomponents", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ISSUE_COMPONENT+"')")
  public  Map<String, Object> listIssuedProductsForRequest(@PathVariable Long id) {
    Map<String, Object> map = new HashMap<String, Object>();
    addEditSelectorOptions(map);
    List<Product> issuedProducts = requestRepository.getIssuedProductsForRequest(id);
    List<ProductViewModel> issuedProductViewModels = null;
    issuedProductViewModels = ProductController.getProductViewModels(issuedProducts);
    map.put("issuedProducts", issuedProductViewModels);
    map.put("productTypeFields", utilController.getFormFieldsForForm("ProductType"));
    return map;
  }
  
  @RequestMapping(value = "{id}",method = RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_REQUEST+"')")
  public  ResponseEntity updateRequest(@Valid @RequestBody RequestBackingForm form,
          @PathVariable Long id) {

      form.setId(id);
      form.setIsDeleted(false);
      requestRepository.updateRequest(form.getRequest());
      return new ResponseEntity(HttpStatus.NO_CONTENT);
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

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  public 
  ResponseEntity deleteRequest(
      @RequestParam("id") Long id) {
    requestRepository.deleteRequest(id);
     return new ResponseEntity(HttpStatus.NO_CONTENT);
  }


  @RequestMapping(value = "{id}/matchingcomponents", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.BLOOD_CROSS_MATCH_CHECK+"')")
  public  Map<String, Object> findMatchingProductsForRequest(
      @PathVariable Long id) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("requestId", id);
    List<MatchingProductViewModel> products = productRepository.findMatchingProductsForRequest(id);
    map.put("compatibilityTestFields", utilController.getFormFieldsForForm("CompatibilityTest"));
    map.put("allProducts", products);
    map.put("labProperties", genericConfigRepository.getConfigProperties("labsetup"));
    return map;
  }

  @RequestMapping(value = "{id}/issuecomponent", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.ISSUE_COMPONENT+"')")
  public  ResponseEntity issueSelectedProducts(
      @PathVariable Long id,
      @RequestParam String componentName) {
   
       requestRepository.issueProductsToRequest(id, componentName);
       return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
  
  /*
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
    m.put("nextPageUrl", getNextPageUrl(request));
    addEditSelectorOptions(m);

    map.put("model", m);
    return map;

  }
  */
  
}
