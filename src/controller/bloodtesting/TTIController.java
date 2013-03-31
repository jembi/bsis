package controller.bloodtesting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestType;
import model.collectedsample.CollectedSample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectedSampleRepository;
import repository.GenericConfigRepository;
import repository.bloodtesting.BloodTestingRepository;
import viewmodel.BloodTestViewModel;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import controller.UtilController;

@Controller
public class TTIController {

  @Autowired
  private UtilController utilController;

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  public TTIController() {
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value="/ttiFormGenerator", method=RequestMethod.GET)
  public ModelAndView getBloodTypingWorksheet(HttpServletRequest request) {
    ModelAndView mv = new ModelAndView("bloodtesting/addTTIForm");
    mv.addObject("refreshUrl", "ttiFormGenerator.html");
    mv.addObject("ttiFormFields", utilController.getFormFieldsForForm("TTIForm"));
    mv.addObject("firstTimeRender", true);

    List<BloodTestViewModel> ttiTests = getBasicTTITests();
    mv.addObject("allTTITests", ttiTests);

    return mv;
  }

  public List<BloodTestViewModel> getBasicTTITests() {
    List<BloodTestViewModel> tests = new ArrayList<BloodTestViewModel>();
    for (BloodTest rawBloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI)) {
      tests.add(new BloodTestViewModel(rawBloodTest));
    }
    return tests;
  }

  @RequestMapping(value="/saveTTITests", method=RequestMethod.POST)
  public ModelAndView saveTTITests(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("collectionNumber") String collectionNumber,
            @RequestParam("ttiInput") String ttiInput) {
    ModelAndView mv = new ModelAndView();

    boolean success = true;
    String errorMessage = "";
    Map<Long, Map<Long, String>> errorMap = null;
    Map<String, Object> fieldErrors = new HashMap<String, Object>();
    CollectedSample collectedSample = null;
    try {
       collectedSample = collectedSampleRepository.findCollectedSampleByCollectionNumber(collectionNumber);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      success = false;
    }

    if (collectedSample == null) {
      fieldErrors.put("collectionNumber", "Collection Number does not exist");
      success = false;
    }

    Map<String, Object> results = null;

    if (success) {
      Map<Long, Map<Long, String>> bloodTestResultsMap = new HashMap<Long, Map<Long, String>>();
      bloodTestResultsMap.put(collectedSample.getId(), ttiInputToMap(ttiInput));
      results = bloodTestingRepository.saveBloodTestingResults(bloodTestResultsMap);
      if (results != null)
        errorMap = (Map<Long, Map<Long, String>>) results.get("errors");
      success = true;
    }

    if (errorMap != null && !errorMap.isEmpty()) {
      success = false;
    }
    if (results == null) {
      success = false;
    }

    if (success) {
      List<BloodTest> allTTITests = bloodTestingRepository.getTTITests();
      Map<String, BloodTest> allTTITestsMap = new HashMap<String, BloodTest>();
      for (BloodTest ttiTest : allTTITests) {
        allTTITestsMap.put(ttiTest.getId().toString(), ttiTest);
      }
      mv.addObject("allTTITests", allTTITestsMap);
      mv.addObject("collectionFields", utilController.getFormFieldsForForm("collectedSample"));
      mv.addObject("collections", results.get("collections"));

      mv.addObject("bloodTypingOutput", results.get("bloodTestingResults"));
      mv.addObject("success", success);
      mv.setViewName("bloodtesting/addTTIFormSuccess");
    } else {
      // errors found
      mv.addObject("errorMap", errorMap);
      mv.addObject("success", success);
      mv.addObject("errorMessage", "There were errors adding tests. Please verify the values of all tests.");      
      mv.setViewName("bloodtesting/addTTIFormError");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    mv.addObject("addAnotherTTIUrl", "ttiFormGenerator.html");
    mv.addObject("refreshUrl", "ttiFormGenerator.html");
    mv.addObject("success", success);
    mv.addObject("errorMessage", errorMessage);
    return mv;
  }

  private Map<Long, String> ttiInputToMap(String ttiInput) {
    ObjectMapper mapper = new ObjectMapper();
    Map<Long, String> ttiInputMap = new HashMap<Long, String>();
    Map<String, String> resultsForCollection = null;
    try {
      resultsForCollection = mapper.readValue(ttiInput, HashMap.class);
    } catch (JsonParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    if (resultsForCollection != null) {
      for (String testIdStr : resultsForCollection.keySet()) {
        Long testId = Long.parseLong(testIdStr);
        ttiInputMap.put(testId, resultsForCollection.get(testIdStr));
      }
    }
    return ttiInputMap;
  }
}
