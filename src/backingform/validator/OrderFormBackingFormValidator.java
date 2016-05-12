package backingform.validator;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.OrderFormBackingForm;
import model.location.Location;
import repository.LocationRepository;

@Component
public class OrderFormBackingFormValidator extends BaseValidator<OrderFormBackingForm> {

  @Autowired
  private LocationRepository locationRepository;

  @Override
  public void validateForm(OrderFormBackingForm form, Errors errors) {
    // Validate dispatchedFrom
    if (form.getDispatchedFrom() == null || form.getDispatchedFrom().getId() == null) {
      errors.rejectValue("dispatchedFrom", "required", "dispatchedFrom is required");
    } else {
      try {
        Location dispatchedFrom = locationRepository.getLocation(form.getDispatchedFrom().getId());
        if (!dispatchedFrom.isDistributionSite()) {
          errors.rejectValue("dispatchedFrom", "invalid", "dispatchedFrom must be a distribution site");
        }
      } catch (NoResultException e) {
        errors.rejectValue("dispatchedFrom", "invalid", "Invalid dispatchedFrom");
      }
    }

    // Validate dispatchedTo
    if (form.getDispatchedTo() == null || form.getDispatchedTo().getId() == null) {
      errors.rejectValue("dispatchedTo", "required", "dispatchedTo is required");
    } else {
      try {
        Location dispatchedTo = locationRepository.getLocation(form.getDispatchedTo().getId());
        if (!dispatchedTo.isDistributionSite() && !dispatchedTo.getIsUsageSite()) {
          errors.rejectValue("dispatchedTo", "invalid", "dispatchedTo must be a distribution or usage site");
        }
      } catch (NoResultException e) {
        errors.rejectValue("dispatchedTo", "invalid", "Invalid dispatchedTo");
      }
    }
    commonFieldChecks(form, errors);
  }

  @Override
  public String getFormName() {
    return "orderForm";
  }
}
