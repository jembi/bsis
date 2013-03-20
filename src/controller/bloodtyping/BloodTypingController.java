package controller.bloodtyping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.bloodtyping.BloodTypingTest;
import model.bloodtyping.BloodTypingTestType;
import model.collectedsample.CollectedSample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectedSampleRepository;
import repository.GenericConfigRepository;
import repository.bloodtyping.BloodTypingRepository;
import viewmodel.BloodTypingTestViewModel;
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
  private BloodTypingRepository bloodTypingRepository;

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
    ModelAndView mv = new ModelAndView("bloodtyping/bloodTypingWorksheetForm");

    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "bloodtyping.plate.step1");
    mv.addObject("tips", tips);
    mv.addObject("plate", bloodTypingRepository.getPlate("bloodtyping"));
    mv.addObject("refreshUrl", "bloodTypingWorksheetGenerator.html");

    return mv;
  }

  @RequestMapping(value="/addCollectionsToBloodTypingPlate")
  public ModelAndView addCollectionsToBloodTypingPlate(HttpServletRequest request,
          HttpServletResponse response,
          @RequestParam(value="collectionNumbers[]") List<String> collectionNumbers) {

    Map<Integer, CollectedSample> collections = collectedSampleRepository.verifyCollectionNumbers(collectionNumbers);

    int numErrors = 0;
    int numValid = 0;
    for (CollectedSample c : collections.values()) {
      if (c == null)
        numErrors++;
      else
        numValid++;
    }

    ModelAndView mv = new ModelAndView();
    mv.addObject("collections", collections);
    mv.addObject("plate", bloodTypingRepository.getPlate("bloodtyping"));

    Map<String, Object> tips = new HashMap<String, Object>();
    if (numErrors > 0 || numValid == 0) {
      mv.addObject("success", false);
      mv.setViewName("bloodtyping/bloodTypingWorksheetForm");
      mv.addObject("refreshUrl", "bloodTypingWorksheetGenerator.html");
      utilController.addTipsToModel(tips, "bloodtyping.plate.step1");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      mv.addObject("errorMessage", "Please fix the errors noted. Some collections do not exist.");
    } else {
      mv.addObject("success", true);
      mv.setViewName("bloodtyping/bloodTypingWells");
      mv.addObject("refreshUrl", getUrl(request));
      mv.addObject("changeCollectionsUrl", "bloodTypingWorksheetGenerator.html");
      utilController.addTipsToModel(tips, "bloodtyping.plate.step2");
      mv.addObject("bloodTestsOnPlate", getBloodTestsOnPlate());
      mv.addObject("bloodTypingConfig", genericConfigRepository.getConfigProperties("bloodTyping"));
    }

    mv.addObject("tips", tips);
    return mv;
  }

  public List<BloodTypingTestViewModel> getBloodTestsOnPlate() {
    List<BloodTypingTestViewModel> tests = new ArrayList<BloodTypingTestViewModel>();
    for (BloodTypingTest rawBloodTest : bloodTypingRepository.getBloodTypingTestsOfType(BloodTypingTestType.BASIC)) {
      tests.add(new BloodTypingTestViewModel(rawBloodTest));
    }
    return tests;
  }

  @RequestMapping(value="/saveBloodTypingTests", method=RequestMethod.POST)
  public ModelAndView saveBloodTypingTests(HttpServletRequest request,
      HttpServletResponse response, @RequestParam(value="rawBloodTests") String rawBloodTests) {

    ModelAndView mv = new ModelAndView();

    Map<String, List<BloodTypingTest>> resultStatus = bloodTypingRepository.saveBloodTypingResults(rawBloodTests);

    return mv;
  }

}
