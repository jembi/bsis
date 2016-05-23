package backingform.validator;

import java.util.List;

import javax.persistence.NoResultException;

import model.location.Location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.LocationRepository;
import backingform.OrderFormBackingForm;
import backingform.OrderFormItemBackingForm;

@Component
public class OrderFormBackingFormValidator extends BaseValidator<OrderFormBackingForm> {

  @Autowired
  private LocationRepository locationRepository;
  
  @Autowired
  private OrderFormItemBackingFormValidator orderFormItemBackingFormValidator;

  @Override
  public void validateForm(OrderFormBackingForm form, Errors errors) {
    // Validate dispatchedFrom
    if (form.getDispatchedFrom() == null || form.getDispatchedFrom().getId() == null) {
      errors.rejectValue("dispatchedFrom", "required", "dispatchedFrom is required");
    } else {
      try {
        Location dispatchedFrom = locationRepository.getLocation(form.getDispatchedFrom().getId());
        if (!dispatchedFrom.getIsDistributionSite()) {
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
        if (!dispatchedTo.getIsDistributionSite() && !dispatchedTo.getIsUsageSite()) {
          errors.rejectValue("dispatchedTo", "invalid", "dispatchedTo must be a distribution or usage site");
        }
      } catch (NoResultException e) {
        errors.rejectValue("dispatchedTo", "invalid", "Invalid dispatchedTo");
      }
    }
    
    // Validate OrderFormItems
    if (form.getItems() != null) { // it can be null if the Order has just been created
      List<OrderFormItemBackingForm> items = form.getItems();
      for (int i=0, len=items.size(); i<len; i++) {
        OrderFormItemBackingForm item = items.get(i); 
        errors.pushNestedPath("items["+i+"]");
        try {
          orderFormItemBackingFormValidator.validate(item, errors);
        } finally {
          errors.popNestedPath();
        }
      }
    }
    
    commonFieldChecks(form, errors);
  }

  @Override
  public String getFormName() {
    return "OrderForm";
  }

  @Override
  public boolean formHasBaseEntity() {
    return false;
  }
}
