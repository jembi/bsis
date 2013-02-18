package controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.crossmatch.CrossmatchTestBackingForm;
import model.crossmatch.CrossmatchTestBackingFormValidator;
import model.request.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.RequestRepository;

@Controller
public class CrossmatchTestsController {

  @Autowired
  private RequestRepository requestRepository;

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
  }

  @RequestMapping(value="/updateCrossmatchTestsFormGenerator", method=RequestMethod.GET)
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
}
