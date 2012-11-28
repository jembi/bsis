package model.donortype;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import model.donortype.DonorType;

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

   System.out.println("donorType: " + target);
   if (target == null)
     return true;

   try {
    if (donorTypeRepository.isDonorTypeValid(target.getDonorType()))
     return true;
   } catch (Exception e) {
    e.printStackTrace();
   }
   return false;
  }

  public void setDonorTypeRepository(DonorTypeRepository donorTypeRepository) {
    this.donorTypeRepository = donorTypeRepository;
  }
}