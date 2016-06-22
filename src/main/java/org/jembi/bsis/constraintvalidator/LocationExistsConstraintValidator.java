package org.jembi.bsis.constraintvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationExistsConstraintValidator implements
    ConstraintValidator<LocationExists, Location> {

  @Autowired
  private LocationRepository locationRepository;

  public LocationExistsConstraintValidator() {
  }

  @Override
  public void initialize(LocationExists constraint) {
  }

  public boolean isValid(Location target, ConstraintValidatorContext context) {
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