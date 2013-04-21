package controller.bloodtesting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import controller.UtilController;

@Controller
public class BloodTestingController {

  @Autowired
  private UtilController utilController;

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  public BloodTestingController() {
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value="bloodTestSummary", method=RequestMethod.GET)
  public ModelAndView getBloodTestSummary(HttpServletRequest request,
      @RequestParam(value="bloodTestId") Integer bloodTestId) {

    ModelAndView mv = new ModelAndView ("admin/bloodTestSummary");
    BloodTestViewModel bloodTest;
    bloodTest = new BloodTestViewModel(bloodTestingRepository.findBloodTestWithWorksheetTypesById(bloodTestId));
    mv.addObject("bloodTest", bloodTest);
    mv.addObject("refreshUrl", getUrl(request));
    return mv;
  }

  @RequestMapping(value="saveNewBloodTest", method=RequestMethod.POST)
  public @ResponseBody Map<String, Object> saveNewBloodTest(HttpServletRequest request,
      HttpServletResponse response, @RequestParam("bloodTest") String newBloodTestAsJsonStr) {
    Map<String, Object> m = new HashMap<String, Object>();
    ObjectMapper mapper = new ObjectMapper();
    boolean success = false;
    try {
      Map<String, Object> newBloodTypingRuleAsMap;
      newBloodTypingRuleAsMap = mapper.readValue(newBloodTestAsJsonStr, HashMap.class);
      bloodTestingRepository.saveNewBloodTest(newBloodTypingRuleAsMap);
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
  
  @RequestMapping(value="deactivateBloodTest", method=RequestMethod.POST)
  public @ResponseBody Map<String, Object> deactivateBloodTest(HttpServletRequest request,
      @RequestParam(value="bloodTestId") Integer bloodTestId) {

    Map<String, Object> m = new HashMap<String, Object>();
    bloodTestingRepository.deactivateBloodTest(bloodTestId);
    return m;
  }
}
