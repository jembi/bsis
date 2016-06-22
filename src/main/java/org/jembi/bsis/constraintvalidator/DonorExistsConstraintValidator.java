package org.jembi.bsis.constraintvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DonorExistsConstraintValidator implements
    ConstraintValidator<DonorExists, Donor> {

  @Autowired
  private DonorRepository donorRepository;

  public DonorExistsConstraintValidator() {
  }

  @Override
  public void initialize(DonorExists constraint) {
  }

  public boolean isValid(Donor target, ConstraintValidatorContext context) {

    if (target == null)
      return true;

    try {

      Donor donor = null;
      if (target.getId() != null) {
        donor = donorRepository.findDonorById(target.getId());
      } else if (target.getDonorNumber() != null) {

        if (target.getDonorNumber().isEmpty())
          return true;

        donor =
            donorRepository.findDonorByDonorNumber(target.getDonorNumber(), false);
      }
      if (donor != null) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public void setDonorRepository(DonorRepository donorRepository) {
    this.donorRepository = donorRepository;
  }
}