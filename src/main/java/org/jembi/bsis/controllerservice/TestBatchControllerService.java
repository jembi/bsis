package org.jembi.bsis.controllerservice;

import java.util.List;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.factory.LocationViewModelFactory;
import org.jembi.bsis.factory.TestBatchViewModelFactory;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.TestBatchCRUDService;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.TestBatchFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestBatchControllerService {
  
  @Autowired
  private TestBatchCRUDService testBatchCRUDService;
  @Autowired
  private TestBatchViewModelFactory testBatchViewModelFactory;
  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private LocationViewModelFactory locationViewModelFactory;
  
  public TestBatchFullViewModel updateTestBatch(TestBatchBackingForm backingForm) {
    TestBatch testBatch = testBatchCRUDService.updateTestBatch(backingForm.getTestBatch().getId(),
        backingForm.getTestBatch().getStatus(), backingForm.getTestBatch().getCreatedDate(),
        backingForm.getDonationBatchIds());
    return testBatchViewModelFactory.createTestBatchFullViewModel(testBatch, true);
  }

  public List<LocationViewModel> getTestingSites() {
    List<Location> locations = locationRepository.getTestingSites();
    return locationViewModelFactory.createLocationViewModels(locations);
  }

}
