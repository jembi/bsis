package backingform.validator;

import javax.persistence.NoResultException;

import model.location.Location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.ComponentRepository;
import repository.LocationRepository;
import backingform.ReturnFormBackingForm;

@Component
public class ReturnFormBackingFormValidator extends BaseValidator<ReturnFormBackingForm> {

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private ComponentRepository componentRepository;

  @Override
  public void validateForm(ReturnFormBackingForm form, Errors errors) {
    // Validate returnedFrom
    if (form.getReturnedFrom() == null || form.getReturnedFrom().getId() == null) {
      errors.rejectValue("returnedFrom", "required", "returnedFrom is required");
    } else {
      try {
        Location returnedFrom = locationRepository.getLocation(form.getReturnedFrom().getId());
        if (!returnedFrom.getIsUsageSite()) {
          errors.rejectValue("returnedFrom", "invalid", "returnedFrom must be a usage site");
        }
      } catch (NoResultException e) {
        errors.rejectValue("returnedFrom", "invalid", "Invalid returnedFrom");
      }
    }

    // Validate returnedTo
    if (form.getReturnedTo() == null || form.getReturnedTo().getId() == null) {
      errors.rejectValue("returnedTo", "required", "returnedTo is required");
    } else {
      try {
        Location dispatchedTo = locationRepository.getLocation(form.getReturnedTo().getId());
        if (!dispatchedTo.getIsDistributionSite()) {
          errors.rejectValue("returnedTo", "invalid", "returnedTo must be a distribution site");
        }
      } catch (NoResultException e) {
        errors.rejectValue("returnedTo", "invalid", "Invalid returnedTo");
      }
    }
    commonFieldChecks(form, errors);
  }

  @Override
  public String getFormName() {
    return "ReturnForm";
  }

  @Override
  public boolean formHasBaseEntity() {
    return false;
  }
}
