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

  @RequestMapping(value = "/worksheetForTestResultsFormGenerator", method = RequestMethod.GET)
  public ModelAndView findWorksheetForTestResultsFormGenerator(HttpServletRequest request, Model model) {

    ModelAndView mv = new ModelAndView("findWorksheetForTestResults");
    Map<String, Object> m = model.asMap();
//    m.put("bloodTests", bloodTestRepository.getAllBloodTests());
    m.put("testResultFields", utilController.getFormFieldsForForm("testResult"));
    m.put("refreshUrl", getUrl(request));
    utilController.addTipsToModel(m, "testResults.worksheet");
    List<String> propertyOwners = Arrays.asList(ConfigPropertyConstants.COLLECTIONS_WORKSHEET);
    m.put("worksheetConfig", genericConfigRepository.getConfigProperties(propertyOwners));
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value="/editTestResultsForWorksheet", method=RequestMethod.GET)
  public ModelAndView editTestResultsForWorksheet(HttpServletRequest request, Model model,
      @RequestParam(value="worksheetBatchId") String worksheetBatchId) {

    ModelAndView mv = new ModelAndView("worksheetForTestResults");
    Map<String, Object> m = model.asMap();
    m.put("worksheetFound", true);
    List<String> propertyOwners = Arrays.asList(ConfigPropertyConstants.COLLECTIONS_WORKSHEET);
    m.put("worksheetConfig", genericConfigRepository.getConfigProperties(propertyOwners));
    m.put("worksheetBatchId", worksheetBatchId);
    m.put("nextPageUrl", getNextPageUrl(request));
    mv.addObject("model", m);

    return mv;
  }

  @RequestMapping(value="/editTestResultsForWorksheetPagination", method=RequestMethod.GET)
  public @ResponseBody Map<String, Object> editTestResultsForWorksheetPagination(HttpServletRequest request, Model model,
      @RequestParam(value="worksheetBatchId") String worksheetBatchId) {

    Map<String, Object> pagingParams = utilController.parsePagingParameters(request);
    List<Object> results = collectedSampleRepository.findCollectionsInWorksheet(worksheetBatchId, pagingParams);

    List<CollectedSample> collectedSamples = (List<CollectedSample>) results.get(0);
    Long totalRecords = (Long) results.get(1);
    return generateDatatablesMap(collectedSamples, totalRecords);
  }

  /**
   * Datatables on the client side expects a json response for rendering data from the server
   * in jquery datatables. Remember of columns is important and should match the column headings
   * in collectionsTable.jsp.
   */
  private Map<String, Object> generateDatatablesMap(List<CollectedSample> collectedSamples, Long totalRecords) {

    List<String> propertyOwners = Arrays.asList(ConfigPropertyConstants.COLLECTIONS_WORKSHEET);
    Map<String, String> properties = genericConfigRepository.getConfigProperties(propertyOwners);

    Map<String, Object> resultsMap = new HashMap<String, Object>();
    ArrayList<Object> resultList = new ArrayList<Object>();

    for (CollectedSample collectedSample : collectedSamples) {

      List<Object> row = new ArrayList<Object>();
      // id goes as the first column to identify the collection uniquely
      row.add(collectedSample.getId());

      // second column is collection number
      if (properties.containsKey("collectionNumber") && properties.get("collectionNumber").equals("true")) {
          row.add(collectedSample.getCollectionNumber());
      }

//      Map<String, TestResult> recentTestResults = testResultRepository.getRecentTestResultsForCollection(collectedSample.getId());
//      // now add results for existing tests related to this worksheet
//      for (BloodTest bt : bloodTests) {
//        String testName = bt.getName();
//        if (properties.containsKey(testName) && properties.get(testName).equals("true")) {
//            if (recentTestResults.containsKey(testName))
//              row.add(recentTestResults.get(testName).getResult());
//            else
//              row.add("");
//        }
//      }

      resultList.add(row);
    }
    resultsMap.put("aaData", resultList);
    resultsMap.put("iTotalRecords", totalRecords);
    resultsMap.put("iTotalDisplayRecords", totalRecords);
    return resultsMap;
  }

  private String getNextPageUrl(HttpServletRequest request) {
    String reqUrl = request.getRequestURL().toString().replaceFirst("editTestResultsForWorksheet.html", "editTestResultsForWorksheetPagination.html");
    String queryString = request.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value="saveWorksheetTestResults", method=RequestMethod.POST)
  public @ResponseBody Map<String, Object>
          saveWorksheetTestResults(HttpServletRequest request,
              HttpServletResponse response,
              @RequestParam(value="params") String requestParams,
              @RequestParam(value="worksheetBatchId") String worksheetBatchId) {

    Map<String, Object> result = new HashMap<String, Object>();

    System.out.println(requestParams);
    ObjectMapper mapper = new ObjectMapper();
    try {
      Map<String, Map<String, String>> testResultChanges = mapper.readValue(requestParams, HashMap.class);
      System.out.println(testResultChanges);
//      testResultRepository.saveTestResultsToWorksheet(worksheetBatchId, testResultChanges);
    } catch (JsonParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    return result;
  }

}
