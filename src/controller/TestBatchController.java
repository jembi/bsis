package controller;

import backingform.TestBatchBackingForm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.testbatch.TestBatch;
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
import repository.SequenceNumberRepository;
import repository.TestBatchRepository;
import utils.PermissionConstants;
import viewmodel.TestBatchViewModel;

@RestController
@RequestMapping("testbatches")
public class TestBatchController {

    @Autowired
    private TestBatchRepository testBatchRepository;
    
    @Autowired
    private SequenceNumberRepository sequenceNumberRepository;


  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public ResponseEntity findAndAddTestBatchFormGenerator() {

    Map<String, Object> map = new HashMap<String, Object>();
    List<TestBatch> allTestBatch = testBatchRepository.getAllTestBatch();
    map.put("allTestBatch", allTestBatch);
    return new ResponseEntity(map, HttpStatus.OK);
  }
  
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
    public ResponseEntity addTestBatch(@RequestBody TestBatchBackingForm form) {
        
        testBatchRepository.saveTestBatch(form.getCollectionBatchIds(), getNextTestBatchNumber());
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "{id}",  method = RequestMethod.GET)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
    public ResponseEntity getTestBatchById(@PathVariable Long id){
        
        Map<String, Object> map = new HashMap<String, Object>();
        TestBatch testBatch = testBatchRepository.findtestBatchById(id);
        map.put("testBatch", new TestBatchViewModel(testBatch));
        return new ResponseEntity(map, HttpStatus.OK);
        
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
    public ResponseEntity findTestBatchPagination(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "createdBeforeDate", required = false) String createdBeforeDate,
            @RequestParam(value = "createdAfterDate", required = false) String createdAfterDate) {

        Map<String, Object> pagingParams = new HashMap<String, Object>();
        pagingParams.put("sortColumn", "id");
        pagingParams.put("sortDirection", "asc");
        
         List<TestBatchViewModel> testBatches = testBatchRepository.findTestBatches(status,
	    		createdAfterDate, createdBeforeDate, pagingParams);
        return new ResponseEntity(testBatches, HttpStatus.OK);

    }
    
    public String getNextTestBatchNumber() {
        return sequenceNumberRepository.getNextTestBatchNumber();
    }
    

}
