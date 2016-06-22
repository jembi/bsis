package org.jembi.bsis.backingform.validator;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class TestBatchBackingFormValidator extends BaseValidator<TestBatchBackingForm> {

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  @Override
  public void validateForm(TestBatchBackingForm form, Errors errors) {
    TestBatch testBatch = form.getTestBatch();
    List<Long> donationBatchIds = form.getDonationBatchIds();
    List<DonationBatch> donationBatches = new ArrayList<DonationBatch>();
    if (donationBatchIds != null && !donationBatchIds.isEmpty()) {
      for (Long donationBatchId : donationBatchIds) {
        DonationBatch db = donationBatchRepository.findDonationBatchById(donationBatchId);
        if (db.getTestBatch() != null) {
          if (testBatch.getId() == null || !testBatch.getId().equals(db.getTestBatch().getId())) {
            errors.rejectValue("donationBatchIds", "", "Donation batch at " + db.getVenue().getName() + " from " + db.getCreatedDate() + " is already in a test batch.");
          }
        }
        donationBatches.add(db);
      }
    }
    testBatch.setDonationBatches(donationBatches);
  }

  @Override
  public String getFormName() {
    return "testBatch";
  }
}