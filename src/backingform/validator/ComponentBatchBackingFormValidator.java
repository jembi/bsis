package backingform.validator;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.DonationBatchRepository;
import backingform.BloodTransportBoxBackingForm;
import backingform.ComponentBatchBackingForm;
import backingform.DonationBatchBackingForm;

@Component
public class ComponentBatchBackingFormValidator extends BaseValidator<ComponentBatchBackingForm> {
  
  @Autowired
  private DonationBatchRepository donationBatchRepository;
  
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
