package controller.bloodtesting;



import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repository.bloodtesting.BloodTestingRepository;
import utils.PermissionConstants;
import viewmodel.BloodTestViewModel;

@RestController
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
  public Map<String, Object> getBloodTestSummary(@RequestParam(value="bloodTestId") Integer bloodTestId) {
      
    Map<String, Object> map = new HashMap<String, Object>();  
    BloodTestViewModel bloodTest;
    bloodTest = new BloodTestViewModel(bloodTestingRepository.findBloodTestWithWorksheetTypesById(bloodTestId));
    map.put("bloodTest", bloodTest);
    return map;
  }

  @RequestMapping(method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public ResponseEntity<Map<String, Object>> saveNewBloodTest(HttpServletRequest request,
      HttpServletResponse response, @RequestBody Map<String, Object> newBloodTestAsMap) {
      Map<String, Object> m = new HashMap<String, Object>();
      bloodTestingRepository.saveNewBloodTest(newBloodTestAsMap);
      return new ResponseEntity<Map<String, Object>>(m, HttpStatus.CREATED);
  }
  
  @RequestMapping(value="{id}/deactivate", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public ResponseEntity<Map<String, Object>> deactivateBloodTest(@PathVariable Integer id) {

    Map<String, Object> m = new HashMap<String, Object>();
    bloodTestingRepository.deactivateBloodTest(id);
    return new ResponseEntity<Map<String, Object>>(m, HttpStatus.CREATED);
  }

  @RequestMapping(value="{id}/activate", method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public ResponseEntity<Map<String, Object>> activateBloodTest(@PathVariable Integer id) {

    Map<String, Object> m = new HashMap<String, Object>();
    bloodTestingRepository.activateBloodTest(id);
    return new ResponseEntity<Map<String, Object>>(m, HttpStatus.CREATED);
  }
}
