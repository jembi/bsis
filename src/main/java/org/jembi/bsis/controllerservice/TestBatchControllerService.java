package org.jembi.bsis.controllerservice;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.backingform.TestBatchDonationRangeBackingForm;
import org.jembi.bsis.backingform.TestBatchDonationsBackingForm;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.factory.TestBatchFactory;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.DonationAdditionResult;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.jembi.bsis.service.TestBatchCRUDService;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.TestBatchFullDonationViewModel;
import org.jembi.bsis.viewmodel.TestBatchFullViewModel;
import org.jembi.bsis.viewmodel.TestBatchViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
  private DonationRepository donationRepository;

  public TestBatchFullViewModel updateTestBatch(TestBatchBackingForm backingForm) {
    TestBatch testBatch = testBatchFactory.createEntity(backingForm);
    testBatch = testBatchCRUDService.updateTestBatch(testBatch);
    return testBatchFactory.createTestBatchFullViewModel(testBatch);
  }

  public List<LocationViewModel> getTestingSites() {
    List<Location> locations = locationRepository.getTestingSites();
    return locationFactory.createViewModels(locations);
  }

  public TestBatchFullViewModel addTestBatch(TestBatchBackingForm form) {
    TestBatch testBatch = testBatchFactory.createEntity(form);
    testBatch = testBatchCRUDService.createTestBatch(testBatch);
    return testBatchFactory.createTestBatchFullViewModel(testBatch);
  }

  public TestBatchFullViewModel getTestBatchById(UUID id) {
    TestBatch testBatch = testBatchRepository.findTestBatchById(id);
    return testBatchFactory.createTestBatchFullViewModel(testBatch);
  }

  public List<TestBatchViewModel> findTestBatches(List<TestBatchStatus> statuses, Date startDate, Date endDate, UUID locationId) {
    List<TestBatch> testBatches = testBatchRepository.findTestBatches(statuses, startDate, endDate, locationId);
    return testBatchFactory.createTestBatchBasicViewModels(testBatches);
  }

  public void deleteTestBatch(UUID id) {
    testBatchCRUDService.deleteTestBatch(id);
  }

  public TestBatchFullDonationViewModel getTestBatchByIdAndBloodTypingMatchStatus(UUID id, BloodTypingMatchStatus bloodTypingMatchStatus) {
    TestBatch testBatch = testBatchRepository.findTestBatchById(id);
    return testBatchFactory.createTestBatchFullDonationViewModel(testBatch, bloodTypingMatchStatus);
  }

  public TestBatchFullViewModel addDonationsToTestBatch(
      TestBatchDonationRangeBackingForm backingForm) {
    TestBatch testBatch = testBatchRepository.findTestBatchById(backingForm.getTestBatchId());
    List<Donation> donationsInRange = donationRepository
        .findDonationsBetweenTwoDins(backingForm.getFromDIN(), backingForm.getToDIN());
    DonationAdditionResult result = testBatchCRUDService
        .addDonationsToTestBatch(testBatch, donationsInRange);
    return testBatchFactory
        .createTestBatchFullViewModel(result.getTestBatch(), result.getDinsWithoutTestSamples(),
            result.getDinsInOtherTestBatches(), result.getDinsInOpenDonationBatch());
  }

  public TestBatchFullViewModel removeDonationsFromBatch(TestBatchDonationsBackingForm backingForm) {
    List<Donation> donationsToRemove = backingForm.getDonationIds().stream()
        .map(uuid -> donationRepository.findDonationById(uuid))
        .collect(Collectors.toList());
    TestBatch testBatch = testBatchRepository.findTestBatchById(backingForm.getTestBatchId());
    testBatch = testBatchCRUDService.removeDonationsFromTestBatch(donationsToRemove, testBatch);
    return testBatchFactory.createTestBatchFullViewModel(testBatch);
  }
}
