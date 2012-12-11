package controller;

import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import model.product.Product;
import model.usage.ProductUsage;
import model.usage.ProductUsageBackingForm;
import model.usage.ProductUsageBackingFormValidator;

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
import repository.UsageRepository;

@Controller
public class UsageController {

	@Autowired
	private UsageRepository usageRepository;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UtilController utilController;

	public UsageController() {
	}
	
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new ProductUsageBackingFormValidator(binder.getValidator()));
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "/editUsageFormGenerator", method = RequestMethod.GET)
  public ModelAndView editUsageFormGenerator(HttpServletRequest request,
      Model model,
      @RequestParam(value="usageId", required=false) Long usageId) {

    ProductUsageBackingForm form = new ProductUsageBackingForm();

    ModelAndView mv = new ModelAndView("editUsageForm");
    Map<String, Object> m = model.asMap();
    m.put("refreshUrl", getUrl(request));
    m.put("existingUsage", false);
    if (usageId != null) {
      form.setId(usageId);
      ProductUsage usage = usageRepository.findUsageById(usageId);
      if (usage != null) {
        form = new ProductUsageBackingForm(usage);
        m.put("existingUsage", true);
      }
      else {
        form = new ProductUsageBackingForm();
      }
    }
    m.put("editUsageForm", form);
    m.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    m.put("usageFields", utilController.getFormFieldsForForm("Usage"));
    mv.addObject("model", m);
    System.out.println(mv.getViewName());
    return mv;
  }

  @RequestMapping(value = "/addUsage", method = RequestMethod.POST)
  public ModelAndView addUsage(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("editUsageForm") @Valid ProductUsageBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editUsageForm");
    boolean success = false;
    String message = "";
    Map<String, Object> m = model.asMap();

    // IMPORTANT: Validation code just checks if the ID exists.
    // We still need to store the product as part of the Usage
    String productNumber = form.getProductNumber();
    if (productNumber != null && !productNumber.isEmpty()) {
      try {
        Product product = productRepository.findSingleProductByProductNumber(productNumber);
        form.setProduct(product);
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
        ProductUsage usage = form.getUsage();
        usage.setIsDeleted(false);
        usageRepository.addUsage(usage);
        m.put("hasErrors", false);
        success = true;
        message = "Usage Successfully Added";
        form = new ProductUsageBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
        message = "Usage Already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
    }

    m.put("editUsageForm", form);
    m.put("existingUsage", false);
    m.put("success", success);
    m.put("message", message);
    m.put("refreshUrl", "editUsageFormGenerator.html");
    m.put("usageFields", utilController.getFormFieldsForForm("usage"));

    mv.addObject("model", m);
    return mv;
  }
}
