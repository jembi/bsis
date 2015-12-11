package constraintvalidator;

import model.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.LocationRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
class LocationExistsConstraintValidator implements
        ConstraintValidator<LocationExists, Location> {

  @Autowired
  private LocationRepository locationRepository;

  public LocationExistsConstraintValidator() {
  }

  @Override
  public void initialize(LocationExists constraint) {
  }

  public boolean isValid(Location target, ConstraintValidatorContext context) {

    System.out.println("here1: " + target);

    if (target == null)
      return true;

    try {
      if (locationRepository.getLocation(target.getId()) != null)
        return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}