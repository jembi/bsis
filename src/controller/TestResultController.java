package controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CustomDateFormatter;
import model.admin.ConfigPropertyConstants;
import model.collectedsample.CollectedSample;
import model.testresults.FindTestResultBackingForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectedSampleRepository;
import repository.GenericConfigRepository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class TestResultController {

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository; 

  @Autowired
  private UtilController utilController;

  public TestResultController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
//    binder.setValidator(new TestResultBackingFormValidator(binder.getValidator(), utilController));
  }

  @RequestMapping(value = "/findTestResultFormGenerator", method = RequestMethod.GET)
  public ModelAndView findTestResultFormGenerator(HttpServletRequest request) {

    FindTestResultBackingForm form = new FindTestResultBackingForm();

    ModelAndView mv = new ModelAndView("findTestResultForm");
    mv.addObject("findTestResultForm", form);

    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "testResults.find");
    mv.addObject("tips", tips);

    addEditSelectorOptions(mv.getModelMap());
    // to ensure custom field names are displayed in the form
    mv.addObject("testResultFields", utilController.getFormFieldsForForm("testResult"));
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

  private void addEditSelectorOptions(Map<String, Object> m) {
//    m.put("bloodTests", bloodTestRepository.getAllBloodTests());
  }

  @RequestMapping(value = "/addAllTestResultsFormGenerator", method = RequestMethod.GET, 
      headers = "X-Requested-With=XMLHttpRequest")
  public ModelAndView addAllTestResultsFormGenerator(HttpServletRequest request,
      Model model) {

    System.out.println("");
    ModelAndView mv = new ModelAndView("addAllTestResultsForm");
    Map<String, Object> m = model.asMap();
    m.put("refreshUrl", getUrl(request));
    addEditSelectorOptions(m);
    // to ensure custom field names are displayed in the form
    m.put("testResultFields", utilController.getFormFieldsForForm("TestResult"));
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/addAllTestResults", method = RequestMethod.POST)
  public ModelAndView addAllTestResults(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam Map<String, String> params) {

    ModelAndView mv = new ModelAndView("addAllTestResultsForm");
    Boolean hasErrors = false; 

    Map<String, Object> m = new HashMap<String, Object>();

    System.out.println(params);
    // IMPORTANT: Validation code just checks if the ID exists.
    // We still need to store the collected sample as part of the product.
    String collectionNumber = params.get("collectionNumber");
    CollectedSample collectedSample = null;
    if (collectionNumber == null) {
      hasErrors = true;
      m.put("collectionNumberError", "Collection does not exist");
    }
    else {
      try {
        collectedSample = collectedSampleRepository.findCollectedSampleByCollectionNumber(collectionNumber);
        if (collectedSample == null) {
          hasErrors = true;
          m.put("collectionNumberError", "Collection does not exist");
        }
      } catch (NoResultException ex) {
        ex.printStackTrace();
        hasErrors = true;
        m.put("collectionNumberError", "Collection does not exist");
      }
    }

    String testedOnStr = params.get("testedOn");
    Date testedOn = null;
    try {
        testedOn = CustomDateFormatter.getDateTimeFromString(testedOnStr);
    } catch (ParseException e) {
      hasErrors = true;
      m.put("testedOnError", CustomDateFormatter.getDateErrorMessage());
      e.printStackTrace();
    }

    m.put("refreshUrl", "addAllTestResultsFormGenerator.html");
    addEditSelectorOptions(m);
    // to ensure custom field names are displayed in the form
    m.put("testResultFields", utilController.getFormFieldsForForm("TestResult"));
    mv.addObject("model", m);

    if (hasErrors) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return mv;
    }

    String notes = params.get("notes");

    for (Entry<String, String> param : params.entrySet()) {
      String name = param.getKey();
      if (!name.startsWith("Test"))
        continue;

      String testName = name.substring(4);
      String testResult = param.getValue();

//      BloodTest bloodTest = bloodTestRepository.findBloodTestByName(testName);
//      TestResult t = new TestResult();
//      t.setCollectedSample(collectedSample);
//      t.setTestedOn(testedOn);
//      t.setBloodTest(bloodTest);
//      t.setResult(testResult);
//      t.setIsDeleted(false);
//      t.setNotes(notes);
//      try {
//        testResultRepository.addTestResult(t);
//      } catch (Exception ex) {
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//      }
    }

    return mv;
  }

  @RequestMapping("/findTestResult")
  public ModelAndView findTestResult(HttpServletRequest request,
      @ModelAttribute("findTestResultForm") FindTestResultBackingForm form) {

    ModelAndView mv = new ModelAndView("testresults/testResultsForCollection");

    String collectionNumber = form.getCollectionNumber();
    CollectedSample c = null;
    c = collectedSampleRepository.findCollectedSampleByCollectionNumber(collectionNumber);
    if (c == null) {
      mv.addObject("collectionFound", false);
    }
    else {
      mv.addObject("collectionFound", true);
      mv.addObject("collectionId", c.getId());
    }
    return mv;
  }
  @RequestMapping(value="/ttiWorksheet", method=RequestMethod.GET)
  public ModelAndView getTtiWorkSheetFormGenerator(HttpServletRequest request,
      HttpServletResponse response) {
    ModelAndView mv = new ModelAndView("ttiWorksheetForm");
    return mv;
  }

}
