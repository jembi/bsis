package controller.bloodtesting;



import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import repository.bloodtesting.BloodTestingRepository;
import utils.PermissionConstants;
import viewmodel.BloodTestViewModel;

@Controller
@RequestMapping("bloodtest")
public class BloodTestingController {

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

  @RequestMapping(method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_OUTCOME+"')")
  public @ResponseBody Map<String, Object> getBloodTestSummary(HttpServletRequest request,
      @RequestParam(value="bloodTestId") Integer bloodTestId) {
      
    Map<String, Object> map = new HashMap<String, Object>();  
    BloodTestViewModel bloodTest;
    bloodTest = new BloodTestViewModel(bloodTestingRepository.findBloodTestWithWorksheetTypesById(bloodTestId));
    map.put("bloodTest", bloodTest);
    map.put("refreshUrl", getUrl(request));
    return map;
  }

  @RequestMapping(method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public @ResponseBody Map<String, Object> saveNewBloodTest(HttpServletRequest request,
      HttpServletResponse response, @RequestBody Map<String, Object> newBloodTestAsMap) {
    Map<String, Object> m = new HashMap<String, Object>();
    ObjectMapper mapper = new ObjectMapper();
    boolean success = false;
    
      bloodTestingRepository.saveNewBloodTest(newBloodTestAsMap);
      success = true;
   
    if (!success)
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return m;
  }
  
  @RequestMapping(value="{id}/deactivate", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public @ResponseBody Map<String, Object> deactivateBloodTest(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> m = new HashMap<String, Object>();
    bloodTestingRepository.deactivateBloodTest(id);
    return m;
  }

  @RequestMapping(value="{id}/activate", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public @ResponseBody Map<String, Object> activateBloodTest(HttpServletRequest request,
      @PathVariable Integer id) {

    Map<String, Object> m = new HashMap<String, Object>();
    bloodTestingRepository.activateBloodTest(id);
    return m;
  }
}
