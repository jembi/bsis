package controllerservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backingform.TestBatchBackingForm;
import factory.TestBatchViewModelFactory;
import model.testbatch.TestBatch;
import service.TestBatchCRUDService;
import viewmodel.TestBatchFullViewModel;

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
