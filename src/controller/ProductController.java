package controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import model.collectedsample.CollectedSample;
import model.product.Product;
import model.product.ProductStatus;
import model.productmovement.ProductStatusChangeReason;
import model.productmovement.ProductStatusChangeReasonCategory;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

import repository.ProductRepository;
import repository.ProductStatusChangeReasonRepository;
import repository.ProductTypeRepository;
import utils.PermissionConstants;
import viewmodel.ProductViewModel;
import backingform.FindProductBackingForm;
import backingform.ProductBackingForm;
import backingform.ProductCombinationBackingForm;
import backingform.RecordProductBackingForm;
import backingform.validator.ProductBackingFormValidator;

@Controller
public class ProductController {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductStatusChangeReasonRepository productStatusChangeReasonRepository;
  
  @Autowired
  private ProductTypeRepository productTypeRepository;
  
  @Autowired
  private UtilController utilController;
  
  public ProductController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new ProductBackingFormValidator(binder.getValidator(), utilController));
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  public static String getNextPageUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString().replaceFirst("findProduct.html", "findProductPagination.html");
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "/productSummary", method = RequestMethod.GET)
  public @ResponseBody  Map<String, Object> productSummaryGenerator(HttpServletRequest request, Model model,
      @RequestParam(value = "productId", required = false) Long productId) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("requestUrl", getUrl(request));
    Product product = null;
    if (productId != null) {
      product = productRepository.findProductById(productId);
      if (product != null) {
        map.put("existingProduct", true);
      }
      else {
        map.put("existingProduct", false);
      }
    }
    ProductViewModel productViewModel = getProductViewModels(Arrays.asList(product)).get(0);
    Map<String, Object> tips = new HashMap<String, Object>();
    addEditSelectorOptions(map);
    utilController.addTipsToModel(tips, "products.findproduct.productsummary");
    map.put("tips", tips);
    map.put("product", productViewModel);
    map.put("refreshUrl", getUrl(request));
    map.put("productStatusChangeReasons",
        productStatusChangeReasonRepository.getAllProductStatusChangeReasonsAsMap());
    // to ensure custom field names are displayed in the form
    map.put("productFields", utilController.getFormFieldsForForm("Product"));
    return map;
  }

  @RequestMapping(value = "/findProductFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public @ResponseBody Map<String, Object> findProductFormGenerator(HttpServletRequest request) {

    FindProductBackingForm form = new FindProductBackingForm();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("findProductForm", form);

    Map<String, Object> tips = new HashMap<String, Object>();
    addEditSelectorOptions(map);
    utilController.addTipsToModel(tips, "products.find");
    map.put("tips", tips);
    // to ensure custom field names are displayed in the form
    map.put("productFields", utilController.getFormFieldsForForm("product"));
    map.put("refreshUrl", getUrl(request));
    return map;
  }

  @RequestMapping("/findProduct")
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public @ResponseBody  Map<String, Object> findProduct(HttpServletRequest request,
      @ModelAttribute("findProductForm") FindProductBackingForm form,
      BindingResult result, Model model) {

    List<Product> products = Arrays.asList(new Product[0]);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("productFields", utilController.getFormFieldsForForm("product"));
    map.put("allProducts", getProductViewModels(products));
    map.put("refreshUrl", getUrl(request));
    map.put("nextPageUrl", getNextPageUrl(request));
    addEditSelectorOptions(map);

    return map;
  }

  /**
   * Form Generator to create Record Product page
   */
  @RequestMapping(value = "/recordProductFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_COMPONENT+"')")
  public Map<String, Object> recordProductFormGenerator(HttpServletRequest request) {

  	RecordProductBackingForm form = new RecordProductBackingForm();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("findProductByPackNumberForm", form);

    Map<String, Object> tips = new HashMap<String, Object>();
    addEditSelectorOptions(map);
    utilController.addTipsToModel(tips, "products.find");
    map.put("tips", tips);
    // to ensure custom field names are displayed in the form
    map.put("productFields", utilController.getFormFieldsForForm("product"));
    map.put("refreshUrl", getUrl(request));
    return map;
  }
  
  /**
   * Get column name from column id, depends on sequence of columns in productsTable.jsp
   */
  private String getSortingColumn(int columnId, Map<String, Map<String, Object>> formFields) {

    List<String> visibleFields = new ArrayList<String>();
    visibleFields.add("id");
    for (String field : Arrays.asList("collectionNumber", "productType", "bloodGroup", "createdOn", "expiresOn", "status")) {
      Map<String, Object> fieldProperties = formFields.get(field);
      if (fieldProperties.get("hidden").equals(false))
        visibleFields.add(field);
    }

    Map<String, String> sortColumnMap = new HashMap<String, String>();
    sortColumnMap.put("id", "id");
    sortColumnMap.put("collectionNumber", "collectedSample.collectionNumber");
    sortColumnMap.put("productType", "productType.productType");
    // just sort by blood abo for now
    sortColumnMap.put("bloodGroup", "collectedSample.bloodAbo");
    sortColumnMap.put("createdOn", "createdOn");
    sortColumnMap.put("expiresOn", "expiresOn");
    sortColumnMap.put("status", "status");
    String sortColumn = visibleFields.get(columnId);

    if (sortColumnMap.get(sortColumn) == null)
      return "id";
    else
      return sortColumnMap.get(sortColumn);
  }
  
  @SuppressWarnings("unchecked")
  @RequestMapping("/findProductPagination")
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public Map<String, Object> findProductPagination(HttpServletRequest request,
      @ModelAttribute("findProductForm") FindProductBackingForm form,
      BindingResult result, Model model) {

    List<Product> products = Arrays.asList(new Product[0]);

    String searchBy = form.getSearchBy();

    Map<String, Object> pagingParams = utilController.parsePagingParameters(request);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");
    int sortColumnId = (Integer) pagingParams.get("sortColumnId");
    pagingParams.put("sortColumn", getSortingColumn(sortColumnId, formFields));

    List<Object> results = new ArrayList<Object>();
    if (searchBy.equals("collectionNumber")) {
      results = productRepository.findProductByCollectionNumber(
          form.getCollectionNumber(), form.getStatus(),
          pagingParams);
    } else if (searchBy.equals("productType")) {
      List<Integer> productTypeIds = new ArrayList<Integer>();
      productTypeIds.add(-1);
      for (String productTypeId : form.getProductTypes()) {
        productTypeIds.add(Integer.parseInt(productTypeId));
      }
      results = productRepository.findProductByProductTypes(
          productTypeIds, form.getStatus(), pagingParams);
    }

    products = (List<Product>) results.get(0);
    Long totalRecords = (Long) results.get(1);
    return generateDatatablesMap(products, totalRecords, formFields);
  }
  
  /**
   * Datatables on the client side expects a json response for rendering data from the server
   * in jquery datatables. Remember of columns is important and should match the column headings
   * in productsTable.jsp.
   */
  private @ResponseBody  Map<String, Object> generateDatatablesMap(List<Product> products, Long totalRecords, Map<String, Map<String, Object>> formFields) {
    Map<String, Object> productsMap = new HashMap<String, Object>();
    ArrayList<Object> productList = new ArrayList<Object>();
    for (ProductViewModel product : getProductViewModels(products)) {
      List<Object> row = new ArrayList<Object>();
      
      row.add(product.getId().toString());

      for (String property : Arrays.asList("collectionNumber", "productType", "bloodGroup", "createdOn", "expiresOn", "status")) {
        if (formFields.containsKey(property)) {
          Map<String, Object> properties = (Map<String, Object>)formFields.get(property);
          if (properties.get("hidden").equals(false)) {
            String propertyValue = property;
            try {
              propertyValue = BeanUtils.getProperty(product, property);
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              e.printStackTrace();
            } catch (NoSuchMethodException e) {
              e.printStackTrace();
            }
            if (property.equals("productType") &&
                StringUtils.isNotBlank(product.getSubdivisionCode())) {
              propertyValue = propertyValue + " (" + product.getSubdivisionCode() + ")";
            }
            row.add(propertyValue.toString());
          }
        }
      }

      productList.add(row);
    }
    productsMap.put("aaData", productList);
    productsMap.put("iTotalRecords", totalRecords);
    productsMap.put("iTotalDisplayRecords", totalRecords);
    return productsMap;
  }
  
  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("productTypes", productTypeRepository.getAllProductTypes());
  }

  @RequestMapping(value = "/addProductFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_COMPONENT+"')")
  public @ResponseBody Map<String, Object> addProductFormGenerator(HttpServletRequest request,
      Model model) {

    ProductBackingForm form = new ProductBackingForm();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("requestUrl", getUrl(request));
    map.put("firstTimeRender", true);
    map.put("addProductForm", form);
    map.put("refreshUrl", getUrl(request));
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");
    // to ensure custom field names are displayed in the form
    map.put("productFields", formFields);
    return map;
  }

  @RequestMapping(value = "/addProductCombinationFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public Map<String, Object> addProductCombinationFormGenerator(HttpServletRequest request,
      Model model) {

    ProductCombinationBackingForm form = new ProductCombinationBackingForm();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("requestUrl", getUrl(request));
    map.put("firstTimeRender", true);
    map.put("addProductCombinationForm", form);

    addOptionsForAddProductCombinationForm(map);
    map.put("refreshUrl", getUrl(request));
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");
    // to ensure custom field names are displayed in the form
    map.put("productFields", formFields);
    return map;
  }

  @RequestMapping(value = "/editProductFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_COMPONENT+"')")
  public @ResponseBody Map<String, Object> editProductFormGenerator(HttpServletRequest request,
      @RequestParam(value="productId") Long productId) {

    Product product = productRepository.findProductById(productId);
    ProductBackingForm form = new ProductBackingForm(product);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("requestUrl", getUrl(request));
    map.put("editProductForm", form);
    map.put("refreshUrl", getUrl(request));
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");
    // to ensure custom field names are displayed in the form
    map.put("productFields", formFields);
    return map;
  }

  @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_COMPONENT+"')")
  public @ResponseBody Map<String, Object> addProduct(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("addProductForm") @Valid ProductBackingForm form,
      BindingResult result, Model model) {

    Map<String, Object> map = new HashMap<String, Object>();
    boolean success = false;

    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");
    map.put("productFields", formFields);

    Product savedProduct = null;
    if (result.hasErrors()) {
      map.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        Product product = form.getProduct();
        product.setIsDeleted(false);
        savedProduct = productRepository.addProduct(product);
        map.put("hasErrors", false);
        success = true;
        form = new ProductBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
      }
    }

    if (success) {
      map.put("product", getProductViewModel(savedProduct));
      map.put("addAnotherProductUrl", "addProductFormGenerator.html");
    } else {
      map.put("errorMessage", "Error creating product. Please fix the errors noted below.");
      map.put("firstTimeRender", false);
      map.put("addProductForm", form);
      map.put("refreshUrl", "addProductFormGenerator.html");
    }

    map.put("success", success);
    return map;
  }

  @RequestMapping(value = "/addProductCombination", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_COMPONENT+"')")
  public @ResponseBody  Map<String, Object> addProductCombination(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("addProductCombinationForm") @Valid ProductCombinationBackingForm form,
      BindingResult result, Model model) {

    Map<String, Object> map = new HashMap<String, Object>();
    boolean success = false;

    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");
    map.put("productFields", formFields);

    List<Product> savedProducts = null;
    if (result.hasErrors()) {
      map.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        savedProducts = productRepository.addProductCombination(form);
        map.put("hasErrors", false);
        success = true;
        form = new ProductCombinationBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
      }
    }

    if (success) {
      // at least one product should be created, all products should have the same collection number
      map.put("collectionNumber", savedProducts.get(0).getCollectionNumber());
      map.put("createdProducts", getProductViewModels(savedProducts));
      List<Product> allProductsForCollection = productRepository.findProductsByCollectionNumber(savedProducts.get(0).getCollectionNumber());
      map.put("allProductsForCollection", getProductViewModels(allProductsForCollection));
      map.put("addAnotherProductUrl", "addProductCombinationFormGenerator.html");
    } else {
      map.put("errorMessage", "Error creating product. Please fix the errors noted below.");
      map.put("firstTimeRender", false);
      addOptionsForAddProductCombinationForm(map);
      map.put("addProductCombinationForm", form);
      map.put("refreshUrl", "addProductCombinationFormGenerator.html");
    }

    map.put("success", success);
    return map;
  }

  private ProductViewModel getProductViewModel(Product product) {
    return new ProductViewModel(product);
  }

  @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_COMPONENT+"')")
  public @ResponseBody  Map<String, Object> updateProduct(
      HttpServletResponse response,
      @ModelAttribute("editProductForm") @Valid ProductBackingForm form,
      BindingResult result, Model model) {

    Map<String, Object> map = new HashMap<String, Object>();
    boolean success = false;
    String message = "";
    addEditSelectorOptions(map);
    // only when the collection is correctly added the existingCollectedSample
    // property will be changed
    map.put("existingProduct", true);


    if (result.hasErrors()) {
      map.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      message = "Please fix the errors noted";
    }
    else {
      try {

        form.setIsDeleted(false);
        Product existingProduct = productRepository.updateProduct(form.getProduct());
        if (existingProduct == null) {
          map.put("hasErrors", true);
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          success = false;
          map.put("existingProduct", false);
          message = "Product does not already exist.";
        }
        else {
          map.put("hasErrors", false);
          success = true;
          message = "Product Successfully Updated";
        }
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        success = false;
        message = "Product Already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
   }

    map.put("editProductForm", form);
    map.put("success", success);
    map.put("errorMessage", message);
    map.put("productFields", utilController.getFormFieldsForForm("Product"));

    return map;
  }

  public static List<ProductViewModel> getProductViewModels(
      List<Product> products) {
    if (products == null)
      return Arrays.asList(new ProductViewModel[0]);
    List<ProductViewModel> productViewModels = new ArrayList<ProductViewModel>();
    for (Product product : products) {
      productViewModels.add(new ProductViewModel(product));
    }
    return productViewModels;
  }

  @RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.VOID_COMPONENT+"')")
  public @ResponseBody
  Map<String, ? extends Object> deleteProduct(
      @RequestParam("productId") Long productId) {

    boolean success = true;
    String errMsg = "";
    try {
      productRepository.deleteProduct(productId);
    } catch (Exception ex) {
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

  @RequestMapping(value = "/discardProductFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.DISCARD_COMPONENT+"')")
  public @ResponseBody Map<String, Object> discardProductFormGenerator(HttpServletRequest request,
      @RequestParam("productId") String productId) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("productId", productId);
    List<ProductStatusChangeReason> statusChangeReasons =
        productStatusChangeReasonRepository.getProductStatusChangeReasons(ProductStatusChangeReasonCategory.DISCARDED);
    map.put("discardReasons", statusChangeReasons);
    return map;
  }


  @RequestMapping(value = "/discardProduct", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.DISCARD_COMPONENT+"')")
  public @ResponseBody Map<String, Object> discardProduct(
      @RequestParam("productId") Long productId,
      @RequestParam("discardReasonId") Integer discardReasonId,
      @RequestParam("discardReasonText") String discardReasonText) {

    boolean success = true;
    String errMsg = "";
    try {
      ProductStatusChangeReason statusChangeReason = new ProductStatusChangeReason();
      statusChangeReason.setId(discardReasonId);
      productRepository.discardProduct(productId, statusChangeReason, discardReasonText);
    } catch (Exception ex) {
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

  @RequestMapping(value = "/returnProductFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.DISCARD_COMPONENT+"')")
  public @ResponseBody Map<String, Object> returnProductFormGenerator(HttpServletRequest request,
      @RequestParam("productId") String productId) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("productId", productId);
    List<ProductStatusChangeReason> statusChangeReasons =
        productStatusChangeReasonRepository.getProductStatusChangeReasons(ProductStatusChangeReasonCategory.RETURNED);
    map.put("returnReasons", statusChangeReasons);
    return map;
  }

  @RequestMapping(value = "/splitProductFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_COMPONENT_COMBINATIONS+"')")
  public @ResponseBody Map<String, Object> splitProductFormGenerator(HttpServletRequest request,
      @RequestParam("productId") Long productId) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("productId", productId);
    map.put("product", getProductViewModel(productRepository.findProduct(productId)));
    return map;
  }


  @RequestMapping(value = "/splitProduct", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_COMPONENT+"')")
  public @ResponseBody Map<String, Object> discardProduct(
      @RequestParam("productId") Long productId,
      @RequestParam("numProductsAfterSplitting") Integer numProductsAfterSplitting) {

    boolean success = true;
    String errMsg = "";
    try {
      success = productRepository.splitProduct(productId, numProductsAfterSplitting);
      if (!success)
        errMsg = "Product cannot be split";
    } catch (Exception ex) {
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

  @RequestMapping(value = "/viewProductHistory", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public Map<String, Object> viewProductHistory(HttpServletRequest request, Model model,
      @RequestParam(value = "productId", required = false) Long productId) {

    Map<String, Object> map = new HashMap<String, Object>();

    Product product = null;
    if (productId != null) {
      product = productRepository.findProductById(productId);
      if (product != null) {
        map.put("existingProduct", true);
      }
      else {
        map.put("existingProduct", false);
      }
    }

    ProductViewModel productViewModel = getProductViewModel(product);
    map.put("product", productViewModel);
    map.put("allProductMovements", productRepository.getProductStatusChanges(product));
    map.put("refreshUrl", getUrl(request));
    map.put("productFields", utilController.getFormFieldsForForm("product"));
    // to ensure custom field names are displayed in the form
    return map;
  }

  @RequestMapping(value = "/returnProduct", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.DISCARD_COMPONENT+"')")
  public @ResponseBody Map<String, Object> returnProduct(
      @RequestParam("productId") Long productId,
      @RequestParam("returnReasonId") Integer returnReasonId,
      @RequestParam("returnReasonText") String returnReasonText) {

    boolean success = true;
    String errMsg = "";
    try {
      ProductStatusChangeReason statusChangeReason = new ProductStatusChangeReason();
      statusChangeReason.setId(returnReasonId);
      productRepository.returnProduct(productId, statusChangeReason, returnReasonText);
    } catch (Exception ex) {
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

  private void addOptionsForAddProductCombinationForm(Map<String, Object> m) {
    m.put("productTypes", productTypeRepository.getAllProductTypes());

    List<ProductTypeCombination> productTypeCombinations = productTypeRepository.getAllProductTypeCombinations();
    m.put("productTypeCombinations", productTypeCombinations);

    ObjectMapper mapper = new ObjectMapper();
    Map<Integer, String> productTypeCombinationsMap = new HashMap<Integer, String>();
    for (ProductTypeCombination productTypeCombination : productTypeCombinations) {
      Map<String, String> productExpiryIntervals = new HashMap<String, String>();
      for (ProductType productType : productTypeCombination.getProductTypes()) {
        Integer expiryIntervalMinutes = productType.getExpiryIntervalMinutes();
        productExpiryIntervals.put(productType.getId().toString(), expiryIntervalMinutes.toString());
      }

      try {
        productTypeCombinationsMap.put(productTypeCombination.getId(), mapper.writeValueAsString(productExpiryIntervals));
      } catch (JsonGenerationException e) {
        e.printStackTrace();
      } catch (JsonMappingException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    m.put("productTypeCombinationsMap", productTypeCombinationsMap);
  }
  
  public static String getNextPageUrlForRecordProduct(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString().replaceFirst("findProductByPackNumber.html", "findProductByPackNumberPagination.html");
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }
  
  @RequestMapping("/findProductByPackNumber")
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public @ResponseBody Map<String, Object> findProductByPackNumber(HttpServletRequest request,
      @ModelAttribute("findProductByPackNumberForm") RecordProductBackingForm form,
      BindingResult result, Model model) {

    List<Product> products = Arrays.asList(new Product[0]);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("productFields", utilController.getFormFieldsForForm("product"));
    map.put("allProducts", getProductViewModels(products));
    map.put("refreshUrl", getUrl(request));
    map.put("nextPageUrl", getNextPageUrlForRecordProduct(request));
    map.put("addProductForm", form);
    addEditSelectorOptionsForNewRecord(map);

    return map;
  }
  @SuppressWarnings("unchecked")
  @RequestMapping("/findProductByPackNumberPagination")
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public Map<String, Object> findProductByPackNumberPagination(HttpServletRequest request,
      @ModelAttribute("findProductByPackNumberForm") RecordProductBackingForm form,
      BindingResult result, Model model) {

    List<Product> products = Arrays.asList(new Product[0]);

    Map<String, Object> pagingParams = utilController.parsePagingParameters(request);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");
    int sortColumnId = (Integer) pagingParams.get("sortColumnId");
    pagingParams.put("sortColumn", getSortingColumn(sortColumnId, formFields));

    List<Object> results = new ArrayList<Object>();
    List<String> status = Arrays.asList("QUARANTINED", "AVAILABLE", "EXPIRED", "UNSAFE", "ISSUED", "USED", "SPLIT", "DISCARDED", "PROCESSED");
    
      results = productRepository.findProductByCollectionNumber(
          form.getCollectionNumber(), status,
          pagingParams);

    products = (List<Product>) results.get(0);
    Long totalRecords = (Long) results.get(1);
    return generateRecordProductTablesMap(products, totalRecords, formFields);
  }
  
  /**
   * Datatables on the client side expects a json response for rendering data from the server
   * in jquery datatables. Remember of columns is important and should match the column headings
   * in recordProductTable.jsp.
   */
  private @ResponseBody  Map<String, Object> generateRecordProductTablesMap(List<Product> products, Long totalRecords, Map<String, Map<String, Object>> formFields) {
    Map<String, Object> productsMap = new HashMap<String, Object>();
    ArrayList<Object> productList = new ArrayList<Object>();
    for (ProductViewModel product : getProductViewModels(products)) {
      List<Object> row = new ArrayList<Object>();
      
      row.add(product.getId().toString());
      row.add(product.getCollectedSample().getId());
      for (String property : Arrays.asList("productType", "donationIdentificationNumber", "createdOn", "expiresOn", "status", "createdBy")) {
        if (formFields.containsKey(property)) {
          Map<String, Object> properties = (Map<String, Object>)formFields.get(property);
          if (properties.get("hidden").equals(false)) {
            String propertyValue = property;
            try {
              propertyValue = BeanUtils.getProperty(product, property);
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              e.printStackTrace();
            } catch (NoSuchMethodException e) {
              e.printStackTrace();
            }
            if (property.equals("productType") &&
                StringUtils.isNotBlank(product.getSubdivisionCode())) {
              propertyValue = propertyValue + " (" + product.getSubdivisionCode() + ")";
            }
            row.add(propertyValue.toString());
          }
        }
      }

      productList.add(row);
    }
    productsMap.put("aaData", productList);
    productsMap.put("iTotalRecords", totalRecords);
    productsMap.put("iTotalDisplayRecords", totalRecords);
    return productsMap;
  }
  
  @RequestMapping("/recordNewProductComponents")
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_COMPONENT+"')")
  public @ResponseBody Map<String, Object> recordNewProductComponents(HttpServletRequest request,
      @ModelAttribute("findProductByPackNumberForm") RecordProductBackingForm form,
      BindingResult result, Model model) {

      ProductType productType2 = productRepository.findProductTypeBySelectedProductType(Integer.valueOf(form.getProductTypes().get(0)));
      String collectionNumber = form.getCollectionNumber();
      String status = form.getStatus().get(0);
      long productId = form.getProductID();
      
      if(collectionNumber.contains("-")){
      	collectionNumber = collectionNumber.split("-")[0];
      }
      String sortName = productType2.getProductTypeNameShort();
      int noOfUnits = form.getNoOfUnits();
      //long hiddenCollectedSampleID =Long.parseLong(request.getParameter("hiddenCollectedSampleID"));
      long collectedSampleID = form.getCollectedSampleID();
      
      String createdPackNumber = collectionNumber +"-"+sortName;
      
      // Add New product
      if(!status.equalsIgnoreCase("PROCESSED")){
      if(noOfUnits > 0 ){
      	
      	for(int i=1; i <= noOfUnits ; i++){
      		try{
	        	Product product = new Product();
	          product.setIsDeleted(false);
	          product.setDonationIdentificationNumber(createdPackNumber+"-"+i);
	          DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	          Date createdOn = formatter.parse(form.getDateExpiresFrom());
	          Date expiresOn = formatter.parse(form.getDateExpiresTo());
	          
	          product.setCreatedOn(createdOn);
	          product.setExpiresOn(expiresOn);
	          ProductType productType = new ProductType();
	          productType.setProductType(form.getProductTypes().get(0));
	          productType.setId(Integer.parseInt(form.getProductTypes().get(0)));
	          product.setProductType(productType);
	          CollectedSample collectedSample = new CollectedSample();
	          collectedSample.setId(collectedSampleID);
	          product.setCollectedSample(collectedSample);
	          product.setStatus(ProductStatus.QUARANTINED);
		        productRepository.addProduct(product);

		        // Once product save successfully update selected product status with processed
		        productRepository.updateProductByProductId(productId);
		        
		      } catch (EntityExistsException ex) {
		        ex.printStackTrace();
		      } catch (Exception ex) {
		        ex.printStackTrace();
		      }
      	}
      }
      else{
      	
      	try{
	        	Product product = new Product();
	          product.setIsDeleted(false);
	          product.setDonationIdentificationNumber(createdPackNumber);
	          DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	          Date createdOn = formatter.parse(form.getDateExpiresFrom());
	          Date expiresOn = formatter.parse(form.getDateExpiresTo());
	          
	          product.setCreatedOn(createdOn);
	          product.setExpiresOn(expiresOn);
	          ProductType productType = new ProductType();
	          productType.setProductType(form.getProductTypes().get(0));
	          productType.setId(Integer.parseInt(form.getProductTypes().get(0)));
	          product.setProductType(productType);
	          CollectedSample collectedSample = new CollectedSample();
	          collectedSample.setId(collectedSampleID);
	          product.setCollectedSample(collectedSample);
	          product.setStatus(ProductStatus.QUARANTINED);
		        productRepository.addProduct(product);
		        productRepository.updateProductByProductId(productId);
		        
		      } catch (EntityExistsException ex) {
		        ex.printStackTrace();
		      } catch (Exception ex) {
		        ex.printStackTrace();
		      }
	    	}
      }
   
    List<Product> products = Arrays.asList(new Product[0]);
   
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("productFields", utilController.getFormFieldsForForm("product"));
    map.put("allProducts", getProductViewModels(products));
    map.put("refreshUrl", getUrlForNewProduct(request,form.getCollectionNumber()));
    map.put("nextPageUrl", getNextPageUrlForNewRecordProduct(request,form.getCollectionNumber()));
    map.put("addProductForm", form);
    
    if(form.getCollectionNumber().contains("-")){
    	addEditSelectorOptionsForNewRecordByList(map,productType2);
  	}
  	else{
  		 addEditSelectorOptionsForNewRecord(map);
  	}

    return map;
  }
  
  @RequestMapping("/getRecordNewProductComponents")
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_COMPONENT+"')")
  public @ResponseBody Map<String, Object> getRecordNewProductComponents(HttpServletRequest request,
      @ModelAttribute("findProductByPackNumberForm") RecordProductBackingForm form,
      BindingResult result, Model model) {

  	ProductType productType = null;
  	if(form.getProductTypes() != null){
	  	String productTypeName = form.getProductTypes().get(form.getProductTypes().size()-1);
	  	productType = productRepository.findProductTypeByProductTypeName(productTypeName);
  	}
  	List<Product> products = Arrays.asList(new Product[0]);
  	
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("productFields", utilController.getFormFieldsForForm("product"));
    map.put("allProducts", getProductViewModels(products));
    map.put("refreshUrl", getUrlForNewProduct(request,form.getCollectionNumber()));
    map.put("nextPageUrl", getNextPageUrlForNewRecordProduct(request,form.getCollectionNumber()));
    map.put("addProductForm", form);
    
    if(form.getCollectionNumber().contains("-") && form.getProductTypes() != null){
    	addEditSelectorOptionsForNewRecordByList(map,productType);
  	}
  	else{
  		 addEditSelectorOptionsForNewRecord(map);
  	}
    
    return map;
  }
  
  private void addEditSelectorOptionsForNewRecordByList(Map<String, Object> m, ProductType productType) {
    m.put("productTypes", productTypeRepository.getProductTypeByIdList(productType.getId()));
  }
  private void addEditSelectorOptionsForNewRecord(Map<String, Object> m) {
    m.put("productTypes", productTypeRepository.getAllParentProductTypes());
  }
  
  public static String getUrlForNewProduct(HttpServletRequest req,String qString) {
    String reqUrl = req.getRequestURL().toString();
    String queryString[] = qString.split("-");   
    if (queryString != null) {
        reqUrl += "?collectionNumber="+queryString[0];
    }
    return reqUrl;
  }
  
  public static String getNextPageUrlForNewRecordProduct(HttpServletRequest req,String qString) {
  	String reqUrl ="";
  	if(req.getRequestURI().contains("recordNewProductComponents")){
  		reqUrl = req.getRequestURL().toString().replaceFirst("recordNewProductComponents.html", "findProductByPackNumberPagination.html");
  	}
  	else{
  		reqUrl = req.getRequestURL().toString().replaceFirst("getRecordNewProductComponents.html", "findProductByPackNumberPagination.html");
  	}
    String queryString[] = qString.split("-"); 
    if (queryString != null) {
        reqUrl += "?collectionNumber="+queryString[0];
    }
    return reqUrl;
  }
  
}
