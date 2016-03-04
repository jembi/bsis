package backingform.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.LocationBackingForm;
import model.location.Location;
import repository.LocationRepository;

@Component
public class LocationBackingFormValidator extends BaseValidator<LocationBackingForm> {

  @Autowired
  private LocationRepository locationRepository;

  @Override
  public void validateForm(LocationBackingForm form, Errors errors) {
    if (StringUtils.isEmpty(form.getName())) {
      errors.rejectValue("name", "400", "Location name cannot be empty.");
    } else if (isDuplicateLocationName(form.getLocation())) {
      errors.rejectValue("name", "400", "Location name already exists.");
    }
    if (!form.getIsUsageSite() && !form.getIsVenue() && !form.getIsMobilesite()) {
      errors.reject("400",
          "Location type must be usage site, venue or mobile site, but all of them are false for this location.");
    }

  }

  @Override
  public String getFormName() {
    return "location";
  }

  private boolean isDuplicateLocationName(Location location) {
    Location existingLocation = locationRepository.findLocationByName(location.getName());
    if (existingLocation != null && !existingLocation.getId().equals(location.getId())) {
      return true;
    }
    return false;
  }
}
