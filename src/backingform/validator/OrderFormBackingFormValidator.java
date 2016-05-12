package backingform.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.OrderFormBackingForm;
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
    } else if (isLocationInvalid(form.getDispatchedFrom().getId())) {
      errors.rejectValue("dispatchedFrom", "invalid", "Invalid dispatchedFrom");
    }
    // Validate dispatchedTo
    if (form.getDispatchedTo() == null || form.getDispatchedTo().getId() == null) {
      errors.rejectValue("dispatchedTo", "required", "dispatchedTo is required");
    } else if (isLocationInvalid(form.getDispatchedTo().getId())) {
      errors.rejectValue("dispatchedTo", "invalid", "Invalid dispatchedTo");
    }
    // Validate orderDate
    if (form.getOrderDate() == null) {
      errors.rejectValue("orderDate", "required", "orderDate is required");
    }
  }

  private boolean isLocationInvalid(Long id) {
    return !locationRepository.verifyLocationExists(id);
  }

  @Override
  public String getFormName() {
    return "orderForm";
  }
}
