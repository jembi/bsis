package controller.bloodtyping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectedSampleRepository;
import repository.GenericConfigRepository;
import repository.bloodtyping.BloodTestingRepository;
import viewmodel.BloodTestViewModel;
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

}
