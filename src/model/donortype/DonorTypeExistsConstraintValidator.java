package model.donortype;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.DonorTypeRepository;

@Component
public class DonorTypeExistsConstraintValidator implements
    ConstraintValidator<DonorTypeExists, DonorType> {

  @Autowired
  private DonorTypeRepository donorTypeRepository;

  public DonorTypeExistsConstraintValidator() {
  }
  
  @Override
  public void initialize(DonorTypeExists constraint) {
  }

  public boolean isValid(DonorType target, ConstraintValidatorContext context) {

   System.out.println("here");
   System.out.println("here1");
   System.out.println("donorType: " + target);
   if (target == null)
     return true;

   try {
    if (donorTypeRepository.getDonorType(target.getDonorType()) != null)
     return true;
   } catch (Exception e) {
    e.printStackTrace();
   }
   return false;
  }

  public void getDonorTypeRepository(DonorTypeRepository donorTypeRepository) {
    this.donorTypeRepository = donorTypeRepository;
  }

  public void setDonorTypeRepository(DonorTypeRepository donorTypeRepository) {
    this.donorTypeRepository = donorTypeRepository;
  }
}