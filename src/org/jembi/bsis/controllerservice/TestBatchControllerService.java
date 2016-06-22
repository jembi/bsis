package org.jembi.bsis.controllerservice;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.factory.TestBatchViewModelFactory;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.service.TestBatchCRUDService;
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
  
  public TestBatchFullViewModel updateTestBatch(TestBatchBackingForm backingForm) {
    TestBatch testBatch = testBatchCRUDService.updateTestBatch(backingForm.getTestBatch().getId(),
        backingForm.getTestBatch().getStatus(), backingForm.getTestBatch().getCreatedDate(),
        backingForm.getDonationBatchIds());
    return testBatchViewModelFactory.createTestBatchFullViewModel(testBatch, true);
  }

}
