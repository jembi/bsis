package constraintvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import model.donation.Donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.DonationRepository;

@Component
public class DonationExistsConstraintValidator implements
    ConstraintValidator<DonationExists, Donation> {

  @Autowired
  private DonationRepository donationRepository;

  public DonationExistsConstraintValidator() {
  }
  
  @Override
  public void initialize(DonationExists constraint) {
  }

  public boolean isValid(Donation target, ConstraintValidatorContext context) {

   if (target == null)
     return true;

   try {

      Donation donation = null;
      if (target.getId() != null) {
        donation = donationRepository.findDonationById(target.getId());
      }
      else if (target.getDonationIdentificationNumber() != null) {

        if (target.getDonationIdentificationNumber().isEmpty())
          return true;

        donation = 
          donationRepository.findDonationByDonationIdentificationNumber(target.getDonationIdentificationNumber());
      }
      if (donation != null) {
        return true;
      }
    } catch (Exception e) {
       e.printStackTrace();
    }
    return false;
  }

  public void setDonorRepository(DonationRepository donationRepository) {
    this.donationRepository = donationRepository;
  }
}