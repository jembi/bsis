package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    String searchBy = form.getSearchBy();
    String dateExpiresFrom = form.getDateExpiresFrom();
    String dateExpiresTo = form.getDateExpiresTo();

    if (searchBy.equals("productNumber")) {
      products = productRepository.findProductByProductNumber(
                                          form.getProductNumber(), form.getAvailable(), form.getQuarantined(),
                                          dateExpiresFrom, dateExpiresTo);
    } else if (searchBy.equals("collectionNumber")) {
      products = productRepository.findProductByCollectionNumber(
          form.getCollectionNumber(), form.getAvailable(), form.getQuarantined(),
          dateExpiresFrom, dateExpiresTo);
    } else if (searchBy.equals("productType")) {
      products = productRepository.findProductByProductTypes(
          form.getProductTypes(), form.getAvailable(), form.getQuarantined(),
          dateExpiresFrom, dateExpiresTo);
    }

    ModelAndView modelAndView = new ModelAndView("productsTable");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findProductsTable");
    m.put("productFields", utilController.getFormFieldsForForm("product"));
    m.put("allProducts", getProductViewModels(products));
    m.put("refreshUrl", getUrl(request));
    addEditSelectorOptions(m);

    modelAndView.addObject("model", m);
    return modelAndView;
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
        ex.printStackTrace();
      }
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
        product.setIsAvailable(true);
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
        ex.printStackTrace();
      }
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
