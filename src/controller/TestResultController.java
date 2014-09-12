package controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import model.collectedsample.CollectedSample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import repository.CollectedSampleRepository;
import utils.PermissionConstants;

@Controller
@RequestMapping("testresult")
public class TestResultController {

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private UtilController utilController;

  public TestResultController() {
  }

  @RequestMapping(value = "/findform", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_OUTCOME+"')")
  public @ResponseBody Map<String, Object> findTestResultFormGenerator(HttpServletRequest request) {


    Map<String, Object> map = new  HashMap<String, Object>();

    Map<String, Object> tips = new HashMap<String, Object>();
    utilController.addTipsToModel(tips, "testResults.find");
    map.put("tips", tips);

    // to ensure custom field names are displayed in the form
    map.put("collectedSampleFields", utilController.getFormFieldsForForm("collectedSample"));
    map.put("refreshUrl", getUrl(request));
    return map;
  }

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }

  @RequestMapping(value = "collectionnumber/{collectionNumber}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_OUTCOME+"')")
  public @ResponseBody Map<String, Object> findTestResult(HttpServletRequest request,
      @PathVariable String collectionNumber ) {

    Map<String, Object> map = new HashMap<String, Object>();
    CollectedSample c = null;
    c = collectedSampleRepository.findCollectedSampleByCollectionNumber(collectionNumber);
    if (c == null) {
      map.put("collectionFound", false);
    }
    else {
      map.put("collectionFound", true);
      map.put("collectionId", c.getId());
    }
    return map;
  }
}
