package org.jembi.bsis.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.backingform.validator.TestBatchBackingFormValidator;
import org.jembi.bsis.controllerservice.TestBatchControllerService;
import org.jembi.bsis.factory.DonationBatchViewModelFactory;
import org.jembi.bsis.factory.DonationSummaryViewModelFactory;
import org.jembi.bsis.factory.TestBatchViewModelFactory;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.SequenceNumberRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTypingMatchStatus;
import org.jembi.bsis.service.TestBatchCRUDService;
import org.jembi.bsis.utils.CustomDateFormatter;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.utils.PermissionUtils;
import org.jembi.bsis.viewmodel.DonationSummaryViewModel;
import org.jembi.bsis.viewmodel.TestBatchFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

  @Autowired
  private TestBatchBackingFormValidator testBatchBackingFormValidator;

  @Autowired
  private DonationSummaryViewModelFactory donationSummaryViewModelFactory;

  @Autowired
  private TestBatchControllerService testBatchControllerService;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(testBatchBackingFormValidator);
  }

  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TESTING_INFORMATION+"')")
  public ResponseEntity<Map<String, Object>> findAndAddTestBatchFormGenerator() {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("status", TestBatchStatus.values());
    map.put("donationBatches", donationBatchViewModelFactory
        .createDonationBatchBasicViewModels(donationBatchRepository.findUnassignedDonationBatches()));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_TEST_BATCH+"')")
  public ResponseEntity<TestBatchFullViewModel> addTestBatch(@Valid @RequestBody TestBatchBackingForm form) {

    TestBatch testBatch = testBatchRepository.saveTestBatch(form.getTestBatch(), getNextTestBatchNumber());
    boolean isTestingSupervisor = PermissionUtils.loggedOnUserHasPermission(PermissionConstants.EDIT_TEST_BATCH);
    return new ResponseEntity<>(testBatchViewModelFactory.createTestBatchFullViewModel(testBatch, isTestingSupervisor),
        HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}",  method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_BATCH+"')")
  @Transactional(readOnly = true)
  public ResponseEntity<Map<String, Object>> getTestBatchById(@PathVariable Long id){

    Map<String, Object> map = new HashMap<String, Object>();
    TestBatch testBatch = testBatchRepository.findTestBatchById(id);
    boolean isTestingSupervisor = PermissionUtils.loggedOnUserHasPermission(PermissionConstants.EDIT_TEST_BATCH);
    map.put("testBatch", testBatchViewModelFactory.createTestBatchFullViewModel(testBatch, isTestingSupervisor));
    return new ResponseEntity<>(map, HttpStatus.OK);

  }

  @RequestMapping(value = "{id}",  method = RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_TEST_BATCH+"')")
  public ResponseEntity<TestBatchFullViewModel> updateTestBatch(@PathVariable Long id,
      @Valid @RequestBody TestBatchBackingForm form) {
    form.setId(id);
    return new ResponseEntity<>(testBatchControllerService.updateTestBatch(form), HttpStatus.OK);
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_BATCH+"')")
  public ResponseEntity<Map<String, Object>> findTestBatchPagination(
      @RequestParam(value = "status", required = false) List<TestBatchStatus> statuses ,
      @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {

    List<TestBatch> testBatches = testBatchRepository.findTestBatches(statuses, startDate, endDate);
    Map<String, Object> map = new HashMap<>();
    map.put("testBatches", testBatchViewModelFactory.createTestBatchBasicViewModels(testBatches));

    return new ResponseEntity<>(map, HttpStatus.OK);

  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_TEST_BATCH + "')")
  public void deleteTestBatchById(@PathVariable Long id) {
    testBatchCRUDService.deleteTestBatch(id);
  }

  @RequestMapping(value = "/{id}/donations", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TESTING_INFORMATION + "')")
  public ResponseEntity<Map<String, Object>> getDonationsForTestBatch(@PathVariable Long id,
      @RequestParam(value = "bloodTypingMatchStatus", required = false) BloodTypingMatchStatus bloodTypingMatchStatus) {

    TestBatch testBatch = testBatchRepository.findTestBatchById(id);
    List<DonationSummaryViewModel> donationSummaryViewModels =
        donationSummaryViewModelFactory.createDonationSummaryViewModels(testBatch, bloodTypingMatchStatus);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donations", donationSummaryViewModels);
    map.put("testBatchCreatedDate", CustomDateFormatter.format(testBatch.getCreatedDate()));
    map.put("numberOfDonations", donationSummaryViewModels.size());

    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  private String getNextTestBatchNumber() {
    return sequenceNumberRepository.getNextTestBatchNumber();
  }
}
