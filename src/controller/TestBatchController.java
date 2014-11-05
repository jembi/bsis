package controller;

import backingform.TestBatchBackingForm;
import backingform.validator.TestBatchBackingFormValidator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import javax.validation.Valid;
import model.collectionbatch.CollectionBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repository.CollectionBatchRepository;
import repository.SequenceNumberRepository;
import repository.TestBatchRepository;
import utils.PermissionConstants;
import viewmodel.TestBatchViewModel;
import viewmodel.CollectionBatchViewModel;

@RestController
@RequestMapping("testbatches")
public class TestBatchController {

    @Autowired
    private TestBatchRepository testBatchRepository;
    
    @Autowired
    private CollectionBatchRepository collectionBatchRepository;
    
    @Autowired
    private SequenceNumberRepository sequenceNumberRepository;
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new TestBatchBackingFormValidator(binder.getValidator(), collectionBatchRepository));
    }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
  public ResponseEntity findAndAddTestBatchFormGenerator() {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("status", TestBatchStatus.values());
    map.put("donationBatches", getCollectionBatchViewModels(collectionBatchRepository.findUnassignedCollectionBatches()));
    return new ResponseEntity(map, HttpStatus.OK);
  }
  
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
    public ResponseEntity addTestBatch(@Valid @RequestBody TestBatchBackingForm form) {
        
        TestBatch testBatch = testBatchRepository.saveTestBatch(form.getTestBatch(), getNextTestBatchNumber());
        return new ResponseEntity(new TestBatchViewModel(testBatch), HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "{id}",  method = RequestMethod.GET)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
    public ResponseEntity getTestBatchById(@PathVariable Long id){
        
        Map<String, Object> map = new HashMap<String, Object>();
        TestBatch testBatch = testBatchRepository.findTestBatchById(id);
        map.put("testBatch", new TestBatchViewModel(testBatch));
        return new ResponseEntity(map, HttpStatus.OK);
        
    }
    
    @RequestMapping(value = "{id}",  method = RequestMethod.PUT)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
    public ResponseEntity updateTestBatch(@PathVariable Long id,
            @RequestBody TestBatchBackingForm form){
        
        TestBatch testBatch = form.getTestBatch();
        testBatch = testBatchRepository.updateTestBatch(testBatch);
        return new ResponseEntity(new TestBatchViewModel(testBatch), HttpStatus.OK);
        
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
         
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("testBatches", testBatches);

        return new ResponseEntity(map, HttpStatus.OK);

    }
    
    @RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_BLOOD_TESTS+"')")
    public ResponseEntity deleteTestBatchById(Long id){
        testBatchRepository.deleteTestBatch(id);
        return  new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
    public String getNextTestBatchNumber() {
        return sequenceNumberRepository.getNextTestBatchNumber();
    }
    
    public static List<CollectionBatchViewModel> getCollectionBatchViewModels(
	      List<CollectionBatch> collectionBatches) {
	    if (collectionBatches == null)
	      return Arrays.asList(new CollectionBatchViewModel[0]);
	    List<CollectionBatchViewModel> collectionBatchViewModels = new ArrayList<CollectionBatchViewModel>();
	    for (CollectionBatch collectionBatch : collectionBatches) {
	      collectionBatchViewModels.add(new CollectionBatchViewModel(collectionBatch));
	    }
	    return collectionBatchViewModels;
	}

}
