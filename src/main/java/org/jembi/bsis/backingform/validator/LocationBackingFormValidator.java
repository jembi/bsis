package org.jembi.bsis.backingform.validator;

import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.DivisionBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.DivisionRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class LocationBackingFormValidator extends BaseValidator<LocationBackingForm> {

  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private DivisionRepository divisionRepository;

  @Override
  public void validateForm(LocationBackingForm form, Errors errors) {
    if (StringUtils.isEmpty(form.getName())) {
      errors.rejectValue("name", "400", "Location name cannot be empty.");
    } else if (isDuplicateLocationName(form)) {
      errors.rejectValue("name", "400", "Location name already exists.");
    }
    if (!form.isUsageSite() && !form.isVenue() && !form.isProcessingSite()
        && !form.isDistributionSite() && !form.isTestingSite() && !form.isReferralSite()) {
      errors.reject("400",
          "Location must be a venue, or a processing, distribution,testing, referral or usage site.");
    }

    validateDivisionLevel3(form.getDivisionLevel3(), errors);
  }

  @Override
  public String getFormName() {
    return "location";
  }
  
  private void validateDivisionLevel3(DivisionBackingForm divisionBackingForm, Errors errors) {
    if (divisionBackingForm == null || divisionBackingForm.getId() == null) {
      errors.rejectValue("divisionLevel3", "required", "Division level 3 is required.");
    } else {
      try {
        Division division = divisionRepository.findDivisionById(divisionBackingForm.getId());
        if (division.getLevel() != 3) {
          errors.rejectValue("divisionLevel3", "invalid", "Division level 3 is is not the correct level.");
        }
      } catch (NoResultException nre) {
        errors.rejectValue("divisionLevel3", "invalid", "Division level 3 is does not exist.");
      }
    }
  }

  private boolean isDuplicateLocationName(LocationBackingForm location) {
    Location existingLocation = locationRepository.findLocationByName(location.getName());
    return existingLocation != null && !existingLocation.getId().equals(location.getId());
  }
}
