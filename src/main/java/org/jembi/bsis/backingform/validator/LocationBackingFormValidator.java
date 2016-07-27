package org.jembi.bsis.backingform.validator;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

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
    if (!form.getIsUsageSite() && !form.getIsVenue() && !form.getIsProcessingSite() 
        && !form.getIsDistributionSite() && !form.getIsTestingSite()) {
      errors.reject("400",
          "Location must be a venue, or a processing, distribution, testing or usage site.");
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
