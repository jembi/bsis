package controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import model.collectedsample.CollectedSample;
import model.product.FindProductBackingForm;
import model.product.Product;
import model.product.ProductBackingForm;
import model.product.ProductBackingFormValidator;
import model.testresults.TestResult;

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

import repository.CollectedSampleRepository;
import repository.ProductRepository;
import repository.ProductTypeRepository;
import repository.TestResultRepository;
import viewmodel.ProductViewModel;

@Controller
public class ProductController {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private ProductTypeRepository productTypeRepository;

  @Autowired
  private TestResultRepository testResultsRepository;
  
  @Autowired
  private UtilController utilController;
  
  public ProductController() {
  }

  @PostConstruct
  public void setQuarantineStatus() {
    productRepository.updateQuarantineStatus();
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

    ModelAndView mv = new ModelAndView("productSummary");
    Map<String, Object> m = model.asMap();

    m.put("requestUrl", getUrl(request));

    Product product = null;
    if (productId != null) {
      product = productRepository.findProductById(productId);
      if (product != null) {
        m.put("existingProduct", true);
      }
      else {
        m.put("existingProduct", false);
      }
    }

    ProductViewModel productViewModel = getProductViewModels(Arrays.asList(product)).get(0);
    m.put("product", productViewModel);
    m.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    m.put("productFields", utilController.getFormFieldsForForm("Product"));
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/testResultsForProduct", method = RequestMethod.GET)
  public ModelAndView testResultsForProduct(HttpServletRequest request, Model model,
      @RequestParam(value = "productId", required = false) Long productId) {

    System.out.println("here");
    ModelAndView mv = new ModelAndView("testResultsForProduct");
    Map<String, Object> m = model.asMap();

    m.put("requestUrl", getUrl(request));

    Product product = null;
    if (productId != null) {
      product = productRepository.findProductById(productId);
      if (product != null) {
        m.put("existingProduct", true);
      }
      else {
        m.put("existingProduct", false);
      }
    }

    String collectionNumber = product.getCollectedSample().getCollectionNumber();
    List<TestResult> testResults = testResultsRepository.findTestResults(collectionNumber, "", "");

    m.put("allTestResults", TestResultController.getTestResultViewModels(testResults));
    m.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    m.put("testResultFields", utilController.getFormFieldsForForm("TestResult"));
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/findProductFormGenerator", method = RequestMethod.GET)
  public ModelAndView findProductFormGenerator(HttpServletRequest request, Model model) {

    FindProductBackingForm form = new FindProductBackingForm();
    model.addAttribute("findProductForm", form);

    ModelAndView mv = new ModelAndView("findProductForm");
    Map<String, Object> m = model.asMap();
    addEditSelectorOptions(m);
    utilController.addTipsToModel(model.asMap(), "products.find");
    // to ensure custom field names are displayed in the form
    m.put("productFields", utilController.getFormFieldsForForm("product"));
    m.put("refreshUrl", getUrl(request));
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping("/findProduct")
  public ModelAndView findProduct(HttpServletRequest request,
      @ModelAttribute("findProductForm") FindProductBackingForm form,
      BindingResult result, Model model) {

    List<Product> products = Arrays.asList(new Product[0]);

    ModelAndView modelAndView = new ModelAndView("productsTable");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findProductsTable");
    m.put("productFields", utilController.getFormFieldsForForm("product"));
    m.put("allProducts", getProductViewModels(products));
    m.put("refreshUrl", getUrl(request));
    m.put("nextPageUrl", getNextPageUrl(request));
    addEditSelectorOptions(m);

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  /**
   * Get column name from column id, depends on sequence of columns in productsTable.jsp
   */
  private String getSortingColumn(int columnId, Map<String, Object> formFields) {

    List<String> visibleFields = new ArrayList<String>();
    visibleFields.add("id");
    for (String field : Arrays.asList("productNumber", "productType","createdOn", "expiresOn", "status")) {
      Map<String, Object> fieldProperties = (Map<String, Object>) formFields.get(field);
      if (fieldProperties.get("hidden").equals(false))
        visibleFields.add(field);
    }

    Map<String, String> sortColumnMap = new HashMap<String, String>();
    sortColumnMap.put("id", "id");
    sortColumnMap.put("productNumber", "productNumber");
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
    Map<String, Object> formFields = utilController.getFormFieldsForForm("product");
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
  private Map<String, Object> generateDatatablesMap(List<Product> products, Long totalRecords, Map<String, Object> formFields) {
    Map<String, Object> productsMap = new HashMap<String, Object>();
    ArrayList<Object> productList = new ArrayList<Object>();
    for (ProductViewModel product : getProductViewModels(products)) {
      List<Object> row = new ArrayList<Object>();
      
      row.add(product.getId().toString());

      for (String property : Arrays.asList("productNumber", "productType", "createdOn", "expiresOn", "status")) {
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

  @RequestMapping(value = "/editProductFormGenerator", method = RequestMethod.GET)
  public ModelAndView editProductFormGenerator(HttpServletRequest request,
      Model model,
      @RequestParam(value="productId", required=false) Long productId) {

    ProductBackingForm form = new ProductBackingForm();

    ModelAndView mv = new ModelAndView("editProductForm");
    Map<String, Object> m = model.asMap();
    m.put("refreshUrl", getUrl(request));
    m.put("existingProduct", false);
    if (productId != null) {
      form.setId(productId);
      Product product = productRepository.findProductById(productId);
      if (product != null) {
        form = new ProductBackingForm(product);
        m.put("existingProduct", true);
      }
      else {
        form = new ProductBackingForm();
      }
    }
    addEditSelectorOptions(m);
    m.put("editProductForm", form);
    m.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    m.put("productFields", utilController.getFormFieldsForForm("Product"));
    mv.addObject("model", m);
    System.out.println(mv.getViewName());
    return mv;
  }

  @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
  public ModelAndView addProduct(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("editProductForm") @Valid ProductBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editProductForm");
    boolean success = false;
    String message = "";
    Map<String, Object> m = model.asMap();

    // IMPORTANT: Validation code just checks if the ID exists.
    // We still need to store the collected sample as part of the product.
    String collectionNumber = form.getCollectionNumber();
    if (collectionNumber != null && !collectionNumber.isEmpty()) {
      try {
        CollectedSample collectedSample = collectedSampleRepository.findSingleCollectedSampleByCollectionNumber(collectionNumber);
        form.setCollectedSample(collectedSample);
      } catch (NoResultException ex) {
        form.setCollectedSample(null);
        ex.printStackTrace();
      }
    } else {
      form.setCollectedSample(null);
    }

    if (result.hasErrors()) {
      m.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);      
      success = false;
      message = "Please fix the errors noted above.";
    } else {
      try {
        Product product = form.getProduct();
        product.setIsDeleted(false);
        productRepository.addProduct(product);
        m.put("hasErrors", false);
        success = true;
        message = "Product Successfully Added";
        form = new ProductBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
        message = "Product Already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
    }

    m.put("editProductForm", form);
    m.put("existingProduct", false);
    m.put("success", success);
    m.put("message", message);
    m.put("refreshUrl", "editProductFormGenerator.html");
    m.put("productFields", utilController.getFormFieldsForForm("product"));
    addEditSelectorOptions(m);

    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
  public ModelAndView updateProduct(
      HttpServletResponse response,
      @ModelAttribute("editProductForm") @Valid ProductBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editProductForm");
    boolean success = false;
    String message = "";
    Map<String, Object> m = model.asMap();
    addEditSelectorOptions(m);
    // only when the collection is correctly added the existingCollectedSample
    // property will be changed
    m.put("existingProduct", true);

    // IMPORTANT: Validation code just checks if the ID exists.
    // We still need to store the collected sample as part of the product.
    String collectionNumber = form.getCollectionNumber();
    if (collectionNumber != null && !collectionNumber.isEmpty()) {
      try {
        CollectedSample collectedSample = collectedSampleRepository.findSingleCollectedSampleByCollectionNumber(collectionNumber);
        form.setCollectedSample(collectedSample);
      } catch (NoResultException ex) {
        form.setCollectedSample(null);
        ex.printStackTrace();
      }
    } else {
      form.setCollectedSample(null);
    }

    if (result.hasErrors()) {
      m.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      message = "Please fix the errors noted above now!";
    }
    else {
      try {

        form.setIsDeleted(false);
        Product existingProduct = productRepository.updateProduct(form.getProduct());
        if (existingProduct == null) {
          m.put("hasErrors", true);
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          success = false;
          m.put("existingProduct", false);
          message = "Product does not already exist.";
        }
        else {
          m.put("hasErrors", false);
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

    m.put("editProductForm", form);
    m.put("success", success);
    m.put("message", message);
    m.put("productFields", utilController.getFormFieldsForForm("Product"));

    mv.addObject("model", m);

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

  @RequestMapping(value = "/discardProduct", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> discardProduct(
      @RequestParam("productId") Long productId) {

    boolean success = true;
    String errMsg = "";
    try {
      productRepository.discardProduct(productId);
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


  /**
   * Redirect to ISBT 128 Label Generator
   */
  @RequestMapping(value="/productLabel", method = RequestMethod.GET)
  public ModelAndView generateProductLabel(HttpServletRequest request,
      Model model,
      @RequestParam(value="productId", required=false) Long productId) {
    ModelAndView mv = new ModelAndView("productLabel");
    Map<String, Object> m = model.asMap();
    m.put("productId", productId);
    m.put("bloodGroup", productRepository.getBloodGroupForProduct(productId).toString());
    mv.addObject("model", m);
    return mv;
  }
}
