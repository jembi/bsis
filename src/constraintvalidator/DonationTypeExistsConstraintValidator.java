package constraintvalidator;

import model.donationtype.DonationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.DonationTypeRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
class DonationTypeExistsConstraintValidator implements
    ConstraintValidator<DonationTypeExists, DonationType> {

  @Autowired
  private DonationTypeRepository donorTypeRepository;

  public DonationTypeExistsConstraintValidator() {
  }

  @Override
  public void initialize(DonationTypeExists constraint) {
  }

  public boolean isValid(DonationType target, ConstraintValidatorContext context) {

    if (target == null)
      return true;

    try {
      if (donorTypeRepository.getDonationTypeById(target.getId()) != null)
        return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public void getDonorTypeRepository(DonationTypeRepository donorTypeRepository) {
    this.donorTypeRepository = donorTypeRepository;
  }

  public void setDonorTypeRepository(DonationTypeRepository donorTypeRepository) {
    this.donorTypeRepository = donorTypeRepository;
  }
}