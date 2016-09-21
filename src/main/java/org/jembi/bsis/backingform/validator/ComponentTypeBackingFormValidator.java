package org.jembi.bsis.backingform.validator;

import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ComponentTypeBackingFormValidator extends BaseValidator<ComponentTypeBackingForm> {
  
  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  @Override
  public void validateForm(ComponentTypeBackingForm form, Errors errors) {
    
    if (StringUtils.isBlank(form.getComponentTypeName())) {
      errors.rejectValue("componentTypeName", "errors.required", "Component type name is required");
    } else {
      if (!componentTypeRepository.isUniqueComponentTypeName(form.getId(), form.getComponentTypeName())) {
        errors.rejectValue("componentTypeName", "errors.nonUnique", "Component type name already exists");
      }
    }
    
    if (StringUtils.isBlank(form.getComponentTypeCode())) {
      errors.rejectValue("componentTypeCode", "errors.required", "Component type code is required");
    } else {
      try {
        ComponentType componentType = componentTypeRepository.findComponentTypeByCode(form.getComponentTypeCode());
        if (!componentType.getId().equals(form.getId())) {
          errors.rejectValue("componentTypeCode", "errors.nonUnique", "Component type code already exists");
        }
      } catch (NoResultException nre) {
        // No component exists with the same code
      }
    }

    if (form.getExpiresAfter() == null) {
      errors.rejectValue("expiresAfter", "errors.required", "Expires after is required");
    } else if (form.getExpiresAfter() <= 0) {
      errors.rejectValue("expiresAfter", "errors.nonPositive", "Expires after must be greater than zero");
    }
  }

  @Override
  public String getFormName() {
    return "ComponentType";
  }

}
