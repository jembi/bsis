package model.donortype;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import model.donortype.DonorType;

import org.springframework.beans.factory.annotation.Autowired;

import repository.DonorTypeRepository;

public class DonorTypeExistsConstraintValidator implements
    ConstraintValidator<DonorTypeExists, DonorType> {

  @Autowired
  private DonorTypeRepository donorTypeRepository;

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

  public void setDonorRepository(DonorTypeRepository donorTypeRepository) {
    this.donorTypeRepository = donorTypeRepository;
  }
}