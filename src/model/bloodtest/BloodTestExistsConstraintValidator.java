package model.bloodtest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.BloodTestRepository;

@Component
public class BloodTestExistsConstraintValidator implements
    ConstraintValidator<BloodTestExists, BloodTest> {

  @Autowired
  private BloodTestRepository bloodTestRepository;

  public BloodTestExistsConstraintValidator() {
  }
  
  @Override
  public void initialize(BloodTestExists constraint) {
  }

  public boolean isValid(BloodTest target, ConstraintValidatorContext context) {

   if (target == null)
     return true;

   if (bloodTestRepository.isBloodTestValid(target.getName()))
     return true;

   return false;
  }

  public void setDonorRepository(BloodTestRepository bloodTestRepository) {
    this.bloodTestRepository = bloodTestRepository;
  }
}