package org.jembi.bsis.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.backingform.validator.TestBatchBackingFormValidator;
import org.jembi.bsis.controllerservice.TestBatchControllerService;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.bloodtesting.BloodTypingMatchStatus;
import org.jembi.bsis.utils.CustomDateFormatter;
import org.jembi.bsis.utils.PermissionConstants;
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
  private TestBatchBackingFormValidator testBatchBackingFormValidator;

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
    map.put("donationBatches", testBatchControllerService.getUnnasignedDonationBatches());
    map.put("testingSites", testBatchControllerService.getTestingSites());
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('"+PermissionConstants.ADD_TEST_BATCH+"')")
  public ResponseEntity<TestBatchFullViewModel> addTestBatch(@Valid @RequestBody TestBatchBackingForm form) {
    TestBatchFullViewModel testBatch = testBatchControllerService.saveTestBatch(form);
    return new ResponseEntity<>(testBatch, HttpStatus.CREATED);
  }

  @RequestMapping(value = "{id}",  method = RequestMethod.GET)
  @PreAuthorize("hasRole('"+PermissionConstants.VIEW_TEST_BATCH+"')")
  @Transactional(readOnly = true)
  public ResponseEntity<Map<String, Object>> getTestBatchById(@PathVariable long id){
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("testBatch", testBatchControllerService.getTestBatchById(id));
    return new ResponseEntity<>(map, HttpStatus.OK);

  }

  @RequestMapping(value = "{id}",  method = RequestMethod.PUT)
  @PreAuthorize("hasRole('"+PermissionConstants.EDIT_TEST_BATCH+"')")
  public ResponseEntity<TestBatchFullViewModel> updateTestBatch(@PathVariable long id,
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
    Map<String, Object> map = new HashMap<>();
    map.put("testBatches", testBatchControllerService.findTestBatches(statuses, startDate, endDate));
    return new ResponseEntity<>(map, HttpStatus.OK);

  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_TEST_BATCH + "')")
  public void deleteTestBatchById(@PathVariable Long id) {
    testBatchControllerService.deleteTestBatch(id);
  }

  @RequestMapping(value = "/{id}/donations", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TESTING_INFORMATION + "')")
  public ResponseEntity<Map<String, Object>> getDonationsForTestBatch(@PathVariable long id,
      @RequestParam(value = "bloodTypingMatchStatus", required = false) BloodTypingMatchStatus bloodTypingMatchStatus) {
    Date testBatchCreatedDate = testBatchControllerService.getTestBatchCreatedDate(id);
    List<DonationSummaryViewModel> donationSummaryViewModels = testBatchControllerService.getDonationsSummaries(id, bloodTypingMatchStatus);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("donations", donationSummaryViewModels);
    map.put("testBatchCreatedDate", CustomDateFormatter.format(testBatchCreatedDate));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
}
