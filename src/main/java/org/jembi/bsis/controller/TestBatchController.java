package org.jembi.bsis.controller;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.backingform.TestBatchDonationRangeBackingForm;
import org.jembi.bsis.backingform.TestBatchDonationsBackingForm;
import org.jembi.bsis.backingform.validator.TestBatchBackingFormValidator;
import org.jembi.bsis.backingform.validator.TestBatchDonationRangeBackingFormValidator;
import org.jembi.bsis.backingform.validator.TestBatchDonationsBackingFormValidator;
import org.jembi.bsis.controllerservice.TestBatchControllerService;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.viewmodel.TestBatchFullDonationViewModel;
import org.jembi.bsis.viewmodel.TestBatchFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

@RestController
@RequestMapping("testbatches")
public class TestBatchController {

  @Autowired
  private TestBatchControllerService testBatchControllerService;
  @Autowired
  private TestBatchBackingFormValidator testBatchBackingFormValidator;
  @Autowired
  private TestBatchDonationsBackingFormValidator testBatchDonationsBackingFormValidator;
  @Autowired
  private TestBatchDonationRangeBackingFormValidator testBatchDonationRangeBackingFormValidator;

  @InitBinder("testBatchBackingForm")
  public void initBinder(WebDataBinder binder) {
    binder.setValidator(testBatchBackingFormValidator);
  }
  @InitBinder("testBatchDonationRangeBackingForm")
  protected void discardInitBinder(WebDataBinder binder) {
    binder.setValidator(testBatchDonationRangeBackingFormValidator);
  }
  @InitBinder("testBatchDonationsBackingForm")
  public void testBatchDonationsBackingFormBinder(WebDataBinder binder) {
    binder.setValidator(testBatchDonationsBackingFormValidator);
  }
  
  @RequestMapping(value = "/form", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TESTING_INFORMATION + "')")
  public Map<String, Object> findAndAddTestBatchFormGenerator() {
    Map<String, Object> map = new HashMap<>();
    map.put("status", TestBatchStatus.values());
    map.put("testingSites", testBatchControllerService.getTestingSites());
    return map;
  }

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_TEST_BATCH + "')")
  @ResponseStatus(HttpStatus.CREATED)
  public TestBatchFullViewModel addTestBatch(@Valid @RequestBody TestBatchBackingForm testBatchBackingForm) {
    return testBatchControllerService.addTestBatch(testBatchBackingForm);
  }

  @RequestMapping(value = "{id}",  method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TEST_BATCH + "')")
  @Transactional(readOnly = true)
  public Map<String, Object> getTestBatchById(@PathVariable UUID id){
    Map<String, Object> map = new HashMap<>();
    map.put("testBatch", testBatchControllerService.getTestBatchById(id));
    return map;

  }

  @RequestMapping(value = "{id}",  method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_TEST_BATCH + "')")
  public TestBatchFullViewModel updateTestBatch(@PathVariable UUID id,
      @Valid @RequestBody TestBatchBackingForm testBatchBackingForm) {
    testBatchBackingForm.setId(id);
    return testBatchControllerService.updateTestBatch(testBatchBackingForm);
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TEST_BATCH + "')")
  public Map<String, Object> findTestBatch(
      @RequestParam(value = "status", required = false) List<TestBatchStatus> statuses ,
      @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
      @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate,
      @RequestParam(value = "locationId", required = false) UUID locationId) {
    Map<String, Object> map = new HashMap<>();
    map.put("testBatches", testBatchControllerService.findTestBatches(statuses, startDate, endDate, locationId));
    return map;

  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('" + PermissionConstants.VOID_TEST_BATCH + "')")
  public void deleteTestBatchById(@PathVariable UUID id) {
    testBatchControllerService.deleteTestBatch(id);
  }

  @RequestMapping(value = "/{id}/donations", method = RequestMethod.GET)
  @PreAuthorize("hasRole('" + PermissionConstants.VIEW_TESTING_INFORMATION + "')")
  public TestBatchFullDonationViewModel getDonationsForTestBatch(@PathVariable UUID id,
      @RequestParam(value = "bloodTypingMatchStatus", required = false) BloodTypingMatchStatus bloodTypingMatchStatus) {
    return testBatchControllerService.getTestBatchByIdAndBloodTypingMatchStatus(id, bloodTypingMatchStatus);
  }

  @RequestMapping(value = "{id}/addDonations",  method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.ADD_TEST_BATCH + "')")
  public TestBatchFullViewModel addDonationsToTestBatch(@PathVariable UUID id,
      @Valid @RequestBody TestBatchDonationRangeBackingForm testBatchDonationRangeBackingForm) {
    testBatchDonationRangeBackingForm.setTestBatchId(id);
    return testBatchControllerService.addDonationsToTestBatch(testBatchDonationRangeBackingForm);
  }

  @RequestMapping(value = "{id}/removeDonations", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('" + PermissionConstants.EDIT_TEST_BATCH + "')")
  public TestBatchFullViewModel removeDonationsFromTestBatch(
      @PathVariable UUID id, @Valid @RequestBody TestBatchDonationsBackingForm testBatchDonationRangeBackingForm) {
    testBatchDonationRangeBackingForm.setTestBatchId(id);
    return testBatchControllerService.removeDonationsFromBatch(testBatchDonationRangeBackingForm);
  }
}
