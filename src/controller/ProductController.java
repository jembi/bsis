package controller;

import java.io.IOException;
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

import model.product.FindProductBackingForm;
import model.product.Product;
import model.product.ProductBackingForm;
import model.product.ProductBackingFormValidator;
import model.product.ProductCombinationBackingForm;
import model.productmovement.ProductStatusChangeReason;
import model.productmovement.ProductStatusChangeReasonCategory;
import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.ProductRepository;
import repository.ProductStatusChangeReasonRepository;
import repository.ProductTypeRepository;
import viewmodel.ProductViewModel;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
  public ModelAndView productSummaryGenerator(HttpServletRequest request, Model model,
      @RequestParam(value = "productId", required = false) Long productId) {

    ModelAndView mv = new ModelAndView("products/productSummary");
    mv.addObject("requestUrl", getUrl(request));
    Product product = null;
    if (productId != null) {
      product = productRepository.findProductById(productId);
      if (product != null) {
        mv.addObject("existingProduct", true);
      }
      else {
        mv.addObject("existingProduct", false);
      }
    }
    ProductViewModel productViewModel = getProductViewModels(Arrays.asList(product)).get(0);
    Map<String, Object> tips = new HashMap<String, Object>();
    addEditSelectorOptions(mv.getModelMap());
    utilController.addTipsToModel(tips, "products.findproduct.productsummary");
    mv.addObject("tips", tips);
    mv.addObject("product", productViewModel);
    mv.addObject("refreshUrl", getUrl(request));
    mv.addObject("productStatusChangeReasons",
        productStatusChangeReasonRepository.getAllProductStatusChangeReasonsAsMap());
    // to ensure custom field names are displayed in the form
    mv.addObject("productFields", utilController.getFormFieldsForForm("Product"));
    return mv;
  }

  @RequestMapping(value = "/findProductFormGenerator", method = RequestMethod.GET)
  public ModelAndView findProductFormGenerator(HttpServletRequest request) {

    FindProductBackingForm form = new FindProductBackingForm();
    ModelAndView mv = new ModelAndView("products/findProductForm");
    mv.addObject("findProductForm", form);

    Map<String, Object> tips = new HashMap<String, Object>();
    addEditSelectorOptions(mv.getModelMap());
    utilController.addTipsToModel(tips, "products.find");
    mv.addObject("tips", tips);
    // to ensure custom field names are displayed in the form
    mv.addObject("productFields", utilController.getFormFieldsForForm("product"));
    mv.addObject("refreshUrl", getUrl(request));
    return mv;
  }

  @RequestMapping("/findProduct")
  public ModelAndView findProduct(HttpServletRequest request,
      @ModelAttribute("findProductForm") FindProductBackingForm form,
      BindingResult result, Model model) {

    List<Product> products = Arrays.asList(new Product[0]);

    ModelAndView mv = new ModelAndView("products/productsTable");
    mv.addObject("tableName", "findProductsTable");
    mv.addObject("productFields", utilController.getFormFieldsForForm("product"));
    mv.addObject("allProducts", getProductViewModels(products));
    mv.addObject("refreshUrl", getUrl(request));
    mv.addObject("nextPageUrl", getNextPageUrl(request));
    addEditSelectorOptions(mv.getModelMap());

    return mv;
  }

  /**
   * Get column name from column id, depends on sequence of columns in productsTable.jsp
   */
  private String getSortingColumn(int columnId, Map<String, Map<String, Object>> formFields) {

    List<String> visibleFields = new ArrayList<String>();
    visibleFields.add("id");
    for (String field : Arrays.asList("collectionNumber", "productType","createdOn", "expiresOn", "status")) {
      Map<String, Object> fieldProperties = formFields.get(field);
      if (fieldProperties.get("hidden").equals(false))
        visibleFields.add(field);
    }

    Map<String, String> sortColumnMap = new HashMap<String, String>();
    sortColumnMap.put("id", "id");
    sortColumnMap.put("collectionNumber", "collectedSample.collectionNumber");
    sortColumnMap.put("productType", "productType.productType");
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
  public @ResponseBody Map<String, Object> findProductPagination(HttpServletRequest request,
      @ModelAttribute("findProductForm") FindProductBackingForm form,
      BindingResult result, Model model) {

    List<Product> products = Arrays.asList(new Product[0]);

    String searchBy = form.getSearchBy();

    Map<String, Object> pagingParams = utilController.parsePagingParameters(request);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");
    int sortColumnId = (Integer) pagingParams.get("sortColumnId");
    pagingParams.put("sortColumn", getSortingColumn(sortColumnId, formFields));

    List<Object> results = new ArrayList<Object>();
    if (searchBy.equals("productNumber")) {
      results = productRepository.findProductByProductNumber(form.getProductNumber(), form.getStatus(),
                                          pagingParams);
    } else if (searchBy.equals("collectionNumber")) {
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
  private Map<String, Object> generateDatatablesMap(List<Product> products, Long totalRecords, Map<String, Map<String, Object>> formFields) {
    Map<String, Object> productsMap = new HashMap<String, Object>();
    ArrayList<Object> productList = new ArrayList<Object>();
    for (ProductViewModel product : getProductViewModels(products)) {
      List<Object> row = new ArrayList<Object>();
      
      row.add(product.getId().toString());

      for (String property : Arrays.asList("collectionNumber", "productType", "createdOn", "expiresOn", "status")) {
        if (formFields.containsKey(property)) {
          Map<String, Object> properties = (Map<String, Object>)formFields.get(property);
          if (properties.get("hidden").equals(false)) {
            String propertyValue = property;
            try {
              propertyValue = BeanUtils.getProperty(product, property);
            } catch (IllegalAccessException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (NoSuchMethodException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
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
  public ModelAndView addProductFormGenerator(HttpServletRequest request,
      Model model) {

    ProductBackingForm form = new ProductBackingForm();

    ModelAndView mv = new ModelAndView("products/addProductForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("addProductForm", form);
    mv.addObject("refreshUrl", getUrl(request));
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");
    // to ensure custom field names are displayed in the form
    mv.addObject("productFields", formFields);
    return mv;
  }

  @RequestMapping(value = "/addProductCombinationFormGenerator", method = RequestMethod.GET)
  public ModelAndView addProductCombinationFormGenerator(HttpServletRequest request,
      Model model) {

    ProductCombinationBackingForm form = new ProductCombinationBackingForm();

    ModelAndView mv = new ModelAndView("products/addProductCombinationForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("addProductCombinationForm", form);

    addOptionsForAddProductCombinationForm(mv.getModelMap());
    mv.addObject("refreshUrl", getUrl(request));
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");
    // to ensure custom field names are displayed in the form
    mv.addObject("productFields", formFields);
    return mv;
  }

  @RequestMapping(value = "/editProductFormGenerator", method = RequestMethod.GET)
  public ModelAndView editProductFormGenerator(HttpServletRequest request,
      @RequestParam(value="productId") Long productId) {

    Product product = productRepository.findProductById(productId);
    ProductBackingForm form = new ProductBackingForm(product);

    ModelAndView mv = new ModelAndView("products/editProductForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("editProductForm", form);
    mv.addObject("refreshUrl", getUrl(request));
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");
    // to ensure custom field names are displayed in the form
    mv.addObject("productFields", formFields);
    return mv;
  }

  @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
  public ModelAndView addProduct(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("addProductForm") @Valid ProductBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView();
    boolean success = false;

    addEditSelectorOptions(mv.getModelMap());
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");
    mv.addObject("productFields", formFields);

    Product savedProduct = null;
    if (result.hasErrors()) {
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        Product product = form.getProduct();
        product.setIsDeleted(false);
        savedProduct = productRepository.addProduct(product);
        mv.addObject("hasErrors", false);
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
      mv.addObject("product", getProductViewModel(savedProduct));
      mv.addObject("addAnotherProductUrl", "addProductFormGenerator.html");
      mv.setViewName("products/addProductSuccess");
    } else {
      mv.addObject("errorMessage", "Error creating product. Please fix the errors noted below.");
      mv.addObject("firstTimeRender", false);
      mv.addObject("addProductForm", form);
      mv.addObject("refreshUrl", "addProductFormGenerator.html");
      mv.setViewName("products/addProductError");
    }

    mv.addObject("success", success);
    return mv;
  }

  @RequestMapping(value = "/addProductCombination", method = RequestMethod.POST)
  public ModelAndView addProductCombination(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("addProductCombinationForm") @Valid ProductCombinationBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView();
    boolean success = false;

    addEditSelectorOptions(mv.getModelMap());
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("product");
    mv.addObject("productFields", formFields);

    List<Product> savedProducts = null;
    if (result.hasErrors()) {
      for (ObjectError error : result.getAllErrors()) {
        System.out.println(error.getObjectName());
        System.out.println(error.getDefaultMessage());
      }
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        savedProducts = productRepository.addProductCombination(form);
        mv.addObject("hasErrors", false);
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
      mv.addObject("collectionNumber", savedProducts.get(0).getCollectionNumber());
      mv.addObject("createdProducts", getProductViewModels(savedProducts));
      List<Product> allProductsForCollection = productRepository.findProductsByCollectionNumber(savedProducts.get(0).getCollectionNumber());
      mv.addObject("allProductsForCollection", getProductViewModels(allProductsForCollection));
      mv.addObject("addAnotherProductUrl", "addProductCombinationFormGenerator.html");
      mv.setViewName("products/addProductCombinationSuccess");
    } else {
      mv.addObject("errorMessage", "Error creating product. Please fix the errors noted below.");
      mv.addObject("firstTimeRender", false);
      addOptionsForAddProductCombinationForm(mv.getModelMap());
      mv.addObject("addProductCombinationForm", form);
      mv.addObject("refreshUrl", "addProductCombinationFormGenerator.html");
      mv.setViewName("products/addProductCombinationError");
    }

    mv.addObject("success", success);
    return mv;
  }

  private ProductViewModel getProductViewModel(Product product) {
    return new ProductViewModel(product);
  }

  @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
  public ModelAndView updateProduct(
      HttpServletResponse response,
      @ModelAttribute("editProductForm") @Valid ProductBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("products/editProductForm");
    boolean success = false;
    String message = "";
    addEditSelectorOptions(mv.getModelMap());
    // only when the collection is correctly added the existingCollectedSample
    // property will be changed
    mv.addObject("existingProduct", true);


    if (result.hasErrors()) {
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      message = "Please fix the errors noted";
    }
    else {
      try {

        form.setIsDeleted(false);
        Product existingProduct = productRepository.updateProduct(form.getProduct());
        if (existingProduct == null) {
          mv.addObject("hasErrors", true);
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          success = false;
          mv.addObject("existingProduct", false);
          message = "Product does not already exist.";
        }
        else {
          mv.addObject("hasErrors", false);
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

    mv.addObject("editProductForm", form);
    mv.addObject("success", success);
    mv.addObject("errorMessage", message);
    mv.addObject("productFields", utilController.getFormFieldsForForm("Product"));

    return mv;
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
  public @ResponseBody
  Map<String, ? extends Object> deleteProduct(
      @RequestParam("productId") Long productId) {

    boolean success = true;
    String errMsg = "";
    try {
      productRepository.deleteProduct(productId);
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

  @RequestMapping(value = "/discardProductFormGenerator", method = RequestMethod.GET)
  public ModelAndView discardProductFormGenerator(HttpServletRequest request,
      @RequestParam("productId") String productId) {
    ModelAndView mv = new ModelAndView("products/discardProductForm");
    mv.addObject("productId", productId);
    List<ProductStatusChangeReason> statusChangeReasons =
        productStatusChangeReasonRepository.getProductStatusChangeReasons(ProductStatusChangeReasonCategory.DISCARDED);
    mv.addObject("discardReasons", statusChangeReasons);
    return mv;
  }


  @RequestMapping(value = "/discardProduct", method = RequestMethod.POST)
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
  public ModelAndView returnProductFormGenerator(HttpServletRequest request,
      @RequestParam("productId") String productId) {
    ModelAndView mv = new ModelAndView("products/returnProductForm");
    mv.addObject("productId", productId);
    List<ProductStatusChangeReason> statusChangeReasons =
        productStatusChangeReasonRepository.getProductStatusChangeReasons(ProductStatusChangeReasonCategory.RETURNED);
    mv.addObject("returnReasons", statusChangeReasons);
    return mv;
  }

  @RequestMapping(value = "/viewProductHistory", method = RequestMethod.GET)
  public ModelAndView viewProductHistory(HttpServletRequest request, Model model,
      @RequestParam(value = "productId", required = false) Long productId) {

    ModelAndView mv = new ModelAndView("products/productMovements");

    Product product = null;
    if (productId != null) {
      product = productRepository.findProductById(productId);
      if (product != null) {
        mv.addObject("existingProduct", true);
      }
      else {
        mv.addObject("existingProduct", false);
      }
    }

    ProductViewModel productViewModel = getProductViewModel(product);
    mv.addObject("product", productViewModel);
    mv.addObject("allProductMovements", productRepository.getProductStatusChanges(product));
    mv.addObject("refreshUrl", getUrl(request));
    mv.addObject("productFields", utilController.getFormFieldsForForm("product"));
    // to ensure custom field names are displayed in the form
    return mv;
  }

  @RequestMapping(value = "/returnProduct", method = RequestMethod.POST)
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
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (JsonMappingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    m.put("productTypeCombinationsMap", productTypeCombinationsMap);
  }
}
