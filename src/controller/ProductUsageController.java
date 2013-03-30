package controller;

import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

import repository.ProductTypeRepository;
import repository.UsageRepository;
import viewmodel.ProductUsageViewModel;

@Controller
public class ProductUsageController {

	@Autowired
	private UsageRepository usageRepository;

	@Autowired
	private ProductTypeRepository productTypeRepository;
	
	@Autowired
	private UtilController utilController;

	public ProductUsageController() {
	}
	
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new ProductUsageBackingFormValidator(binder.getValidator(), utilController));
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
    utilController.addTipsToModel(model.asMap(), "usage.addusage");
    mv.addObject("model", m);
    System.out.println(mv.getViewName());
    return mv;
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
}
