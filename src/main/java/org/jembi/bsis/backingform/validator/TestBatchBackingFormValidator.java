package org.jembi.bsis.backingform.validator;

import java.util.Date;
import java.util.List;   
import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.DateGeneratorService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class TestBatchBackingFormValidator extends BaseValidator<TestBatchBackingForm> {
  
  @Autowired
  private DonationBatchRepository donationBatchRepository;
  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private DateGeneratorService dateGeneratorService;

  @Override
  public void validateForm(TestBatchBackingForm form, Errors errors) {
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
    
    // Validate donation batches
    List<UUID> donationBatchIds = form.getDonationBatchIds();
    if (donationBatchIds != null && !donationBatchIds.isEmpty()) {
      for (UUID donationBatchId : donationBatchIds) {
        DonationBatch db = donationBatchRepository.findDonationBatchById(donationBatchId);
        if (db.getTestBatch() != null) {
          if (form.getId() == null || !form.getId().equals(db.getTestBatch().getId())) {
            errors.rejectValue("donationBatchIds", "errors.invalid", "Donation batch at " + db.getVenue().getName()
                + " from " + db.getDonationBatchDate() + " is already in a test batch.");
          }
        }
      }
    }
    
    // Validate testBatchDate
    if (form.getTestBatchDate() == null) {
      errors.rejectValue("testBatchDate", "errors.invalid", "Test batch date is invalid");
    } else if (dateGeneratorService.generateDate(form.getTestBatchDate()).after(new Date())) {
      errors.rejectValue("testBatchDate", "errors.invalid", "Test batch date is after current date");
    }

    commonFieldChecks(form, errors);
  }

  @Override
  public String getFormName() {
    return "testBatch";
  }

  @Override
  protected boolean formHasBaseEntity() {
    return false;
  }
}
