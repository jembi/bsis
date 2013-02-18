package controller;

import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import model.crossmatch.CrossmatchTest;
import model.crossmatch.CrossmatchTestBackingForm;
import model.crossmatch.CrossmatchTestBackingFormValidator;
import model.product.Product;
import model.request.Request;

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

import repository.CrossmatchTestRepository;
import repository.CrossmatchTypeRepository;
import repository.ProductRepository;
import repository.RequestRepository;

@Controller
public class CrossmatchTestsController {

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CrossmatchTestRepository crossmatchRepository;

  @Autowired
  private CrossmatchTypeRepository crossmatchTypeRepository;

  @Autowired
  private UtilController utilController;

  public CrossmatchTestsController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new CrossmatchTestBackingFormValidator(binder.getValidator(), utilController));
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  private void addEditSelectorOptions(Map<String, Object> m) {
    m.put("crossmatchTypes", crossmatchTypeRepository.getAllCrossmatchTypes());
  }

  @RequestMapping(value="/editCrossmatchTestFormGenerator", method=RequestMethod.GET)
  public ModelAndView updateCrossmatchTestsFormGenerator(HttpServletRequest request,
      Model model,
      @RequestParam(value="requestId", required=true) String requestId) {

    Map<String, Object> m = model.asMap();
    addEditSelectorOptions(m);
    m.put("refreshUrl", getUrl(request));
    m.put("crossmatchForRequest", true);
    // to ensure custom field names are displayed in the form
    Map<String, Object> formFields = utilController.getFormFieldsForForm("crossmatchTest");
    m.put("crossmatchTestFields", formFields);

    CrossmatchTestBackingForm form = new CrossmatchTestBackingForm();
    m.put("editCrossmatchTestForm", form);

    Request productRequest = requestRepository.findRequestById(requestId);
    form.setForRequest(productRequest);

    ModelAndView mv = new ModelAndView("editCrossmatchTestForm");
    mv.addObject("model", m);
    return mv;

  }

  @RequestMapping(value = "/addCrossmatchTestForRequest", method = RequestMethod.POST)
  public ModelAndView
        addDonor(HttpServletRequest request,
                 HttpServletResponse response,
                 @ModelAttribute("editCrossmatchTestForm") @Valid CrossmatchTestBackingForm form,
                 BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editCrossmatchTestForm");
    boolean success = false;
    String message = "";

    Map<String, Object> m = model.asMap();
    // IMPORTANT: Validation code just checks if the ID exists.
    // We still need to store the collected sample as part of the product.
    String productNumber = form.getProductNumber();
    if (productNumber != null && !productNumber.isEmpty()) {
      try {
        Product product = productRepository.findProductByProductNumber(productNumber);
        form.setTestedProduct(product);
      } catch (NoResultException ex) {
        form.setTestedProduct(null);
        ex.printStackTrace();
      }
    } else {
      form.setTestedProduct(null);
    }

    if (result.hasErrors()) {
      m.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);      
      success = false;
      message = "Please fix the errors noted above.";
    } else {
      try {
        CrossmatchTest crossmatchTest = form.getCrossmatchTest();
        crossmatchTest.setIsDeleted(false);
        crossmatchRepository.addCrossmatchTest(crossmatchTest);
        m.put("hasErrors", false);
        success = true;
        message = "Crossmatch test successfully added";
        form = new CrossmatchTestBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
        message = "Crossmatch Test already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
    }

    m.put("editCrossmatchTestForm", form);
    m.put("existingCrossmatchTest", false);
    m.put("refreshUrl", "editCrossmatchTestFormGenerator.html");
    m.put("success", success);
    m.put("message", message);
    m.put("crossmatchTestFields", utilController.getFormFieldsForForm("CrossmatchTest"));

    mv.addObject("model", m);
    return mv;
  }
}
