package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;

import model.Product;
import model.ProductBackingForm;

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

import repository.DisplayNamesRepository;
import repository.ProductRepository;
import repository.RecordFieldsConfigRepository;
import utils.ControllerUtil;
import utils.LoggerUtil;
import viewmodel.ProductViewModel;

@Controller
public class ProductsController {
  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private DisplayNamesRepository displayNamesRepository;

  @Autowired
  private RecordFieldsConfigRepository recordFieldsConfigRepository;

  @RequestMapping(value = "/findProductFormGenerator", method = RequestMethod.GET)
  public ModelAndView findProductFormInit(Model model) {

    ProductBackingForm form = new ProductBackingForm();
    model.addAttribute("findProductForm", form);

    ModelAndView mv = new ModelAndView("findProductForm");
    Map<String, Object> m = model.asMap();
    // to ensure custom field names are displayed in the form
    ControllerUtil.addProductDisplayNamesToModel(m, displayNamesRepository);
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping("/findProduct")
  public ModelAndView findProduct(
      @ModelAttribute("findProductForm") ProductBackingForm form,
      BindingResult result, Model model) {

    List<Product> products = productRepository.findAnyProductMatching(
        form.getProductNumber(), form.getCollectionNumber(), form.getTypes(),
        form.getAvailability());

    ModelAndView modelAndView = new ModelAndView("productsTable");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findProductsTable");
    ControllerUtil.addProductDisplayNamesToModel(m, displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("product", m,
        recordFieldsConfigRepository);
    m.put("allProducts", getProductViewModels(products));

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  @RequestMapping("/findAvailableProducts")
  public ModelAndView findAvailableProducts(Model model) {

    List<Product> products = productRepository.findAnyProductMatching(
        "", "", Arrays.asList(""),
        Arrays.asList("available"));

    ModelAndView modelAndView = new ModelAndView("productsTable");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findAvailableProductsTable");
    m.put("showAddProductButton", false);
    ControllerUtil.addProductDisplayNamesToModel(m, displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("product", m,
        recordFieldsConfigRepository);
    m.put("allProducts", getProductViewModels(products));

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  @RequestMapping(value = "/editProductFormGenerator", method = RequestMethod.GET)
  public ModelAndView editProductFormGenerator(
      Model model,
      @RequestParam(value = "productNumber", required = false) String productNumber,
      @RequestParam(value = "isDialog", required = false) String isDialog) {

    ProductBackingForm form = new ProductBackingForm();
    Map<String, Object> m = model.asMap();
    m.put("isDialog", isDialog);
    m.put("selectedType", "wholeBlood");
    if (productNumber != null) {
      form.setCollectionNumber(productNumber);
      Product product = productRepository
          .findProductByProductNumber(productNumber);
      if (product != null) {
        form = new ProductBackingForm(product);
        m.put("selectedType", product.getType());
      } else
        form = new ProductBackingForm();
    }
    m.put("editProductForm", form);
    // to ensure custom field names are displayed in the form
    ControllerUtil.addProductDisplayNamesToModel(m, displayNamesRepository);
    ModelAndView mv = new ModelAndView("editProductForm");
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> updateOrAddProduct(
      @ModelAttribute("editProductForm") ProductBackingForm form) {

    boolean success = true;
    String errMsg = "";
    try {
      Product product = form.getProduct();
      productRepository.updateOrAddProduct(product);
    } catch (EntityExistsException ex) {
      // TODO: Replace with logger
      System.err.println("Entity Already exists");
      System.err.println(ex.getMessage());
      success = false;
      errMsg = "Product Already Exists";
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

  private List<ProductViewModel> getProductViewModels(List<Product> products) {
    if (products == null)
      return Arrays.asList(new ProductViewModel[0]);
    List<ProductViewModel> productViewModels = new ArrayList<ProductViewModel>();
    for (Product product : products) {
      productViewModels.add(new ProductViewModel(product));
    }
    return productViewModels;
  }

  @RequestMapping("/productsLandingPage")
  public ModelAndView getProductsLandingPage(
      HttpServletRequest httpServletRequest) {
    LoggerUtil.logUrl(httpServletRequest);

    return new ModelAndView("productsLandingPage");
  }

  @RequestMapping("/products")
  public ModelAndView getProducts(HttpServletRequest httpServletRequest) {
    LoggerUtil.logUrl(httpServletRequest);

    ModelAndView modelAndView = new ModelAndView("products");
    Map<String, Object> model = new HashMap<String, Object>();

    ControllerUtil.addProductDisplayNamesToModel(model, displayNamesRepository);
    modelAndView.addObject("model", model);

    return modelAndView;
  }

}
