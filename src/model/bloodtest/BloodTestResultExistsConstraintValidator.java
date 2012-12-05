package model.bloodtest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.BloodTestResultRepository;

@Component
public class BloodTestResultExistsConstraintValidator implements
    ConstraintValidator<BloodTestResultExists, BloodTestResult> {

  @Autowired
  private BloodTestResultRepository bloodTestResultRepository;

  public BloodTestResultExistsConstraintValidator() {
  }
  
  @Override
  public void initialize(BloodTestResultExists constraint) {
  }

  public boolean isValid(BloodTestResult target, ConstraintValidatorContext context) {

    // TODO: need a better check to see if blood test result is compatible with blood test
   if (target == null)
     return true;

   if (bloodTestResultRepository.isBloodTestResultValid(target.getId()))
     return true;
   return false;
  }

  public void setDonorRepository(BloodTestResultRepository bloodTestResultRepository) {
    this.bloodTestResultRepository = bloodTestResultRepository;
  }
}