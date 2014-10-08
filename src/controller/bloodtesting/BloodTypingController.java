package controller.bloodtesting;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.UtilController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestType;
import model.collectedsample.CollectedSample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repository.CollectedSampleRepository;
import repository.GenericConfigRepository;
import repository.bloodtesting.BloodTestingRepository;
import utils.PermissionConstants;
import viewmodel.BloodTestViewModel;
import viewmodel.BloodTestingRuleResult;
import viewmodel.BloodTestingRuleViewModel;
import viewmodel.CollectedSampleViewModel;

@RestController
@RequestMapping("bloodtypes")
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

    /**
     * issue $209[Adapt BSIS to expos rest Services] Reason _ No worksheet
     * concept
     *
  @RequestMapping(value="/bloodTypingWorksheetGenerator", method=RequestMethod.GET)
  public Map<String, Object> getBloodTypingWorksheet(HttpServletRequest request) {
    Map<String, Object> map = new Map<String, Object>("bloodtesting/bloodTypingWorksheetForm");

    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "bloodtyping.plate.step1");
    map.put("tips", tips);
    map.put("plate", bloodTestingRepository.getPlate("bloodtyping"));
    map.put("refreshUrl", "bloodTypingWorksheetGenerator.html");

    return map;
  }
  * */

  @RequestMapping(value="/addCollectionsToBloodTypingPlate", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_BLOOD_TYPING_OUTCOME+"')")
  public Map<String, Object> addCollectionsToBloodTypingPlate(HttpServletRequest request,
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

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("collections", collections);
    map.put("collectionNumbers", StringUtils.join(collectionNumbers,","));
    map.put("plate", bloodTestingRepository.getPlate("bloodtyping"));

    Map<String, Object> tips = new HashMap<String, Object>();
    if (numErrors > 0 || numValid == 0) {
      map.put("success", false);
      map.put("refreshUrl", "bloodTypingWorksheetGenerator.html");
      utilController.addTipsToModel(tips, "bloodtyping.plate.step1");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      map.put("errorMessage", "Please fix the errors noted. Some collections do not exist.");
    } else {

      // this is a map with id of the collected sample as the key
      Map<Long, CollectedSample> collectionMap = new HashMap<Long, CollectedSample>();
      for (CollectedSample c : collections) {
        collectionMap.put(c.getId(), c);
      }
      map.put("collectionMap", collectionMap);

      // no errors but the user should be warned if there are any collections for which testing partly done
      Map<Long, BloodTestingRuleResult> collectionsWithBloodTypingTests = 
          collectedSampleRepository.filterCollectionsWithBloodTypingResults(collections);
      map.put("collectionsWithBloodTypingTests", collectionsWithBloodTypingTests);

      Map<String, BloodTest> bloodTypingTestsMap = new HashMap<String, BloodTest>();
      for (BloodTest bloodTypingTest : bloodTestingRepository.getBloodTypingTests()) {
        bloodTypingTestsMap.put(bloodTypingTest.getId().toString(), bloodTypingTest);
      }
      map.put("bloodTypingTests", bloodTypingTestsMap);

      map.put("success", true);
      map.put("refreshUrl", getUrl(request));
      map.put("changeCollectionsUrl", "bloodTypingWorksheetGenerator.html");
      utilController.addTipsToModel(tips, "bloodtyping.plate.step2");
      map.put("bloodTestsOnPlate", getBloodTestsOnPlate());
      map.put("bloodTypingConfig", genericConfigRepository.getConfigProperties("bloodTyping"));
    }

    map.put("tips", tips);
    return map;
  }

  public List<BloodTestViewModel> getBloodTestsOnPlate() {
    List<BloodTestViewModel> tests = new ArrayList<BloodTestViewModel>();
    for (BloodTest rawBloodTest : bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)) {
      tests.add(new BloodTestViewModel(rawBloodTest));
    }
    return tests;
  }

  @SuppressWarnings("unchecked")
  
  @RequestMapping(value = "test", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_BLOOD_TYPING_OUTCOME+"')")
  public ResponseEntity<Map<String, Object>> saveBloodTypingTests(
      @RequestParam(value="bloodTypingTests") String bloodTypingTests,
      @RequestParam(value="collectionNumbers[]") List<String> collectionNumbers,
      @RequestParam(value="refreshUrl") String refreshUrl,
      @RequestParam(value="saveUninterpretableResults") boolean saveUninterpretableResults) {

    HttpStatus httpStatus = HttpStatus.CREATED;
    Map<String, Object> map = new HashMap<String, Object>();
    List<CollectedSample> collections = collectedSampleRepository.verifyCollectionNumbers(collectionNumbers);
    map.put("collectionNumbers", StringUtils.join(collectionNumbers, ","));
    map.put("collections", collections);

    Map<Long, Map<Long, String>> bloodTypingTestResults = parseBloodTestResults(bloodTypingTests);
    Map<Long, Map<Long, String>> errorMap = null;
    map.put("bloodTypingTestResults", bloodTypingTestResults);
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


    if (success) {
      List<BloodTest> allBloodTypingTests = bloodTestingRepository.getBloodTypingTests();
      Map<String, BloodTest> allBloodTypingTestsMap = new HashMap<String, BloodTest>();
      for (BloodTest allBloodTypingTest : allBloodTypingTests) {
        allBloodTypingTestsMap.put(allBloodTypingTest.getId().toString(), allBloodTypingTest);
      }
      map.put("allBloodTypingTests", allBloodTypingTestsMap);
      map.put("collectionFields", utilController.getFormFieldsForForm("collectedSample"));
      map.put("collectionsByCollectionId", results.get("collections"));

      List<String> collectionIds = new ArrayList<String>();
      for (CollectedSample collection : collections) {
        collectionIds.add(collection.getId().toString());
      }
      map.put("collectionIds", StringUtils.join(collectionIds, ","));

      map.put("bloodTypingOutput", results.get("bloodTestingResults"));
      map.put("success", success);
    }
    else {
      // errors found
      map.put("plate", bloodTestingRepository.getPlate("bloodtyping"));
      map.put("errorMap", errorMap);
      map.put("bloodTypingTests", bloodTypingTests);
      map.put("success", success);
      map.put("refreshUrl", refreshUrl);
      map.put("changeCollectionsUrl", "bloodTypingWorksheetGenerator.html");
      map.put("collectionsWithUninterpretableResults", results.get("collectionsWithUninterpretableResults"));
      map.put("collectionsByCollectionId", results.get("collections"));

      utilController.addTipsToModel(tips, "bloodtyping.plate.step2");
      map.put("bloodTestsOnPlate", getBloodTestsOnPlate());
      map.put("bloodTypingConfig", genericConfigRepository.getConfigProperties("bloodTyping"));
      map.put("errorMessage", "There were errors adding tests. Please verify the results in the wells highlighted in red.");      
      httpStatus = HttpStatus.BAD_REQUEST;
    }

    return new ResponseEntity<Map<String, Object>>(map, httpStatus);
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
          if (StringUtils.isBlank(testId))
            continue;
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

  @RequestMapping(value="/test/status/donation/{id}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_BLOOD_TYPING_OUTCOME+"')")
  public Map<String, Object> getBloodTypingStatusForCollections(
                        @PathVariable String id
                        ) {
    Map<String, Object> map = new HashMap<String, Object>();
    String[] collectionIds = id.split(",");
    Map<String, Object> results = bloodTestingRepository.getAllTestsStatusForCollections(Arrays.asList(collectionIds));
    List<BloodTest> allBloodTypingTests = bloodTestingRepository.getBloodTypingTests();
    Map<String, BloodTest> allBloodTypingTestsMap = new HashMap<String, BloodTest>();
    for (BloodTest allBloodTypingTest : allBloodTypingTests) {
      allBloodTypingTestsMap.put(allBloodTypingTest.getId().toString(), allBloodTypingTest);
    }
    map.put("allBloodTypingTests", allBloodTypingTestsMap);
    map.put("collectionFields", utilController.getFormFieldsForForm("collectedSample"));
    // depend on the getBloodTypingTestStatus() method to return collections, blood typing output as
    // a linked hashmap so that iteration is done in the same order as the collections in the well
    map.put("collections", results.get("collections"));
    map.put("bloodTypingOutput", results.get("bloodTestingResults"));
    map.put("success", true);
    return map;
  }

  @RequestMapping(value="test/results/donation/{id}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_BLOOD_TYPING_OUTCOME+"')")
  public Map<String, Object> showBloodTypingResultsForCollection(
      @PathVariable Long id) {
      
    Map<String, Object> map = new HashMap<String, Object>();
    CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(id);
    BloodTestingRuleResult ruleResult = bloodTestingRepository.getAllTestsStatusForCollection(id);
    map.put("collection", new CollectedSampleViewModel(collectedSample));
    map.put("collectionId", collectedSample.getId());
    map.put("bloodTypingOutputForCollection", ruleResult);
    map.put("collectionFields", utilController.getFormFieldsForForm("collectedSample"));

    List<BloodTest> bloodTypingTests = bloodTestingRepository.getBloodTypingTests();
    Map<String, BloodTest> bloodTypingTestsMap = new LinkedHashMap<String, BloodTest>();
    for (BloodTest bloodTypingTest : bloodTypingTests) {
      bloodTypingTestsMap.put(bloodTypingTest.getId().toString(), bloodTypingTest);
    }
    map.put("allBloodTypingTests", bloodTypingTestsMap);

    List<BloodTest> allBloodTypingTests = bloodTestingRepository.getBloodTypingTests();
    Map<String, BloodTest> allBloodTypingTestsMap = new TreeMap<String, BloodTest>();
    for (BloodTest bloodTypingTest : allBloodTypingTests) {
      allBloodTypingTestsMap.put(bloodTypingTest.getId().toString(), bloodTypingTest);
    }
    map.put("allBloodTypingTests", allBloodTypingTestsMap);
    return map;
  }

  @RequestMapping(value="/test/collection/{id}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_OUTCOME+"')")
  public Map<String, Object> showCollectionSummaryForTesting(
      @PathVariable Long id) {
    Map<String, Object> map = new HashMap<String, Object>();
    CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(id);
    map.put("collection", new CollectedSampleViewModel(collectedSample));
    map.put("collectionId", collectedSample.getId());
    map.put("collectionFields", utilController.getFormFieldsForForm("collectedSample"));
    return map;
  }

  @RequestMapping(value="/test/addiional", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_BLOOD_TYPING_OUTCOME+"')")
  public ResponseEntity<Map<String, Object>> saveAdditionalBloodTypingTests(
      @RequestParam(value="collectionId") String collectionId,
      @RequestParam(value="saveTestsData") String saveTestsDataStr,
      @RequestParam(value="saveUninterpretableResults") boolean saveUninterpretableResults) {

    Map<String, Object> m = new HashMap<String, Object>();
    HttpStatus httpStatus = HttpStatus.CREATED;
    
      Map<Long, Map<Long, String>> bloodTypingTestResultsMap = new HashMap<Long, Map<Long,String>>();
      Map<Long, String> saveTestsDataWithLong = new HashMap<Long, String>();
      ObjectMapper mapper = new ObjectMapper();
      @SuppressWarnings("unchecked")
      Map<String, String> saveTestsData = null;
      try {
          saveTestsData = mapper.readValue(saveTestsDataStr, HashMap.class);
      } catch (IOException ex) {
          ex.printStackTrace();
          httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
          return new ResponseEntity<Map<String, Object>>(m, httpStatus);
      }
      for (String testIdStr : saveTestsData.keySet()) {
        saveTestsDataWithLong.put(Long.parseLong(testIdStr), saveTestsData.get(testIdStr));
      }
      bloodTypingTestResultsMap.put(Long.parseLong(collectionId), saveTestsDataWithLong);
      Map<String, Object> results = bloodTestingRepository.saveBloodTestingResults(bloodTypingTestResultsMap, saveUninterpretableResults);
      @SuppressWarnings("unchecked")
      Map<Long, Object> errorMap = (Map<Long, Object>) results.get("errors");
      System.out.println(errorMap);
      if (errorMap != null && !errorMap.isEmpty()) {
        httpStatus = HttpStatus.BAD_REQUEST;
        @SuppressWarnings("unchecked")
        Map<Long, String> errorsForCollection = (Map<Long, String>) errorMap.get(Long.parseLong(collectionId));
        if (errorsForCollection != null && errorsForCollection.size() == 1 && errorsForCollection.containsKey((long)-1))
          m.put("uninterpretable", true);
        else
          m.put("invalidResults", true);
      }

    return new ResponseEntity<Map<String, Object>>(m, httpStatus);
  }

  @RequestMapping(value="rule/{id}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_BLOOD_TYPING_OUTCOME+"')")
  public Map<String, Object> getBloodTypingRuleSummary(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> map = new HashMap<String, Object>();
    BloodTestingRuleViewModel bloodTypingRule;
    bloodTypingRule = new BloodTestingRuleViewModel(bloodTestingRepository.getBloodTestingRuleById(id));
    map.put("bloodTypingRule", bloodTypingRule);
    map.put("refreshUrl", getUrl(request));
    List<BloodTest> bloodTypingTests = bloodTestingRepository.getBloodTypingTests();
    map.put("bloodTypingTests", bloodTypingTests);
    map.put("bloodTypingTestsMap", getBloodTypingTestsAsMap(bloodTypingTests));
    return map;
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value="rule", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public Map<String, Object> saveNewBloodTypingRule(HttpServletRequest request,
      HttpServletResponse response, @RequestParam("newBloodTypingRule") String newBloodTypingRuleAsJsonStr) {
    Map<String, Object> m = new HashMap<String, Object>();
    ObjectMapper mapper = new ObjectMapper();
    boolean success = false;
    try {
      Map<String, Object> newBloodTypingRuleAsMap;
      newBloodTypingRuleAsMap = mapper.readValue(newBloodTypingRuleAsJsonStr, HashMap.class);
      bloodTestingRepository.saveNewBloodTypingRule(newBloodTypingRuleAsMap);
      success = true;
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
    if (!success)
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return m;
  }
  
  @RequestMapping(value = "/rule/{id}", method=RequestMethod.DELETE)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public Map<String, Object> deleteBloodTypingRule(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> m = new HashMap<String, Object>();
    bloodTestingRepository.deleteBloodTestingRule(id);
    return m;
  }

  private Map<Integer, BloodTest> getBloodTypingTestsAsMap(List<BloodTest> bloodTypingTests) {
    Map<Integer, BloodTest> bloodTypingTestsMap = new HashMap<Integer, BloodTest>();
    for (BloodTest bt : bloodTypingTests) {
      bloodTypingTestsMap.put(bt.getId(), bt);
    }
    return bloodTypingTestsMap;
  }
}
