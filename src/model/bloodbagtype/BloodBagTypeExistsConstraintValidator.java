package model.bloodbagtype;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.BloodBagTypeRepository;

@Component
public class BloodBagTypeExistsConstraintValidator implements
    ConstraintValidator<BloodBagTypeExists, BloodBagType> {

  @Autowired
  private BloodBagTypeRepository bloodBagTypeRepository;

  public BloodBagTypeExistsConstraintValidator() {
  }
  
  @Override
  public void initialize(BloodBagTypeExists constraint) {
  }

  public boolean isValid(BloodBagType target, ConstraintValidatorContext context) {

   if (target == null)
     return true;

   try {
    if (bloodBagTypeRepository.isBloodBagTypeValid(target.getBloodBagType()))
      return true;
   } catch (Exception e) {
    e.printStackTrace();
   }
   return false;
  }

//  public void setBloodBagTypeRepository(BloodBagTypeRepository bloodBagTypeRepository) {
//    this.bloodBagTypeRepository = bloodBagTypeRepository;
//  }
}