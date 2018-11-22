package org.jembi.bsis.backingform.validator;

import java.util.Date;
import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.DateGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class TestBatchBackingFormValidator extends BaseValidator<TestBatchBackingForm> {

  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private DateGeneratorService dateGeneratorService;

  @Override
  public void validateForm(TestBatchBackingForm form, Errors errors) {
    // Validate location
    if (form.getLocation() != null) {
      try {
        Location location = locationRepository.getLocation(form.getLocation().getId());
        if (!location.getIsTestingSite()) {
          errors.rejectValue("location", "errors.invalid", "Location \"" + location.getName() + "\" is not a testing site");
        }
        if (location.getIsDeleted()) {
          errors.rejectValue("location", "errors.deleted", "Location has been deleted");
        }
      } catch (NoResultException nre) {
        errors.rejectValue("location", "errors.notFound", "Location not found");
      }
    }
    
    // Validate testBatchDate
    Date today = dateGeneratorService.generateDate();
    if (form.getTestBatchDate() == null) {
      errors.rejectValue("testBatchDate", "errors.invalid", "Test batch date is invalid");
    } else if (form.getTestBatchDate().after(dateGeneratorService.generateDate())) {
      errors.rejectValue("testBatchDate", "errors.invalid",
          "Test batch date is after current date");
    } else if (form.getId() == null && !form.isBackEntry() && 
        !dateGeneratorService.generateLocalDate(today)
            .equals(dateGeneratorService.generateLocalDate(form.getTestBatchDate()))) {
      errors.rejectValue("testBatchDate", "errors.invalid",
          "Test batch date should be current date");
    }

    commonFieldChecks(form, errors);
  }

  @Override
  public String getFormName() {
    return "testBatch";
  }

  @Override
  protected boolean formHasBaseEntity() {
    return false;
  }
}
