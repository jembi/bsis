package org.jembi.bsis.backingform.validator;

import java.util.Iterator;

import org.jembi.bsis.backingform.BloodTransportBoxBackingForm;
import org.jembi.bsis.backingform.ComponentBatchBackingForm;
import org.jembi.bsis.backingform.DonationBatchBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ComponentBatchBackingFormValidator extends BaseValidator<ComponentBatchBackingForm> {
  
  @Autowired
  private DonationBatchRepository donationBatchRepository;
  
  @Autowired
  private LocationRepository locationRepository;
  
  @Autowired
  private BloodTransportBoxBackingFormValidator bloodTransportBoxBackingFormValidator;
  
  @Override
  public void validateForm(ComponentBatchBackingForm form, Errors errors) {
    DonationBatchBackingForm donationBatchBackingForm = form.getDonationBatch();
    // donation batch is specified
    if (donationBatchBackingForm == null || donationBatchBackingForm.getId() == null) {
      errors.rejectValue("componentBatch.donationBatch", "donationBatch.empty", "DonationBatch is required.");
    } else {
      // donation batch exists
      if (!donationBatchRepository.verifyDonationBatchExists(donationBatchBackingForm.getId())) {
        errors.rejectValue("componentBatch.donationBatch", "donationBatch.invalid", "DonationBatch is invalid.");
      }
    }

    // location is specified
    LocationBackingForm locationBackingForm = form.getLocation();
    if (locationBackingForm == null || locationBackingForm.getId() == null) {
      errors.rejectValue("componentBatch.location", "location.empty", "Location is required.");
    } else {
      // location exists and is a processing site
      Location location = locationRepository.getLocation(locationBackingForm.getId());
      if (location == null || location.getIsDeleted() || !location.getIsProcessingSite()) {
        errors.rejectValue("componentBatch.location", "location.invalid", "Location is invalid.");
      }
    }
    
    // blood transport boxes
    if (form.getBloodTransportBoxes() != null) {
      Iterator<BloodTransportBoxBackingForm> it = form.getBloodTransportBoxes().iterator();
      while (it.hasNext()) {
        bloodTransportBoxBackingFormValidator.validate(it.next(), errors);
      }
    }
    // common validations (field length, required)
    commonFieldChecks(form, errors);
  }

  @Override
  public String getFormName() {
    return "ComponentBatch";
  }
}
