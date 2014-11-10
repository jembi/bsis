package controller.bloodtesting;



import backingform.BloodTestBackingForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import model.bloodtesting.BloodTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import repository.bloodtesting.BloodTestingRepository;
import utils.PermissionConstants;
import viewmodel.BloodTestViewModel;

@RestController
@RequestMapping("bloodtests")
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
  
  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public  Map<String, Object> configureBloodTests() {
    Map<String, Object> map = new HashMap<String, Object>();
    List<BloodTestViewModel> bloodTests = new ArrayList<BloodTestViewModel>();
    for (BloodTest bt : bloodTestingRepository.getAllBloodTestsIncludeInactive()) {
      bloodTests.add(new BloodTestViewModel(bt));
    }
    map.put("bloodTests", bloodTests);
    return map;
  }
  @RequestMapping(value = "{id}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_OUTCOME+"')")
  public Map<String, Object> getBloodTestSummary(@PathVariable Integer id) {
      
    Map<String, Object> map = new HashMap<String, Object>();  
    BloodTestViewModel bloodTest;
    bloodTest = new BloodTestViewModel(bloodTestingRepository.findBloodTestWithWorksheetTypesById(id));
    map.put("bloodTest", bloodTest);
    return map;
  }

  @RequestMapping(method=RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public ResponseEntity saveNewBloodTest(
      @RequestBody BloodTestBackingForm form) {
      bloodTestingRepository.saveBloodTest(form);
      return new ResponseEntity(new BloodTestViewModel(form.getBloodTest()), HttpStatus.CREATED);
  }
  
  @RequestMapping(value="{id}/deactivate", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public ResponseEntity deactivateBloodTest(@PathVariable Integer id) {
    bloodTestingRepository.deactivateBloodTest(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @RequestMapping(value="{id}/activate", method=RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public ResponseEntity activateBloodTest(@PathVariable Integer id) {

   
    bloodTestingRepository.activateBloodTest(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}
