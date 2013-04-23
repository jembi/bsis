package controller;

import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import model.product.Product;
import model.request.Request;
import model.usage.ProductUsage;
import model.usage.ProductUsageBackingForm;
import model.usage.UsageBackingFormValidator;

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
import org.springframework.web.servlet.ModelAndView;

import repository.ProductRepository;
import repository.ProductTypeRepository;
import repository.RequestRepository;
import repository.UsageRepository;
import viewmodel.ProductUsageViewModel;
import viewmodel.RequestViewModel;

@Controller
public class UsageController {

	@Autowired
	private UsageRepository usageRepository;

	@Autowired
	private ProductTypeRepository productTypeRepository;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private UtilController utilController;

	public UsageController() {
	}
	
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new UsageBackingFormValidator(binder.getValidator(), utilController));
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "/addUsageFormGenerator", method = RequestMethod.GET)
  public ModelAndView addUsageFormGenerator(HttpServletRequest request) {

    ProductUsageBackingForm form = new ProductUsageBackingForm();

    ModelAndView mv = new ModelAndView("usage/addUsageForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("addUsageForm", form);
    mv.addObject("refreshUrl", getUrl(request));
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> formFields = utilController.getFormFieldsForForm("usage");
    // to ensure custom field names are displayed in the form
    mv.addObject("usageFields", formFields);
    return mv;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("productTypes", productTypeRepository.getAllProductTypes());
  }

  @RequestMapping(value = "/addUsage", method = RequestMethod.POST)
  public ModelAndView addUsage(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("addUsageForm") @Valid ProductUsageBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView();
    boolean success = false;

    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> formFields = utilController.getFormFieldsForForm("usage");
    mv.addObject("usageFields", formFields);

    ProductUsage savedUsage = null;
    if (result.hasErrors()) {
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        ProductUsage productUsage = form.getUsage();
        productUsage.setIsDeleted(false);
        savedUsage = usageRepository.addUsage(productUsage);
        mv.addObject("hasErrors", false);
        success = true;
        form = new ProductUsageBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
      }
    }

    if (success) {
      mv.addObject("usageId", savedUsage.getId());
      mv.addObject("usage",  new ProductUsageViewModel(savedUsage));
      mv.addObject("addAnotherUsageUrl", "addUsageFormGenerator.html");
      mv.setViewName("usage/addUsageSuccess");
    } else {
      mv.addObject("errorMessage", "Error creating usage. Please fix the errors noted below.");
      mv.addObject("firstTimeRender", false);
      mv.addObject("addUsageForm", form);
      mv.addObject("refreshUrl", "addUsageFormGenerator.html");
      mv.setViewName("usage/addUsageError");
    }

    mv.addObject("success", success);
    return mv;
  }

  @RequestMapping(value = "/addUsageByRequestFormGenerator", method=RequestMethod.GET)
  public ModelAndView addUsageByRequestFormGenerator(HttpServletRequest request) {

    ModelAndView mv = new ModelAndView("usage/addUsageByRequestForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("refreshUrl", getUrl(request));
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> formFields = utilController.getFormFieldsForForm("usage");
    // to ensure custom field names are displayed in the form
    mv.addObject("usageFields", formFields);
    return mv;
  }

  @RequestMapping(value="/findIssuedProductsForRequest", method=RequestMethod.GET)
  public ModelAndView findIssuedProductsForRequest(HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(value="requestNumber") String requestNumber) {

    ModelAndView mv = new ModelAndView();

    Request req = requestRepository.findRequest(requestNumber);
    boolean success = true;
    if (req == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      mv.addObject("errorMessage", "Request not found");
    } else {
      mv.addObject("request", new RequestViewModel(req));
      mv.addObject("issuedProducts", requestRepository.getIssuedProductsForRequest(req.getId()));
    }

    mv.addObject("productFields", utilController.getFormFieldsForForm("product"));
    mv.addObject("success", success);
    mv.setViewName("usage/addUsageForIssuedProducts");
    return mv;
  }

  @RequestMapping(value = "/addUsageForProductFormGenerator", method = RequestMethod.GET)
  public ModelAndView addUsageForProductFormGenerator(HttpServletRequest request,
      @RequestParam(value="productId") Long productId) {

    ProductUsageBackingForm form = new ProductUsageBackingForm();

    Product product = productRepository.findProductById(productId);
    form.setCollectionNumber(product.getCollectionNumber());
    form.setProductType(product.getProductType().getId().toString());

    ModelAndView mv = new ModelAndView("usage/addUsageForProductForm");
    mv.addObject("requestUrl", getUrl(request));
    mv.addObject("firstTimeRender", true);
    mv.addObject("addUsageForProductForm", form);
    mv.addObject("productType", product.getProductType().getProductTypeNameShort());
    mv.addObject("refreshUrl", getUrl(request));
    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> formFields = utilController.getFormFieldsForForm("usage");
    // to ensure custom field names are displayed in the form
    mv.addObject("usageFields", formFields);
    return mv;
  }

  @RequestMapping(value = "/addUsageForProduct", method = RequestMethod.POST)
  public ModelAndView addUsageForProduct(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("addUsageForProductForm") @Valid ProductUsageBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView();
    boolean success = false;

    addEditSelectorOptions(mv.getModelMap());
    Map<String, Object> formFields = utilController.getFormFieldsForForm("usage");
    mv.addObject("usageFields", formFields);

    ProductUsage savedUsage = null;
    if (result.hasErrors()) {
      mv.addObject("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        ProductUsage productUsage = form.getUsage();
        productUsage.setIsDeleted(false);
        savedUsage = usageRepository.addUsage(productUsage);
        mv.addObject("hasErrors", false);
        success = true;
        form = new ProductUsageBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
      }
    }

    if (success) {
      mv.addObject("usageId", savedUsage.getId());
      mv.addObject("usage",  new ProductUsageViewModel(savedUsage));
      mv.setViewName("usage/addUsageForProductSuccess");
    } else {
      mv.addObject("errorMessage", "Error creating usage. Please fix the errors noted below.");
      mv.addObject("firstTimeRender", false);
      mv.addObject("addUsageForm", form);
      mv.addObject("refreshUrl", "addUsageForProductFormGenerator.html");
      mv.setViewName("usage/addUsageForProductError");
    }

    mv.addObject("success", success);
    return mv;
  }

}
