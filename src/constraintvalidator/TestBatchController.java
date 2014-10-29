package constraintvalidator;

import backingform.TestBatchBackingForm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.testbatch.TestBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repository.SequenceNumberRepository;
import repository.TestBatchRepository;

@RestController
@RequestMapping("testbatches")
public class TestBatchController {

    @Autowired
    private TestBatchRepository testBatchRepository;
    
    @Autowired
    private SequenceNumberRepository sequenceNumberRepository;


  @RequestMapping(value = "/form", method = RequestMethod.GET)
  public ResponseEntity findAndAddTestBatchFormGenerator() {

    Map<String, Object> map = new HashMap<String, Object>();
    List<TestBatch> allTestBatch = testBatchRepository.getAllTestBatch();
    map.put("allTestBatch", allTestBatch);
    return new ResponseEntity(map, HttpStatus.OK);
  }
  
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addTestBatch(@RequestBody TestBatchBackingForm form) {
        
        testBatchRepository.saveTestBatch(form.getFirstDIN(), form.getLastDIN(), getNextTestBatchNumber());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity findTestBatchPagination(
            @RequestParam("firstDIN") String firstDIN,
            @RequestParam("lastDIN") String lastDIN,
            @RequestParam("testBatchNumber") String testBatchNumber,
            @RequestParam("createdBeforeDate") String createdBeforeDate,
            @RequestParam("createdAfterDate") String createdAfterDate) {

        Map<String, Object> pagingParams = new HashMap<String, Object>();
        pagingParams.put("sortColumn", "id");
        pagingParams.put("sortDirection", "asc");
        
         List<Object> results = testBatchRepository.findCollectedSamplesByTestBatch(
	    		testBatchNumber,
	    		createdAfterDate, createdBeforeDate, pagingParams);
        return new ResponseEntity(results, HttpStatus.OK);

    }
    
    public String getNextTestBatchNumber() {
        return sequenceNumberRepository.getNextTestBatchNumber();
    }
    

}
