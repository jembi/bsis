package org.jembi.bsis.backingform.validator;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class TestBatchBackingFormValidator extends BaseValidator<TestBatchBackingForm> {

  @Autowired
  private DonationBatchRepository donationBatchRepository;
  @Autowired
  private LocationRepository locationRepository;

  @Override
  public void validateForm(TestBatchBackingForm form, Errors errors) {
    TestBatch testBatch = form.getTestBatch();
    
    // Validate donation batches
    List<Long> donationBatchIds = form.getDonationBatchIds();
    List<DonationBatch> donationBatches = new ArrayList<DonationBatch>();
    if (donationBatchIds != null && !donationBatchIds.isEmpty()) {
      for (Long donationBatchId : donationBatchIds) {
        DonationBatch db = donationBatchRepository.findDonationBatchById(donationBatchId);
        if (db.getTestBatch() != null) {
          if (testBatch.getId() == null || !testBatch.getId().equals(db.getTestBatch().getId())) {
            errors.rejectValue("donationBatchIds", "errors.invalid", "Donation batch at " + db.getVenue().getName() + " from " + db.getCreatedDate() + " is already in a test batch.");
          }
        }
        donationBatches.add(db);
      }
    }
    testBatch.setDonationBatches(donationBatches);
    
    // Validate location
    if (form.getLocation() != null) {
      try {
        Location location = locationRepository.getLocation(form.getLocation().getId());
        if (!location.getIsTestingSite()) {
          errors.rejectValue("location", "errors.invalid", "Location \"" + location.getName() + "\" is not a testing site");
        }
        if (location.getIsDeleted()) {
          errors.rejectValue("location", "errors.deleted", "Location has been deleted");
        }
      } catch (NoResultException nre) {
        errors.rejectValue("location", "errors.notFound", "Location not found");
      }
    }

    commonFieldChecks(form, errors);
  }

  @Override
  public String getFormName() {
    return "testBatch";
  }
}