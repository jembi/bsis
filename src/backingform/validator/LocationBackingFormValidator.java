package backingform.validator;

import model.location.Location;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.LocationRepository;
import backingform.LocationBackingForm;

@Component
public class LocationBackingFormValidator  extends BaseValidator<LocationBackingForm> {
  
  @Autowired
  private LocationRepository locationRepository;

    @Override
    public void validateForm(LocationBackingForm form, Errors errors) {
        if (isDuplicateLocationName(form.getLocation()))
            errors.rejectValue("name", "400", "Location name already exists.");
    }
    
  @Override
  public String getFormName() {
    return "location";
  }
    
  private boolean isDuplicateLocationName(Location location) {
    String locationName = location.getName();
    if (StringUtils.isBlank(locationName)) {
      return false;
    }

    Location existingLocation = locationRepository.findLocationByName(locationName);
    if (existingLocation != null && !existingLocation.getId().equals(location.getId())) {
      return true;
    }

    return false;
  }
}
