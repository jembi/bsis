package constraintvalidator;

import model.packtype.PackType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.PackTypeRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
class PackTypeExistsConstraintValidator implements
    ConstraintValidator<PackTypeExists, PackType> {

  @Autowired
  private PackTypeRepository packTypeRepository;

  public PackTypeExistsConstraintValidator() {
  }

  @Override
  public void initialize(PackTypeExists constraint) {
  }

  public boolean isValid(PackType target, ConstraintValidatorContext context) {

    if (target == null)
      return true;

    try {
      if (packTypeRepository.getPackTypeById(target.getId()) != null)
        return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}