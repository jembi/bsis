package org.jembi.bsis.controllerservice;

import java.util.Date;
import java.util.List;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.factory.DonationBatchViewModelFactory;
import org.jembi.bsis.factory.DonationSummaryViewModelFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.factory.TestBatchFactory;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.SequenceNumberRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.jembi.bsis.service.TestBatchCRUDService;
import org.jembi.bsis.utils.PermissionConstants;
import org.jembi.bsis.utils.PermissionUtils;
import org.jembi.bsis.viewmodel.DonationBatchViewModel;
import org.jembi.bsis.viewmodel.DonationSummaryViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.TestBatchFullViewModel;
import org.jembi.bsis.viewmodel.TestBatchViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestBatchControllerService {
  
  @Autowired
  private TestBatchCRUDService testBatchCRUDService;
  @Autowired
  private TestBatchFactory testBatchFactory;
  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private LocationFactory locationFactory;
  
  @Autowired
  private TestBatchRepository testBatchRepository;

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  @Autowired
  private SequenceNumberRepository sequenceNumberRepository;

  @Autowired
  private DonationBatchViewModelFactory donationBatchViewModelFactory;

  @Autowired
  private DonationSummaryViewModelFactory donationSummaryViewModelFactory;

  public TestBatchFullViewModel updateTestBatch(TestBatchBackingForm backingForm) {
    TestBatch testBatch = testBatchFactory.createEntity(backingForm);
    testBatch = testBatchCRUDService.updateTestBatch(testBatch);
    return testBatchFactory.createTestBatchFullViewModel(testBatch, true);
  }

  public List<LocationViewModel> getTestingSites() {
    List<Location> locations = locationRepository.getTestingSites();
    return locationFactory.createViewModels(locations);
  }

  public List<DonationBatchViewModel> getUnnasignedDonationBatches() {
    return donationBatchViewModelFactory.createDonationBatchBasicViewModels(donationBatchRepository.findUnassignedDonationBatches());
  }
  
  public TestBatchFullViewModel saveTestBatch(TestBatchBackingForm form) {
    TestBatch testBatch = testBatchRepository.saveTestBatch(form.getTestBatch(), sequenceNumberRepository.getNextTestBatchNumber());
    boolean isTestingSupervisor = PermissionUtils.loggedOnUserHasPermission(PermissionConstants.EDIT_TEST_BATCH);
    return testBatchFactory.createTestBatchFullViewModel(testBatch, isTestingSupervisor);
  }

  public TestBatchFullViewModel getTestBatchById(long id) {
    TestBatch testBatch = testBatchRepository.findTestBatchById(id);
    boolean isTestingSupervisor = PermissionUtils.loggedOnUserHasPermission(PermissionConstants.EDIT_TEST_BATCH);
    return testBatchFactory.createTestBatchFullViewModel(testBatch, isTestingSupervisor);
  }

  public List<TestBatchViewModel> findTestBatches(List<TestBatchStatus> statuses, Date startDate, Date endDate) {
    List<TestBatch> testBatches = testBatchRepository.findTestBatches(statuses, startDate, endDate);
    return testBatchFactory.createTestBatchBasicViewModels(testBatches);
  }

  public void deleteTestBatch(long id) {
    testBatchCRUDService.deleteTestBatch(id);
  }

  public List<DonationSummaryViewModel> getDonationsSummaries(long id, BloodTypingMatchStatus bloodTypingMatchStatus) {
    TestBatch testBatch = testBatchRepository.findTestBatchById(id);
    return donationSummaryViewModelFactory.createDonationSummaryViewModels(testBatch, bloodTypingMatchStatus);
  }

  public Date getTestBatchCreatedDate(long id) {
    TestBatch testBatch = testBatchRepository.findTestBatchById(id);
    return testBatch.getCreatedDate();
  }

}
