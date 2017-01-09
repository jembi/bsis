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
  
  private static final Integer MAX_LENGTH_NAME = 50;
  private static final Integer MAX_LENGTH_CODE = 30;
  private static final Integer MAX_MAX_BLEED_TIME = 60;
  private static final Integer MAX_MAX_TIME_SINCE_DONATION = 30;
  private static final Double MIN_GRAVITY = 0.001;
  private static final Double MAX_GRAVITY = 2.000;
  private static final Integer MAX_GRAVITY_DECIMAL_PLACES = 3;
  
  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  @Override
  public void validateForm(ComponentTypeBackingForm form, Errors errors) {
    
    if (StringUtils.isBlank(form.getComponentTypeName())) {
      errors.rejectValue("componentTypeName", "errors.required", "Component type name is required");
    } else if (form.getComponentTypeName().length() > MAX_LENGTH_NAME) {
      errors.rejectValue("componentTypeName", "fieldLength.error", "Maximum length for this field is " + MAX_LENGTH_NAME);
    } else {
      if (!componentTypeRepository.isUniqueComponentTypeName(form.getId(), form.getComponentTypeName())) {
        errors.rejectValue("componentTypeName", "errors.nonUnique", "Component type name already exists");
      }
    }
    
    if (StringUtils.isBlank(form.getComponentTypeCode())) {
      errors.rejectValue("componentTypeCode", "errors.required", "Component type code is required");
    } else if (form.getComponentTypeCode().length() > MAX_LENGTH_CODE) {
      errors.rejectValue("componentTypeCode", "fieldLength.error", "Maximum length for this field is " + MAX_LENGTH_CODE);
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

    if (form.getMaxBleedTime() != null) {
      if (form.getMaxBleedTime() < 0 || form.getMaxBleedTime() > MAX_MAX_BLEED_TIME) {
        errors.rejectValue("maxBleedTime", "errors.invalid", "maxBleedTime should be between 0 and "
            + MAX_MAX_BLEED_TIME + " minutes");
      }
    }

    if (form.getMaxTimeSinceDonation() != null) {
      if (form.getMaxTimeSinceDonation() < 0 || form.getMaxTimeSinceDonation() > MAX_MAX_TIME_SINCE_DONATION) {
        errors.rejectValue("maxTimeSinceDonation", "errors.invalid", "maxTimeSinceDonation should be between 0 and "
            + MAX_MAX_TIME_SINCE_DONATION + " hours");
      }
    }
    
    if (form.getGravity() != null) {
      if (form.getGravity() < MIN_GRAVITY || form.getGravity() > MAX_GRAVITY) {
        errors.rejectValue("gravity", "errors.invalid", "Gravity should be between "+MIN_GRAVITY+" and "+MAX_GRAVITY);
      }
      String [] numArray = String.valueOf(form.getGravity()).split("\\.");
      if (numArray.length > 1 && numArray[1].length() > MAX_GRAVITY_DECIMAL_PLACES) {
        errors.rejectValue("gravity", "errors.invalid", "Gravity should have a maximum of "+MAX_GRAVITY_DECIMAL_PLACES+" decimal places");
      }
    }
  }
  
    @Override
  public String getFormName() {
    return "ComponentType";
  }

}
