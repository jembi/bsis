package controller.bloodtesting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestType;
import model.collectedsample.CollectedSample;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectedSampleRepository;
import repository.GenericConfigRepository;
import repository.bloodtesting.BloodTestingRepository;
import viewmodel.BloodTestViewModel;
import viewmodel.BloodTestingRuleResult;
import viewmodel.BloodTestingRuleViewModel;
import viewmodel.CollectedSampleViewModel;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import controller.UtilController;

@Controller
public class BloodTypingController {

  @Autowired
  private UtilController utilController;

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  public BloodTypingController() {
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value="/bloodTypingWorksheetGenerator", method=RequestMethod.GET)
  public ModelAndView getBloodTypingWorksheet(HttpServletRequest request) {
    ModelAndView mv = new ModelAndView("bloodtesting/bloodTypingWorksheetForm");

    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "bloodtyping.plate.step1");
    mv.addObject("tips", tips);
    mv.addObject("plate", bloodTestingRepository.getPlate("bloodtyping"));
    mv.addObject("refreshUrl", "bloodTypingWorksheetGenerator.html");

    return mv;
  }

  @RequestMapping(value="/addCollectionsToBloodTypingPlate")
  public ModelAndView addCollectionsToBloodTypingPlate(HttpServletRequest request,
          HttpServletResponse response,
          @RequestParam(value="collectionNumbers[]") List<String> collectionNumbers) {

    // here the key of the map corresponds to the index of the
    // collection number in the parameter collectionNumbers
    // corresponding to the collected sample in the value of the map
    List<CollectedSample> collections = collectedSampleRepository.verifyCollectionNumbers(collectionNumbers);

    int numErrors = 0;
    int numValid = 0;
    for (CollectedSample c : collections) {
      if (c == null)
        numErrors++;
      else
        numValid++;
    }

    ModelAndView mv = new ModelAndView();
    mv.addObject("collections", collections);
    mv.addObject("collectionNumbers", StringUtils.join(collectionNumbers,","));
    mv.addObject("plate", bloodTestingRepository.getPlate("bloodtyping"));

    Map<String, Object> tips = new HashMap<String, Object>();
    if (numErrors > 0 || numValid == 0) {
      mv.addObject("success", false);
      mv.setViewName("bloodtesting/bloodTypingWorksheetForm");
      mv.addObject("refreshUrl", "bloodTypingWorksheetGenerator.html");
      utilController.addTipsToModel(tips, "bloodtyping.plate.step1");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      mv.addObject("errorMessage", "Please fix the errors noted. Some collections do not exist.");
    } else {

      // this is a map with id of the collected sample as the key
      Map<Long, CollectedSample> collectionMap = new HashMap<Long, CollectedSample>();
      for (CollectedSample c : collections) {
        collectionMap.put(c.getId(), c);
      }
      mv.addObject("collectionMap", collectionMap);

      // no errors but the user should be warned if there are any collections for which testing partly done
      Map<Long, BloodTestingRuleResult> collectionsWithBloodTypingTests = 
          collectedSampleRepository.filterCollectionsWithBloodTypingResults(collections);
      mv.addObject("collectionsWithBloodTypingTests", collectionsWithBloodTypingTests);

      Map<String, BloodTest> bloodTypingTestsMap = new HashMap<String, BloodTest>();
      for (BloodTest bloodTypingTest : bloodTestingRepository.getBloodTypingTests()) {
        bloodTypingTestsMap.put(bloodTypingTest.getId().toString(), bloodTypingTest);
      }
      mv.addObject("bloodTypingTests", bloodTypingTestsMap);

      mv.addObject("success", true);
      mv.setViewName("bloodtesting/bloodTypingWells");
      mv.addObject("refreshUrl", getUrl(request));
      mv.addObject("changeCollectionsUrl", "bloodTypingWorksheetGenerator.html");
      utilController.addTipsToModel(tips, "bloodtyping.plate.step2");
      mv.addObject("bloodTestsOnPlate", getBloodTestsOnPlate());
      mv.addObject("bloodTypingConfig", genericConfigRepository.getConfigProperties("bloodTyping"));
    }

    mv.addObject("tips", tips);
    return mv;
  }

  public List<BloodTestViewModel> getBloodTestsOnPlate() {
    List<BloodTestViewModel> tests = new ArrayList<BloodTestViewModel>();
    for (BloodTest rawBloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)) {
      tests.add(new BloodTestViewModel(rawBloodTest));
    }
    return tests;
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value="/saveBloodTypingTests", method=RequestMethod.POST)
  public ModelAndView saveBloodTypingTests(HttpServletRequest request,
      HttpServletResponse response, @RequestParam(value="bloodTypingTests") String bloodTypingTests,
      @RequestParam(value="collectionNumbers[]") List<String> collectionNumbers,
      @RequestParam(value="refreshUrl") String refreshUrl,
      @RequestParam(value="saveUninterpretableResults") boolean saveUninterpretableResults) {

    ModelAndView mv = new ModelAndView();
    List<CollectedSample> collections = collectedSampleRepository.verifyCollectionNumbers(collectionNumbers);
    mv.addObject("collectionNumbers", StringUtils.join(collectionNumbers, ","));
    mv.addObject("collections", collections);

    Map<Long, Map<Long, String>> bloodTypingTestResults = parseBloodTestResults(bloodTypingTests);
    Map<Long, Map<Long, String>> errorMap = null;
    mv.addObject("bloodTypingTestResults", bloodTypingTestResults);
    boolean success = true;
    Map<String, Object> results = null;
    try {
      results = bloodTestingRepository.saveBloodTestingResults(bloodTypingTestResults, saveUninterpretableResults);
      if (results != null)
        errorMap = (Map<Long, Map<Long, String>>) results.get("errors");
    } catch (Exception ex) {
      ex.printStackTrace();
      success = false;
    }
    if (errorMap != null && !errorMap.isEmpty())
      success = false;
    Map<String, Object> tips = new HashMap<String, Object>();

    System.out.println(errorMap);

    if (success) {
      List<BloodTest> allBloodTypingTests = bloodTestingRepository.getBloodTypingTests();
      Map<String, BloodTest> allBloodTypingTestsMap = new HashMap<String, BloodTest>();
      for (BloodTest allBloodTypingTest : allBloodTypingTests) {
        allBloodTypingTestsMap.put(allBloodTypingTest.getId().toString(), allBloodTypingTest);
      }
      mv.addObject("allBloodTypingTests", allBloodTypingTestsMap);
      mv.addObject("collectionFields", utilController.getFormFieldsForForm("collectedSample"));
      mv.addObject("collectionsByCollectionId", results.get("collections"));

      List<String> collectionIds = new ArrayList<String>();
      for (CollectedSample collection : collections) {
        collectionIds.add(collection.getId().toString());
      }
      mv.addObject("collectionIds", StringUtils.join(collectionIds, ","));

      mv.addObject("bloodTypingOutput", results.get("bloodTestingResults"));
      mv.addObject("success", success);
      mv.setViewName("bloodtesting/bloodTypingWellsSuccess");
    }
    else {
      // errors found
      mv.addObject("plate", bloodTestingRepository.getPlate("bloodtyping"));
      mv.addObject("errorMap", errorMap);
      mv.addObject("bloodTypingTests", bloodTypingTests);
      mv.addObject("success", success);
      mv.addObject("refreshUrl", refreshUrl);
      mv.addObject("changeCollectionsUrl", "bloodTypingWorksheetGenerator.html");
      mv.addObject("collectionsWithUninterpretableResults", results.get("collectionsWithUninterpretableResults"));
      mv.addObject("collectionsByCollectionId", results.get("collections"));

      utilController.addTipsToModel(tips, "bloodtyping.plate.step2");
      mv.addObject("bloodTestsOnPlate", getBloodTestsOnPlate());
      mv.addObject("bloodTypingConfig", genericConfigRepository.getConfigProperties("bloodTyping"));
      mv.addObject("errorMessage", "There were errors adding tests. Please verify the results in the wells highlighted in red.");      
      mv.setViewName("bloodtesting/bloodTypingWellsError");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    return mv;
  }

  private Map<Long, Map<Long, String>> parseBloodTestResults(
      String bloodTestingResults) {
    ObjectMapper mapper = new ObjectMapper();
    System.out.println(bloodTestingResults);
    Map<Long, Map<Long, String>> bloodTestResultsMap = new HashMap<Long, Map<Long,String>>();
    try {
      @SuppressWarnings("unchecked")
      Map<String, Map<String, String>> generatedMap = mapper.readValue(bloodTestingResults, HashMap.class);
      for (String collectionId : generatedMap.keySet()) {
        Map<Long, String> testResults = new HashMap<Long, String>();
        bloodTestResultsMap.put(Long.parseLong(collectionId), testResults);
        for (String testId : generatedMap.get(collectionId).keySet()) {
          testResults.put(Long.parseLong(testId), generatedMap.get(collectionId).get(testId));
        }
      } 
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
    System.out.println(bloodTestResultsMap);
    return bloodTestResultsMap;
  }

  @RequestMapping(value="/getBloodTypingStatusForCollections", method=RequestMethod.GET)
  public ModelAndView getBloodTypingStatusForCollections(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam(value="collectionIds") String collectionIdsParam
                        ) {
    ModelAndView mv = new ModelAndView();
    String[] collectionIds = collectionIdsParam.split(",");
    Map<String, Object> results = bloodTestingRepository.getAllTestsStatusForCollections(Arrays.asList(collectionIds));
    List<BloodTest> allBloodTypingTests = bloodTestingRepository.getBloodTypingTests();
    Map<String, BloodTest> allBloodTypingTestsMap = new HashMap<String, BloodTest>();
    for (BloodTest allBloodTypingTest : allBloodTypingTests) {
      allBloodTypingTestsMap.put(allBloodTypingTest.getId().toString(), allBloodTypingTest);
    }
    mv.addObject("allBloodTypingTests", allBloodTypingTestsMap);
    mv.addObject("collectionFields", utilController.getFormFieldsForForm("collectedSample"));
    // depend on the getBloodTypingTestStatus() method to return collections, blood typing output as
    // a linked hashmap so that iteration is done in the same order as the collections in the well
    mv.addObject("collections", results.get("collections"));
    mv.addObject("bloodTypingOutput", results.get("bloodTestingResults"));
    mv.addObject("success", true);
    mv.setViewName("bloodtesting/bloodTypingResultsForCollections");
    return mv;
  }

  @RequestMapping(value="/showBloodTypingResultsForCollection", method=RequestMethod.GET)
  public ModelAndView showBloodTypingResultsForCollection(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(value="collectionId") String collectionId) {
    ModelAndView mv = new ModelAndView();
    collectionId = collectionId.trim();
    Long collectedSampleId = Long.parseLong(collectionId);
    CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(collectedSampleId);
    BloodTestingRuleResult ruleResult = bloodTestingRepository.getAllTestsStatusForCollection(collectedSampleId);
    mv.addObject("collection", new CollectedSampleViewModel(collectedSample));
    mv.addObject("collectionId", collectedSample.getId());
    mv.addObject("bloodTypingOutputForCollection", ruleResult);
    mv.addObject("collectionFields", utilController.getFormFieldsForForm("collectedSample"));

    List<BloodTest> bloodTypingTests = bloodTestingRepository.getBloodTypingTests();
    Map<String, BloodTest> bloodTypingTestsMap = new LinkedHashMap<String, BloodTest>();
    for (BloodTest bloodTypingTest : bloodTypingTests) {
      bloodTypingTestsMap.put(bloodTypingTest.getId().toString(), bloodTypingTest);
    }
    mv.addObject("allBloodTypingTests", bloodTypingTestsMap);

    List<BloodTest> allBloodTypingTests = bloodTestingRepository.getBloodTypingTests();
    Map<String, BloodTest> allBloodTypingTestsMap = new TreeMap<String, BloodTest>();
    for (BloodTest bloodTypingTest : allBloodTypingTests) {
      allBloodTypingTestsMap.put(bloodTypingTest.getId().toString(), bloodTypingTest);
    }
    mv.addObject("allBloodTypingTests", allBloodTypingTestsMap);

    mv.setViewName("bloodtesting/bloodTypingSummaryCollection");

    mv.addObject("refreshUrl", getUrl(request));

    return mv;
  }

  @RequestMapping(value="/showCollectionSummaryForTesting", method=RequestMethod.GET)
  public ModelAndView showCollectionSummaryForTesting(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(value="collectionId") String collectionId) {
    ModelAndView mv = new ModelAndView();
    collectionId = collectionId.trim();
    Long collectedSampleId = Long.parseLong(collectionId);
    CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(collectedSampleId);
    mv.addObject("collection", new CollectedSampleViewModel(collectedSample));
    mv.addObject("collectionId", collectedSample.getId());
    mv.addObject("collectionFields", utilController.getFormFieldsForForm("collectedSample"));

    mv.setViewName("bloodtesting/collectionSummaryForBloodTesting");

    mv.addObject("refreshUrl", getUrl(request));

    return mv;
  }

  @RequestMapping(value="/saveAdditionalBloodTypingTests", method=RequestMethod.POST)
  public @ResponseBody Map<String, Object> saveAdditionalBloodTypingTests(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(value="collectionId") String collectionId,
      @RequestParam(value="saveTestsData") String saveTestsDataStr,
      @RequestParam(value="saveUninterpretableResults") boolean saveUninterpretableResults) {

    Map<String, Object> m = new HashMap<String, Object>();

    try {
      Map<Long, Map<Long, String>> bloodTypingTestResultsMap = new HashMap<Long, Map<Long,String>>();
      Map<Long, String> saveTestsDataWithLong = new HashMap<Long, String>();
      ObjectMapper mapper = new ObjectMapper();
      Map<String, String> saveTestsData = mapper.readValue(saveTestsDataStr, HashMap.class);
      for (String testIdStr : saveTestsData.keySet()) {
        saveTestsDataWithLong.put(Long.parseLong(testIdStr), saveTestsData.get(testIdStr));
      }
      bloodTypingTestResultsMap.put(Long.parseLong(collectionId), saveTestsDataWithLong);
      Map<String, Object> results = bloodTestingRepository.saveBloodTestingResults(bloodTypingTestResultsMap, saveUninterpretableResults);
      Map<Long, Object> errorMap = (Map<Long, Object>) results.get("errors");
      System.out.println(errorMap);
      if (errorMap != null && !errorMap.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Map<Long, String> errorsForCollection = (Map<Long, String>) errorMap.get(Long.parseLong(collectionId));
        if (errorsForCollection != null && errorsForCollection.size() == 1 && errorsForCollection.containsKey((long)-1))
          m.put("uninterpretable", true);
        else
          m.put("invalidResults", true);
      }

    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    return m;
  }

  @RequestMapping(value="bloodTypingRuleSummary", method=RequestMethod.GET)
  public ModelAndView getBloodTypingRuleSummary(HttpServletRequest request,
      @RequestParam(value="bloodTypingRuleId") Integer ruleId,
      @RequestParam(value="ruleNumber") String ruleNumber) {

    ModelAndView mv = new ModelAndView ("admin/bloodTypingRuleSummary");
    BloodTestingRuleViewModel bloodTypingRule;
    bloodTypingRule = new BloodTestingRuleViewModel(bloodTestingRepository.getBloodTestingRuleById(ruleId));
    mv.addObject("bloodTypingRule", bloodTypingRule);
    mv.addObject("ruleNumber", ruleNumber);
    mv.addObject("refreshUrl", getUrl(request));
    List<BloodTest> bloodTypingTests = bloodTestingRepository.getBloodTypingTests();
    mv.addObject("bloodTypingTests", bloodTypingTests);
    mv.addObject("bloodTypingTestsMap", getBloodTypingTestsAsMap(bloodTypingTests));
    return mv;
  }

  private Map<Integer, BloodTest> getBloodTypingTestsAsMap(List<BloodTest> bloodTypingTests) {
    Map<Integer, BloodTest> bloodTypingTestsMap = new HashMap<Integer, BloodTest>();
    for (BloodTest bt : bloodTypingTests) {
      bloodTypingTestsMap.put(bt.getId(), bt);
    }
    return bloodTypingTestsMap;
  }
}
