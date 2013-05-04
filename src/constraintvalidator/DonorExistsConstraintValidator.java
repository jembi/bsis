package constraintvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import model.donor.Donor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.DonorRepository;

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
       }
       else if (target.getDonorNumber() != null) {

         if (target.getDonorNumber().isEmpty())
           return true;

         donor = 
           donorRepository.findDonorByDonorNumber(target.getDonorNumber());
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