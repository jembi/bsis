package controller;

import java.util.*;

import javax.validation.Valid;

import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import factory.DonationBatchViewModelFactory;
import factory.TestBatchViewModelFactory;
import repository.DonationBatchRepository;
import repository.SequenceNumberRepository;
import repository.TestBatchRepository;
import service.TestBatchCRUDService;
import utils.PermissionConstants;
import viewmodel.DonationBatchViewModel;
import viewmodel.TestBatchViewModel;
import backingform.TestBatchBackingForm;
import backingform.validator.TestBatchBackingFormValidator;

@RestController
@RequestMapping("testbatches")
public class TestBatchController {

    @Autowired
    private TestBatchRepository testBatchRepository;
    
    @Autowired
    private DonationBatchRepository donationBatchRepository;
    
    @Autowired
    private SequenceNumberRepository sequenceNumberRepository;
    
    @Autowired
    private TestBatchCRUDService testBatchCRUDService;
    
    @Autowired
    private DonationBatchViewModelFactory donationBatchViewModelFactory;
    
    @Autowired
    private TestBatchViewModelFactory testBatchViewModelFactory;
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new TestBatchBackingFormValidator(binder.getValidator(), donationBatchRepository));
    }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TESTING_INFORMATION+"')")
  public ResponseEntity findAndAddTestBatchFormGenerator() {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("status", TestBatchStatus.values());
    map.put("donationBatches", getDonationBatchViewModels(donationBatchRepository.findUnassignedDonationBatches()));
    return new ResponseEntity(map, HttpStatus.OK);
  }
  
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('"+PermissionConstants.ADD_TEST_BATCH+"')")
    public ResponseEntity<TestBatchViewModel> addTestBatch(@Valid @RequestBody TestBatchBackingForm form) {
        
        TestBatch testBatch = testBatchRepository.saveTestBatch(form.getTestBatch(), getNextTestBatchNumber());
        return new ResponseEntity<>(testBatchViewModelFactory.createTestBatchViewModel(testBatch), HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "{id}",  method = RequestMethod.GET)
    @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_BATCH+"')")
    public ResponseEntity getTestBatchById(@PathVariable Long id){
        
        Map<String, Object> map = new HashMap<String, Object>();
        TestBatch testBatch = testBatchRepository.findTestBatchById(id);
        map.put("testBatch", testBatchViewModelFactory.createTestBatchViewModel(testBatch));
        return new ResponseEntity(map, HttpStatus.OK);
        
    }
    
    @RequestMapping(value = "{id}",  method = RequestMethod.PUT)
    @PreAuthorize("hasRole('"+PermissionConstants.EDIT_TEST_BATCH+"')")
    public ResponseEntity<TestBatchViewModel> updateTestBatch(@PathVariable Long id,
            @RequestBody TestBatchBackingForm form){
        
        TestBatch testBatch = testBatchCRUDService.updateTestBatchStatus(id, form.getTestBatch().getStatus());
        return new ResponseEntity<>(testBatchViewModelFactory.createTestBatchViewModel(testBatch), HttpStatus.OK);
        
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_BATCH+"')")
    public ResponseEntity findTestBatchPagination(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {

        Map<String, Object> pagingParams = new HashMap<String, Object>();
        pagingParams.put("sortColumn", "id");
        pagingParams.put("sortDirection", "asc");
        
        List<TestBatchViewModel> testBatches = testBatchRepository.findTestBatches(status,
                startDate, endDate, pagingParams);
         
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("testBatches", testBatches);

        return new ResponseEntity(map, HttpStatus.OK);

    }
    
    @RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('"+PermissionConstants.VOID_TEST_BATCH+"')")
    public ResponseEntity deleteTestBatchById(Long id){
        testBatchRepository.deleteTestBatch(id);
        return  new ResponseEntity(HttpStatus.NO_CONTENT);
    }
  
    
    public String getNextTestBatchNumber() {
        return sequenceNumberRepository.getNextTestBatchNumber();
    }
    
    public List<DonationBatchViewModel> getDonationBatchViewModels(
	      List<DonationBatch> donationBatches) {
	    if (donationBatches == null)
	      return Arrays.asList(new DonationBatchViewModel[0]);
	    List<DonationBatchViewModel> donationBatchViewModels = new ArrayList<DonationBatchViewModel>();
	    for (DonationBatch donationBatch : donationBatches) {
	      donationBatchViewModels.add(donationBatchViewModelFactory.createDonationBatchViewModel(donationBatch));
	    }
	    return donationBatchViewModels;
	}
    
    public List<TestBatchViewModel> getTestBatchViewModels(
            List<TestBatch> testBatches) {
        if (testBatches == null) {
            return Arrays.asList(new TestBatchViewModel[0]);
        }
        List<TestBatchViewModel> testBatchViewModels = new ArrayList<TestBatchViewModel>();
        for (TestBatch testBatch : testBatches) {
            testBatchViewModels.add(testBatchViewModelFactory.createTestBatchViewModel(testBatch));
        }
        return testBatchViewModels;
    }

       
}
