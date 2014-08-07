package controller;

import backingform.ProductUsageBackingForm;
import backingform.validator.UsageBackingFormValidator;
import com.wordnik.swagger.annotations.Api;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import model.product.Product;
import model.request.Request;
import model.usage.ProductUsage;
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
import repository.ProductTypeRepository;
import repository.RequestRepository;
import repository.UsageRepository;
import utils.PermissionConstants;
import viewmodel.ProductUsageViewModel;
import viewmodel.RequestViewModel;

@Controller
@RequestMapping
@Api(value = "Account operations")
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
  @PreAuthorize("hasRole('"+PermissionConstants.ISSUE_COMPONENT+"')")
  public Map<String, Object> addUsageFormGenerator(HttpServletRequest request) {

    ProductUsageBackingForm form = new ProductUsageBackingForm();

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("requestUrl", getUrl(request));
    map.put("firstTimeRender", true);
    map.put("addUsageForm", form);
    map.put("refreshUrl", getUrl(request));
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("usage");
    // to ensure custom field names are displayed in the form
    map.put("usageFields", formFields);
    return map;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("productTypes", productTypeRepository.getAllProductTypes());
  }

  @RequestMapping(value = "/addUsage", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ISSUE_COMPONENT+"')")
  public Map<String, Object> addUsage(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("addUsageForm") @Valid ProductUsageBackingForm form,
      BindingResult result, Model model) {

    Map<String, Object> map = new HashMap<String, Object>();
    boolean success = false;

    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("usage");
    map.put("usageFields", formFields);

    ProductUsage savedUsage = null;
    if (result.hasErrors()) {
      map.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        ProductUsage productUsage = form.getUsage();
        productUsage.setIsDeleted(false);
        savedUsage = usageRepository.addUsage(productUsage);
        map.put("hasErrors", false);
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
      map.put("usageId", savedUsage.getId());
      map.put("usage",  new ProductUsageViewModel(savedUsage));
      map.put("addAnotherUsageUrl", "addUsageFormGenerator.html");
    } else {
      map.put("errorMessage", "Error creating usage. Please fix the errors noted below.");
      map.put("firstTimeRender", false);
      map.put("addUsageForm", form);
      map.put("refreshUrl", "addUsageFormGenerator.html");
    }

    map.put("success", success);
    return map;
  }

  @RequestMapping(value = "/addUsageByRequestFormGenerator", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ISSUE_COMPONENT+"')")
  public Map<String, Object> addUsageByRequestFormGenerator(HttpServletRequest request) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("requestUrl", getUrl(request));
    map.put("firstTimeRender", true);
    map.put("refreshUrl", getUrl(request));
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("usage");
    // to ensure custom field names are displayed in the form
    map.put("usageFields", formFields);
    return map;
  }

  @RequestMapping(value="/findIssuedProductsForRequest", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ISSUE_COMPONENT+"')")
  public @ResponseBody Map<String, Object> findIssuedProductsForRequest(HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(value="requestNumber") String requestNumber) {

    Map<String, Object> map = new HashMap<String, Object>();

    Request req = requestRepository.findRequest(requestNumber);
    boolean success = true;
    if (req == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
      map.put("errorMessage", "Request not found");
    } else {
      map.put("request", new RequestViewModel(req));
      map.put("issuedProducts", requestRepository.getIssuedProductsForRequest(req.getId()));
    }

    map.put("productFields", utilController.getFormFieldsForForm("product"));
    map.put("success", success);
    return map;
  }

  @RequestMapping(value = "/addUsageForProductFormGenerator", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ISSUE_COMPONENT+"')")
  public Map<String, Object> addUsageForProductFormGenerator(HttpServletRequest request,
      @RequestParam(value="productId") Long productId) {

    ProductUsageBackingForm form = new ProductUsageBackingForm();

    Product product = productRepository.findProductById(productId);
    form.setProduct(product);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("requestUrl", getUrl(request));
    map.put("firstTimeRender", true);
    map.put("addUsageForProductForm", form);
    map.put("productType", product.getProductType().getProductTypeNameShort());
    map.put("refreshUrl", getUrl(request));
    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("usage");
    // to ensure custom field names are displayed in the form
    map.put("usageFields", formFields);
    return map;
  }

  @RequestMapping(value = "/addUsageForProduct", method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ISSUE_COMPONENT+"')")
  public @ResponseBody Map<String, Object> addUsageForProduct(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("addUsageForProductForm") @Valid ProductUsageBackingForm form,
      BindingResult result, Model model) {

    Map<String, Object> map = new HashMap<String, Object>();
    boolean success = false;

    addEditSelectorOptions(map);
    Map<String, Map<String, Object>> formFields = utilController.getFormFieldsForForm("usage");
    map.put("usageFields", formFields);

    ProductUsage savedUsage = null;
    if (result.hasErrors()) {
      map.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      success = false;
    } else {
      try {
        ProductUsage productUsage = form.getUsage();
        productUsage.setIsDeleted(false);
        savedUsage = usageRepository.addUsage(productUsage);
        map.put("hasErrors", false);
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
      map.put("usageId", savedUsage.getId());
      map.put("usage",  new ProductUsageViewModel(savedUsage));
    } else {
      map.put("errorMessage", "Error creating usage. Please fix the errors noted below.");
      map.put("firstTimeRender", false);
      map.put("addUsageForm", form);
      map.put("refreshUrl", "addUsageForProductFormGenerator.html");
    }

    map.put("success", success);
    return map;
  }

}
