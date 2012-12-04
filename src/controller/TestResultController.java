package controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.testresults.TestResult;
import model.testresults.TestResultBackingForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
}
