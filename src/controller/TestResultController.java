package controller;

import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import model.collectedsample.CollectedSample;
import model.testresults.TestResult;
import model.testresults.TestResultBackingForm;
import model.testresults.TestResultBackingFormValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.BloodBagTypeRepository;
import repository.BloodTestRepository;
import repository.CollectedSampleRepository;
import repository.DonorRepository;
import repository.DonorTypeRepository;
import repository.LocationRepository;
import repository.TestResultRepository;

@Controller
public class TestResultController {
  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private TestResultRepository testResultRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private BloodBagTypeRepository bloodBagTypeRepository;
  @Autowired
  private DonorTypeRepository donorTypeRepository;

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private BloodTestRepository bloodTestRepository;

  @Autowired
  private UtilController utilController;

  public TestResultController() {
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(new TestResultBackingFormValidator(binder.getValidator()));
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
    m.put("bloodTests", bloodTestRepository.getAllBloodTests());
  }

  @RequestMapping(value = "/editTestResultFormGenerator", method = RequestMethod.GET, 
      headers = "X-Requested-With=XMLHttpRequest")
  public ModelAndView editTestResultFormGenerator(HttpServletRequest request,
      Model model,
      //@RequestParam(value="collectionNumber", required=false) Long collectionNumber,
      @RequestParam(value="collectionId", required=false) Long collectionId) {

    TestResultBackingForm form = new TestResultBackingForm();

    System.out.println("");
    ModelAndView mv = new ModelAndView("editTestResultForm");
    Map<String, Object> m = model.asMap();
    m.put("refreshUrl", getUrl(request));
    m.put("existingTestResult", false);
    if (collectionId != null) {
      form.setId(collectionId);
      TestResult testResult = testResultRepository.findTestResultByCollectionId(collectionId);
      if (testResult != null) {
        form = new TestResultBackingForm(testResult);
        m.put("existingTestResult", true);
      }
    }

    addEditSelectorOptions(m);
    m.put("editTestResultForm", form);
    m.put("refreshUrl", getUrl(request));
    // to ensure custom field names are displayed in the form
    m.put("testResultFields", utilController.getFormFieldsForForm("TestResult"));
    System.out.println(m);
    mv.addObject("model", m);
    System.out.println(mv);
    System.out.println(mv.getView());
    return mv;
  }

  @RequestMapping(value = "/addTestResult", method = RequestMethod.POST)
  public ModelAndView addTestResult(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("editTestResultForm") @Valid TestResultBackingForm form,
      BindingResult result, Model model) {

    ModelAndView mv = new ModelAndView("editTestResultForm");
    boolean success = false;
    String message = "";
    Map<String, Object> m = model.asMap();

    // IMPORTANT: Validation code just checks if the ID exists.
    // We still need to store the donor as part of the collected sample.
    String collectionNumber = form.getCollectionNumber();
    if (collectionNumber != null && !collectionNumber.isEmpty()) {
      try {
        CollectedSample collectedSample = collectedSampleRepository.findSingleCollectedSampleByCollectionNumber(collectionNumber);
        form.setCollectedSample(collectedSample);
      } catch (NoResultException ex) {
        ex.printStackTrace();
      }
    }

    if (result.hasErrors()) {
      m.put("hasErrors", true);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);      
      success = false;
      message = "Please fix the errors noted above.";
      System.out.println("there are errors");
      for (ObjectError error : result.getAllErrors()) {
        System.out.println(error.getObjectName());
        System.out.println(error.getCode());
        System.out.println(form.getTestResult());
        System.out.println(error.toString());
        System.out.println(error.getDefaultMessage());
      }
    } else {
      try {
        TestResult testResult = form.getTestResult();
        testResult.setIsDeleted(false);
        testResultRepository.addTestResult(testResult);
        m.put("hasErrors", false);
        success = true;
        message = "Test Result Successfully Added";
        form = new TestResultBackingForm();
      } catch (EntityExistsException ex) {
        ex.printStackTrace();
        success = false;
        message = "Test Result Already exists.";
      } catch (Exception ex) {
        ex.printStackTrace();
        success = false;
        message = "Internal Error. Please try again or report a Problem.";
      }
    }

    m.put("editTestResultForm", form);
    m.put("existingTestResult", false);
    m.put("success", success);
    m.put("message", message);
    m.put("refreshUrl", "editTestResultFormGenerator.html");
    m.put("testResultFields", utilController.getFormFieldsForForm("TestResult"));
    addEditSelectorOptions(m);

    mv.addObject("model", m);
    return mv;
  }

}
