package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import model.collectedsample.CollectedSample;
import model.collectionbatch.CollectionBatch;
import model.testbatch.TestBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import repository.CollectedSampleRepository;
import repository.TestBatchRepository;
import repository.bloodtesting.BloodTestingRepository;
import utils.PermissionConstants;
import viewmodel.CollectedSampleViewModel;
import viewmodel.BloodTestingRuleResult;

@RestController
@RequestMapping("testresults")
public class TestResultController {

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private TestBatchRepository testBatchRepository;
  
  @Autowired
  private BloodTestingRepository bloodTestingRepository;
  
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

  public static String getUrl(HttpServletRequest req) {
    String reqUrl = req.getRequestURL().toString();
    String queryString = req.getQueryString();   // d=789
    if (queryString != null) {
        reqUrl += "?"+queryString;
    }
    return reqUrl;
  }
  */

  @RequestMapping(value = "{donationIdentificationNumber}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_OUTCOME+"')")
  public ResponseEntity findTestResult(@PathVariable String donationIdentificationNumber ) {

    Map<String, Object> map = new HashMap<String, Object>();
    CollectedSample c = collectedSampleRepository.findCollectedSampleByCollectionNumber(donationIdentificationNumber);
    BloodTestingRuleResult results =  bloodTestingRepository.getAllTestsStatusForCollection(c.getId());
    map.put("donation", new CollectedSampleViewModel(c));
    map.put("testResults", results);
    return new ResponseEntity(map, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_OUTCOME+"')")
  public ResponseEntity findTestResultsForTestBatch(HttpServletRequest request,
		@RequestParam(value = "testBatch", required = true) Long testBatchId) {
	  
		Map<String, Object> map = new HashMap<String, Object>();
		
		TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
		List<CollectionBatch> collectionBatches = testBatch.getCollectionBatches();
		List<Integer> donationBatchIds = new ArrayList<Integer>();
		for(CollectionBatch collectionBatch : collectionBatches){
			donationBatchIds.add(collectionBatch.getId());
		}
	
	    List<BloodTestingRuleResult> ruleResults =
	    		bloodTestingRepository.getAllTestsStatusForDonationBatches(donationBatchIds);
	
		map.put("testResults", ruleResults);
	
		return new ResponseEntity(map, HttpStatus.OK);
  }

}
