package backingform.validator;

import backingform.DonationBatchBackingForm;
import controller.UtilController;
import model.donationbatch.DonationBatch;
import model.location.Location;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import viewmodel.DonationBatchViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class DonationBatchBackingFormValidator implements Validator {

  private Validator validator;

  private UtilController utilController;

  public DonationBatchBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(DonationBatchBackingForm.class,
        DonationBatch.class,
        DonationBatchViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    DonationBatchBackingForm form = (DonationBatchBackingForm) obj;
    updateAutoGeneratedFields(form);

    if (form.getId() != null && utilController.isDuplicateDonationBatchNumber(form.getDonationBatch()))
      errors.rejectValue("donationBatch.batchNumber", "batchNumber.nonunique",
          "There exists a donation batch with the same batch number.");

    Location venue = form.getDonationBatch().getVenue();
    ArrayList<Long> venueIds = new ArrayList<>();
    venueIds.add(venue.getId());
    if (venue.getId() == null) {
      errors.rejectValue("donationBatch.venue", "venue.empty",
          "Venue is required.");
    } else if (!utilController.isVenue(venue.getId())) {
      errors.rejectValue("donationBatch.venue", "venue.invalid",
          "Location is not a Venue.");
    } else if (form.getId() == null && utilController.findOpenDonationBatches(venueIds).size() > 0) {
      errors.rejectValue("donationBatch.venue", "venue.openBatch",
          "There is already an open donation batch for that venue.");
    }

    utilController.commonFieldChecks(form, "donationBatch", errors);
  }

  private void updateAutoGeneratedFields(DonationBatchBackingForm form) {
    if (StringUtils.isBlank(form.getBatchNumber()) &&
        utilController.isFieldAutoGenerated("donationBatch", "batchNumber")) {
      form.setBatchNumber(utilController.getNextBatchNumber());
    }
  }

}
