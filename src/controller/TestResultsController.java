package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;

import model.Collection;
import model.TestResult;
import model.TestResultBackingForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectionRepository;
import repository.DisplayNamesRepository;
import repository.ProductRepository;
import repository.RecordFieldsConfigRepository;
import repository.TestResultRepository;
import utils.ControllerUtil;
import viewmodel.TestResultViewModel;

@Controller
public class TestResultsController {
  @Autowired
  private CollectionRepository collectionRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private TestResultRepository testResultRepository;

  @Autowired
  private DisplayNamesRepository displayNamesRepository;

  @Autowired
  private RecordFieldsConfigRepository recordFieldsConfigRepository;

  @RequestMapping(value = "/findTestResultFormGenerator", method = RequestMethod.GET)
  public ModelAndView findTestResultFormInit(Model model) {

    TestResultBackingForm form = new TestResultBackingForm();
    model.addAttribute("findTestResultForm", form);

    ModelAndView mv = new ModelAndView("findTestResultForm");
    Map<String, Object> m = model.asMap();
    // to ensure custom field names are displayed in the form
    ControllerUtil.addTestResultDisplayNamesToModel(m, displayNamesRepository);
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping("/findTestResult")
  public ModelAndView findTestResult(
      @ModelAttribute("findTestResultForm") TestResultBackingForm form,
      BindingResult result, Model model) {

    List<TestResult> testResults = testResultRepository
        .findAnyTestResultMatching(form.getCollectionNumber(), form.getHiv(),
            form.getHbv(), form.getHcv(), form.getSyphilis(),
            form.getDateTestedFrom(), form.getDateTestedTo());

    ModelAndView modelAndView = new ModelAndView("testResultsTable");
    Map<String, Object> m = model.asMap();
    m.put("tableName", "findTestResultsTable");
    ControllerUtil.addTestResultDisplayNamesToModel(m, displayNamesRepository);
    ControllerUtil.addFieldsToDisplay("testResult", m,
        recordFieldsConfigRepository);
    m.put("allTestResults", getTestResultViewModels(testResults));

    modelAndView.addObject("model", m);
    return modelAndView;
  }

  @RequestMapping(value = "/editTestResultFormGenerator", method = RequestMethod.GET)
  public ModelAndView editTestResultFormGenerator(
      Model model,
      @RequestParam(value = "collectionNumber", required = false) String collectionNumber,
      @RequestParam(value = "isDialog", required = false) String isDialog) {

    TestResultBackingForm form = new TestResultBackingForm();
    Map<String, Object> m = model.asMap();
    m.put("isDialog", isDialog);

    if (collectionNumber != null) {
      form.setCollectionNumber(collectionNumber);
      TestResult testResult = testResultRepository
          .findTestResultByCollectionNumber(collectionNumber);
      if (testResult != null) {
        form = new TestResultBackingForm(testResult);
      } else
        form = new TestResultBackingForm();
      System.out.println("here1");
    }
    m.put("editTestResultForm", form);
    // to ensure custom field names are displayed in the form
    ControllerUtil.addTestResultDisplayNamesToModel(m, displayNamesRepository);
    ModelAndView mv = new ModelAndView("editTestResultForm");
    mv.addObject("model", m);
    return mv;
  }

  @RequestMapping(value = "/updateTestResult", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, ? extends Object> updateOrAddTestResult(
      @ModelAttribute("editTestResultForm") TestResultBackingForm form) {

    boolean success = true;
    String errMsg = "";
    try {
      TestResult testResult = form.getTestResult();
      testResultRepository.updateOrAddTestResult(testResult);
    } catch (EntityExistsException ex) {
      // TODO: Replace with logger
      System.err.println("Entity Already exists");
      System.err.println(ex.getMessage());
      success = false;
      errMsg = "Test Result Already Exists";
    } catch (Exception ex) {
      // TODO: Replace with logger
      System.err.println("Internal Exception");
      System.err.println(ex.getMessage());
      success = false;
      errMsg = "Internal Server Error";
    }

    Map<String, Object> m = new HashMap<String, Object>();
    m.put("success", success);
    m.put("errMsg", errMsg);
    return m;
  }

  private List<TestResultViewModel> getTestResultViewModels(
      List<TestResult> testResults) {
    if (testResults == null)
      return Arrays.asList(new TestResultViewModel[0]);
    List<TestResultViewModel> testResultViewModels = new ArrayList<TestResultViewModel>();
    for (TestResult testResult : testResults) {
      testResultViewModels.add(new TestResultViewModel(testResult));
    }
    return testResultViewModels;
  }

  @RequestMapping("/testResultsAdd")
  public ModelAndView getAddTestResultsPage(HttpServletRequest request) {

    ModelAndView modelAndView = new ModelAndView("testResultsAdd");
    Map<String, Object> model = new HashMap<String, Object>();

    ControllerUtil.addTestResultDisplayNamesToModel(model,
        displayNamesRepository);
    modelAndView.addObject("model", model);

    return modelAndView;
  }

  @RequestMapping("/addNewTestResults")
  public ModelAndView addNewTestResults(
      @RequestParam Map<String, String> params, HttpServletRequest request) {

    String collectionNumber = params.get("collectionNumber");
    Collection collection = collectionRepository
        .findCollectionByNumber(collectionNumber);
    ModelAndView modelAndView = new ModelAndView("testResultsAdd");
    Map<String, Object> model = new HashMap<String, Object>();
    if (collection == null) {
      TestResult testResult = new TestResult(collectionNumber, null,
          getDate(params.get("testResultDate")), params.get("hiv"),
          params.get("hbv"), params.get("hcv"), params.get("syphilis"),
          params.get("abo"), params.get("rhd"), Boolean.FALSE,
          params.get("comment"));
      model.put("collectionNotFound", true);
      model.put("collectionNumber", collectionNumber);
      model.put("hasTestResult", true);
      model.put("testResult", new TestResultViewModel(testResult));

    } else {
      TestResult testResult = new TestResult(collectionNumber,
          collection.getDateCollected(), getDate(params.get("testResultDate")),
          params.get("hiv"), params.get("hbv"), params.get("hcv"),
          params.get("syphilis"), params.get("abo"), params.get("rhd"),
          Boolean.FALSE, params.get("comment"));
      testResultRepository.saveTestResult(testResult);
      updateCollectionBloodType(testResult, collection);
      modelAndView = new ModelAndView("testResultsUpdate");
      model.put("testResult", new TestResultViewModel(testResult));
      model.put("hasTestResult", true);
      model.put("testResultAdded", true);
      if (allIndicatorsNegative(params)) {
        if (!productRepository.isProductCreated(collectionNumber)) {
          model.put("createNewProduct", true);
          model.put("collectionNumber", collectionNumber);
        }
      }

    }
    ControllerUtil.addTestResultDisplayNamesToModel(model,
        displayNamesRepository);

    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping("/updateExistingTestResults")
  public ModelAndView updateExistingTestResults(
      @RequestParam Map<String, String> params, HttpServletRequest request) {

    String collectionNumber = params.get("collectionNumber");
    Collection collection = collectionRepository
        .findCollectionByNumber(collectionNumber);
    ModelAndView modelAndView = new ModelAndView("testResultsUpdate");
    Map<String, Object> model = new HashMap<String, Object>();
    if (collection == null) {
      model.put("collectionNotFound", true);
      model.put("collectionNumber", collectionNumber);
      model.put("hasTestResult", true);
      model.put("testResultUpdated", false);
      TestResult existingTestResult = testResultRepository.find(params
          .get("existingTestResultId"));
      TestResult testResult = new TestResult(collectionNumber,
          existingTestResult.getDateCollected(),
          getDate(params.get("testResultDate")), params.get("hiv"),
          params.get("hbv"), params.get("hcv"), params.get("syphilis"),
          params.get("abo"), params.get("rhd"), Boolean.FALSE,
          params.get("comment"));
      existingTestResult.copy(testResult);
      model.put("testResult", new TestResultViewModel(existingTestResult));

    } else {
      TestResult testResult = new TestResult(collectionNumber,
          collection.getDateCollected(), getDate(params.get("testResultDate")),
          params.get("hiv"), params.get("hbv"), params.get("hcv"),
          params.get("syphilis"), params.get("abo"), params.get("rhd"),
          Boolean.FALSE, params.get("comment"));
      TestResult existingTestResult = testResultRepository.updateTestResult(
          testResult, getParam(params, "existingTestResultId"));
      updateCollectionBloodType(testResult, collection);
      model.put("testResult", new TestResultViewModel(existingTestResult));
      model.put("hasTestResult", true);
      model.put("testResultUpdated", true);
      if (allIndicatorsNegative(params)) {
        if (!productRepository.isProductCreated(collectionNumber)) {
          model.put("createNewProduct", true);
          model.put("collectionNumber", collectionNumber);
        }
      }
    }
    ControllerUtil.addTestResultDisplayNamesToModel(model,
        displayNamesRepository);

    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping("/deleteExistingTestResult")
  public ModelAndView deleteExistingTestResults(
      @RequestParam Map<String, String> params, HttpServletRequest request) {

    ModelAndView modelAndView = new ModelAndView("testResultsAdd");
    Map<String, Object> model = new HashMap<String, Object>();
    Long existingTestResultId = getParam(params, "existingTestResultId");
    testResultRepository.deleteTestResult(existingTestResultId);
    model.put("testResultDeleted", true);
    ControllerUtil.addTestResultDisplayNamesToModel(model,
        displayNamesRepository);

    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping("/testResultsView")
  public ModelAndView viewTestResults(@RequestParam Map<String, String> params,
      HttpServletRequest request) {

    ModelAndView modelAndView = new ModelAndView("testResultsTable");
    Map<String, Object> model = new HashMap<String, Object>();

    ControllerUtil.addTestResultDisplayNamesToModel(model,
        displayNamesRepository);
    modelAndView.addObject("model", model);

    return modelAndView;
  }

  @RequestMapping("/findAllTestResultsByCollection")
  public ModelAndView findAllTestResultsByCollection(
      @RequestParam Map<String, String> params, HttpServletRequest request) {

    String collectionNumber = params.get("collectionNumber");
    ModelAndView modelAndView = new ModelAndView("testResultsTable");
    Map<String, Object> model = new HashMap<String, Object>();
    List<TestResult> allTestResults = testResultRepository
        .getAllTestResults(collectionNumber);
    if (allTestResults.size() == 0) {
      model.put("noResultsFound", true);
      model.put("collectionNumber", collectionNumber);
    } else {
      List<TestResultViewModel> allTestResultViewModels = new ArrayList<TestResultViewModel>();
      for (TestResult testResult : allTestResults) {
        allTestResultViewModels.add(new TestResultViewModel(testResult));
      }
      model.put("allTestResults", allTestResultViewModels);
    }
    ControllerUtil.addTestResultDisplayNamesToModel(model,
        displayNamesRepository);

    modelAndView.addObject("model", model);
    return modelAndView;
  }

  @RequestMapping("/selectTestResult")
  public ModelAndView selectTestResultsByCollection(
      @RequestParam Map<String, String> params, HttpServletRequest request) {

    String selectedTestResultId = params.get("selectedTestResultId");
    TestResult testResult = testResultRepository.find(selectedTestResultId);
    ModelAndView modelAndView = new ModelAndView("testResultsUpdate");
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("testResult", new TestResultViewModel(testResult));
    model.put("hasTestResult", true);
    ControllerUtil.addTestResultDisplayNamesToModel(model,
        displayNamesRepository);

    modelAndView.addObject("model", model);
    return modelAndView;
  }

  private Long getParam(Map<String, String> params, String paramName) {
    String paramValue = params.get(paramName);
    return paramValue == null || paramValue.isEmpty() ? null : Long
        .parseLong(paramValue);
  }

  private boolean allIndicatorsNegative(Map<String, String> params) {
    if ("negative".equals(params.get("hiv"))
        && "negative".equals(params.get("hbv"))
        && "negative".equals(params.get("hcv"))
        && "negative".equals(params.get("syphilis"))) {
      return true;
    }
    return false;
  }

  private Date getDate(String dateParam) {
    DateFormat formatter;
    formatter = new SimpleDateFormat("MM/dd/yyyy");
    Date collectionDate = null;
    try {
      String collectionDateEntered = dateParam;
      if (collectionDateEntered.length() > 0) {
        collectionDate = (Date) formatter.parse(collectionDateEntered);
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return collectionDate;
  }

  private void updateCollectionBloodType(TestResult testResult,
      Collection collection) {
    List<TestResult> allTestResults = testResultRepository
        .getAllTestResults(collection.getCollectionNumber());
    TestResult latestTestResult = null;
    if (allTestResults.size() > 0) {
      latestTestResult = Collections.max(allTestResults,
          new Comparator<TestResult>() {
            public int compare(TestResult testResult, TestResult testResult1) {
              return testResult.getDateTested().compareTo(
                  testResult1.getDateTested());
            }
          });
    }

    Boolean bloodGroupChanged = Boolean.FALSE;
    String abo = "";
    String rhd = "";

    if (latestTestResult != null) {
      if (testResult.getDateTested() != null
          && latestTestResult.getDateTested().compareTo(
              testResult.getDateTested()) <= 0) {
        if (latestTestResult.getAbo() != testResult.getAbo()
            && testResult.getAbo() != "") {
          Collection updatedCollection = new Collection();
          updatedCollection.copy(collection);
          updatedCollection.setAbo(testResult.getAbo());
          collection = collectionRepository.updateCollection(updatedCollection,
              collection.getCollectionId());
          bloodGroupChanged = Boolean.TRUE;
          abo = testResult.getAbo();

        }
        if (latestTestResult.getRhd() != testResult.getRhd()
            && testResult.getRhd() != "") {
          Collection updatedCollection = new Collection();
          updatedCollection.copy(collection);
          updatedCollection.setRhd(testResult.getRhd());
          collection = collectionRepository.updateCollection(updatedCollection,
              collection.getCollectionId());
          bloodGroupChanged = Boolean.TRUE;
          rhd = testResult.getRhd();

        }
        if (bloodGroupChanged) {
          productRepository.updateProductBloodGroup(
              collection.getCollectionNumber(), abo, rhd);
        }
      }
    } else {
      Collection updatedCollection = new Collection();
      updatedCollection.copy(collection);
      updatedCollection.setAbo(testResult.getAbo());
      updatedCollection.setRhd(testResult.getRhd());
      collection = collectionRepository.updateCollection(updatedCollection,
          collection.getCollectionId());
      bloodGroupChanged = Boolean.TRUE;
      abo = testResult.getAbo();
    }
  }
}
