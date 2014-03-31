package controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.collectedsample.CollectedSample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectedSampleRepository;
import backingform.FindTestResultBackingForm;

@Controller
public class TestResultController {

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private UtilController utilController;

  public TestResultController() {
  }

  @RequestMapping(value = "/findTestResultFormGenerator", method = RequestMethod.GET)
  public ModelAndView findTestResultFormGenerator(HttpServletRequest request) {

    FindTestResultBackingForm form = new FindTestResultBackingForm();

    ModelAndView mv = new ModelAndView("testresults/findTestResultForm");
    mv.addObject("findTestResultForm", form);

    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "testResults.find");
    mv.addObject("tips", tips);

    // to ensure custom field names are displayed in the form
    mv.addObject("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    mv.addObject("refreshUrl", getUrl(request));
    return mv;
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping("/findTestResult")
  public ModelAndView findTestResult(HttpServletRequest request,
      @ModelAttribute("findTestResultForm") FindTestResultBackingForm form) {

    ModelAndView mv = new ModelAndView("testresults/testResultsForCollection");

    String collectionNumber = form.getCollectionNumber();
    CollectedSample c = null;
    c = collectedSampleRepository.findCollectedSampleByCollectionNumber(collectionNumber,false);
    if (c == null) {
      mv.addObject("collectionFound", false);
    }
    else {
      mv.addObject("collectionFound", true);
      mv.addObject("collectionId", c.getId());
    }
    return mv;
  }
}
