package model.testresults;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import repository.BloodTestRepository;

@Component
public class TestExistsConstraintValidator implements
    ConstraintValidator<TestExists, BloodTest> {

  @Autowired
  private BloodTestRepository bloodTestRepository;

  public TestExistsConstraintValidator() {
  }

  @Override
  public void initialize(TestExists constraint) {
  }

  public boolean isValid(BloodTest target, ConstraintValidatorContext context) {

   if (target == null)
     return true;

   try {
    BloodTest bloodTest = bloodTestRepository.findBloodTestById(target.getId());
    if (bloodTest != null) {
     return true;
    }
   } catch (Exception e) {
    e.printStackTrace();
   }
   return false;
  }

  public void setBloodTestRepository(BloodTestRepository bloodTestRepository) {
    this.bloodTestRepository = bloodTestRepository;
  }
}