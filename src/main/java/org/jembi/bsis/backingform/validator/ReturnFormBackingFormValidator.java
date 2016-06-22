package org.jembi.bsis.backingform.validator;

import java.util.List;

import javax.persistence.NoResultException;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.ReturnFormBackingForm;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

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

    // Validate components
    if (form.getComponents() != null) { // it can be null if the Return form has just been created
      List<ComponentBackingForm> components = form.getComponents();
      for (int i = 0, len = components.size(); i < len; i++) {
        errors.pushNestedPath("components[" + i + "]");
        try {
          validateComponentForm(components.get(i), errors);
        } finally {
          errors.popNestedPath();
        }
      }
    }

    commonFieldChecks(form, errors);

  }
  
  private void validateComponentForm(ComponentBackingForm componentBackingForm, Errors errors) {
    if (componentBackingForm.getId() == null) {
      errors.rejectValue("id", "required", "component id is required.");
    } else {
      org.jembi.bsis.model.component.Component component = componentRepository.findComponent(componentBackingForm.getId());
      if (component == null) {
        errors.rejectValue("id", "invalid", "component id is invalid.");
      } else {
        if (!component.getStatus().equals(ComponentStatus.ISSUED)) {
          errors.rejectValue("status", "invalid status", "component status must be ISSUED");
        }
      }
    }
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
