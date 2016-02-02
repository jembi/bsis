package constraintvalidator;

import model.donationbatch.DonationBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.DonationBatchRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
class DonationBatchExistsConstraintValidator implements
    ConstraintValidator<DonationBatchExists, DonationBatch> {

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  public DonationBatchExistsConstraintValidator() {
  }

  @Override
  public void initialize(DonationBatchExists constraint) {
  }

  public boolean isValid(DonationBatch target, ConstraintValidatorContext context) {

    if (target == null)
      return true;

    try {

      DonationBatch donationBatch = null;
      if (target.getId() != null) {
        donationBatch = donationBatchRepository.findDonationBatchById(target.getId());
      } else if (target.getBatchNumber() != null) {

        if (target.getBatchNumber().isEmpty())
          return true;

        donationBatch =
            donationBatchRepository.findDonationBatchByBatchNumber(target.getBatchNumber());
      }
      if (donationBatch != null) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public void setDonorRepository(DonationBatchRepository donationBatchRepository) {
    this.donationBatchRepository = donationBatchRepository;
  }
}