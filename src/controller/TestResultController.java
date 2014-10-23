package controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import model.collectedsample.CollectedSample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import repository.CollectedSampleRepository;
import utils.PermissionConstants;

@RestController
@RequestMapping("testresults")
public class TestResultController {

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  public TestResultController() {
  }
/*
  isssue - #209
  Reason - Dummy method
  @RequestMapping(value = "/findform", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_OUTCOME+"')")
  public  Map<String, Object> findTestResultFormGenerator(HttpServletRequest request) {

    Map<String, Object> map = new  HashMap<String, Object>();

    // to ensure custom field names are displayed in the form
    map.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    return map;
  }
*/
  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "{donationNumber}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_OUTCOME+"')")
  public ResponseEntity findTestResult(@PathVariable String donationNumber ) {

    Map<String, Object> map = new HashMap<String, Object>();
    CollectedSample c = collectedSampleRepository.findCollectedSampleByCollectionNumber(donationNumber);
    map.put("collectionId", c.getId());
    return new ResponseEntity(map, HttpStatus.OK);
  }
}
