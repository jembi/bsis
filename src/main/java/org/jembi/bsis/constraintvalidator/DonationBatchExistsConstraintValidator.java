package org.jembi.bsis.constraintvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DonationBatchExistsConstraintValidator implements
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